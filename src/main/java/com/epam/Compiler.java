package com.epam;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import java.io.*;
import java.util.Stack;
import static jdk.internal.org.objectweb.asm.Opcodes.*;

class Compiler {

    private ClassNode myClass;
    private MethodNode runMethod;
    private InsnList command;

    Compiler() {
        myClass = new ClassNode();
        myClass.version = Opcodes.V1_8;
        myClass.access = ACC_PUBLIC;
        myClass.name = "MyClass";
        myClass.superName = "java/lang/Object";
        myClass.interfaces.add("java/lang/Runnable");

        MethodNode constructor = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
        InsnList bytecode = constructor.instructions;
        bytecode.add(new VarInsnNode(ALOAD, 0));
        bytecode.add(new MethodInsnNode(INVOKESPECIAL, myClass.superName, "<init>", "()V", false));
        bytecode.add(new InsnNode(RETURN));
        myClass.methods.add(constructor);

        runMethod = new MethodNode(ACC_PUBLIC, "run", "()V", null, null);
        myClass.methods.add(runMethod);

        command = runMethod.instructions;
        command.add(new LdcInsnNode(30000));
        command.add(new VarInsnNode(NEWARRAY, T_CHAR));
        command.add(new VarInsnNode(ASTORE, 1));
        command.add(new InsnNode(ICONST_0));
        command.add(new VarInsnNode(ISTORE, 2));

    }

    void compile(String сode) throws IOException {

        Stack<LabelNode> labl = new Stack<>();

        for (int i = 0; i < сode.length(); i++) {
            LabelNode label = new LabelNode();
            switch (сode.charAt(i)) {
                case '>':
                    command.add(new IincInsnNode(2, 1));
                    command.add(new VarInsnNode(ILOAD, 2));
                    command.add(new VarInsnNode(ALOAD, 1));
                    command.add(new InsnNode(ARRAYLENGTH));
                    command.add(new JumpInsnNode(IF_ICMPNE, label));
                    command.add(new InsnNode(ICONST_0));
                    command.add(new VarInsnNode(ISTORE, 2));
                    command.add(label);
                    break;

                case '<':
                    command.add(new IincInsnNode(2, -1));
                    command.add(new VarInsnNode(ILOAD, 2));
                    command.add(new InsnNode(ICONST_M1));
                    command.add(new JumpInsnNode(IF_ICMPNE, label));
                    command.add(new VarInsnNode(ALOAD, 1));
                    command.add(new InsnNode(ARRAYLENGTH));
                    command.add(new InsnNode(ICONST_1));
                    command.add(new InsnNode(ISUB));
                    command.add(new VarInsnNode(ISTORE, 2));
                    command.add(label);
                    break;

                case '+':
                    command.add(new VarInsnNode(ALOAD, 1));
                    command.add(new VarInsnNode(ILOAD, 2));
                    command.add(new InsnNode(DUP2));
                    command.add(new InsnNode(CALOAD));
                    command.add(new InsnNode(ICONST_1));
                    command.add(new InsnNode(IADD));
                    command.add(new InsnNode(I2C));
                    command.add(new InsnNode(CASTORE));

                    command.add(new VarInsnNode(ALOAD, 1));
                    command.add(new VarInsnNode(ILOAD, 2));
                    command.add(new InsnNode(CALOAD));
                    command.add(new LdcInsnNode(256));
                    command.add(new JumpInsnNode(IF_ICMPNE, label));
                    command.add(new VarInsnNode(ALOAD, 1));
                    command.add(new VarInsnNode(ILOAD, 2));
                    command.add(new InsnNode(ICONST_0));
                    command.add(new InsnNode(CASTORE));
                    command.add(label);
                    break;

                case '-':
                    command.add(new VarInsnNode(ALOAD, 1));
                    command.add(new VarInsnNode(ILOAD, 2));
                    command.add(new InsnNode(DUP2));
                    command.add(new InsnNode(CALOAD));
                    command.add(new InsnNode(ICONST_M1));
                    command.add(new InsnNode(IADD));
                    command.add(new InsnNode(I2C));
                    command.add(new InsnNode(CASTORE));

                    command.add(new VarInsnNode(ALOAD, 1));
                    command.add(new VarInsnNode(ILOAD, 2));
                    command.add(new InsnNode(CALOAD));
                    command.add(new LdcInsnNode(65535));
                    command.add(new JumpInsnNode(IF_ICMPNE, label));
                    command.add(new VarInsnNode(ALOAD, 1));
                    command.add(new VarInsnNode(ILOAD, 2));
                    command.add(new LdcInsnNode(255));
                    command.add(new InsnNode(CASTORE));
                    command.add(label);
                    break;

                case '.':
                    command.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                    command.add(new VarInsnNode(ALOAD, 1));
                    command.add(new VarInsnNode(ILOAD, 2));
                    command.add(new InsnNode(CALOAD));
                    command.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(C)V", false));
                    break;

                case ',':
                    command.add(new VarInsnNode(ALOAD, 1));
                    command.add(new VarInsnNode(ILOAD, 2));
                    command.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;"));
                    command.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/InputStream", "read", "()I", false));
                    command.add(new InsnNode(CASTORE));
                    break;

                case '[':
                    LabelNode begin = new LabelNode();
                    LabelNode end = new LabelNode();

                    labl.push(end);
                    labl.push(begin);
                    command.add(begin);

                    command.add(new VarInsnNode(ALOAD, 1));
                    command.add(new VarInsnNode(ILOAD, 2));
                    command.add(new InsnNode(CALOAD));
                    command.add(new JumpInsnNode(IFEQ, end));
                    break;

                case ']':
                    command.add(new JumpInsnNode(GOTO, labl.pop()));
                    command.add(labl.pop());
                    break;
            }
        }
        runMethod.instructions.add(new InsnNode(RETURN));
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        myClass.accept(writer);
        byte[] bytes = writer.toByteArray();
        try (FileOutputStream output = new FileOutputStream(new File("compiledFile.class"))) {
            output.write(bytes);
        }
    }
}