package com.epam;

import java.io.IOException;
import java.io.InputStreamReader;

class View {

    void writeCell(char cell){
        System.out.println(cell + " ");
    }

    char readCell() throws IOException {
        return (char) new InputStreamReader(System.in).read();
    }

}
