package com.epam;

import java.io.*;

class Controller
{
    Controller(boolean isTrace, String fileName, Model model, View view) {
        this.view = view;
        this.model = model;
        this.isTrace = isTrace;
        getCodeArray(fileName);
    }

    private void getCodeArray(String fileName) {
        try (InputStreamReader inp = new InputStreamReader(Input.class.getResourceAsStream("./../../" + fileName));
             BufferedReader reader = new BufferedReader(inp)) {
            String new_line;
            StringBuilder builder = new StringBuilder();

            while ((new_line = reader.readLine()) != null)
                builder.append(new_line);

            this.codeArray = builder.toString().toCharArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processCommands() throws IOException {
        for (int codeIndex = 0; codeIndex < codeArray.length; codeIndex++) {
            switch (codeArray[codeIndex]) {
                case Tokens.FORWARD:
                    model.incrementIndex();
                    break;

                case Tokens.BACKWARD:
                    model.decrementIndex();
                    break;

                case Tokens.INCREMENT:
                    model.incrementCellValue();
                    break;

                case Tokens.DECREMENT:
                    model.decrementCellValue();
                    break;

                case Tokens.INPUT:
                    model.setCellValue(view.inputData());
                    break;

                case Tokens.OUTPUT:
                    view.outputData(model.getCellValue());
                    break;

                case Tokens.L_BRACKET:
                    if (model.getCellValue() == 0)
                        codeIndex = getNewIndex(codeIndex, true);
                    break;

                case Tokens.R_BRACKET:
                    if (model.getCellValue() != 0)
                        codeIndex = getNewIndex(codeIndex, false);
                    break;

                default:
            }

            if(isTrace)
            {
                if("+-<>,.[]".contains(Character.toString(codeArray[codeIndex])))
                    view.traceOperation(model.getCellIndex(), codeArray[codeIndex], model.getCellValue());
            }
        }
    }

    private int getNewIndex(int codeIndex, boolean leftBracket) {
        int bracketsCounter = 0;

        if(leftBracket) {
            codeIndex++;

            while(codeArray[codeIndex] != Tokens.R_BRACKET || bracketsCounter > 0) {
                if(codeArray[codeIndex] == Tokens.L_BRACKET)
                    bracketsCounter++;
                else if(codeArray[codeIndex] == Tokens.R_BRACKET)
                    bracketsCounter--;

                codeIndex++;
            }
        } else {
            codeIndex--;

            while (codeArray[codeIndex] != Tokens.L_BRACKET || bracketsCounter > 0) {
                if (codeArray[codeIndex] == Tokens.R_BRACKET)
                    bracketsCounter++;
                else if (codeArray[codeIndex] == Tokens.L_BRACKET)
                    bracketsCounter--;

                codeIndex--;
            }

            codeIndex--;
        }

        return codeIndex;
    }

    private class Tokens {
        private static final char INPUT     = ',';
        private static final char OUTPUT    = '.';
        private static final char FORWARD   = '>';
        private static final char BACKWARD  = '<';
        private static final char INCREMENT = '+';
        private static final char DECREMENT = '-';
        private static final char R_BRACKET = ']';
        private static final char L_BRACKET = '[';
    }

    private View view;
    private Model model;
    private boolean isTrace;
    private char[] codeArray;
}
