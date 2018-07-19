package com.epam;

import java.io.IOException;

public interface View {
    char inputData() throws IOException;
    void outputData(char cellValue);
    void traceOperation(int cellIndex, char operation, char cellValue);
}
