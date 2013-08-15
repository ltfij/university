package com.hjwylde.uni.comp103.assignment5.forest;

/*
 * Code for Assignment 5, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.awt.Color;
import java.util.Random;

import com.hjwylde.uni.comp103.util.DrawingCanvas;

public class Dandelion {
    
    // Fields
    private final double PI = Math.PI;
    private final double branchAngle = (PI / 3) * Math.random();
    private final double shrinkage = 0.3 + (0.3 * Math.random());
    private final int numBranches = 2 + (int) (4 * Math.random());
    private final DrawingCanvas canvas;
    private final Color col;
    
    // Growth fields
    private final double x;
    private final double y;
    private final long seed;
    
    /**
     * Creates a new Dandelion with random co-ordinates and draws it to the
     * DrawingCanvas <code>c</code>.
     * 
     * @param c
     *            the DrawingCanvas to draw the Dandelion on.
     */
    public Dandelion(DrawingCanvas c) {
        this(c, 900 * Math.random(), 400.0 + (100.0 * Math.random()), true);
    }
    
    /**
     * Creates a new Dandelion with the specified x and y co-ordinates. Optionally
     * draws it to the
     * DrawingCanvas.
     * 
     * @param c
     *            the DrawingCanvas to draw the Dandelion on.
     * @param x
     *            the x co-ordinate.
     * @param y
     *            the y co-ordinate.
     * @param draw
     *            whether to draw the Dandelion.
     */
    public Dandelion(DrawingCanvas c, double x, double y, boolean draw) {
        canvas = c;
        
        this.x = x;
        this.y = y;
        
        col = new Color((float) (0.2 + (0.8 * Math.random())),
            (float) (0.2 + (0.8 * Math.random())),
            (float) (0.2 + (0.8 * Math.random())));
        
        seed = System.currentTimeMillis(); // Used for consistant random angles in
                                           // drawing the branches
        
        if (draw)
            draw(x, y, 200 * Math.random(), -PI / 2);
    }
    
    public void draw(double x, double y, double dist, double ang) {
        // If length is less than 10, or the branch is not visible on the canvas...
        if ((dist < 10) || (x < 0) || (y < 0) || (x > canvas.getWidth())
            || (y > canvas.getHeight()))
            return; // ...then return
            
        // Draw a line from (x,y) of length dist at angle ang
        double newX = x + (dist * Math.cos(ang));
        double newY = y + (dist * Math.sin(ang));
        
        // Draw this branch
        canvas.setColor(col);
        canvas.drawLine((int) x, (int) y, (int) newX, (int) newY);
        
        // For calculating the next branches
        double branchSpan = ((double) numBranches - 1) * branchAngle;
        Random rand = new Random(seed);
        
        // Draw each of the branches
        // Note: This randomizes the branch angle slightly and shrinkage. With using
        // a "seed", these
        // ranomizations will be consistant for all drawings of this Dandelion
        for (double i = 0; i < numBranches; i++)
            draw(
                newX,
                newY,
                shrinkage * dist * (0.3 + rand.nextDouble()),
                ((ang + (i * branchAngle)) - (branchSpan / 2))
                    + (((0.5 - rand.nextDouble()) * PI) / 6));
    }
    
    /**
     * Show the growth of this Dandelion over time. It will draw the Dandelion in
     * it's original
     * position and continually be shown to the right of it until the end of the
     * screen.
     */
    public void showGrowth() {
        // Random initial length of the Dandelion
        double length = 10 + (Math.random() * 15);
        double xCoord = x;
        
        // Draw Dandelions while the Dandelion is still on the canvas
        while (xCoord < canvas.getWidth()) {
            draw(xCoord, y, length, -PI / 2);
            xCoord += 30 + (120 * shrinkage); // Spread out the growths, the larger the
            // branches will be,
            // the more spread out each Dandelion
            // should be to prevent
            // overlapping
            length *= 1.5; // Grow the tree!
        }
    }
}
