package com.hjwylde.uni.swen221.lab02.shapes.viewer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import com.hjwylde.uni.swen221.lab02.shapes.core.Shape;

/*
 * Code for Laboratory 2, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class BoardCanvas extends Canvas {

    private static final long serialVersionUID = 1L;

    private final ArrayList<Shape> shapes;

    private Image offscreen = null;

    public BoardCanvas(ArrayList<Shape> shapes) {
        this.shapes = shapes;
        setBounds(0, 0, 400, 400);
    }

    @Override
    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        for (Shape s : shapes)
            s.paint(g);
    }

    @Override
    public void update(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        if ((offscreen == null) || (offscreen.getWidth(this) != width)
                || (offscreen.getHeight(this) != height))
            offscreen = createImage(width, height);
        Image localOffscreen = offscreen;
        Graphics offgc = offscreen.getGraphics();
        // do normal redraw
        paint(offgc);
        // transfer offscreen to window
        g.drawImage(localOffscreen, 0, 0, this);
    }
}
