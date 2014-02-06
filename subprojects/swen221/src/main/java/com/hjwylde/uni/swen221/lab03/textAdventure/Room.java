package com.hjwylde.uni.swen221.lab03.textAdventure;

import java.util.ArrayList;
import java.util.List;

/*
 * Code for Laboratory 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * This class represents a room in the adventure game.
 * 
 * @author djp
 * 
 */
public class Room {
    
    private String name;
    private String description;
    private ArrayList<Item> items;
    
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        items = new ArrayList<>();
    }
    
    /**
     * Get access to the items in this room
     * 
     * @return the items.
     */
    public List<Item> getItems() {
        return items;
    }
    
    /**
     * Get the description of this room
     */
    String getDescription() {
        return description;
    }
    
    /**
     * Get the name of this room
     */
    String getName() {
        return name;
    }
}
