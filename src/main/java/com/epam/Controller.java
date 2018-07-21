package com.epam;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

class Controller {
    private View view;
    private Model model;
    private boolean isTrace;
    private char[] sourceCode;

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

    Controller(String fileName, Model model, View view, boolean isTrace) throws URISyntaxException, IOException {
        this.view = view;
        this.model = model;
        this.isTrace = isTrace;
        this.sourceCode = getSourceCode(fileName);
    }

    void process() throws IOException {
        for (int codeIndex = 0; codeIndex < sourceCode.length; codeIndex++) {
            if (isTrace)
                if (",.><+-[]".contains(Character.toString(sourceCode[codeIndex])))
                    view.traceCommand(model.getArrayIndex(), sourceCode[codeIndex], model.getCellValue());

            switch(sourceCode[codeIndex]) {
                case Tokens.INPUT :
                    model.setCellValue(view.inputData());
                    break;

                case Tokens.OUTPUT :
                    view.outputData(model.getCellValue());
                    break;

                case Tokens.FORWARD :
                    model.incArrayIndex();
                    break;

                case Tokens.BACKWARD :
                    model.decArrayIndex();
                    break;

                case Tokens.INCREMENT :
                    model.incCellValue();
                    break;

                case Tokens.DECREMENT :
                    model.decCellValue();
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

    private char[] getSourceCode(String fileName) throws URISyntaxException, IOException {
        Path path = Paths.get(new File(fileName).getPath());

        if (Controller.class.getResource("./../../" + fileName) != null)
            path = Paths.get(Controller.class.getResource("./../../" + fileName).toURI());

        if (path == null)
            throw new FileNotFoundException("This file doesn't exist");

        try(InputStream in = new FileInputStream(path.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            StringBuilder builder = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null)
                builder.append(line);

            return builder.toString().toCharArray();
        }
    }
}
