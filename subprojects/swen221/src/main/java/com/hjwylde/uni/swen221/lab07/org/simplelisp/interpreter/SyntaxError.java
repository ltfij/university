package com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter;

/*
 * Code for Laboratory 7, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class SyntaxError extends Error {

    private static final long serialVersionUID = 1L;

    private int _line;
    private int _column;

    public SyntaxError(int line, int col, String s) {
        super(s);
        _line = line;
        _column = col;
    }

    public int getColumn() {
        return _column;
    }

    public int getLine() {
        return _line;
    }
}
