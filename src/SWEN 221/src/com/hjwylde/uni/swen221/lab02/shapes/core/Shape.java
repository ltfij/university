package com.hjwylde.uni.swen221.lab02.shapes.core;

import java.awt.Color;
import java.awt.Graphics;

import com.hjwylde.uni.swen221.lab02.shapes.math.BoundingBox;
import com.hjwylde.uni.swen221.lab02.shapes.math.Vec2D;

/*
 * Code for Laboratory 2, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public interface Shape {
    
    /**
     * Check if this shape has collided with another shape. If so, respond in some appropriate
     * manner.
     * 
     * @param s the shape to check with.
     */
    public void checkShapeCollision(Shape s);
    
    /**
     * Check if this shape has collided with one of the outer walls. If so,
     * respond in some appropriate manner.
     */
    public void checkWallCollision(int width, int height);
    
    /**
     * A clock event is signaled. The shape must respond to this by moving in
     * some manner.
     */
    public void clockTick();
    
    /**
     * Return the bounding box for the shape in question. The bounding box is
     * useful as a first stage in determining whether two shapes are touching.
     * 
     * @return the bounding box.
     */
    public BoundingBox getBoundingBox();
    
    /**
     * Return the color of this shape.
     * 
     * @return the color.
     */
    public Color getColor();
    
    /**
     * Return the velocity of this shape. That is, its direction and speed.
     * 
     * @return the velocity.
     */
    public Vec2D getVelocity();
    
    /**
     * Draw this shape onto the given graphics context.
     * 
     * @param context the context.
     */
    public void paint(Graphics context);
}
