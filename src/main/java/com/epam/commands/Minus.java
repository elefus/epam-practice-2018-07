package com.epam.commands;

import com.epam.Cells;
import com.epam.View;

public class Minus implements Command {

    @Override
    public void execute(String code, Cells cells, View view) {
        cells.sub();
    }
}
