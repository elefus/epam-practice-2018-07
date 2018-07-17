package com.epam;

import org.apache.commons.cli.*;

class Parser
{
  private static Options options = null;

  private void createOptions() {
    Option f_input = new Option("f", "f_input", true, "Input file to read data from");
    f_input.setArgs(1);
    f_input.setArgName("file_name");

    Option size = new Option("s", "size", true, "Size of array of cells in programm");
    size.setRequired(false);

    Option tracing = new Option("tr", "tracing", false, "Tracing of commands in file");
    tracing.setRequired(false);

    options = new Options();
    options.addOption(f_input);
    options.addOption(size);
    options.addOption(tracing);
  }

  public void parsing(String[] args) {
    createOptions();
    CommandLineParser parser = new DefaultParser();

    try {
      CommandLine cmd = parser.parse(options, args);

      if(cmd.hasOption("f"))
      {
        Controller controller = new Controller();

        if(cmd.hasOption("s"))
        {
          if(cmd.hasOption("tr"))
            controller.setTrace();

          controller.initialize(cmd.getOptionValue("f"), cmd.getOptionValue("s"));
        }
        else
        {
          if(cmd.hasOption("tr"))
            controller.setTrace();

          controller.initialize(cmd.getOptionValue("f"));
        }
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}
