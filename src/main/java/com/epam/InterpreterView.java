package com.epam;

import java.io.IOException;

public interface InterpreterView {
    char requestInput() throws IOException;
    void printData(char dataAtCurrentMemCell);
    void printMem(char[] memory, int p_position, char c);
}
