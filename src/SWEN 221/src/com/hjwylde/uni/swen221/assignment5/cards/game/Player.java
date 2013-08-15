package com.hjwylde.uni.swen221.assignment5.cards.game;

import java.util.Random;

/*
 * Code for Assignment 5, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * The Player class represents a player
 * 
 * @author Henry J. Wylde
 */
public enum Player {
    /**
     * The NORTH player orders their cards by rank first and then suit.
     */
    NORTH(new Hand(Card.COMP_RANK_SUIT)),
    /**
     * The EAST player orders their cards by natural ordering, except that the ace is the smallest
     * card.
     */
    EAST(new Hand(Card.COMP_ACE_SMALLEST)),
    /**
     * The SOUTH player orders their cards by the natural ordering: suit first then rank.
     */
    SOUTH(new Hand()),
    /**
     * The WEST player orders their cards with the picture cards grouped together as the highest.
     */
    WEST(new Hand(Card.COMP_PICTURE_HIGHEST));
    
    private static Random randomGenerator = new Random();
    private Hand hand = new Hand();
    
    /**
     * Private constructer. There are only 4 static players available.
     * 
     * @param hand the hand the player uses.
     */
    private Player(Hand hand) {
        this.hand = hand;
    }
    
    /**
     * Gets the hand of this player.
     * 
     * @return the players hand.
     */
    public Hand getHand() {
        return hand;
    }
    
    /**
     * Gets the next player after this one. The order is as follows: NORTH -> EAST -> SOUTH -> WEST
     * ->
     * NORTH.
     * 
     * @return the next player.
     */
    public Player next() {
        if (equals(NORTH))
            return EAST;
        if (equals(EAST))
            return SOUTH;
        if (equals(SOUTH))
            return WEST;
        return NORTH;
    }
    
    /**
     * Gets a random player.
     * 
     * @return a random player.
     */
    public static Player getRandom() {
        switch (Player.randomGenerator.nextInt(4)) {
        case 0:
            return NORTH;
        case 1:
            return EAST;
        case 2:
            return SOUTH;
        default:
            return WEST;
        }
    }
}
