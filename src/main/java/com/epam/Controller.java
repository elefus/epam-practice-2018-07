package com.epam;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import static java.util.stream.Collectors.joining;

public class Controller {
    private final View view;
    private final Model model;
    private final boolean isTrace;
    private final char[] sourceCode;


    public Controller(String fileName, Model model, View view, boolean isTrace) throws IOException, URISyntaxException {
        this.view = view;
        this.model = model;
        this.isTrace = isTrace;
        sourceCode = getSourceCode(fileName);
    }

    public void process() throws IOException {
        int count = 0;
        for (int prgPtr = 0; prgPtr < sourceCode.length; prgPtr++) {
            if (isTrace)
                if (",.><+-[]".contains(Character.toString(sourceCode[prgPtr])))
                    view.traceCommand(model.getPointer(), sourceCode[prgPtr], model.getValue());

            switch (sourceCode[prgPtr]) {
                case ',':
                    int sym = view.inputData();
                    model.setValue(sym);
                    break;

                case '.':
                    view.outputData((char) model.getValue());
                    break;

                case '>':
                    model.shiftRight();
                    break;

                case '<':
                    model.shiftLeft();
                    break;

                case '+':
                    model.inc();
                    break;

                case '-':
                    model.dec();
                    break;

                case '[':
                    if (model.getValue() == 0)
                        prgPtr++;
                    while (count != 0 || sourceCode[prgPtr] != ']') {
                        if (sourceCode[prgPtr] == '[') {
                            count++;
                        }
                        if (sourceCode[prgPtr] == ']') {
                            count--;
                        }
                    }
                    break;

                case ']':
                    if (model.getValue() != 0)
                        prgPtr--;

                    while (sourceCode[prgPtr] != '[' || count > 0) {
                        if (sourceCode[prgPtr] == ']')
                            count++;
                        else if (sourceCode[prgPtr] == '[')
                            count--;
                        prgPtr--;
                    }

                    prgPtr--;
                    break;

                default:
            }
        }
    }


    private static char[] getSourceCode(String fileName) throws IOException, URISyntaxException {

       String code;

        if (Controller.class.getResource("./../../" + fileName) == null) {
            throw new FileNotFoundException("File not found");
        }
        code = Files.lines(Paths.get(Controller.class.getResource("./../../" + fileName).toURI()))
                .collect(joining());
        return code.toString().toCharArray();
    }
}
