package com.hjwylde.uni.swen221.assignment2.walker;

import maze.Direction;
import maze.View;
import maze.Walker;

/*
 * Code for Assignment 2, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A walker which randomly moves about the board.
 */
public class RandomWalker extends Walker {
    
    /**
     * Creates a new random walker.
     */
    public RandomWalker() {
        super("Random");
    }
    
    @Override
    public Direction move(View v) {
        int random = (int) (Math.random() * 4);
        switch (random) {
        case 0:
            return Direction.NORTH;
        case 1:
            return Direction.EAST;
        case 2:
            return Direction.SOUTH;
        default:
            return Direction.WEST;
        }
    }
}
