package com.compiler;

import static jdk.internal.org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;

public class ChangeIdx implements Commands {

  @Override
  public InsnList execute(String val) {
    int number = Integer.parseInt(val);
    boolean backwards = false;
    if (number < 0) {
      backwards = true;
      number = Math.abs(number);
    }
    InsnList list = new InsnList();
    Parser.pushRightType(number, list);
    if (backwards) {
      list.add(new InsnNode(ICONST_0));
    } else {
      list.add(new InsnNode(ICONST_1));
    }

    list.add(new MethodInsnNode(INVOKESTATIC, Compiler.className, "changeIdx", "(IZ)V", false));
    return list;
  }
}
