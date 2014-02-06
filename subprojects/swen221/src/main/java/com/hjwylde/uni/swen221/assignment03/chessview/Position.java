package com.hjwylde.uni.swen221.assignment03.chessview;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public final class Position {
    
    private int row; // must be between 1 and 8
    private int col; // must be between 1 and 8
    
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public int column() {
        return col;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Position) {
            Position p = (Position) o;
            return (row == p.row) && (col == p.col);
        }
        return false;
    }
    
    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        
        int result = 1;
        result = (prime * result) + col;
        result = (prime * result) + row;
        
        return result;
        
        // return row ^ col;
    }
    
    public int row() {
        return row;
    }
    
    @Override
    public String toString() {
        return ((char) ('a' + (col - 1))) + Integer.toString(row);
    }
}