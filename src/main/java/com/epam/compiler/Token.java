package com.epam.compiler;

public class Token {
    private Type type;
    private int value;

    public Token(Type type) {
        this.type = type;
        this.value = 1;
    }

    public Token(Token token) {
        this.type = token.getType();
        this.value = token.getValue();
    }

    public Token(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public void incValue(int value) {
        this.value += value;
    }
}
