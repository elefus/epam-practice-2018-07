package com.epam;

import com.epam.Interpreter.Controller;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControlTest {

    @Test
    void zeroingTest() {
        Controller controller = new Controller("test1.bf");
        controller.setInStream(IOUtils.toInputStream("5", StandardCharsets.UTF_8));
        controller.prepare();
        controller.interpret();
        char[] ch = new char[2];
        controller.getModel().setMemPointer(0);
        ch[0] = controller.getModel().getMemCell();
        controller.getModel().setMemPointer(1);
        ch[1] = controller.getModel().getMemCell();
        assertEquals((byte)ch[0], 0);
    }

    @Test
    void copyTest() {
        Controller controller = new Controller("test2.bf");
        controller.setInStream(IOUtils.toInputStream("65", StandardCharsets.UTF_8));
        controller.prepare();
        controller.interpret();
        char[] ch = new char[2];
        controller.getModel().setMemPointer(0);
        ch[0] = controller.getModel().getMemCell();
        controller.getModel().setMemPointer(1);
        ch[1] = controller.getModel().getMemCell();
        assertEquals((byte)ch[0], 53);
        assertEquals((byte)ch[1], 53);
    }

    @Test
    void increaseTest() {
        Controller controller = new Controller("test3.bf");
        controller.setInStream(IOUtils.toInputStream("65", StandardCharsets.UTF_8));
        controller.prepare();
        controller.interpret();
        char[] ch = new char[2];
        controller.getModel().setMemPointer(0);
        ch[0] = controller.getModel().getMemCell();
        controller.getModel().setMemPointer(1);
        ch[1] = controller.getModel().getMemCell();
        assertEquals((byte)ch[0], 107);
        assertEquals((byte)ch[1], 53);
    }

    @Test
    void decreaseTest() {
        Controller controller = new Controller("test4.bf");
        controller.setInStream(IOUtils.toInputStream("65", StandardCharsets.UTF_8));
        controller.prepare();
        controller.interpret();
        char[] ch = new char[2];
        controller.getModel().setMemPointer(0);
        ch[0] = controller.getModel().getMemCell();
        controller.getModel().setMemPointer(1);
        ch[1] = controller.getModel().getMemCell();
        assertEquals((byte)ch[0], 1);
        assertEquals((byte)ch[1], 53);
    }

    @Test
    void multipleTest() {
        Controller controller = new Controller("test5.bf");
        controller.setInStream(IOUtils.toInputStream("\n\b", StandardCharsets.UTF_8));
        controller.prepare();
        controller.interpret();
        char[] ch = new char[2];
        controller.getModel().setMemPointer(0);
        ch[0] = controller.getModel().getMemCell();
        controller.getModel().setMemPointer(1);
        ch[1] = controller.getModel().getMemCell();
        assertEquals((byte)ch[0], 80);
        assertEquals((byte)ch[1], 8);
    }

    @Test
    void squareTest() {
        Controller controller = new Controller("test6.bf");
        controller.setInStream(IOUtils.toInputStream("\n\n", StandardCharsets.UTF_8));
        controller.prepare();
        controller.interpret();
        char[] ch = new char[2];
        controller.getModel().setMemPointer(0);
        ch[0] = controller.getModel().getMemCell();
        controller.getModel().setMemPointer(1);
        ch[1] = controller.getModel().getMemCell();
        assertEquals((byte)ch[0], 100);
    }

    @Test
    void divideTest() {
        Controller controller = new Controller("test7.bf");
        controller.setInStream(IOUtils.toInputStream("P\n", StandardCharsets.UTF_8));
        controller.prepare();
        controller.interpret();
        char[] ch = new char[2];
        controller.getModel().setMemPointer(0);
        ch[0] = controller.getModel().getMemCell();
        controller.getModel().setMemPointer(1);
        ch[1] = controller.getModel().getMemCell();
        assertEquals((byte)ch[0], 8);
        assertEquals((byte)ch[1], 10);
    }

    @Test
    void powerTest() {
        Controller controller = new Controller("test8.bf");
        controller.setInStream(IOUtils.toInputStream("\2\3", StandardCharsets.UTF_8));
        controller.prepare();
        controller.interpret();
        char[] ch = new char[2];
        controller.getModel().setMemPointer(0);
        ch[0] = controller.getModel().getMemCell();
        controller.getModel().setMemPointer(1);
        ch[1] = controller.getModel().getMemCell();
        assertEquals((byte)ch[0], 8);
    }

    @Test
    void swapTest() {
        Controller controller = new Controller("test9.bf");
        controller.setInStream(IOUtils.toInputStream("65", StandardCharsets.UTF_8));
        controller.prepare();
        controller.interpret();
        char[] ch = new char[2];
        controller.getModel().setMemPointer(0);
        ch[0] = controller.getModel().getMemCell();
        controller.getModel().setMemPointer(1);
        ch[1] = controller.getModel().getMemCell();
        assertEquals((byte)ch[0], 53);
        assertEquals((byte)ch[1], 54);
    }
}