package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser;

/*
 * Code for Assignment 3, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * An exception that is thrown if a file can not be parsed using a certain
 * grammar. Has an optional error message parameter to give details about the
 * <code>ParseException</code>.
 * 
 * @author Henry J. Wylde
 */
public class ParseException extends Exception {
    
    private static final long serialVersionUID = 8216643967176707177L;
    
    /**
     * A default <code>ParseException</code> with an unhelpful error message.
     */
    public ParseException() {
        this("Incorrect grammar format.");
    }
    
    /**
     * A <code>ParseException</code> with the specified error message.
     * 
     * @param err the error message.
     */
    public ParseException(String err) {
        super("Parse exception: " + err);
    }
}
