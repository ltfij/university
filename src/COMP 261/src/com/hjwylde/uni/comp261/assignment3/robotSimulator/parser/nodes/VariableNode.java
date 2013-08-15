package com.hjwylde.uni.comp261.assignment3.robotSimulator.parser.nodes;

import com.hjwylde.uni.comp261.assignment3.robotSimulator.parser.ParseException;

/*
 * Code for Assignment 3, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A non-terminal node representing a VARIABLE.
 * 
 * @author Henry J. Wylde
 */
public class VariableNode implements Node {
    
    public static final String NAME_REGEX = "\\$[a-z][A-Za-z0-9_]*";
    
    private final String name;
    
    /**
     * Constructs a <code>VariableNode</code> with the specified name. Throws an
     * error if the name doesn't match <code>NAME_REGEX</code>.
     * 
     * @param name the variable name.
     * 
     * @throws ParseException if the name isn't a valid variable name.
     */
    public VariableNode(String name) throws ParseException {
        if (!name.matches(VariableNode.NAME_REGEX))
            throw new ParseException("Illegal variable name \"" + name
                + "\". Does not match \"" + VariableNode.NAME_REGEX + "\".");
        
        this.name = name;
    }
    
    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        
        if ((obj == null) || !(obj instanceof VariableNode))
            return false;
        
        VariableNode other = (VariableNode) obj;
        if (!name.equals(other.name))
            return false;
        
        return true;
    }
    
    /**
     * @return the <code>name</code>.
     */
    public String getName() {
        return name;
    }
    
    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name;
    }
}