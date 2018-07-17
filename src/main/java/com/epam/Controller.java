package com.epam;

import java.io.*;

class Controller extends Model
{
  // IO
  private static BufferedReader input;
  private static BufferedWriter output;

  // Tracing
  private boolean hasTrace = false;
  public void setTrace() {
    hasTrace = true;
  }

  public void initialize(String file_name, String new_size) {
    try {
      setArraySize(new_size);
      initialize(file_name);
    }
    catch (NumberFormatException e) {
      System.out.printf("Incorrect array size : '%s'%n", new_size);
    }
    catch (IndexOutOfBoundsException e) {
      System.out.println(e.getMessage());
    }
  }

  public void initialize(String file_name) {

    try (InputStreamReader inp = new InputStreamReader(Input.class.getResourceAsStream("./../../" + file_name));
         BufferedReader buf = new BufferedReader(inp);
         BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out)))
    {
      input = reader;
      output = writer;

      String new_line;
      StringBuilder string = new StringBuilder();

      while((new_line = buf.readLine()) != null)
        string.append(new_line);

      process_commands(string.toString().toCharArray());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void process_commands(char[] code_array) throws IOException {

    int bracket_counter = 0;
    int current_pointer = 0;

    for( ;current_pointer < code_array.length; current_pointer++)
    {
      switch (code_array[current_pointer])
      {
        case Tokens.FORWARD : {
          try {
            if (cell_pointer + 1 >= MAX_ARRAY_SIZE)
              throw new IndexOutOfBoundsException("Pointer cell bigger than " + MAX_ARRAY_SIZE);
            else
              cell_pointer++;
          } catch (IndexOutOfBoundsException e) {
            e.getMessage();
          }

          break;
        }
        case Tokens.BACKWARD : {
          try {
            if (cell_pointer - 1 < 0)
              throw new IndexOutOfBoundsException("Pointer cell less than " + MAX_ARRAY_SIZE);
            else
              cell_pointer--;
          } catch (IndexOutOfBoundsException e) {
            e.getMessage();
          }

          break;
        }
        case Tokens.PLUS : {
          if ((int)array_of_cells[cell_pointer] + 1 > MAX_CELL_SIZE)
            array_of_cells[cell_pointer] = (char) 0;
          else
            array_of_cells[cell_pointer]++;

          break;
        }
        case Tokens.MINUS : {
          if ((int)array_of_cells[cell_pointer] - 1 < 0) {
            array_of_cells[cell_pointer] = (char) MAX_CELL_SIZE;
          } else {
            array_of_cells[cell_pointer]--;
          }

          break;
        }
        case Tokens.L_BRACKET : {
          if ((int)array_of_cells[cell_pointer] == 0) {
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
        case Tokens.R_BRACKET : {
          if ((int) array_of_cells[cell_pointer] != 0) {
            current_pointer--;

            while (code_array[current_pointer] != '[' || bracket_counter > 0) {
              if (code_array[current_pointer] == ']')
                bracket_counter++;
              else if (code_array[current_pointer] == '[')
                bracket_counter--;

              current_pointer--;
            }

            current_pointer--;
          }

          break;
        }
        case Tokens.INPUT : {
          array_of_cells[cell_pointer] = (char)input.read();
          break;
        }
        case Tokens.OUTPUT : {
          if((int)array_of_cells[cell_pointer] == 0)
            output.write('0');
          else
            output.write((int)array_of_cells[cell_pointer] + " ");
          break;
        }
      }
    }
  }
}
