package com.epam;

import org.apache.commons.cli.*;

import java.io.*;

public class Launcher {
    public static void main(String[] args) {
        Option tapeLengthOption = new Option("l", "tapelength", true,
                "Length of the tape");

        // interpreter.exe -s src1.bf src2.bf src3.bf ...
        Option sourcesOption = new Option("s", "sources", true,
                "1-20 files with source code");
        sourcesOption.setArgs(20);
        sourcesOption.setOptionalArg(true);

        Options options = new Options();
        options.addOption(tapeLengthOption);
        options.addOption(sourcesOption);

        CommandLineParser cmdLineParser = new DefaultParser();
        CommandLine cmdLine;
        try {
            cmdLine = cmdLineParser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Can't parse input parameters");
            e.printStackTrace();
            return;
        }

        Interpreter interpreter;
        if (cmdLine.hasOption("l")) {
            int tapeLength;
            try {
                tapeLength = Integer.parseInt(cmdLine.getOptionValue("l"));
                if (tapeLength < 1)
                    throw new NumberFormatException();
            }
            catch (NumberFormatException e) {
                System.out.println("Incorrect length of tape. It will be 30 000");
                tapeLength = 30000;
            }
            interpreter = new Interpreter(tapeLength);
        } else {
            interpreter = new Interpreter();
        }

        if (cmdLine.hasOption("s")) {
            String[] files = cmdLine.getOptionValues("s");

            for (String file : files) {
                try (FileReader reader = new FileReader(file);
                     BufferedReader bufferedReader = new BufferedReader(reader)) {

                    StringBuilder builder = new StringBuilder();
                    while (bufferedReader.ready()) {
                        builder.append(bufferedReader.readLine());
                    }

                    interpreter.interpret(builder.toString());
                } catch (FileNotFoundException e) {
                    System.out.println("Can't find file " + file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
