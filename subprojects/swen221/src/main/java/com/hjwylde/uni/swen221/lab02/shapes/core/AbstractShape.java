package com.hjwylde.uni.swen221.lab02.shapes.core;

import java.awt.Color;
import java.util.ArrayList;

import com.hjwylde.uni.swen221.lab02.shapes.math.BoundingBox;
import com.hjwylde.uni.swen221.lab02.shapes.math.Vec2D;

/*
 * Code for Laboratory 2, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public abstract class AbstractShape implements Shape {

    /**
     * Position is the current position of the shape.
     */
    private Vec2D position;

    /**
     * Current velocity is the current velocity of the shape.
     */
    private Vec2D currentVelocity;

    /**
     * Next velocity is the velocity this shape will have in the next tick.
     */
    private Vec2D nextVelocity;

    /**
     * Set of current collisions
     */
    private ArrayList<Shape> currentCollisions = new ArrayList<>();

    /**
     * The color of this shape.
     */
    private Color color;

    public AbstractShape(Vec2D position, Vec2D velocity, Color color) {
        this.position = position;
        currentVelocity = velocity;
        nextVelocity = velocity;
        this.color = color;
    }

    @Override
    public void checkShapeCollision(Shape shape) {
        BoundingBox myBox = getBoundingBox();
        BoundingBox shapeBox = shape.getBoundingBox();
        if (myBox.intersect(shapeBox)) {
            // Check if we're already colliding with this shape. If not, then respond ... otherwise
            // ignore.

            if (!currentCollisions.contains(shape)) {
                nextVelocity = nextVelocity.add(shape.getVelocity());
                nextVelocity = nextVelocity.multiply(0.5);
                currentCollisions.add(shape);
            }
        } else
            currentCollisions.remove(shape);
    }

    @Override
    public void checkWallCollision(int width, int height) {
        BoundingBox box = getBoundingBox();
        int lx = box.getX();
        int rx = lx + box.getWidth();
        int ly = box.getY();
        int ry = ly + box.getHeight();

        if ((lx <= 0) && (nextVelocity.getX() < 0)) {
            nextVelocity = nextVelocity.invertX();
            nextVelocity = nextVelocity.multiply(0.95); // friction
        } else if ((rx >= width) && (nextVelocity.getX() > 0)) {
            nextVelocity = nextVelocity.invertX();
            nextVelocity = nextVelocity.multiply(0.95); // friction
        }

        if ((ly <= 0) && (nextVelocity.getY() < 0)) {
            nextVelocity = nextVelocity.invertY();
            nextVelocity = nextVelocity.multiply(0.95); // friction
        } else if ((ry >= height) && (nextVelocity.getY() > 0)) {
            nextVelocity = nextVelocity.invertY();
            nextVelocity = nextVelocity.multiply(0.95); // friction
        }
    }

    @Override
    public void clockTick() {
        currentVelocity = nextVelocity;
        position = position.add(currentVelocity);
    }

    /**
     * Get the bounding box of this shape
     */
    @Override
    public abstract BoundingBox getBoundingBox();

    /**
     * Get the color of this shape
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * Get the reference position of this shape
     */
    public Vec2D getPosition() {
        return position;
    }

    /**
     * Get the velocity of this shape
     */
    @Override
    public Vec2D getVelocity() {
        return currentVelocity;
    }
}
