package com.epam;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class GuiView extends JFrame implements View{
    private JPanel mainPanel;
    private JTextArea cellsTextArea;
    private JButton runButton;
    private JButton stepButton;
    private JButton stopButton;
    private JPanel Buttons;
    private JTextArea codeTextArea;
    private JTextField inputTextField;
    private JButton submitButton;
    private JTextField exportTextField;
    private JButton exportButton;
    private JSlider delaySlider;
    private JPanel code;
    private JPanel input;
    private JPanel output;
    private JTextArea outputTextArea;
    private boolean pressedInputButton;

    public static void main(String[] args) {
        new GuiView();
    }

    public void initListeners(Cells cells) {
        delaySlider.addChangeListener(e -> Controller.delay = delaySlider.getValue());
        exportButton.addActionListener(e -> {
            codeTextArea.setText("");
            if (exportTextField.getText().equals("")) {
                return;
            }
            try {
                codeTextArea.append(Controller.getCode(exportTextField.getText()));
            } catch (IOException | URISyntaxException e1) {
                codeTextArea.setText(e1.getMessage());
            }
        });
        runButton.addActionListener(e -> {
            outputTextArea.setText("");
            Controller.restart(cells);
            runButton.setEnabled(false);
            Controller.stop = false;
            View guiView = this;
            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() {
                    Controller.interpret(codeTextArea.getText(), cells, guiView);
                    runButton.setEnabled(true);
                    return null;
                }
            };
            swingWorker.execute();
        });
        stopButton.addActionListener(e -> {
            Controller.stop = true;
            runButton.setEnabled(true);
        });
        submitButton.addActionListener(e -> pressedInputButton = true);
    }

    public GuiView() {
        this.setContentPane(mainPanel);
        this.setSize(800,600);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        delaySlider.setValue(Controller.delay);
    }

    @Override
    public void printSymbol(int cell) {
        outputTextArea.append(String.valueOf(cell));
    }

    @Override
    public int readSymbol() {
        submitButton.setEnabled(true);
        inputTextField.setText("");
        while (!pressedInputButton) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        submitButton.setEnabled(false);
        pressedInputButton = false;
        return inputTextField.getText().charAt(0);
    }

    @Override
    public void print(int[] cells, int currentCell, int delay) {
        cellsTextArea.setText("");
        Highlighter highlighter = cellsTextArea.getHighlighter();
        highlighter.removeAllHighlights();
        HighlightPainter painter = new DefaultHighlightPainter(Color.green);

        for (int cell : cells) {
            cellsTextArea.append(String.valueOf(cell));
            if (cell < 10) {
                cellsTextArea.append("    ");
            } else if (cell < 100) {
                cellsTextArea.append("   ");
            } else {
                cellsTextArea.append("  ");
            }
        }

        try {
            highlighter.addHighlight(currentCell*5, currentCell*5 + 5, painter);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
