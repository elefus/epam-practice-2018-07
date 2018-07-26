package com.compiler.commands;

public class Skip implements Command {

    @Override
    public int add(String code, int currentCell) {
        return -1;
    }

    @Override
    public void execute(int numOfCommands) {

    }

    @Override
    public int optimize(String code, int currentCell) {
        return 0;
    }
}
