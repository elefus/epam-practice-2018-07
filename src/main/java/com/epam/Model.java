package com.epam;

class Model {

    int length;
    char[] date;
    private int MAX_NUMBER_OF_CELLS = 30000;

    Model(int linght){
        this.length = (linght != 0) && (length < 30000) ? linght : MAX_NUMBER_OF_CELLS;
        date = new char[this.length];
    }

    char getVal(int index) {
        return date[index];
    }

    void setVal(int index, char value){
        date[index] = value;
    }

}
