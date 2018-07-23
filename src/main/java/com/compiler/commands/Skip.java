package com.compiler.commands;

public class Skip implements Command {
    @Override
    public void execute(int numOfCommands) {

    }

    @Override
    public int optimize(String code, int currentCell) {
        return 0;
    }
}
