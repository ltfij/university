package com.hjwylde.uni.comp261.assignment3.robotSimulator.parser.types;

/*
 * Code for Assignment 3, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A type representing the different options for the non-terminal VALUE_METHOD.
 * 
 * @author Henry J. Wylde
 */
public enum ValueMethodType {
    
    BOX_DISTANCE("BoxDistance()"),
    NUMBER_OF_THINGS_NOT_IN_BOXES("NumberOfThingsNotInBoxes()"),
    THING_DISTANCE("ThingDistance()");
    
    private final String name;
    
    private ValueMethodType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}