package com.hjwylde.uni.swen222.assignment02.cluedo.util;

import java.util.Objects;

/**
 * An immutable Cartesian co-ordinate.
 * 
 * @author Henry J. Wylde
 * 
 * @since 10/08/2013
 */
public final class Coordinate {

    private final int x;
    private final int y;

    /**
     * Creates a new <code>Coordinate</code> with the given x and y values.
     * 
     * @param x the x value.
     * @param y the y value.
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordinate))
            return false;

        Coordinate coord = (Coordinate) obj;

        return (x == coord.x) && (y == coord.y);
    }

    /**
     * Gets the x co-ordinate.
     * 
     * @return the x co-ordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y co-ordinate.
     * 
     * @return the y co-ordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
