package com.epam.compiler;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class Compiler {

    private ClassNode programClass;
    private MethodNode runMethod;
    private Boolean runMethodHasReturn;
    private Boolean enableOptimization;

    public Compiler(Boolean enableOptimization) {
        runMethodHasReturn = false;
        this.enableOptimization = enableOptimization;
        programClass = new ClassNode();
        programClass.version = Opcodes.V1_8;
        programClass.access = ACC_PUBLIC;
        programClass.name = "ProgramClass";
        programClass.superName = "java/lang/Object";

        MethodNode constructor = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
        InsnList bytecode = constructor.instructions;
        bytecode.add(new VarInsnNode(ALOAD, 0));
        bytecode.add(new MethodInsnNode(INVOKESPECIAL, programClass.superName, "<init>", "()V", false));
        bytecode.add(new InsnNode(RETURN));

        programClass.methods.add(constructor);

        programClass.interfaces.add("java/lang/Runnable");
        runMethod = new MethodNode(ACC_PUBLIC, "run", "()V", null, null);
        runMethod.exceptions.add("java/io/IOException");

        programClass.methods.add(runMethod);
    }

    public void compile(String sourceCode) {
        if (sourceCode == null)
            throw new IllegalArgumentException("sourceCode argument was null");
        if (runMethodHasReturn)
            throw new IllegalStateException("run() method already has a return statement");

        ArrayList<Token> tokens = Tokenizer.tokenize(sourceCode);

        if (enableOptimization)
            Optimizer.optimize(tokens);

        InsnList bytecode = runMethod.instructions;

        bytecode.add(new LdcInsnNode(30000));
        bytecode.add(new VarInsnNode(NEWARRAY, T_CHAR));
        bytecode.add(new VarInsnNode(ASTORE, 1));
        bytecode.add(new InsnNode(ICONST_0));
        bytecode.add(new VarInsnNode(ISTORE, 2));

        Stack<LabelNode> labels = new Stack<>();

        for (Token token : tokens) {
            LabelNode label = new LabelNode();
            switch (token.type) {
                case Add:
                    bytecode.add(new VarInsnNode(ALOAD, 1));
                    bytecode.add(new VarInsnNode(ILOAD, 2));
                    bytecode.add(new InsnNode(DUP2));
                    bytecode.add(new InsnNode(CALOAD));
                    bytecode.add(new LdcInsnNode(token.parameter));
                    bytecode.add(new InsnNode(IADD));
                    bytecode.add(new LdcInsnNode(256));
                    bytecode.add(new InsnNode(IREM));
                    bytecode.add(new InsnNode(I2C));
                    bytecode.add(new InsnNode(CASTORE));
                    break;

                case Shift:
                    bytecode.add(new VarInsnNode(ILOAD, 2));
                    bytecode.add(new LdcInsnNode(token.parameter));
                    bytecode.add(new InsnNode(IADD));
                    bytecode.add(new LdcInsnNode(30000));
                    bytecode.add(new InsnNode(IREM));
                    bytecode.add(new VarInsnNode(ISTORE, 2));
                    bytecode.add(new VarInsnNode(ILOAD, 2));
                    bytecode.add(new JumpInsnNode(IFGE, label));

                    bytecode.add(new VarInsnNode(ALOAD, 1));
                    bytecode.add(new InsnNode(ARRAYLENGTH));
                    bytecode.add(new VarInsnNode(ILOAD, 2));
                    bytecode.add(new InsnNode(IADD));
                    bytecode.add(new VarInsnNode(ISTORE, 2));

                    bytecode.add(label);
                    break;

                case Input:
                    bytecode.add(new VarInsnNode(ALOAD, 1));
                    bytecode.add(new VarInsnNode(ILOAD, 2));
                    bytecode.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;"));
                    bytecode.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/InputStream", "read", "()I", false));
                    bytecode.add(new InsnNode(CASTORE));
                    break;

                case Output:
                    bytecode.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                    bytecode.add(new VarInsnNode(ALOAD, 1));
                    bytecode.add(new VarInsnNode(ILOAD, 2));
                    bytecode.add(new InsnNode(CALOAD));
                    bytecode.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(C)V", false));
                    break;

                case SetToZero:
                    bytecode.add(new VarInsnNode(ALOAD, 1));
                    bytecode.add(new VarInsnNode(ILOAD, 2));
                    bytecode.add(new InsnNode(ICONST_0));
                    bytecode.add(new InsnNode(CASTORE));
                    break;

                case LoopStart:
                    LabelNode begin = new LabelNode();
                    LabelNode end = new LabelNode();

                    labels.push(end);
                    labels.push(begin);
                    bytecode.add(begin);

                    bytecode.add(new VarInsnNode(ALOAD, 1));
                    bytecode.add(new VarInsnNode(ILOAD, 2));
                    bytecode.add(new InsnNode(CALOAD));
                    bytecode.add(new JumpInsnNode(IFEQ, end));
                    break;

                case LoopEnd:
                    bytecode.add(new JumpInsnNode(GOTO, labels.pop()));
                    bytecode.add(labels.pop());
                    break;
            }
        }
    }

    public void addReturn() {
        runMethod.instructions.add(new InsnNode(RETURN));
        runMethodHasReturn = true;
    }

    public void launchProgram() {
        if (!runMethodHasReturn)
            addReturn();

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        programClass.accept(writer);

        byte[] bytes = writer.toByteArray();

        Class<?> programClassLoader = new ClassLoader() {
            Class<?> load() {
                return defineClass(null, bytes, 0, bytes.length);
            }
        }.load();

        try {
            Object ref = programClassLoader.newInstance();
            ((Runnable) ref).run();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void toFile(String fileName) throws IOException {
        if (!runMethodHasReturn)
            addReturn();

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        programClass.accept(writer);

        byte[] bytes = writer.toByteArray();

        try (FileOutputStream stream = new FileOutputStream(new File(fileName))) {
            stream.write(bytes);
        }
    }

}
