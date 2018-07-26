package com.compiler.commands;

import com.compiler.Compiler;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class Read implements Command {

    @Override
    public int add(String code, int currentCell) {
        return 0;
    }

    public static boolean used = false;

    @Override
    public void execute(int numOfCommands) {
        for (int i = 0; i < numOfCommands; i++) {
            Compiler.bytecode.add(new VarInsnNode(ALOAD,1));
            Compiler.bytecode.add(new VarInsnNode(ILOAD,2));
            Compiler.bytecode.add(new VarInsnNode(ALOAD,3));
            Compiler.bytecode.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/InputStreamReader", "read", "()I", false));
            Compiler.bytecode.add(new InsnNode(IASTORE));
        }
    }

    @Override
    public int optimize(String code, int currentCell) {
        used = true;
        int numOfCommands = 0;
        while (currentCell+numOfCommands < code.length() && code.charAt(currentCell+numOfCommands) == ',') {
            numOfCommands++;
        }
        return numOfCommands;
    }
}
