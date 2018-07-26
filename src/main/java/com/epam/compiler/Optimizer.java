package com.epam.compiler;

import java.util.List;

public class Optimizer {

    public static void optimize(List<Token> tokens) {

        for (int i = 0; i < tokens.size() - 2; i++) {
            Token previous = tokens.get(i);
            Token current = tokens.get(i + 1);
            Token next = tokens.get(i + 2);

            if (previous.type == TokenType.LoopStart
                    && current.type == TokenType.Add
                    && next.type == TokenType.LoopEnd) {
                tokens.add(i, new Token(TokenType.SetToZero, 0));
                tokens.remove(i + 1);
                tokens.remove(i + 1);
                tokens.remove(i + 1);
            }
        }

        for (int i = 0; i < tokens.size() - 1; i++) {
            Token current = tokens.get(i);

            if (current.type == TokenType.Shift
                    || current.type == TokenType.Add
                    || current.type == TokenType.SetToZero) {
                Token next = tokens.get(i + 1);

                if (current.type == next.type) {
                    int newParameter = current.parameter + next.parameter;

                    if (newParameter != 0 || current.type == TokenType.SetToZero) {
                        tokens.add(i, new Token(current.type, newParameter));
                        tokens.remove(i + 1);
                        tokens.remove(i + 1);
                    } else {
                        tokens.remove(i);
                        tokens.remove(i);
                    }

                    i--;
                }
            }
        }

    }

}
