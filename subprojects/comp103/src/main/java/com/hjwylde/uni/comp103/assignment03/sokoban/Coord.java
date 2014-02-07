package com.hjwylde.uni.comp103.assignment03.sokoban;

/*
 * Code for Assignment 3, COMP 103 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A pair of row and col representing a coordinate in the warehouse. Also has a method to return the
 * next Coord in a given direction.
 */

public class Coord {

    public final int row; // because they are final (can't be changed), it is
    public final int col; // safe to make these fields final.

    Coord(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /** Returns the next coord in the specified direction */
    public Coord next(Direction dir) {
        if (dir == Direction.up)
            return new Coord(row - 1, col);
        if (dir == Direction.down)
            return new Coord(row + 1, col);
        if (dir == Direction.left)
            return new Coord(row, col - 1);
        if (dir == Direction.right)
            return new Coord(row, col + 1);
        return this;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }

}
