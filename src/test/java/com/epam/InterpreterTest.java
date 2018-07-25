package com.epam;

import com.epam.interpreter.Interpreter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class InterpreterTest {

    private Interpreter initializeInterpreter(int first) {
        Interpreter interpreter = new Interpreter();
        interpreter.getTape().setValue((char) first);
        return interpreter;
    }

    private Interpreter initializeInterpreter(int first, int second) {
        Interpreter interpreter = new Interpreter();
        interpreter.getTape().setValue((char) first);
        interpreter.getTape().shiftRight();
        interpreter.getTape().setValue((char) second);
        interpreter.getTape().shiftLeft();
        return interpreter;
    }

    @Test
    void assignZeroTest() throws IOException {
        Interpreter interpreter = initializeInterpreter(56);
        String code = Common.readAllLines("3.bf");

        interpreter.interpret(code);

        assertEquals(interpreter.getTape().getValue(), 0);
    }

    @Test
    void copyingTest() throws IOException {
        Interpreter interpreter = initializeInterpreter(23, 44);
        String code = Common.readAllLines("4.bf");

        interpreter.interpret(code);

        assertEquals(interpreter.getTape().getValue(), 44);
        interpreter.getTape().shiftRight();
        assertEquals(interpreter.getTape().getValue(), 44);
    }

    @Test
    void additionTest() throws IOException {
        Interpreter interpreter = initializeInterpreter(10, 15);
        String code = Common.readAllLines("5.bf");

        interpreter.interpret(code);

        assertEquals(interpreter.getTape().getValue(), 25);
        interpreter.getTape().shiftRight();
        assertEquals(interpreter.getTape().getValue(), 15);
    }

    @Test
    void subtractionTest() throws IOException {
        Interpreter interpreter = initializeInterpreter(12, 5);
        String code = Common.readAllLines("6.bf");

        interpreter.interpret(code);

        assertEquals(interpreter.getTape().getValue(), 7);
        interpreter.getTape().shiftRight();
        assertEquals(interpreter.getTape().getValue(), 5);
    }

    @Test
    void multiplicationTest() throws IOException {
        Interpreter interpreter = initializeInterpreter(9, 5);
        String code = Common.readAllLines("7.bf");

        interpreter.interpret(code);

        assertEquals(interpreter.getTape().getValue(), 45);
        interpreter.getTape().shiftRight();
        assertEquals(interpreter.getTape().getValue(), 5);
    }

    @Test
    void squaringTest() throws IOException {
        Interpreter interpreter = initializeInterpreter(7);
        String code = Common.readAllLines("8.bf");

        interpreter.interpret(code);

        assertEquals(interpreter.getTape().getValue(), 49);
    }

    @Test
    void divisionTest() throws IOException {
        Interpreter interpreter = initializeInterpreter(12, 4);
        String code = Common.readAllLines("9.bf");

        interpreter.interpret(code);

        assertEquals(interpreter.getTape().getValue(), 3);
        interpreter.getTape().shiftRight();
        assertEquals(interpreter.getTape().getValue(), 4);
    }

    @Test
    void exponentiationTest() throws IOException {
        Interpreter interpreter = initializeInterpreter(2, 4);
        String code = Common.readAllLines("10.bf");

        interpreter.interpret(code);

        assertEquals(interpreter.getTape().getValue(), 16);
        interpreter.getTape().shiftRight();
        // There is a bug in the Brainfuck program.
        // It loses the second argument (y)
        assertEquals(interpreter.getTape().getValue(), 0);
    }

    @Test
    void swapTest() throws IOException {
        Interpreter interpreter = initializeInterpreter(64, 28);
        String code = Common.readAllLines("11.bf");

        interpreter.interpret(code);

        assertEquals(interpreter.getTape().getValue(), 28);
        interpreter.getTape().shiftRight();
        assertEquals(interpreter.getTape().getValue(), 64);
    }

    @Test
    void cyclicTapeTest() {
        Interpreter interpreter = new Interpreter();
        interpreter.getTape().setValue((char) 112);

        for (int i = 0; i < 30000; i++) {
            interpreter.getTape().shiftRight();
        }

        assertEquals(interpreter.getTape().getValue(), 112);
    }

}
