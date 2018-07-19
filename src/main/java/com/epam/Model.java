package com.epam;

public class Model {

    private char[] array = new char[3000];
    private int index;
    private int maxCellValue;

    public Model(int cellSize) {
        switch (cellSize) {
            case 8: this.maxCellValue = 255; break;
            case 16: this.maxCellValue = 65535; break;
        }
    }

    public void incrementIndex() {
        if (index == 2999) {
            index = 0;
        } else {
            index++;
        }
    }

    public void decrementIndex() {
        if (index == 0) {
            index = 2999;
        } else {
            index--;
        }
    }

    public void incrementValue() {
        if (array[index] == maxCellValue) {
            array[index] = 0;
        } else {
            array[index]++;
        }
    }

    public void decrementValue() {
        if (array[index] == 0) {
            array[index] = (char) maxCellValue;
        } else {
            array[index]--;
        }
    }

    public char getValue() {
        return array[index];
    }

    public void setValue(char value) {
        if (value > (char) maxCellValue) {
            array[index] = 0;
        } else {
            array[index] = value;
        }
    }

    public int getIndex() {
        return index;
    }
}

