package com.epam.compiler;

public class Token implements Cloneable {

  enum Type {
    CHANGE_VAL,
    SHIFT,
    OUTPUT,
    INPUT,
    WHILE_START,
    WHILE_END,
    SET_TO_ZERO
  }

  private Type type;
  public int val = 1;

  public Token(Type type, int val) {
    this.type = type;
    this.val = val;
  }

  public Token(Type type) {
    this.type = type;
  }

  public Token(Token other) {
    this.type = other.getType();
    this.val = other.val;
  }

  public Type getType() {
    return type;
  }

}
