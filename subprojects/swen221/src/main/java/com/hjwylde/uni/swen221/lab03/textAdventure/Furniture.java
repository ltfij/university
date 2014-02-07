package com.hjwylde.uni.swen221.lab03.textAdventure;

/*
 * Code for Laboratory 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * This class represents an item of furniture in the game.
 * 
 * @author djp
 * 
 */
public class Furniture implements Item {

    private String shortDescription;
    private String longDescription;

    public Furniture(String shortDescription, String longDescription) {
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    @Override
    public String longDescription() {
        return longDescription;
    }

    @Override
    public void pickUp(Player p, Room r) {
        System.out.println("You can't pick up furniture you idiot!");
    }

    @Override
    public void prod(Player player, Room room) {
        System.out.println("Nothing happens (as usual)!");
    }

    @Override
    public String shortDescription() {
        return shortDescription;
    }

    @Override
    public Room walkInto(Player player, Room room) {
        System.out.println("Don't be silly ... you can't walk into furniture!");
        return room;
    }
}
