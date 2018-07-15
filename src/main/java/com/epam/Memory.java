package com.epam;

import java.security.InvalidParameterException;

public class Memory {
    private char[] mem;
    private int memIdx = 0;
    private int size;
    public Memory(int size){
        this.size = size;
        if(size<1){
            throw new InvalidParameterException("Memory size can't be less than 1");
        }
        mem = new char[size];
    }

    public void incMemIdx(){
        memIdx = memIdx + 1 == size?0:memIdx + 1;
    }
    public void decMemIdx(){
        memIdx = memIdx == 0?size - 1:memIdx - 1;
    }

    public void incDataAtCurrentCell() {
        mem[memIdx]++;
        if(mem[memIdx] == 256){
            mem[memIdx] = 0;
        }
    }
    public void decDataAtCurrentCell() {
        if(mem[memIdx] == 0){
            mem[memIdx] = 256;
        }
        mem[memIdx]--;

    }

    public void setDataAtCurrentCell(char data) {
        mem[memIdx] = data;
    }
    public char getDataAtCurrentCell() {
        return mem[memIdx];
    }

    public char[] getMem() {
        return mem;
    }

    public int getMemIdx() {
        return memIdx;
    }

}
