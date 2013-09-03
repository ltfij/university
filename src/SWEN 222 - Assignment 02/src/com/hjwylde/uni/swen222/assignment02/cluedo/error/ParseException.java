package com.hjwylde.uni.swen222.assignment02.cluedo.error;

import java.io.IOException;

/**
 * Represents a parse exception when triyng to read some input.
 * 
 * @author Henry J. Wylde
 * 
 * @since 5/08/2013
 */
public class ParseException extends IOException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new <code>ParseException</code>.
     */
    public ParseException() {
        super();
    }
    
    /**
     * Creates a new <code>ParseException</code> with the given message.
     * 
     * @param message the message.
     */
    public ParseException(String message) {
        super(message);
    }
    
    /**
     * Creates a new <code>ParseException</code> with the given message and cause.
     * 
     * @param message the message.
     * @param cause the cause.
     */
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates a new <code>ParseException</code> with the given cause.
     * 
     * @param cause the cause.
     */
    public ParseException(Throwable cause) {
        super(cause);
    }
}