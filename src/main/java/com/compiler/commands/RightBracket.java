package com.compiler.commands;

public class RightBracket implements Command {
    @Override
    public void execute(int numOfCommands) {

    }

    @Override
    public int optimize(String code, int currentCell) {
        return 1;
    }
}
