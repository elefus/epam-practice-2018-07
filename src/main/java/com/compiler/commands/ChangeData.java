package com.compiler.commands;


import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import com.compiler.Compiler;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;

public class ChangeData implements Command {

  @Override
  public InsnList execute(String val) {
    int number = Integer.parseInt(val);
    InsnList list = new InsnList();
    Compiler.pushRightType(number, list);
    list.add(new MethodInsnNode(INVOKESTATIC, Compiler.className, "changeData", "(I)V", false));
    return list;
  }
}
