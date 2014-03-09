package com.hjwylde.uni.swen221.lab03.textAdventure;

import java.util.List;

/*
 * Code for Laboratory 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class SpecialBookcase extends Furniture {

    private List<Book> books;

    public SpecialBookcase(String shortDescription, String longDescription, List<Book> books) {
        super(shortDescription, longDescription);

        this.books = books;
    }

    @Override
    public void prod(Player player, Room room) {
        if (!books.isEmpty()) {
            System.out.println("You find the bookcase full of books and decide to pick one up.");

            player.getItems().add(books.remove(0));
        } else
            System.out.println("The bookcase is empty...");
    }

}
