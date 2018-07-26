package com.compiler;

import com.compiler.commands.*;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class Compiler {

    public static final int NUM_OF_CELLS = 100;
    public static InsnList bytecode;
    public static int maxValue = 255;
    public static int currentBracket = 0;
    public static List<LabelNode> leftBrackets = new ArrayList<>();
    public static List<LabelNode> rightBrackets = new ArrayList<>();
    private static final int HUNDRED = 100;

    public static void pushNum(int num) {
        int numOfAdds = -1;
        for (int i = 0; i < num / HUNDRED; i++) {
            bytecode.add(new VarInsnNode(BIPUSH, HUNDRED));
            numOfAdds++;
        }
        if (num % 100 < 6) {
            bytecode.add(new InsnNode(ICONST_0 + num % HUNDRED));
            numOfAdds++;
        } else {
            bytecode.add(new VarInsnNode(BIPUSH, num % HUNDRED));
            numOfAdds++;
        }
        for (int i = 0; i < numOfAdds; i++) {
            bytecode.add(new InsnNode(IADD));
        }
    }

    public static void main(String[] args) throws MissingOptionException, IOException, URISyntaxException {

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("f", "file", true, "Filename");
        options.addOption("h", "help", false, "Help");

        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (line != null && line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            String header = "Compiler for BrainFuck";
            String footer = "";
            String cmdLineSyntax = "Compiler -f <File> [-h]";
            formatter.printHelp(cmdLineSyntax, header, options, footer);
            return;
        }

        if (line != null && !line.hasOption("f")) {
            throw new MissingOptionException("Missing required option -f ");
        }
        String file = null;
        if (line != null) {
            file = line.getOptionValue("f");
        }

        if (Compiler.class.getResource("./../../" + file) == null) {
            throw new FileNotFoundException("File not found");
        }
        String code = Files.lines(Paths.get(Compiler.class.getResource("./../../" + file).toURI()))
                .collect(joining(System.lineSeparator()));


        ClassNode myClass = new ClassNode();
        myClass.version = Opcodes.V1_8;
        myClass.access = ACC_PUBLIC;
        String name = "CompiledFile";
        myClass.name = name;
        myClass.superName = "java/lang/Object";


        MethodNode constructor = new MethodNode(ACC_PUBLIC, "<init>", "()V", "Hello", null);
        bytecode = constructor.instructions;
        bytecode.add(new VarInsnNode(ALOAD, 0));
        bytecode.add(new MethodInsnNode(INVOKESPECIAL, myClass.superName, "<init>", "()V", false));
        bytecode.add(new InsnNode(RETURN));

        MethodNode main = new MethodNode(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        bytecode = main.instructions;

        //VAR1 = ARRAY
        pushNum(NUM_OF_CELLS);
        bytecode.add(new VarInsnNode(NEWARRAY, T_INT));
        bytecode.add(new VarInsnNode(ASTORE, 1));

        //VAR 2 = CURRENT_CELL
        pushNum(1);
        pushNum(1);
        bytecode.add(new InsnNode(ISUB));
        bytecode.add(new VarInsnNode(ISTORE, 2));


        //COMMAND MAP
        Map<Character, Command> commandMap = new HashMap<>();
        commandMap.put('+', new Plus());
        commandMap.put('-', new Minus());
        commandMap.put('>', new ShiftRight());
        commandMap.put('<', new ShiftLeft());
        commandMap.put('[', new LeftBracket());
        commandMap.put(']', new RightBracket());
        commandMap.put('.', new Write());
        commandMap.put(',', new Read());
        commandMap.put('A', new Assign());


        //OPTIMIZING
        List<Integer> commandAmount = new ArrayList<>();
        StringBuilder optimizedCode = new StringBuilder();

        int codePointer = 0;

        while (codePointer < code.length()) {
            if ((commandMap.getOrDefault(code.charAt(codePointer), new Skip()).add(code, codePointer)) == 0) {
                optimizedCode.append(code.charAt(codePointer));
            }
            codePointer++;
        }

        code = optimizedCode.toString();
        optimizedCode.delete(0, optimizedCode.length());
        codePointer = 0;

        while (codePointer < code.length()) {
            int numOfCommands = commandMap.getOrDefault(code.charAt(codePointer), new Skip()).
                    optimize(code, codePointer);
            if (numOfCommands == 0) {
                codePointer++;
            } else {
                commandAmount.add(numOfCommands);
                optimizedCode.append(code.charAt(codePointer));
                codePointer += numOfCommands;
            }
        }

        code = optimizedCode.toString();
        optimizedCode.delete(0, optimizedCode.length());
        codePointer = 0;
        List<Integer> tempCommandAmount = new ArrayList<>(commandAmount);
        commandAmount.clear();

        while (codePointer < code.length()) {
            if ((code.charAt(codePointer) == '[' && code.length() > codePointer + 3) && (
                    code.charAt(codePointer + 1) == '+' || code.charAt(codePointer + 1) == '-') && (
                    code.charAt(codePointer + 2) == ']')) {

                if (code.charAt(codePointer + 3) == '+') {
                    optimizedCode.append('A');
                    commandAmount.add(tempCommandAmount.get(codePointer + 3));
                    codePointer += 4;
                } else if (code.charAt(codePointer + 3) == '-') {
                    optimizedCode.append('A');
                    commandAmount.add(256 - tempCommandAmount.get(codePointer + 3));
                    codePointer += 4;
                } else {
                    optimizedCode.append('A');
                    commandAmount.add(0);
                    codePointer += 3;
                }
            } else {
                optimizedCode.append(code.charAt(codePointer));
                commandAmount.add(tempCommandAmount.get(codePointer));
                codePointer++;
            }
        }

        boolean changed = true;
        while(changed) {
            changed = false;
            if (optimizedCode.indexOf("+-") != -1){
                changed = true;
                int idx = optimizedCode.indexOf("+-");
                int diff = commandAmount.get(idx) - commandAmount.get(idx+1);
                if (diff > 0) {
                    optimizedCode.replace(idx, idx + 2, "+");
                    commandAmount.remove(idx+1);
                    commandAmount.set(idx,diff);
                } else if (diff < 0) {
                    optimizedCode.replace(idx, idx + 2, "-");
                    commandAmount.remove(idx+1);
                    commandAmount.set(idx,Math.abs(diff));
                } else {
                    optimizedCode.replace(idx, idx + 2, "");
                    commandAmount.remove(idx+1);
                    commandAmount.remove(idx);
                }
            }

            if (optimizedCode.indexOf("-+") != -1){
                changed = true;
                int idx = optimizedCode.indexOf("-+");
                int diff = commandAmount.get(idx) - commandAmount.get(idx+1);
                if (diff > 0) {
                    optimizedCode.replace(idx, idx + 2, "-");
                    commandAmount.remove(idx+1);
                    commandAmount.set(idx,diff);
                } else if (diff < 0) {
                    optimizedCode.replace(idx, idx + 2, "+");
                    commandAmount.remove(idx+1);
                    commandAmount.set(idx,Math.abs(diff));
                } else {
                    optimizedCode.replace(idx, idx + 2, "");
                    commandAmount.remove(idx+1);
                    commandAmount.remove(idx);
                }
            }

            if (optimizedCode.indexOf("<>") != -1){
                changed = true;
                int idx = optimizedCode.indexOf("<>");
                int diff = commandAmount.get(idx) - commandAmount.get(idx+1);
                if (diff > 0) {
                    optimizedCode.replace(idx, idx + 2, "<");
                    commandAmount.remove(idx+1);
                    commandAmount.set(idx,diff);
                } else if (diff < 0) {
                    optimizedCode.replace(idx, idx + 2, ">");
                    commandAmount.remove(idx+1);
                    commandAmount.set(idx,Math.abs(diff));
                } else {
                    optimizedCode.replace(idx, idx + 2, "");
                    commandAmount.remove(idx+1);
                    commandAmount.remove(idx);
                }
            }

            if (optimizedCode.indexOf("><") != -1){
                changed = true;
                int idx = optimizedCode.indexOf("><");
                int diff = commandAmount.get(idx) - commandAmount.get(idx+1);
                if (diff > 0) {
                    optimizedCode.replace(idx, idx + 2, ">");
                    commandAmount.remove(idx+1);
                    commandAmount.set(idx,diff);
                } else if (diff < 0) {
                    optimizedCode.replace(idx, idx + 2, "<");
                    commandAmount.remove(idx+1);
                    commandAmount.set(idx,Math.abs(diff));
                } else {
                    optimizedCode.replace(idx, idx + 2, "");
                    commandAmount.remove(idx+1);
                    commandAmount.remove(idx);
                }
            }

            if (optimizedCode.indexOf("++") != -1){
                changed = true;
                int idx = optimizedCode.indexOf("++");
                int sum = commandAmount.get(idx) + commandAmount.get(idx+1);
                optimizedCode.replace(idx, idx + 2, "+");
                commandAmount.remove(idx+1);
                commandAmount.set(idx,sum);
            }

            if (optimizedCode.indexOf("--") != -1){
                changed = true;
                int idx = optimizedCode.indexOf("--");
                int sum = commandAmount.get(idx) + commandAmount.get(idx+1);
                optimizedCode.replace(idx, idx + 2, "-");
                commandAmount.remove(idx+1);
                commandAmount.set(idx,sum);
            }

            if (optimizedCode.indexOf(">>") != -1){
                changed = true;
                int idx = optimizedCode.indexOf(">>");
                int sum = commandAmount.get(idx) + commandAmount.get(idx+1);
                optimizedCode.replace(idx, idx + 2, ">");
                commandAmount.remove(idx+1);
                commandAmount.set(idx,sum);
            }

            if (optimizedCode.indexOf("<<") != -1){
                changed = true;
                int idx = optimizedCode.indexOf("<<");
                int sum = commandAmount.get(idx) + commandAmount.get(idx+1);
                optimizedCode.replace(idx, idx + 2, "<");
                commandAmount.remove(idx+1);
                commandAmount.set(idx,sum);
            }
        }

        //CREATE INPUT STREAM
        if (Read.used) {
            bytecode.add(new TypeInsnNode(NEW, "java/io/InputStreamReader"));
            bytecode.add(new InsnNode(DUP));
            bytecode.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;"));
            bytecode.add(new MethodInsnNode(INVOKESPECIAL, "java/io/InputStreamReader", "<init>",
                    "(Ljava/io/InputStream;)V", false));
            bytecode.add(new VarInsnNode(ASTORE, 3));
        }

        //COMPILING
        for (int i = 0; i < optimizedCode.length(); i++) {
            commandMap.get(optimizedCode.charAt(i)).execute(commandAmount.get(i));
        }

        //RETURN
        bytecode.add(new InsnNode(RETURN));

        myClass.methods.add(constructor);
        myClass.methods.add(main);

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        myClass.accept(writer);

        byte[] bytes = writer.toByteArray();

        Class<?> myClassLoaded = new ClassLoader() {
            Class<?> load() {
                return defineClass(null, bytes, 0, bytes.length);
            }
        }.load();

        try {
            Object ref = myClassLoaded.newInstance();
            System.out.println(ref);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try (FileOutputStream output = new FileOutputStream(new File("./" + name + ".class"))) {
            output.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
