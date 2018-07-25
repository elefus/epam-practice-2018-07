
package com.epam;

import java.io.IOException;
import java.io.InputStreamReader;

class View {
    private InputStreamReader in = new InputStreamReader(System.in);
    void printData(char cell){
        System.out.println(cell + " ");
    }

    char readData() throws IOException {
        return (char) in.read();
    }

}