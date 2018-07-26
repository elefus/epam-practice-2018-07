package com.compiler.commands;

import com.compiler.Compiler;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class Write implements Command {

    @Override
    public int add(String code, int currentCell) {
        return 0;
    }

    @Override
    public void execute(int numOfCommands) {
        for (int i = 0; i < numOfCommands; i++) {
            Compiler.bytecode.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
            Compiler.bytecode.add(new VarInsnNode(ALOAD,1));
            Compiler.bytecode.add(new VarInsnNode(ILOAD,2));
            Compiler.bytecode.add(new InsnNode(IALOAD));
            Compiler.bytecode.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(C)V", false));
        }
    }

    @Override
    public int optimize(String code, int currentCell) {
        int numOfCommands = 0;
        while (currentCell+numOfCommands < code.length() && code.charAt(currentCell+numOfCommands) == '.') {
            numOfCommands++;
        }
        return numOfCommands;
    }
}
