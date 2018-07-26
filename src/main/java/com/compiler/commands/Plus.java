package com.compiler.commands;

import com.compiler.Compiler;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class Plus implements Command {

    @Override
    public int add(String code, int currentCell) {
        return 0;
    }

    @Override
    public void execute(int numOfCommands) {
        LabelNode labelNode = new LabelNode();
        Compiler.bytecode.add(new VarInsnNode(ALOAD,1));
        Compiler.bytecode.add(new VarInsnNode(ILOAD,2));
        Compiler.bytecode.add(new InsnNode(DUP2));
        Compiler.bytecode.add(new InsnNode(IALOAD));
        Compiler.pushNum(numOfCommands);
        Compiler.bytecode.add(new InsnNode(IADD));
        Compiler.bytecode.add(new InsnNode(DUP));
        Compiler.pushNum(Compiler.maxValue);
        Compiler.bytecode.add(new JumpInsnNode(IF_ICMPLE,labelNode));
        Compiler.pushNum(Compiler.maxValue+1);
        Compiler.bytecode.add(new InsnNode(ISUB));
        Compiler.bytecode.add(labelNode);
        Compiler.bytecode.add(new InsnNode(IASTORE));
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
