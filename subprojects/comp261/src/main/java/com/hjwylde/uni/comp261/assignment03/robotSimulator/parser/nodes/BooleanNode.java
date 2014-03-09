package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.nodes;

import java.util.Map;

import com.hjwylde.uni.comp261.assignment03.robotSimulator.World;
import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types.BooleanType;

/*
 * Code for Assignment 3, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A non-terminal node representing a BOOLEAN.
 * 
 * @author Henry J. Wylde
 */
public class BooleanNode implements Node {

    private final BooleanType type;

    // For type = BooleanType.NOT
    private BooleanNode bool;

    // For type = BooleanType.CONDITION
    private BooleanConditionNode boolCondition;

    // For type = BooleanType.TOUCHING
    private BooleanMethodNode boolMethod;

    /**
     * Constructs a <code>BooleanNode</code> of the specified type.
     * 
     * @param type the BooleanType.
     */
    public BooleanNode(BooleanType type) {
        this.type = type;
    }

    /**
     * Evaluates this <code>BooleanNode</code> and returns its boolean value.
     * 
     * @param variables a list with variable values.
     * @param robotID the robot ID to evaluate for.
     * @param world the world to evaluate for.
     * @return this BooleanNode's value.
     */
    public boolean evaluate(Map<VariableNode, Integer> variables, int robotID, World world) {
        switch (type) {
            case TRUE:
                return true;
            case FALSE:
                return false;
            case CONDITION:
                return boolCondition.evaluate(variables, robotID, world);
            case NOT:
                return !bool.evaluate(variables, robotID, world);
            default: // TOUCHING
                return boolMethod.evaluate(robotID, world);
        }
    }

    /**
     * @param bool the <code>bool</code> to set.
     */
    public void setBool(BooleanNode bool) {
        this.bool = bool;
    }

    /**
     * @param boolCondition the <code>boolCondition</code> to set.
     */
    public void setBoolCondition(BooleanConditionNode boolCondition) {
        this.boolCondition = boolCondition;
    }

    /**
     * @param boolMethod the <code>boolMethod</code> to set.
     */
    public void setBoolMethod(BooleanMethodNode boolMethod) {
        this.boolMethod = boolMethod;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        switch (type) {
            case CONDITION:
                return "(" + boolCondition + ")";
            case NOT:
                return "!" + bool;
            case TOUCHING:
                return type.getName() + boolMethod;
            default:
                return type.getName();
        }
    }
}
