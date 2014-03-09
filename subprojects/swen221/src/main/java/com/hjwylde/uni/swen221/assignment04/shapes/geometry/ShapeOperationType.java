package com.hjwylde.uni.swen221.assignment04.shapes.geometry;

/*
 * Code for Assignment 4, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Some basic shape operation types.
 * 
 * @author Henry J. Wylde
 */
public enum ShapeOperationType {

    /**
     * The difference is written as "A - B" or "A \ B" and represents all parts of A that aren't in
     * B.
     */
    DIFFERENCE('-'),
    /**
     * The intersection is written as "A & B" and represents all parts that are in both A and B.
     */
    INTERSECTION('&'),
    /**
     * The union is written as "A + B" and represents all parts that are in either A or B.
     */
    UNION('+');

    /**
     * The operator character.
     */
    private char op;

    /**
     * Specifies the character that represents this ShapeOperationType.
     * 
     * @param op the operation character.
     */
    private ShapeOperationType(char op) {
        this.op = op;
    }

    /*
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return op + "";
    }

    /**
     * Tries to return the ShapeOperationType based on the input character. Returns null if the
     * operator does not correspond to a ShapeOperationType.
     * 
     * @param op the character to find the corresponding ShapeOperationType for.
     * @return the ShapeOperationType or null if the character doesn't correspond to one.
     */
    public static ShapeOperationType getFromOperator(char op) {
        switch (op) {
            case '-':
                return DIFFERENCE;
            case '&':
                return INTERSECTION;
            case '+':
                return UNION;
        }

        return null;
    }

}
