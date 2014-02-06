package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.nodes;

import com.hjwylde.uni.comp261.assignment03.robotSimulator.World;
import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types.ValueMethodType;

/*
 * Code for Assignment 3, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A non-terminal node representing a VALUE_METHOD.
 * 
 * @author Henry J. Wylde
 */
public class ValueMethodNode implements Node {
    
    private final ValueMethodType type;
    
    /**
     * Constructs a <code>ValueMethodNode</code> of the specified type.
     * 
     * @param type the ValueMethodType.
     */
    public ValueMethodNode(ValueMethodType type) {
        this.type = type;
    }
    
    /**
     * Evaluates this <code>ValueMethodNode</code> and returns its integer value.
     * 
     * @param robotID the robot ID to evaluate for.
     * @param world the world to evaluate for.
     * @return this ValueMethodNode's value.
     */
    public int evaluate(int robotID, World world) {
        switch (type) {
        case BOX_DISTANCE:
            return world.distanceToFirstBox(robotID);
        case THING_DISTANCE:
            return world.distanceToFirstThing(robotID);
        default: // NUMBER_OF_THINGS_NOT_IN_BOXES
            return world.numberOfThingsNotInBoxes();
        }
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return type.getName();
    }
}
