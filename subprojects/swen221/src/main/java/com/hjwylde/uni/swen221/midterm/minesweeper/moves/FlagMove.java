package com.hjwylde.uni.swen221.midterm.minesweeper.moves;

import com.hjwylde.uni.swen221.midterm.minesweeper.Game;
import com.hjwylde.uni.swen221.midterm.minesweeper.Position;
import com.hjwylde.uni.swen221.midterm.minesweeper.SyntaxError;
import com.hjwylde.uni.swen221.midterm.minesweeper.squares.Square;

/*
 * Code for Mid-term Test, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Represents a move which either flags or unflags a square on the board.
 * 
 * @author David J. Pearce
 * 
 */
public class FlagMove extends Move {

    private boolean isFlagged;

    /**
     * Construct a FlagSquare move which either flags or unflags a given position.
     * 
     * @param position --- position to be flagged/unflagged.
     * @param isFlagged --- true if position is being flagged; false if it is being unflagged.
     */
    public FlagMove(Position position, boolean isFlagged) {
        super(position);
        this.isFlagged = isFlagged;
    }

    /**
     * Apply this move to a given game and check that it is valid. A square can only be flagged if
     * it is currently unflagged and vice versa.
     * 
     * @param game --- game to which this move is applied.
     */
    @Override
    public void apply(Game game) throws SyntaxError {
        Square square = game.squareAt(position);

        if (!square.isHidden())
            throw new SyntaxError("Can't flag a non-hidden square.");

        if (square.isFlagged() == isFlagged)
            throw new SyntaxError("Trying to flag a flagged square or unflag an unflagged square.");

        square.setFlagged(isFlagged);
    }

    @Override
    public String toString() {
        if (isFlagged)
            return "F" + position;

        return "U" + position;
    }
}
