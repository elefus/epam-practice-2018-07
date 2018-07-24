package com.epam;

public class Model {
    private int memLength;
    private char[] mem;
    private int memPointer;

    public Model() {
        memLength = 30000;
        mem = new char[memLength];
        memPointer = 0;
    }

    public Model(int pMemLength) {
        memLength = pMemLength;
        mem = new char[memLength];
        memPointer = 0;
    }

    public void setMemLength(int value) {
        memLength = value;
    }

    public int getMemLength() {
        return memLength;
    }

    public char[] getMem() {
        return mem;
    }

    public char getMemCell() {
        return mem[memPointer];
    }

    public void setMemCell(int c) {
        mem[memPointer] = (char)c;
    }

    public int getMemPointer() {
        return memPointer;
    }

    public void incMemPointer() {
        memPointer++;
    }

    public void decMemPointer() {
        memPointer--;
    }

    public void setMemPointer(int value) {
        memPointer = value;
    }
}
