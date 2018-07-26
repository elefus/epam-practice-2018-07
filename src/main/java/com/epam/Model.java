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

    public char getValue(int i) {
        return memory[i];
    }


}