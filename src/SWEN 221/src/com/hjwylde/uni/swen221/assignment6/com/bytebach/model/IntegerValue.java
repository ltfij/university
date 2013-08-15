package com.hjwylde.uni.swen221.assignment6.com.bytebach.model;

/*
 * Code for Assignment 6, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class IntegerValue implements Value {
    
    private final int value;
    
    public IntegerValue(int val) {
        value = val;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof IntegerValue) {
            IntegerValue v = (IntegerValue) o;
            return v.value == value;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return value;
    }
    
    @Override
    public String toString() {
        return Integer.toString(value);
    }
    
    public int value() {
        return value;
    }
}
