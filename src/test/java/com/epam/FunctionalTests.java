package com.epam;

import com.epam.interpeter.ConsoleView;
import com.epam.interpeter.Controller;
import com.epam.interpeter.Model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

class FunctionalTests {
    @Test
    void zeroTest() {
        Model model = new Model(1);
        model.setCellValue((char) 49);

        try {
            new Controller("test1.bf", model, new ConsoleView(),false).process();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(0, model.getCellsArray()[0]);
    }

    @Test
    void assignmentTest() {
        Model model = new Model(3);
        model.getCellsArray()[0] = 20;
        model.getCellsArray()[1] = 40;

        try {
            new Controller("test2.bf", model, new ConsoleView(),false).process();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(40, model.getCellsArray()[0]);
    }

    @Test
    void sumTest() {
        Model model = new Model(3);
        model.getCellsArray()[0] = 20;
        model.getCellsArray()[1] = 40;

        try {
            new Controller("test3.bf", model, new ConsoleView(), false).process();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(60, model.getCellsArray()[0]);
    }

    @Test
    void diffTest() {
        Model model = new Model(4);
        model.getCellsArray()[0] = 8;
        model.getCellsArray()[1] = 19;

        try {
            new Controller("test4.bf", model, new ConsoleView(), false).process();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(245, model.getCellsArray()[0]);
    }

    @Test
    void multiplicationTest() {
        Model model = new Model(10);
        model.getCellsArray()[0] = 9;
        model.getCellsArray()[1] = 8;

        try {
            new Controller("test5.bf", model, new ConsoleView(), false).process();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(72, model.getCellsArray()[0]);
    }

    @Test
    void multiplicationXTest() {
        Model model = new Model(10);
        model.getCellsArray()[0] = 8;

        try {
            new Controller("test6.bf", model, new ConsoleView(), false).process();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(64, model.getCellsArray()[0]);
    }

    @Test
    void divisionTest() {
        Model model = new Model(10);
        model.getCellsArray()[0] = 15;
        model.getCellsArray()[1] = 3;

        try {
            new Controller("test7.bf", model, new ConsoleView(), false).process();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(5, model.getCellsArray()[0]);
    }

    @Test
    void powerTest() {
        Model model = new Model(10);
        model.getCellsArray()[0] = 3;
        model.getCellsArray()[1] = 4;

        try {
            new Controller("test8.bf", model, new ConsoleView(), false).process();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(81, model.getCellsArray()[0]);
    }

    @Test
    void swapTest() {
        Model model = new Model(10);
        model.getCellsArray()[0] = 23;
        model.getCellsArray()[1] = 32;

        try {
            new Controller("test9.bf", model, new ConsoleView(), false).process();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(32, model.getCellsArray()[0]);
        assertEquals(23, model.getCellsArray()[1]);
    }
}
