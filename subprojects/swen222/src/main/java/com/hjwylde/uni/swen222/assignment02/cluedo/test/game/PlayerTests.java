package com.hjwylde.uni.swen222.assignment02.cluedo.test.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.hjwylde.uni.swen222.assignment02.cluedo.game.Player;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.Card;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.CharacterCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.RoomCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.WeaponCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.intrigue.KeeperCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Character;

/**
 * Player tests. A rather bad suite of tests... Most tests are guaranteed by the defensive
 * programming techniques used. (No nulls, immutability, views.)
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 10/08/2013
 */
@SuppressWarnings("static-method")
public final class PlayerTests {
    
    /**
     * Tests crossing off all of the player's cards in their note book by adding every card to their
     * hand.
     */
    @Test
    public void cardCross() {
        Player player = new Player("foo", Character.DIANE_WHITE);
        
        for (Card card : CharacterCard.values()) {
            assertTrue(player.getNotebook().availableCards().contains(card));
            player.addCard(card);
            assertFalse(player.getNotebook().availableCards().contains(card));
        }
        for (Card card : WeaponCard.values()) {
            assertTrue(player.getNotebook().availableCards().contains(card));
            player.addCard(card);
            assertFalse(player.getNotebook().availableCards().contains(card));
        }
        for (Card card : RoomCard.values()) {
            assertTrue(player.getNotebook().availableCards().contains(card));
            player.addCard(card);
            assertFalse(player.getNotebook().availableCards().contains(card));
        }
    }
    
    /**
     * Tests adding and removing multiple of the same class keeper cards.
     */
    @Test
    public void multipleKeeperCard() {
        Player player = new Player("foo", Character.DIANE_WHITE);
        
        player.addKeeperCard(KeeperCard.values()[0]); // DiceAdd
        player.addKeeperCard(KeeperCard.values()[1]); // DiceAdd
        
        assertEquals(player.getKeeperCards().size(), 2);
        
        player.removeKeeperCard(KeeperCard.DiceAdd.class);
        
        assertEquals(player.getKeeperCards().size(), 1);
        
        player.removeKeeperCard(KeeperCard.DiceAdd.class);
        
        assertEquals(player.getKeeperCards().size(), 0);
    }
}