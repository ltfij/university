package com.hjwylde.uni.swen221.assignment03.chessview.moves;

import com.hjwylde.uni.swen221.assignment03.chessview.Board;
import com.hjwylde.uni.swen221.assignment03.chessview.Position;
import com.hjwylde.uni.swen221.assignment03.chessview.pieces.King;
import com.hjwylde.uni.swen221.assignment03.chessview.pieces.Piece;
import com.hjwylde.uni.swen221.assignment03.chessview.pieces.Rook;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Represents a castle move in the game of Chess.
 * 
 * @author Henry J. Wylde
 */
public class Castling implements MultiPieceMove {
    
    /**
     * A boolean that keeps a flag for whether the castle move is valid, in other words, if the king
     * and rook to move have not yet moved.
     * 
     * Note: this is a very poor solution for checking. Ideally a flag would be kept in each piece
     * that would be set when it has moved, so we could just check that - but with how the Move's
     * are
     * implemented (they create new Piece's / copies) flags won't work.
     */
    private boolean isValid = true;
    
    private boolean isWhite;
    private boolean kingSide;
    
    /**
     * Creates a new castle move. Only needs 2 booleans to specify what type of castle it is, one
     * for
     * the color and one for what side to castle on.
     * 
     * @param isWhite the color of the castle move.
     * @param kingSide the side of the castle move.
     */
    public Castling(boolean isWhite, boolean kingSide) {
        this.isWhite = isWhite;
        this.kingSide = kingSide;
    }
    
    /*
     * @see assignment3.chessview.moves.Move#apply(assignment3.chessview.Board)
     */
    @Override
    public void apply(Board board) {
        Position kingPos = getKingPosition();
        Position rookPos = getRookPosition();
        
        board.move(kingPos, new Position(kingPos.row(), kingPos.column()
            + (kingSide ? 2 : -2)));
        board.move(rookPos, new Position(kingPos.row(), kingPos.column()
            + (kingSide ? 1 : -1)));
    }
    
    /**
     * Checks what side this castle move is.
     * 
     * @return true if the castle move is on the king side.
     */
    public boolean isKingSide() {
        return kingSide;
    }
    
    /*
     * @see assignment3.chessview.moves.Move#isValid(assignment3.chessview.Board)
     */
    @Override
    public boolean isValid(Board board) {
        if (!isValid) // If the king or rook has previously moved...
            return false;
        
        Position kingPos = getKingPosition();
        Position rookPos = getRookPosition();
        
        // If the square that the king is going to move through is in check...
        if (board.isInCheck(new Position(kingPos.row(), kingPos.column()
            + (kingSide ? 1 : -1)), isWhite))
            return false;
        
        Piece king = board.pieceAt(kingPos);
        Piece rook = board.pieceAt(rookPos);
        
        // If the board pieces don't match the notation pieces...
        if (!(king instanceof King) || !(rook instanceof Rook))
            return false;
        
        // Return true if the row is clear to castle on.
        return (board.clearRowExcept(kingPos, rookPos, king, rook));
    }
    
    /*
     * @see assignment3.chessview.moves.Move#isWhite()
     */
    @Override
    public boolean isWhite() {
        return isWhite;
    }
    
    /**
     * Sets whether this castle move is valid or not. Should be set based on a flag that remembers
     * whether the king or rook for this castle move has moved before.
     * 
     * @param isValid whether the castle move is valid.
     */
    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (kingSide)
            return "O-O";
        
        return "O-O-O";
    }
    
    /**
     * Helper method to get the position that the king should be in for this castle.
     * 
     * @return the position of the king.
     */
    private Position getKingPosition() {
        return new Position(isWhite ? 1 : 8, 5);
    }
    
    /**
     * Helper method to get the position that the rook should be in for this castle.
     * 
     * @return the position of the rook.
     */
    private Position getRookPosition() {
        return new Position(isWhite ? 1 : 8, kingSide ? 8 : 1);
    }
}
