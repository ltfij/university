package com.hjwylde.uni.swen222.assignment02.cluedo.game.items;

import java.util.Locale;

import com.hjwylde.uni.swen222.assignment02.cluedo.util.Util;

/**
 * A character enumeration of all the characters in the game.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 2/08/2013
 */
public enum Character {

    /**
     * Kasandra Scarlett.
     */
    KASANDRA_SCARLETT,
    /**
     * Jack Mustard.
     */
    JACK_MUSTARD,
    /**
     * Diane White.
     */
    DIANE_WHITE,
    /**
     * Jacob Green.
     */
    JACOB_GREEN,
    /**
     * Eleanor Peacock.
     */
    ELEANOR_PEACOCK,
    /**
     * Victor Plum.
     */
    VICTOR_PLUM;

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
}
