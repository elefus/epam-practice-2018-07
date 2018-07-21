package com.epam;

import org.apache.commons.cli.*;
import java.io.IOException;
import java.net.URISyntaxException;

class OptionParser {
    private Options options = createOptions();

    void parse(String[] args) {
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine line = parser.parse(options, args);

            int maxArraySize = line.hasOption("s") ? Integer.parseInt(line.getOptionValue("s")) : 30000;

            new Controller(line.getOptionValue("f"), new Model(maxArraySize), new ConsoleView(), line.hasOption("t")).process();
        } catch (NumberFormatException | IOException | URISyntaxException e) {
            e.printStackTrace();
        } catch (MissingArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Options createOptions() {
        Option  file = new Option("f","file", true, "Input file to read data from");
        Option  size = new Option("s","size", true, "Size of array of cells in program");
        Option trace = new Option("t","trace",false,"Tracing of commands in file");

        size.setRequired(false);
        trace.setRequired(false);

        Options options = new Options();
        options.addOption(file);
        options.addOption(size);
        options.addOption(trace);
        return options;
    }
}
