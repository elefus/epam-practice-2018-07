package com.epam;

import java.util.Scanner;

public class ConsoleView implements View {
    private Scanner scanner;

    public ConsoleView() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void print(char value) {
        System.out.print(value);
    }

    @Override
    public char read() {
        return scanner.next().charAt(0);
    }
}
