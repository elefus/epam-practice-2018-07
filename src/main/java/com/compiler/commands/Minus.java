package com.compiler.commands;

import com.compiler.Main;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class Minus implements Command {
    @Override
    public void execute(int numOfCommands) {
        LabelNode labelNode = new LabelNode();
        Main.bytecode.add(new VarInsnNode(ALOAD,1));
        Main.bytecode.add(new VarInsnNode(ILOAD,2));
        Main.bytecode.add(new InsnNode(DUP2));
        Main.bytecode.add(new InsnNode(IALOAD));
        Main.pushNum(numOfCommands);
        Main.bytecode.add(new InsnNode(ISUB));
        Main.bytecode.add(new InsnNode(DUP));
        Main.bytecode.add(new JumpInsnNode(IFGE,labelNode));
        Main.pushNum(Main.maxValue+1);
        Main.bytecode.add(new InsnNode(IADD));
        Main.bytecode.add(labelNode);
        Main.bytecode.add(new InsnNode(IASTORE));
    }

    @Override
    public int optimize(String code, int currentCell) {
        int numOfCommands = 0;
        while (currentCell+numOfCommands < code.length() && code.charAt(currentCell+numOfCommands) == '-') {
            numOfCommands++;
        }
        return numOfCommands;
    }
}
