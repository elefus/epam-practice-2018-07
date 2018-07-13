package com.epam;

import org.apache.commons.cli.*;

public class Terminal {

    private static Options opts;
    private static CommandLine cl;

    private static void initialize(String[] args) throws ParseException {
        Option opt1 = new Option("tr", "trace", false, "Trace the program.");
        opt1.setArgs(0);
        opt1.setArgName("trace");
        Option opt2 = new Option("f", "file", true, "File name.");
        opt2.setArgs(1);
        opt2.setArgName("name");
        Options opts = new Options();
        opts.addOption(opt1);
        opts.addOption(opt2);

        DefaultParser dp = new DefaultParser();
        cl = dp.parse(opts, args);
    }

    public static void optionsParse(String[] args) {
        try {
            initialize(args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (cl.hasOption("f")) {
            Controller.interpret(cl.getOptionValue("f"));
        } else {
            System.out.println("Required key -f is missing.");
        }
    }
}
