package com.epam.compiler;

public class Token {

    public enum Operation {
        SHIFT,
        OUTPUT,
        INPUT,
        VALUE_CHANGE,
        CYCLE_START,
        CYCLE_END,
    }

    private int value = 1;
    private Operation operation;

    public Token(Operation operation) {
        this.operation = operation;
    }

    public Token(Operation operation, int value) {
        this.operation = operation;
        this.value = value;
    }

    public Operation getOperation() {
        return operation;
    }

    public int getValue() {
        return value;
    }
}