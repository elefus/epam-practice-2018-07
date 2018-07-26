package com.epam.compiler;

import com.epam.Interpreter.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Preparing {

    public static char[] processFile(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream in = Controller.class.getResourceAsStream("./../../../" + fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cleanProgram(stringBuilder.toString().toCharArray());
    }

    public static char[] cleanProgram(char[] chars) {
        char[] temp = new char[chars.length];
        int pos = 0;
        for (int i = 0; i < chars.length; ++i) {
            switch (chars[i]) {
                case Compiler.Command.SHIFT_RIGHT: {
                    temp[pos++] = Compiler.Command.SHIFT_RIGHT;
                    break;
                }
                case Compiler.Command.SHIFT_LEFT: {
                    temp[pos++] = Compiler.Command.SHIFT_LEFT;
                    break;
                }

                case Compiler.Command.OUTPUT: {
                    temp[pos++] = Compiler.Command.OUTPUT;
                    break;
                }
                case Compiler.Command.INPUT: {
                    temp[pos++] = Compiler.Command.INPUT;
                    break;
                }

                case Compiler.Command.PLUS: {
                    temp[pos++] = Compiler.Command.PLUS;
                    break;
                }
                case Compiler.Command.MINUS: {
                    temp[pos++] = Compiler.Command.MINUS;
                    break;
                }

                case Compiler.Command.BRACKET_LEFT: {
                    temp[pos++] = Compiler.Command.BRACKET_LEFT;
                    break;
                }
                case Compiler.Command.BRACKET_RIGHT: {
                    temp[pos++] = Compiler.Command.BRACKET_RIGHT;
                    break;
                }
                default:
            }
        }
        char[] program = new char[pos];
        System.arraycopy(temp, 0, program, 0, pos);
        return program;
    }
}
