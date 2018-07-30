package com.epam.interpeter;

import org.apache.commons.cli.*;

import java.io.IOException;

public class OptionParser {
    private Options options = createOptions();

    public void parse(String...args) throws ParseException, IOException {
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        if (line.hasOption("h")) {
            if(args.length == 1)
                getHelp();
            else
                throw new ParseException("Option -h must go without another options/arguments");
        } else if (line.hasOption("S")) {
            if(args.length == 1) {
                int size = line.hasOption("s") ? Integer.parseInt(line.getOptionValue("s")) : 30000;
                String fileName = line.hasOption("f") ? line.getOptionValue("f") : null;
                new SwingView(false, 0, 0).addActionListeners();
            } else {
                int size = line.hasOption("s") ? Integer.parseInt(line.getOptionValue("s")) : 0;
                int delay = line.hasOption("d") ? Integer.parseInt(line.getOptionValue("d")) : 0;
                new SwingView(line.hasOption("t"), size, delay).addActionListeners();
            }
        } else {
            if (!line.hasOption("f"))
                throw new MissingOptionException("Missing option : -f");

            int size = line.hasOption("s") ? Integer.parseInt(line.getOptionValue("s")) : 30000;
            new Controller(Controller.getSourceCode(line.getOptionValue("f")), new Model(size), new ConsoleView(), line.hasOption("t")).process();
        }
    }

    private Options createOptions() {
        Option  file = new Option("f","file", true, "INPUT file to read data from");
        Option  help = new Option("h","help", false,"Get help about actual options");
        Option  size = new Option("s","size", true, "Dimension of array of cells");
        Option swing = new Option("S","swing",false,"Graphic display of the program ");
        Option trace = new Option("t","trace",false,"Tracing of commands in the file");
        Option delay = new Option("d","delay",true,"Delay of view in graphics mode (from 0 to 50)");

        size.setRequired(false);
        trace.setRequired(false);
        delay.setRequired(false);

        Options options = new Options();
        options.addOption(help);
        options.addOption(file);
        options.addOption(size);
        options.addOption(trace);
        options.addOption(swing);
        options.addOption(delay);
        return options;
    }

    private void getHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("-h | [-S] -f <FILE_NAME> [-s SIZE] [-t]", options);
    }
}
