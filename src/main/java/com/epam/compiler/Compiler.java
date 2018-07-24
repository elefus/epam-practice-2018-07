package com.epam.compiler;

import com.epam.compiler.Token.Type;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.cli.*;

public class Compiler {

  private static String fileName;
  private static boolean optimization;

  public static void main(String[] args) {
    HelpFormatter formatter = new HelpFormatter();

    CommandLineParser cmdLineParser = new DefaultParser();
    CommandLine commandLine;

    try {
      commandLine = cmdLineParser.parse(getOptions(), args);
      if (commandLine.hasOption("h")) {
        formatter.printHelp("Compiler", getOptions());
        System.exit(0);
      }

      if (commandLine.hasOption("s")) {
        fileName = commandLine.getOptionValue("s");
      }

      if (commandLine.hasOption("o")) {
        optimization = true;
      }
    } catch (ParseException e) {
      System.out.println("Source file is required");
      formatter.printHelp("Compiler", getOptions());
      System.exit(1);
    }

    ArrayList<Token> tokens = createTokens(readFile(fileName));

    if (optimization) {
      tokens = Optimizer.optimize(tokens);
    }

    byte[] byteCode = ByteCodeGenerator.createByteCode(tokens);

    Class<?> myClassLoaded =
        new ClassLoader() {
          Class<?> load() {
            return defineClass(null, byteCode, 0, byteCode.length);
          }
        }.load();

    try {
      Object ref = myClassLoaded.newInstance();
      System.out.println(ref);
      ((Runnable) ref).run();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private static Options getOptions() {
    Option sourceOption = Option.builder("s")
        .hasArgs()
        .desc("source file")
        .longOpt("source")
        .required()
        .build();
    Option optimizeOption = Option.builder("o")
        .hasArg(false)
        .desc("enable optimization")
        .longOpt("optimize")
        .build();
    Option helpOption = Option.builder("h")
        .hasArg(false)
        .desc("show help")
        .longOpt("help")
        .build();

    Options options = new Options();
    options.addOption(sourceOption);
    options.addOption(optimizeOption);
    options.addOption(helpOption);
    return options;
  }

  private static String readFile(String fileName) {
    StringBuilder stringBuilder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return stringBuilder.toString();
  }

  private static ArrayList<Token> createTokens(String code) {
    ArrayList<Token> tokens = new ArrayList<>();

    for (int i = 0; i < code.length(); i++) {
      switch (code.charAt(i)) {
        case '+':
          tokens.add(new Token(Type.CHANGE_VAL));
          break;

        case '-':
          tokens.add(new Token(Type.CHANGE_VAL, -1));
          break;

        case '>':
          tokens.add(new Token(Type.SHIFT));
          break;

        case '<':
          tokens.add(new Token(Type.SHIFT, -1));
          break;

        case ',':
          tokens.add(new Token(Type.INPUT));
          break;

        case '.':
          tokens.add(new Token(Type.OUTPUT));
          break;

        case '[':
          tokens.add(new Token(Type.WHILE_START));
          break;

        case ']':
          tokens.add(new Token(Type.WHILE_END));
          break;
      }
    }

    return tokens;
  }
}
