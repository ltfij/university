package com.hjwylde.uni.swen221.assignment5.cards.tests;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.assignment5.cards.game.Card;
import com.hjwylde.uni.swen221.assignment5.cards.game.CardGame;

/*
 * Code for Assignment 5, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class CardTests {
    
    @Test
    public void testCardCompareTo() {
        List<Card> deck1 = CardGame.createDeck();
        List<Card> deck2 = CardGame.createDeck();
        for (int i = 1; i != 52; ++i) {
            Card c1 = deck1.get(i - 1);
            Card c2 = deck1.get(i);
            Card c3 = deck2.get(i);
            if (c1.compareTo(c2) >= 0)
                Assert.fail("Card " + c1 + " should be greater than " + c2);
            if (c2.compareTo(c1) <= 0)
                Assert.fail("Card " + c2 + " should be less than " + c1);
            if (c3.compareTo(c2) != 0)
                Assert.fail("Card " + c3 + " should equal " + c2);
        }
    }
    
    @Test
    public void testCardEquals() {
        List<Card> deck1 = CardGame.createDeck();
        List<Card> deck2 = CardGame.createDeck();
        for (int i = 0; i != 52; ++i) {
            Card c1 = deck1.get(i);
            Card c2 = deck2.get(i);
            if (!c1.equals(c2))
                Assert.fail("CARD: " + c1 + " should equal " + c2);
        }
    }
    
    @Test
    public void testCardNotEquals() {
        List<Card> deck1 = CardGame.createDeck();
        for (int i = 0; i != 52; ++i)
            for (int j = 0; j != 52; ++j)
                if (i != j) {
                    Card c1 = deck1.get(i);
                    Card c2 = deck1.get(j);
                    if (c1.equals(c2))
                        Assert.fail("CARD: " + c1 + " should not equal " + c2);
                }
    }
    
    /**
     * Tests whether each of the comparators for the players hands are reflexive.
     */
    @Test
    public void testComparators() {
        List<Card> deck1 = CardGame.createDeck();
        List<Card> deck2 = CardGame.createDeck();
        
        for (int i = 0; i < 52; i++)
            for (int j = 0; j < 52; j++) {
                Card c1 = deck1.get(i);
                Card c2 = deck2.get(j);
                
                if (Math.signum(Card.COMP_ACE_SMALLEST.compare(c1, c2)) != -Math
                    .signum(Card.COMP_ACE_SMALLEST.compare(c2, c1)))
                    Assert.fail("Ace Smallest: " + c1 + " compared to " + c2
                        + ". Results: "
                        + Math.signum(Card.COMP_ACE_SMALLEST.compare(c1, c2))
                        + " and "
                        + Math.signum(Card.COMP_ACE_SMALLEST.compare(c2, c1)));
                
                if (Math.signum(Card.COMP_RANK_SUIT.compare(c1, c2)) != -Math
                    .signum(Card.COMP_RANK_SUIT.compare(c2, c1)))
                    Assert.fail("Rank Suit: " + c1 + " compared to " + c2
                        + ". Results: "
                        + Math.signum(Card.COMP_RANK_SUIT.compare(c1, c2))
                        + " and "
                        + Math.signum(Card.COMP_RANK_SUIT.compare(c2, c1)));
                
                if (Math.signum(Card.COMP_SUIT_RANK.compare(c1, c2)) != -Math
                    .signum(Card.COMP_SUIT_RANK.compare(c2, c1)))
                    Assert.fail("Suit Rank: " + c1 + " compared to " + c2
                        + ". Results: "
                        + Math.signum(Card.COMP_SUIT_RANK.compare(c1, c2))
                        + " and "
                        + Math.signum(Card.COMP_SUIT_RANK.compare(c2, c1)));
                
                if (Math.signum(Card.COMP_PICTURE_HIGHEST.compare(c1, c2)) != -Math
                    .signum(Card.COMP_PICTURE_HIGHEST.compare(c2, c1)))
                    Assert
                        .fail("Picture Highest: "
                            + c1
                            + " compared to "
                            + c2
                            + ". Results: "
                            + Math.signum(Card.COMP_PICTURE_HIGHEST.compare(c1,
                                c2))
                            + " and "
                            + Math.signum(Card.COMP_PICTURE_HIGHEST.compare(c2,
                                c1)));
            }
    }
}
