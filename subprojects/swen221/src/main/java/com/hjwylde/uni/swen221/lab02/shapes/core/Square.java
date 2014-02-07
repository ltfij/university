package com.hjwylde.uni.swen221.lab02.shapes.core;

import java.awt.Color;
import java.awt.Graphics;

import com.hjwylde.uni.swen221.lab02.shapes.math.BoundingBox;
import com.hjwylde.uni.swen221.lab02.shapes.math.Vec2D;

/*
 * Code for Laboratory 2, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Represents a simple square on the display.
 * 
 * @author David J. Pearce
 * 
 */
public class Square extends AbstractShape {

    private int width;

    public Square(int width, Vec2D position, Vec2D velocity, Color color) {
        super(position, velocity, color);
        this.width = width;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox((int) getPosition().getX(), (int) getPosition().getY(), width, width);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(getColor());
        g.fillRect((int) getPosition().getX(), (int) getPosition().getY(), width, width);
    }
}
