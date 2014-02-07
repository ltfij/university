package com.hjwylde.uni.swen221.assignment02.walker;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import maze.Direction;

/*
 * Code for Assignment 2, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * This class represents a ball of string - a line of points that only (and always) consecutively
 * differ in value by one co-ordinate and by only 1. The ball of string has a direction which can be
 * returned when there exists at least 2 points, and it can find the direction between two different
 * points. It is used for remembering history of a path that is walked.
 * 
 * The co-ordinate system of the points used is as follows:
 * <ul>
 * <li>Direction.NORTH: +y</li>
 * <li>Direction.SOUTH: -y</li>
 * <li>Direction.EAST: +x</li>
 * <li>Direction.WEST: -x</li>
 * </ul>
 * 
 * @author Henry J. Wylde
 */
public class BallOfString {

    private List<Point> ball;

    /**
     * Creates a new empty ball of string.
     */
    public BallOfString() {
        ball = new ArrayList<>();
    }

    /**
     * Gets the direction of this ball of string. The direction is based on the last two points that
     * the string was lined to. If there is less than 2 points in the ball, then null is returned.
     * 
     * @return the direction of the ball of string, or null if less than 2 points.
     */
    public Direction getDirection() {
        if (length() <= 1)
            return null;

        return BallOfString.getDirection(getEnd(1), getEnd());
    }

    /**
     * Gets the end of the ball of string. Equivalent to calling <code>getEnd(0)</code>. Returns
     * null if this ball of string has no points.
     * 
     * @return the end of the ball of string or null if empty.
     */
    public Point getEnd() {
        return getEnd(0);
    }

    /**
     * Gets the end of the ball of string with the specified offset. Offset 0 is the same as getting
     * the end of the ball, while offset 1 will get the point directly previous to the end of the
     * ball, and so on. Returns null if the ball is empty or throws an exception if the offset is
     * out of bounds.
     * 
     * @param offset the offset from the end of the point.
     * @return the point or null if the ball is empty.
     */
    public Point getEnd(int offset) {
        if (length() == 0)
            return null;

        return ball.get(length() - (offset + 1));
    }

    /**
     * Checks whether this ball of string has passed the specified point or not. This method doesn't
     * care about what direction the point might have come from.
     * 
     * @param p the point to check.
     * @return true if this string contains the specified point.
     */
    public boolean hasPassedPoint(Point p) {
        return (numberOfPasses(p) > 0);
    }

    /**
     * Checks whether this ball of string has passed the <code>to</code> point after having come
     * from the <code>from</code> point. Essentially checks whether the ball has passed the point in
     * the same direction in the past or not.
     * 
     * @param from the from point.
     * @param to the to point.
     * @return true if the ball has passed the <code>to</code> point from the <code>from</code>
     *         point.
     */
    public boolean hasPassedPoint(Point from, Point to) {
        return (numberOfPasses(from, to) > 0);
    }

    /**
     * Returns the length of this ball of string.
     * 
     * @return the length of the ball.
     */
    public int length() {
        return ball.size();
    }

    /**
     * Lines this ball of string in the specified direction. This basically creates a new point that
     * is advanced by 1 in the direction specified and adds it to the ball of string.
     * 
     * @param dir the direction to line in.
     */
    public void lineIn(Direction dir) {
        ball.add(getNextPoint(dir));
    }

    /**
     * Lines this ball of string to the specified point (adds it to the end). Assumes that the point
     * agrees with the representation of the ball of string in that it only alters one co-ordinate
     * by 1. Does not allow null values.
     * 
     * @param p the point to line to.
     */
    public void lineTo(Point p) {
        if (p == null)
            throw new NullPointerException();

        ball.add(p);
    }

    /**
     * Calculates the number of times this ball of string has passed the specified point (without
     * caring about what direction it came from). Includes the initial start point (getEnd(length()
     * - 1)).
     * 
     * @param p the point to check.
     * @return the number of times this ball of string has passed the point.
     */
    public int numberOfPasses(Point p) {
        // Shorthand code / doesn't conform to the style guides where we shouldn't use assign
        // operations
        // inside while loops.
        /*
         * int index = length(); int count = 0; while ((index = ball.subList(0,
         * index).lastIndexOf(p)) >= 0) { count++; }
         */

        int index = ball.lastIndexOf(p);

        int count = 0;
        while (index >= 0) {
            count++;

            index = ball.subList(0, index).lastIndexOf(p);
        }

        return count;
    }

    /**
     * Counts the number of times this ball of string has passed the <code>to</code> point when it
     * has come from the <code>from</code> point. Essentially this means it counts the number of
     * times it has passed the point with the same direction as <code>getDirection(from, to)</code>.
     * 
     * @param from the from point.
     * @param to the to point.
     * @return the number of times this ball of string has passed the <code>to</code> point from the
     *         specified <code>from</code> point.
     */
    public int numberOfPasses(Point from, Point to) {
        // Shorthand code / doesn't conform to the style guides where we shouldn't use assign
        // operations
        // inside while loops.
        /*
         * int index = length(); int count = 0; while ((index = ball.subList(0,
         * index).lastIndexOf(to)) > 0) if (ball.get(index - 1).equals(from)) count++;
         */

        int index = ball.lastIndexOf(to);

        int count = 0;
        while (index > 0) {
            if (ball.get(index - 1).equals(from))
                count++;

            index = ball.subList(0, index).lastIndexOf(to);
        }

        return count;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");

        Iterator<Point> it = ball.iterator();
        if (it.hasNext())
            sb.append(BallOfString.pointToString(it.next()));

        while (it.hasNext())
            sb.append(", " + BallOfString.pointToString(it.next()));

        return (sb.append(")").toString());
    }

    /**
     * Returns the next point from the last point on this ball of string in the specified direction.
     * 
     * @param dir the direction of the next point.
     * @return the next point.
     */
    private Point getNextPoint(Direction dir) {
        return BallOfString.getNextPoint(getEnd(), dir);
    }

    /**
     * Gets the direction from the <code>from</code> point to the <code>to</code> point. Assumes
     * that the two points only differ by either the <i>x</i> or <i>y</i> co-ordinate and that they
     * only differ by 1.
     * 
     * @param from the from point.
     * @param to the to point.
     * @return the direction from <code>from</code> to <code>to</code>.
     */
    protected static Direction getDirection(Point from, Point to) {
        if (from.x == to.x) {
            if (from.y < to.y)
                return Direction.NORTH;

            return Direction.SOUTH;
        } else if (from.x < to.x)
            return Direction.EAST;

        return Direction.WEST;
    }

    /**
     * Gets the next point that comes from the specified point in the given direction.
     * 
     * @param p the point to come from.
     * @param dir the direction to move in.
     * @return the new point.
     */
    protected static Point getNextPoint(Point p, Direction dir) {
        Point nextP = new Point(p);

        switch (dir) {
            case NORTH:
                nextP.y++;
                break;
            case EAST:
                nextP.x++;
                break;
            case SOUTH:
                nextP.y--;
                break;
            default: // WEST
                nextP.x--;
        }

        return nextP;
    }

    /**
     * Helper method for printing out a <code>Point</code> in a nicer fashion. The string is of the
     * form: <i>(x, y)</i>.
     * 
     * @param p a point
     * @return a string representation of that point.
     */
    private static String pointToString(Point p) {
        return "(" + p.x + ", " + p.y + ")";
    }
}
