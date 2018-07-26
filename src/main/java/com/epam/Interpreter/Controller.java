package com.epam.Interpreter;

import java.io.*;

public class Controller {
    private String fileName;
    private char[] program;
    private int cmdPointer;
    private BufferedReader fileReader;
    private BufferedReader inStream;
    private BufferedWriter outStream;
    private View view;
    private Model model;

    private static class Command {
        private final static char SHIFT_RIGHT = '>';
        private final static char SHIFT_LEFT = '<';
        private final static char PLUS = '+';
        private final static char MINUS = '-';
        private final static char OUTPUT = '.';
        private final static char INPUT = ',';
        private final static char BRACKET_LEFT = '[';
        private final static char BRACKET_RIGHT = ']';
    }

    public Controller(String fName) {
        fileName = fName;
        program = new char[0];
        cmdPointer = 0;
        fileReader = null;
        inStream = new BufferedReader((new InputStreamReader(System.in)));
        outStream = new BufferedWriter((new OutputStreamWriter(System.out)));
        view = new ConsoleView();
        model = new Model();
    }

    public void setFileName(String value) {
        fileName = value;
    }

    public void setInStream(InputStream in) {
        inStream = new BufferedReader(new InputStreamReader(in));
    }

    public void setOutStream(OutputStream out) {
        outStream = new BufferedWriter(new OutputStreamWriter(out));
    }

    public BufferedReader getInStream() {
        return inStream;
    }

    public BufferedWriter getOutStream() {
        return outStream;
    }

    public void setView(View value) {
        view = value;
    }

    public View getView() {
        return view;
    }

    public Model getModel() {
        return model;
    }

    public void prepare() {
        try (InputStream in = Controller.class.getResourceAsStream("./../../" + fileName)) {
            fileReader = new BufferedReader(new InputStreamReader(in));

            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = fileReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            cleanProgram(stringBuilder.toString().toCharArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        view.start(this);
    }

    public void interpret () {
        int bracketStack = 0;
        for (; cmdPointer < program.length; cmdPointer++) {
            switch (program[cmdPointer]) {

                case Command.SHIFT_RIGHT: {
                    if (model.getMemPointer() + 1 >= model.getMemLength()) {
                        throw new IndexOutOfBoundsException("Out of memory!");
                    } else {
                        model.incMemPointer();
                    }
                    break;
                }

                case Command.SHIFT_LEFT: {
                    if (model.getMemPointer() - 1 < 0) {
                        throw new IndexOutOfBoundsException("Out of memory!");
                    } else {
                        model.decMemPointer();
                    }
                    break;
                }

                case Command.PLUS: {
                    if (model.getMemCell() + 1 > 255) {
                        model.setMemCell(0);
                    } else {
                        model.setMemCell(model.getMemCell() + 1);
                    }
                    break;
                }

                case Command.MINUS: {
                    if (model.getMemCell() - 1 < 0) {
                        model.setMemCell(255);
                    } else {
                        model.setMemCell(model.getMemCell() - 1);
                    }
                    break;
                }

                case Command.OUTPUT: {
                    try {
                        outStream.write(model.getMemCell());
                        outStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                case Command.INPUT: {
                    try {
                        model.setMemCell(inStream.read());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                case Command.BRACKET_LEFT: {
                    bracketStack = cycleLeft(bracketStack);
                    break;
                }
                case Command.BRACKET_RIGHT:
                    bracketStack = cycleRight(bracketStack);
                    break;
                default:
            }
        }
    }

    private void cleanProgram(char[] chars) {
        char[] temp = new char[chars.length];
        int pos = 0;
        for (int i = 0; i < chars.length; ++i) {
            switch (chars[i]) {
                case Command.SHIFT_RIGHT: {
                    temp[pos++] = Command.SHIFT_RIGHT;
                    break;
                }
                case Command.SHIFT_LEFT: {
                    temp[pos++] = Command.SHIFT_LEFT;
                    break;
                }
                case Command.PLUS: {
                    temp[pos++] = Command.PLUS;
                    break;
                }
                case Command.MINUS: {
                    temp[pos++] = Command.MINUS;
                    break;
                }
                case Command.OUTPUT: {
                    temp[pos++] = Command.OUTPUT;
                    break;
                }
                case Command.INPUT: {
                    temp[pos++] = Command.INPUT;
                    break;
                }
                case Command.BRACKET_LEFT: {
                    temp[pos++] = Command.BRACKET_LEFT;
                    break;
                }
                case Command.BRACKET_RIGHT: {
                    temp[pos++] = Command.BRACKET_RIGHT;
                    break;
                }
                default:
            }
        }
        program = new char[pos];
        System.arraycopy(temp, 0, program, 0, pos);
    }

    private int cycleLeft(int bracketStack) {
        if (model.getMemCell() == 0) {
            cmdPointer++;
            while (bracketStack > 0 || program[cmdPointer] != Command.BRACKET_RIGHT) {
                if (program[cmdPointer] == Command.BRACKET_LEFT) {
                    ++bracketStack;
                } else if (program[cmdPointer] == Command.BRACKET_RIGHT) {
                    --bracketStack;
                }
                cmdPointer++;
            }
        }
        return bracketStack;
    }

    private int cycleRight(int bracketStack) {
        if (model.getMemCell() != 0) {
            cmdPointer--;
            while (bracketStack > 0 || program[cmdPointer] != Command.BRACKET_LEFT) {
                if (program[cmdPointer] == Command.BRACKET_RIGHT) {
                    ++bracketStack;
                } else if (program[cmdPointer] == Command.BRACKET_LEFT) {
                    --bracketStack;
                }
                cmdPointer--;
            }
            cmdPointer--;
        }
        return bracketStack;
    }
}