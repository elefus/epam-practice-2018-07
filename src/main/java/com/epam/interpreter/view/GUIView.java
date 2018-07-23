package com.epam.interpreter.view;

import com.epam.interpreter.Controller;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GUIView implements View {
  private final Executor executor = Executors.newSingleThreadExecutor();
  private JTextArea output;
  private JTextField inputField;
  private JTextArea inputCode;
  private JTextField memoryInputField;
  private JButton interpret;
  private String[] files;
  private int inputIndex = 0;
  private char[] input;
  private int cellSize = 8;

  public GUIView() {
    JFrame frame = new JFrame("Interpreter");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    LayoutManager manager = new BorderLayout();
    JPanel panel = new JPanel(manager);
    panel.setPreferredSize(new Dimension(600, 500));

    JPanel topPanel = getTopPanel();

    JPanel bottomPanel = getBottomPanel();

    JPanel centerPanel = getCenterPanel();

    panel.add(topPanel, BorderLayout.NORTH);
    panel.add(centerPanel, BorderLayout.CENTER);
    panel.add(bottomPanel, BorderLayout.SOUTH);

    frame.add(panel);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  private JPanel getCenterPanel() {
    inputCode = new JTextArea();
    JScrollPane inputCodeSp = new JScrollPane(inputCode);

    output = new JTextArea();
    output.setEditable(false);
    JScrollPane outputSp = new JScrollPane(output);

    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    centerPanel.add(inputCodeSp);
    centerPanel.add(outputSp);
    return centerPanel;
  }

  private JPanel getBottomPanel() {
    inputField = new JTextField(30);

    interpret = new JButton("Запуск");
    interpret.addActionListener(e -> runInterpreter());

    JPanel bottomPanel = new JPanel();
    bottomPanel.add(inputField);
    bottomPanel.add(interpret);
    return bottomPanel;
  }

  private JPanel getTopPanel() {
    JButton chooseFile = new JButton("Выберете файл");
    chooseFile.addActionListener(
        e -> {
          JFileChooser chooser = new JFileChooser();
          chooser.setMultiSelectionEnabled(true);
          int ret = chooser.showDialog(null, "Открыть файл");
          if (ret == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles();
            files = new String[selectedFiles.length];
            for (int i = 0; i < selectedFiles.length; i++) {
              files[i] = selectedFiles[i].getAbsolutePath();
            }
          }
        });

    ButtonGroup cellSizeGroup = new ButtonGroup();
    JRadioButton button8bits = new JRadioButton("8 bits", false);
    button8bits.addActionListener(e -> cellSize = 8);
    JRadioButton button16bits = new JRadioButton("16 bits", true);
    button16bits.addActionListener(e -> cellSize = 16);
    cellSizeGroup.add(button8bits);
    cellSizeGroup.add(button16bits);

    JLabel cellSizeLabel = new JLabel("Cell size: ");
    JLabel memorySizeLabel = new JLabel("Memory size: ");

    memoryInputField = new JTextField(15);

    JPanel topPanel = new JPanel();
    topPanel.add(chooseFile);
    topPanel.add(cellSizeLabel);
    topPanel.add(button8bits);
    topPanel.add(button16bits);
    topPanel.add(memorySizeLabel);
    topPanel.add(memoryInputField);
    return topPanel;
  }

  private void runInterpreter() {
    interpret.setEnabled(false);
    input = inputField.getText().toCharArray();
    inputIndex = 0;
    executor.execute(
        () -> {
          Controller.GUIRun(this);
          interpret.setEnabled(true);
          files = null;
        });
  }

  @Override
  public char readChar() {
    if (inputIndex < input.length) {
      return input[inputIndex++];
    } else {
      return Character.MIN_VALUE;
    }
  }

  @Override
  public void printChar(char c) {
    output.append(Character.toString(c));
  }

  @Override
  public int getCellSize() {
    return cellSize;
  }

  @Override
  public int getMemoryCapacity() {
    int memoryCapacity;
    try {
      memoryCapacity = Integer.parseInt(memoryInputField.getText());
      if (memoryCapacity <= 0) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      memoryCapacity = 30000;
    }
    return memoryCapacity;
  }

  @Override
  public String[] getFiles() {
    return files;
  }

  public String getCode() {
    return inputCode.getText();
  }
}
