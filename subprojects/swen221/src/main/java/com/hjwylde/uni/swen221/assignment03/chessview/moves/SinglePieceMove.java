package com.hjwylde.uni.swen221.assignment03.chessview.moves;

import com.hjwylde.uni.swen221.assignment03.chessview.Board;
import com.hjwylde.uni.swen221.assignment03.chessview.Position;
import com.hjwylde.uni.swen221.assignment03.chessview.pieces.*;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class SinglePieceMove implements MultiPieceMove {
    
    protected Piece piece;
    protected Position oldPosition;
    protected Position newPosition;
    
    public SinglePieceMove(Piece piece, Position oldPosition,
        Position newPosition) {
        this.piece = piece;
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
    }
    
    @Override
    public void apply(Board b) {
        b.move(oldPosition, newPosition);
    }
    
    @Override
    public boolean isValid(Board board) {
        return piece.isValidMove(oldPosition, newPosition, null, board);
    }
    
    @Override
    public boolean isWhite() {
        return piece.isWhite();
    }
    
    public Position newPosition() {
        return newPosition;
    }
    
    public Position oldPosition() {
        return oldPosition;
    }
    
    public Piece piece() {
        return piece;
    }
    
    @Override
    public String toString() {
        return SinglePieceMove.pieceChar(piece) + oldPosition + "-"
            + newPosition;
    }
    
    protected static String pieceChar(Piece p) {
        if (p instanceof Pawn)
            return "";
        else if (p instanceof Knight)
            return "N";
        else if (p instanceof Bishop)
            return "B";
        else if (p instanceof Rook)
            return "R";
        else if (p instanceof Queen)
            return "Q";
        else
            return "K";
    }
}
