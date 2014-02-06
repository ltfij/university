package com.hjwylde.uni.swen221.assignment03.chessview.moves;

import com.hjwylde.uni.swen221.assignment03.chessview.Board;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A wrapper for a move. Shows that the move it wraps does not result in a check.
 * 
 * @author Henry J. Wylde
 */
public class NonCheck implements Move {
    
    private MultiPieceMove move;
    
    /**
     * Creates a new NonCheck move that wraps the given move.
     * 
     * @param move the move to wrap.
     */
    public NonCheck(MultiPieceMove move) {
        this.move = move;
    }
    
    /*
     * @see assignment3.chessview.moves.Move#apply(assignment3.chessview.Board)
     */
    @Override
    public void apply(Board board) {
        move.apply(board);
    }
    
    /*
     * @see assignment3.chessview.moves.Move#isValid(assignment3.chessview.Board)
     */
    @Override
    public boolean isValid(Board board) {
        return move.isValid(board);
    }
    
    /*
     * @see assignment3.chessview.moves.Move#isWhite()
     */
    @Override
    public boolean isWhite() {
        return move.isWhite();
    }
    
    /**
     * Gets the move that this NonCheck is wrapping.
     * 
     * @return the move that is wrapped.
     */
    public MultiPieceMove move() {
        return move;
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return move.toString();
    }
}