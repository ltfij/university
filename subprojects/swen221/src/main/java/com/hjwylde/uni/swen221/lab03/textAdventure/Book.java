package com.hjwylde.uni.swen221.lab03.textAdventure;

/*
 * Code for Laboratory 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class Book implements Item {

    private String title;
    private String blurb;

    private String clue;

    public Book(String title, String blurb, String clue) {
        this.title = title;
        this.blurb = blurb;

        this.clue = clue;
    }

    @Override
    public String longDescription() {
        return shortDescription() + " " + blurb;
    }

    @Override
    public void pickUp(Player player, Room room) {
        room.getItems().remove(this);
        player.getItems().add(this);
    }

    @Override
    public void prod(Player player, Room room) {
        if (player.getSmarts() == 10)
            System.out.println("You find a clue: " + clue);
        else
            System.out.println("You open the book, but are too stupid to be able to read it.");
    }

    @Override
    public String shortDescription() {
        return "A book called " + title + ".";
    }

    @Override
    public Room walkInto(Player player, Room room) {
        System.out.println("Don't be silly ... you can't walk into a book!");

        return room;
    }

}
