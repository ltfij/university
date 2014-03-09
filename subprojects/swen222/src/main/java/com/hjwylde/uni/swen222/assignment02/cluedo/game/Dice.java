package com.hjwylde.uni.swen222.assignment02.cluedo.game;

/**
 * A simple dice class that provides a roll method.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 2/08/2013
 */
public final class Dice {

    /**
     * This class cannot be instantiated
     */
    private Dice() {}

    /**
     * Rolls the dice! This method returns a random number between 1 and 6 inclusive.
     * 
     * @return a random number between 1 and 6 inclusive.
     */
    public static int roll() {
        return 1 + (int) (Math.random() * 6);
    }
}
