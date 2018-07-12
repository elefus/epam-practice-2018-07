package com.epam;

import java.io.*;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Example1 {

    public static void main(String[] args) {
        // IO <- blocking streams
        // NIO <- non-blocking streams


        // byte-streams:
        // I - inputStream
        // O - outputStream
        //
        // char-steams:
        // I - reader
        // O - writer

        String line1 = null;
        String line2 = null;
        InputStream testInputStream = null;
        try {
            testInputStream = Example1.class.getResourceAsStream("./../../test.txt");
            InputStreamReader reader = new InputStreamReader(testInputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            line1 = bufferedReader.readLine();
            line2 = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (testInputStream != null) {
                    testInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try (InputStream input = Example1.class.getResourceAsStream("./../../test.txt");
             FileWriter writer = new FileWriter("out.txt")) {
            writer.write(line2);
            writer.write(System.lineSeparator());
            writer.write(line1);
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
