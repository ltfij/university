package com.hjwylde.uni.comp103.assignment03.sokoban;

/*
 * Code for Assignment 3, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/** An Enum for the possible cells, along with useful methods : */

public enum Cell {
    empty,
    wall,
    box,
    shelf,
    boxOnShelf;
    
    /** Whether the cell is free to move onto */
    public boolean free() {
        return ((this == empty) || (this == shelf));
    }
    
    /** Whether there is a box on this cell */
    public boolean hasBox() {
        return ((this == box) || (this == boxOnShelf));
    }
    
    /** The cell you get if you push a box off this cell */
    public Cell moveOff() {
        if (this == box)
            return empty;
        if (this == boxOnShelf)
            return shelf;
        return this;
    }
    
    /** The cell you get if you push a box on to this cell */
    public Cell moveOn() {
        if (this == empty)
            return box;
        if (this == shelf)
            return boxOnShelf;
        return this;
    }
    
}
