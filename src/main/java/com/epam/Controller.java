package com.epam;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.io.FileNotFoundException;

public class Controller {
    private final View view;
    private final Model model;
    private final boolean isTrace;
    private final char[] sourceCode;

    private class Tokens {
        static final char INPUT = ',';
        static final char OUTPUT = '.';
        static final char FORWARD = '>';
        static final char BACKWARD = '<';
        static final char INCREMENT = '+';
        static final char DECREMENT = '-';
        static final char L_BRACKET = '[';
        static final char R_BRACKET = ']';
    }

    public Controller(String fileName, Model model, View view, boolean isTrace) throws IOException {
        this.view = view;
        this.model = model;
        this.isTrace = isTrace;
        sourceCode = getSourceCode(fileName);
    }

    public void process() throws IOException {
        for (int codeIndex = 0; codeIndex < sourceCode.length; codeIndex++) {
            if (isTrace)
                if (",.><+-[]".contains(Character.toString(sourceCode[codeIndex])))
                    view.traceCommand(model.getCellIndex(), sourceCode[codeIndex], model.getCellValue());

            switch(sourceCode[codeIndex]) {
                case Tokens.INPUT :
                    model.setCellValue(view.inputData());
                    break;

                case Tokens.OUTPUT :
                    view.outputData(model.getCellValue());
                    break;

                case Tokens.FORWARD :
                    model.incrementCellIndex();
                    break;

                case Tokens.BACKWARD :
                    model.decrementCellIndex();
                    break;

                case Tokens.INCREMENT :
                    model.incrementCellValue();
                    break;

                case Tokens.DECREMENT :
                    model.decrementCellValue();
                    break;

                case Tokens.L_BRACKET :
                    if (model.getCellValue() == 0)
                        codeIndex = getNewIndex(codeIndex, true);
                    break;

                case Tokens.R_BRACKET :
                    if (model.getCellValue() != 0)
                        codeIndex = getNewIndex(codeIndex, false);
                    break;

                default:
            }
        }
    }

    private int getNewIndex(int codeIndex, boolean isLeftBracket) {
        int bracketsCounter = 0;

        if (isLeftBracket) {
            codeIndex++;

            while(sourceCode[codeIndex] != Tokens.R_BRACKET || bracketsCounter > 0) {
                if(sourceCode[codeIndex] == Tokens.L_BRACKET)
                    bracketsCounter++;
                else if(sourceCode[codeIndex] == Tokens.R_BRACKET)
                    bracketsCounter--;

                codeIndex++;
            }
        } else {
            codeIndex--;

            while (sourceCode[codeIndex] != Tokens.L_BRACKET || bracketsCounter > 0) {
                if (sourceCode[codeIndex] == Tokens.R_BRACKET)
                    bracketsCounter++;
                else if (sourceCode[codeIndex] == Tokens.L_BRACKET)
                    bracketsCounter--;

                codeIndex--;
            }

            codeIndex--;
        }

        return codeIndex;
    }

    private static char[] getSourceCode(String fileName) throws IOException {
        Path path = Paths.get(new File(fileName).getPath());

        if (Controller.class.getResource("./../../" + fileName) != null) {
            path = Paths.get(Controller.class.getResource("./../../" + fileName).getPath());
        } else if (Files.notExists(path)) {
            throw new FileNotFoundException("This file doesn't exist");
        }

        StringBuilder builder = new StringBuilder();
        Files.readAllLines(path).forEach(builder::append);
        return builder.toString().toCharArray();
    }
}
