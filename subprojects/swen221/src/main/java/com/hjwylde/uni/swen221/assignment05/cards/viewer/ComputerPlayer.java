package com.hjwylde.uni.swen221.assignment05.cards.viewer;

import java.util.Set;

import com.hjwylde.uni.swen221.assignment05.cards.game.Card;
import com.hjwylde.uni.swen221.assignment05.cards.game.Player;
import com.hjwylde.uni.swen221.assignment05.cards.game.Suit;
import com.hjwylde.uni.swen221.assignment05.cards.game.Trick;

/*
 * Code for Assignment 5, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class ComputerPlayer {
    
    private final Player player;
    
    public ComputerPlayer(Player player) {
        this.player = player;
    }
    
    /**
     * Determine the next card to play.
     */
    public Card nextCard(Trick round) {
        Set<Card> cards = player.getHand().cards();
        Suit leadSuit = round.leadSuit();
        if (leadSuit != null)
            for (Card c : cards)
                if (c.suit().equals(leadSuit))
                    return c;
        for (Card c : cards)
            return c;
        throw new IllegalArgumentException("Hand of this player is empty!");
    }
}
