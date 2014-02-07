package com.hjwylde.uni.swen221.midterm.minesweeper.moves;

import com.hjwylde.uni.swen221.midterm.minesweeper.Game;
import com.hjwylde.uni.swen221.midterm.minesweeper.Position;
import com.hjwylde.uni.swen221.midterm.minesweeper.SyntaxError;
import com.hjwylde.uni.swen221.midterm.minesweeper.squares.BombSquare;
import com.hjwylde.uni.swen221.midterm.minesweeper.squares.Square;

/*
 * Code for Mid-term Test, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Represents a move which ends the game. If all blank squares are exposed, then the player has one.
 * Or, if a bomb is exposed, then the player has lost.
 * 
 * @author David J. Pearce
 * 
 */
public class EndMove extends ExposeMove {

    private boolean isWinner;

    /**
     * Construct a EndGame object which represents a successful or unsuccessul outcome.
     * 
     * @param position --- position to which this move applies.
     * @param isWinner --- true if the player has won the game; false if a bomb is exposed.
     */
    public EndMove(Position position, boolean isWinner) {
        super(position);
        this.isWinner = isWinner;
    }

    /**
     * Apply this move to a given game and check that it is valid. An end move must either expose a
     * bomb or expose the last remaining blank square.
     * 
     * @param game --- game to which this move is applied.
     */
    @Override
    public void apply(Game game) throws SyntaxError {
        super.apply(game); // Must apply expose move first so as to trigger cascade and other
                           // checks.

        Square square = game.squareAt(position);

        if (isWinner) {
            if (square instanceof BombSquare)
                throw new SyntaxError(
                        "Winning move must be on a square that doesn't contain a bomb.");

            if (game.getStatus() != Game.PLAYER_WON)
                throw new SyntaxError("Winning move must end with no hidden blank squares.");
        } else if (!(square instanceof BombSquare))
            throw new SyntaxError("Loosing move must be on a square that contains a bomb.");
    }

    @Override
    public String toString() {
        if (isWinner)
            return "W" + position;

        return "L" + position;
    }
}
