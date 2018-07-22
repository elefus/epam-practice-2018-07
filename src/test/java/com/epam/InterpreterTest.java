package com.epam;

import org.junit.jupiter.api.Test;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    private String getCode(String fileName) throws IOException {
        try (InputStream input = InterpreterTest.class.getResourceAsStream("./../../" + fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))){
            StringBuilder builder = new StringBuilder();
            while (reader.ready()) {
                builder.append(reader.readLine() + System.lineSeparator());
            }

            return builder.toString();
        }
    }

    @Test
    void setToZeroTest() throws IOException {
        Model model = new Model(20);
        Controller controller = new Controller(model, new View());

        controller.interpreter(getCode("1.bf"));

        assertEquals(0, (int) model.getVal(0));
    }

    @Test
    void assignmentTest() throws IOException {
        Model model = new Model(20);
        Controller controller = new Controller(model, new View());
        model.setVal(0, '5');
        model.setVal(1, '9');
        controller.interpreter(getCode("2.bf"));

        assertEquals( '9', model.getVal(0));
    }

    @Test
    void sumTest() throws IOException {
        Model model = new Model(16);
        Controller controller = new Controller(model, new View());
        model.setVal(0, '0');
        model.setVal(1, '1');

        controller.interpreter(getCode("3.bf"));

        assertEquals('a', model.getVal(0));
    }

    @Test
    void subtractionTest() throws IOException {
        Model model = new Model(16);
        Controller controller = new Controller(model, new View());
        model.setVal(0, 'a');
        model.setVal(1, '1');

        controller.interpreter(getCode("4.bf"));

        assertEquals('0', model.getVal(0));
    }

    @Test
    void multiplicationTest() throws IOException {
        Model model = new Model(16);
        Controller controller = new Controller(model, new View());

        controller.interpreter(getCode("5.bf"));

        assertEquals(12, (int) model.getVal(0));
    }

    @Test
    void squareTest() throws IOException {
        Model model = new Model(16);
        Controller controller = new Controller(model, new View());
        int c = 10;
        model.setVal(0,(char) c);

        controller.interpreter(getCode("6.bf"));

        assertEquals('d', (int) model.getVal(0));
    }

    @Test
    void divisionTest() throws IOException {
        Model model = new Model(16);
        Controller controller = new Controller(model, new View());
        model.setVal(0, (char) 64);
        model.setVal(1,(char) 2);

        controller.interpreter(getCode("7.bf"));

        assertEquals(32, (int) model.getVal(0));
    }

    @Test
    void toAPowerTest() throws IOException {
        Model model = new Model(16);
        Controller controller = new Controller(model, new View());
        controller.interpreter(getCode("8.bf"));

        assertEquals('@', (int) model.getVal(0));
    }

    @Test
    void swapTest() throws IOException {
        Model model = new Model(16);
        Controller controller = new Controller(model, new View());
        model.setVal(0,(char) 66);
        model.setVal(1,(char) 77);

        controller.interpreter(getCode("9.bf"));

        assertEquals(77, (int) model.getVal(0));
        assertEquals(66, (int) model.getVal(1));
    }
}