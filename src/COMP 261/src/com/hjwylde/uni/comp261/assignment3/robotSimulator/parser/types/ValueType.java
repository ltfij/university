package com.hjwylde.uni.comp261.assignment3.robotSimulator.parser.types;

/*
 * Code for Assignment 3, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A type representing the different options for the non-terminal VALUE.
 * 
 * @author Henry J. Wylde
 */
public enum ValueType {
    
    INTEGER("int"),
    VARIABLE("variable"),
    GET("get");
    
    private final String name;
    
    private ValueType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}