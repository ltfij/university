package com.hjwylde.uni.swen221.assignment03.chessview;

import com.hjwylde.uni.swen221.assignment03.chessview.moves.Check;
import com.hjwylde.uni.swen221.assignment03.chessview.moves.Move;
import com.hjwylde.uni.swen221.assignment03.chessview.pieces.*;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Board {
    
    private Piece[][] pieces; // this is the underlying data structure for a board.
    
    /**
     * Construct an initial board.
     */
    public Board() {
        pieces = new Piece[9][9];
        
        for (int i = 1; i <= 8; ++i) {
            pieces[2][i] = new Pawn(true);
            pieces[7][i] = new Pawn(false);
        }
        
        // rooks
        pieces[1][1] = new Rook(true);
        pieces[1][8] = new Rook(true);
        pieces[8][1] = new Rook(false);
        pieces[8][8] = new Rook(false);
        
        // knights
        pieces[1][2] = new Knight(true);
        pieces[1][7] = new Knight(true);
        pieces[8][2] = new Knight(false);
        pieces[8][7] = new Knight(false);
        
        // bishops
        pieces[1][3] = new Bishop(true);
        pieces[1][6] = new Bishop(true);
        pieces[8][3] = new Bishop(false);
        pieces[8][6] = new Bishop(false);
        
        // king + queen
        pieces[1][4] = new Queen(true);
        pieces[1][5] = new King(true);
        pieces[8][4] = new Queen(false);
        pieces[8][5] = new King(false);
    }
    
    /**
     * Construct a board which is identical to another board.
     */
    public Board(Board board) {
        pieces = new Piece[9][9];
        for (int row = 1; row <= 8; ++row)
            for (int col = 1; col <= 8; ++col)
                pieces[row][col] = board.pieces[row][col];
    }
    
    /**
     * Apply a given move to this board, returning true is successful, otherwise
     * false.
     */
    public boolean apply(Move move) {
        if (move.isValid(this)) { // If the move is valid...
            // Save the current state of the board so we may revert if after applying we find that
            // the
            // move was actually invalid.
            Piece[][] boardState = new Piece[9][9];
            for (int row = 1; row <= 8; ++row)
                System.arraycopy(pieces[row], 1, boardState[row], 1, 8);
            
            move.apply(this);
            
            // Check if the player who just moved is in check... If they are, then their move was
            // illegal
            // because they should have moved to cancel the check...
            if (isInCheck(move.isWhite())) {
                // Revert the board state and return false.
                pieces = boardState;
                return false;
            }
            
            // Check if the notation said that the move resulted in check for the opposition... If
            // it
            // didn't, then the move is illegal because the notation was incorrect.
            if (move instanceof Check) {
                if (!isInCheck(!move.isWhite())) {
                    // Revert the board state and return false.
                    pieces = boardState;
                    return false;
                }
            } else if (isInCheck(!move.isWhite())) { // move instanceof NonCheck
                // The notation said that the move didn't result in check for the opposition... If
                // it
                // actually did, then the move is illegal because the notation was incorrect.
                
                // Revert the board state and return false.
                pieces = boardState;
                return false;
            }
            
            // Check that there isn't a pawn on the 1st or 8th ranks - this can never happen because
            // a
            // pawn can't go backwards and must promote if it moves to the end rank.
            for (int col = 1; col <= 8; col++)
                if ((pieces[1][col] instanceof Pawn)
                    || (pieces[8][col] instanceof Pawn)) {
                    
                    // Revert the board state and return false.
                    pieces = boardState;
                    return false;
                }
            
            return true;
        }
        
        // Move isn't valid.
        return false;
    }
    
    /**
     * The following method checks whether the given column is completely
     * clear, except for a given set of pieces. Observe that this doesn't
     * guarantee a given column move is valid, since this method does not
     * ensure anything about the relative positions of the given pieces.
     * 
     * @param startPosition - start of column
     * @param endPosition - end of column
     * @param exceptions - the list of pieces allowed on the column
     */
    public boolean clearColumnExcept(Position startPosition,
        Position endPosition, Piece... exceptions) {
        int diffCol = Math.abs(startPosition.column() - endPosition.column());
        int diffRow = Math.abs(startPosition.row() - endPosition.row());
        
        if ((diffCol != 0) || (diffRow == 0))
            return false;
        
        int row = startPosition.row();
        while (row != endPosition.row()) {
            Piece p = pieces[row][startPosition.column()];
            
            // If there exists a piece on this column and it isn't in the allowed exceptions...
            if ((p != null) && !Board.contains(p, exceptions))
                return false;
            
            row += ((row < endPosition.row()) ? 1 : -1);
        }
        
        return true;
    }
    
    /**
     * The following method checks whether the given diaganol is completely
     * clear, except for a given set of pieces. Observe that this doesn't
     * guarantee a given diaganol move is valid, since this method does not
     * ensure anything about the relative positions of the given pieces.
     * 
     * @param startPosition - start of diaganol
     * @param endPosition - end of diaganol
     * @param exceptions - the list of pieces allowed on the diaganol
     */
    public boolean clearDiaganolExcept(Position startPosition,
        Position endPosition, Piece... exceptions) {
        int startCol = startPosition.column();
        int endCol = endPosition.column();
        int startRow = startPosition.row();
        int endRow = endPosition.row();
        int diffCol = Math.abs(startPosition.column() - endPosition.column());
        int diffRow = Math.abs(startPosition.row() - endPosition.row());
        
        if ((diffCol ^ diffRow) != 0)
            return false;
        
        int row = startRow;
        int col = startCol;
        while (row != endRow) {
            Piece p = pieces[row][col];
            
            // If there exists a piece on this diagonal and it isn't in the allowed exceptions...
            if ((p != null) && !Board.contains(p, exceptions))
                return false;
            
            col += ((col < endCol) ? 1 : -1);
            row += ((row < endRow) ? 1 : -1);
        }
        
        return true;
    }
    
    /**
     * The following method checks whether the given row is completely
     * clear, except for a given set of pieces. Observe that this doesn't
     * guarantee a given row move is valid, since this method does not
     * ensure anything about the relative positions of the given pieces.
     * 
     * @param startPosition - start of row
     * @param endPosition - end of row
     * @param exceptions - the list of pieces allowed on the row
     */
    public boolean clearRowExcept(Position startPosition, Position endPosition,
        Piece... exceptions) {
        int diffCol = Math.abs(startPosition.column() - endPosition.column());
        int diffRow = Math.abs(startPosition.row() - endPosition.row());
        
        if ((diffRow != 0) || (diffCol == 0))
            return false;
        
        int col = startPosition.column();
        while (col != endPosition.column()) {
            Piece p = pieces[startPosition.row()][col];
            
            // If there exists a piece on this row and it isn't in the allowed exceptions...
            if ((p != null) && !Board.contains(p, exceptions))
                return false;
            
            col += ((col < endPosition.column()) ? 1 : -1);
        }
        
        return true;
    }
    
    /**
     * This method determines whether or not one side is in check.
     * 
     * @param isWhite
     *            --- true means check whether white is in check; otherwise,
     *            check black.
     */
    public boolean isInCheck(boolean isWhite) {
        Position kingPos = null;
        
        // First, find my king
        outer:
        for (int row = 1; row <= 8; ++row)
            for (int col = 1; col <= 8; ++col) {
                Position pos = new Position(row, col);
                Piece p = pieceAt(pos);
                if ((p instanceof King) && (p.isWhite() == isWhite)) {
                    // Found him.
                    kingPos = pos;
                    // The following will break out of the entire loop, not
                    // just the innermost loop. This isn't exactly great
                    // style, but it is pretty convenient here.
                    break outer;
                }
            }
        
        // Second, check opposition pieces to see whether they can take
        // my king or not. If one can, we're in check!
        return isInCheck(kingPos, isWhite);
    }
    
    /**
     * Checks whether the square at <code>kingPos</code> is in check. In other words, if any
     * opposing
     * piece is threatening that square. This is useful for checking whether a King can castle
     * through
     * a given square and for checking if a King is in check or not.
     * 
     * @param kingPos the position to check.
     * @param isWhite the color of the player to check.
     * @return true if player <code>isWhite</code> would be in check if they had a king at
     *         <code>kingPos</code>.
     */
    public boolean isInCheck(Position kingPos, boolean isWhite) {
        if (kingPos == null)
            return false;
        
        Piece king = pieceAt(kingPos);
        
        for (int row = 1; row <= 8; ++row)
            for (int col = 1; col <= 8; ++col) {
                Position pos = new Position(row, col);
                Piece p = pieceAt(pos);
                // If this is an opposition piece, and it can take my king, then we're definitely in
                // check.
                if ((p != null) && (p.isWhite() != isWhite)
                    && p.isValidMove(pos, kingPos, king, this))
                    
                    // p can take opposition king, so we're in check.
                    return true;
            }
        
        return false;
    }
    
    /**
     * Move a piece from one position to another.
     */
    public void move(Position oldPosition, Position newPosition) {
        Piece p = pieces[oldPosition.row()][oldPosition.column()];
        pieces[newPosition.row()][newPosition.column()] = p;
        pieces[oldPosition.row()][oldPosition.column()] = null;
    }
    
    public Piece pieceAt(Position pos) {
        return pieces[pos.row()][pos.column()];
    }
    
    public void setPieceAt(Position pos, Piece piece) {
        pieces[pos.row()][pos.column()] = piece;
    }
    
    @Override
    public String toString() {
        String r = "";
        for (int row = 8; row != 0; row--) {
            r += row + "|";
            for (int col = 1; col <= 8; col++) {
                Piece p = pieces[row][col];
                if (p != null)
                    r += p + "|";
                else
                    r += "_|";
            }
            r += "\n";
        }
        return r + "  a b c d e f g h";
    }
    
    private static boolean contains(Piece p1, Piece... pieces) {
        if (p1 == null)
            return false;
        
        for (Piece p2 : pieces)
            if ((p2 != null) && (p1 == p2))
                return true;
        
        return false;
    }
}
