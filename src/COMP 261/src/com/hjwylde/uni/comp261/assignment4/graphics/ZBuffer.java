package com.hjwylde.uni.comp261.assignment4.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

/*
 * Code for Assignment 4, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A <code>ZBuffer</code> is used to create an image which only renders the pixels closest to the
 * camera.
 * 
 * @author Henry J. Wylde
 */
public class ZBuffer {
    
    private final int width;
    private final int height;
    
    private BufferedImage img;
    private double[][] depth;
    
    /**
     * Creates a new <code>ZBuffer</code> of size <code>(width, height)</code>.
     * 
     * @param width the width of the image.
     * @param height the height of the image.
     */
    public ZBuffer(int width, int height) {
        if ((width <= 0) || (height <= 0))
            throw new IllegalArgumentException();
        
        this.width = width;
        this.height = height;
        
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); // Allow transparency.
        depth = new double[width][height];
        
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                depth[x][y] = Integer.MIN_VALUE; // Double.MIN_VALUE doesn't work in Math.min(...)
                                                 // calculations.
    }
    
    /**
     * Get the height of this <code>ZBuffer</code>.
     * 
     * @return the <code>height</code>.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Get the Image that this <code>ZBuffer</code> has generated.
     * 
     * @return the Image.
     */
    public Image getImage() {
        return img;
    }
    
    /**
     * Get the width of this <code>ZBuffer</code>.
     * 
     * @return the <code>width</code>.
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Attempts to set the pixel at <code>(x, y)</code> with the specified color. It will only set
     * the
     * pixel if the depth <code>z</code> is less (closer towards the camera) than the current
     * recorded
     * depth. Updates the depth[][] array if the pixel is set.
     * 
     * @param x the x-value of the pixel.
     * @param y the y-value of the pixel.
     * @param z the depth of the pixel.
     * @param color the color of the pixel.
     */
    public void set(int x, int y, double z, Color color) {
        if ((x < 0) || (x >= width) || (y < 0) || (y >= height)
            || (color == null)) // If (x, y) is not
                                // within bounds...
            return; // ... return.
            
        if (depth[x][y] > z) // If the current color is more prominent (closer to camera)...
            return; // ... return.
            
        // Set the point at (x, y) to have the new depth and color.
        depth[x][y] = z;
        img.setRGB(x, y, color.getRGB());
    }
}