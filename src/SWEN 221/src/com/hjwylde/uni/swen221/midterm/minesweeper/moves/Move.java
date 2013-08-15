package com.hjwylde.uni.swen221.midterm.minesweeper.moves;

import com.hjwylde.uni.swen221.midterm.minesweeper.Game;
import com.hjwylde.uni.swen221.midterm.minesweeper.Position;
import com.hjwylde.uni.swen221.midterm.minesweeper.SyntaxError;

/*
 * Code for Mid-term Test, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Represents a move taken during a minesweeper game. Since every move has a
 * position that it being modified, it is contained here.
 * 
 * @author David J. Pearce
 * 
 */
public abstract class Move {
    
    protected Position position;
    
    /**
     * Construct a move for a given position on the board.
     * 
     * @param position the position.
     */
    public Move(Position position) {
        this.position = position;
    }
    
    /**
     * Apply this move to the board, whilst checking that it is valid. If the
     * move is invalid, then a syntax error should be thrown.
     * 
     * @param game
     *            --- game to validate this move against.
     */
    public abstract void apply(Game game) throws SyntaxError;
    
    public Position getPosition() {
        return position;
    }
}
