package com.hjwylde.uni.swen221.lab03.textAdventure;

/*
 * Code for Laboratory 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * This class represents a door in the game. It provides references to the rooms
 * which are on either side of the door.
 * 
 * @author djp
 * 
 */
public class Door implements Item {
    
    private String name;
    
    private Room oneSide;
    
    private Room otherSide;
    
    public Door(String name, Room oneSide, Room otherSide) {
        this.name = name;
        this.oneSide = oneSide;
        this.otherSide = otherSide;
    }
    
    @Override
    public String longDescription() {
        return "It's a door.  You could go through it.";
    }
    
    public String name() {
        return name;
    }
    
    public Room oneSide() {
        return oneSide;
    }
    
    public Room otherSide() {
        return oneSide;
    }
    
    @Override
    public void pickUp(Player p, Room r) {
        if (p.getStrength() > 7)
            System.out
                .println("Muscles ripple.  Wood creaks.  Nothing happens.  You might be strong, but you're not that strong!");
        else
            System.out.println("You can't pick up doors.  Doofus.");
    }
    
    @Override
    public void prod(Player player, Room room) {
        System.out
            .println("You prodded the door.  Nothing happened.  What did you expect?");
    }
    
    @Override
    public String shortDescription() {
        return name + ".  It connects " + oneSide.getName() + " to "
            + otherSide.getName();
    }
    
    @Override
    public Room walkInto(Player player, Room room) {
        if (room == oneSide) {
            System.out.println("You enter " + otherSide.getName());
            return otherSide;
        }
        
        System.out.println("You enter " + oneSide.getName());
        return oneSide;
    }
}
