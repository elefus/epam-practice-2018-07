package com.compiler;

import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.Opcodes;

public class Read implements Commands {

  @Override
  public InsnList execute(String val) {
    InsnList list = new InsnList();
    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,Compiler.className,"read","()V",false));
    return list;
  }
}
