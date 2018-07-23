package com.compiler;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;

public class Print implements Commands {

  @Override
  public InsnList execute(String val) {
    InsnList list = new InsnList();
    list.add(new MethodInsnNode(INVOKESTATIC,Compiler.className,"print","()V",false));
    return list;
  }
}
