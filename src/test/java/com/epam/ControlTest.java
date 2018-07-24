package com.epam;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.junit.jupiter.api.Test;

class ControlTest {

  @Test
  void testXeqZero() {
    int result;
    Memory memory = new Memory(20);
    memory.setDataAtCurrentCell('5');

    try {
      Control testControl = new Control(false, "test1.bf", memory, new TerminalView());
      testControl.interpret();
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    result = memory.getMem()[0];
    assertEquals(0, result);
  }

  @Test
  void testXeqY() {
    char result;
    Memory memory = new Memory(20);
    memory.getMem()[0] = 212;
    memory.getMem()[1] = 39;
    try {
      Control control = new Control(false, "test2.bf", memory, new TerminalView());
      control.interpret();
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    result = memory.getMem()[0];
    assertEquals(39, result);

  }

  @Test
  void testSum() {
    char result;
    Memory memory = new Memory(20);
    memory.getMem()[0] = 50;
    memory.getMem()[1] = 120;
    try {
      Control control = new Control(false, "test3.bf", memory, new TerminalView());
      control.interpret();
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    result = memory.getMem()[0];
    assertEquals(170, result);
  }

  @Test
  void testDiff() {
    char result;
    Memory memory = new Memory(20);
    memory.getMem()[0] = 5;
    memory.getMem()[1] = 10;
    try {
      Control control = new Control(false, "test4.bf", memory, new TerminalView());
      control.interpret();
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    result = memory.getMem()[0];
    assertEquals(251, result);
  }

  @Test
  void testMult() {
    char result;
    Memory memory = new Memory(20);
    memory.getMem()[0] = 5;
    memory.getMem()[1] = 2;
    try {
      Control control = new Control(false, "test5.bf", memory, new TerminalView());
      control.interpret();
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    result = memory.getMem()[0];
    assertEquals(10, result);
  }

  @Test
  void testXPowerTwo() {
    char result;
    Memory memory = new Memory(20);
    memory.getMem()[0] = 5;

    try {
      Control control = new Control(false, "test6.bf", memory, new TerminalView());
      control.interpret();
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    result = memory.getMem()[0];
    assertEquals(25, result);
  }

  @Test
  void testDiv() {
    char result;
    Memory memory = new Memory(20);
    memory.getMem()[0] = 26;
    memory.getMem()[1] = 3;
    try {
      Control control = new Control(false, "test7.bf", memory, new TerminalView());
      control.interpret();
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    result = memory.getMem()[0];
    assertEquals(8, result);
  }

  @Test
  void testPower() {
    char result;
    Memory memory = new Memory(20);
    memory.getMem()[0] = 15;
    memory.getMem()[1] = 2;
    try {
      Control control = new Control(false, "test8.bf", memory, new TerminalView());
      control.interpret();
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    result = memory.getMem()[0];
    assertEquals(225, result);
  }

  @Test
  void testSwap() {
    Memory memory = new Memory(20);
    memory.getMem()[0] = 15;
    memory.getMem()[1] = 2;
    try {
      Control control = new Control(false, "test9.bf", memory, new TerminalView());
      control.interpret();
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    assertEquals(2, memory.getMem()[0]);
    assertEquals(15, memory.getMem()[1]);
  }

  @Test
  void setCodeTest() {
    StringBuilder code = new StringBuilder();
    String line;
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(getClass().getResourceAsStream("./../../test1.bf")))) {
      while ((line = reader.readLine()) != null) {
        code.append(line)
            .append(System.lineSeparator());
      }
      Control control = new Control(false, "test1.bf", new Memory(20), new TerminalView());
      assertEquals(code.toString(), control.getCode());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}