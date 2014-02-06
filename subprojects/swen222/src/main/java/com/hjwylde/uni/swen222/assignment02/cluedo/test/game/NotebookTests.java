package com.hjwylde.uni.swen222.assignment02.cluedo.test.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.hjwylde.uni.swen222.assignment02.cluedo.game.Notebook;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.CharacterCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.RoomCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.WeaponCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Character;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Room;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Weapon;

/**
 * Notebook tests. A rather bad suite of tests... Most tests are guaranteed by the defensive
 * programming techniques used. (No nulls, immutability, views.)
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 10/08/2013
 */
@SuppressWarnings("static-method")
public final class NotebookTests {
    
    /**
     * Tests crossing off every card that is expected to be in the notebook.
     */
    @Test
    public void noCards() {
        Notebook nb = new Notebook();
        
        for (Character character : Character.values())
            nb.cross(new CharacterCard(character));
        for (Weapon weapon : Weapon.values())
            nb.cross(new WeaponCard(weapon));
        for (Room room : Room.values())
            nb.cross(new RoomCard(room));
        
        assertTrue(nb.availableCards().isEmpty());
    }
    
    /**
     * Tests that the notebook does not contain the pool room card.
     */
    @Test
    public void noPoolRoomCard() {
        Notebook nb = new Notebook();
        assertFalse(nb.availableCards().contains(new RoomCard(Room.POOL)));
    }
}
