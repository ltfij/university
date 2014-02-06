package com.hjwylde.uni.swen221.assignment05.cards.game;

/*
 * Code for Assignment 5, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Represents the possible ranks for a traditional playing card.
 * 
 * @author Henry J. Wylde, djp
 */
public enum Rank {
    
    /**
     * Two.
     */
    TWO("2"),
    /**
     * Three.
     */
    THREE("3"),
    /**
     * Four.
     */
    FOUR("4"),
    /**
     * Five.
     */
    FIVE("5"),
    /**
     * Six.
     */
    SIX("6"),
    /**
     * Seven.
     */
    SEVEN("7"),
    /**
     * Eight.
     */
    EIGHT("8"),
    /**
     * Nine.
     */
    NINE("9"),
    /**
     * Ten.
     */
    TEN("10"),
    /**
     * Jack.
     */
    JACK("J"),
    /**
     * Queen.
     */
    QUEEN("Q"),
    /**
     * King.
     */
    KING("K"),
    /**
     * Ace.
     */
    ACE("A");
    
    private final String name;
    
    private Rank(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    /**
     * Checks whether the rank is a picture rank. A picture rank is either ACE, KING, QUEEN or JACK.
     * 
     * @param r the rank to check.
     * @return true if it's a picture rank.
     */
    public static boolean isPicture(Rank r) {
        switch (r) {
        case ACE:
        case KING:
        case QUEEN:
        case JACK:
            return true;
        default:
            return false;
        }
    }
}