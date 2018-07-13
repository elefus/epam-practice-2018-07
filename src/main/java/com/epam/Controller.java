package com.epam;

import java.io.*;

public class Controller extends Model {

    public static void interpret(String fileName) {
        interpret(fileName, 30000, System.in, System.out);
    }

    public static void interpret(String fileName, int stackLength) {
        interpret(fileName, stackLength, System.in, System.out);
    }

    public static void interpret(String fileName, int stackLength, InputStream inputStream) {
        interpret(fileName, stackLength, inputStream, System.out);
    }

    public static void interpret(String fileName, int stackLength, OutputStream outputStream) {
        interpret(fileName, stackLength, System.in, outputStream);
    }

    public static void interpret(String fileName, InputStream inputStream) {
        interpret(fileName, 30000, inputStream, System.out);
    }

    public static void interpret(String fileName, OutputStream outputStream) {
        interpret(fileName, 30000, System.in, outputStream);
    }

    public static void interpret(String fileName, InputStream inputStream, OutputStream outputStream) {
        interpret(fileName, 30000, inputStream, outputStream);
    }

    public static void interpret(String fileName, int stackLength, InputStream inputStream, OutputStream outputStream) {
        STACK_LENGTH = stackLength;
        mem = new char[STACK_LENGTH];
        cmd_pointer = 0;
        mem_pointer = 0;

        try (InputStream in = Controller.class.getResourceAsStream("./../../" + fileName);
             BufferedReader br = new BufferedReader (new InputStreamReader(inputStream));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream))) {

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

    private static void CommandProccess () throws IOException {
        int l = 0;
        for(; cmd_pointer < program.length; cmd_pointer++) {
            switch (program[cmd_pointer]) {
                case Command.SHIFT_RIGHT:
                    mem_pointer = (mem_pointer == STACK_LENGTH - 1) ? 0 : mem_pointer + 1;
                    break;
                case Command.SHIFT_LEFT:
                    mem_pointer = (mem_pointer == 0) ? STACK_LENGTH - 1 : mem_pointer - 1;
                    break;
                case Command.PLUS:
                    mem[mem_pointer]++;
                    break;
                case Command.MINUS:
                    mem[mem_pointer]--;
                    break;
                case Command.OUTPUT:
                    if (mem[mem_pointer] == 0) {
                        output.write('0');
                    } else {
                        output.write(mem[mem_pointer]);
                    }
                    output.flush();
                    break;
                case Command.INPUT:
                    mem[mem_pointer] = (char)input.read();
                    break;
                case Command.BRACKET_LEFT:
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
                case Command.BRACKET_RIGHT:
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
}
