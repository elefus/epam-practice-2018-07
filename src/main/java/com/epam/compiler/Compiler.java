package com.epam.compiler;

import com.epam.compiler.Token.Operation;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static jdk.internal.org.objectweb.asm.Opcodes.*;
import static jdk.internal.org.objectweb.asm.Opcodes.GOTO;

public class Compiler {

    public static class Command {
        public final static char SHIFT_RIGHT = '>';
        public final static char SHIFT_LEFT = '<';
        public final static char PLUS = '+';
        public final static char MINUS = '-';
        public final static char OUTPUT = '.';
        public final static char INPUT = ',';
        public final static char BRACKET_LEFT = '[';
        public final static char BRACKET_RIGHT = ']';
    }

    private static class Generator {

        private static InsnList run;
        private static Deque<LabelNode> cycleStack = new ArrayDeque<>();

        public static byte[] generate(List<Token> tokens) {
            ClassNode mainClass = new ClassNode();
            mainClass.name = "test";
            mainClass.superName = "java/lang/Object";
            mainClass.interfaces.add("java/lang/Runnable");
            mainClass.version = V1_8;
            mainClass.access = ACC_PUBLIC;

            MethodNode run = new MethodNode(ACC_PUBLIC, "run", "()V", null, null);
            Generator.run = run.instructions;
            Generator.run.add(new LdcInsnNode(30000));
            Generator.run.add(new IntInsnNode(NEWARRAY, T_CHAR));
            Generator.run.add(new VarInsnNode(ASTORE, 1));
            Generator.run.add(new InsnNode(ICONST_0));
            Generator.run.add(new VarInsnNode(ISTORE, 2));

            MethodNode constructor = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
            InsnList insnConstructor = constructor.instructions;
            insnConstructor.add(new VarInsnNode(ALOAD, 0));
            insnConstructor.add(new MethodInsnNode(INVOKESPECIAL, mainClass.superName, "<init>", "()V", false));
            insnConstructor.add(new InsnNode(RETURN));
            mainClass.methods.add(constructor);

            for (int i = 0; i < tokens.size(); i++) {
                switch (tokens.get(i).getOperation()) {

                    case SHIFT: {
                        shift(tokens.get(i).getValue());
                        break;
                    }
                    case OUTPUT: {
                        printValue(tokens.get(i).getValue());
                        break;
                    }
                    case INPUT: {
                        readValue(tokens.get(i).getValue());
                        break;
                    }
                    case VALUE_CHANGE: {
                        changeValue(tokens.get(i).getValue());
                        break;
                    }
                    case CYCLE_START: {
                        whileStart();
                        break;
                    }
                    case CYCLE_END: {
                        whileEnd();
                        break;
                    }
                }
            }
            Generator.run.add(new InsnNode(RETURN));
            mainClass.methods.add(run);
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            mainClass.accept(writer);
            try (FileOutputStream output = new FileOutputStream(new File("./" + mainClass.name + ".class"))) {
                output.write(writer.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return writer.toByteArray();
        }

        private static void readValue(int val) {
            for (int i = 0; i < val; i++) {
                run.add(new VarInsnNode(ALOAD, 1));
                run.add(new VarInsnNode(ILOAD, 2));
                run.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;"));
                run.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/InputStream", "read", "()I", false));
                run.add(new InsnNode(CASTORE));
            }
        }

        private static void printValue(int val) {
            for (int i = 0; i < val; i++) {
                run.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                run.add(new VarInsnNode(ALOAD, 1));
                run.add(new VarInsnNode(ILOAD, 2));
                run.add(new InsnNode(CALOAD));
                run.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(C)V", false));
            }
        }

        private static void shift(int val) {
            run.add(new IincInsnNode(2, val));
        }

        private static void changeValue(int val) {
            run.add(new VarInsnNode(ALOAD, 1));
            run.add(new VarInsnNode(ILOAD, 2));
            run.add(new InsnNode(DUP2));
            run.add(new InsnNode(CALOAD));
            run.add(new LdcInsnNode(val));
            run.add(new InsnNode(IADD));
            run.add(new InsnNode(CASTORE));
        }

        private static void whileStart() {
            LabelNode begin = new LabelNode();
            LabelNode end = new LabelNode();

            cycleStack.push(end);
            cycleStack.push(begin);

            run.add(begin);
            run.add(new VarInsnNode(ALOAD, 1));
            run.add(new VarInsnNode(ILOAD, 2));
            run.add(new InsnNode(CALOAD));
            run.add(new JumpInsnNode(IFEQ, end));
        }

        private static void whileEnd() {
            run.add(new JumpInsnNode(GOTO, cycleStack.pop()));
            run.add(cycleStack.pop());
        }
    }

    private static String fileName;

    public static void main(String[] args) {

        fileName = OptionParse.parsing(args);
        byte[] byteCode = Generator.generate(createTokens(Preparing.processFile(fileName)));
        Class<?> myClass = new ClassLoader() {
            Class<?> load() { return defineClass(null, byteCode, 0, byteCode.length); } }.load();
        try {
            Object reference = myClass.newInstance();
            ((Runnable) reference).run();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static ArrayList<Token> createTokens(char[] program) {
        ArrayList<Token> tokens = new ArrayList<>();

        for (int i = 0; i < program.length; i++) {
            switch (program[i]) {

                case Command.SHIFT_RIGHT: {
                    tokens.add(new Token(Operation.SHIFT));
                    break;
                }
                case Command.SHIFT_LEFT: {
                    tokens.add(new Token(Operation.SHIFT, -1));
                    break;
                }
                case Command.INPUT: {
                    tokens.add(new Token(Operation.INPUT));
                    break;
                }
                case Command.OUTPUT: {
                    tokens.add(new Token(Operation.OUTPUT));
                    break;
                }
                case Command.PLUS: {
                    tokens.add(new Token(Operation.VALUE_CHANGE));
                    break;
                }
                case Command.MINUS: {
                    tokens.add(new Token(Operation.VALUE_CHANGE, -1));
                    break;
                }
                case Command.BRACKET_LEFT: {
                    tokens.add(new Token(Operation.CYCLE_START));
                    break;
                }
                case Command.BRACKET_RIGHT: {
                    tokens.add(new Token(Operation.CYCLE_END));
                    break;
                }
            }
        }
        return tokens;
    }
}

