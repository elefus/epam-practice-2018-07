package com.epam;

import java.io.IOException;

public interface View {
    char inputData() throws IOException;
    void outputData(int value);
    void traceCommand(int ptr, char operation, int value);
}