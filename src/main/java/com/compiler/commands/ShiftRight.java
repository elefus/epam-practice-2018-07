package com.compiler.commands;

public class ShiftRight implements Command {
    @Override
    public void execute(int numOfCommands) {

    }

    @Override
    public int optimize(String code, int currentCell) {
        int numOfCommands = 0;
        while (currentCell+numOfCommands < code.length() && code.charAt(currentCell+numOfCommands) == '>') {
            numOfCommands++;
        }
        return numOfCommands;
    }
}
