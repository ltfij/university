package com.hjwylde.uni.swen221.assignment5.cards.game;

// DO NOT REMOVE OR CHANGE METHODS IN THIS INTERFACE!

/*
 * Code for Assignment 5, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A round (also called trick) in our card game.
 * The player who leads a trick is allowed to play an arbitrary card from their hand.
 * The other players follow in clockwise direction. They have to follow suit if they can,
 * i.e. they must play a card of the same suit as the lead player if possible.
 * When every player has played a card, the winner of the trick can be determined.
 * A trick is won by the player who has played the highest-ranked card of the suit led by the
 * leader.
 */
public interface Trick {
    
    /**
     * Returns the winner of this trick.
     * 
     * @return the winner.
     */
    Player getWinner();
    
    /**
     * Returns the suit of the lead player;
     * null if the lead player hasn't played yet.
     * 
     * @return the lead suit.
     */
    Suit leadSuit();
    
    /**
     * Determine the next player to play.
     * If trick has finished (all players have played once), returns null.
     * 
     * @return the next player.
     */
    Player nextPlayer();
    
    /**
     * A player plays a card. This should check that playing the given card by the given player
     * is a valid move and, if that is the case, remove the card from the player's hand.
     * 
     * @param player the player to play a card.
     * @param card the card the player plays.
     * 
     * @throws IllegalMoveException if the move is not allowed.
     */
    void play(Player player, Card card) throws IllegalMoveException;
    
    /**
     * Get the card played by a given player.
     * Returns null if the player has not played yet.
     * 
     * @param player the player to ask for the card of.
     * @return the card the player played.
     */
    Card played(Player player);
}