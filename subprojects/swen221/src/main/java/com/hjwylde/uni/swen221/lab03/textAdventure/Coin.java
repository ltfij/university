package com.hjwylde.uni.swen221.lab03.textAdventure;

/*
 * Code for Laboratory 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * This class represents a collection of coins in the game.
 * 
 * @author djp
 * 
 */
public class Coin implements Item {

    private int amount;

    public Coin(int amount) {
        this.amount = amount;
    }

    @Override
    public String longDescription() {
        return amount + " Gold coins.";
    }

    @Override
    public void pickUp(Player p, Room r) {
        System.out.println("You picked up " + amount + " gold coins.  You're rich!");
        r.getItems().remove(this);
        p.getItems().add(this);
    }

    @Override
    public void prod(Player player, Room room) {
        System.out.println("You prodded the gold pieces.  You guessed it.  Nothing happened!");
    }

    @Override
    public String shortDescription() {
        return "Gold coins.";
    }

    @Override
    public Room walkInto(Player player, Room room) {
        System.out.println("Don't be silly ... you can't walk into gold coins!");
        return room;
    }
}
