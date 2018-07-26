package com.epam.interpeter;

import com.epam.interpeter.Controller;
import com.epam.interpeter.Model;
import com.epam.interpeter.View;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.concurrent.TimeUnit;

public class SwingView extends JFrame implements View {
    private JPanel panel;
    private JButton btnRun;
    private boolean isEnter;
    private JButton btnEnter;
    private JTextArea fileArea;
    private JTextArea sizeArea;
    private JTextArea inputArea;
    private TextArea outputArea;
    private JRadioButton[] options;

    public SwingView() {
        panel = new JPanel();
        panel.setLayout(null);
        setSize(400, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void initialize(String fileName, int arrSize, boolean isTrace) {
        setControlElements();
        setOptionElements();
        setIOElements();
        setContentPane(panel);

        if(fileName != null) {
            options[0].setSelected(true);
            fileArea.setText(fileName);
            fileArea.setEditable(true);
        }

        if(arrSize != 30000) {
            options[1].setSelected(true);
            sizeArea.setText(Integer.toString(arrSize));
            sizeArea.setEditable(true);
        }

        if (isTrace)
            options[2].setSelected(true);

        setVisible(true);
    }

    private void startProcess() {
        View view = this;

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                String fileName = options[0].isSelected() ? fileName = fileArea.getText() : null;
                int size = options[1].isSelected() ? Integer.parseInt(sizeArea.getText()) : 30000;
                boolean isTrace = options[2].isSelected();

                try {
                    btnRun.setEnabled(false);
                    new Controller(fileName, new Model(size), view, isTrace).process();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                btnRun.setEnabled(true);
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
        outputArea.append("\n[Enter value to input area]:");

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
    public void outputData(char cellValue) {
        outputArea.append(Integer.toString((int) cellValue));
    }

    @Override
    public void traceCommand(int cellIndex, char operation, char cellValue) {
        outputArea.append("\n|| CELL : '" + (cellIndex + 1) + "' || OPERATION : '" + operation + "' || VALUE : '" +
                           (int) cellValue + "' ||");
    }

    private void setControlElements() {
        final int screenStartX = 200;
        final int screenStartY = 10;

        JButton btnHelp = new JButton("Help");
        btnHelp.setHorizontalAlignment(SwingConstants.CENTER);
        btnHelp.setBounds(screenStartX + 30, screenStartY + 60, 70, 20);
        panel.add(btnHelp);

        btnRun = new JButton("Run");
        btnRun.setHorizontalAlignment(SwingConstants.CENTER);
        btnRun.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                inputArea.setText("");
                outputArea.setText("");
                startProcess();
            }
        });
        btnRun.setBounds(screenStartX + 30, screenStartY + 90, 70, 20);
        panel.add(btnRun);
    }

    private void setOptionElements() {
        JLabel startLbl = new JLabel("Choose options:");
        startLbl.setHorizontalAlignment(SwingConstants.CENTER);
        startLbl.setBounds(30, 10 + 20, 120, 20);
        panel.add(startLbl);

        fileArea = new JTextArea();
        fileArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
        fileArea.setBounds(100, 10 + 52, 90, 20);
        fileArea.setEditable(false);
        panel.add(fileArea);

        sizeArea = new JTextArea();
        sizeArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
        sizeArea.setBounds(100, 10 + 77, 90, 20);
        sizeArea.setEditable(false);
        panel.add(sizeArea);

        options = new JRadioButton[3];
        for (int i = 0; i < 3; i++) {
            options[i] = new JRadioButton();
            options[i].setBounds(30, 10 + (i + 2) * 25, 70, 20);

            switch (i) {
                case 0 :
                    options[i].setText("file");
                    options[i].addMouseListener(new MouseAdapter() {
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

                case 1 :
                    options[i].setText("size");
                    options[i].addMouseListener(new MouseAdapter() {
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

                case 2 :
                    options[i].setText("trace");
                    break;
            }
            panel.add(options[i]);
        }
    }

    private void setIOElements() {
        JLabel inpLbl = new JLabel("INPUT:");
        inpLbl.setBounds(30, 150, 80, 20);
        panel.add(inpLbl);

        // Text INPUT Area
        inputArea = new JTextArea();
        inputArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
        inputArea.setBounds(30, 175, 40, 20);
        inputArea.setEditable(true);
        inputArea.setEnabled(false);
        panel.add(inputArea);

        btnEnter = new JButton("Enter");
        btnEnter.setBounds(120, 175, 80, 20);
        btnEnter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isEnter = !isEnter;
            }
        });
        btnEnter.setEnabled(false);
        panel.add(btnEnter);

        JLabel outLbl = new JLabel("OUTPUT:");
        outLbl.setBounds(30, 210, 80, 20);
        panel.add(outLbl);

        // Text OUTPUT Area
        outputArea = new TextArea();
        outputArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
        outputArea.setBounds(30, 235, 340, 210);
        outputArea.setEditable(false);
        panel.add(outputArea);
    }
}
