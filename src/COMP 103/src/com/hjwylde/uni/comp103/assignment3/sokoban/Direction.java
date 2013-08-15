package com.hjwylde.uni.comp103.assignment3.sokoban;

/*
 * Code for Assignment 3, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * An enumerated type for the four directions, along with a method for getting
 * the opposite of a
 * direction
 */

public enum Direction {
    
    left,
    right,
    up,
    down;
    
    public Direction opposite() {
        if (this == right)
            return left;
        if (this == left)
            return right;
        if (this == up)
            return down;
        if (this == down)
            return up;
        return this;
    }
    
}
