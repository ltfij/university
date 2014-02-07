package com.hjwylde.uni.comp102.assignment10.miniDraw;

import java.awt.Color;
import java.awt.Point;
import java.util.Scanner;

import com.hjwylde.uni.comp102.util.DrawingCanvas;

/*
 * Code for Assignment 10, COMP 102 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Line represents a straight line. Implements the Shape interface.
 */
public class Line implements Shape {

    // Line co-ordinates.
    private Point p1;
    private Point p2;

    // Line properties.
    private int width;
    private int height;
    private double length;
    private Color color;

    /**
     * Creates a new Line class taking the parameters for two points on a 2d plane and color values.
     * 
     * @param x1 the x1 co-ordinate.
     * @param y1 the y1 co-ordinate.
     * @param x2 the x2 co-ordinate.
     * @param y2 the y2 co-ordinate.
     * @param color the color.
     */
    public Line(int x1, int y1, int x2, int y2, Color color) {
        p1 = new Point(x1, y1);
        p2 = new Point(x2, y2);

        width = p2.x - p1.x;
        height = p2.y - p1.x;
        length = p1.distance(p2);
        assert color != null : "Colour of line should not be null.";
        this.color = color;
    }

    /**
     * Creates a new Line class taking the parameters for two points on a 2d plane and color values.
     * 
     * @param x1 the x1 co-ordinate.
     * @param y1 the y1 co-ordinate.
     * @param x2 the x2 co-ordinate.
     * @param y2 the y2 co-ordinate.
     * @param r the red color value (0-255).
     * @param g the green color value (0-255).
     * @param b the blue color value (0-255).
     */
    public Line(int x1, int y1, int x2, int y2, int r, int g, int b) {
        this(x1, y1, x2, y2, new Color(r, g, b));
    }

    /**
     * Creates a new Line class taking a scanner holding values for the parameters for it's two
     * points on a 2d plane and color values.
     * 
     * @param data the scanner.
     */
    public Line(Scanner data) {
        this(data.nextInt(), data.nextInt(), data.nextInt(), data.nextInt(), new Color(
                data.nextInt(), data.nextInt(), data.nextInt()));
    }

    /**
     * Returns the color of this Line.
     * 
     * @return the color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the height of this Line.
     * 
     * @return the height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the length of this Line.
     * 
     * @return the length.
     */
    public double getLength() {
        return length;
    }

    /**
     * Returns the first point of this Line.
     * 
     * @return the first point.
     */
    public Point getPoint1() {
        return p1;
    }

    /**
     * Returns the second point of this Line.
     * 
     * @return the second point.
     */
    public Point getPoint2() {
        return p2;
    }

    /**
     * Returns the width of this Line.
     * 
     * @return the width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Changes the position of the shape by dx and dy. If it was positioned at (x, y), it will now
     * be at (x + dx, y + dy).
     * 
     * @param dx value to translate the x co-ordinate by.
     * @param dy value to translate the y co-ordinate by.
     */
    @Override
    public void moveBy(int dx, int dy) {
        p1.translate(dx, dy);
        p2.translate(dx, dy);
    }

    /**
     * Returns true if the point (x, y) is on top of the shape.
     * 
     * @param x the x co-ordinate.
     * @param y the y co-ordinate.
     * @return whether the point is on this shape.
     */
    @Override
    public boolean pointOnShape(int x, int y) {
        int threshold = 3;
        if ((x < (Math.min(p1.x, p2.x) - threshold)) || (x > (Math.max(p1.x, p2.x) + threshold))
                || (y < (Math.min(p1.y, p2.y) - threshold))
                || (y > (Math.max(p1.y, p2.y) + threshold)))
            return false;

        int dx = x - p1.x;
        int dy = y - p1.y;
        return (Math.abs(((dy * width) - (dx * height)) / length) <= threshold);
    }

    /**
     * Renders the shape on a canvas. It uses the drawing methods with the extra last argument of
     * "false" so that the shape will not actually appear until the canvas is redisplayed later.
     * This gives much smoother redrawing.
     * 
     * @param canvas the DrawingCanvas to draw to.
     */
    @Override
    public void render(DrawingCanvas canvas) {
        Color canvasColor = canvas.getForeground();
        canvas.setForeground(color);

        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, false);
        canvas.setForeground(canvasColor);
    }

    /**
     * Changes the width and height of the shape by the specified amounts. The amounts may be
     * negative, which means that the shape should get smaller, at least in that direction. The
     * shape should never become smaller than 1 pixel in width or height The center of the shape
     * should remain the same.
     * 
     * @param changeWidth the width to be changed.
     * @param changeHeight the height to be changed.
     */
    @Override
    public void resize(int changeWidth, int changeHeight) {
        if (p1.x < p2.x) {
            p1.x -= changeWidth / 2;
            p2.x += changeWidth / 2;
        } else {
            p1.x += changeWidth / 2;
            p2.x -= changeWidth / 2;
        }
        if (p1.y < p2.y) {
            p1.y -= changeHeight / 2;
            p2.y += changeHeight / 2;
        } else {
            p1.y += changeHeight / 2;
            p2.y -= changeHeight / 2;
        }

        width = p2.x - p1.x;
        height = p2.y - p1.y;
        length = p1.distance(p2);
    }

    /**
     * Sets this Line's color to <code>color</code>.
     * 
     * @param color the color.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets the first point of this Line to <code>p</code>.
     * 
     * @param p the point to set to.
     */
    public void setPoint1(Point p) {
        p1 = p;

        width = p2.x - p1.x;
        height = p2.y - p1.x;
        length = p1.distance(p2);
    }

    /**
     * Sets the second point of this Line to <code>p</code>.
     * 
     * @param p the point to set to.
     */
    public void setPoint2(Point p) {
        p2 = p;

        width = p2.x - p1.x;
        height = p2.y - p1.x;
        length = p1.distance(p2);
    }

    /**
     * Returns a string description of the shape in a form suitable for writing to a file in order
     * to reconstruct the shape later
     * 
     * @return the string representing this Shape.
     */
    @Override
    public String toString() {
        return ("Line " + p1.x + " " + p1.y + " " + p2.x + " " + p2.y + " " + color.getRed() + " "
                + color.getGreen() + " " + color.getBlue());
    }
}
