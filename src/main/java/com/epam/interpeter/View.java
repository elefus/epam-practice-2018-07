package com.epam.interpeter;

import java.io.IOException;

public interface View {
    char inputData() throws IOException;
    void outputData(char cellValue);
    void traceCommand(int cellIndex, char operation, char cellValue);
}
