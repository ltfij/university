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
 * Oval represents an oval shape Implements the Shape interface.
 */
public class Oval implements Shape {
    
    /*
     * Ellipse Equation: x^2/a^2 + y^2/b^2 = 1
     */
    
    // Oval co-ordinates.
    private Point p;
    // a = width / 2.
    private int a;
    private double aSquared;
    // b = height / 2.
    private int b;
    private double bSquared;
    
    // Oval properties.
    private Color color;
    
    /**
     * Creates a new Oval class taking the parameters for the top left corner,
     * width and height on a 2d plane and color.
     * 
     * @param x
     *            the x co-ordinate.
     * @param y
     *            the y co-ordinate.
     * @param width
     *            the width.
     * @param height
     *            the height.
     * @param color
     *            the color.
     */
    public Oval(int x, int y, int width, int height, Color color) {
        a = width / 2;
        aSquared = Math.pow(a, 2);
        b = height / 2;
        bSquared = Math.pow(b, 2);
        p = new Point(x + a, y + b);
        
        this.color = color;
    }
    
    /**
     * Creates a new Oval class taking the parameters for the top left corner,
     * width and height on a 2d plane and color values.
     * 
     * @param x
     *            the x co-ordinate.
     * @param y
     *            the y co-ordinate.
     * @param width
     *            the width.
     * @param height
     *            the height.
     * @param r
     *            the red color value (0-255).
     * @param g
     *            the green color value (0-255).
     * @param b
     *            the blue color value (0-255).
     */
    public Oval(int x, int y, int width, int height, int r, int g, int b) {
        this(x, y, width, height, new Color(r, g, b));
    }
    
    /**
     * Creates a new Oval class taking a scanner holding values for the parameters
     * for it's the top left corner, width and height on a 2d plane and color
     * values.
     * 
     * @param data
     *            the scanner.
     */
    public Oval(Scanner data) {
        this(data.nextInt(), data.nextInt(), data.nextInt(), data.nextInt(),
            new Color(data.nextInt(), data.nextInt(), data.nextInt()));
    }
    
    /**
     * Returns the color of this Oval.
     * 
     * @return the color.
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Returns the height of this Oval.
     * 
     * @return the height.
     */
    public int getHeight() {
        return b * 2;
    }
    
    /**
     * Returns the point of the top left corner of the Oval.
     * 
     * @return the point.
     */
    public Point getPoint() {
        return new Point(p.x - a, p.y - b);
    }
    
    /**
     * Returns the width of this Oval.
     * 
     * @return the width.
     */
    public int getWidth() {
        return a * 2;
    }
    
    /**
     * Returns the x co-ordinate of the top left corner of the Oval.
     * 
     * @return the x co-ordinate.
     */
    public int getX() {
        return p.x - a;
    }
    
    /**
     * Returns the y co-ordinate of the top left corner of the Oval.
     * 
     * @return the y co-ordinate.
     */
    public int getY() {
        return p.y - b;
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
        // If (x-x1)^2 / a^2 + (y-y1)^2 / b^2 <= 1, then the point is
        // within the ellipse.
        double result = (Math.pow(x - p.x, 2) / aSquared)
            + (Math.pow(y - p.y, 2) / bSquared);
        
        return result <= 1;
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
        
        // Fill oval function draws the ellipse by the top left corner with a width
        // and height argument.
        canvas.fillOval(p.x - a, p.y - b, a * 2, b * 2, false);
        canvas.setForeground(canvasColor);
    }
    
    /**
     * Changes the width and height of the shape by the specified amounts. The
     * amounts may be negative, which means that the shape should get smaller, at
     * least in that direction. The shape should never become smaller than 1 pixel
     * in width or height The center of the shape should remain the same.
     * 
     * @param changeWidth
     *            the width to be changed.
     * @param changeHeight
     *            the height to be changed.
     */
    @Override
    public void resize(int changeWidth, int changeHeight) {
        a += changeWidth / 2;
        aSquared = Math.pow(a, 2);
        b += changeHeight / 2;
        bSquared = Math.pow(b, 2);
    }
    
    /**
     * Sets this Oval's color to <code>color</code>
     * 
     * @param color
     *            the color.
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Sets this Oval's height to <code>height</code>.
     * 
     * @param height
     *            the height.
     */
    public void setHeight(int height) {
        b = height / 2;
        bSquared = Math.pow(b, 2);
    }
    
    /**
     * Sets this Oval's top left corner to <code>p</code>.
     * 
     * @param p
     *            the point to set to.
     */
    public void setPoint(Point p) {
        this.p = new Point(p.x + a, p.y + b);
    }
    
    /**
     * Sets this Oval's width to <code>width</code>.
     * 
     * @param width
     *            the width.
     */
    public void setWidth(int width) {
        a = width / 2;
        aSquared = Math.pow(a, 2);
    }
    
    /**
     * Sets this Oval's top left corner x co-ordinate to <code>x</code>.
     * 
     * @param x
     *            the x co-ordinate.
     */
    public void setX(int x) {
        p.x = x + a;
    }
    
    /**
     * Sets this Oval's top left corner y co-ordinate to <code>y</code>.
     * 
     * @param y
     *            the y co-ordinate.
     */
    public void setY(int y) {
        p.y = y + b;
    }
    
    /**
     * Returns a string description of the shape in a form suitable for writing to
     * a file in order to reconstruct the shape later
     * 
     * @return the string representing this Shape.
     */
    @Override
    public String toString() {
        return ("Oval " + (p.x - a) + " " + (p.y - b) + " " + (a * 2) + " "
            + " " + (b * 2) + " " + color.getRed() + " " + color.getGreen()
            + " " + color.getBlue());
    }
}