package com.epam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Common {

    public static String readAllLines(String fileName) throws IOException {

        try (InputStream resourceStream = InterpreterTest.class.getResourceAsStream(fileName);
             InputStreamReader reader = new InputStreamReader(resourceStream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            StringBuilder builder = new StringBuilder();
            while (bufferedReader.ready()) {
                builder.append(bufferedReader.readLine());
                builder.append(System.lineSeparator());
            }

            return builder.toString();
        }
    }
}
