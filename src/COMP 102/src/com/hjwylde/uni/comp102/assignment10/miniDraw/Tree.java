package com.hjwylde.uni.comp102.assignment10.miniDraw;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.hjwylde.uni.comp102.util.DrawingCanvas;

/*
 * Code for Assignment 10, COMP 102
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Tree represents a tree shape. Implements the Shape interface.
 */
public class Tree implements Shape {
    
    // Tree co-ordinates.
    private Point p1;
    private Point p2;
    private final ArrayList<Line> branches = new ArrayList<>();
    private final ArrayList<Dot> leaves = new ArrayList<>();
    
    // Tree properties.
    private final long seed;
    private final Random r;
    private Color color;
    
    /**
     * Constructor with explicit values Arguments are the x and y of the top left
     * corner, the width and height, and the color.
     * 
     * @param x1 x1 co-ordinate.
     * @param y1 y1 co-ordinate.
     * @param x2 x2 co-ordinate.
     * @param y2 y2 co-ordinate.
     * @param color the color.
     */
    public Tree(int x1, int y1, int x2, int y2, Color color) {
        this(x1, y1, x2, y2, color, System.currentTimeMillis());
    }
    
    /**
     * Constructor with explicit values Arguments are the x and y of the top left
     * corner, the width and height, and the color.
     * 
     * @param x1 x1 co-ordinate.
     * @param y1 y1 co-ordinate.
     * @param x2 x2 co-ordinate.
     * @param y2 y2 co-ordinate.
     * @param color the color.
     * @param seed the random seed.
     */
    public Tree(int x1, int y1, int x2, int y2, Color color, long seed) {
        p1 = new Point(x1, y1);
        p2 = new Point(x2, y2);
        
        assert color != null : "Colour of line should not be null.";
        this.seed = seed;
        r = new Random(seed);
        this.color = color;
        
        createTree();
    }
    
    /**
     * Constructor with explicit values Arguments are the x and y of the top left
     * corner, the width and height, and the color.
     * 
     * @param x1 x1 co-ordinate.
     * @param y1 y1 co-ordinate.
     * @param x2 x2 co-ordinate.
     * @param y2 y2 co-ordinate.
     * @param r the red component.
     * @param g the green component.
     * @param b the blue component.
     */
    public Tree(int x1, int y1, int x2, int y2, int r, int g, int b) {
        this(x1, y1, x2, y2, new Color(r, g, b), System.currentTimeMillis());
    }
    
    /**
     * Constructor with explicit values Arguments are the x and y of the top left
     * corner, the width and height, and the color.
     * 
     * @param x1 x1 co-ordinate.
     * @param y1 y1 co-ordinate.
     * @param x2 x2 co-ordinate.
     * @param y2 y2 co-ordinate.
     * @param r the red component.
     * @param g the green component.
     * @param b the blue component.
     * @param seed the random seed.
     */
    public Tree(int x1, int y1, int x2, int y2, int r, int g, int b, long seed) {
        this(x1, y1, x2, y2, new Color(r, g, b), seed);
    }
    
    /**
     * [Extension] Constructor which reads values from a file (scanner) The
     * argument is a Scanner that contains the specification of the Rectangle. The
     * next 7 integers specify the position of the top left corner, and the width
     * and height, and three ints specifying the color.
     * 
     * @param data the scanner.
     */
    public Tree(Scanner data) {
        this(data.nextInt(), data.nextInt(), data.nextInt(), data.nextInt(),
            new Color(data.nextInt(), data.nextInt(), data.nextInt()), data
                .nextLong());
    }
    
    /**
     * Returns the color of this Tree.
     * 
     * @return the color.
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Returns the height of this Tree.
     * 
     * @return the height.
     */
    public int getHeight() {
        return Math.abs(p1.y - p2.y);
    }
    
    /**
     * Returns the length of this Tree.
     * 
     * @return the length.
     */
    public double getLength() {
        return p1.distance(p2);
    }
    
    /**
     * Returns the first point of this Tree.
     * 
     * @return the first point.
     */
    public Point getPoint1() {
        return p1;
    }
    
    /**
     * Returns the second point of this Tree.
     * 
     * @return the second point.
     */
    public Point getPoint2() {
        return p2;
    }
    
    /**
     * Returns the width of this Tree.
     * 
     * @return the width.
     */
    public int getWidth() {
        return Math.abs(p1.x - p2.x);
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
        p1.translate(dx, dy);
        p2.translate(dx, dy);
        
        for (Line branch : branches)
            branch.moveBy(dx, dy);
        for (Dot leaf : leaves)
            leaf.moveBy(dx, dy);
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
        for (Line branch : branches)
            if (branch.pointOnShape(x, y))
                return true;
        for (Dot leaf : leaves)
            if (leaf.pointOnShape(x, y))
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
        
        for (Line branch : branches)
            branch.render(canvas);
        for (Dot leaf : leaves)
            leaf.render(canvas);
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
        p1.translate(changeWidth, changeHeight);
        createTree();
    }
    
    /**
     * Sets this Tree's color to <code>color</code>.
     * 
     * @param color the color.
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Sets the first point of this Tree to <code>p</code>.
     * 
     * @param p the point to set to.
     */
    public void setPoint1(Point p) {
        p1 = p;
    }
    
    /**
     * Sets the second point of this Tree to <code>p</code>.
     * 
     * @param p
     *            the point to set to.
     */
    public void setPoint2(Point p) {
        p2 = p;
    }
    
    /**
     * Returns a string description of the shape in a form suitable for writing to
     * a file in order to reconstruct the shape later
     * 
     * @return the string representing this Shape.
     */
    @Override
    public String toString() {
        return ("Tree " + p1.x + " " + p1.y + " " + p2.x + " " + p2.y + " "
            + color.getRed() + " " + color.getGreen() + " " + color.getBlue()
            + " " + seed);
    }
    
    /**
     * A recursive function for adding branches of the Tree to an ArrayList. Takes
     * the starting position, length and angle of the branch to be added as
     * parameters. Then calls itself 3 times: with a shortened length and various
     * angles.
     * 
     * @param x0
     *            the x co-ordinate of the branch.
     * @param y0
     *            the y co-ordinate of the branch.
     * @param length
     *            the length of the branch.
     * @param angle
     *            the angle of the branch.
     */
    private void addBranch(double x0, double y0, double length, double angle) {
        // Do not create any more branches if length is less than 4 pixels, instead
        // add a leaf (5 pixel dot) to the end.
        if (length <= 4) {
            leaves.add(new Dot((int) x0, (int) y0, color));
            return;
        }
        
        double x1 = x0 + (length * Math.cos(angle));
        double y1 = y0 - (length * Math.sin(angle));
        
        branches.add(new Line((int) x0, (int) y0, (int) x1, (int) y1, color));
        
        // Create another 3 branches off of this one, each with different sized
        // lengths and angles.
        addBranch(x1, y1, length * 0.75,
            angle + (Math.PI / 6) + ((r.nextDouble() * Math.PI) / 9));
        addBranch(x1, y1, length * 0.4, angle
            + ((r.nextDouble() * Math.PI) / 18));
        addBranch(x1, y1, length * 0.66,
            (angle - ((Math.PI / 18) * 5)) + ((r.nextDouble() * Math.PI) / 9));
    }
    
    /**
     * Creates a tree using a recursive function based on the length of the base
     * trunk of the tree (p1 and p2). Adds all the branches and leaves (5 pixel
     * dots at end of last branches) to an ArrayList.
     */
    private void createTree() {
        branches.clear();
        leaves.clear();
        
        // Calculate starting angle of tree trunk.
        double angle;
        if ((p2.x - p1.x) == 0) {
            if (p2.y < p1.y)
                angle = Math.PI / 2;
            else
                angle = -Math.PI / 2;
        } else
            angle = Math.atan2(p1.y - p2.y, p2.x - p1.x);
        // Start the recursive function.
        addBranch(p1.getX(), p1.getY(), p1.distance(p2), angle);
    }
}
