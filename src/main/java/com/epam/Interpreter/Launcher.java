package com.epam.Interpreter;


import java.util.Scanner;

public class Launcher {
    public static void main(String[] args) {
        Controller controller = new Controller("");
        Scanner sc = new Scanner(System.in);
        String temp = sc.nextLine();
        String[] arguments = temp.split(" ");
        OptionsParse optionsParse = new OptionsParse();
        optionsParse.start(arguments, controller);
        controller.prepare();
    }
}