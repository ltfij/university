package com.hjwylde.uni.swen221.lab02.shapes.core;

import java.awt.Color;
import java.awt.Graphics;

import com.hjwylde.uni.swen221.lab02.shapes.math.BoundingBox;
import com.hjwylde.uni.swen221.lab02.shapes.math.Vec2D;

/*
 * Code for Laboratory 2, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Represents a circle. A circle has a radius which is given at instantiation.
 * 
 * @author Henry J. Wylde
 */
public class Circle extends AbstractShape {

    private int radius;

    /**
     * Creates a new Circle with the specified parameters.
     * 
     * @param pos the initial position of the circle.
     * @param radius the radius of the circle.
     * @param velocity the initial velocity of the circle.
     * @param color the color of the circle.
     */
    public Circle(Vec2D pos, int radius, Vec2D velocity, Color color) {
        super(pos, velocity, color);

        this.radius = radius;
    }

    /*
     * @see lab2.shapes.core.AbstractShape#getBoundingBox()
     */
    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox((int) getPosition().getX(), (int) getPosition().getY(), radius,
                radius);
    }

    /*
     * @see lab2.shapes.core.Shape#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        g.setColor(getColor());
        g.fillOval((int) getPosition().getX(), (int) getPosition().getY(), radius, radius);
    }
}
