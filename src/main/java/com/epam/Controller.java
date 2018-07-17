package com.epam;

import java.io.*;

public class Controller extends Model {

    public static String getFileCode(String name) {

        String code = "";

        try (InputStream input = Controller.class.getResourceAsStream("./../../" + name);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))){
            String line = "";
            while ((line = reader.readLine()) != null) {
                code += line;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return code;
    }

    public static void interpreter(String code) throws IOException {
        int index = 0;
        int count = 0;
        for(int i = 0; i < code.length(); i++)
            switch (code.charAt(i)){
                case '>':
                    if (index == 29999){
                        index = 0;
                    }
                    else
                        index++;
                    break;
                case '<':
                    if (index == 0){
                        index = 29999;
                    }
                    else
                        index--;
                    break;
                case '+':
                    if (date[index] == 255){
                        date[index] = 0;
                    }
                    else
                        date[index]++;
                    break;
                case '-':
                    if (date[index] == 0){
                        date[index] = 255;
                    }
                    else
                        date[index]--;
                    break;
                case ',':
                        date[index] = (char) new InputStreamReader(System.in).read();
                    break;
                case '.':
                    System.out.println(date[index]);
                    break;
                case '[':
                    if (date[index] == 0) {
                        i++;
                        while (count > 0 || code.charAt(i) != ']') {
                            if (code.charAt(i) == '[') count++;
                            if (code.charAt(i) == ']') count--;
                            i++;
                        }
                    }
                    break;
                case ']':
                    if (date[index] != 0) {
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
