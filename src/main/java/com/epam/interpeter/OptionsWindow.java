package com.epam.interpeter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OptionsWindow extends JFrame {
    private JPanel mainPanel;
    private JRadioButton traceButton;
    private JTextField sizeField;
    private JButton applyButton;
    private JButton cancelButton;

    public OptionsWindow() {
        sizeField.setForeground(Color.LIGHT_GRAY);
        sizeField.setText(Integer.toString(SwingView.size));
        if(SwingView.isTrace) {
            traceButton.setText("Enabled");
            traceButton.setSelected(true);
        } else {
            traceButton.setText("Disabled");
        }

        this.setSize(200, 200);
        this.setContentPane(mainPanel);
        this.setVisible(true);
    }

    public void addMouseListeners() {
        JFrame mainFrame = this;
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                destroyWindow(mainFrame);
            }
        });

        traceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (traceButton.isSelected())
                    traceButton.setText("Enabled");
                else
                    traceButton.setText("Disabled");
            }
        });

        applyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (!sizeField.getText().equals("") && Integer.parseInt(sizeField.getText()) > 0)
                        SwingView.size = Integer.parseInt(sizeField.getText());
                    SwingView.isTrace = traceButton.getText().equals("Enabled");
                    destroyWindow(mainFrame);
                } catch (NumberFormatException e1) {
                    sizeField.setText("Invalid size");
                }
            }
        });

        sizeField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(sizeField.getText().equals("Invalid size") || (sizeField.getText().equals("30000") &&
                   sizeField.getForeground().equals(Color.LIGHT_GRAY))) {
                    sizeField.setText("");
                }
                sizeField.setForeground(Color.BLACK);
            }
        });
    }

    private void destroyWindow(JFrame mainFrame) {
        mainFrame.setVisible(false);
        mainFrame.dispose();
    }
}
