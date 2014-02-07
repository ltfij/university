package com.hjwylde.uni.swen222.lab02.org.simplelisp.error;

/**
 * Represents an exception that occurs during parsing.
 * 
 * @author Henry J. Wylde
 * 
 * @since 5/08/2013
 */
public final class ParseException extends Exception {

    private final int line, col;

    public ParseException(String message, int line, int col) {
        super(message);

        this.line = line;
        this.col = col;
    }

    public int getColumn() {
        return col;
    }

    public int getLine() {
        return line;
    }
}
