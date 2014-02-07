package com.hjwylde.uni.comp102.assignment10.miniDraw;

import com.hjwylde.uni.comp102.util.DrawingCanvas;

/*
 * Code for Assignment 10, COMP 102 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * The interface for all Shape objects Provides four methods
 */
public interface Shape {

    /**
     * Changes the position of the shape by dx and dy. If it was positioned at (x, y), it will now
     * be at (x + dx, y + dy).
     * 
     * @param dx value to translate the x co-ordinate by.
     * @param dy value to translate the y co-ordinate by.
     */
    public void moveBy(int dx, int dy);

    /**
     * Returns true if the point (x, y) is on top of the shape.
     * 
     * @param x the x co-ordinate.
     * @param y the y co-ordinate.
     * @return whether the point is on this shape.
     */
    public boolean pointOnShape(int x, int y);

    /**
     * Renders the shape on a canvas. It uses the drawing methods with the extra last argument of
     * "false" so that the shape will not actually appear until the canvas is redisplayed later.
     * This gives much smoother redrawing.
     * 
     * @param canvas the DrawingCanvas to draw to.
     */
    public void render(DrawingCanvas canvas);

    /**
     * Changes the width and height of the shape by the specified amounts. The amounts may be
     * negative, which means that the shape should get smaller, at least in that direction. The
     * shape should never become smaller than 1 pixel in width or height The center of the shape
     * should remain the same.
     * 
     * @param changeWd the width to be changed.
     * @param changeHt the height to be changed.
     */
    public void resize(int changeWd, int changeHt);

    /**
     * Returns a string description of the shape in a form suitable for writing to a file in order
     * to reconstruct the shape later
     * 
     * @return the string representing this Shape.
     */
    @Override
    public String toString();

}
