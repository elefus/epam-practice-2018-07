package com.epam.interpreter;

import com.epam.Launcher;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class GuiView implements View {

    @FXML private TextArea outputTextArea;
    @FXML private TextField inputTextField;
    @FXML private TextField fileTextField;
    @FXML private Button launchButton;
    @FXML private Button stopButton;

    private Launcher launcher;
    private Stage stage;

    @Override
    public void print(char value) {
        outputTextArea.appendText(Character.toString(value));
    }

    @Override
    public char read() {
        String text = inputTextField.getText();
        if (text != null && !text.isEmpty()) {
            char result = text.charAt(0);
            text = text.substring(1);
            inputTextField.setText(text);
            return result;
        } else return 0;
    }

    public void setController(Launcher launcher) {
        this.launcher = launcher;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void launchButton_Click(ActionEvent actionEvent) {
        launcher.setFilePath(fileTextField.getText());
        if (launcher.getService().getState() == Worker.State.READY)
            launcher.getService().start();
        else launcher.getService().restart();
        launchButton.setDisable(true);
        stopButton.setDisable(false);
    }

    public void stopButton_Click(ActionEvent actionEvent) {
        launcher.getService().cancel();
    }

    public void controllerFinishedWork() {
        launchButton.setDisable(false);
        stopButton.setDisable(true);
    }

    public void openFileButton_Click(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        FileChooser.ExtensionFilter bfFilter =
                new FileChooser.ExtensionFilter("BF files (*.bf)", "*.bf");
        FileChooser.ExtensionFilter allFilter =
                new FileChooser.ExtensionFilter("All files", "*");
        fileChooser.getExtensionFilters().add(bfFilter);
        fileChooser.getExtensionFilters().add(allFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            fileTextField.setText(file.getAbsolutePath());
        }
    }
}
