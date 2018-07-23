package com.epam;

import org.apache.commons.cli.*;
import java.io.IOException;
import static javax.xml.bind.DatatypeConverter.parseInt;


class Launcher{

	public static void main(String[] args) throws IOException {
		Options options = new Options();
		options.addOption("h", false, "help");
		options.addOption("o", true, "output source code");
		options.addOption("s", true, "set the size for cells");
		options.addOption("i", true, "set the full path to the file");
		CommandLineParser cmdLineParser = new DefaultParser();
		CommandLine commandLine;

		int size = 0;

		try {
			commandLine = cmdLineParser.parse(options, args);

			if (commandLine.hasOption("s")){
				size = parseInt(commandLine.getOptionValue("s"));
			}

			Controller control = new Controller(new Model(size),new View());

			if (commandLine.hasOption("h")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("info ", options);
			} else
				if (commandLine.hasOption("o")) {
				System.out.println(control.getFileCode(commandLine.getOptionValue("o")));
			} else
				if(commandLine.hasOption("i")) {
					control.interpreter(control.getFileCode(commandLine.getOptionValue("i")),false);
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}
}