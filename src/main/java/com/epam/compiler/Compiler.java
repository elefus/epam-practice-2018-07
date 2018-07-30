package com.epam.compiler;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import java.io.*;
import java.util.Stack;
import static jdk.internal.org.objectweb.asm.Opcodes.*;
import org.apache.commons.cli.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

class Compiler {

    private ClassNode classNode;
    private MethodNode runMethod;
    private InsnList command;

    private Compiler() {
        classNode = new ClassNode();
        classNode.version = Opcodes.V1_8;
        classNode.access = ACC_PUBLIC;
        classNode.name = "Compiled";
        classNode.superName = "java/lang/Object";
        classNode.interfaces.add("java/lang/Runnable");
        MethodNode constructor = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
        InsnList bytecode = constructor.instructions;
        bytecode.add(new VarInsnNode(ALOAD, 0));
        bytecode.add(new MethodInsnNode(INVOKESPECIAL, classNode.superName, "<init>", "()V", false));
        bytecode.add(new InsnNode(RETURN));
        classNode.methods.add(constructor);

        runMethod = new MethodNode(ACC_PUBLIC, "run", "()V", null, null);
        classNode.methods.add(runMethod);

        command = runMethod.instructions;
        command.add(new LdcInsnNode(30000));
        command.add(new VarInsnNode(NEWARRAY, T_CHAR));
        command.add(new VarInsnNode(ASTORE, 1));
        command.add(new InsnNode(ICONST_0));
        command.add(new VarInsnNode(ISTORE, 2));

    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        CommandLineParser cmdLineParser = new DefaultParser();
        CommandLine commandLine = null;
        Options options = new Options();
        options.addOption("f", true, "Input file");
        Compiler compiler = new Compiler();

        try {
            commandLine = cmdLineParser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (commandLine != null) {
            compiler.compile(compiler.getSource(commandLine.getOptionValue("f")));

        }
    }

    private String getSource(String fileName) throws URISyntaxException, IOException {
        Path path = Paths.get(new File(fileName).getPath());

        if (Compiler.class.getResource("./../../../" + fileName) != null)
            path = Paths.get(Compiler.class.getResource("./../../../" + fileName).toURI());

        if (path == null)
            throw new FileNotFoundException("This file doesn't exist");

        try (InputStream in = new FileInputStream(path.toString());
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null)
                builder.append(line);

            return builder.toString();
        }
    }

    private void compile(String code) throws IOException {

        Stack<LabelNode> labl = new Stack<>();

        for (int i = 0; i < code.length(); i++) {
            LabelNode label = new LabelNode();
            switch (code.charAt(i)) {

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
        classNode.accept(writer);
        byte[] bytes = writer.toByteArray();
        try (FileOutputStream output = new FileOutputStream(new File("Compiled.class"))) {
            output.write(bytes);
        }
    }
}