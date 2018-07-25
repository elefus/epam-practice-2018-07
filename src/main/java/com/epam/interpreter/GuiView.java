package com.epam.interpreter;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class GuiView implements View {

    @FXML
    private TextArea outputTextArea;

    @FXML
    private TextField inputTextField;

    @Override
    public void print(char value) {
        outputTextArea.appendText(Character.toString(value));
    }

    @Override
    public char read() {
        return 0;
    }
}
