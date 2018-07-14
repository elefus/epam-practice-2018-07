package com.epam;

public class View {
    public static void start() {
        if (Controller.getGraphInterface()) {
        } else {
            Controller.interpret();
        }
    }
}
