package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types;

/*
 * Code for Assignment 3, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A type representing the different options for the non-terminal INSTRUCTION_METHOD.
 * 
 * @author Henry J. Wylde
 */
public enum InstructionMethodType {

    BOX("Box()"), THING("Thing()");

    private final String name;

    private InstructionMethodType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
