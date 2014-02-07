package com.hjwylde.uni.swen222.lab03.battleships;

/**
 * This class represents a battle ship!
 * 
 * @author djp
 * 
 */
public class BattleShip {

    private final String name;
    private final int length;
    private int nhits; // number of hits so far

    /**
     * Construct new battle ship.
     * 
     * @param name Name of battle ship
     * @param length Length of battle ship (in squares)
     */
    public BattleShip(String name, int length) {
        this.name = name;
        this.length = length;
        nhits = 0; // no hits so far
    }

    /**
     * Check the length of this ship
     * 
     * @return
     */
    public int getLength() {
        return length;
    }

    /**
     * Check whether or not this battle ship has been destroyed
     */
    public boolean isDestroyed() {
        return nhits == length;
    }

    /**
     * Signal that this battle ship has been hit.
     */
    public void isHit() {
        nhits++;
    }
}
