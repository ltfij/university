package com.hjwylde.uni.swen221.midterm.minesweeper;

/*
 * Code for Mid-term Test, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A syntax error is a simple kind of error message which is used to signal a problem occurred
 * during parsing.
 * 
 * @author David J. Pearce
 * 
 */
public class SyntaxError extends Exception {

    private static final long serialVersionUID = 1L;

    public SyntaxError(String msg) {
        super(msg);
    }
}
