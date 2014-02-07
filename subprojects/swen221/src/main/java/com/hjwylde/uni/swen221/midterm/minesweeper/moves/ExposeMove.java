package com.hjwylde.uni.swen221.midterm.minesweeper.moves;

import com.hjwylde.uni.swen221.midterm.minesweeper.Game;
import com.hjwylde.uni.swen221.midterm.minesweeper.Position;
import com.hjwylde.uni.swen221.midterm.minesweeper.SyntaxError;
import com.hjwylde.uni.swen221.midterm.minesweeper.squares.BlankSquare;
import com.hjwylde.uni.swen221.midterm.minesweeper.squares.Square;

/*
 * Code for Mid-term Test, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Represents a move that exposes a square. The square may contain a bomb, or be blank. However, it
 * should not be flagged.
 * 
 * @author David J. Pearce
 * 
 */
public class ExposeMove extends Move {

    /**
     * Construct an ExposeMove at a given position on the board.
     * 
     * @param position the position.
     */
    public ExposeMove(Position position) {
        super(position);
    }

    /**
     * Apply this move to a given game and check it is valid. A square can only be exposed if it is
     * not already exposed. And, if either a bomb is exposed or there are no remaining unexposed
     * squares, then this should be an EndGame move.
     * 
     * @param game --- game to which this move is applied.
     */
    @Override
    public void apply(Game game) throws SyntaxError {
        Square square = game.squareAt(position);

        // Checks
        if (!square.isHidden() || square.isFlagged())
            throw new SyntaxError("Can't click on a flagged or non-hidden square.");

        // Check for whether this square should trigger a cascade effect.
        if (game.isWithinBounds(position))
            if (square instanceof BlankSquare)
                if (((BlankSquare) square).getNumberOfBombsAround() == 0)
                    game.cascade(position);

        square.setHidden(false); // now exposed
    }

    @Override
    public String toString() {
        return "E" + position;
    }
}
