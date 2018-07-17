package com.epam;

import org.apache.commons.cli.*;
import java.io.IOException;


class Launcher extends Controller {

	public static void main(String[] args) throws IOException {
		Options options = new Options();
		options.addOption("h", false, "help");
		options.addOption("i", true, "name of the interpreted file");
		options.addOption("o", true, "output source code");
		CommandLineParser cmdLineParser = new DefaultParser();
		CommandLine commandLine = null;

		try {
			commandLine = cmdLineParser.parse(options, args);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		if (commandLine.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("info ", options);
		} else if (commandLine.hasOption("i")) {
			interpreter(getFileCode(commandLine.getOptionValue("i")));
		} else if (commandLine.hasOption("o")) {
			System.out.println(getFileCode(commandLine.getOptionValue("o")));
		}
	}
}