package com.epam;

import java.io.*;
import java.util.Arrays;

public class Controller extends Model {
    private static boolean graphInterface = false;
    private static boolean isTrace = false;
    private static String fileName = "";
    private static char[] program = new char[0];
    private static BufferedReader fileReader = null;
    private static BufferedReader input = null;
    private static BufferedWriter output = null;
    private static InputStreamReader inStream = new InputStreamReader(System.in);
    private static OutputStreamWriter outStream = new OutputStreamWriter(System.out);

    public static void setGraphInterface(boolean value) {
        graphInterface = value;
    }

    public static boolean getGraphInterface () {
        return graphInterface;
    }

    public static void setIsTrace(boolean value) {
        isTrace = value;
    }

    public static boolean getIsTrace () {
        return isTrace;
    }

    public static void setMemLength(int value) {
        if (value < 1) {
            throw new IllegalArgumentException("Memory length must be a natural number!");
        } else {
            memLength = value;
            mem = new char[memLength];
        }
    }

    public static int getMemLength () {
        return memLength;
    }

    public static void setFileName(String value) {
        if (!value.endsWith(".bf")) {
            throw new IllegalArgumentException("The file must have a extension of .bf!");
        } else {
            fileName = value;
        }
    }

    public static String getFileName(String value) {
        return fileName;
    }

    public static void setInStream(InputStreamReader value) {
        inStream = value;
    }

    public  static void setOutStream(OutputStreamWriter value) {
        outStream = value;
    }

    public static char[] readMemory() {
        return mem;
    }

    public static void interpret() {

        try (InputStream in = Controller.class.getResourceAsStream("./../../" + fileName);
             BufferedReader br = new BufferedReader (inStream);
             BufferedWriter bw = new BufferedWriter(outStream)) {
            fileReader = new BufferedReader(new InputStreamReader(in));
            input = br;
            output = bw;

            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = fileReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            programClean(stringBuilder.toString().toCharArray());
            CommandProccess();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void programClean(char[] chars) {
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

    private static void CommandProccess () throws IOException {
        if (isTrace) {
            System.out.println("PROGRAM");
            for (int i = 0; i < program.length; ++i) {
                System.out.print(program[i]);
            }
            System.out.println();
        }

        int bracketStack = 0;
        for(; cmd_pointer < program.length; ++cmd_pointer) {
            switch (program[cmd_pointer]) {
                case Command.SHIFT_RIGHT: {
                    if (mem_pointer + 1 >= memLength) {
                        throw new IndexOutOfBoundsException("Out of memory!");
                    }else
                    mem_pointer++;
                    break;
                }
                case Command.SHIFT_LEFT: {
                    if (mem_pointer - 1 < 0) {
                        throw new IndexOutOfBoundsException("Out of memory!");
                    }
                    else
                    mem_pointer--;
                    break;
                }
                case Command.PLUS: {
                    if (++mem[mem_pointer] > 255) {
                        mem[mem_pointer] = 0;
                    }
                    break;
                }
                case Command.MINUS: {
                    if (--mem[mem_pointer] < 0) {
                        mem[mem_pointer] = 255;
                    }
                    break;
                }
                case Command.OUTPUT: {
                    output.write(((int)mem[mem_pointer]) + " ");
                    output.flush();
                    break;
                }
                case Command.INPUT: {
                    mem[mem_pointer] = (char) input.read();
                    break;
                }
                case Command.BRACKET_LEFT: {
                    if (mem[mem_pointer] == 0) {
                        ++cmd_pointer;
                        while (bracketStack > 0 || program[cmd_pointer] != Command.BRACKET_RIGHT) {
                            if (program[cmd_pointer] == Command.BRACKET_LEFT) {
                                ++bracketStack;
                            } else if (program[cmd_pointer] == Command.BRACKET_RIGHT) {
                                --bracketStack;
                            }
                            ++cmd_pointer;
                        }
                    }
                    break;
                }
                case Command.BRACKET_RIGHT:
                    if (mem[mem_pointer] != 0) {
                        --cmd_pointer;
                        while (bracketStack > 0 || program[cmd_pointer] != Command.BRACKET_LEFT) {
                            if (program[cmd_pointer] == Command.BRACKET_RIGHT) {
                                ++bracketStack;
                            } else if (program[cmd_pointer] == Command.BRACKET_LEFT) {
                                --bracketStack;
                            }
                            --cmd_pointer;
                        }
                        --cmd_pointer;
                    }
                    break;
                default:
            }
            if (isTrace) {
                trace();
            }
        }
    }

    private static void trace() throws IOException {
        output.write("\nMemory pointer -> [" + mem_pointer + "] = " + (int)mem[mem_pointer]
                        + ", command pointer -> [" + cmd_pointer + "] = \'" + program[cmd_pointer] + "\'\n");
        output.flush();
    }
}