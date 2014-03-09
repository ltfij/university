package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types;

/*
 * Code for Assignment 3, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A type representing the different operations for the non-terminal BOOLEAN_CONDITION.
 * 
 * @author Henry J. Wylde
 */
public enum OperationType {

    SET("="), ADD("+="), SUB("-="), MUL("*="), DIV("/=");

    private final String name;

    private OperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
