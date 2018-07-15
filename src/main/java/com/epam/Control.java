package com.epam;

import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;

import static java.util.stream.Collectors.joining;

public class Control {
    private int instructionIdx = 0;
    private Memory memory;
    private InterpreterView view;
    private String code;
    private boolean TRACING;

    public Control(boolean trace, String filename, Memory memory, InterpreterView view)
            throws IOException, URISyntaxException {
        this.TRACING = trace;
        this.memory = memory;
        this.view = view;
        getCode(filename);
    }

    private void getCode(String filename) throws IOException, URISyntaxException {
        if(getClass().getResource("./../../" + filename) == null){
            throw new FileNotFoundException("File not found");
        }
        code = Files.lines(Paths.get(getClass().getResource("./../../" + filename).toURI()))
                .collect(joining(System.lineSeparator())) + System.lineSeparator();
    }

    public void interpret() throws IOException {
        String commands = "<>+-.,[]";
        while(instructionIdx != code.length()) {
            switch (code.charAt(instructionIdx)) {
                case '<':
                    memory.decMemIdx();
                    break;
                case '>':
                    memory.incMemIdx();
                    break;
                case '+':
                    memory.incDataAtCurrentCell();
                    break;
                case '-':
                    memory.decDataAtCurrentCell();
                    break;
                case ',':
                    memory.setDataAtCurrentCell(view.requestInput());
                    break;
                case '.':
                    view.printData(memory.getDataAtCurrentCell());
                    break;
                case '[':
                    if(memory.getDataAtCurrentCell() == 0){
                        int brackets = 1;
                        while(code.charAt(instructionIdx)!=']' || brackets != 0){
                            instructionIdx++;
                            if(code.charAt(instructionIdx)=='['){
                                brackets++;
                            } else if(code.charAt(instructionIdx)==']'){
                                brackets--;
                            }
                        }
                    }
                    break;
                case ']':
                    if(memory.getDataAtCurrentCell() != 0){
                        int brackets = 1;
                        while ((code.charAt(instructionIdx) != '[' || brackets != 0)){
                            instructionIdx--;
                            if(code.charAt(instructionIdx) == '['){
                                brackets--;
                            } else if(code.charAt(instructionIdx) == ']'){
                                brackets++;
                            }
                        }

                    }
                    break;
                case '/':
                    while (System.lineSeparator().contains(Character.toString(code.charAt(instructionIdx)))) {
                        instructionIdx++;
                    }
                    break;
            }

            if(TRACING){
                if (commands.contains(Character.toString(code.charAt(instructionIdx)))) {
                    view.printMem(memory.getMem(), memory.getMemIdx(), code.charAt(instructionIdx));
                }
            }
            instructionIdx++;
        }
    }
    public static Control getCustomControl(String[] args)
            throws ParseException, IOException, URISyntaxException {

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("f","file",true,"filename to interpret");
        options.addOption("s","size",true,"set memory size");
        options.addOption("h","help",false,"view help");
        options.addOption("g","gui",false,"launch GUI");
        options.addOption("t","trace",false,"enable tracing");

        CommandLine line = parser.parse(options,args);
        if(line.hasOption("h")){
            HelpFormatter formatter = new HelpFormatter();
            String header = "Interpret brainfuck code";
            String footer = "Not a complete list*";
            formatter.printHelp("control -f <FILE> [-s <MemorySize>] [-h] [-t] [-g] ",header,options,footer);
            return null;
        }
        if(!line.hasOption("f")){
            throw new InvalidParameterException("Missing option -f ");
        }

        int size = line.hasOption("s")? Integer.parseInt(line.getOptionValue("s")):30000;
        boolean trace = line.hasOption("t");
        String filename = line.getOptionValue("f");
        
        return new Control(trace,filename,new Memory(size),new TerminalView());
    }


    public static void main(String[] args) {
        try {
            Control control = getCustomControl(args);
            if(control != null) {
                control.interpret();
            }
        } catch (ParseException | IOException | URISyntaxException | InvalidParameterException e) {
            System.err.println(e.getMessage());
        }
    }

}
