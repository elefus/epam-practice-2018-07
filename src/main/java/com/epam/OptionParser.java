package com.epam;

import org.apache.commons.cli.*;

import java.io.IOException;

public class OptionParser {
    private Options options = createOptions();

    public void parse(String...args) throws ParseException, IOException {
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        if (line.hasOption("h")) {
            getHelp();
        } else {
            if (!line.hasOption("f"))
                throw new MissingOptionException("Missing option : -f");

            int size = line.hasOption("s") ? Integer.parseInt(line.getOptionValue("s")) : 30000;

            new Controller(line.getOptionValue("f"), new Model(size), new ConsoleView(), line.hasOption("t")).process();
        }
    }

    private Options createOptions() {
        Option  file = new Option("f","file", true, "Input file to read data from");
        Option  help = new Option("h","help", false,"Get help about actual options");
        Option  size = new Option("s","size", true, "Dimension of array of cells");
        Option swing = new Option("S","swing",false,"Graphic display of the program ");
        Option trace = new Option("t","trace",false,"Tracing of commands in the file");

        size.setRequired(false);
        trace.setRequired(false);

        Options options = new Options();
        options.addOption(help);
        options.addOption(file);
        options.addOption(size);
        options.addOption(trace);
        options.addOption(swing);
        return options;
    }

    private void getHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("interpreter", options);
    }
}
