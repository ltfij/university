package com.hjwylde.uni.swen221.assignment3.chessview.moves;

import com.hjwylde.uni.swen221.assignment3.chessview.Board;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * This represents a "check move". Note that, a check move can only be made up
 * from an underlying simple move; that is, we can't check a check move.
 * 
 * @author djp
 */
public class Check implements Move {
    
    private MultiPieceMove move;
    
    /**
     * Creates a new Check move that wraps the given move.
     * 
     * @param move the move being wrapped.
     */
    public Check(MultiPieceMove move) {
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
     * Gets the move that this Check is wrapping.
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
        return move.toString() + "+";
    }
}