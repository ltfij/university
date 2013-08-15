package com.hjwylde.uni.comp261.assignment3.robotSimulator.parser.nodes;

import java.awt.Graphics;

import com.hjwylde.uni.comp261.assignment3.robotSimulator.World;
import com.hjwylde.uni.comp261.assignment3.robotSimulator.parser.types.InstructionMethodType;

/*
 * Code for Assignment 3, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A non-terminal node representing a INSTRUCTION_METHOD.
 * 
 * @author Henry J. Wylde
 */
public class InstructionMethodNode implements Node {
    
    private final InstructionMethodType type;
    
    /**
     * Constructs a <code>InstructionMethodNode</code> of the specified type.
     * 
     * @param type the InstructionMethodType.
     */
    public InstructionMethodNode(InstructionMethodType type) {
        this.type = type;
    }
    
    /**
     * Executes this <code>InstructionMethodNode</code>.
     * 
     * @param robotID the robot ID to execute for.
     * @param world the world to execute for.
     * @param g the graphics object to animate this execution.
     */
    public void execute(int robotID, World world, Graphics g) {
        switch (type) {
        case BOX:
            world.turnTowardsFirstBox(robotID, g);
            
            break;
        default: // THING
            world.turnTowardsFirstThing(robotID, g);
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