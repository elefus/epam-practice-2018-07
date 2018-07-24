package com.compiler.commands;

public interface Command {
    void execute(int numOfCommands);
    int optimize(String code, int currentCell);
}
