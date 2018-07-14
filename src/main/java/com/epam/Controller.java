package com.epam;

import java.io.*;

public class Controller extends Model {
    private static boolean graphInterface = false;
    private static boolean isTrace = false;
    private static String fileName;
    private static BufferedReader fileReader = null;
    private static BufferedReader input = null;
    private static BufferedWriter output = null;
    private static InputStream inStream = System.in;
    private static OutputStream outStream = System.out;

    public static void interpret() {
        interpret(memLength, System.in, System.out);
    }

    public static void interpret(int memoryLength) {
        interpret(memoryLength, inStream, outStream);
    }

    public static void interpret(int memoryLength, InputStream inputStream) {
        interpret(memoryLength, inputStream, outStream);
    }

    public static void interpret(int memoryLength, OutputStream outputStream) {
        interpret(memoryLength, inStream, outputStream);
    }

    public static void interpret(InputStream inputStream) {
        interpret(memLength, inputStream, outStream);
    }

    public static void interpret(OutputStream outputStream) {
        interpret(memLength, inStream, outputStream);
    }

    public static void interpret(InputStream inputStream, OutputStream outputStream) {
        interpret(memLength, inputStream, outputStream);
    }

    public static void interpret(int memoryLength, InputStream inputStream, OutputStream outputStream) {
        Model.memLength = memoryLength;
        mem = new char[Model.memLength];
        cmd_pointer = 0;
        mem_pointer = 0;
        inStream = inputStream;
        outStream = outputStream;

        try (InputStream in = Controller.class.getResourceAsStream("./../../" + fileName);
             BufferedReader br = new BufferedReader (new InputStreamReader(inStream));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outStream))) {
            fileReader = new BufferedReader(new InputStreamReader(in));
            input = br;
            output = bw;

            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = fileReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            program = stringBuilder.toString().toCharArray();
            CommandProccess();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        memLength = value;
    }

    public static int getMemLength () {
        return memLength;
    }

    public static void setFileName(String value) {
        fileName = value;
    }

    public static String getFileName(String value) {
        return fileName;
    }

    private static void CommandProccess () throws IOException {
        int l = 0;
        for(; cmd_pointer < program.length; cmd_pointer++) {
            switch (program[cmd_pointer]) {
                case Command.SHIFT_RIGHT: {
                    if (isTrace) {
                        trace(Command.SHIFT_RIGHT);
                    }
                    mem_pointer++;
                    break;
                }
                case Command.SHIFT_LEFT: {
                    if (isTrace) {
                        trace(Command.SHIFT_LEFT);
                    }
                    mem_pointer--;
                    break;
                }
                case Command.PLUS: {
                    if (isTrace) {
                        trace(Command.PLUS);
                    }
                    mem[mem_pointer]++;
                    break;
                }
                case Command.MINUS: {
                    if (isTrace) {
                        trace(Command.MINUS);
                    }
                    mem[mem_pointer]--;
                    break;
                }
                case Command.OUTPUT: {
                    if (isTrace) {
                        trace(Command.OUTPUT);
                    }
                    if (mem[mem_pointer] == 0) {
                        output.write('0');
                    } else {
                        output.write(mem[mem_pointer]);
                    }
                    output.flush();
                    break;
                }
                case Command.INPUT: {
                    mem[mem_pointer] = (char) input.read();
                    if (isTrace) {
                        trace(Command.INPUT);
                    }
                    break;
                }
                case Command.BRACKET_LEFT: {
                    if (isTrace) {
                        trace(Command.BRACKET_LEFT);
                    }
                    if (mem[mem_pointer] == 0) {
                        cmd_pointer++;
                        while (l > 0 || program[cmd_pointer] != Command.BRACKET_RIGHT) {
                            if (program[cmd_pointer] == Command.BRACKET_LEFT) {
                                l++;
                            } else if (program[cmd_pointer] == Command.BRACKET_RIGHT) {
                                l--;
                            }
                            cmd_pointer++;
                        }
                    }
                    break;
                }
                case Command.BRACKET_RIGHT:
                    if (isTrace) {
                        trace(Command.BRACKET_RIGHT);
                    }
                    if (mem[mem_pointer] != 0) {
                        cmd_pointer--;
                        while (l > 0 || program[cmd_pointer] != Command.BRACKET_LEFT) {
                            if (program[cmd_pointer] == Command.BRACKET_RIGHT) {
                                l++;
                            } else if (program[cmd_pointer] == Command.BRACKET_LEFT) {
                                l--;
                            }
                            cmd_pointer--;
                        }
                        cmd_pointer--;
                    }
                    break;
                default:
            }
        }
    }

    private static void trace(char c) throws IOException {
        output.write("\nMemory pointer -> [" + mem_pointer + "] = " + (int)mem[mem_pointer]
                        + ", command pointer -> [" + cmd_pointer + "] = \'" + c + "\'\n");
        output.flush();
    }
}