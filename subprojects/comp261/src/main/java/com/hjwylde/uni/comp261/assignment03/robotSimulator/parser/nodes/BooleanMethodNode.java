package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.nodes;

import com.hjwylde.uni.comp261.assignment03.robotSimulator.World;
import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types.BooleanMethodType;

/*
 * Code for Assignment 3, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A non-terminal node representing a BOOLEAN_METHOD.
 * 
 * @author Henry J. Wylde
 */
public class BooleanMethodNode implements Node {

    private final BooleanMethodType type;

    /**
     * Constructs a <code>BooleanMethodNode</code> of the specified type.
     * 
     * @param type the BooleanMethodType.
     */
    public BooleanMethodNode(BooleanMethodType type) {
        this.type = type;
    }

    /**
     * Evaluates this <code>BooleanMethodNode</code> and returns its boolean value.
     * 
     * @param robotID the robot ID to evaluate for.
     * @param world the world to evaluate for.
     * @return this BooleanMethodNode's value.
     */
    public boolean evaluate(int robotID, World world) {
        switch (type) {
            case BOX:
                return world.touchingBox(robotID);
            case ROBOT:
                return world.touchingRobot(robotID);
            case THING:
                return world.touchingThing(robotID);
            default: // WALL
                return world.touchingWall(robotID);
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
