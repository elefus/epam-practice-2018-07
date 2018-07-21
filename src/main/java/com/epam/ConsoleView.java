package com.epam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleView implements View {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public char inputData() throws IOException {
        System.out.println("Enter value:");
        return (char) reader.read();
    }

    @Override
    public void outputData(char cellValue) {
        System.out.print((int) cellValue + " ");
    }

    @Override
    public void traceCommand(int cellIndex, char operation, char cellValue) {
        System.out.printf("\n|| CELL : '%d' || OPERATION : '%c' || VALUE : '%d' ||",
                cellIndex, operation, (int) cellValue);
    }
}
