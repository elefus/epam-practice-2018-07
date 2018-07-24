package com.epam.commands;

import com.epam.Cells;
import com.epam.Controller;
import com.epam.View;

public class LeftBracket implements Command {
    @Override
    public void execute(String code, Cells cells, View view) {
        if (cells.getCells()[cells.getCurrentCell()] == 0) {
            while (Controller.openedBrackets != 0 || code.charAt(Controller.instructionNumber) != ']') {
                Controller.instructionNumber++;
                if (code.charAt(Controller.instructionNumber) == '[') {
                    Controller.openedBrackets++;
                }
                if (code.charAt(Controller.instructionNumber) == ']') {
                    Controller.openedBrackets--;
                }
            }
        }
    }
}
