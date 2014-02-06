package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types;

/*
 * Code for Assignment 3, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A type representing the different options for the non-terminal
 * BOOLEAN_METHOD.
 * 
 * @author Henry J. Wylde
 */
public enum BooleanMethodType {
    
    BOX("Box()"),
    ROBOT("Robot()"),
    THING("Thing()"),
    WALL("Wall()");
    
    private final String name;
    
    private BooleanMethodType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}