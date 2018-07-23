package com.epam.interpreter;

public class Model {

  private static int MAX_8BIT_VALUE = 255;
  private static int MAX_16BIT_VALUE = 65535;

  private char[] array;
  private int index;
  private int maxCellValue;

  public Model(int cellSize, int arraySize) {
    switch (cellSize) {
      case 8:
        this.maxCellValue = MAX_8BIT_VALUE;
        break;

      case 16:
        this.maxCellValue = MAX_16BIT_VALUE;
        break;
    }

    array = new char[arraySize];
  }

  public void incrementIndex() {
    if (index == array.length - 1) {
      index = 0;
    } else {
      index++;
    }
  }

  public void decrementIndex() {
    if (index == 0) {
      index = array.length - 1;
    } else {
      index--;
    }
  }

  public void incrementValue() {
    if (array[index] == maxCellValue) {
      array[index] = 0;
    } else {
      array[index]++;
    }
  }

  public void decrementValue() {
    if (array[index] == 0) {
      array[index] = (char) maxCellValue;
    } else {
      array[index]--;
    }
  }

  public char getValue() {
    return array[index];
  }

  public void setValue(char value) {
    if (value > (char) maxCellValue) {
      array[index] = 0;
    } else {
      array[index] = value;
    }
  }

  public int getIndex() {
    return index;
  }
}
