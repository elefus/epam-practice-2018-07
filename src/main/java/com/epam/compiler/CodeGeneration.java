package com.epam.compiler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

class CodeGeneration {
    private static InsnList runList;
    private static Stack<LabelNode> Stack = new Stack<>();

    public static ArrayList<Token> createTokens(char[] code) {
        ArrayList<Token> tokens = new ArrayList<>();

        for (char aCode : code) {
            switch (aCode) {
                case '>':
                    tokens.add(new Token(Type.REPLACE));
                    break;

                case '<':
                    tokens.add(new Token(Type.REPLACE, -1));
                    break;

                case '+':
                    tokens.add(new Token(Type.ADD_VAL));
                    break;

                case '-':
                    tokens.add(new Token(Type.ADD_VAL, -1));
                    break;

                case ',':
                    tokens.add(new Token(Type.INPUT));
                    break;

                case '.':
                    tokens.add(new Token(Type.OUTPUT));
                    break;

                case '[':
                    tokens.add(new Token(Type.R_BRACKET));
                    break;

                case ']':
                    tokens.add(new Token(Type.L_BRACKET));
                    break;
            }
        }

        return tokens;
    }

    public static byte[] generateByteCode(ArrayList<Token> tokens, int size) {
        ClassNode classNode = new ClassNode();
        classNode.version = Opcodes.V1_8;
        classNode.access = ACC_PUBLIC;
        classNode.name = "Creation";
        classNode.superName = "java/lang/Object";
        classNode.interfaces.add("java/lang/Runnable");

        MethodNode initMethod = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
        InsnList initList = initMethod.instructions;
        initList.add(new VarInsnNode(ALOAD, 0));
        initList.add(new MethodInsnNode(INVOKESPECIAL, classNode.superName, "<init>", "()V", false));
        initList.add(new InsnNode(RETURN));
        classNode.methods.add(initMethod);

        MethodNode runMethod = new MethodNode(ACC_PUBLIC, "run", "()V", null, null);
        runList = runMethod.instructions;
        runList.add(new LdcInsnNode(size));
        runList.add(new IntInsnNode(NEWARRAY, T_CHAR));
        runList.add(new VarInsnNode(ASTORE, 1));

        runList.add(new InsnNode(ICONST_0));
        runList.add(new VarInsnNode(ISTORE, 2));

        for (Token token : tokens) {
            switch (token.getType()) {
                case REPLACE:
                    replace(token.getValue());
                    break;

                case ADD_VAL:
                    addValue(token.getValue());
                    break;

                case INPUT:
                    readValue(token.getValue());
                    break;

                case OUTPUT:
                    writeValue(token.getValue());
                    break;

                case R_BRACKET:
                    goToLeftBracket();
                    break;

                case L_BRACKET:
                    goToRightBracket();
                    break;

                case ZEROED:
                    zeroed();
                    break;
            }
        }

        runList.add(new InsnNode(RETURN));

        classNode.methods.add(runMethod);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);

        return writer.toByteArray();
    }

    private static void addValue(int val) {
        runList.add(new VarInsnNode(ALOAD, 1));
        runList.add(new VarInsnNode(ILOAD, 2));
        runList.add(new InsnNode(DUP2));
        runList.add(new InsnNode(CALOAD));
        runList.add(new LdcInsnNode(val));
        runList.add(new InsnNode(IADD));
        runList.add(new InsnNode(CASTORE));
    }

    private static void replace(int value) {
        runList.add(new IincInsnNode(2, value));
    }

    private static void readValue(int value) {
        for (int i = 0; i < value; i++) {
            runList.add(new VarInsnNode(ALOAD, 1));
            runList.add(new VarInsnNode(ILOAD, 2));
            runList.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;"));
            runList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/InputStream", "read", "()I", false));
            runList.add(new InsnNode(CASTORE));
        }
    }

    private static void writeValue(int value) {
        for (int i = 0; i < value; i++) {
            runList.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
            runList.add(new VarInsnNode(ALOAD, 1));
            runList.add(new VarInsnNode(ILOAD, 2));
            runList.add(new InsnNode(CALOAD));
            runList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(C)V", false));
        }
    }

    private static void goToRightBracket() {
        runList.add(new JumpInsnNode(GOTO, Stack.pop()));
        runList.add(Stack.pop());
    }

    private static void goToLeftBracket() {
        LabelNode begin = new LabelNode();
        LabelNode end = new LabelNode();

        Stack.push(end);
        Stack.push(begin);

        runList.add(begin);
        runList.add(new VarInsnNode(ALOAD, 1));
        runList.add(new VarInsnNode(ILOAD, 2));
        runList.add(new InsnNode(CALOAD));
        runList.add(new JumpInsnNode(IFEQ, end));
    }

    private static void zeroed() {
        runList.add(new VarInsnNode(ALOAD, 1));
        runList.add(new VarInsnNode(ILOAD, 2));
        runList.add(new InsnNode(ICONST_0));
        runList.add(new InsnNode(CASTORE));
    }
}
