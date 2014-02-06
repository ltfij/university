package com.hjwylde.uni.swen221.assignment06.com.bytebach.model;

/*
 * Code for Assignment 6, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * An invalid operation exception is used to signal the user attempted something which is not
 * permitted.
 * 
 * @author djp
 */
public class InvalidOperation extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public InvalidOperation() {
        this("Invalid operation.");
    }
    
    public InvalidOperation(String msg) {
        super(msg);
    }
}
