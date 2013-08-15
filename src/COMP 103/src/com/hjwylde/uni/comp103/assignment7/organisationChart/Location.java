package com.hjwylde.uni.comp103.assignment7.organisationChart;

/*
 * Code for Assignment 7, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Represents a position on the window
 */
public class Location {
    
    // Fields
    private final int x;
    private final int y;
    
    /*
     * Constructors
     */
    
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /*
     * Methods
     */
    
    public int distance(Location other) {
        return (int) (Math.hypot((x - other.x), (y - other.y)));
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
