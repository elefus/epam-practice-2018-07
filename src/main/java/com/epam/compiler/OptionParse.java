package com.epam.compiler;

import org.apache.commons.cli.*;

public class OptionParse {
    public static String parsing(String[] args) {
        Option opt1 = new Option("f", "file", true, "File name.");
        opt1.setArgs(1);
        opt1.setArgName("fileName");
        Option opt2 = new Option("h", "help", true, "Help message.");
        opt2.setArgName("option");
        opt2.setOptionalArg(true);
        Options opts = new Options();
        opts.addOption(opt1);
        opts.addOption(opt2);

        DefaultParser dp = new DefaultParser();
        CommandLine cl;
        try {
            cl = dp.parse(opts, args);

            if (cl.hasOption("h")) {
                String value = (cl.getOptionValue("h") == null) ? "" : cl.getOptionValue("h");
                if (value.contentEquals("file")) {
                    System.out.println("Option -f <FileName>:\tname of program file. This option is necessary.");
                    System.out.println("Example: -f test.bf");
                } else if (value.contentEquals("view")) {
                    System.out.println("Option -v:\tset graphic interface.");
                    System.out.println("Example: -v");
                } else if (value.contentEquals("size")) {
                    System.out.println("Option -s <memorySize>:\tset size of memory.");
                    System.out.println("Example: -s 40000");
                } else if (value.contentEquals("i")) {
                    System.out.println("Option -i <type> <name>:\tset stream to read input data.");
                    System.out.println("Example: -i file dataFile.txt");
                } else if (value.contentEquals("o")) {
                    System.out.println("Option -o:\tset stream to write output data.");
                    System.out.println("Example: -o <type> <OutputStream>");
                } else if (value.contentEquals("t")) {
                    System.out.println("Option -t:\ttrace the program, if needed.");
                    System.out.println("Example: -t");
                } else {
                    System.out.println("Option -f <FileName>:\tname of program file. This option is necessary.");
                    System.out.println("Example: -f test.bf");
                    System.out.println("Option -v:\tset graphic interface.");
                    System.out.println("Example: -v");
                    System.out.println("Option -s <memorySize>:\tset size of memory.");
                    System.out.println("Example: -s 40000");
                    System.out.println("Option -i <InputStream>:\tset stream to read input data.");
                    System.out.println("Example: -i dataFile.txt");
                    System.out.println("Option -i <InputStream>:\tset stream to read input data.");
                    System.out.println("Example: -i dataFile.txt");
                    System.out.println("Option -o:\tset stream to write output data.");
                    System.out.println("Example: -o <OutputStream>");
                    System.out.println("Option -t:\ttrace the program, if needed.");
                    System.out.println("Example: -t");
                }
                System.exit(1);
            }

            if (cl.hasOption("f")) {
                return cl.getOptionValue("f");
            } else {
                System.out.println("Required key -f is missing!");
                System.exit(1);
            }

        } catch (ParseException e) {
            System.out.println("Can't parse input parameters!");
            e.printStackTrace();
            System.exit(1);
        }

        return "";
    }
}
