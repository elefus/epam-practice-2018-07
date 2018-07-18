package com.epam;

import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    private static void interpret(String code, Cells cells, View view) throws IOException {
        int openedBrackets;
        for (int i = 0; i < code.length(); i++) {

            openedBrackets = 1;
            switch (code.charAt(i)) {
                case '+':
                    cells.add();
                    break;

                case '-':
                    cells.sub();
                    break;

                case '>':
                    cells.shiftRight();
                    break;

                case '<':
                    cells.shiftLeft();
                    break;

                case '.':
                    view.printSymbol(cells.getCells()[cells.getCurrentCell()]);
                    break;

                case ',':
                    int symbol = view.readSymbol();
                    cells.input((char) symbol);
                    break;

                case '[':
                    if (cells.getCells()[cells.getCurrentCell()] == 0) {
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
                    if (cells.getCells()[cells.getCurrentCell()] != 0) {
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
                    view.print(cells.getCells(), cells.getCurrentCell(), delay);
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

        if (line.hasOption("t")) {
            trace = line.hasOption("t");
            delay = Integer.parseInt(line.getOptionValue("t"));
        }

        Cells cells = new Cells(numOfCells);
        View view = new View();

        String code = getCode(filename);
        interpret(code, cells, view);
    }
}
