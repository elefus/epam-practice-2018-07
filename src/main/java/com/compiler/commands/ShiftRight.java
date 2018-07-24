package com.compiler.commands;

import com.compiler.Main;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class ShiftRight implements Command {
    @Override
    public void execute(int numOfCommands) {
        LabelNode labelNode = new LabelNode();
        Main.bytecode.add(new VarInsnNode(ILOAD,2));
        Main.pushNum(numOfCommands);
        Main.bytecode.add(new InsnNode(IADD));
        Main.bytecode.add(new InsnNode(DUP));
        Main.pushNum(Main.numOfCells);
        Main.bytecode.add(new JumpInsnNode(IF_ICMPLT,labelNode));
        Main.pushNum(Main.numOfCells);
        Main.bytecode.add(new InsnNode(ISUB));
        Main.bytecode.add(labelNode);
        Main.bytecode.add(new VarInsnNode(ISTORE,2));
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