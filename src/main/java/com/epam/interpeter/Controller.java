package com.epam.interpeter;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.joining;

public class Controller {
    private static boolean isStop;
    private static boolean isPause;
    private final View view;
    private final Model model;
    private final boolean isTrace;
    private final char[] sourceCode;

    public static void setStop(boolean stop) {
        isStop = stop;
    }

    public static void setPause(boolean pause) {
        isPause = pause;
    }

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

    public Controller(String sourceCode, Model model, View view, boolean isTrace) {
        this.view = view;
        this.model = model;
        this.isTrace = isTrace;
        this.sourceCode = sourceCode.toCharArray();
    }

    public static String getSourceCode(String fileName) throws IOException {
        Path path = Paths.get(new File(fileName).getPath());

        if (Controller.class.getResource("./../../../" + fileName) != null) {
            try {
                path = Paths.get(Controller.class.getResource("./../../../" + fileName).toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (Files.notExists(path)) {
            throw new FileNotFoundException("This file doesn't exist");
        }

        return Files.lines(Paths.get(path.toUri())).collect(joining(System.lineSeparator()));
    }

    public void process() throws IOException {
        for (int codeIndex = 0; codeIndex < sourceCode.length; codeIndex++) {
            if (!isStop && !isPause) {
                if (isTrace)
                    if (",.><+-[]".contains(Character.toString(sourceCode[codeIndex])))
                        view.traceCommand(model.getCellsArray(), model.getCellIndex(), sourceCode[codeIndex]);

                switch (sourceCode[codeIndex]) {
                    case Tokens.INPUT:
                        model.setCellValue(view.inputData());
                        break;

                    case Tokens.OUTPUT:
                        view.outputData(model.getCellValue());
                        break;

                    case Tokens.FORWARD:
                        model.incrementCellIndex();
                        break;

                    case Tokens.BACKWARD:
                        model.decrementCellIndex();
                        break;

                    case Tokens.INCREMENT:
                        model.incrementCellValue();
                        break;

                    case Tokens.DECREMENT:
                        model.decrementCellValue();
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
            } else if (isPause) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else /*if (isStop)*/ {
                break;
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
}
