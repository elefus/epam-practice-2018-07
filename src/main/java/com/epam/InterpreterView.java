package com.epam;

import java.io.IOException;

public interface InterpreterView {
    char requestInput() throws IOException;
    void print(char dataAtCurrentMemCell);
    void printMem(char[] memory, int p_position, char c);
}
