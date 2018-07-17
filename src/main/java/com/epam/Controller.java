package com.epam;

import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.stream.Collectors.joining;

public class Controller {

    private static int numOfCells = 256;
    private static boolean trace;
    private static int delay;

    private static String getCode(String file) throws IOException, URISyntaxException {
        String code;

        if (Controller.class.getResource("./../../" + file) == null) {
            throw new FileNotFoundException("File not found");
        }
        code = Files.lines(Paths.get(Controller.class.getResource("./../../" + file).toURI()))
                .collect(joining());
        return code;
    }

    private static void interpret(String code) throws IOException {
        int openedBrackets;
        for (int i = 0; i < code.length(); i++) {
            openedBrackets = 1;
            switch (code.charAt(i)) {
                case '+':
                    Cells.add();
                    break;

                case '-':
                    Cells.sub();
                    break;

                case '>':
                    Cells.shiftRight();
                    break;

                case '<':
                    Cells.shiftLeft();
                    break;

                case '.':
                    System.out.println(Cells.cells[Cells.currentCell]);                     //перенести во вью
                    break;

                case ',':
                    char symbol = (char) new InputStreamReader(System.in).read();           // Перенести во вью
                    Cells.input(symbol);
                    break;

                case '[':
                    if (Cells.cells[Cells.currentCell] == 0) {
                        while (openedBrackets != 0 || code.charAt(i) != ']') {
                            i++;
                            if (code.charAt(i) == '[') {
                                openedBrackets++;
                            }
                            if (code.charAt(i) == ']') {
                                openedBrackets--;
                            }
                        }
                    }
                    break;

                case ']':
                    if (Cells.cells[Cells.currentCell] != 0) {
                        while (openedBrackets != 0 || code.charAt(i) != '[') {
                            i--;
                            if (code.charAt(i) == ']') {
                                openedBrackets++;
                            }
                            if (code.charAt(i) == '[') {
                                openedBrackets--;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
            if (trace) {
                String commands = "<>+-.,";
                if (commands.contains(Character.toString(code.charAt(i)))) {
                    View.print(Cells.getCells(), Cells.getCurrentCell(), delay);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException, ParseException {

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("f", "file", true, "Filename");
        options.addOption("s", "size", true, "Number of cells");
        options.addOption("h", "help", false, "Help");
        options.addOption("t", "trace", true, "Tracing in milliseconds");
        CommandLine line = parser.parse(options, args);
        if (line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            String header = "Interpreter for brainFuck";
            String footer = "";
            String cmdLineSyntax = "control -f <File> [-s <Number of cells>] [-h] [-t <delay>]";
            formatter.printHelp(cmdLineSyntax, header, options, footer);
            return;
        }
        if (!line.hasOption("f")) {
            throw new MissingOptionException("Missing required option -f ");
        }
        String filename = line.getOptionValue("f");
        if (line.hasOption("s")) {
            numOfCells = Integer.parseInt(line.getOptionValue("s"));
        }
        trace = line.hasOption("t");
        delay = Integer.parseInt(line.getOptionValue("t"));

        Cells.create(numOfCells);
        String code = getCode(filename);
        interpret(code);
    }
}
