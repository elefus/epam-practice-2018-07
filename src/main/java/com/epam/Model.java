package com.epam;

public class Module {
    private char[] memory = null;
    private int memIdx = 0;
    private int size;
    public Module(int size){
        this.size = size;
        memory = new char[size];
    }

    public void incMemIdx(){
        memIdx = memIdx + 1 == size?0:memIdx + 1;
    }
    public void decMemIdx(){
        memIdx = memIdx == 0?size - 1:memIdx - 1;
    }

    public void incDataAtCurrentMemCell() {
        memory[memIdx]++;
    }
    public void decDataAtCurrentMemCell() {
        memory[memIdx]--;
    }

    public void setDataAtCurrentMemCell(int aVoid) {
        memory[memIdx] = (char)(aVoid);
    }
    public char getDataAtCurrentMemCell() {
        return memory[memIdx];
    }

    public char[] getMem() {
        return memory;
    }

    public int getMemIdx() {
        return memIdx;
    }
}
