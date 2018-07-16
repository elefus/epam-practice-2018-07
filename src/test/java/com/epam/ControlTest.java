package com.epam;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControlTest {

    @Test
    void testXeqZero() {
        int result;
        Memory memory = new Memory(20);
        memory.setDataAtCurrentCell('5');

        Control testControl = new Control(false,"test1.bf", memory, new TerminalView());
        try {
            testControl.interpret();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        result = memory.getMem()[0];
        assertEquals(0,result);
    }
    @Test
    void testXeqY(){
        char result;
        Memory memory = new Memory(20);
        memory.getMem()[0]=212;
        memory.getMem()[1]=39;
        Control control = new Control(false, "test2.bf", memory, new TerminalView());
        try {
            control.interpret();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        result = memory.getMem()[0];
        assertEquals(39,result);
    }
    @Test
    void testSum(){
        char result;
        Memory memory = new Memory(20);
        memory.getMem()[0]=50;
        memory.getMem()[1]=120;
        Control control = new Control(false, "test3.bf", memory, new TerminalView());
        try {
            control.interpret();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        result = memory.getMem()[0];
        assertEquals(170,result);
    }
    @Test
    void testDiff(){
        char result;
        Memory memory = new Memory(20);
        memory.getMem()[0]=5;
        memory.getMem()[1]=10;
        Control control = new Control(false, "test4.bf", memory, new TerminalView());
        try {
            control.interpret();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        result = memory.getMem()[0];
        assertEquals(251,result);
    }
    @Test
    void testMult(){
        char result;
        Memory memory = new Memory(20);
        memory.getMem()[0]=5;
        memory.getMem()[1]=2;
        Control control = new Control(false, "test5.bf", memory, new TerminalView());
        try {
            control.interpret();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        result = memory.getMem()[0];
        assertEquals(10,result);
    }
    @Test
    void testXPowerTwo(){
        char result;
        Memory memory = new Memory(20);
        memory.getMem()[0] = 5;

        Control control = new Control(false,"test6.bf", memory, new TerminalView());
        try {
            control.interpret();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        result = memory.getMem()[0];
        assertEquals(25,result);
    }
    @Test
    void testDiv(){
        char result;
        Memory memory = new Memory(20);
        memory.getMem()[0]=26;
        memory.getMem()[1]=3;
        Control control = new Control(false, "test7.bf", memory, new TerminalView());
        try {
            control.interpret();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        result = memory.getMem()[0];
        assertEquals(8,result);
    }

    @Test
    void testPower(){
        char result;
        Memory memory = new Memory(20);
        memory.getMem()[0]=15;
        memory.getMem()[1]=2;
        try {
            Control control = new Control(false, "test8.bf", memory, new TerminalView());
            control.interpret();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        result = memory.getMem()[0];
        assertEquals(225,result);
    }
    @Test
    void testSwap(){
        Memory memory = new Memory(20);
        memory.getMem()[0]=15;
        memory.getMem()[1]=2;
        try {
            Control control = new Control(false, "test9.bf", memory, new TerminalView());
            control.interpret();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        assertEquals(2, memory.getMem()[0]);
        assertEquals(15, memory.getMem()[1]);
    }
}