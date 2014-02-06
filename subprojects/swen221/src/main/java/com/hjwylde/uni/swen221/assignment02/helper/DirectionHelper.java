package com.hjwylde.uni.swen221.assignment02.helper;

import maze.Direction;

/*
 * Code for Assignment 2, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * This class is a helper class just for providing some basic utility functions for operating on a
 * <code>Direction</code>. Preferably these methods would be part of the <code>Direction</code>
 * enum, but it wasn't possible to edit the source code to add them in.
 * 
 * @author Henry J. Wylde
 */
public class DirectionHelper {
    
    private DirectionHelper() {}
    
    /**
     * Returns the direction that is anti-clockwise to the input direction.
     * 
     * @param dir the input direction.
     * @return the direction that is directly anti-clockwise to the input.
     */
    public static Direction antiClockwise(Direction dir) {
        switch (dir) {
        case NORTH:
            return Direction.WEST;
        case EAST:
            return Direction.NORTH;
        case SOUTH:
            return Direction.EAST;
        default: // WEST
            return Direction.SOUTH;
        }
    }
    
    /**
     * Returns the direction that is clockwise to the input direction.
     * 
     * @param dir the input direction.
     * @return the direction that is directly clockwise to the input.
     */
    public static Direction clockwise(Direction dir) {
        switch (dir) {
        case NORTH:
            return Direction.EAST;
        case EAST:
            return Direction.SOUTH;
        case SOUTH:
            return Direction.WEST;
        default: // WEST
            return Direction.NORTH;
        }
    }
    
    /**
     * Returns the direction that is opposite to the input direction.
     * 
     * @param dir the input direction.
     * @return the direction that is directly opposite to the input.
     */
    public static Direction opposite(Direction dir) {
        switch (dir) {
        case NORTH:
            return Direction.SOUTH;
        case EAST:
            return Direction.WEST;
        case SOUTH:
            return Direction.NORTH;
        default: // WEST
            return Direction.EAST;
        }
    }
}
