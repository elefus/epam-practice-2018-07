package com.epam;

import java.util.Scanner;

public class ConsoleView implements View {
    private Scanner scanner;
    private StringBuilder builder;

    public ConsoleView() {
        scanner = new Scanner(System.in);
        builder = null;
    }

    @Override
    public void print(char value) {
        System.out.print(value);
    }

    @Override
    public char read() {
        if (builder == null) {
            builder = new StringBuilder(scanner.next());
        }

        char result = builder.charAt(0);
        builder.deleteCharAt(0);

        if (builder.length() == 0) {
            builder = null;
        }

        return result;
    }
}
