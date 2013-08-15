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
 * This class records various pieces of information about the game player,
 * including: their name, how strong they are, and smart they are, and what
 * they're carrying.
 * 
 * @author djp
 * 
 */
public class Player {
    
    /**
     * Name of this player
     */
    private String name;
    
    /**
     * How strong is this player (from 1 to 10)
     */
    private int strength;
    
    /**
     * How smart is this player (from 1 to 10)
     */
    private int smarts;
    
    /**
     * Items the player is carrying
     */
    private ArrayList<Item> items;
    
    public Player(String name, int strength, int smarts) {
        this.name = name;
        this.strength = strength;
        this.smarts = smarts;
        items = new ArrayList<>();
    }
    
    /**
     * Get items the player is carrying
     */
    public List<Item> getItems() {
        return items;
    }
    
    /**
     * Get name of this player
     */
    public String getName() {
        return name;
    }
    
    /**
     * get player smarts (from 1 to 10)
     */
    public int getSmarts() {
        return smarts;
    }
    
    /**
     * How player strength (from 1 to 10)
     */
    public int getStrength() {
        return strength;
    }
    
}
