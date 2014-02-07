package com.hjwylde.uni.swen221.lab03.textAdventure;

/*
 * Code for Laboratory 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class MagicItem implements Item {

    private String shortDescription;
    private String longDescription;

    public MagicItem(String shortDescription, String longDescription) {
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    @Override
    public String longDescription() {
        return longDescription;
    }

    @Override
    public void pickUp(Player player, Room room) {
        if (player.getSmarts() > 7) {
            room.getItems().remove(this);
            player.getItems().add(this);
        } else
            System.out
                    .println("Why on earth would you want to pick this up?  It's useless to you!");
    }

    @Override
    public void prod(Player player, Room room) {
        if (player.getSmarts() > 7)
            System.out.println("The item hums when you prod it.");
        else
            System.out.println("You prod the magical item.  It doesn't do anything interesting...");
    }

    @Override
    public String shortDescription() {
        return shortDescription;
    }

    @Override
    public Room walkInto(Player player, Room room) {
        System.out.println("Don't be silly ... you can't walk into magic items!");
        return room;
    }

}
