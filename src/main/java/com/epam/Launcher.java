package com.epam;

import java.util.Scanner;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("Please, enter the keys:");
        Scanner sc = new Scanner(System.in);
        String temp = sc.nextLine();
        String[] arguments = temp.split(" ");
        OptionsParse.optionsParse(arguments);
        sc.close();
    }
}