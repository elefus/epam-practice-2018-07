package com.epam;

public class Model {
    private int arrayIndex;
    private int maxArraySize;
    private char[] arrayOfCells;
    private final int maxCellSize = 256;

    Model(int maxArraySize) throws IllegalArgumentException {
        if ((this.maxArraySize = maxArraySize) < 1)
            throw new IllegalArgumentException("Size of array must be more than 0");
        arrayOfCells = new char[maxArraySize];
    }

    void incArrayIndex() {
        if (++arrayIndex == maxArraySize)
            arrayIndex = 0;
    }

    void decArrayIndex() {
        if (--arrayIndex < 0)
            arrayIndex = maxArraySize - 1;
    }

    int getArrayIndex() {
        return arrayIndex;
    }

    void incCellValue() {
        if (++arrayOfCells[arrayIndex] == maxCellSize)
            arrayOfCells[arrayIndex] = 0;
    }

    void decCellValue() {
        if (--arrayOfCells[arrayIndex] < 0)
            arrayOfCells[arrayIndex] = maxCellSize - 1;
    }

    char getCellValue() {
        return arrayOfCells[arrayIndex];
    }

    void setCellValue(char cellValue) {
        arrayOfCells[arrayIndex] = cellValue;
    }

    public char[] getArrayOfCells() {
        return arrayOfCells;
    }
}
