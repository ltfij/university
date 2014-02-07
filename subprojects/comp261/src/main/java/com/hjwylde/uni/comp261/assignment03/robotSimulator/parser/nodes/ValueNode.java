package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.nodes;

import java.util.Map;

import com.hjwylde.uni.comp261.assignment03.robotSimulator.World;
import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types.ValueType;

/*
 * Code for Assignment 3, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A non-terminal node representing a VALUE.
 * 
 * @author Henry J. Wylde
 */
public class ValueNode implements Node {

    private final ValueType type;

    // For type = ValueType.INTEGER
    private int val;

    // For type = ValueType.VARIABLE
    private VariableNode variable;

    // For type = ValueType.GET
    private ValueMethodNode valMethod;

    /**
     * Constructs a <code>ValueNode</code> of the specified type.
     * 
     * @param type the ValueType.
     */
    public ValueNode(ValueType type) {
        this.type = type;
    }

    /**
     * Evaluates this <code>ValueNode</code> and returns its integer value.
     * 
     * @param variables a list with variable values.
     * @param robotID the robot ID to evaluate for.
     * @param world the world to evaluate for.
     * @return this ValueNode's value.
     */
    public int evaluate(Map<VariableNode, Integer> variables, int robotID, World world) {
        switch (type) {
            case VARIABLE:
                return variables.get(variable);
            case GET:
                return valMethod.evaluate(robotID, world);
            default:
                return val;
        }
    }

    /**
     * @param val the <code>val</code> to set.
     */
    public void setVal(int val) {
        this.val = val;
    }

    /**
     * @param valMethod the <code>valMethod</code> to set.
     */
    public void setValMethod(ValueMethodNode valMethod) {
        this.valMethod = valMethod;
    }

    /**
     * @param variable the <code>variable</code> to set.
     */
    public void setVariable(VariableNode variable) {
        this.variable = variable;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        switch (type) {
            case INTEGER:
                return String.valueOf(val);
            case VARIABLE:
                return variable.toString();
            default:
                return type.getName() + valMethod.toString();
        }
    }
}
