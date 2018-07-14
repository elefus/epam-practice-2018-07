package com.epam;

public class Model {
    protected static int memLength = 30000;
    protected static char[] mem = new char[memLength];
    protected static char[] program = new char[0];
    protected static int mem_pointer = 0;
    protected static int cmd_pointer = 0;

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
