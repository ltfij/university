package com.hjwylde.uni.swen221.lab03.textAdventure;

/*
 * Code for Laboratory 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * This class represents an item stored in a room.
 * 
 * @author djp
 */
public interface Item {

    /**
     * Get a long description of the item.
     */
    public String longDescription();

    /**
     * Pick up this item from the room.
     */
    public void pickUp(Player player, Room room);

    /**
     * Prod this item (this might cause it to do something)
     */
    public void prod(Player player, Room room);

    /**
     * Get a short description of the item.
     */
    public String shortDescription();

    /**
     * Try to walk into this item (if it's a door, you'll go through it!). This method should return
     * the room the player is now in. If no room change has taken place, then it just returns the
     * room parameter given.
     */
    public Room walkInto(Player player, Room room);
}
