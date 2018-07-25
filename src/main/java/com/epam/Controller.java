package com.epam;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;


class Controller {

    private Model model;
    private View view;
    boolean type;

    Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }


    String getSource(String fileName) throws URISyntaxException, IOException {
        Path path = Paths.get(new File(fileName).getPath());

        if (Controller.class.getResource("./../../" + fileName) != null)
            path = Paths.get(Controller.class.getResource("./../../" + fileName).toURI());

        if (path == null)
            throw new FileNotFoundException("This file doesn't exist");

        try (InputStream in = new FileInputStream(path.toString());
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null)
                builder.append(line);

            return builder.toString();
        }
    }

    void interprete(String sourceCode, boolean isGui) throws IOException {
        int memPtr = 0;
        int count = 0;
        for (int i = 0; i < sourceCode.length(); i++)
            switch (sourceCode.charAt(i)) {

                case '>':
                    if (memPtr == (model.memorySize - 1)) {
                        memPtr = 0;
                    } else
                        memPtr++;
                    break;

                case '<':
                    if (memPtr == 0) {
                        memPtr = model.memorySize - 1;
                    } else
                        memPtr--;
                    break;

                case '+':
                    if (model.memory[memPtr] == 255) {
                        model.memory[memPtr] = 0;
                    } else
                        model.memory[memPtr]++;
                    break;

                case '-':
                    if (model.memory[memPtr] == 0) {
                        model.memory[memPtr] = 255;
                    } else
                        model.memory[memPtr]--;
                    break;

                case ',':
                    if (isGui)
                        model.memory[memPtr] = GuiView.enterParameter();
                    else
                        model.memory[memPtr] = view.readData();
                    break;

                case '.':
                    if (!isGui) view.printData(model.memory[memPtr]);
                    break;

                case '[':
                    if (model.memory[memPtr] == 0) {
                        i++;
                        while (count > 0 || sourceCode.charAt(i) != ']') {
                            if (sourceCode.charAt(i) == '[') count++;
                            if (sourceCode.charAt(i) == ']') count--;
                            i++;
                        }
                    }
                    break;

                case ']':
                    if (model.memory[memPtr] != 0) {
                        i--;
                        while (count > 0 || sourceCode.charAt(i) != '[') {
                            if (sourceCode.charAt(i) == ']') count++;
                            if (sourceCode.charAt(i) == '[') count--;
                            i--;
                        }
                        i--;
                    }
                    break;
            }
    }
}