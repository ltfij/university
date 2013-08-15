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
 * Rectangle represents a solid rectangle shape Implements the Shape interface.
 */
public class Rectangle implements Shape {
    
    // Rectangle co-ordinates.
    private Point p;
    private int width;
    private int height;
    
    // Rectangle properties.
    private Color color;
    
    /**
     * Creates a new Rectangle class taking the parameters for the top left
     * corner, width and height on a 2d plane and color.
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
    public Rectangle(int x, int y, int width, int height, Color color) {
        p = new Point(x, y);
        this.width = width;
        this.height = height;
        
        assert color != null : "Colour of line should not be null.";
        this.color = color;
    }
    
    /**
     * Creates a new Rectangle class taking the parameters for the top left
     * corner, width and height on a 2d plane and color values.
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
    public Rectangle(int x, int y, int width, int height, int r, int g, int b) {
        this(x, y, width, height, new Color(r, g, b));
    }
    
    /**
     * Creates a new Rectangle class taking a scanner holding values for the
     * parameters for it's the top left corner, width and height on a 2d plane and
     * color values.
     * 
     * @param data
     *            the scanner.
     */
    public Rectangle(Scanner data) {
        this(data.nextInt(), data.nextInt(), data.nextInt(), data.nextInt(),
            new Color(data.nextInt(), data.nextInt(), data.nextInt()));
    }
    
    /**
     * Returns the color of this Rectangle.
     * 
     * @return the color.
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Returns the height of this Rectangle.
     * 
     * @return the height.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Returns the point of the top left corner of the Rectangle.
     * 
     * @return the point.
     */
    public Point getPoint() {
        return p;
    }
    
    /**
     * Returns the width of this Rectangle.
     * 
     * @return the width.
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Returns the x co-ordinate of the top left corner of the Rectangle.
     * 
     * @return the x co-ordinate.
     */
    public int getX() {
        return p.x;
    }
    
    /**
     * Returns the y co-ordinate of the top left corner of the Rectangle.
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
        if ((x >= p.x) && (x <= (p.x + width)) && (y >= p.y)
            && (y <= (p.y + height)))
            return true;
        
        return false;
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
        
        canvas.fillRect(p.x, p.y, width, height, false);
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
        if ((width + changeWidth) < 1)
            changeWidth = -(width - 1);
        if ((height + changeHeight) < 1)
            changeHeight = -(height - 1);
        
        p.x -= changeWidth / 2;
        p.y -= changeHeight / 2;
        
        width += changeWidth;
        height += changeHeight;
    }
    
    /**
     * Sets this Rectangle's color to <code>color</code>
     * 
     * @param color
     *            the color.
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Sets this Rectangle's height to <code>height</code>.
     * 
     * @param height
     *            the height.
     */
    public void setHeight(int height) {
        this.height = height;
    }
    
    /**
     * Sets this Rectangle's top left corner to <code>p</code>.
     * 
     * @param p
     *            the point to set to.
     */
    public void setPoint(Point p) {
        this.p = p;
    }
    
    /**
     * Sets this Rectangle's width to <code>width</code>.
     * 
     * @param width
     *            the width.
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    /**
     * Sets this Rectangle's top left corner x co-ordinate to <code>x</code>.
     * 
     * @param x
     *            the x co-ordinate.
     */
    public void setX(int x) {
        p.x = x;
    }
    
    /**
     * Sets this Rectangle's top left corner y co-ordinate to <code>y</code>.
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
        return ("Rect " + p.x + " " + p.y + " " + width + " " + height + " "
            + color.getRed() + " " + color.getGreen() + " " + color.getBlue());
    }
}
