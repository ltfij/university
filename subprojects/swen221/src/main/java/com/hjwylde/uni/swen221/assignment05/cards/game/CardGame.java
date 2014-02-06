package com.hjwylde.uni.swen221.assignment05.cards.game;

import java.util.*;

/*
 * Code for Assignment 5, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class CardGame {
    
    private Trick trick;
    private Map<Player, Integer> numTricks = new HashMap<>();
    
    public CardGame() {
        for (Player p : Player.values())
            numTricks.put(p, 0);
        List<Card> deck = CardGame.createDeck();
        Collections.shuffle(deck);
        Player player = Player.NORTH;
        for (Card c : deck) {
            player.getHand().addCard(c);
            player = player.next();
        }
        trick = new TrickImpl(Player.getRandom());
    }
    
    public Trick getTrick() {
        return trick;
    }
    
    /**
     * Returns the winner of the game.
     */
    public String getWinner() {
        if (!isFinished())
            throw new IllegalArgumentException("Game not finished!");
        final int northSouth = numTricks.get(Player.NORTH)
            + numTricks.get(Player.SOUTH);
        final int eastWest = numTricks.get(Player.EAST)
            + numTricks.get(Player.WEST);
        if (northSouth > eastWest)
            return "Partnership NORTH/SOUTH";
        return "Partnership EAST/WEST";
    }
    
    /**
     * Check whether game is finished.
     */
    public boolean isFinished() {
        for (Player p : Player.values())
            if (!p.getHand().cards().isEmpty())
                return false;
        return true;
    }
    
    /**
     * Signal that it's time for the next round.
     */
    public void nextRound() {
        Player winner = getTrick().getWinner();
        numTricks.put(winner, numTricks.get(winner) + 1);
        trick = new TrickImpl(winner);
    }
    
    @Override
    public String toString() {
        String r = "";
        for (Player player : Player.values()) {
            if (!"".equals(r))
                r += ", ";
            r += player + ":" + numTricks.get(player);
        }
        return r;
    }
    
    public static List<Card> createDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values())
            for (Rank rank : Rank.values())
                deck.add(new Card(suit, rank));
        return deck;
    }
}
