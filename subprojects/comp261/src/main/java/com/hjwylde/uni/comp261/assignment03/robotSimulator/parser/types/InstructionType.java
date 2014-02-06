package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types;

/*
 * Code for Assignment 3, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A type representing the different options for the non-terminal INSTRUCTION.
 * 
 * @author Henry J. Wylde
 */
public enum InstructionType {
    
    MOVE("move"),
    TURN("turn"),
    PICKUP("pickup()"),
    DROP("drop()"),
    WHILE("while"),
    IF("if"),
    DO("do"),
    VARIABLE("variable"),
    TURN_TOWARDS_FIRST("turnTowardsFirst");
    
    private final String name;
    
    private InstructionType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}