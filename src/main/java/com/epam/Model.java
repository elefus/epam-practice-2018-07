package com.epam;

class Model {

    int memorySize;
    public static int ptr;
    char[] memory;
    private int maxMemorySize = 30000;
    private final int maxCellSize = 255;

    Model(int size) {
        if ((size != 0) && (size < 30000)) memorySize = size;
        else memorySize = maxMemorySize;
        memory = new char[memorySize];
    }

    public void inc() {
        memory[ptr]++;
        if (memory[ptr] + 1 > maxCellSize)
            memory[ptr] = 0;

    }

    public void dec() {
        memory[ptr]--;
        if (memory[ptr] - 1 < 0)
            memory[ptr] = maxCellSize;
    }

    public void shiftRight() {
        if (ptr == memorySize - 1)
            ptr = 0;
        else ptr++;
    }


    public void shiftLeft() {
        if (ptr - 1 < memorySize)
            ptr = 0;
        else ptr--;
    }

    public void setValue(char value) {
        memory[ptr] = value;
    }

    public char getValue() {
        return memory[ptr];
    }

    public int getPointer() {
        return ptr;
    }

    public char[] getCellsArray() {
        return memory;


    }

    public char getValue(int i) {
        return memory[i];
    }
    void setVal(int index, char value){
        memory[ptr] = value;
    }
    char getVal(int index) {
        return memory[index];
    }



}