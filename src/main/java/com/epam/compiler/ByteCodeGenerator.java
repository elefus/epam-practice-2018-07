package com.epam.compiler;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.*;
import static jdk.internal.org.objectweb.asm.Opcodes.*;

class ByteCodeGenerator {

  private static InsnList runInsn;

  private static Deque<LabelNode> whileStack = new ArrayDeque<>();

  public static byte[] createByteCode(List<Token> tokens) {
    ClassNode mainClass = new ClassNode();
    mainClass.version = V1_8;
    mainClass.access = ACC_PUBLIC + ACC_SUPER;
    mainClass.name = "TestClass";
    mainClass.superName = "java/lang/Object";
    mainClass.interfaces.add("java/lang/Runnable");

    MethodNode constructor = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
    InsnList constrInsn = constructor.instructions;
    constrInsn.add(new VarInsnNode(ALOAD, 0));
    constrInsn.add(new MethodInsnNode(INVOKESPECIAL, mainClass.superName, "<init>", "()V", false));
    constrInsn.add(new InsnNode(RETURN));
    mainClass.methods.add(constructor);

    MethodNode run = new MethodNode(ACC_PUBLIC, "run", "()V", null, null);
    runInsn = run.instructions;
    runInsn.add(new LdcInsnNode(30000));
    runInsn.add(new IntInsnNode(NEWARRAY, T_CHAR));
    runInsn.add(new VarInsnNode(ASTORE, 1));

    runInsn.add(new InsnNode(ICONST_0));
    runInsn.add(new VarInsnNode(ISTORE, 2));

    for (Token token : tokens) {
      switch (token.getType()) {
        case CHANGE_VAL:
          changeValue(token.val);
          break;

        case SHIFT:
          shift(token.val);
          break;

        case OUTPUT:
          printValue(token.val);
          break;

        case INPUT:
          readValue(token.val);
          break;

        case WHILE_START:
          whileStart();
          break;

        case WHILE_END:
          whileEnd();
          break;

        case SET_TO_ZERO:
          setToZero();
          break;
      }
    }

    runInsn.add(new InsnNode(RETURN));

    mainClass.methods.add(run);
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    mainClass.accept(writer);

    return writer.toByteArray();
  }

  private static void setToZero() {
    runInsn.add(new VarInsnNode(ALOAD, 1));
    runInsn.add(new VarInsnNode(ILOAD, 2));
    runInsn.add(new InsnNode(ICONST_0));
    runInsn.add(new InsnNode(CASTORE));
  }

  private static void whileEnd() {
    runInsn.add(new JumpInsnNode(GOTO, whileStack.pop()));
    runInsn.add(whileStack.pop());
  }

  private static void whileStart() {
    LabelNode begin = new LabelNode();
    LabelNode end = new LabelNode();

    whileStack.push(end);
    whileStack.push(begin);

    runInsn.add(begin);
    runInsn.add(new VarInsnNode(ALOAD, 1));
    runInsn.add(new VarInsnNode(ILOAD, 2));
    runInsn.add(new InsnNode(CALOAD));
    runInsn.add(new JumpInsnNode(IFEQ, end));
  }

  private static void readValue(int val) {
    for (int i = 0; i < val; i++) {
      runInsn.add(new VarInsnNode(ALOAD, 1));
      runInsn.add(new VarInsnNode(ILOAD, 2));
      runInsn.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;"));
      runInsn.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/InputStream", "read", "()I", false));
      runInsn.add(new InsnNode(CASTORE));
    }
  }

  private static void printValue(int val) {
    for (int i = 0; i < val; i++) {
      runInsn.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
      runInsn.add(new VarInsnNode(ALOAD, 1));
      runInsn.add(new VarInsnNode(ILOAD, 2));
      runInsn.add(new InsnNode(CALOAD));
      runInsn.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(C)V", false));
    }
  }

  private static void shift(int val) {
    runInsn.add(new IincInsnNode(2, val));
  }

  private static void changeValue(int val) {
    runInsn.add(new VarInsnNode(ALOAD, 1));
    runInsn.add(new VarInsnNode(ILOAD, 2));
    runInsn.add(new InsnNode(DUP2));
    runInsn.add(new InsnNode(CALOAD));
    runInsn.add(new LdcInsnNode(val));
    runInsn.add(new InsnNode(IADD));
    runInsn.add(new InsnNode(CASTORE));
  }
}
