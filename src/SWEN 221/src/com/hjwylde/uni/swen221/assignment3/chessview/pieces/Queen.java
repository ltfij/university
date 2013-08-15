package com.hjwylde.uni.swen221.assignment3.chessview.pieces;

import com.hjwylde.uni.swen221.assignment3.chessview.Board;
import com.hjwylde.uni.swen221.assignment3.chessview.Position;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Represents a Queen piece in the game of Chess.
 * 
 * @author Henry J. Wylde
 */
public class Queen extends PieceImpl implements Piece {
    
    /**
     * Creates a new Queen piece with the specified color.
     * 
     * @param isWhite true if the Queen is for the white player.
     */
    public Queen(boolean isWhite) {
        super(isWhite);
    }
    
    /*
     * @see assignment3.chessview.pieces.Piece#isValidMove(assignment3.chessview.Position,
     * assignment3.chessview.Position, assignment3.chessview.pieces.Piece,
     * assignment3.chessview.Board)
     */
    @Override
    public boolean isValidMove(Position oldPosition, Position newPosition,
        Piece isTaken, Board board) {
        Piece p = board.pieceAt(oldPosition);
        Piece t = board.pieceAt(newPosition);
        
        // If the board piece does not match notation piece...
        if (!equals(p))
            return false;
        
        // If the board piece to take does not match notation piece to take...
        if ((t != isTaken) && ((isTaken == null) || !isTaken.equals(t)))
            return false;
        
        // Return true if the queen has a clear column, row or diagonal to move in.
        return ((board.clearColumnExcept(oldPosition, newPosition, p, t)
            || board.clearRowExcept(oldPosition, newPosition, p, t) || board
                .clearDiaganolExcept(oldPosition, newPosition, p, t)));
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (isWhite)
            return "Q";
        
        return "q";
    }
}