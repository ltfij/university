package com.hjwylde.uni.swen221.assignment3.chessview.pieces;

import com.hjwylde.uni.swen221.assignment3.chessview.Board;
import com.hjwylde.uni.swen221.assignment3.chessview.Position;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public interface Piece {
    
    /**
     * Check whether or not a given move on a given board is valid. For takes,
     * the piece being taken must be supplied.
     * 
     * @param oldPosition
     *            --- position of this piece before move.
     * @param newPosition
     *            --- position of this piece after move.
     * @param isTaken
     *            --- piece being taken, or null if no piece taken.
     * @param board
     *            --- board on which the validity of this move is being checked.
     */
    public boolean isValidMove(Position oldPosition, Position newPosition,
        Piece isTaken, Board board);
    
    /**
     * Determine whether this piece is white or black.
     */
    public boolean isWhite();
}
