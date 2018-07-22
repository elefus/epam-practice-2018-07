package com.epam.commands;

import com.epam.Cells;
import com.epam.View;

public class ReadSymbol implements Command {
    @Override
    public void execute(String code, Cells cells, View view) {
        int symbol = view.readSymbol();
        cells.input((char) symbol);
    }
}
