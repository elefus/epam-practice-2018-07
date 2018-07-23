package com.compiler;

import jdk.internal.org.objectweb.asm.tree.InsnList;

public interface Commands {
  InsnList execute(String val);
}
