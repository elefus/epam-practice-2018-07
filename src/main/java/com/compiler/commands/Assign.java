package com.compiler.commands;

import com.compiler.Compiler;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class Assign implements Command {
    @Override
    public int add(String code, int currentCell) {
        return -1;
    }

    @Override
    public void execute(int numOfCommands) {
        Compiler.bytecode.add(new VarInsnNode(ALOAD,1));
        Compiler.bytecode.add(new VarInsnNode(ILOAD,2));
        Compiler.pushNum(numOfCommands);
        Compiler.bytecode.add(new InsnNode(IASTORE));
    }

    @Override
    public int optimize(String code, int currentCell) {
        return 0;
    }
}
