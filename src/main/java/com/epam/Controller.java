package com.epam;

import com.epam.commands.*;
import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;



public class Controller {
    private static int numOfCells = 256;
    private static boolean trace;
    public static boolean stop;
    public static int delay;
    public static int instructionNumber;
    public static int openedBrackets;

    public static void main(String[] args) throws IOException, URISyntaxException, ParseException {

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("f", "file", true, "Filename");
        options.addOption("s", "size", true, "Number of cells");
        options.addOption("h", "help", false, "Help");
        options.addOption("t", "trace", true, "Tracing in milliseconds");
        options.addOption("g", "gui", false, "GUI");

        CommandLine line = parser.parse(options, args);

        if (line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            String header = "Interpreter for brainFuck";
            String footer = "";
            String cmdLineSyntax = "control -f <File> [-s <Number of cells>] [-h] [-t <delay>]";
            formatter.printHelp(cmdLineSyntax, header, options, footer);
            return;
        }
        boolean gui = line.hasOption("g");
        if (!line.hasOption("f")&&!gui) {
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
        if (!gui) {
            TerminalView view = new TerminalView();
            String code = getCode(filename);
            interpret(code, cells, view);
        } else {
            GuiView guiView = new GuiView();
            guiView.initListeners(cells);
        }
    }

    public static void restart(Cells cells) {
        cells.restart();
    }

    public static String getCode(String file) throws IOException, URISyntaxException {
        String code;

        if (Controller.class.getResource("./../../" + file) == null) {
            throw new FileNotFoundException("File not found");
        }
        code = Files.lines(Paths.get(Controller.class.getResource("./../../" + file).toURI()))
                .collect(joining(System.lineSeparator()));
        return code;
    }

    public static void interpret(String code, Cells cells, View view) {

        Map<Character, Command> commandMap = new HashMap<>();
        commandMap.put('+', new Plus());
        commandMap.put('-', new Minus());
        commandMap.put('>', new ShiftRight());
        commandMap.put('<', new ShiftLeft());
        commandMap.put('.', new PrintSymbol());
        commandMap.put(',', new ReadSymbol());
        commandMap.put('[', new LeftBracket());
        commandMap.put(']', new RightBracket());

        for (instructionNumber = 0; instructionNumber < code.length(); instructionNumber++) {
            if (stop) {
                break;
            }
            openedBrackets = 1;
            Command command = commandMap.getOrDefault(code.charAt(instructionNumber), new Skip());
            command.execute(code, cells, view);
            if (trace) {
                String commands = "<>+-.,";
                if (commands.contains(Character.toString(code.charAt(instructionNumber)))) {
                    view.print(cells.getCells(), cells.getCurrentCell(), delay);
                }
            }
        }
    }
}
