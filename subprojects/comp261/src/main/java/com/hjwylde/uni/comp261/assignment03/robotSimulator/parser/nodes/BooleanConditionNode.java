package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.nodes;

import java.util.Map;

import com.hjwylde.uni.comp261.assignment03.robotSimulator.World;
import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types.BooleanConditionType;
import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types.BooleanConditionType.BooleanConditionOpType;

/*
 * Code for Assignment 3, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A non-terminal node representing a BOOLEAN_CONDITION.
 * 
 * @author Henry J. Wylde
 */
public class BooleanConditionNode implements Node {
    
    private final BooleanConditionType type;
    private BooleanConditionOpType opType;
    
    // For type = BooleanConditionType.BOOLEAN
    private BooleanNode firstBool;
    private BooleanNode secBool;
    
    // For type = BooleanConditionType.VALUE
    private ValueNode firstVal;
    private ValueNode secVal;
    
    /**
     * Constructs a <code>BooleanConditionNode</code> of the specified type.
     * 
     * @param type the BooleanConditionType.
     */
    public BooleanConditionNode(BooleanConditionType type) {
        this.type = type;
    }
    
    /**
     * Evaluates this <code>BooleanConditionNode</code> and returns its boolean
     * value.
     * 
     * @param variables a list with variable values.
     * @param robotID the robot ID to evaluate for.
     * @param world the world to evaluate for.
     * @return this BooleanConditionNode's value.
     */
    public boolean evaluate(Map<VariableNode, Integer> variables, int robotID,
        World world) {
        switch (type) {
        case BOOLEAN:
            switch (opType) {
            case OR:
                return firstBool.evaluate(variables, robotID, world)
                    || secBool.evaluate(variables, robotID, world);
            default: // AND
                return firstBool.evaluate(variables, robotID, world)
                    && secBool.evaluate(variables, robotID, world);
            }
        default: // VALUE
            switch (opType) {
            case EQUALS:
                return firstVal.evaluate(variables, robotID, world) == secVal
                    .evaluate(variables, robotID, world);
            case NOT_EQUALS:
                return firstVal.evaluate(variables, robotID, world) != secVal
                    .evaluate(variables, robotID, world);
            case GREATER_THAN:
                return firstVal.evaluate(variables, robotID, world) > secVal
                    .evaluate(variables, robotID, world);
            default: // LESS_THAN
                return firstVal.evaluate(variables, robotID, world) < secVal
                    .evaluate(variables, robotID, world);
            }
        }
    }
    
    /**
     * @param firstBool the <code>firstBool</code> to set.
     */
    public void setFirstBool(BooleanNode firstBool) {
        this.firstBool = firstBool;
    }
    
    /**
     * @param firstVal the <code>firstVal</code> to set.
     */
    public void setFirstVal(ValueNode firstVal) {
        this.firstVal = firstVal;
    }
    
    public void setOpType(BooleanConditionOpType opType) {
        this.opType = opType;
    }
    
    /**
     * @param secBool the <code>secBool</code> to set.
     */
    public void setSecBool(BooleanNode secBool) {
        this.secBool = secBool;
    }
    
    /**
     * @param secVal the <code>secVal</code> to set.
     */
    public void setSecVal(ValueNode secVal) {
        this.secVal = secVal;
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        switch (type) {
        case BOOLEAN:
            return firstBool + " " + opType.getName() + " " + secBool;
        default: // VALUE
            return firstVal + " " + opType.getName() + " " + secVal;
        }
    }
}