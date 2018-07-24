package com.epam.commands;

import com.epam.Cells;
import com.epam.View;

public class PrintSymbol implements Command {
    @Override
    public void execute(String code, Cells cells, View view) {
        view.printSymbol(cells.getCells()[cells.getCurrentCell()]);
    }
}
