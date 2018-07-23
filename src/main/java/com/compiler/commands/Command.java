package com.compiler.commands;

import jdk.internal.org.objectweb.asm.tree.InsnList;

public interface Command {
  InsnList execute(String val);
}
