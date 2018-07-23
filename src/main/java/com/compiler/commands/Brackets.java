package com.compiler.commands;


import static jdk.internal.org.objectweb.asm.Opcodes.IALOAD;
import static jdk.internal.org.objectweb.asm.Opcodes.IFEQ;
import static jdk.internal.org.objectweb.asm.Opcodes.IFNE;

import com.compiler.Compiler;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;

public class Brackets implements Command {
  class Pairs{
    Pairs(LabelNode l1,LabelNode l2){
      firstBracket = l1;
      secondBracket = l2;
    }
    public LabelNode firstBracket;
    public LabelNode secondBracket;

  }
  public static int currentBracket = 0;
  public static Map<Integer,Pairs> pairsMap = new HashMap<>();
  @Override
  public InsnList execute(String val) {
    int number = Integer.parseInt(val);
    Pairs node;
    InsnList bytecode = new InsnList();
    bytecode.add(new FieldInsnNode(Opcodes.GETSTATIC,Compiler.className,"array","[I"));
    bytecode.add(new FieldInsnNode(Opcodes.GETSTATIC,Compiler.className,"idx","I"));
    bytecode.add(new InsnNode(IALOAD));
    if(number > 0){
      currentBracket++;
      pairsMap.put(currentBracket,new Pairs(new LabelNode(), new LabelNode()));
      node = pairsMap.get(currentBracket);
      bytecode.add(new JumpInsnNode(IFEQ,node.secondBracket));
      bytecode.add(node.firstBracket);
    }else {
      node = pairsMap.get(currentBracket);
      bytecode.add(new JumpInsnNode(IFNE,node.firstBracket));
      bytecode.add(node.secondBracket);
      currentBracket--;
    }


    return bytecode;
  }
}
