package com.hjwylde.uni.swen221.assignment4.shapes.geometry;

/*
 * Code for Assignment 4, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Represents a rectangle shape. The Rectangle is immutable.
 * 
 * @author Henry J. Wylde
 */
public class Rectangle implements Shape {
    
    private final int x, y;
    private final int width, height;
    
    /**
     * Creates a new Rectangle with the specified inputs. Will throw an
     * <code>IllegalArgumentException</code> if the width or height is less than 0.
     * 
     * @param x the top left x co-ordinate of the rectangle.
     * @param y the top left y co-ordinate of the rectangle.
     * @param width the width of the rectangle.
     * @param height the height of the rectangle.
     */
    public Rectangle(int x, int y, int width, int height) {
        if ((width < 0) || (height < 0))
            throw new IllegalArgumentException();
        
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Calculates and returns the area of the rectangle.
     * 
     * @return the area of the rectangle.
     */
    public int area() {
        return width * height;
    }
    
    /*
     * @see assignment4.shapes.geometry.Shape#boundingBox()
     */
    @Override
    public Rectangle boundingBox() {
        return (Rectangle) clone();
    }
    
    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        return new Rectangle(x, y, width, height);
    }
    
    /*
     * @see assignment4.shapes.geometry.Shape#contains(int, int)
     */
    @Override
    public boolean contains(int x, int y) {
        if ((x < this.x) || (x >= (this.x + width)))
            return false;
        
        if ((y < this.y) || (y >= (this.y + height)))
            return false;
        
        return true;
    }
    
    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        
        if ((obj == null) || !(obj instanceof Rectangle))
            return false;
        
        Rectangle other = (Rectangle) obj;
        if ((x != other.x) || (y != other.y))
            return false;
        if ((height != other.height) || (width != other.width))
            return false;
        
        return true;
    }
    
    /**
     * Gets the height of the rectangle.
     * 
     * @return the height.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Gets the width of the rectangle.
     * 
     * @return the width.
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Gets the top left x co-ordinate of the rectangle.
     * 
     * @return the top left x co-ordinate.
     */
    public int getX() {
        return x;
    }
    
    /**
     * Gets the top left y co-ordinate of the rectangle.
     * 
     * @return the top left y co-ordinate of the rectangle.
     */
    public int getY() {
        return y;
    }
    
    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        
        int result = 1;
        result = (prime * result) + x;
        result = (prime * result) + y;
        result = (prime * result) + width;
        result = (prime * result) + height;
        
        return result;
    }
    
    /**
     * Calculates whether this rectangle is empty or not. A rectangle is empty if it does not have
     * any
     * area - in other words, no <code>(x, y)</code> point is contained inside of the rectangle.
     * 
     * @return whether the rectangle is empty.
     */
    public boolean isEmpty() {
        return (area() == 0);
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Rectangle [x=" + x + ", y=" + y + ", width=" + width
            + ", height=" + height + "]";
    }
    
    /**
     * Calculates a "bounding" difference of the specified two rectangles. The "bounding" difference
     * is the smallest rectangle that contains <code>(left - right)</code>.
     * 
     * @param left the left rectangle.
     * @param right the right rectangle.
     * @return the "bounding" rectangle of the difference of the two rectangles specified.
     */
    public static Rectangle difference(Rectangle left, Rectangle right) {
        if ((right == null) || (left == null) || right.isEmpty())
            return left;
        
        // Defaults for when right overlaps left.
        int x1 = left.x;
        int y1 = left.y;
        int x2 = right.x;
        int y2 = right.y;
        
        // Check for special cases:
        
        // If "right" doesn't overlap "left" from east or west...
        if ((right.y > left.y)
            || ((right.y + right.height) < (left.y + left.height)))
            x2 = left.x + left.width;
        else if (right.contains(left.x, left.y)) // If "right" is actually to the left of "left"...
            // Adjust the left x.
            x1 = right.x + right.width;
        
        // If "right" doesn't overlap "left" from north or south...
        if ((right.x > left.x)
            || ((right.x + right.width) < (left.x + left.width)))
            y2 = left.y + left.height;
        else if (right.contains(left.x, left.y)) // If "right" is actually above "left"...
            // Adjust the top y.
            y1 = right.y + right.height;
        
        // Return a new rectangle, but check first if the width and height are less than 0 and if
        // they
        // are, create with 0 width or height.
        return new Rectangle(x1, y1, (((x2 - x1) < 0) ? 0 : (x2 - x1)),
            (((y2 - y1) < 0) ? 0 : (y2 - y1)));
        
    }
    
    /**
     * Calculates a "bounding" intersection of the specified two rectangles. The "bounding"
     * intersection is the smallest rectangle that contains <code>(left & right)</code>. Returns
     * null
     * if the rectangles don't intersect at all - note that this is different to
     * "intersecting with 0 area".
     * 
     * @param left the left rectangle.
     * @param right the right rectangle.
     * @return the "bounding" rectangle of the intersection of the two rectangles specified or null
     *         if
     *         they don't intersect at all.
     */
    public static Rectangle intersection(Rectangle left, Rectangle right) {
        if ((left == null) || (right == null))
            return null;
        
        // Compute the intersection top left and bottom right co-ordinates.
        int x1 = Math.max(left.x, right.x);
        int y1 = Math.max(left.y, right.y);
        int x2 = Math.min(left.x + left.width, right.x + right.width);
        int y2 = Math.min(left.y + left.height, right.y + right.height);
        
        // If there is no intersection (note: this is not the same as an empty rectangle)...
        if (((x2 - x1) < 0) || ((y2 - y1) < 0))
            return null;
        
        return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }
    
    /**
     * Calculates a "bounding" union of the specified two rectangles. The "bounding" union is the
     * smallest rectangle that contains <code>(left + right)</code>.
     * 
     * @param left the left rectangle.
     * @param right the right rectangle.
     * @return the "bounding" rectangle of the union of the two rectangles specified.
     */
    public static Rectangle union(Rectangle left, Rectangle right) {
        if ((left == null) || left.isEmpty())
            return right;
        if ((right == null) || right.isEmpty())
            return left;
        
        // Compute the union top left and bottom right co-ordinates.
        int x1 = Math.min(left.x, right.x);
        int y1 = Math.min(left.y, right.y);
        int x2 = Math.max(left.x + left.width, right.x + right.width);
        int y2 = Math.max(left.y + left.height, right.y + right.height);
        
        return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }
}