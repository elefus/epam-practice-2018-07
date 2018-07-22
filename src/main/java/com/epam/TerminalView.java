package com.epam;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class TerminalView implements View{

    private InputStreamReader input = new InputStreamReader(System.in);

    public void printSymbol(int cell) {
        System.out.println((char) cell);
    }

    public int readSymbol() {
        int symbol = 0;
        try {
            symbol =  input.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return symbol;
    }

    public void print(int[] cells, int currentCell, int delay) {

        for (int cell : cells) {
            System.out.print(cell);
            if (cell < 10) {
                System.out.print("    ");
            } else if (cell < 100) {
                System.out.print("   ");
            } else {
                System.out.print("  ");
            }
        }
        System.out.println();

        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < currentCell; i++) {
            spaces.append("     ");
        }
        String pointer = spaces + "^";
        System.out.println(pointer);
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
