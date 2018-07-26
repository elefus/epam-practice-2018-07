package com.compiler.commands;

import com.compiler.Compiler;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class LeftBracket implements Command {

    @Override
    public int add(String code, int currentCell) {
        return 0;
    }

    @Override
    public void execute(int numOfCommands) {
        LabelNode labelNode1 = new LabelNode();
        Compiler.leftBrackets.add(labelNode1);
        LabelNode labelNode2 = new LabelNode();
        Compiler.rightBrackets.add(labelNode2);

        Compiler.bytecode.add(Compiler.leftBrackets.get(Compiler.currentBracket));
        Compiler.bytecode.add(new VarInsnNode(ALOAD,1));
        Compiler.bytecode.add(new VarInsnNode(ILOAD,2));
        Compiler.bytecode.add(new InsnNode(IALOAD));

        Compiler.bytecode.add(new JumpInsnNode(IFEQ,Compiler.rightBrackets.get(Compiler.currentBracket)));
        Compiler.currentBracket++;
    }

    @Override
    public int optimize(String code, int currentCell) {
        return 1;
    }
}
