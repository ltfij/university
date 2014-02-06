package com.hjwylde.uni.swen222.assignment02.cluedo.game.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.hjwylde.uni.swen222.assignment02.cluedo.util.Util;

/**
 * A room enumeration of all the rooms in the game.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 2/08/2013
 */
public enum Room {
    
    /**
     * The kitchen room.
     */
    KITCHEN,
    /**
     * The dining room.
     */
    DINING_ROOM,
    /**
     * The guest house room.
     */
    GUEST_HOUSE,
    /**
     * The patio room.
     */
    PATIO,
    /**
     * The pool room.
     */
    POOL,
    /**
     * The hall room.
     */
    HALL,
    /**
     * The observatory room.
     */
    OBSERVATORY,
    /**
     * The living room.
     */
    LIVING_ROOM,
    /**
     * The theatre room.
     */
    THEATRE,
    /**
     * The spa room.
     */
    SPA;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String part : name().split("_")) {
            if (sb.length() != 0)
                sb.append(" ");
            
            sb.append(Util.toTitleCase(part, Locale.ENGLISH));
        }
        
        return sb.toString();
    }
    
    /**
     * Gets an array of all possible murder rooms. This list is all of the rooms except for the
     * pool.
     * 
     * @return an array of all possible murder rooms.
     */
    public static Room[] murderRoomValues() {
        List<Room> builder = new ArrayList<>();
        for (Room room : values())
            if (room != Room.POOL) // The pool is not a valid murder room card
                builder.add(room);
        
        return builder.toArray(new Room[0]);
    }
}