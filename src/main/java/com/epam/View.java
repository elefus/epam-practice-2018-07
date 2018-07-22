package com.epam;

public interface View {
    void printSymbol(int cell);
    int readSymbol();
    void print(int[] cells, int currentCell, int delay);
}
