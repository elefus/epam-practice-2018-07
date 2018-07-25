package com.epam;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

class GuiView extends JFrame {
    private JPanel mainPanel;
    private JButton startButton;
    private JButton exitButton;


    private JTextArea textArea;
    private JComboBox chooseBox;
    private JTextField memorySize;
    private String way;

    GuiView() {
        super("INTERPRETER");
        super.setSize(700, 480);
        chooseBox.addItem("1.bf");
        chooseBox.addItem("2.bf");
        chooseBox.addItem("3.bf");
        chooseBox.addItem("4.bf");
        chooseBox.addItem("5.bf");
        chooseBox.addItem("6.bf");
        chooseBox.addItem("7.bf");
        chooseBox.addItem("8.bf");
        chooseBox.addItem("9.bf");
        setContentPane(mainPanel);
        setVisible(true);
        setLocationRelativeTo(null);
        textArea.setEditable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addListeners();

    }

    static char enterParameter() {
        String parameter = JOptionPane.showInputDialog(null, "Enter parameter");
        return parameter.charAt(0);
    }

    private void addListeners() {
        chooseBox.addActionListener(e -> {
            switch (chooseBox.getSelectedIndex()) {
                case 0:
                    way = (String) chooseBox.getSelectedItem();
                    break;
                case 1:
                    way = (String) chooseBox.getSelectedItem();

                    break;
                case 2:
                    way = (String) chooseBox.getSelectedItem();
                    break;
                case 3:
                    way = (String) chooseBox.getSelectedItem();
                    break;
                case 4:
                    way = (String) chooseBox.getSelectedItem();
                    break;
                case 5:
                    way = (String) chooseBox.getSelectedItem();
                    break;
                case 6:
                    way = (String) chooseBox.getSelectedItem();
                    break;
                case 7:
                    way = (String) chooseBox.getSelectedItem();
                    break;
                case 8:
                    way = (String) chooseBox.getSelectedItem();
                    break;
                case 9:
                    way = (String) chooseBox.getSelectedItem();
                    break;
                case 10:
                    way = (String) chooseBox.getSelectedItem();
                    break;
            }
        });


        startButton.addActionListener(e -> {
            if (e.getSource() == startButton) {
                int size;
                size = Integer.parseInt(memorySize.getText());
                Model model = new Model(size);
                View view = new View();
                Controller controller = new Controller(model, view);
                StringBuilder line = new StringBuilder();
                try {
                    boolean isGui = true;
                    controller.interprete(controller.getSource(way), isGui);
                    for (int i = 0; i < size; i++) {
                        line.append((int) model.getVal(i));
                        line.append(" ");
                    }

                    textArea.setText(line.toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });


        ;
    }
}
