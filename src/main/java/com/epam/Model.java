package com.epam;

public class Model {
    private int cellIndex = 0;
    private char[] cellsArray;
    private final int maxArraySize;
    private final int maxCellSize = 256;

    public Model(int size) throws IllegalArgumentException {
        if ( (maxArraySize = size) < 1 )
            throw new IllegalArgumentException("Size of array must be more than 0");
        cellsArray = new char[maxArraySize];
    }

    public void incrementCellIndex() {
        cellIndex = cellIndex == maxArraySize - 1 ? 0: ++cellIndex;
    }

    public void decrementCellIndex() {
        cellIndex = cellIndex == 0 ? maxArraySize - 1: --cellIndex;
    }

    public void incrementCellValue() {
        cellsArray[cellIndex] = cellsArray[cellIndex] + 1 == maxCellSize ? 0 : ++cellsArray[cellIndex];
    }

    public void decrementCellValue() {
        cellsArray[cellIndex] = cellsArray[cellIndex] - 1 < 0 ? maxCellSize - 1 : --cellsArray[cellIndex];
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public char getCellValue() {
        return cellsArray[cellIndex];
    }

    public void setCellValue(char cellValue) {
        cellsArray[cellIndex] = cellValue;
    }

    public char[] getCellsArray() {
        return cellsArray;
    }
}
