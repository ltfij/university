package com.hjwylde.uni.swen221.assignment03.chessview.moves;

import com.hjwylde.uni.swen221.assignment03.chessview.Board;

/*
 * Code for Assignment 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A move is any move which is permitted by either the white or black player. This includes simple
 * moves (where pieces just take on new positions), take moves (where a piece is taken as well), and
 * check moves (where the opponent is put into check)
 * 
 * @author djp
 */
public interface Move {

    /**
     * Update the board to reflect the board after the move is played.
     */
    public void apply(Board board);

    /**
     * Check whether this move is valid or not.
     */
    public boolean isValid(Board board);

    /**
     * Is this move for white or black?
     * 
     * @return true if this move is white.
     */
    public boolean isWhite();
}
