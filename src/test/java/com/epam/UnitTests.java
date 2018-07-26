package com.epam;

import com.epam.interpeter.ConsoleView;
import com.epam.interpeter.Controller;
import com.epam.interpeter.Model;
import com.epam.interpeter.OptionParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.FileNotFoundException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;


class UnitTests {
    //........................... Model Tests ......................................
    @Test
    void invalidSizeTest() {
        assertThrows(IllegalArgumentException.class, ()-> new Model(0));
    }

    @Test
    void correctSizeTest() {
        assertAll(()-> new Model(2));
    }

    @Test
    void setAndGetValueTest() {
        Model model = new Model(2);
        model.setCellValue((char) 3);
        assertEquals(3, model.getCellValue());
    }

    @Test
    void incrementCellValueTest() {
        Model model = new Model(2);
        model.incrementCellValue();
        model.incrementCellValue();
        assertEquals(2, model.getCellValue());
    }

    @Test
    void decrementCellValueTest() {
        Model model = new Model(2);
        model.decrementCellValue();
        model.decrementCellValue();
        assertEquals(254, model.getCellValue());
    }

    @Test
    void incrementCellIndexTest() {
        Model model = new Model(10);
        model.incrementCellIndex();
        model.incrementCellIndex();
        assertEquals(2, model.getCellIndex());
    }

    @Test
    void decrementCellIndexTest() {
        Model model = new Model(10);
        model.decrementCellIndex();
        model.decrementCellIndex();
        assertEquals(8, model.getCellIndex());
    }


    @Test
    void getArrayOfCellsTest() {
        Model model = new Model(2);
        model.getCellsArray()[0] = 12;
        model.getCellsArray()[1] = 32;
        model.incrementCellValue();
        model.incrementCellValue();

        model.incrementCellIndex();

        model.incrementCellValue();
        assertEquals(14, model.getCellsArray()[0]);
        assertEquals(33, model.getCellsArray()[1]);
    }
    //...............................................................................


    //.......................... OptionParser Tests ..................................
    @Test
    void missingOptionTest() {
        // Option -f not stated
        assertThrows(MissingOptionException.class, ()-> new OptionParser().parse("test"));
    }

    @Test
    void missingArgumentExceptionOneTest() {
        // File not stated
        assertThrows(MissingArgumentException.class, ()-> new OptionParser().parse("-f", "-s"));
    }

    @Test
    void missingArgumentExceptionTwoTest() {
        // Size not stated
        assertThrows(MissingArgumentException.class, ()-> new OptionParser().parse("-f", "test", "-s"));
    }

    @Test
    void numberFormatExceptionTest() {
        // Size of array is string value
        assertThrows(NumberFormatException.class, ()-> new OptionParser().parse("-f", "test", "-s", "a"));
    }
    //...............................................................................


    //........................... Controller Tests ..................................
    @Test
    void fileNotFoundExceptionTest() {
        assertThrows(FileNotFoundException.class, ()-> new Controller("rwetest534",
                     new Model(1), new ConsoleView(), false));
    }
    //...............................................................................


    //........................... ConsoleView Tests ..................................
    @Test
    void inputDataTest() {
        // Close input stream for testing
        try {
            System.in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThrows(IOException.class, ()-> new ConsoleView().inputData());
    }
    //...............................................................................
}
