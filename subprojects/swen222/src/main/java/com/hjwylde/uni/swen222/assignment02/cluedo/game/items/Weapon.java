package com.hjwylde.uni.swen222.assignment02.cluedo.game.items;

import java.util.Locale;

import com.hjwylde.uni.swen222.assignment02.cluedo.util.Util;

/**
 * A weapon enumeration of all the weapons in the game.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 2/08/2013
 */
public enum Weapon {

    /**
     * The rope weapon.
     */
    ROPE,
    /**
     * The candlestick weapon.
     */
    CANDLESTICK,
    /**
     * The knife weapon.
     */
    KNIFE,
    /**
     * The pistol weapon.
     */
    PISTOL,
    /**
     * The baseball bat weapon.
     */
    BASEBALL_BAT,
    /**
     * The dumbbell weapon.
     */
    DUMBBELL,
    /**
     * The trophy weapon.
     */
    TROPHY,
    /**
     * The poison weapon.
     */
    POISON,
    /**
     * The axe weapon.
     */
    AXE;

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
