package com.hjwylde.uni.swen221.assignment3.chessview.moves;

import com.hjwylde.uni.swen221.assignment3.chessview.Board;
import com.hjwylde.uni.swen221.assignment3.chessview.Position;
import com.hjwylde.uni.swen221.assignment3.chessview.pieces.Pawn;
import com.hjwylde.uni.swen221.assignment3.chessview.pieces.Piece;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * This represents an "en passant move" --- http://en.wikipedia.org/wiki/En_passant.
 * 
 * @author djp
 */
public class EnPassant implements MultiPieceMove {
    
    /**
     * A boolean that keeps a flag for whether the en passant move is valid, in other words, if the
     * previous move was one of the opposite pawn moving 2 squares from it's original square.
     * 
     * Note: this is a very poor solution for checking, as it means we can't as easily add in
     * functionality to allow for the game to be played normally, instead the game has to read
     * notation.
     */
    private boolean isValid = false;
    
    private SinglePieceMove move;
    
    /**
     * Creates a new EnPassant move by wrapping a move. By default this move is invalid.
     * 
     * @param move the move that is en passant.
     */
    public EnPassant(SinglePieceMove move) {
        this.move = move;
    }
    
    /*
     * @see assignment3.chessview.moves.Move#apply(assignment3.chessview.Board)
     */
    @Override
    public void apply(Board board) {
        move.apply(board); // Move the pawn.
        board.setPieceAt(new Position(move.oldPosition().row(), move
            .newPosition().column()), null); // Take the other pawn.
    }
    
    /**
     * Gets the wrapped move.
     * 
     * @return the move being wrapped.
     */
    public SinglePieceMove getMove() {
        return move;
    }
    
    /*
     * @see assignment3.chessview.moves.Move#isValid(assignment3.chessview.Board)
     */
    @Override
    public boolean isValid(Board board) {
        if (!(move.piece() instanceof Pawn) || !isValid)
            return false;
        
        Position oldPos = move.oldPosition();
        Position newPos = move.newPosition();
        
        Piece p = board.pieceAt(oldPos);
        
        // If the board piece doesn't match the notation piece...
        if (!move.piece().equals(p))
            return false;
        
        // If the piece that is on the same row as us originally and the same column as our new
        // square
        // isn't a pawn...
        if (!(board.pieceAt(new Position(oldPos.row(), newPos.column()))
            .equals(new Pawn(!isWhite()))))
            return false;
        
        int dir = move.isWhite() ? 1 : -1;
        
        int diffCol = Math.abs(oldPos.column() - newPos.column());
        
        // Return true if we're moving 1 square in a diagonal in the correct direction.
        return ((diffCol == 1) && (newPos.row() == (oldPos.row() + dir)));
    }
    
    /*
     * @see assignment3.chessview.moves.Move#isWhite()
     */
    @Override
    public boolean isWhite() {
        return move.isWhite();
    }
    
    /**
     * Set whether this move is valid. Should be done when the previous move is an opposite colored
     * pawn moving 2 squares from it's original position.
     * 
     * @param isValid whether this EnPassant move is valid.
     */
    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return move.toString() + "ep";
    }
}