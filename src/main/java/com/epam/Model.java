package com.epam;

import java.io.IOException;

import static com.epam.Controller.output;

class Model
{
  // Model
  protected static int cell_pointer = 0;
  protected static int MAX_CELL_SIZE = 255;
  protected static int MAX_ARRAY_SIZE;
  protected static char[] array_of_cells = new char[MAX_ARRAY_SIZE];

  public void setArraySize(String array_size) {
    MAX_ARRAY_SIZE = Integer.parseInt(array_size);

    if (MAX_ARRAY_SIZE < 0)
      throw new IndexOutOfBoundsException("Array size less than 0");
    }

  public int getArraySize()
  {
    return MAX_ARRAY_SIZE;
  }

  public class Tokens
  {
    public static final char INPUT     = ',';
    public static final char OUTPUT    = '.';
    public static final char FORWARD   = '>';
    public static final char BACKWARD  = '<';
    public static final char PLUS      = '+';
    public static final char MINUS     = '-';
    public static final char R_BRACKET = ']';
    public static final char L_BRACKET = '[';
  }
}
