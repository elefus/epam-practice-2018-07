package com.epam;

import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleView implements View {
    private InputStreamReader reader = new InputStreamReader(System.in);

    @Override
    public char inputData() throws IOException {
        System.out.printf("Enter value:");
        return (char) reader.read();
    }

    @Override
    public void outputData(int value) {
        System.out.print(value + " ");
    }
    @Override
    public void traceCommand(int ptr, char command, int value) {
        System.out.printf("\n|| CELL : '%d' || OPERATION : '%c' || VALUE : '%d' ||",
                ptr, command, (int) value);
    }
}