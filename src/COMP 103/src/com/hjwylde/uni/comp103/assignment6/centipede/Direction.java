package com.hjwylde.uni.comp103.assignment6.centipede;

/*
 * Code for Assignment 6, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Direction Class Author: pondy Description: Provides four distinct directions,
 * and methods to
 * obtain the direction left, right, and opposite a given direction.
 */
public enum Direction {
    North,
    South,
    East,
    West;
    
    public Direction left() {
        switch (this) {
        case North:
            return West;
        case South:
            return East;
        case East:
            return North;
        case West:
            return South;
        }
        return null;
    }
    
    public Direction opposite() {
        switch (this) {
        case North:
            return South;
        case South:
            return North;
        case East:
            return West;
        case West:
            return East;
        }
        return null;
    }
    
    public Direction right() {
        switch (this) {
        case North:
            return East;
        case South:
            return West;
        case East:
            return South;
        case West:
            return North;
        }
        return null;
    }
    
}
