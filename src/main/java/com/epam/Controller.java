package com.epam;

import com.epam.View.ConsoleView;
import com.epam.View.View;

import java.io.*;

public class Controller {

    private static Interpreter interpreter;

    public static void main(String[] args) {
        View consoleView = new ConsoleView(args);
        Model model = new Model(consoleView.getCellSize());
        interpreter = new Interpreter(model, consoleView);
        interpretFiles(consoleView.getFiles());
    }

    public static void interpretFiles(String[] files) {
        for (String file : files) {
            try {
                interpreter.interpret(readSourceFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readSourceFile(String file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}

class Interpreter {
    Model model;
    View view;

    Interpreter(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void interpret(String source){
        int bracketCounter = 0;
        for (int i = 0; i < source.length(); i++) {
            switch (source.charAt(i)) {
                case '>':
                    model.incrementIndex();
                    break;
                case '<':
                    model.decrementIndex();
                    break;
                case '+':
                    model.incrementValue();
                    break;
                case '-':
                    model.decrementValue();
                    break;
                case ',':
                    model.setValue(view.readChar());
                    break;
                case '.':
                    view.printChar(model.getValue());
                    break;
                case '[':
                    if (model.getValue() == 0) {
                        i++;
                        while (bracketCounter > 0 || source.charAt(i) != ']') {
                            if (source.charAt(i) == '[') bracketCounter++;
                            if (source.charAt(i) == ']') bracketCounter--;
                            i++;
                        }
                    }
                    break;
                case ']':
                    if (model.getValue() != 0) {
                        i--;
                        while (bracketCounter > 0 || source.charAt(i) != '[') {
                            if (source.charAt(i) == ']') bracketCounter++;
                            if (source.charAt(i) == '[') bracketCounter--;
                            i--;
                        }
                        i--;
                    }
                    break;
            }
        }
    }
}