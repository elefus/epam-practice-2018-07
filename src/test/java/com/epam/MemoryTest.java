package com.epam;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MemoryTest {

    @Test
    void incMemIdx() {
        Memory mem = new Memory(20);
        int currentIdx = mem.getMemIdx();
        mem.incMemIdx();
        assertEquals(currentIdx+1,mem.getMemIdx());
    }

    @Test
    void decMemIdx() {
        Memory mem = new Memory(20);
        mem.decMemIdx();
        assertEquals(19,mem.getMemIdx());
    }

    @Test
    void incDataAtCurrentCell() {
        Memory mem = new Memory(20);
        int currentData = mem.getDataAtCurrentCell();
        mem.incDataAtCurrentCell();
        assertEquals(currentData+1,mem.getDataAtCurrentCell());
    }

    @Test
    void decDataAtCurrentCell() {
        Memory mem = new Memory(20);
        mem.decDataAtCurrentCell();
        assertEquals(255,mem.getDataAtCurrentCell());
    }

    @Test
    void setDataAtCurrentCell() {
        Memory mem = new Memory(20);
        mem.setDataAtCurrentCell('5');
        assertEquals('5',mem.getDataAtCurrentCell());
    }

    @Test
    void getDataAtCurrentCell() {
        Memory mem = new Memory(20);
        mem.setDataAtCurrentCell('9');
        assertEquals('9',mem.getMem()[0]);
    }
}