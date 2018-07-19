package com.epam;

public class Cells {

    private int[] cells;
    private int currentCell;
    private static int numOfCells;
    private static final int cellSize = 256;

    public Cells(int num) {
        numOfCells = num;
        cells = new int[numOfCells];
        for (int i = 0; i < numOfCells; i++) {
            cells[i] = 0;
        }
    }

    public int[] getCells() {
        return cells;
    }

    public int getCurrentCell() {
        return currentCell;
    }

    public void add() {
        cells[currentCell]++;
        if (cells[currentCell] == cellSize + 1) {
            cells[currentCell] = 0;
        }
    }

    public void sub() {
        cells[currentCell]--;
        if (cells[currentCell] == -1) {
            cells[currentCell] = cellSize;
        }
    }

    public void shiftRight() {
        currentCell++;
        if (currentCell == numOfCells + 1) {
            currentCell = 0;
        }
    }

    public void shiftLeft() {
        currentCell--;
        if (currentCell == -1) {
            currentCell = numOfCells;
        }
    }

    public void input(char symbol) {
        cells[currentCell] = symbol;
    }
}
