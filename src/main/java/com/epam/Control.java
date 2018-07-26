package com.epam;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Control {

  private int instructionIdx = 0;
  private final Memory memory;
  private final View view;
  private String code;
  private final boolean TRACING;
  private boolean stop;

  public Control(boolean trace, String filename, Memory memory, View view)
      throws FileNotFoundException {
    this.TRACING = trace;
    this.memory = memory;
    this.view = view;
    code = getCodeFromFile(filename);
  }

  private Control(Memory mem, View view) {
    this.TRACING = true;
    this.memory = mem;
    this.view = view;
  }

  public static void main(String[] args) {
    try {
      launchWithArgs(args);
    } catch (IOException | NullPointerException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  public void reset() {
    instructionIdx = 0;
    memory.reset();
    stop = false;
  }

  public void interpret() throws IOException {
    while (instructionIdx != code.length()) {
      if (TRACING) {
        String commands = "<>+-.,[]";
        if (commands.contains(Character.toString(code.charAt(instructionIdx)))) {
          view.printMem(memory.getMem(), memory.getMemIdx(), instructionIdx);
        }
      }
      switch (code.charAt(instructionIdx)) {
        case '<':
          memory.decMemIdx();
          break;
        case '>':
          memory.incMemIdx();
          break;
        case '+':
          memory.incDataAtCurrentCell();
          break;
        case '-':
          memory.decDataAtCurrentCell();
          break;
        case ',':
          memory.setDataAtCurrentCell(view.requestInput());
          break;
        case '.':
          view.printData(memory.getDataAtCurrentCell());
          break;
        case '[':
          int idx = getPair(code, instructionIdx);
          if (memory.getDataAtCurrentCell() == 0) {
            instructionIdx = idx;
          }
          break;
        case ']':
          idx = getPair(code, instructionIdx);
          if (memory.getDataAtCurrentCell() != 0) {
            instructionIdx = idx;
          }
          break;
        case '/':
          if (code.charAt(instructionIdx + 1) == '/') {
            while (!System.lineSeparator()
                .contains(Character.toString(code.charAt(instructionIdx)))) {
              instructionIdx++;
            }
          }
          break;
      }
      if (stop) {
        break;
      }
      instructionIdx++;
    }
    view.printMem(memory.getMem(), memory.getMemIdx(), instructionIdx);
  }

  public void setCode(String text) {
    code = text;
  }

  public String getCode() {
    return code;
  }

  public static String getCodeFromFile(String filename) throws FileNotFoundException {
    String code = null;
    Path path = Paths.get(new File(filename).getPath());
    if (Control.class.getResource("./../../" + filename) != null) {
      try {
        path = Paths.get(Control.class.getResource("./../../" + filename).toURI());
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }
    if (path == null) {
      throw new FileNotFoundException();
    }
    try {
      code = Files.lines(path)
          .collect(joining(System.lineSeparator())) + System.lineSeparator();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return code;
  }

  public void setStop(boolean stop) {
    this.stop = stop;
  }

  public static int getPair(String code, int currentIdx) throws IllegalStateException {
    int tIdx = currentIdx;
    boolean backwards = code.charAt(tIdx) == ']';
    int brackets = 1;
    while ((code.charAt(tIdx) != ']' && !backwards) || (code.charAt(tIdx) != '[' && backwards) ||
        brackets != 0) {
      tIdx = backwards ? tIdx - 1 : tIdx + 1;
      if (tIdx == code.length() || tIdx == -1) {
        throw new IllegalStateException();
      }
      if (code.charAt(tIdx) == '[') {
        brackets = backwards ? brackets - 1 : brackets + 1;
      } else if (code.charAt(tIdx) == ']') {
        brackets = backwards ? brackets + 1 : brackets - 1;
      }
    }
    return tIdx;
  }

  private static void launchWithArgs(String[] args) throws IOException {

    CommandLineParser parser = new DefaultParser();
    Options options = new Options();

    options.addOption("f", "file", true, "filename to interpret");
    options.addOption("s", "size", true, "set memory size");
    options.addOption("h", "help", false, "view help");
    options.addOption("g", "gui", false, "launchWithArgs GUI");
    options.addOption("t", "trace", false, "enable tracing");
    try {
      CommandLine line = parser.parse(options, args);
      if (line.hasOption("h")) {
        HelpFormatter formatter = new HelpFormatter();
        String header = "Interprets brainfuck code";
        String footer = "*Not a complete list*";
        String cmdLineSyntax = "control -f <FILE> [-s <MemorySize>] [-h] [-t] [-g] ";
        formatter.printHelp(cmdLineSyntax, header, options, footer);
        return;
      }
      if (line.hasOption("g")) {
        View view = new SwingView();
        Control control = new Control(new Memory(20), view);
        ((SwingView) view).initListeners(control);
        return;
      }
      if (!line.hasOption("f")) {
        throw new MissingOptionException("Missing required option -f ");
      }
      boolean trace = line.hasOption("t");
      int size = line.hasOption("s") ? Integer.parseInt(line.getOptionValue("s")) : 30000;
      String filename = line.getOptionValue("f");

      new Control(trace, filename, new Memory(size), new TerminalView()).interpret();
    } catch (ParseException e) {
      System.err.println(e.getMessage());
    } catch (NumberFormatException e) {
      System.err.println("-s must have an int arg");
    }
  }
}
