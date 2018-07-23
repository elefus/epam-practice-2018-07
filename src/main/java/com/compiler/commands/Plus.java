package com.compiler.commands;

public class Plus implements Command {
    @Override
    public void execute(int numOfCommands) {
//        Main.bytecode.add(new VarInsnNode(ALOAD,1));

    }

    @Override
    public int optimize(String code, int currentCell) {
        int numOfCommands = 0;
        while (currentCell+numOfCommands < code.length() && code.charAt(currentCell+numOfCommands) == '+') {
            numOfCommands++;
        }
        return numOfCommands;
    }
}
