package com.epam.interpreter.view;

import org.apache.commons.cli.*;

import java.io.IOException;

public class ConsoleView implements View {

  private String[] files;
  private char[] input;
  private int inputIndex = 0;
  private int cellSize = 8;
  private int memoryCapacity = 30000;

  public ConsoleView(String[] args) {
    Option sourceOption =
        Option.builder("s").hasArgs().desc("source files").longOpt("source").required().build();
    Option inputOption =
        Option.builder("i").hasArgs().desc("input for programs").longOpt("input").build();
    Option cellSizeOption =
        Option.builder("c").hasArg().desc("set cell size (8/16").longOpt("cell").build();
    Option arraySizeOption =
        Option.builder("m").hasArg().desc("set memory capacity").longOpt("memory").build();
    Option helpOption = Option.builder("h").hasArg(false).desc("show help").longOpt("help").build();

    Options options = new Options();
    options.addOption(sourceOption);
    options.addOption(inputOption);
    options.addOption(cellSizeOption);
    options.addOption(arraySizeOption);
    options.addOption(helpOption);

    HelpFormatter formatter = new HelpFormatter();
    CommandLineParser cmdLineParser = new DefaultParser();
    CommandLine commandLine;

    try {
      commandLine = cmdLineParser.parse(options, args);

      if (commandLine.hasOption("h")) {
        formatter.printHelp("Compiler", options);
        System.exit(0);
      }

      if (commandLine.hasOption("m")) {
        memoryCapacity = Integer.parseInt(commandLine.getOptionValue("m"));
      }

      if (commandLine.hasOption("s")) {
        files = commandLine.getOptionValues("s");
      }

      if (commandLine.hasOption("i")) {
        String[] inputArray = commandLine.getOptionValues("i");
        StringBuilder s = new StringBuilder();
        for (String i : inputArray) {
          s.append(i);
        }
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
    } catch (ParseException e) {
      formatter.printHelp("Compiler", options);
      System.exit(1);
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

  @Override
  public int getMemoryCapacity() {
    return memoryCapacity;
  }
}
