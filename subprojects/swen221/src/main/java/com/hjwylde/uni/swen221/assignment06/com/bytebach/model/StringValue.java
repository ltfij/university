package com.hjwylde.uni.swen221.assignment06.com.bytebach.model;

/*
 * Code for Assignment 6, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class StringValue implements Value {
    
    private final String value;
    
    public StringValue(String val) {
        value = val;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof StringValue) {
            StringValue v = (StringValue) o;
            return v.value.equals(value);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
    @Override
    public String toString() {
        return value;
    }
    
    public String value() {
        return value;
    }
}
