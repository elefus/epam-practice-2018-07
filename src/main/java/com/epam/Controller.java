package com.epam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Controller
{
  private static BufferedReader input;
  private static int pointer = 0;
  private static int MAX_CELL_SIZE = 255;
  private static int MAX_ARRAY_SIZE = 30000;
  private static char[] array_of_cells = new char[MAX_ARRAY_SIZE];

  public void parsing(String file)
  {
    try (InputStreamReader in = new InputStreamReader(Input.class.getResourceAsStream("./../../" + file));
         BufferedReader buf = new BufferedReader(in))
    {
      input = new BufferedReader(new InputStreamReader(System.in));
      String new_line;
      StringBuilder string = new StringBuilder();

      while((new_line = buf.readLine()) != null)
        string.append(new_line);

      process(string.toString().toCharArray());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void process(char[] code_array) throws IOException {
    int bracket_counter = 0;
    int current_pointer = 0;

    for( ;current_pointer < code_array.length; current_pointer++)
    {
      switch (code_array[current_pointer])
      {
        case '<' : {
          try {
            if (pointer - 1 >= 0)
              pointer--;
            else
              throw new IndexOutOfBoundsException("Pointer cell less than " + MAX_ARRAY_SIZE);
          } catch (IndexOutOfBoundsException e) {
            e.getMessage();
          }

          break;
        }
        case '>' : {
          try {
            if (pointer + 1 < MAX_ARRAY_SIZE)
              pointer++;
            else
              throw new IndexOutOfBoundsException("Pointer cell bigger than " + MAX_ARRAY_SIZE);
          } catch (IndexOutOfBoundsException e) {
            e.getMessage();
          }

          break;
        }
        case '+' : {
          if ((int)array_of_cells[pointer] + 1 > MAX_CELL_SIZE)
            array_of_cells[pointer] = '0';
          else
            array_of_cells[pointer]++;

          break;
        }
        case '-' : {
          if ((int)array_of_cells[pointer] - 1 < 0) {
            array_of_cells[pointer] = (char) MAX_CELL_SIZE;
          } else {
            array_of_cells[pointer]--;
          }

          break;
        }
        case '[' : {
          if ((int)array_of_cells[pointer] == 0) {
            current_pointer = current_pointer + 1;

            while(code_array[current_pointer] != ']' || bracket_counter > 0)
            {
              if(code_array[current_pointer] == '[')
                bracket_counter++;
              else if(code_array[current_pointer] == ']')
                bracket_counter--;

              current_pointer++;
            }
          }

          break;
        }
        case ']' : {
          if ((int)array_of_cells[pointer] != 0) {
            current_pointer--;

            while(code_array[current_pointer] != '[' || bracket_counter > 0)
            {
              if(code_array[current_pointer] == ']')
                bracket_counter++;
              else if(code_array[current_pointer] == '[')
                bracket_counter--;

              current_pointer--;
            }

            current_pointer--;
          }

          break;
        }
        case '.' : {
          if((int)array_of_cells[pointer] == 0)
            System.out.print("0 ");
          else
            System.out.print((int)array_of_cells[pointer] + " ");

          break;
        }
        case ',' : {
          array_of_cells[pointer] = (char)input.read();
          break;
        }
      }
    }
  }
}
