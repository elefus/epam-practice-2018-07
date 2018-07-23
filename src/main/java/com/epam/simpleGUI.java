package com.epam;

import javax.swing.*;
import java.io.IOException;

class simpleGUI extends JFrame {
    private JPanel mainPanel;
    private JButton startButton;
    private JButton exitButton;
    private JButton browseButton;
    private JTextField textField1;
    private JTextArea textArea;
    private JCheckBox checkBox;
    private String way;
    private final JFileChooser fc = new JFileChooser();


    private void addListeners() {
        browseButton.addActionListener(e -> {
            if (e.getSource() == browseButton)
                if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fc.getSelectedFile();
                    way = file.getAbsolutePath();
                    textField1.setText(way);
                }
        });

        startButton.addActionListener(e -> {
            if ((textField1 != null) && (e.getSource() == startButton)){
                Model model = new Model(30);
                View view = new View();
                Controller controller = new Controller(model, view);
                StringBuilder line = new StringBuilder();
                try {
                    controller.interpreter(controller.getFileCode(way),true);
                    if (checkBox.isSelected()){
                        for (int i = 0; i < 20; i++){
                            line.append((int)model.getVal(i));
                            line.append(" ");
                        }
                    }else
                        for (int i = 0; i < 20; i++){
                            line.append(model.getVal(i));
                            line.append(" ");
                        }
                    textArea.setText(line.toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        exitButton.addActionListener(e -> {
            if( e.getSource() == exitButton){
            setVisible(false);
            dispose();
            }
        });
    }


    private simpleGUI() {
        super.setSize(640,480);
        setContentPane(mainPanel);
        setVisible(true);
        textArea.setEditable(false);
        textField1.setEditable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addListeners();
    }


    public static void main(String[] args){
        new simpleGUI();
    }
}

