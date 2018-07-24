package com.compiler.commands;

import com.compiler.Main;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class Read implements Command {

    public static boolean used = false;

    @Override
    public void execute(int numOfCommands) {
        for (int i = 0; i < numOfCommands; i++) {
            Main.bytecode.add(new VarInsnNode(ALOAD,1));
            Main.bytecode.add(new VarInsnNode(ILOAD,2));
            Main.bytecode.add(new VarInsnNode(ALOAD,3));
            Main.bytecode.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/InputStreamReader", "read", "()I", false));
            Main.bytecode.add(new InsnNode(IASTORE));
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
