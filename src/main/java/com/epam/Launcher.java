package com.epam;

import org.apache.commons.cli.*;

import java.io.*;

class LaunchInfo {
    public final int tapeLength;
    public final String[] fileNames;

    public LaunchInfo(int tapeLength, String[] fileNames) {
        this.tapeLength = tapeLength;
        this.fileNames = fileNames;
    }
}

public class Launcher {
    public static void main(String[] args) {
        LaunchInfo launchInfo;
        try {
            launchInfo = parseArgs(args);
        } catch (ParseException e) {
            System.out.println("Can't parse arguments");
            e.printStackTrace();
            return;
        }

        Interpreter interpreter = new Interpreter(launchInfo.tapeLength, new ConsoleView());

        for (String file : launchInfo.fileNames) {
            try {
                interpreter.interpret(readAllLines(file));
            } catch (IOException e) {
                System.out.println("Failed to read " + file);
                e.printStackTrace();
            }
        }
    }

    public static String readAllLines(String fileName) throws IOException {
        try (FileReader reader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            StringBuilder builder = new StringBuilder();
            while (bufferedReader.ready()) {
                builder.append(bufferedReader.readLine());
            }

            return builder.toString();
        }
    }

    public static LaunchInfo parseArgs(String[] args) throws ParseException {
        Option tapeLengthOption = new Option("l", "tapeLength", true,
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
        cmdLine = cmdLineParser.parse(options, args);

        int tapeLength = 30000;
        if (cmdLine.hasOption("l")) {
            try {
                tapeLength = Integer.parseInt(cmdLine.getOptionValue("l"));
                if (tapeLength < 1)
                    throw new NumberFormatException();
            }
            catch (NumberFormatException e) {
                System.out.println("Incorrect length of tape. It will be 30 000");
            }
        }

        String[] fileNames = new String[0];
        if (cmdLine.hasOption("s")) {
            fileNames = cmdLine.getOptionValues("s");
        }

        return new LaunchInfo(tapeLength, fileNames);
    }
}
