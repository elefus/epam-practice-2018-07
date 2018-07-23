package com.epam;

import java.io.*;

class Controller{

    private Model model;
    private View view;

    Controller(Model model, View view){
        this.model = model;
        this.view = view;
    }

    String getFileCode(String name) {

        StringBuilder code = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(name))){
            String line;
            while ((line = reader.readLine()) != null) {
                code.append(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return code.toString();
    }

    void interpreter(String code, boolean type) throws IOException {
        int index = 0;
        int count = 0;
        for(int i = 0; i < code.length(); i++)
            switch (code.charAt(i)){

                case '>':
                    if (index == (model.length - 1)){
                        index = 0;
                    }
                    else
                        index++;
                    break;

                case '<':
                    if (index == 0){
                        index = model.length - 1;
                    }
                    else
                        index--;
                    break;

                case '+':
                    if (model.date[index] == 255){
                        model.date[index] = 0;
                    }
                    else
                        model.date[index]++;
                    break;

                case '-':
                    if (model.date[index] == 0){
                        model.date[index] = 255;
                    }
                    else
                        model.date[index]--;
                    break;

                case ',':
                    model.date[index] = view.readCell();
                    break;

                case '.':
                    view.writeCell(model.date[index]);
                    break;

                case '[':
                    if (model.date[index] == 0) {
                        i++;
                        while (count > 0 || code.charAt(i) != ']') {
                            if (code.charAt(i) == '[') count++;
                            if (code.charAt(i) == ']') count--;
                            i++;
                        }
                    }
                    break;

                case ']':
                    if (model.date[index] != 0) {
                        i--;
                        while (count > 0 || code.charAt(i) != '[') {
                            if (code.charAt(i) == ']') count++;
                            if (code.charAt(i) == '[') count--;
                            i--;
                        }
                        i--;
                    }
                    break;
            }
    }
}
