package com.epam;

import java.io.IOException;
import org.apache.commons.cli.ParseException;

public class Launcher {
    public static void main(String...args) {
        try {
            new OptionParser().parse(args);
        } catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
