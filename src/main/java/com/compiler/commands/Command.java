package com.compiler.commands;

public interface Command {
    int add(String code, int currentCell);
    void execute(int numOfCommands);
    int optimize(String code, int currentCell);
}
