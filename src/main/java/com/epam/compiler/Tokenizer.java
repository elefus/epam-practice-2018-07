package com.epam.compiler;

import java.util.ArrayList;

public class Tokenizer {

    public static ArrayList<Token> tokenize(String sourceCode) {
        if (sourceCode == null)
            throw new IllegalArgumentException("sourceCode argument was null");

        ArrayList<Token> result = new ArrayList<>();

        for (int i = 0; i < sourceCode.length(); i++) {
            switch (sourceCode.charAt(i)) {
                case '>':
                    result.add(new Token(TokenType.Shift, 1));
                    break;

                case '<':
                    result.add(new Token(TokenType.Shift, -1));
                    break;

                case '+':
                    result.add(new Token(TokenType.Add, 1));
                    break;

                case '-':
                    result.add(new Token(TokenType.Add, -1));
                    break;

                case '.':
                    result.add(new Token(TokenType.Output, 0));
                    break;

                case ',':
                    result.add(new Token(TokenType.Input, 0));
                    break;

                case '[':
                    result.add(new Token(TokenType.LoopStart, 0));
                    break;

                case ']':
                    result.add(new Token(TokenType.LoopEnd, 0));
                    break;
            }
        }

        return result;
    }

}
