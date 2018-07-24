package com.epam;

import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalView implements View {

  private final InputStreamReader console = new InputStreamReader(System.in);

  @Override
  public char requestInput() throws IOException {
    System.out.print("Awaiting input: ");
    return (char) console.read();
  }

  @Override
  public void printData(char dataAtCurrentMemCell) {
    System.out.println(dataAtCurrentMemCell);
  }

  @Override
  public void printMem(char[] memory, int p_position, int c) {

    for (char aMemory : memory) {
      System.out.print(((int) aMemory) + ", ");
    }
    System.out.println();

    System.out.println(
        "Current memory pointer position: [" + p_position + "] ,current operation: [" + c + "]");
  }
}
