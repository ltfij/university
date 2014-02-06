package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types;

/*
 * Code for Assignment 3, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A type representing the different options for the non-terminal BOOLEAN.
 * 
 * @author Henry J. Wylde
 */
public enum BooleanType {
    
    TRUE("true"),
    FALSE("false"),
    CONDITION("condition"),
    NOT("!"),
    TOUCHING("touching");
    
    private final String name;
    
    private BooleanType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}