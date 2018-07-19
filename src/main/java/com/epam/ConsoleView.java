package com.epam;

import java.io.*;

public class ConsoleView implements View {
    @Override
    public char inputData() throws IOException {
        System.out.println("\nEnter your value :");
        return (char) System.in.read();
    }

    @Override
    public void outputData(char cellValue) {
        System.out.print((int) cellValue + " ");
    }

    @Override
    public void traceOperation(int cellIndex, char operation, char cellValue) {
        System.out.printf("\n[%d cell] :: current operation '%c' :: value in cell '%d' ",
                          cellIndex + 1, operation, (int) cellValue);
    }
}
