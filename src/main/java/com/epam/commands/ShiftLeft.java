package com.epam.commands;

import com.epam.Cells;
import com.epam.View;

public class ShiftLeft implements Command {
    @Override
    public void execute(String code, Cells cells, View view) {
        cells.shiftLeft();
    }
}
