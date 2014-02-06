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

/**
 * Represents a rectangle. A rectangle has a width and height.
 * 
 * @author Henry J. Wylde
 */
public class Rectangle extends AbstractShape {
    
    private int width;
    private int height;
    
    /**
     * Creates a new Rectangle witht he specified parameters.
     * 
     * @param pos the initial position of the rectangle.
     * @param width the width of the rectangle.
     * @param height the height of the rectangle.
     * @param velocity the initial velocity of the rectangle.
     * @param color the color of the rectangle.
     */
    public Rectangle(Vec2D pos, int width, int height, Vec2D velocity,
        Color color) {
        super(pos, velocity, color);
        
        this.width = width;
        this.height = height;
    }
    
    /*
     * @see lab2.shapes.core.AbstractShape#getBoundingBox()
     */
    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox((int) getPosition().getX(), (int) getPosition()
            .getY(), width, height);
    }
    
    /*
     * @see lab2.shapes.core.Shape#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        g.setColor(getColor());
        g.fillRect((int) getPosition().getX(), (int) getPosition().getY(),
            width, height);
    }
    
}
