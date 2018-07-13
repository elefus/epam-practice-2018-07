package com.epam;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class Model {

    protected static int STACK_LENGTH;
    protected static char[] mem;
    protected static char[] program;
    protected static int mem_pointer;
    protected static int cmd_pointer;

    protected static BufferedReader fileReader;
    protected static BufferedReader input;
    protected static BufferedWriter output;

    protected static class Command {
        protected final static char SHIFT_RIGHT = '>';
        protected final static char SHIFT_LEFT = '<';
        protected final static char PLUS = '+';
        protected final static char MINUS = '-';
        protected final static char OUTPUT = '.';
        protected final static char INPUT = ',';
        protected final static char BRACKET_LEFT = '[';
        protected final static char BRACKET_RIGHT = ']';
    }
}
