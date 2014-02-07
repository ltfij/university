package com.hjwylde.uni.swen221.lab03.textAdventure;

/*
 * Code for Laboratory 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class FakeDoor extends Door {

    public FakeDoor(String name, Room oneSide, Room otherSide) {
        super(name, oneSide, otherSide);
    }

    /*
     * @see lab3.textAdventure.Door#walkInto(lab3.textAdventure.Player, lab3.textAdventure.Room)
     */
    @Override
    public Room walkInto(Player player, Room room) {
        System.out.println("Ouch! You just walked into a fake door.");

        return room;
    }

}
