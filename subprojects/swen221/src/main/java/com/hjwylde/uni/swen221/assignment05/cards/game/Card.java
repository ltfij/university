package com.hjwylde.uni.swen221.assignment05.cards.game;

import java.util.Comparator;

/*
 * Code for Assignment 5, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Represents a traditional card that has a rank and a suit.
 * 
 * @author Henry J. Wylde
 */
public class Card implements Comparable<Card> {
    
    /**
     * Orders cards by:
     * Suit: C < D < S < H
     * Rank: A < 2 < 3 < 4 < 5 < 6 < 7 < 8 < 9 < 10 < J < Q < K
     */
    public static final Comparator<Card> COMP_ACE_SMALLEST = new Comparator<Card>() {
        
        /**
         * Orders cards by:
         * Suit: C < D < S < H
         * Rank: A < 2 < 3 < 4 < 5 < 6 < 7 < 8 < 9 < 10 < J < Q < K
         * 
         * @param first the first card.
         * @param second the second card.
         * @return -1 if first is before second, 0 if they are the same, 1 if second comes before
         *         first.
         */
        @Override
        public int compare(Card first, Card second) {
            int res = first.suit.compareTo(second.suit);
            
            if (res != 0)
                return res;
            
            if ((first.rank == Rank.ACE) && (second.rank == Rank.ACE))
                return 0;
            else if (first.rank == Rank.ACE)
                return -1;
            else if (second.rank == Rank.ACE)
                return 1;
            
            return first.rank.compareTo(second.rank);
        }
        
    };
    
    /**
     * Orders cards by:
     * Natural Ordering for: 2, 3, 4, 5, 6, 7, 8, 9, 10
     * Rank: J < Q < K < A
     * Suit: C < D < S < H
     */
    public static final Comparator<Card> COMP_PICTURE_HIGHEST = new Comparator<Card>() {
        
        /**
         * Orders cards by:
         * Natural Ordering for: 2, 3, 4, 5, 6, 7, 8, 9, 10
         * Rank: J < Q < K < A
         * Suit: C < D < S < H
         * 
         * @param first the first card.
         * @param second the second card.
         * @return -1 if first is before second, 0 if they are the same, 1 if second comes before
         *         first.
         */
        @Override
        public int compare(Card first, Card second) {
            if (!Rank.isPicture(first.rank) && !Rank.isPicture(second.rank))
                return Card.COMP_SUIT_RANK.compare(first, second);
            
            return Card.COMP_RANK_SUIT.compare(first, second);
        }
        
    };
    
    /**
     * Orders cards by:
     * Rank: 2 < 3 < 4 < 5 < 6 < 7 < 8 < 9 < 10 < J < Q < K < A
     * Suit: C < D < S < H
     */
    public static final Comparator<Card> COMP_RANK_SUIT = new Comparator<Card>() {
        
        /**
         * Orders cards by:
         * Rank: 2 < 3 < 4 < 5 < 6 < 7 < 8 < 9 < 10 < J < Q < K < A
         * Suit: C < D < S < H
         * 
         * @param first the first card.
         * @param second the second card.
         * @return -1 if first is before second, 0 if they are the same, 1 if second comes before
         *         first.
         */
        @Override
        public int compare(Card first, Card second) {
            int res = first.rank.compareTo(second.rank);
            
            return (res == 0 ? first.suit.compareTo(second.suit) : res);
        }
        
    };
    
    /**
     * Orders cards by:
     * Suit: C < D < S < H
     * Rank: 2 < 3 < 4 < 5 < 6 < 7 < 8 < 9 < 10 < J < Q < K < A
     */
    public static final Comparator<Card> COMP_SUIT_RANK = new Comparator<Card>() {
        
        /**
         * Orders cards by:
         * Suit: C < D < S < H
         * Rank: 2 < 3 < 4 < 5 < 6 < 7 < 8 < 9 < 10 < J < Q < K < A
         * 
         * @param first the first card.
         * @param second the second card.
         * @return -1 if first is before second, 0 if they are the same, 1 if second comes before
         *         first.
         */
        @Override
        public int compare(Card first, Card second) {
            int res = first.suit.compareTo(second.suit);
            
            return (res == 0 ? first.rank.compareTo(second.rank) : res);
        }
        
    };
    
    private final Rank rank;
    private final Suit suit;
    
    /**
     * Creates a new card with the given suit and rank.
     * 
     * @param suit the suit.
     * @param rank the rank.
     */
    public Card(Suit suit, Rank rank) {
        if ((suit == null) || (rank == null))
            throw new IllegalArgumentException();
        
        this.suit = suit;
        this.rank = rank;
    }
    
    /*
     * Orders cards by:
     * Suit: C < D < S < H
     * Rank: 2 < 3 < 4 < 5 < 6 < 7 < 8 < 9 < 10 < J < Q < K < A
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Card other) {
        return Card.COMP_SUIT_RANK.compare(this, other);
    }
    
    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        
        if ((obj == null) || !(obj instanceof Card))
            return false;
        
        Card other = (Card) obj;
        if (rank != other.rank)
            return false;
        if (suit != other.suit)
            return false;
        
        return true;
    }
    
    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        
        int result = 1;
        result = (prime * result) + rank.hashCode();
        result = (prime * result) + suit.hashCode();
        
        return result;
    }
    
    /**
     * Gets this cards rank.
     * 
     * @return the rank.
     */
    public Rank rank() {
        return rank;
    }
    
    /**
     * Gets this cards suit.
     * 
     * @return the suit.
     */
    public Suit suit() {
        return suit;
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "" + rank + suit;
    }
    
}