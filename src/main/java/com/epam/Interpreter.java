package com.epam;

import java.util.Scanner;

public class Interpreter {
    private Tape tape;
    private Scanner scanner;

    public Interpreter(int tapeLength) {
        tape = new Tape(tapeLength);
        scanner = new Scanner(System.in);
    }

    public Interpreter() {
        this(30000);
    }

    public Tape getTape() { return tape; }

    public void interpret(String sourceCode) {
        if (sourceCode == null)
            throw new IllegalArgumentException("sourceCode argument was null");

        for (int i = 0; i < sourceCode.length(); i++) {
            switch (sourceCode.charAt(i)) {
                case '>': tape.shiftRight(); break;
                case '<': tape.shiftLeft(); break;
                case '+': tape.increment(); break;
                case '-': tape.decrement(); break;
                case '.': System.out.print(tape.getValue()); break;
                case ',': tape.setValue(scanner.next().charAt(0)); break;
                case '[':
                    if (tape.getValue() == 0) {
                        i++;
                        int temp = 0;
                        while (temp != 0 || sourceCode.charAt(i) != ']') {
                            if (sourceCode.charAt(i) == '[')
                                temp++;
                            if (sourceCode.charAt(i) == ']')
                                temp--;
                            i++;
                        }
                    }
                    break;
                case ']':
                    if (tape.getValue() != 0) {
                        i--;
                        int temp = 0;
                        while (temp != 0 || sourceCode.charAt(i) != '[') {
                            if (sourceCode.charAt(i) == ']')
                                temp++;
                            if (sourceCode.charAt(i) == '[')
                                temp--;
                            i--;
                        }
                    }
                    break;
            }
        }
    }
}
