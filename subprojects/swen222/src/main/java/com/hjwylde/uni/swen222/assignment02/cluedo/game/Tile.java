package com.hjwylde.uni.swen222.assignment02.cluedo.game;

import com.google.common.base.Optional;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Room;

/**
 * An enumeration of the possible tiles for the game board.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 5/08/2013
 */
public enum Tile {
    
    /**
     * An intrigue card tile. This tile is for the player to step on and pick up an intrigue card.
     */
    INTRIGUE_CARD('?'),
    /**
     * A doorway tile. The doorway is the door into the room. A player must be standing on a doorway
     * tile before they may enter a room.
     */
    DOORWAY('d'),
    /**
     * An empty tile.
     */
    EMPTY(' '),
    /**
     * An impassable tile.
     */
    IMPASSABLE('*'),
    /**
     * A kitchen room tile.
     */
    ROOM_KITCHEN('7'),
    /**
     * A dining room tile.
     */
    ROOM_DINING_ROOM('8'),
    /**
     * A guest house room tile.
     */
    ROOM_GUEST_HOUSE('9'),
    /**
     * A patio room tile.
     */
    ROOM_PATIO('4'),
    /**
     * A pool room tile.
     */
    ROOM_POOL('5'),
    /**
     * A hall room tile.
     */
    ROOM_HALL('6'),
    /**
     * An observatory room tile.
     */
    ROOM_OBSERVATORY('3'),
    /**
     * A living room tile.
     */
    ROOM_LIVING_ROOM('2'),
    /**
     * A theatre room tile.
     */
    ROOM_THEATRE('1'),
    /**
     * A spa room tile.
     */
    ROOM_SPA('0'),
    /**
     * A character start position for Kasandra Scarlett.
     */
    CHARACTER_KASANDRA_SCARLETT('S'),
    /**
     * A character start position for Jacob Green.
     */
    CHARACTER_JACOB_GREEN('G'),
    /**
     * A character start position for Jack Mustard.
     */
    CHARACTER_JACK_MUSTARD('M'),
    /**
     * A character start position for Victor Plum.
     */
    CHARACTER_VICTOR_PLUM('R'),
    /**
     * A character start position for Diane White.
     */
    CHARACTER_DIANE_WHITE('W'),
    /**
     * A character start position for Eleanor Peacock.
     */
    CHARACTER_ELEANOR_PEACOCK('P');
    
    /**
     * The character that represents this tile on a textual board.
     */
    private char tile;
    
    /**
     * Creates a new <code>Tile</code> with the given tile character.
     * 
     * @param tile the tile character.
     */
    private Tile(char tile) {
        this.tile = tile;
    }
    
    /**
     * Attempts to get the room this tile represents if it is one, or <code>Optional.absent()</code>
     * if it is not.
     * 
     * @return an optional of the room this tile is or absent if it is not a room.
     */
    public Optional<Room> getRoom() {
        switch (this) {
        case ROOM_DINING_ROOM:
            return Optional.of(Room.DINING_ROOM);
        case ROOM_GUEST_HOUSE:
            return Optional.of(Room.GUEST_HOUSE);
        case ROOM_HALL:
            return Optional.of(Room.HALL);
        case ROOM_KITCHEN:
            return Optional.of(Room.KITCHEN);
        case ROOM_LIVING_ROOM:
            return Optional.of(Room.LIVING_ROOM);
        case ROOM_OBSERVATORY:
            return Optional.of(Room.OBSERVATORY);
        case ROOM_PATIO:
            return Optional.of(Room.PATIO);
        case ROOM_POOL:
            return Optional.of(Room.POOL);
        case ROOM_SPA:
            return Optional.of(Room.SPA);
        case ROOM_THEATRE:
            return Optional.of(Room.THEATRE);
        default:
            return Optional.absent();
        }
    }
    
    /**
     * Gets the tile character for a textual board.
     * 
     * @return the tile character.
     */
    public char getTile() {
        return tile;
    }
    
    /**
     * Checks whether this tile is a room tile or not.
     * 
     * @return true if this tile is a room tile.
     */
    public boolean isRoom() {
        switch (this) {
        case ROOM_DINING_ROOM:
        case ROOM_GUEST_HOUSE:
        case ROOM_HALL:
        case ROOM_KITCHEN:
        case ROOM_LIVING_ROOM:
        case ROOM_OBSERVATORY:
        case ROOM_PATIO:
        case ROOM_POOL:
        case ROOM_SPA:
        case ROOM_THEATRE:
            return true;
        default:
            return false;
        }
    }
}