package com.hjwylde.uni.swen221.assignment03.chessview.pieces;

import com.hjwylde.uni.swen221.assignment03.chessview.Board;
import com.hjwylde.uni.swen221.assignment03.chessview.Position;

/*
 * Code for Assignment 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Represents a King piece in the game of Chess.
 * 
 * @author Henry J. Wylde
 */
public class King extends PieceImpl implements Piece {

    /**
     * Creates a new King with the specified color.
     * 
     * @param isWhite true if the King is for the white player.
     */
    public King(boolean isWhite) {
        super(isWhite);
    }

    /*
     * @see assignment3.chessview.pieces.Piece#isValidMove(assignment3.chessview.Position,
     * assignment3.chessview.Position, assignment3.chessview.pieces.Piece,
     * assignment3.chessview.Board)
     */
    @Override
    public boolean isValidMove(Position oldPosition, Position newPosition, Piece isTaken,
            Board board) {
        Piece p = board.pieceAt(oldPosition);
        Piece t = board.pieceAt(newPosition);

        int diffCol = Math.abs(oldPosition.column() - newPosition.column());
        int diffRow = Math.abs(oldPosition.row() - newPosition.row());

        // If the board piece does not match notation piece...
        if (!equals(p))
            return false;

        // If the board piece to take does not match notation piece to take...
        if ((t != isTaken) && ((isTaken == null) || !isTaken.equals(t)))
            return false;

        // Return true if the movement matches that of a King: the total squares moved is 1.
        return ((diffCol | diffRow) == 1);
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (isWhite)
            return "K";

        return "k";
    }
}
