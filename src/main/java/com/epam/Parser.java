package com.epam;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.URISyntaxException;


class Parser {

    public static void main(String[] args) throws IOException, URISyntaxException {
        Options options = new Options();
        options.addOption("h", false, "HELP");
        options.addOption("g", false, "GUI");
        options.addOption("s", true, "Memory size");
        options.addOption("f", true, "Input file");
        CommandLineParser cmdLineParser = new DefaultParser();
        CommandLine commandLine;

        int size = 0;

        try {
            commandLine = cmdLineParser.parse(options, args);

            if (commandLine.hasOption("s")) {
                size = Integer.parseInt(commandLine.getOptionValue("s"));
            }

            if (commandLine.hasOption("g")) {
                new GuiView();
            }

            Controller control = new Controller(new Model(size), new View());

            if (commandLine.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("info ", options);
            } else if (commandLine.hasOption("f")) {
                boolean isGui = false;
                control.interprete(control.getSource(commandLine.getOptionValue("f")), isGui);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }
}





