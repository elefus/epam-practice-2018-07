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

import com.compiler.commands.Assign;
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
    map.put("D", new ChangeData());
    map.put("M", new ChangeIdx());
    map.put(".", new Print());
    map.put(",", new Read());
    map.put("G", new Brackets());
    map.put("A", new Assign());
    MethodNode main = new MethodNode(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V",
        null, null);
    InsnList bytecode = main.instructions;
    bytecode.add(new TypeInsnNode(NEW, "java/io/InputStreamReader"));
    bytecode.add(new InsnNode(DUP));
    bytecode.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;"));
    bytecode.add(new MethodInsnNode(INVOKESPECIAL, "java/io/InputStreamReader", "<init>",
        "(Ljava/io/InputStream;)V", false));
    bytecode
        .add(new FieldInsnNode(PUTSTATIC, Compiler.className, "reader",
            "Ljava/io/InputStreamReader;"));
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
      currentCommand = currentCommand == '-' || currentCommand == '+' ? 'D' : currentCommand;
      currentCommand = currentCommand == '<' || currentCommand == '>' ? 'M' : currentCommand;
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
        code.replace(idx + number, idx + number + 1, "G -1 ");
        code.replace(idx, idx + 1, "G 1 ");

      }
      idx++;
    }
    String[] split;
    boolean changed = true;
    StringBuilder newStr = new StringBuilder();
    while (changed) {
      changed = false;
      split = code.toString().split(" ");
      for (int i = 0; i < split.length; i += 2) {

        if (!(i + 2 >= split.length) && split[i].equals(split[i + 2])) {
          int number = Integer.parseInt(split[i + 1]) + Integer.parseInt(split[i + 3]);
          if (number != 0) {
            newStr.append(split[i]).append(" ");
            newStr.append(number).append(" ");
          }
          changed = true;
          i = i + 2;
        } else {
          newStr.append(split[i]).append(" ");
          newStr.append(split[i + 1]).append(" ");
        }

      }
      code.delete(0, code.length());
      code.append(newStr);
      newStr.delete(0, newStr.length());

    }
    while (code.indexOf("G 1 D -1 G -1") != -1) {
      code.replace(code.indexOf("G 1 D -1 G -1"),
          code.indexOf("G 1 D -1 G -1") + "G 1 D -1 G -1".length(), "A 0");
    }
    while (code.indexOf("G 1 D 1 G -1") != -1) {
      code.replace(code.indexOf("G 1 D 1 G -1"),
          code.indexOf("G 1 D 1 G -1") + "G 1 D 1 G -1".length(), "A 0");
    }
    split = code.toString().split(" ");
    idx = 0;
    while (idx != split.length) {
      if (idx + 2 < split.length && split[idx].equals("A") && split[idx + 2].equals("D")) {
        newStr.append(split[idx] + " " + split[idx + 3] + " ");
        idx += 2;
      } else {
        newStr.append(split[idx] + " " + split[idx + 1] + " ");
      }
      idx += 2;
    }
    code = newStr;

    return String.valueOf(code);
  }

  public static void main(String[] args) {
    try {
      System.out.println(parse(Control.getCodeFromFile("test8.bf")));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
