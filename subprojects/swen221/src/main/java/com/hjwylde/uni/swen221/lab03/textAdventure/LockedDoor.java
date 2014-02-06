package com.hjwylde.uni.swen221.lab03.textAdventure;

/*
 * Code for Laboratory 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class LockedDoor extends Door {
    
    private Key key;
    
    public LockedDoor(String name, Room oneSide, Room otherSide) {
        this(name, oneSide, otherSide, null);
    }
    
    public LockedDoor(String name, Room oneSide, Room otherSide, Key key) {
        super(name, oneSide, otherSide);
        
        this.key = key;
    }
    
    /*
     * @see lab3.textAdventure.Door#longDescription()
     */
    @Override
    public String longDescription() {
        return "It's a door, but it looks like it's locked.  Do you have the key?";
    }
    
    /*
     * @see lab3.textAdventure.Door#walkInto(lab3.textAdventure.Player, lab3.textAdventure.Room)
     */
    @Override
    public Room walkInto(Player player, Room room) {
        for (Item i : player.getItems())
            if (i instanceof Key)
                if (key.equals(i))
                    return super.walkInto(player, room);
        
        System.out
            .println("It's locked shut.  Looks like you don't have the key.");
        
        return room;
    }
    
}
