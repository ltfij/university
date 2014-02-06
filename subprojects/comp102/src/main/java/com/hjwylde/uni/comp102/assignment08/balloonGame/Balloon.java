package com.hjwylde.uni.comp102.assignment08.balloonGame;

import java.awt.Color;

import com.hjwylde.uni.comp102.util.DrawingCanvas;

/*
 * Code for Assignment 8, COMP 102
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Balloon {
    
    // Fields
    private final DrawingCanvas canvas;
    
    private int radius = 10;
    private final int centerX, centerY;
    private final Color color;
    
    private boolean popped = false;
    
    // Constructors
    /**
     * Construct a new Balloon object. Parameters are the DrawingCanvas and the
     * 
     * @param c the canvas.
     * @param x x co-ordinate.
     * @param y y co-ordinate.
     */
    public Balloon(DrawingCanvas c, int x, int y) {
        centerX = x;
        centerY = y;
        canvas = c;
        color = Balloon.randomBrightColor();
        draw();
    }
    
    public void draw() {
        canvas.setColor(color);
        canvas.fillOval(centerX - radius, centerY - radius, radius * 2,
            radius * 2);
        canvas.setColor(Color.black);
        canvas.drawOval(centerX - radius, centerY - radius, radius * 2,
            radius * 2);
    }
    
    /** Make the balloon larger by a random amount between 4 and 10, and redraw it */
    public void expand() {
        radius = radius + (int) (4 + (Math.random() * 6));
        draw();
    }
    
    public boolean isPopped() {
        return popped;
    }
    
    /**
     * Returns true if the point (x,y) is on the balloon, and false otherwise
     * 
     * @param x x co-ordinate.
     * @param y y co-ordinate.
     * @return true if the point is on this balloon.
     */
    public boolean on(int x, int y) {
        int dx = centerX - x;
        int dy = centerY - y;
        return (((dx * dx) + (dy * dy)) < (radius * radius));
    }
    
    /** pop the balloon (draws it in gray, and pauses briefly) */
    public void pop() {
        canvas.setColor(Color.lightGray);
        canvas.fillOval(centerX - radius, centerY - radius, radius * 2,
            radius * 2);
        
        popped = true;
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {}
    }
    
    /**
     * Calculates and returns the area of the balloon
     * 
     * @return the size.
     */
    public double size() {
        return radius * radius * Math.PI;
    }
    
    /**
     * Returns true if this Balloon is touching the other balloon, and false
     * otherwise
     * 
     * @param other the other balloon.
     * @return true if this balloon touches other.
     */
    public boolean touches(Balloon other) {
        double xDistance = centerX - other.centerX;
        double yDistance = centerY - other.centerY;
        double distanceSquared = Math.pow(xDistance, 2)
            + Math.pow(yDistance, 2);
        
        return distanceSquared < Math.pow((radius + other.radius), 2);
    }
    
    /**
     * Bright colours have one of components at 1.0, another at 0.0, and the third
     * colour anwhere between 0.0 and 1.0. This assigns 1.0, a random value, and
     * 0.0 to red, green, blue, then randomly switches them around.
     * 
     * @return a random color.
     */
    public static Color randomBrightColor() {
        float red = 1;
        float green = (float) Math.random();
        float blue = 0;
        if (Math.random() < 0.5) { // maybe switch red and green
            red = green;
            green = 1;
        }
        if (Math.random() < 0.67)
            if (Math.random() < 0.5) { // maybe switch blue and red
                blue = red;
                red = 0;
            } else { // switch blue and green
                blue = green;
                green = 0;
            }
        return new Color(red, green, blue);
    }
    
}
