package com.hjwylde.uni.swen221.assignment05.cards.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*
 * Code for Assignment 5, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Represents a trick round for the card game. A trick round consists of each of the 4 players
 * playing one card each.
 * 
 * @author Henry J. Wylde
 */
public class TrickImpl implements Trick {

    private Player lead;

    private Map<Player, Card> played;

    /**
     * Creates a new round with the given lead player.
     * 
     * @param lead the lead player for the round.
     */
    public TrickImpl(Player lead) {
        this.lead = lead;

        played = new HashMap<>();
    }

    /*
     * @see assignment5.cards.game.Trick#getWinner()
     */
    @Override
    public Player getWinner() {
        if (played.size() != 4) // If the round isn't finished...
            return null;

        // Find the winner...
        Player winner = lead;
        for (Entry<Player, Card> e : played.entrySet())
            if (e.getValue().suit() == leadSuit())
                if (played(e.getKey()).compareTo(played(winner)) > 0)
                    winner = e.getKey();

        return winner;
    }

    /*
     * @see assignment5.cards.game.Trick#leadSuit()
     */
    @Override
    public Suit leadSuit() {
        if (played.containsKey(lead))
            return played(lead).suit();

        return null;
    }

    /*
     * @see assignment5.cards.game.Trick#nextPlayer()
     */
    @Override
    public Player nextPlayer() {
        if (played.isEmpty()) // If no players have played in this round...
            return lead;

        if (played.size() == 4) // If all players have played in this round...
            return null;

        // Find the next player.
        Player next = lead;
        while (played.containsKey(next.next()))
            next = next.next();

        return next.next();
    }

    /*
     * @see assignment5.cards.game.Trick#play(assignment5.cards.game.Player,
     * assignment5.cards.game.Card)
     */
    @Override
    public void play(Player player, Card card) throws IllegalMoveException {
        if (!player.equals(nextPlayer()))
            throw new IllegalMoveException();

        // If the player isn't the lead, then the card they play has to follow the lead suit if they
        // can.
        if (!player.equals(lead))
            if ((player.getHand().has(leadSuit()) != null) && (card.suit() != leadSuit()))
                throw new IllegalMoveException();

        player.getHand().removeCard(card); // Don't want them keeping the card do we?
        played.put(player, card);
    }

    /*
     * @see assignment5.cards.game.Trick#played(assignment5.cards.game.Player)
     */
    @Override
    public Card played(Player player) {
        return (played.get(player));
    }
}
