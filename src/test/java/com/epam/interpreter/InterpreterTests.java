package com.epam.interpreter;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTests {

  private static int MEMORY_CAPASITY = 3000;

  String readFile(String fileName) throws IOException {
    try (InputStream resourceStream = InterpreterTests.class.getResourceAsStream(fileName);
        InputStreamReader reader = new InputStreamReader(resourceStream);
        BufferedReader bufferedReader = new BufferedReader(reader)) {
      StringBuilder builder = new StringBuilder();
      while (bufferedReader.ready()) {
        builder.append(bufferedReader.readLine() + System.lineSeparator());
      }

      return builder.toString();
    }
  }

  @Test
  void setToZeroTest() throws IOException {
    Model model = new Model(16, MEMORY_CAPASITY);
    Interpreter interpreter = new Interpreter(model, null);

    interpreter.interpret(readFile("./../../../1.bf"));

    assertEquals(0, (int) model.getValue());
  }

  @Test
  void assignmentTest() throws IOException {
    Model model = new Model(16, MEMORY_CAPASITY);
    Interpreter interpreter = new Interpreter(model, null);
    model.setValue((char) 6);
    model.incrementIndex();
    model.setValue((char) 10);

    interpreter.interpret(readFile("./../../../2.bf"));

    assertEquals(10, (int) model.getValue());
  }

  @Test
  void sumTest() throws IOException {
    Model model = new Model(16, MEMORY_CAPASITY);
    Interpreter interpreter = new Interpreter(model, null);
    model.setValue((char) 6);
    model.incrementIndex();
    model.setValue((char) 10);

    interpreter.interpret(readFile("./../../../3.bf"));

    assertEquals(16, (int) model.getValue());
  }

  @Test
  void subtractionTest() throws IOException {
    Model model = new Model(16, MEMORY_CAPASITY);
    Interpreter interpreter = new Interpreter(model, null);
    model.setValue((char) 88);
    model.incrementIndex();
    model.setValue((char) 22);

    interpreter.interpret(readFile("./../../../4.bf"));

    assertEquals(66, (int) model.getValue());
  }

  @Test
  void multiplicationTest() throws IOException {
    Model model = new Model(16, MEMORY_CAPASITY);
    Interpreter interpreter = new Interpreter(model, null);
    model.setValue((char) 20);
    model.incrementIndex();
    model.setValue((char) 5);

    interpreter.interpret(readFile("./../../../5.bf"));

    assertEquals(100, (int) model.getValue());
  }

  @Test
  void squareTest() throws IOException {
    Model model = new Model(16, MEMORY_CAPASITY);
    Interpreter interpreter = new Interpreter(model, null);
    model.setValue((char) 6);

    interpreter.interpret(readFile("./../../../6.bf"));

    assertEquals(36, (int) model.getValue());
  }

  @Test
  void divisionTest() throws IOException {
    Model model = new Model(16, MEMORY_CAPASITY);
    Interpreter interpreter = new Interpreter(model, null);
    model.setValue((char) 66);
    model.incrementIndex();
    model.setValue((char) 3);

    interpreter.interpret(readFile("./../../../7.bf"));

    assertEquals(22, (int) model.getValue());
  }

  @Test
  void toAPowerTest() throws IOException {
    Model model = new Model(16, MEMORY_CAPASITY);
    Interpreter interpreter = new Interpreter(model, null);
    model.setValue((char) 2);
    model.incrementIndex();
    model.setValue((char) 7);

    interpreter.interpret(readFile("./../../../8.bf"));

    assertEquals(128, (int) model.getValue());
  }

  @Test
  void swapTest() throws IOException {
    Model model = new Model(16, MEMORY_CAPASITY);
    Interpreter interpreter = new Interpreter(model, null);
    model.setValue((char) 66);
    model.incrementIndex();
    model.setValue((char) 77);

    interpreter.interpret(readFile("./../../../9.bf"));

    assertEquals(77, (int) model.getValue());
    model.incrementIndex();
    assertEquals(66, (int) model.getValue());
  }
}
