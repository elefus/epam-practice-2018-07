package com.epam.commands;

import com.epam.Cells;
import com.epam.View;

public interface Command {
    void execute(String code, Cells cells, View view);
}
