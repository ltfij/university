package com.hjwylde.uni.swen221.assignment5.cards.game;

/*
 * Code for Assignment 5, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Represents the four possible suits of a traditional playing card.
 * 
 * @author Henry J. Wylde, djp
 */
public enum Suit {
    
    /**
     * Spades.
     */
    SPADES("S"),
    /**
     * Diamonds.
     */
    DIAMONDS("D"),
    /**
     * Clubs.
     */
    CLUBS("C"),
    /**
     * Hearts.
     */
    HEARTS("H");
    
    private final String name;
    
    /**
     * Creates a new suit with the given name.
     * 
     * @param name the name.
     */
    private Suit(String name) {
        this.name = name;
    }
    
    /*
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return name;
    }
    
}