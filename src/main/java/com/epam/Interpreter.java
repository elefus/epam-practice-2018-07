package com.epam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import org.apache.commons.cli.*;


public class Interpreter extends Model {



    public void parsing(String filename) {


        try (InputStreamReader in = new InputStreamReader(Interpreter.class.getResourceAsStream("./../../" + filename+".bf"));
             BufferedReader buf = new BufferedReader(in)) {
            input = new BufferedReader(new InputStreamReader(System.in));
            String newLine;
            StringBuilder string = new StringBuilder();

            while ((newLine = buf.readLine()) != null)
                string.append(newLine);

            interprete(string.toString());
            String str1 = toString();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void interprete(String str) throws IOException {
        int cmd_ptr = 0;
        int count = 0;

        for (int i = 0; i < str.length(); i++) {



            if (str.charAt(i) == '>') {
                if (cmd_ptr == MAX_STACK_SIZE - 1)
                    cmd_ptr = 0;
                else
                    cmd_ptr++;
            }

            else if (str.charAt(i) == '<') {
                if (cmd_ptr == 0)


                    cmd_ptr = MAX_STACK_SIZE - 1;
                else
                    cmd_ptr--;
            }

            else if (str.charAt(i) == '+')
                memory[cmd_ptr]++;


            else if (str.charAt(i) == '-')
                memory[cmd_ptr]--;


            else if (str.charAt(i) == '.')
                System.out.print((char) (memory[cmd_ptr]));


            else if (str.charAt(i) == ',')
                memory[cmd_ptr] = (char)input.read();


            else if (str.charAt(i) == '[') {
                if (memory[cmd_ptr] == 0) {
                    i++;
                    while (count > 0 || str.charAt(i) != ']') {
                        if (str.charAt(i) == '[')
                            count++;
                        else if (str.charAt(i) == ']')
                            count--;
                        i++;
                    }
                }
            }

            else if (str.charAt(i) == ']') {
                if (memory[cmd_ptr] != 0) {
                    i--;
                    while (count > 0 || str.charAt(i) != '[') {
                        if (str.charAt(i) == ']')
                            count++;
                        else if (str.charAt(i) == '[')
                            count--;
                        i--;
                    }
                    i--;
                }
            }

        }
    }
    public static void main(String[] args) {
        Interpreter controller = new Interpreter();
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        options.addOption("f", "file", true, "Name of file");
        options.addOption("h",false,"Shows help");
        try {
            CommandLine commandLine = parser.parse(options,args);
           // System.out.println(commandLine.getOptionValue("f"));
            controller.parsing(commandLine.getOptionValue("f"));
            System.out.println("");
            if (commandLine.hasOption("h")){
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Commandline parameters",options);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}