package com.epam.Interpreter;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class WindowView extends JFrame implements View {

    private int memSize = 10;
    private JLabel inputData;
    private JLabel outputData;
    private JButton startButton;
    private JTextField inputArea;
    private JTextArea outputArea;
    private JPanel panel;
    Controller controller;

    public void start(Controller controller) {
        this.controller = controller;
        this.setVisible(true);
        controller.setOutStream(System.out);
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                try {
                    controller.setInStream(new ByteArrayInputStream(inputArea.getText().getBytes("UTF-8")));
                    controller.setOutStream(new OutputStream() {
                        @Override
                        public void write(int b) throws IOException {
                            outputArea.append((Integer.toString(b)));
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                controller.interpret();
                controller.getView().printMem();
            }
        });
    }

    public WindowView() {
        super("Interpreter");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inputData = new JLabel("Input Data");
        outputData = new JLabel("Output Data");
        startButton = new JButton("Run");
        inputArea = new JTextField();
        outputArea = new JTextArea();
        panel = new JPanel();

        panel.setBounds(0,0,800,500);
        inputData.setBounds(5, 10, 100, 10);
        inputArea.setBounds(5, 40, 500, 20);
        outputData.setBounds(5, 70, 100, 10);
        outputArea.setBounds(5, 100, 500, 200);
        startButton.setBounds(5, 350, 100, 20);

        panel.setLayout(null);
        panel.add(inputData);
        panel.add(inputArea);
        panel.add(outputData);
        panel.add(outputArea);
        panel.add(startButton);
        setContentPane(panel);
    }

    public void printMem() {
        outputArea.append("\n");
        for (int i = 0; i < memSize; i++) {
            controller.getModel().setMemPointer(i);
            outputArea.append((byte)controller.getModel().getMemCell() + " ");
        }
    }
}