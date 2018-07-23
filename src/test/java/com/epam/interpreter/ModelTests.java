package com.epam.interpreter;

import com.epam.interpreter.Model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelTests {

  private static int MEMORY_CAPASITY = 3000;

  @Test
  void incrementIndexTest() {
    Model model = new Model(8, MEMORY_CAPASITY);

    for (int i = 0; i < 3000; i++) {
      model.incrementIndex();
    }

    assertEquals(0, model.getIndex());
  }

  @Test
  void decrementIndexTest() {
    Model model = new Model(8, MEMORY_CAPASITY);

    model.decrementIndex();

    assertEquals(2999, model.getIndex());
  }

  @Test
  void incrementValueTest() {
    Model model8 = new Model(8, MEMORY_CAPASITY);
    Model model16 = new Model(16, MEMORY_CAPASITY);

    model8.setValue((char) 255);
    model8.incrementValue();

    model16.setValue((char) 65535);
    model16.incrementValue();

    assertEquals(0, model8.getValue());
    assertEquals(0, model16.getValue());
  }

  @Test
  void decrementValueTest() {
    Model model8 = new Model(8, MEMORY_CAPASITY);
    Model model16 = new Model(16, MEMORY_CAPASITY);

    model8.setValue((char) 0);
    model8.decrementValue();

    model16.setValue((char) 0);
    model16.decrementValue();

    assertEquals(255, model8.getValue());
    assertEquals(65535, model16.getValue());
  }

  @Test
  void setValueTest() {
    Model model8 = new Model(8, MEMORY_CAPASITY);

    model8.setValue((char) 256);

    assertEquals(0, model8.getValue());
  }
}
