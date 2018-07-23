package com.compiler.commands;

import static jdk.internal.org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import com.compiler.Compiler;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;

public class ChangeData implements Command {

  @Override
  public InsnList execute(String val) {
    int number = Integer.parseInt(val);
    boolean backwards = false;
    if (number < 0){
      backwards = true;
      number = Math.abs(number);
    }
    InsnList list = new InsnList();
    Compiler.pushRightType(number,list);
    if(backwards){
      list.add(new InsnNode(ICONST_1));
    }else{
      list.add(new InsnNode(ICONST_0));
    }
    list.add(new MethodInsnNode(INVOKESTATIC,Compiler.className,"changeData","(IZ)V",false));
    return list;
  }
}
