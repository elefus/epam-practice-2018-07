package com.epam.View;

import org.apache.commons.cli.*;

import java.io.IOException;

public class ConsoleView implements View {

    private String[] files;
    private char[] input;
    private int inputIndex = 0;
    private int cellSize = 8;

    public ConsoleView(String[] args) {
        Option sourceOption = Option.builder("s")
                .hasArgs()
                .desc("source files")
                .longOpt("source")
                .required()
                .build();
        Option inputOption = Option.builder("i")
                .hasArgs()
                .desc("input for programs")
                .longOpt("input")
                .build();
        Option cellSizeOption = Option.builder("c")
                .hasArg()
                .desc("set cell size (8/16")
                .longOpt("cell")
                .build();

        Options options = new Options();
        options.addOption(sourceOption);
        options.addOption(inputOption);
        options.addOption(cellSizeOption);

        CommandLineParser cmdLineParser = new DefaultParser();
        CommandLine commandLine = null;

        try {
            commandLine = cmdLineParser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (commandLine.hasOption("s")) {
            files = commandLine.getOptionValues("s");
        }

        if (commandLine.hasOption("i")) {
            String[] inputArray = commandLine.getOptionValues("i");
            StringBuilder s = new StringBuilder();
            for (String i : inputArray)
                s.append(i);
            input = s.toString().toCharArray();
        }

        if (commandLine.hasOption("c")) {
            int size = Integer.parseInt(commandLine.getOptionValue("c"));
            if (size == 8 || size == 16) {
                cellSize = size;
            } else {
                System.out.println("Cell size can be only 8 or 16 bits. The cell size is set to 8");
            }
        }
    }

    @Override
    public String[] getFiles() {
        return files;
    }

    @Override
    public char readChar() {
        if (inputIndex < input.length) {
            return input[inputIndex++];
        } else {
            char c = Character.MIN_VALUE;
            try {
                c = (char) System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return c;
        }
    }

    @Override
    public void printChar(char c) {
        System.out.print(c);
    }

    @Override
    public int getCellSize() {
        return cellSize;
    }
}
