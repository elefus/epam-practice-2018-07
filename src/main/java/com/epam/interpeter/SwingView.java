package com.epam.interpeter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SwingView extends JFrame implements View {
    private JPanel mainPanel;
    private JTextField inputField;
    private JTextField outputField;
    private JButton openButton;
    private JButton runButton;
    private JButton enterButton;
    private JTextArea cellsArrayArea;
    private JButton pauseButton;
    private JSlider delaySlider;
    private TextArea fileArea;
    private JButton optionsButton;
    private JPanel fileAreaPanel;
    private JTextField fileField;

    private int delay;
    private boolean isEnter;
    public static boolean isTrace;
    public static int size = 45; // Only for view

    public SwingView(boolean isTrace, int size, int delay) {
        this.delay = delay > 0 ? delay : delaySlider.getMaximum() / 2;
        SwingView.isTrace = isTrace;
        SwingView.size = size > 0 ? size: SwingView.size;
        addAreas();
        this.setSize(500, 500);
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void addAreas() {
        fileArea = new TextArea();
        fileArea.setBounds(0, 100, 500, 209);
        fileArea.setEditable(false);
        fileArea.setBackground(Color.WHITE);
        fileAreaPanel.add(fileArea);
    }

    public void addActionListeners() {
        openButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(fileField.getText().equals("")) {
                    outputField.setText("File not found");
                } else {
                    outputField.setText("");
                    try {
                        fileArea.setText(Controller.getSourceCode(fileField.getText()));
                        runButton.setEnabled(true);
                    } catch (IOException e1) {
                        outputField.setText("File not found");
                    }
                }
            }
        });

        View view = this;
        optionsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new OptionsWindow().addMouseListeners();
            }
        });

       runButton.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               if(runButton.getText().equals("Run")) {
                   pauseButton.setVisible(true);
                   startProcess(view);
               } else {
                   Controller.setStop(true);
                   if (inputField.isEnabled())
                       inputField.setEnabled(false);
                   if (enterButton.isEnabled())
                       enterButton.setEnabled(false);
                   if (outputField.isEnabled())
                       outputField.setEnabled(false);
                   pauseButton.setVisible(false);
                   runButton.setText("Run");
               }
           }
       });

       enterButton.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               if (!pauseButton.getText().equals("Go"))
                   isEnter = !isEnter;
           }
       });

       delaySlider.addChangeListener(e -> delay = delaySlider.getValue());

       pauseButton.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               if(pauseButton.getText().equals("Pause")) {
                   Controller.setPause(true);
                   pauseButton.setText("Go");
               } else {
                   Controller.setPause(false);
                   pauseButton.setText("Pause");
               }
           }
       });
    }

    private void startProcess(View view) {
        outputField.setText("");
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    Controller.setStop(false);
                    Controller.setPause(false);
                    runButton.setText("Stop");
                    new Controller(fileArea.getText(), new Model(size), view, isTrace).process();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pauseButton.setVisible(false);
                runButton.setText("Run");
                return null;
            }
        };
        worker.execute();
    }

    @Override
    public char inputData() {
        inputField.setEnabled(true);
        inputField.setText("");
        enterButton.setEnabled(true);

        while (!isEnter) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        isEnter = !isEnter;
        enterButton.setEnabled(false);
        inputField.setEnabled(false);
        if (inputField.getText().equals(""))
            return 0;
        else
            return inputField.getText().charAt(0);
    }

    @Override
    public void outputData(char cellValue) {
        outputField.setEnabled(true);
        outputField.setText(outputField.getText() + Integer.toString((int)(cellValue)) + " ");
    }

    @Override
    public void traceCommand(char[] cellsArray, int cellIndex, char command) {
        cellsArrayArea.setText("");

        for (char cell : cellsArray) {
            cellsArrayArea.append(Integer.toString((int) cell));
            if (cell < 10)
                cellsArrayArea.append("    ");
            else if (cell < 100)
                cellsArrayArea.append("   ");
            else
                cellsArrayArea.append("  ");
        }

        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
