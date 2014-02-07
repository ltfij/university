package com.hjwylde.uni.swen221.assignment03.chessview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.hjwylde.uni.swen221.assignment03.chessview.moves.*;
import com.hjwylde.uni.swen221.assignment03.chessview.pieces.*;

/*
 * Code for Assignment 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * This class represents a game of chess, which is essentially just a list moves that make up the
 * game.
 * 
 * @author djp
 */
public class ChessGame {

    private ArrayList<Round> rounds;

    /**
     * Construct a ChessGame object from a given game sheet, where each round occurs on a new line.
     * 
     * @param input the input to make the game off.
     * @throws IOException if the input cannot be read properly.
     */
    public ChessGame(Reader input) throws IOException {
        rounds = new ArrayList<>();

        /*
         * Flags to keep track of whether each color can castle on the king or queen side when
         * creating the moves.
         */
        boolean whiteKingCastle = true;
        boolean whiteQueenCastle = true;
        boolean blackKingCastle = true;
        boolean blackQueenCastle = true;

        BufferedReader reader = new BufferedReader(input);

        // First, read in the commands
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals(""))
                continue;
            int pos = line.indexOf(' ');
            if (pos == -1)
                pos = line.length();
            Move white = ChessGame.moveFromString(line.substring(0, pos), true);

            /*
             * Checks: whether the move is that of a king, rook or castle. If it is a rook or king,
             * it will change the correct flags to show that white can no longer castle on 1 side or
             * both. If it is a castle move, it will check what type of castle it is and set whether
             * it is valid based on the flags.
             */
            Move unwrappedWhite = ChessGame.unwrapMove(white);
            if (unwrappedWhite instanceof SinglePieceMove) {
                SinglePieceMove spmWhite = (SinglePieceMove) unwrappedWhite;
                if (spmWhite.piece() instanceof King) {
                    whiteKingCastle = false;
                    whiteQueenCastle = false;
                } else if (spmWhite.piece() instanceof Rook)
                    if (spmWhite.oldPosition().column() == 1)
                        whiteQueenCastle = false;
                    else if (spmWhite.oldPosition().column() == 8)
                        whiteKingCastle = false;
            } else if (unwrappedWhite instanceof Castling)
                ((Castling) unwrappedWhite)
                        .setIsValid(((Castling) unwrappedWhite).isKingSide() ? whiteKingCastle
                                : whiteQueenCastle);
            /*
             * End checks.
             */

            // Validate the move if it is an EnPassant only if it isn't the first move.
            if (rounds.size() > 0)
                ChessGame.validateEnPassant(unwrappedWhite,
                        ChessGame.unwrapMove(rounds.get(rounds.size() - 1).black()));

            Move black = null;
            if (pos != line.length()) {
                black = ChessGame.moveFromString(line.substring(pos + 1), false);

                /*
                 * Checks: whether the move is that of a king, rook or castle. If it is a rook or
                 * king, it will change the correct flags to show that black can no longer castle on
                 * 1 side or both. If it is a castle move, it will check what type of castle it is
                 * and set whether it is valid based on the flags.
                 */
                Move unwrappedBlack = ChessGame.unwrapMove(black);
                if (unwrappedBlack instanceof SinglePieceMove) {
                    SinglePieceMove spmBlack = (SinglePieceMove) unwrappedBlack;
                    if (spmBlack.piece() instanceof King) {
                        blackKingCastle = false;
                        blackQueenCastle = false;
                    } else if (spmBlack.piece() instanceof Rook)
                        if (spmBlack.oldPosition().column() == 1)
                            blackQueenCastle = false;
                        else if (spmBlack.oldPosition().column() == 8)
                            blackKingCastle = false;
                } else if (unwrappedBlack instanceof Castling)
                    ((Castling) unwrappedBlack)
                            .setIsValid(((Castling) unwrappedBlack).isKingSide() ? blackKingCastle
                                    : blackQueenCastle);

                // Validate the move if it is an EnPassant.
                if (unwrappedBlack instanceof EnPassant)
                    ChessGame.validateEnPassant(unwrappedBlack, unwrappedWhite);
            }

            rounds.add(new Round(white, black));
        }
    }

    public ChessGame(String sheet) throws IOException {
        this(new StringReader(sheet));
    }

    /**
     * This method computes the list of boards which make up the game. If an invalid move, or board
     * is encountered then a RuntimeException is thrown.
     * 
     * @return the list of boards.
     */
    public List<Board> boards() {
        ArrayList<Board> boards = new ArrayList<>();
        Board b = new Board();
        boards.add(b);
        boolean lastTime = false;
        for (Round r : rounds) {
            if (lastTime)
                return boards;
            b = new Board(b);
            if (!b.apply(r.white()))
                return boards;
            boards.add(b);
            if (r.black() != null) {
                b = new Board(b);
                if (!b.apply(r.black()))
                    return boards;
                boards.add(b);
            } else
                lastTime = true;
        }
        return boards;
    }

    public List<Round> rounds() {
        return rounds;
    }

    /**
     * Unwraps the move from it's NonCheck or Check wrapper so that the actual type of move may be
     * checked, and the actual piece for the move may be found.
     * 
     * @param move the move to unwrap.
     * @return the unwrapped move.
     */
    protected static Move unwrapMove(Move move) {
        if (move instanceof NonCheck)
            return ((NonCheck) move).move();
        else if (move instanceof Check)
            return ((Check) move).move();

        return move;
    }

    /**
     * Construct a move object from a given string.
     * 
     * @param str
     * @return
     */
    private static Move moveFromString(String str, boolean isWhite) {
        Piece piece;
        int index = 0;
        char lookahead = str.charAt(index);

        switch (lookahead) {
            case 'N':
                piece = new Knight(isWhite);
                index++;
                break;
            case 'B':
                piece = new Bishop(isWhite);
                index++;
                break;
            case 'R':
                piece = new Rook(isWhite);
                index++;
                break;
            case 'K':
                piece = new King(isWhite);
                index++;
                break;
            case 'Q':
                piece = new Queen(isWhite);
                index++;
                break;
            case 'O':
                if (str.equals("O-O"))
                    return new Castling(isWhite, true);
                else if (str.equals("O-O-O"))
                    return new Castling(isWhite, false);
                else
                    throw new IllegalArgumentException("invalid sheet");
            default:
                piece = new Pawn(isWhite);
        }

        Position start = ChessGame.positionFromString(str.substring(index, index + 2));
        char moveType = str.charAt(index + 2);
        Piece target = null;
        index = index + 3;

        if (moveType == 'x') {
            lookahead = str.charAt(index);
            switch (lookahead) {
                case 'N':
                    target = new Knight(!isWhite);
                    index++;
                    break;
                case 'B':
                    target = new Bishop(!isWhite);
                    index++;
                    break;
                case 'R':
                    target = new Rook(!isWhite);
                    index++;
                    break;
                case 'K':
                    target = new King(!isWhite);
                    index++;
                    break;
                case 'Q':
                    target = new Queen(!isWhite);
                    index++;
                    break;
                default:
                    target = new Pawn(!isWhite);
            }
        } else if (moveType != '-')
            throw new IllegalArgumentException("invalid sheet");

        Position end = ChessGame.positionFromString(str.substring(index, index + 2));
        index = index + 2;

        Move move;

        if (target != null)
            move = new SinglePieceTake(piece, target, start, end);
        else
            move = new SinglePieceMove(piece, start, end);

        if (((index + 1) < str.length()) && (str.charAt(index) == 'e')
                && (str.charAt(index + 1) == 'p')) {
            move = new EnPassant((SinglePieceMove) move);
            index += 2;
        } else if (((index + 1) < str.length()) && (str.charAt(index) == '=')) {
            lookahead = str.charAt(index + 1);
            Piece promotion;
            switch (lookahead) {
                case 'N':
                    promotion = new Knight(isWhite);
                    break;
                case 'B':
                    promotion = new Bishop(isWhite);
                    break;
                case 'R':
                    promotion = new Rook(isWhite);
                    break;
                case 'K':
                    promotion = new King(isWhite);
                    break;
                case 'Q':
                    promotion = new Queen(isWhite);
                    break;
                default:
                    throw new IllegalArgumentException("invalid sheet");
            }
            move = new PawnPromotion((SinglePieceMove) move, promotion);
            index += 2;
        }

        if ((index < str.length()) && (str.charAt(index) == '+')) {
            if ((str.length() > ++index) && (str.charAt(index) == '+')) { // Checkmate move.
                move = new Checkmate((MultiPieceMove) move);
                index++;
            } else
                move = new Check((MultiPieceMove) move);
        } else
            move = new NonCheck((MultiPieceMove) move);

        if (index != str.length())
            throw new IllegalArgumentException("invalid sheet");

        return move;
    }

    private static Position positionFromString(String pos) {
        if (pos.length() != 2)
            throw new IllegalArgumentException("invalid position: " + pos);
        int col = (pos.charAt(0) - 'a') + 1;
        int row = Integer.parseInt(pos.substring(1, 2));
        return new Position(row, col);
    }

    private static void validateEnPassant(Move epMove, Move prevMove) {
        if (!(epMove instanceof EnPassant))
            return;

        if (!(prevMove instanceof SinglePieceMove))
            return;

        int dir = prevMove.isWhite() ? 2 : -2;
        Position oldPos = ((SinglePieceMove) prevMove).oldPosition();
        Position newPos = ((SinglePieceMove) prevMove).newPosition();

        if ((newPos.row() == (oldPos.row() + dir))
                && (newPos.row() == (prevMove.isWhite() ? 4 : 5)))
            ((EnPassant) epMove).setIsValid(true);
    }
}
