package com.hjwylde.uni.swen221.assignment03.chessview;

import com.hjwylde.uni.swen221.assignment03.chessview.moves.Move;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A round consists of a move by white, and will normally also have a move by
 * black. The latter may not happen in the case that White wins the game.
 * 
 * @author djp
 * 
 */
public class Round {
    
    private Move white;
    private Move black;
    
    /**
     * Create a round from a white move, and an optional black move.
     * 
     * @param white - whites move; cannot be null;
     * @param black - blacks move; may be null.
     */
    public Round(Move white, Move black) {
        if (white == null)
            throw new IllegalArgumentException(
                "A round must always consist of a move by white");
        
        this.white = white;
        this.black = black;
    }
    
    public Move black() {
        return black;
    }
    
    @Override
    public String toString() {
        String r = white.toString();
        if (black != null)
            r += " " + black.toString();
        return r;
    }
    
    public Move white() {
        return white;
    }
}