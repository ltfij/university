package com.hjwylde.uni.swen221.assignment03.chessview.moves;

/*
 * Code for Assignment 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * This represents a "checkmate move". Note that, a checkmate move can only be made up from an
 * underlying simple move; that is, we can't check a check move.
 * 
 * @author Henry J. Wylde
 */
public class Checkmate extends Check {

    /**
     * Creates a new Checkmate move that wraps the given move.
     * 
     * @param move the move being wrapped.
     */
    public Checkmate(MultiPieceMove move) {
        super(move);
    }

    /*
     * @see assignment3.chessview.moves.Check#toString()
     */
    @Override
    public String toString() {
        return super.toString() + "+";
    }
}
