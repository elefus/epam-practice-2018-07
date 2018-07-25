package com.epam;

import com.epam.interpreter.GuiView;
import com.epam.interpreter.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.cli.*;

import java.io.*;

import com.epam.compiler.Compiler;
import com.epam.interpreter.Interpreter;
import com.epam.interpreter.ConsoleView;

class LaunchInfo {
    public final int tapeLength;
    public final String[] fileNames;
    public final Boolean needToCompile;
    public final Boolean guiEnabled;

    public LaunchInfo(int tapeLength, String[] fileNames, Boolean needToCompile,
                      Boolean guiEnabled) {
        this.tapeLength = tapeLength;
        this.fileNames = fileNames;
        this.needToCompile = needToCompile;
        this.guiEnabled = guiEnabled;
    }
}

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LaunchInfo launchInfo;
        try {
            launchInfo = parseArgs(getParameters().getRaw().toArray(new String[0]));
        } catch (ParseException e) {
            System.out.println("Can't parse arguments");
            e.printStackTrace();
            return;
        }

        if (launchInfo.needToCompile) {
            launchCompiler(launchInfo);
        } else {
            launchInterpreter(launchInfo, primaryStage);
        }

        if (!launchInfo.guiEnabled)
            Platform.exit();
    }

    private static void launchCompiler(LaunchInfo launchInfo) {
        Compiler compiler = new Compiler(true);

        for (String file : launchInfo.fileNames) {
            try {
                compiler.compile(readAllLines(file));
            } catch (IOException e) {
                System.out.println("Failed to read " + file);
                e.printStackTrace();
            }
        }

        try {
            compiler.toFile("program.class");
        } catch (IOException e) {
            System.out.println("Failed to write program.class");
            e.printStackTrace();
        }

        compiler.launchProgram();
    }

    private static void launchInterpreter(LaunchInfo launchInfo, Stage primaryStage) {

        View view;
        if (launchInfo.guiEnabled) {
            try {
                FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("InterpreterWindowLayout.fxml"));
                Parent load = loader.load();
                view = loader.getController();
                primaryStage.setTitle("Brainfuck interpreter");
                primaryStage.setScene(new Scene(load));
                primaryStage.show();
            } catch (IOException e) {
                System.out.println("Failed to load GUI. Interpreter will work with the console");
                e.printStackTrace();
                view = new ConsoleView();
            }
        } else {
            view = new ConsoleView();
        }

        Interpreter interpreter = new Interpreter(launchInfo.tapeLength, view);

        for (String file : launchInfo.fileNames) {
            try {
                interpreter.interpret(readAllLines(file));
            } catch (IOException e) {
                System.out.println("Failed to read " + file);
                e.printStackTrace();
            }
        }
    }

    public static String readAllLines(String fileName) throws IOException {
        try (FileReader reader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            StringBuilder builder = new StringBuilder();
            while (bufferedReader.ready()) {
                builder.append(bufferedReader.readLine());
            }

            return builder.toString();
        }
    }

    public static LaunchInfo parseArgs(String[] args) throws ParseException {
        Option tapeLengthOption = new Option("l", "tapeLength", true,
                "Length of the tape");

        Option needToCompileOption = new Option("c", "compile", false,
                "Need to compile program");

        Option guiEnabledOption = new Option("g", "gui", false,
                "Enable graphical user interface");

        // interpreter.exe -s src1.bf src2.bf src3.bf ...
        Option sourcesOption = new Option("s", "sources", true,
                "1-20 files with source code");
        sourcesOption.setArgs(20);
        sourcesOption.setOptionalArg(true);

        Options options = new Options();
        options.addOption(tapeLengthOption);
        options.addOption(needToCompileOption);
        options.addOption(guiEnabledOption);
        options.addOption(sourcesOption);

        CommandLineParser cmdLineParser = new DefaultParser();
        CommandLine cmdLine;
        cmdLine = cmdLineParser.parse(options, args);

        int tapeLength = 30000;
        if (cmdLine.hasOption("l")) {
            try {
                tapeLength = Integer.parseInt(cmdLine.getOptionValue("l"));
                if (tapeLength < 1)
                    throw new NumberFormatException();
            }
            catch (NumberFormatException e) {
                System.out.println("Incorrect length of tape. It will be 30 000");
            }
        }

        String[] fileNames = new String[0];
        if (cmdLine.hasOption("s")) {
            fileNames = cmdLine.getOptionValues("s");
        }

        return new LaunchInfo(tapeLength, fileNames, cmdLine.hasOption("c"), cmdLine.hasOption("g"));
    }
}
