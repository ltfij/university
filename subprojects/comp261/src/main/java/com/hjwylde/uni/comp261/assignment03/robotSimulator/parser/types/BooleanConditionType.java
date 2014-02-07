package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types;

/*
 * Code for Assignment 3, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A type representing the different options for the non-terminal BOOLEAN_CONDITION.
 * 
 * @author Henry J. Wylde
 */
public enum BooleanConditionType {

    VALUE("value"), BOOLEAN("boolean");

    private final String name;

    private BooleanConditionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public enum BooleanConditionOpType {

        EQUALS("=="), NOT_EQUALS("!="), GREATER_THAN(">"), LESS_THAN("<"), OR("||"), AND("&&");

        private final String name;

        private BooleanConditionOpType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
