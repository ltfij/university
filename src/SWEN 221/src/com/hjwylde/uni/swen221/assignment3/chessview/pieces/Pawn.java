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
 * Represents a Pawn piece in the game of Chess.
 * 
 * @author Henry J. Wylde
 */
public class Pawn extends PieceImpl implements Piece {
    
    /**
     * Creates a new Pawn with the specified color.
     * 
     * @param isWhite true if the Pawn is for the white player.
     */
    public Pawn(boolean isWhite) {
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
        
        int dir = isWhite ? 1 : -1;
        int oldRow = oldPosition.row();
        int newRow = newPosition.row();
        
        int diffCol = Math.abs(oldPosition.column() - newPosition.column());
        int diffRow = Math.abs(oldRow - newRow);
        
        // If the board piece does not match notation piece...
        if (!equals(p))
            return false;
        
        // If the board piece to take does not match notation piece to take...
        if ((t != isTaken) && ((isTaken == null) || !isTaken.equals(t)))
            return false;
        
        // If we're taking a piece...
        if (t != null)
            // Check that we're moving in a diagonal in the correct direction.
            return ((diffCol == 1) && (newRow == (oldRow + dir)));
        
        // We're not taking a piece, check that we're moving straight...
        if (diffCol != 0)
            return false;
        
        // If we're trying to move 2 squares, check that we're on our first move...
        if (diffRow == 2)
            return (((dir == 1) && (oldRow == 2)) || ((dir == -1) && (oldRow == 7)))
                && board.clearColumnExcept(oldPosition, newPosition, p);
        
        // Return true if we're moving in the correct direction by 1 square.
        return (newRow == (oldRow + dir));
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (isWhite)
            return "P";
        
        return "p";
    }
}
