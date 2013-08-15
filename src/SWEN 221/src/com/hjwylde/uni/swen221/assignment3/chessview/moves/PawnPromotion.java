package com.hjwylde.uni.swen221.assignment3.chessview.moves;

import com.hjwylde.uni.swen221.assignment3.chessview.Board;
import com.hjwylde.uni.swen221.assignment3.chessview.pieces.Pawn;
import com.hjwylde.uni.swen221.assignment3.chessview.pieces.Piece;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * This represents a pawn promotion.
 * 
 * @author djp
 */
public class PawnPromotion implements MultiPieceMove {
    
    private SinglePieceMove move;
    private Piece promotion;
    
    /**
     * Represents a Pawn move in which it is promoted.
     * 
     * @param move the move the pawn makes.
     * @param promotion the piece the pawn would like to promote to.
     */
    public PawnPromotion(SinglePieceMove move, Piece promotion) {
        this.move = move;
        this.promotion = promotion;
    }
    
    /*
     * @see assignment3.chessview.moves.Move#apply(assignment3.chessview.Board)
     */
    @Override
    public void apply(Board board) {
        board.setPieceAt(move.oldPosition(), null); // Remove the pawn from the board.
        board.setPieceAt(move.newPosition(), promotion); // Add the promoted piece to the board.
    }
    
    /*
     * @see assignment3.chessview.moves.Move#isValid(assignment3.chessview.Board)
     */
    @Override
    public boolean isValid(Board board) {
        if (!(move.piece() instanceof Pawn)) // If we're not actually a pawn...
            return false;
        
        // If we're not actually moving to the end row for our color...
        if ((move.isWhite() && (move.newPosition().row() != 8))
            || (!move.isWhite() && (move.newPosition().row() != 1)))
            return false;
        
        // Return true if the move is normally a valid move, this checks for the case of takes and
        // normal moves of the pawn.
        return move.isValid(board);
    }
    
    /*
     * @see assignment3.chessview.moves.Move#isWhite()
     */
    @Override
    public boolean isWhite() {
        return move.isWhite();
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return move.toString() + "=" + SinglePieceMove.pieceChar(promotion);
    }
}