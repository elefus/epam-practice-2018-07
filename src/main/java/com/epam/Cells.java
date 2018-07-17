package com.epam;

public class Cells {

    public static int[] cells;
    public static int currentCell;
    private static int numOfCells;
    private static final int cellSize = 256;

    public static void create(int num){
        numOfCells = num;
        cells = new int[numOfCells];
        for (int i = 0; i < numOfCells; i++) {
            cells[i] = 0;
        }
    }

    public static int[] getCells(){
        return cells;
    }

    public static int getCurrentCell(){
        return currentCell;
    }

    public static void add(){
        cells[currentCell]++;
        if (cells[currentCell] == cellSize+1) {
            cells[currentCell] = 0;
        }
    }

    public static void sub(){
        cells[currentCell]--;
        if (cells[currentCell] == -1) {
            cells[currentCell] = cellSize;
        }
    }

    public static void shiftRight(){
        currentCell++;
        if (currentCell == numOfCells+1){
            currentCell = 0;
        }
    }

    public static void shiftLeft(){
        currentCell--;
        if (currentCell == -1){
            currentCell = numOfCells;
        }
    }

    public static void input(char symbol){
        cells[currentCell] = symbol;
    }
}
