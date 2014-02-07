package com.hjwylde.uni.swen221.assignment03.chessview.moves;

import com.hjwylde.uni.swen221.assignment03.chessview.Board;
import com.hjwylde.uni.swen221.assignment03.chessview.Position;
import com.hjwylde.uni.swen221.assignment03.chessview.pieces.Piece;

/*
 * Code for Assignment 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class SinglePieceTake extends SinglePieceMove {

    private Piece isTaken;

    public SinglePieceTake(Piece piece, Piece isTaken, Position oldPosition, Position newPosition) {
        super(piece, oldPosition, newPosition);
        this.isTaken = isTaken;
    }

    @Override
    public boolean isValid(Board board) {
        return piece.isValidMove(oldPosition, newPosition, isTaken, board);
    }

    @Override
    public String toString() {
        return SinglePieceMove.pieceChar(piece) + oldPosition + "x"
                + SinglePieceMove.pieceChar(isTaken) + newPosition;
    }
}
