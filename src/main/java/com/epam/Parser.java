package com.epam;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.URISyntaxException;

public class Parser {
    private Options options = createOptions();

    public void parse(String...args) throws ParseException, IOException, URISyntaxException {
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        if (line.hasOption("h")) {
            if(args.length == 1)
                getHelp();
            else
                throw new ParseException("Option -h must go without another options/arguments");
        } else if (line.hasOption("g")) {
            if(args.length == 1) {
                new GuiView().initialize(null, 30000, false);
            } else {
                int size = line.hasOption("s") ? Integer.parseInt(line.getOptionValue("s")) : 30000;
                String fileName = line.hasOption("f") ? line.getOptionValue("f") : null;
                new GuiView().initialize(fileName, size, line.hasOption("t"));
            }
        } else {
            if (!line.hasOption("f"))
                throw new MissingOptionException("Missing option : -f");

            int size = line.hasOption("s") ? Integer.parseInt(line.getOptionValue("s")) : 30000;
            new Controller(line.getOptionValue("f"), new Model(size), new ConsoleView(), line.hasOption("t")).process();
        }
    }

    private Options createOptions() {
        Option  file = new Option("f","file", true, "Input file");
        Option  help = new Option("h","help", false,"Get help");
        Option  size = new Option("s","size", true, "Memory size");
        Option gui = new Option("g","gui",false,"GUI");
        Option trace = new Option("t","trace",false,"Tracing");

        size.setRequired(false);
        trace.setRequired(false);

        Options options = new Options();
        options.addOption(help);
        options.addOption(file);
        options.addOption(size);
        options.addOption(trace);
        options.addOption(gui);
        return options;
    }

    private void getHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("-h | [-g] -f <FILE_NAME> [-s SIZE] [-t]", options);
    }
}