package com.hjwylde.uni.swen221.assignment05.cards.game;

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/*
 * Code for Assignment 5, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A card hand is a set of up to 13 cards.
 */
public class Hand {

    private SortedSet<Card> cards;

    /**
     * Creates a hand that uses the natural ordering of a card.
     */
    public Hand() {
        this(null);
    }

    /**
     * Creates a hand that uses the given comparator to order the hand.
     * 
     * @param comp the comparator.
     */
    public Hand(Comparator<Card> comp) {
        if (comp == null)
            cards = new TreeSet<>();
        else
            cards = new TreeSet<>(comp);
    }

    /**
     * Add a card to the hand.
     * 
     * @param card the card to add.
     */
    public void addCard(Card card) {
        if (cards.contains(card))
            throw new IllegalArgumentException("Cannot add card to hand; hand already has card!");
        if (isFull())
            throw new IllegalArgumentException("Cannot add card to hand; hand is full!");

        cards.add(card);
    }

    /**
     * Returns an unmodifiable Set of cards that are in this hand. This prevents clients from adding
     * or removing cards from the hand without the hand knowing. Clients must use the addCard or
     * removeCard methods, which means this hand can check whether the hand is too full, etc.
     * 
     * DO NOT CHANGE THIS METHOD!
     * 
     * @return an unmodifiable set of cards in this hand.
     */
    public SortedSet<Card> cards() {
        return Collections.unmodifiableSortedSet(cards);
    }

    /**
     * Checks whether this hand contains the given suit.
     * 
     * @param suit the suit to check.
     * @return a card of the given suit or null if it doesn't contain any.
     */
    public Card has(Suit suit) {
        for (Card c : cards)
            if (c.suit() == suit)
                return c;

        return null;
    }

    /**
     * Checks whether this hand is full. A hand is full if it has 13 or more cards.
     * 
     * @return true if it contains 13 or more cards.
     */
    public boolean isFull() {
        return cards.size() >= 13;
    }

    /**
     * Remove a card from the hand.
     * 
     * @param card the card to remove.
     */
    public void removeCard(Card card) {
        if (!cards.remove(card))
            throw new IllegalArgumentException("Cannot remove card from hand; card not in hand!");
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String r = "";
        boolean firstTime = true;
        for (Card c : cards) {
            if (!firstTime)
                r += ", ";
            firstTime = false;
            r += c;
        }
        return r;
    }
}
