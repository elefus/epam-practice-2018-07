package com.epam.compiler;

public class Token {
    public final TokenType type;
    public final int parameter;

    public Token(TokenType type, int parameter) {
        this.type = type;
        this.parameter = parameter;
    }
}
