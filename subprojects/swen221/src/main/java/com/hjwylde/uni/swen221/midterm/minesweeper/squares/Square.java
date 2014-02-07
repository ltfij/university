package com.hjwylde.uni.swen221.midterm.minesweeper.squares;

/*
 * Code for Mid-term Test, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Represents a square on the game board. Every square can be either hidden or not, and flagged or
 * not.
 * 
 * @author David J. Pearce
 * 
 */
public abstract class Square {

    private boolean isHidden;
    private boolean isFlagged;

    /**
     * Construct a square which initially hidden and un-flagged.
     */
    public Square() {
        isFlagged = false;
        isHidden = true;
    }

    /**
     * Check whether this square is flagged or not.
     */
    public boolean isFlagged() {
        return isFlagged;
    }

    /**
     * Check whether this square is hidden or not.
     */
    public boolean isHidden() {
        return isHidden;
    }

    /**
     * Set the flag status of this square.
     * 
     * @param flag --- true if being flagged; false if being unflagged.
     */
    public void setFlagged(boolean flag) {
        isFlagged = flag;
    }

    /**
     * Set the hidden status of this square.
     * 
     * @param hidden --- true if being hidden; false if being unhidden.
     */
    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }
}
