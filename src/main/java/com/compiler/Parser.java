package com.compiler;

import static jdk.internal.org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static jdk.internal.org.objectweb.asm.Opcodes.ACC_STATIC;
import static jdk.internal.org.objectweb.asm.Opcodes.DUP;
import static jdk.internal.org.objectweb.asm.Opcodes.GETSTATIC;
import static jdk.internal.org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static jdk.internal.org.objectweb.asm.Opcodes.NEW;
import static jdk.internal.org.objectweb.asm.Opcodes.NEWARRAY;
import static jdk.internal.org.objectweb.asm.Opcodes.PUTSTATIC;
import static jdk.internal.org.objectweb.asm.Opcodes.RETURN;
import static jdk.internal.org.objectweb.asm.Opcodes.T_INT;

import com.compiler.commands.Brackets;
import com.compiler.commands.ChangeData;
import com.compiler.commands.ChangeIdx;
import com.compiler.commands.Command;
import com.compiler.commands.Print;
import com.compiler.commands.Read;
import com.epam.Control;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.TypeInsnNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

public class Parser {

  public static MethodNode getMain(String path) {
    Map<String, Command> map = new HashMap<>();
    map.put("C", new ChangeData());
    map.put("S", new ChangeIdx());
    map.put(".", new Print());
    map.put(",", new Read());
    map.put("G", new Brackets());
    MethodNode main = new MethodNode(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V",
        null, null);
    InsnList bytecode = main.instructions;
    bytecode.add(new TypeInsnNode(NEW, "java/io/InputStreamReader"));
    bytecode.add(new InsnNode(DUP));
    bytecode.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;"));
    bytecode.add(new MethodInsnNode(INVOKESPECIAL, "java/io/InputStreamReader", "<init>",
        "(Ljava/io/InputStream;)V", false));
    bytecode
        .add(new FieldInsnNode(PUTSTATIC, Compiler.className, "reader", "Ljava/io/InputStreamReader;"));
    bytecode.add(new FieldInsnNode(GETSTATIC, Compiler.className, "size", "I"));
    bytecode.add(new VarInsnNode(NEWARRAY, T_INT));
    bytecode.add(new FieldInsnNode(PUTSTATIC, Compiler.className, "array", "[I"));
    String code = null;
    try {
      code = parse(Control.getCodeFromFile(path));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    assert code != null;
    String[] arr = code.split(" ");
    for (int i = 0; i < arr.length; i = i + 2) {
      bytecode.add(map.get(arr[i]).execute(arr[i + 1]));
    }
    bytecode.add(new InsnNode(RETURN));
    return main;
  }

  private static String parse(String toParse) {
    String commands = "+-.,[]><";
    StringBuilder code = new StringBuilder();
    for (char i : toParse.toCharArray()) {
      if (commands.contains(Character.toString(i))) {
        code.append(i);
      }
    }
    int idx = 0;
    while (idx < code.length()) {
      char currentCommand = code.charAt(idx);
      int number = 1;
      int altIdx = idx + 1;
      while (altIdx < code.length() && code.charAt(altIdx) == currentCommand) {
        number++;
        altIdx++;
      }
      int offset = number;
      number = currentCommand == '-' || currentCommand == '<' ? -number : number;
      currentCommand = currentCommand == '-' || currentCommand == '+' ? 'C' : currentCommand; //Change
      currentCommand = currentCommand == '<' || currentCommand == '>'? 'S' : currentCommand; //Shift
      String str = currentCommand + " " + number + " ";
      if (!(currentCommand == ']' || currentCommand == '[')) {
        code.replace(idx, idx + offset, str);
        idx = idx + str.length();
      } else {
        idx++;
      }
    }
    idx = 0;
    while (idx < code.length()) {
      if (code.charAt(idx) == ']' || code.charAt(idx) == '[') {
        int number = Control.getPair(code.toString(), idx);
        number = number - idx;
        code.replace(idx + number, idx + number + 1, "G " + -number + " ");
        code.replace(idx, idx + 1, "G " + number + " ");

      }
      idx++;
    }

    return String.valueOf(code);
  }
}
