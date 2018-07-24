package com.compiler;

import com.compiler.commands.*;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class Main {

    private static String name = "CompiledFile";
    public static final int numOfCells = 5;
    public static InsnList bytecode;
    public static int maxValue = 255;

    public static void pushNum(int num) {
        int numOfAdds = -1;
        for (int i = 0; i < num / 100; i++) {
            bytecode.add(new VarInsnNode(BIPUSH,100));
            numOfAdds++;
        }
        if (num%100 < 6) {
            bytecode.add(new InsnNode(ICONST_0 + num%100));
            numOfAdds++;
        } else {
            bytecode.add(new VarInsnNode(BIPUSH, num % 100));
            numOfAdds++;
        }
        for (int i = 0; i < numOfAdds; i++) {
            bytecode.add(new InsnNode(IADD));
        }
    }

    public static void main(String[] args) {
        ClassNode myClass = new ClassNode();
        myClass.version = Opcodes.V1_8;
        myClass.access = ACC_PUBLIC;
        myClass.name = name;
        myClass.superName = "java/lang/Object";


        MethodNode constructor = new MethodNode(ACC_PUBLIC, "<init>", "()V", "Hello", null);
        bytecode = constructor.instructions;
        bytecode.add(new VarInsnNode(ALOAD, 0));
        bytecode.add(new MethodInsnNode(INVOKESPECIAL, myClass.superName, "<init>", "()V", false));
        bytecode.add(new InsnNode(RETURN));

        MethodNode main = new MethodNode(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null,null);
        bytecode = main.instructions;

//----------------------------------------------------------------------------------------------------------------------
        String tempString = ".<.<.<.<.<.<.<.<.<.";
//----------------------------------------------------------------------------------------------------------------------

        //VAR1 = ARRAY
        pushNum(numOfCells);
        bytecode.add(new VarInsnNode(NEWARRAY, T_INT));
        bytecode.add(new VarInsnNode(ASTORE,1));

        //VAR 2 = CURRENT_CELL
        pushNum(1);
        pushNum(1);
        bytecode.add(new InsnNode(ISUB));
        bytecode.add(new VarInsnNode(ISTORE,2));


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


        //OPTIMIZING
        List<Integer> commandAmount = new ArrayList<>();
        StringBuilder optimizedCode = new StringBuilder();
        int codePointer = 0;
        while (codePointer < tempString.length()) {
            int numOfCommands = commandMap.getOrDefault(tempString.charAt(codePointer),new Skip()).
                    optimize(tempString, codePointer);
            if (numOfCommands == 0) {
                codePointer++;
            } else {
                commandAmount.add(numOfCommands);
                optimizedCode.append(tempString.charAt(codePointer));
                codePointer += numOfCommands;
            }
        }

        //CREATE INPUT STREAM
        if (Read.used) {
            bytecode.add(new TypeInsnNode(NEW, "java/io/InputStreamReader"));
            bytecode.add(new InsnNode(DUP));
            bytecode.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;"));
            bytecode.add(new MethodInsnNode(INVOKESPECIAL, "java/io/InputStreamReader", "<init>",
                    "(Ljava/io/InputStream;)V", false));
            bytecode.add(new VarInsnNode(ASTORE,3));
        }

        //COMPILING
        for(int i = 0; i < optimizedCode.length(); i++) {
            commandMap.get(optimizedCode.charAt(i)).execute(commandAmount.get(i));
        }

        //RETURN
        bytecode.add(new InsnNode(RETURN));

        myClass.methods.add(constructor);
        myClass.methods.add(main);

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        myClass.accept(writer);

        byte[] bytes = writer.toByteArray();

//----------------------------------------------------------------------------------------------------------------------
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
//----------------------------------------------------------------------------------------------------------------------
        try (FileOutputStream output = new FileOutputStream(new File("./" + name + ".class"))) {
            output.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
