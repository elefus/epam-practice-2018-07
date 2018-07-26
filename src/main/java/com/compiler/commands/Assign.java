package com.compiler.commands;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import com.compiler.Compiler;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;


public class Assign implements Command {

  @Override
  public InsnList execute(String val) {
    int num = Integer.parseInt(val);
    InsnList list = new InsnList();
    Compiler.pushRightType(num, list);
    list.add(new MethodInsnNode(INVOKESTATIC, Compiler.className, "set", "(I)V", false));
    return list;
  }
}
