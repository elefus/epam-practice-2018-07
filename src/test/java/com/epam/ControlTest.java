package com.epam;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class ControlTest {

    @Test
    void testXeqZero() {
        int result = -1;
        try {
            Memory memory = new Memory(20);
            memory.setDataAtCurrentCell('5');

            Control testControl = new Control(false,"test1.bf", memory, new TerminalView());
            testControl.interpret();
            result = memory.getMem()[0];
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        assertEquals(0,result);
    }
    @Test
    void testXeqY(){
        char result = 0;
        Memory memory = new Memory(20);
        memory.getMem()[0]=212;
        memory.getMem()[1]=39;
        try {
            Control control = new Control(false, "test2.bf", memory, new TerminalView());
            control.interpret();
            result = memory.getMem()[0];
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        assertEquals(39,result);
    }
    @Test
    void testSum(){
        char result = 0;
        Memory memory = new Memory(20);
        memory.getMem()[0]=50;
        memory.getMem()[1]=120;
        try {
            Control control = new Control(false, "test3.bf", memory, new TerminalView());
            control.interpret();
            result = memory.getMem()[0];
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        assertEquals(170,result);
    }
    @Test
    void testDiff(){
        char result = 0;
        Memory memory = new Memory(20);
        memory.getMem()[0]=5;
        memory.getMem()[1]=10;
        try {
            Control control = new Control(false, "test4.bf", memory, new TerminalView());
            control.interpret();
            result = memory.getMem()[0];
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        assertEquals(251,result);
    }
    @Test
    void testMult(){
        char result = 0;
        Memory memory = new Memory(20);
        memory.getMem()[0]=5;
        memory.getMem()[1]=2;
        try {
            Control control = new Control(false, "test5.bf", memory, new TerminalView());
            control.interpret();
            result = memory.getMem()[0];
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        assertEquals(10,result);
    }
    @Test
    void testXPowerTwo(){
        char result = 0;
        Memory memory = new Memory(20);
        memory.getMem()[0] = 5;

        try {
            Control control = new Control(false,"test6.bf", memory, new TerminalView());
            control.interpret();
            result = memory.getMem()[0];
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        assertEquals(25,result);
    }
    @Test
    void testDiv(){
        char result = 0;
        Memory memory = new Memory(20);
        memory.getMem()[0]=26;
        memory.getMem()[1]=3;
        try {
            Control control = new Control(false, "test7.bf", memory, new TerminalView());
            control.interpret();
            result = memory.getMem()[0];
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        assertEquals(8,result);
    }

    @Test
    void testPower(){
        char result = 0;
        Memory memory = new Memory(20);
        memory.getMem()[0]=15;
        memory.getMem()[1]=2;
        try {
            Control control = new Control(false, "test8.bf", memory, new TerminalView());
            control.interpret();
            result = memory.getMem()[0];
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
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
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        assertEquals(2, memory.getMem()[0]);
        assertEquals(15, memory.getMem()[1]);
    }
}