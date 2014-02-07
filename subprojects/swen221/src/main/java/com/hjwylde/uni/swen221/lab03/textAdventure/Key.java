package com.hjwylde.uni.swen221.lab03.textAdventure;

/*
 * Code for Laboratory 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class Key implements Item {

    public Key() {}

    @Override
    public String longDescription() {
        return "A key with no markings.";
    }

    @Override
    public void pickUp(Player player, Room room) {
        room.getItems().remove(this);
        player.getItems().add(this);
    }

    @Override
    public void prod(Player player, Room room) {
        System.out.println("You prodded the key.  Nothing happened, yet again!");
    }

    @Override
    public String shortDescription() {
        return "A key.";
    }

    @Override
    public Room walkInto(Player player, Room room) {
        System.out.println("Don't be silly ... you can't walk into a key!");

        return room;
    }

}
