package com.epam.interpreter.view;

public interface View {
  String[] getFiles();

  char readChar();

  void printChar(char c);

  int getCellSize();

  int getMemoryCapacity();
}
