package com.epam;

class Model
{
    Model(final int size) throws IllegalArgumentException {
        if ((MAX_ARRAY_SIZE = size) < 1)
            throw new IllegalArgumentException("Size of model cannot be less than 1");

        arrayOfCells = new char[MAX_ARRAY_SIZE];
    }

    public void incrementCellValue() {
        if ((int)++arrayOfCells[actualIndex] > MAX_CELL_SIZE)
            arrayOfCells[actualIndex] = (char) 0;
    }

    public void decrementCellValue() {
        if ((int)--arrayOfCells[actualIndex] < 0)
            arrayOfCells[actualIndex] = (char) MAX_CELL_SIZE;
    }

    public void incrementIndex() {
        actualIndex = actualIndex + 1 > MAX_ARRAY_SIZE ? 0 : actualIndex + 1;
    }

    public void decrementIndex() {
        actualIndex = actualIndex - 1 < 0 ? MAX_ARRAY_SIZE : actualIndex - 1;
    }

    public void setCellValue(char value) {
        arrayOfCells[actualIndex] = value;
    }

    public char getCellValue() {
        return arrayOfCells[actualIndex];
    }

    public int getCellIndex() {
        return actualIndex;
    }

    private int actualIndex;
    private char[] arrayOfCells;
    private int MAX_ARRAY_SIZE;
    private final int MAX_CELL_SIZE = 255;
}
