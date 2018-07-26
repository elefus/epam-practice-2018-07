package com.epam.compiler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.epam.interpeter.Controller;
import org.apache.commons.cli.*;

import static com.epam.compiler.CodeGeneration.generateByteCode;
import static com.epam.compiler.CodeGeneration.createTokens;

public class Compiler {

    public static void main(String[] args) {
        CommandLineParser cmdLineParser = new DefaultParser();
        CommandLine line;

        try {
            line = cmdLineParser.parse(getOptions(), args);
            if (line.hasOption("h")) {
                if (args.length == 1) {
                    HelpFormatter formatter = new HelpFormatter();
                    formatter.printHelp("compiler [-h] [-f <FILENAME>] [-o]", getOptions());
                } else {
                    throw new ParseException("Option -h must go without another options/arguments");
                }
            } else {
                String fileName;
                if (!line.hasOption("f"))
                    throw new ParseException("Missing required option -f");
                else
                    fileName = line.getOptionValue("f");

                int size = line.hasOption("s") ? Integer.parseInt(line.getOptionValue("s")): 30000;

                boolean optimize = line.hasOption("o");
                compile(fileName, size, optimize);
            }
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void compile(String fileName, int size, boolean optimize) throws IOException {
        ArrayList<Token> tokens = createTokens(getSourceCode(fileName));
        tokens = optimize ? optimize(tokens) : tokens;
        byte[] byteCode = generateByteCode(tokens, size);

        try (FileOutputStream output = new FileOutputStream(
                new File("./Created.class"))) {
            output.write(byteCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Options getOptions() {
        Option help = new Option("h", "help", false, "Know about options");
        Option file = new Option("f", "file", true, "Source file to compile");
        Option size = new Option("s", "size", true, "Size of cells array");
        Option optimize = new Option("o", "optimize", false, "Optimize compilation");

        Options options = new Options();
        options.addOption(file);
        options.addOption(optimize);
        options.addOption(help);
        return options;
    }

    private static char[] getSourceCode(String fileName) throws IOException {
        Path path = Paths.get(new File(fileName).getPath());

        if (Compiler.class.getResource("./../../../" + fileName) != null) {
            path = Paths.get(Controller.class.getResource("./../../../" + fileName).getPath());
        } else if (Files.notExists(path)) {
            throw new FileNotFoundException("This file doesn't exist");
        }

        StringBuilder builder = new StringBuilder();
        Files.readAllLines(path).forEach(builder::append);
        return builder.toString().toCharArray();
    }

    private static ArrayList<Token> optimizedTokens;

    public static ArrayList<Token> optimize(ArrayList<Token> tokens) {
        optimizedTokens = new ArrayList<>();

        for (Token token : tokens) {
            switch (token.getType()) {
                case R_BRACKET:
                    optimizedTokens.add(new Token(token));
                    break;

                case L_BRACKET:
                    checkIfZeroed(token);
                    break;

                default:
                    writeToken(token);
                    break;
            }
        }

        return new ArrayList<>(optimizedTokens);
    }

    private static void checkIfZeroed(Token token) {
        if (optimizedTokens.get(optimizedTokens.size() - 1).getType() == Type.ADD_VAL
                && optimizedTokens.get(optimizedTokens.size() - 2).getType() == Type.R_BRACKET) {
            optimizedTokens.remove(optimizedTokens.size() - 1);
            optimizedTokens.remove(optimizedTokens.size() - 1);
            optimizedTokens.add(new Token(Type.ZEROED));
        } else {
            optimizedTokens.add(new Token(token));
        }
    }

    private static void writeToken(Token token) {
        if (!optimizedTokens.isEmpty() && optimizedTokens.get(optimizedTokens.size() - 1).getType() == token.getType()) {
            optimizedTokens.get(optimizedTokens.size() - 1).incValue(token.getValue());
        } else {
            optimizedTokens.add(new Token(token));
        }
    }
}