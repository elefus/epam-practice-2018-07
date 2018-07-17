package com.epam;

import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class OptionsParse {
    private static Options opts;
    private static CommandLine cl;

    public static void optionsParse(String[] args) {
        try {
            initialize(args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        InputStream in = System.in;
        OutputStream out = System.out;

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
                System.out.println("Example: -o <OutputStream>");
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
            return;
        }
        if (cl.hasOption("f")) {
            Controller.setFileName(cl.getOptionValue("f"));
        } else {
            System.out.println("Required key -f is missing!");
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
            } catch (NumberFormatException e) {
                System.out.println("The argument of option -s must be natural number!");
                return;
            }
        }
        if (cl.hasOption("i")) {
            String[] opts = cl.getOptionValues("i");
            if (opts[0].contentEquals("string")) {
                in = IOUtils.toInputStream(opts[1], StandardCharsets.UTF_8);
                Controller.setInStream(new InputStreamReader(in));
            } else if (opts[0].contentEquals("file")) {
                try {
                    in = new FileInputStream(opts[1]);
                    Controller.setInStream(new InputStreamReader(in));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Incorrect type of input stream!");
                return;
            }
        }
        if (cl.hasOption("o")) {
            String[] opts = cl.getOptionValues("o");
            if (opts[0].contentEquals("file")) {
                try {
                    out = new FileOutputStream(opts[1]);
                    Controller.setOutStream(new OutputStreamWriter(out));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Incorrect type of input stream!");
                return;
            }
        }
        if (cl.hasOption("t")) {
            Controller.setIsTrace(true);
        } else {
            Controller.setIsTrace(false);
        }

        View.start();

        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        opt4.setArgs(2);
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
        try {
            cl = dp.parse(opts, args);
        } catch (ParseException e) {
            System.out.println("Can't parse input parameters!");
            e.printStackTrace();
            return;
        }
    }
}
