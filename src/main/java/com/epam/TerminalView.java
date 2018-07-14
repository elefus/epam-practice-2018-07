package com.epam;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class View {
    public int requestInput() throws IOException {
        return new InputStreamReader(System.in).read();

    }

    public void print(char dataAtCurrentMemCell) {
        System.out.println(dataAtCurrentMemCell);
    }
    public void printMem(char[] memory, int p_position, char c) {
        int[] mem = new int[memory.length];
        for(int i = 0;i<mem.length;i++){
            mem[i] = memory[i];
        }
        System.out.println(Arrays.toString(mem));
        System.out.println("Current pointer position: "+ p_position+ " ,current operation: " + c);
    }
}
