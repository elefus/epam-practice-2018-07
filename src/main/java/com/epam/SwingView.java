package com.epam;

import java.awt.Color;
import java.awt.Component;
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

public class SwingView extends JFrame implements View {

  private JPanel mainPanel;
  private JPanel memoryPane;
  private JButton runBtn;
  private JButton submitButton;
  private JTextArea codeTA;
  private JSlider delaySlider;
  private JTextField inputField;
  private JTextArea outputTA;
  private JPanel labelPanel;
  private JButton stepButton;
  private String output = "";
  private boolean step;
  private boolean run;

  private static final Object stepMonitor = new Object();
  private static final Object inputMonitor = new Object();


  public SwingView() {
    this.setContentPane(mainPanel);
    this.setSize(950, 760);
    this.setResizable(false);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    inputField.setVisible(false);
    submitButton.setVisible(false);
    codeTA.setFont(codeTA.getFont().deriveFont(20f));
    codeTA.setText("+++++ +++++             initialize counter (cell #0) to 10\n"
        + "[                       use loop to set 70/100/30/10\n"
        + "    > +++++ ++              add  7 to cell #1\n"
        + "    > +++++ +++++           add 10 to cell #2\n"
        + "    > +++                   add  3 to cell #3\n"
        + "    > +                     add  1 to cell #4\n"
        + "<<<< -                  decrement counter (cell #0)\n"
        + "]\n"
        + "> ++ .                  print 'H'\n"
        + "> + .                   print 'e'\n"
        + "+++++ ++ .              print 'l'\n"
        + ".                       print 'l'\n"
        + "+++ .                   print 'o'\n"
        + "> ++ .                  print ' '\n"
        + "<< +++++ +++++ +++++ .  print 'W'\n"
        + "> .                     print 'o'\n"
        + "+++ .                   print 'r'\n"
        + "----- - .               print 'l'\n"
        + "----- --- .             print 'd'\n"
        + "> + .                   print '!'\n"
        + "> .                     print '\\n'");
    this.setVisible(true);


  }

  public void initListeners(Control control) {
    runBtn.addActionListener(e -> {
      if (!run && !step) {            //Start
        run = true;
        runBtn.setText("Stop");
        stepButton.setText("Pause");
        start(control);
      } else if (step) {              //Continue
        runBtn.setText("Stop");
        run = true;
        stepButton.setText("Pause");
        step = false;
        synchronized (stepMonitor) {
          stepMonitor.notify();
        }
      } else {                        //Stop
        run = false;
        control.setStop(true);
      }
    });

    stepButton.addActionListener(e -> {
      if (!step && !run) {            //Start
        start(control);
        runBtn.setText("Continue");
        step = true;
      } else if (run) {               //Pause

        step = true;
        run = false;
        runBtn.setText("Continue");
        stepButton.setText("Step");

      } else {                        //Step
        synchronized (stepMonitor) {

          stepMonitor.notify();
        }
      }
    });

    submitButton.addActionListener(e -> {
      output = inputField.getText();
      synchronized (inputMonitor) {
        inputMonitor.notify();
      }
    });
  }


  @Override
  public char requestInput() {
    inputField.setVisible(true);
    submitButton.setVisible(true);
    runBtn.setEnabled(false);
    stepButton.setEnabled(false);

    inputField.requestFocus();
    output = "";
    synchronized (inputMonitor) {
      try {
        inputMonitor.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    inputField.setText("");

    inputField.setVisible(false);
    submitButton.setVisible(false);
    runBtn.setEnabled(true);
    stepButton.setEnabled(true);
    return output.charAt(0);
  }

  @Override
  public void printData(char dataAtCurrentMemCell) {
    outputTA.append(String.valueOf(dataAtCurrentMemCell));
  }

  @Override
  public void printMem(char[] memory, int p_position, int instrIdx) {

    for (int i = 0; i < 20; i++) {
      Component component = memoryPane.getComponent(i);
      if (component instanceof JTextField) {
        ((JTextField) component).setText(Integer.toString((int) memory[i]));
      }

    }
    for (Component c : labelPanel.getComponents()) {
      if (c instanceof JLabel) {
        ((JLabel) c).setText("");
      }
    }
    Component component = labelPanel.getComponent(p_position);
    ((JLabel) component).setText("^");

    Highlighter highlighter = codeTA.getHighlighter();
    highlighter.removeAllHighlights();
    HighlightPainter painter = new DefaultHighlightPainter(Color.BLACK);
    try {
      highlighter.addHighlight(instrIdx, instrIdx + 1, painter);
    } catch (BadLocationException e) {
      e.printStackTrace();
    }

    try {
      TimeUnit.MILLISECONDS.sleep(delaySlider.getValue());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (step) {
      synchronized (stepMonitor) {
        try {
          stepMonitor.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }


  private void start(Control control) {
    control.reset();
    outputTA.setText("");
    codeTA.setEditable(false);
    control.setCode(codeTA.getText());
    SwingWorker<Void, Void> stringVoidSwingWorker = new SwingWorker<Void, Void>() {

      @Override
      protected Void doInBackground() throws IOException {
        control.interpret();
        codeTA.setEditable(true);
        step = false;
        run = false;
        runBtn.setText("Run");
        stepButton.setText("Step");
        return null;
      }
    };
    stringVoidSwingWorker.execute();
  }
}
