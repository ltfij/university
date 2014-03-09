package com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Room;

/**
 * Represents a room card. A room card takes a single room from the enumeration of possible rooms.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 25/07/2013
 */
public final class RoomCard implements Card {

    private final Room room;

    /**
     * Creates a new <code>RoomCard</code> with the given room.
     * 
     * @param room the room.
     */
    public RoomCard(Room room) {
        this.room = Objects.requireNonNull(room);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RoomCard))
            return false;

        return room == ((RoomCard) obj).room;
    }

    /**
     * Gets the room of this card.
     * 
     * @return the room.
     */
    public Room getRoom() {
        return room;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return room.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return room.toString();
    }

    /**
     * Gets an array of all possible values for this class. The array is generated using the enum of
     * rooms.
     * 
     * @return an array of all possible room cards.
     */
    public static RoomCard[] values() {
        List<RoomCard> builder = new ArrayList<>();
        for (Room room : Room.murderRoomValues())
            builder.add(new RoomCard(room));

        return builder.toArray(new RoomCard[0]);
    }
}
