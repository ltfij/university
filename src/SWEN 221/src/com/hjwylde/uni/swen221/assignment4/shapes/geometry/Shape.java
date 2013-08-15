package com.hjwylde.uni.swen221.assignment4.shapes.geometry;

/*
 * Code for Assignment 4, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * This class captures the abstract notion of a Shape.
 * 
 * @author djp
 */
public interface Shape extends Cloneable {
    
    /**
     * Determine a <i>bounding box</i> of the current shape. A bounding box is a box that will fit
     * around the entire shape and, hence, can be used to determine the maximum width and height of
     * the shape. This is useful when it comes to drawing the shape!
     * 
     * @return returns the minimum possible bounding box for this shape.
     */
    public Rectangle boundingBox();
    
    /**
     * Clones a shape.
     * 
     * @return a clone of the Shape.
     */
    public Object clone();
    
    /**
     * Determine whether or not the given point is contained within this shape.
     * 
     * @param x the x co-ordinate.
     * @param y the y co-ordinate
     * @return true if the point (x, y) is contained within the shape.
     */
    public boolean contains(int x, int y);
}