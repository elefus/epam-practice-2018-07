package com.epam;

import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;

public class View {
    public static void start() {
        if (Controller.getGraphInterface()) {
        } else {
            Controller.interpret();
        }
    }
}
