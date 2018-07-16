package com.epam;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.UnexpectedException;

import static java.util.stream.Collectors.joining;

public class Control {
    private int instructionIdx = 0;
    private final Memory memory;
    private final InterpreterView view;
    private String code;
    private boolean TRACING;

    public Control(boolean trace, String filename, Memory memory, InterpreterView view) {
        this.TRACING = trace;
        this.memory = memory;
        this.view = view;
        setCode(filename);
    }

    public void setCode(String filename) {
        try {
            code = Files.lines(Paths.get(getClass().getResource("./../../" + filename).toURI()))
                    .collect(joining(System.lineSeparator())) + System.lineSeparator();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void interpret() throws IOException {
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
                     int idx = getPair(code);
                    if(memory.getDataAtCurrentCell() == 0) {
                        instructionIdx = idx;
                    }
                    break;
                case ']':
                    idx = getPair(code);
                    if(memory.getDataAtCurrentCell() != 0){
                        instructionIdx = idx;
                    }
                    break;
                case '/':
                    if(code.charAt(instructionIdx+1) =='/') {
                        while (!System.lineSeparator().contains(Character.toString(code.charAt(instructionIdx)))) {
                            instructionIdx++;
                        }
                    }
                    break;
            }

            if(TRACING){
                String commands = "<>+-.,[]";
                if (commands.contains(Character.toString(code.charAt(instructionIdx)))) {
                    view.printMem(memory.getMem(), memory.getMemIdx(), code.charAt(instructionIdx));
                }
            }
            instructionIdx++;
        }
    }

    private int getPair(String code) throws UnexpectedException {
        int tIdx = instructionIdx;
        boolean backwards = code.charAt(tIdx) == ']';
        int brackets = 1;
        if(!backwards) {
            while (code.charAt(tIdx) != ']' || brackets != 0) {
                tIdx++;
                if (tIdx == code.length()) {
                    throw new UnexpectedException("Missmatched brackets");//Not really sure witch exception to use
                }
                if (code.charAt(tIdx) == '[') {
                    brackets++;
                } else if (code.charAt(tIdx) == ']') {
                    brackets--;
                }
            }
            return tIdx;
        }{
            while ((code.charAt(tIdx) != '[' || brackets != 0)){
                tIdx--;
                if (tIdx == -1) {
                    throw new UnexpectedException("Missmatched brackets");
                }
                if(code.charAt(tIdx) == '['){
                    brackets--;
                } else if(code.charAt(tIdx) == ']'){
                    brackets++;
                }
            }
            return tIdx;
        }
    }

    private static void launchWithArgs(String[] args) throws IOException {

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("f", "file", true, "filename to interpret");
        options.addOption("s", "size", true, "set memory size");
        options.addOption("h", "help", false, "view help");
        options.addOption("g", "gui", false, "launchWithArgs GUI");
        options.addOption("t", "trace", false, "enable tracing");
        try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                String header = "Interprets brainfuck code";
                String footer = "*Not a complete list*";
                String cmdLineSyntax = "control -f <FILE> [-s <MemorySize>] [-h] [-t] [-g] ";
                formatter.printHelp(cmdLineSyntax, header, options, footer);
                return;
            }
            if(!line.hasOption("f")){
                throw new MissingOptionException("Missing required option -f ");
            }
            boolean trace = line.hasOption("t");
            int size = line.hasOption("s") ?Integer.parseInt(line.getOptionValue("s")):30000;
            String filename = line.getOptionValue("f");

            new Control(trace, filename, new Memory(size), new TerminalView()).interpret();
        } catch (ParseException  e) {
            System.err.println(e.getMessage());
        }catch (NumberFormatException e){
            System.err.println("-s must have an int arg");
        }
    }


    public static void main(String[] args) {
        try {
            launchWithArgs(args);
        } catch (IOException | NullPointerException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
