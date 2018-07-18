package com.epam;

import java.awt.Color;
import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

public class SwingView extends JFrame implements InterpreterView {

  private JPanel mainPanel;
  private JPanel memoryPane;
  private JButton runBtn;
  private JButton submitButton;
  private JTextField fileInputField;
  private JTextArea code;
  private JSlider delaySlider;
  private JTextField inputField;
  private JLabel inputLbl;
  private JTextArea outputTA;
  private JPanel labelPanel;
  private JButton stepButton;
  private JButton exportButton;
  private String output = "";
  private boolean trace;

  private static final Object monitor = new Object();


  public SwingView() {
    this.setContentPane(mainPanel);
    this.setSize(910, 700);
    this.setResizable(false);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    inputLbl.setVisible(false);
    inputField.setVisible(false);
    submitButton.setVisible(false);
    code.setFont(code.getFont().deriveFont(20f));
    this.setVisible(true);

  }

  public void initListeners(Control control) {
    runBtn.addActionListener(e -> {
      if(!trace) {
        start(control);
      } else {
        runBtn.setText("Run");
        trace = false;
        synchronized (monitor){
          monitor.notify();
        }
      }
    });
    submitButton.addActionListener(e -> {
      output = inputField.getText();
      synchronized (monitor) {
        monitor.notify();
      }
    });
    exportButton.addActionListener(e -> {
      if (!fileInputField.getText().isEmpty()) {
        try {
          code.setText(control.getCodeFromFile(fileInputField.getText()));
        } catch (FileNotFoundException e1) {
          e1.printStackTrace();
        }
      }
    });
    stepButton.addActionListener(e -> {
      if (!trace){
        start(control);
        runBtn.setText("Continue");
      }
      trace = true;
      synchronized (monitor){
        monitor.notify();
      }
    });
  }


  @Override
  public char requestInput() {
    inputLbl.setVisible(true);
    inputField.setVisible(true);
    submitButton.setVisible(true);
    output = "";
    synchronized (monitor) {
      try {
        monitor.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    inputLbl.setVisible(false);
    inputField.setVisible(false);
    submitButton.setVisible(false);
    mainPanel.updateUI();
    return output.charAt(0);
  }

  @Override
  public void printData(char dataAtCurrentMemCell) {
    outputTA.append(dataAtCurrentMemCell + System.lineSeparator());
    mainPanel.updateUI();
  }

  @Override
  public void printMem(char[] memory, int p_position, int instrIdx) {

    for (int i = 0; i < 20; i++) {
      Component component = memoryPane.getComponent(i);
      if (component instanceof JTextField) {
        ((JTextField) component).setText(Integer.toString((int) memory[i]));
      }

    }
    Component component = labelPanel.getComponent(p_position);
    ((JLabel) component).setText("^");
    if (p_position != 0) {
      ((JLabel) labelPanel.getComponent(p_position - 1)).setText("");
    }
    if (p_position != 20) {
      ((JLabel) labelPanel.getComponent(p_position + 1)).setText("");
    }

    Highlighter highlighter = code.getHighlighter();
    highlighter.removeAllHighlights();
    HighlightPainter painter = new DefaultHighlightPainter(Color.ORANGE);
    try {
      highlighter.addHighlight(instrIdx, instrIdx + 1, painter);
    } catch (BadLocationException e) {
      e.printStackTrace();
    }

    mainPanel.updateUI();
    try {
      TimeUnit.MILLISECONDS.sleep(delaySlider.getValue());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if(trace){
      synchronized (monitor){
        try {
          monitor.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
  private void start(Control control){
    outputTA.setText("");
    code.setEditable(false);
    control.setCode(code.getText());
    SwingWorker<String, Void> stringVoidSwingWorker = new SwingWorker<String, Void>() {

      @Override
      protected String doInBackground() throws IOException {
        control.interpret();
        code.setEditable(true);
        trace = false;
        runBtn.setText("Run");
        stepButton.setText("Step");
        return null;
      }
    };
    stringVoidSwingWorker.execute();
    mainPanel.updateUI();
    control.reset();
  }
}
