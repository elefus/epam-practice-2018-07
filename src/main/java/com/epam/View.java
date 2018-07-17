package com.epam;

import java.util.concurrent.TimeUnit;

public class View {
    public static void print(int[] cells, int currentCell, int delay) {

        for (int i = 0; i < cells.length; i++) {
            System.out.print(cells[i]);
            if (cells[i] < 10) {
                System.out.print("    ");
            } else if (cells[i] < 100) {
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
