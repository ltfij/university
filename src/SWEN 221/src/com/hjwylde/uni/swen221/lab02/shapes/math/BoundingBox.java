package com.hjwylde.uni.swen221.lab02.shapes.math;

/*
 * Code for Laboratory 2, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Represents a bounding box on the screen. Thus, the position and dimension of
 * the box is represented using actual screen coordinates.
 * 
 * @author David J. Pearce
 * 
 */
public final class BoundingBox {
    
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    
    public BoundingBox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public boolean intersect(BoundingBox b) {
        int mrx = x + width;
        int mry = y + height;
        int brx = b.x + b.width;
        int bry = b.y + b.height;
        
        boolean xcol = ((x >= b.x) && (x < brx))
            || ((mrx >= b.x) && (mrx < brx));
        boolean ycol = ((y >= b.y) && (y < bry))
            || ((mry >= b.y) && (mry < bry));
        return xcol && ycol;
        
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
