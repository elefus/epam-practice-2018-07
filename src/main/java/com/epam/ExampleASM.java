package com.epam;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class ExampleASM {

    public static void main(String[] args) {
        ClassNode myClass = new ClassNode();
        myClass.version = Opcodes.V1_8;
        myClass.access = ACC_PUBLIC;
        myClass.name = "TestClass";
        myClass.superName = "java/lang/Object";
        myClass.interfaces.add("java/lang/Runnable");

        MethodNode constructor = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
        InsnList bytecode = constructor.instructions;
        bytecode.add(new VarInsnNode(ALOAD, 0));
        bytecode.add(new MethodInsnNode(INVOKESPECIAL, myClass.superName, "<init>", "()V", false));
        bytecode.add(new InsnNode(RETURN));

        MethodNode runMethod = new MethodNode(ACC_PUBLIC, "run", "()V", null, null);
        // TODO

        myClass.methods.add(constructor);

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        myClass.accept(writer);

        byte[] bytes = writer.toByteArray();
        // TODO запись в .class-файл

        Class<?> myClassLoaded = new ClassLoader() {
            Class<?> load() {
                return defineClass(null, bytes, 0, bytes.length);
            }
        }.load();

        try {
            Object ref = myClassLoaded.newInstance();
            System.out.println(ref);
            ((Runnable) ref).run();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
