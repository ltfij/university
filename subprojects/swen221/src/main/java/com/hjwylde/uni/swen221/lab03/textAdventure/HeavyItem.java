package com.hjwylde.uni.swen221.lab03.textAdventure;

/*
 * Code for Laboratory 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class HeavyItem implements Item {
    
    private String shortDescription;
    private String longDescription;
    
    public HeavyItem(String shortDescription, String longDescription) {
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }
    
    @Override
    public String longDescription() {
        return longDescription;
    }
    
    @Override
    public void pickUp(Player player, Room room) {
        if (player.getStrength() > 7) {
            room.getItems().remove(this);
            player.getItems().add(this);
        } else
            System.out
                .println("You strain to try and pick up the heavy item.  Looks like you're too weak though - it doesn't budge.");
    }
    
    @Override
    public void prod(Player player, Room room) {
        System.out
            .println("The item stays still as you prod it looking like an idiot.");
    }
    
    @Override
    public String shortDescription() {
        return shortDescription;
    }
    
    @Override
    public Room walkInto(Player player, Room room) {
        System.out
            .println("Don't be silly ... you can't walk into heavy items!");
        return room;
    }
    
}
