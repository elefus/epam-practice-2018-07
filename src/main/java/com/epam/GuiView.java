package com.epam;

import java.awt.event.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class GuiView extends JFrame implements View {
    private JPanel panelMain;
    private JButton startButton;
    private boolean isEnter;
    private JButton btnEnter;
    private JTextArea fileArea;
    private JTextArea sizeArea;
    private JTextArea inputArea;
    private JTextArea output;
    private JRadioButton[] options;
    private JRadioButton SizeRadioButton;
    private JRadioButton TraceRadioButton;
    private JRadioButton FileRadioButton;

    public GuiView() {

        this.setContentPane(panelMain);
        this.setSize(700, 400);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void initialize(String fileName, int arrSize, boolean isTrace) {

        setControlElements();
        setOptionElements();
        setIOElements();
        setContentPane(panelMain);

        if (fileName != null) {
            FileRadioButton.setSelected(true);
            fileArea.setText(fileName);
            fileArea.setEditable(true);
        }

        if (arrSize != 30000) {
            SizeRadioButton.setSelected(true);
            sizeArea.setText(Integer.toString(arrSize));
            sizeArea.setEditable(true);
        }

        if (isTrace)
            TraceRadioButton.setSelected(true);

        setVisible(true);
    }

    private void startProcess() {
        GuiView view = this;

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws URISyntaxException {
                String fileName = FileRadioButton.isSelected() ? fileName = fileArea.getText() : null;
                int size = SizeRadioButton.isSelected() ? Integer.parseInt(sizeArea.getText()) : 30000;
                boolean isTrace = TraceRadioButton.isSelected();

                try {
                    startButton.setEnabled(false);
                    new Controller(fileName, new Model(size), view, isTrace).process();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startButton.setEnabled(true);
                return null;
            }
        };
        worker.execute();
    }

    @Override
    public char inputData() {
        inputArea.setEnabled(true);
        inputArea.setText("");
        btnEnter.setEnabled(true);
        output.append("\n[Enter value to input area]:");

        while (!isEnter) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        isEnter = !isEnter;
        btnEnter.setEnabled(false);
        inputArea.setEnabled(false);
        return inputArea.getText().charAt(0);
    }


    @Override
    public void outputData(int value) {
        output.append(Integer.toString((int) value));
    }

    @Override
    public void traceCommand(int cellIndex, char operation, int value) {
        output.append("\n|| CELL : '" + (cellIndex + 1) + "' || OPERATION : '" + operation + "' || VALUE : '" +
                (int) value + "' ||");
    }

    private void setControlElements() {



        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                inputArea.setText("");
                output.setText("");
                startProcess();
            }
        });

    }

    private void setOptionElements() {


        for (int i = 0; i < 3; i++) {


            switch (i) {
                case 0:

                    FileRadioButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (!fileArea.isEditable()) {
                                fileArea.setEditable(true);
                            } else {
                                fileArea.setText("");
                                fileArea.setEditable(false);
                            }
                        }
                    });
                    break;

                case 1:

                    SizeRadioButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (!sizeArea.isEditable()) {
                                sizeArea.setText("30000");
                                sizeArea.setEditable(true);
                            } else {
                                sizeArea.setText("");
                                sizeArea.setEditable(false);
                            }
                        }
                    });
                    break;

                case 2:
                    TraceRadioButton.setText("trace");
                    break;
            }

        }
    }

    private void setIOElements() {

        inputArea.setEditable(true);
        inputArea.setEnabled(false);

        btnEnter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isEnter = !isEnter;
            }
        });
        btnEnter.setEnabled(false);
        output.setEditable(false);

    }
}
