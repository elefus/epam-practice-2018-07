package com.epam;

public class Interpreter {
    private Tape tape;
    private View view;

    public Interpreter(int tapeLength, View view) {
        tape = new Tape(tapeLength);
        this.view = view;
    }

    public Interpreter() {
        this(30000, new ConsoleView());
    }

    public Tape getTape() { return tape; }

    public void interpret(String sourceCode) {
        if (sourceCode == null)
            throw new IllegalArgumentException("sourceCode argument was null");

        for (int i = 0; i < sourceCode.length(); i++) {
            switch (sourceCode.charAt(i)) {
                case '>':
                    tape.shiftRight();
                    break;

                case '<':
                    tape.shiftLeft();
                    break;

                case '+':
                    tape.increment();
                    break;

                case '-':
                    tape.decrement();
                    break;

                case '.':
                    view.print(tape.getValue());
                    break;

                case ',':
                    tape.setValue(view.read());
                    break;

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
