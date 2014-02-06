package com.hjwylde.uni.swen221.assignment06.com.bytebach.model;

/*
 * Code for Assignment 6, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class BooleanValue implements Value {
    
    private final boolean value;
    
    public BooleanValue(boolean val) {
        value = val;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof BooleanValue) {
            BooleanValue v = (BooleanValue) o;
            return v.value == value;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return value ? 1 : 0;
    }
    
    @Override
    public String toString() {
        return Boolean.toString(value);
    }
    
    public boolean value() {
        return value;
    }
}
