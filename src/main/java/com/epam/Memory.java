package com.epam;

import java.security.InvalidParameterException;

public class Memory {

  private char[] mem;
  private int memIdx = 0;
  private final int size;

  public Memory(int size) {
  if(size<1){
    throw new InvalidParameterException("Size cannot be less than 1");
  }
    this.size = size;
    mem = new char[size];
  }

  public void incMemIdx() {
    memIdx = (memIdx + 1) % size;
  }

  public void decMemIdx() {
    memIdx = memIdx == 0 ? size - 1 : memIdx - 1;
  }

  public void incDataAtCurrentCell() {
    mem[memIdx] = (char) ((mem[memIdx] + 1) % 256);
  }


  public void decDataAtCurrentCell() {
    if (mem[memIdx] == 0) {
      mem[memIdx] = 256;
    }
    mem[memIdx] = mem[memIdx] == 0 ? 255 : --mem[memIdx];
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

  public void reset() {
    mem = new char[size];
    memIdx = 0;
  }
}
