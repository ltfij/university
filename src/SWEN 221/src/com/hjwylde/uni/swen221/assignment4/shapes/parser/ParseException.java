package com.hjwylde.uni.swen221.assignment4.shapes.parser;

/*
 * Code for Assignment 4, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * An exception that is thrown if some input cannot be parsed using a certain grammar.
 * 
 * @author Henry J. Wylde
 */
public class ParseException extends IllegalArgumentException {
    
    private static final long serialVersionUID = 8216643967176707177L;
    
    /**
     * A default <code>ParseException</code> with an unhelpful error message.
     */
    public ParseException() {
        this("Incorrect grammar format.");
    }
    
    /**
     * A <code>ParseException</code> with the specified message.
     * 
     * @param m the message.
     */
    public ParseException(String m) {
        super("Parse exception: " + m);
    }
    
    /**
     * A <code>ParseException</code> with the specified message and cause.
     * 
     * @param m the message.
     * @param c the cause.
     */
    public ParseException(String m, Throwable c) {
        super("Parse exception: " + m, c);
    }
}