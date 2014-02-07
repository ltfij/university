package com.hjwylde.uni.swen221.lab02.shapes.math;

/*
 * Code for Laboratory 2, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Represents a 2-Dimensional Vector. Note that Vec2D objects are immutable (i.e. they cannot be
 * changed);
 * 
 * @author David J. Pearce
 * 
 */
public final class Vec2D {

    private final double xComponent;
    private final double yComponent;

    public Vec2D(double x, double y) {
        xComponent = x;
        yComponent = y;
    }

    /**
     * Add a vector onto this vector.
     * 
     * @param other the vector to add.
     * @return the new combined vector.
     */
    public Vec2D add(Vec2D other) {
        return new Vec2D(xComponent + other.xComponent, yComponent + other.yComponent);
    }

    /**
     * Get x component of this vector
     * 
     * @return the x co-ordinate.
     */
    public double getX() {
        return xComponent;
    }

    /**
     * Get y component of this vector
     * 
     * @return the y component.
     */
    public double getY() {
        return yComponent;
    }

    /**
     * Invert both components
     * 
     * @return the inverted vector.
     */
    public Vec2D invert() {
        return new Vec2D(-xComponent, -yComponent);
    }

    /**
     * Reflect this velocity along the x-axis
     * 
     * @return the vector with its x co-ordinate inverted.
     */
    public Vec2D invertX() {
        return new Vec2D(-xComponent, yComponent);
    }

    /**
     * Reflect this velocity along the y-axis
     * 
     * @return the vector with its y co-ordinate inverted.
     */
    public Vec2D invertY() {
        return new Vec2D(xComponent, -yComponent);
    }

    /**
     * Multiple a vector by a constant
     * 
     * @param constant the constant to multiply all components by.
     * @return the new multiplied vector.
     */
    public Vec2D multiply(double constant) {
        return new Vec2D(xComponent * constant, yComponent * constant);
    }

    @Override
    public String toString() {
        return "(" + xComponent + ", " + yComponent + ")";
    }
}
