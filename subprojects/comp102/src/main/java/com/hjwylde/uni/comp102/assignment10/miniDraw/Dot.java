package com.hjwylde.uni.comp102.assignment10.miniDraw;

import java.awt.Color;
import java.awt.Point;
import java.util.Scanner;

import com.hjwylde.uni.comp102.util.DrawingCanvas;

/*
 * Code for Assignment 10, COMP 102
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Dot represents a small circle shape of a fixed size (5 pixels) Implements the
 * Shape interface.
 */
public class Dot implements Shape {
    
    // Dot co-ordinates.
    private Point p;
    
    // Dot properties.
    private final double r = 2.5;
    private final double rSquared = Math.pow(r, 2);
    private Color color;
    
    /**
     * Creates a new Dot class taking the parameters for it's position on a 2d
     * plane and color.
     * 
     * @param x
     *            the x co-ordinate.
     * @param y
     *            the y co-ordinate.
     * @param color
     *            the color.
     */
    public Dot(int x, int y, Color color) {
        p = new Point(x, y);
        
        assert color != null : "Colour of line should not be null.";
        this.color = color;
    }
    
    /**
     * Creates a new Dot class taking the parameters for it's position on a 2d
     * plane and color values.
     * 
     * @param x
     *            the x co-ordinate.
     * @param y
     *            the y co-ordinate.
     * @param r
     *            the red color value (0-255).
     * @param g
     *            the green color value (0-255).
     * @param b
     *            the blue color value (0-255).
     */
    public Dot(int x, int y, int r, int g, int b) {
        this(x, y, new Color(r, g, b));
    }
    
    /**
     * Creates a new Dot class taking a scanner holding values for the parameters
     * for it's position on a 2d plane and color values.
     * 
     * @param data
     *            the scanner.
     */
    public Dot(Scanner data) {
        this(data.nextInt(), data.nextInt(), new Color(data.nextInt(),
            data.nextInt(), data.nextInt()));
    }
    
    /**
     * Gets this Dot's color.
     * 
     * @return the color.
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Gets this Dot's x co-ordinate.
     * 
     * @return the x co-ordinate.
     */
    public int getX() {
        return p.x;
    }
    
    /**
     * Gets this Dot's y co-ordinate.
     * 
     * @return the y co-ordinate.
     */
    public int getY() {
        return p.y;
    }
    
    /**
     * Changes the position of the shape by dx and dy. If it was positioned at (x,
     * y), it will now be at (x + dx, y + dy).
     * 
     * @param dx
     *            value to translate the x co-ordinate by.
     * @param dy
     *            value to translate the y co-ordinate by.
     */
    @Override
    public void moveBy(int dx, int dy) {
        p.translate(dx, dy);
    }
    
    /**
     * Returns true if the point (x, y) is on top of the shape.
     * 
     * @param x
     *            the x co-ordinate.
     * @param y
     *            the y co-ordinate.
     * @return whether the point is on this shape.
     */
    @Override
    public boolean pointOnShape(int x, int y) {
        double result = Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2);
        
        return result <= rSquared;
    }
    
    /**
     * Renders the shape on a canvas. It uses the drawing methods with the extra
     * last argument of "false" so that the shape will not actually appear until
     * the canvas is redisplayed later. This gives much smoother redrawing.
     * 
     * @param canvas
     *            the DrawingCanvas to draw to.
     */
    @Override
    public void render(DrawingCanvas canvas) {
        Color canvasColor = canvas.getForeground();
        canvas.setForeground(color);
        
        canvas.fillOval(p.x, p.y, (int) r * 2, (int) r * 2, false);
        canvas.setForeground(canvasColor);
    }
    
    /**
     * Required by interface. A Dot must have a fixed width and height of 5
     * pixels.
     */
    @Override
    public void resize(int changeWd, int changeHt) {}
    
    /**
     * Sets this Dot's color to <code>color</code>.
     * 
     * @param color
     *            the color.
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Sets this Dot's position to <code>p</code>.
     * 
     * @param p
     *            the position to set to.
     */
    public void setPoint(Point p) {
        this.p = p;
    }
    
    /**
     * Sets this Dot's x co-ordinate to <code>x</code>.
     * 
     * @param x
     *            the x co-ordinate.
     */
    public void setX(int x) {
        p.x = x;
    }
    
    /**
     * Sets this Dot's y co-ordinate to <code>y</code>.
     * 
     * @param y
     *            the y co-ordinate.
     */
    public void setY(int y) {
        p.y = y;
    }
    
    /**
     * Returns a string description of the shape in a form suitable for writing to
     * a file in order to reconstruct the shape later
     * 
     * @return the string representing this Shape.
     */
    @Override
    public String toString() {
        return ("Dot " + p.x + " " + p.y + " " + color.getRed() + " "
            + color.getGreen() + " " + color.getBlue());
    }
}
