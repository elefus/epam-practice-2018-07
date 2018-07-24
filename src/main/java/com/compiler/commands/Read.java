package com.compiler.commands;

import com.compiler.Compiler;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.Opcodes;

public class Read implements Command {

  @Override
  public InsnList execute(String val) {
    int num = Integer.parseInt(val);
    InsnList list = new InsnList();
    for (int i = 0; i < num; i++) {
      list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Compiler.className, "read", "()V", false));
    }
    return list;
  }
}
