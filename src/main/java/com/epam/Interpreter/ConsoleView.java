package com.epam.Interpreter;

public class ConsoleView implements View {

    private int memSize = 10;
    Controller controller;

    public void start(Controller controller) {
        this.controller = controller;
        controller.setInStream(System.in);
        controller.setOutStream(System.out);
    }

    public void printMem() {
        for (int i = 0; i < memSize; i++) {
            controller.getModel().setMemPointer(i);
            System.out.print(controller.getModel().getMemCell() + " ");
        }
    }
}