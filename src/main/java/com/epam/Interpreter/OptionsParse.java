package com.epam.Interpreter;

import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class OptionsParse {
    private Options opts;
    private CommandLine cl;

    public void start(String[] args, Controller controller) {
        Option opt1 = new Option("f", "file", true, "File name.");
        opt1.setArgs(1);
        opt1.setArgName("fileName");
        Option opt2 = new Option("v", "view", true, "Type of view - console or graphic interface.");
        opt1.setArgs(1);
        opt1.setArgName("type");
        Option opt3 = new Option("s", "size", true, "Size of memory.");
        opt3.setArgs(1);
        opt3.setArgName("size");
        Option opt4 = new Option("i", "input", true, "Name of input stream.");
        opt4.setArgs(2);
        opt4.setArgName("inputStream");
        Option opt5 = new Option("o", "output", true, "Name of output stream.");
        opt5.setArgs(1);
        opt5.setArgName("outputStream");
        Option opt6 = new Option("h", "help", true, "Help message.");
        opt6.setArgName("option");
        opt6.setOptionalArg(true);

        opts = new Options();
        opts.addOption(opt1);
        opts.addOption(opt2);
        opts.addOption(opt3);
        opts.addOption(opt4);
        opts.addOption(opt5);
        opts.addOption(opt6);

        DefaultParser dp = new DefaultParser();
        try {
            cl = dp.parse(opts, args);
        } catch (ParseException e) {
            System.out.println("Can't parse input parameters!");
            e.printStackTrace();
            return;
        }

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
            return;
        }
        if (cl.hasOption("f")) {
            controller.setFileName(cl.getOptionValue("f"));
        } else {
            System.out.println("Required key -f is missing!");
            return;
        }
        if (cl.hasOption("v")) {
            if (cl.getOptionValue("v").contentEquals("window")) {
                controller.setView(new WindowView());
            } else if (cl.getOptionValue("v").contentEquals("console")) {
                controller.setView(new ConsoleView());
            }  else {
                System.out.println("Incorrect type of interface!");
                return;
            }
        }
        if (cl.hasOption("i")) {
            String[] opts = cl.getOptionValues("i");
            if (opts[0].contentEquals("string")) {
                controller.setInStream(IOUtils.toInputStream(opts[1], StandardCharsets.UTF_8));
            } else if (opts[0].contentEquals("file")) {
                try {
                    controller.setInStream(new FileInputStream(opts[1]));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
            } else if (opts[0].contentEquals("file")) {
                controller.setInStream(System.in);
            } else {
                System.out.println("Incorrect type of input stream!");
                return;
            }
        }
        if (cl.hasOption("o")) {
            String[] opts = cl.getOptionValues("o");
            if (opts[0].contentEquals("file")) {
                try {
                    controller.setOutStream(new FileOutputStream(opts[1]));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
            } else if (opts[0].contentEquals("console")) {
                controller.setOutStream(System.out);
            } else {
                System.out.println("Incorrect type of output stream!");
                return;
            }
        }
        if (cl.hasOption("s")) {
            try {
                controller.getModel().setMemLength((Integer.parseInt(cl.getOptionValue("s"))));
            } catch (NumberFormatException e) {
                System.out.println("The argument of option -s must be natural number!");
                return;
            }
        }
    }
}