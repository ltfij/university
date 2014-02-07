package com.hjwylde.uni.swen221.lab02.shapes.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.Iterator;
import java.util.List;

import com.hjwylde.uni.swen221.lab02.shapes.math.BoundingBox;
import com.hjwylde.uni.swen221.lab02.shapes.math.Vec2D;

/*
 * Code for Laboratory 2, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Represents a polytope. A polytope is any shape that has 3 or more points.
 * 
 * @author Henry J. Wylde
 */
public class Polytope extends AbstractShape {

    List<Vec2D> points;

    /**
     * Creates a new Polytope with the specified parameters. The polytope points list must contain
     * at least 3 points or else it will throw an exception.
     * 
     * @param pos the position of the polytope.
     * @param points the list of points of the polytope.
     * @param velocity the velocity of the polytope.
     * @param color the color of the polytope.
     */
    public Polytope(Vec2D pos, List<Vec2D> points, Vec2D velocity, Color color) {
        super(pos, velocity, color);

        if (points.size() < 3)
            throw new IllegalArgumentException();

        this.points = points;
    }

    /*
     * @see lab2.shapes.core.AbstractShape#getBoundingBox()
     */
    @Override
    public BoundingBox getBoundingBox() {
        int x1 = Integer.MAX_VALUE;
        int y1 = Integer.MAX_VALUE;
        int x2 = Integer.MIN_VALUE;
        int y2 = Integer.MIN_VALUE;

        for (Vec2D point : points) {
            if (point.getX() < x1)
                x1 = (int) point.getX();
            if (point.getY() < y1)
                y1 = (int) point.getY();
            if (point.getX() > x2)
                x2 = (int) point.getX();
            if (point.getY() > y2)
                y2 = (int) point.getY();
        }

        return new BoundingBox((int) getPosition().getX(), (int) getPosition().getY(), x2 - x1, y2
                - y1);
    }

    /*
     * @see lab2.shapes.core.Shape#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        g.setColor(getColor());
        GeneralPath p = new GeneralPath();

        Iterator<Vec2D> it = points.iterator();

        Vec2D point;
        point = it.next();

        p.moveTo(point.getX() + getPosition().getX(), point.getY() + getPosition().getY()); // Start
                                                                                            // point
                                                                                            // of
                                                                                            // the
                                                                                            // polytope.

        while (it.hasNext()) { // Create the outline of the polytope.
            point = it.next();
            p.lineTo(point.getX() + getPosition().getX(), point.getY() + getPosition().getY());
        }

        p.closePath(); // Close the polytope so it can be filled.

        Graphics2D g2d = (Graphics2D) g;
        g2d.fill(p); // Fill the polytope with it's color.
    }
}
