package com.epam;

import org.apache.commons.cli.*;

public class OptionsParse {
    private static Options opts;
    private static CommandLine cl;

    public static void optionsParse(String[] args) {
        try {
            initialize(args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (cl.hasOption("h")) {
            String option = (cl.getOptionValue("h") == null) ? "" : cl.getOptionValue("h");
            if (option.contentEquals("file")) {
                System.out.println("Option -f <FileName>:\tname of program file. This option is necessary.");
                System.out.println("Example: -f test.bf");
            } else if (option.contentEquals("view")) {
                System.out.println("Option -v:\tset graphic interface.");
                System.out.println("Example: -v");
            } else if (option.contentEquals("size")) {
                System.out.println("Option -s <memorySize>:\tset size of memory.");
                System.out.println("Example: -s 40000");
            } else if (option.contentEquals("i")) {
                System.out.println("Option -i <InputStream>:\tset stream to read input data.");
                System.out.println("Example: -i dataFile.txt");
            } else if (option.contentEquals("o")) {
                System.out.println("Option -o:\tset stream to write output data.");
                System.out.println("Example: -o <OutputStream>");
            } else if (option.contentEquals("t")) {
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
            return;
        }
        if (cl.hasOption("f")) {
            Controller.setFileName(cl.getOptionValue("f"));
        } else {
            System.out.println("Required key -f is missing.");
            return;
        }
        if (cl.hasOption("v")) {
            Controller.setGraphInterface(true);
        } else {
            Controller.setGraphInterface(false);
        }
        if (cl.hasOption("s")) {
            try {
                Controller.setMemLength(Integer.parseInt(cl.getOptionValue("s")));
            } catch (Exception e) {
                System.out.println("The argument of option -s must be natural number.");
                return;
            }
        }
        if (cl.hasOption("i")) {
        }
        if (cl.hasOption("o")) {
        }
        if (cl.hasOption("t")) {
            Controller.setIsTrace(true);
        } else {
            Controller.setIsTrace(false);
        }

        View.start();
    }

    private static void initialize(String[] args) throws ParseException {
        Option opt1 = new Option("f", "file", true, "File name.");
        opt1.setArgs(1);
        opt1.setArgName("fileName");
        Option opt2 = new Option("v", "view", false, "Type of view - console or graphic interface.");
        Option opt3 = new Option("s", "size", true, "Size of memory.");
        opt3.setArgs(1);
        opt3.setArgName("size");
        Option opt4 = new Option("i", "input", true, "Name of input stream.");
        opt4.setArgs(1);
        opt4.setArgName("inputStream");
        Option opt5 = new Option("o", "output", true, "Name of output stream.");
        opt5.setArgs(1);
        opt5.setArgName("outputStream");
        Option opt6 = new Option("t", "trace", false, "Trace the program.");
        Option opt7 = new Option("h", "help", true, "Help message.");
        opt7.setArgName("option");
        opt7.setOptionalArg(true);

        opts = new Options();
        opts.addOption(opt1);
        opts.addOption(opt2);
        opts.addOption(opt3);
        opts.addOption(opt4);
        opts.addOption(opt5);
        opts.addOption(opt6);
        opts.addOption(opt7);

        DefaultParser dp = new DefaultParser();
        cl = dp.parse(opts, args);
    }
}
