package com.epam.View;

public interface View {
    String[] getFiles();

    char readChar();

    void printChar(char c);

    int getCellSize();
}
