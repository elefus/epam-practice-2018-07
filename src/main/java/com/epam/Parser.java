package com.epam;

import org.apache.commons.cli.*;

import java.io.IOException;

class Parser
{
    public void parse(String[] args) {
        CommandLineParser parser = new DefaultParser();

        try{
            Options options = createOptions();
            CommandLine line = parser.parse(options, args);

            if(!line.hasOption("f"))
                throw new MissingArgumentException("Missing required option 'f'");

            int size = line.hasOption("s") ? Integer.parseInt(line.getOptionValue("s")) : 30000;
            boolean isTrace = line.hasOption("tr");

            new Controller(isTrace, line.getOptionValue("f"), new Model(size), new ConsoleView()).processCommands();
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        } catch (MissingArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Options createOptions() {
        Option f_input = new Option("f", "f_input",true, "Input file to read data from");
        Option  m_size = new Option("s", "size",   true, "Size of array of cells in program");
        m_size.setRequired(false);
        Option tracing = new Option("tr","trace",  false,"Tracing of commands in file");
        tracing.setRequired(false);

        Options options = new Options();
        options.addOption(f_input);
        options.addOption(m_size);
        options.addOption(tracing);

        return options;
    }
}
