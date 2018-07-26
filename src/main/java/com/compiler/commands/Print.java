package com.compiler.commands;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import com.compiler.Compiler;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;

public class Print implements Command {

  @Override
  public InsnList execute(String val) {
    int number = Integer.parseInt(val);
    InsnList list = new InsnList();
    for (int i = 0; i < number; i++) {
      list.add(new MethodInsnNode(INVOKESTATIC, Compiler.className, "print", "()V", false));
    }
    return list;
  }
}
