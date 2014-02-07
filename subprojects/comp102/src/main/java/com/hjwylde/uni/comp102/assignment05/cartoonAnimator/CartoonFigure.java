package com.hjwylde.uni.comp102.assignment05.cartoonAnimator;

/*
 * Code for Assignment 5, COMP 102 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

import java.io.File;

import com.hjwylde.uni.comp102.util.DrawingCanvas;

/**
 * A CartoonFigure object is a cartoon figure, displayed on the screen that can move around, change
 * the direction the figure is facing, change its emotion (smiling or frowning) speak a phrase
 */

public class CartoonFigure {

    /* the window that the CartoonFigure is drawn in. */
    private final DrawingCanvas canvas;

    /* fields representing the state of a CartoonFigure */
    private final String imageName;
    private int figureX;
    private final int figureY;
    private String direction = "right";
    private String emotion = "smile";
    private boolean entered = false;

    /* fields containing dimensions of CartoonFigures */

    private final int figureHeight = 100;
    private final int figureWidth = 70;

    private final int bubbleWidth = 140;
    private final int bubbleHeight = 35;

    /**
     * Constructor requires the DrawingCanvas that the figure will be drawn on, the name of the set
     * of the images, and the coordinates (left, top) of where it should be placed
     */
    public CartoonFigure(DrawingCanvas c, String image, int x, int y) {
        this(c, image, x, y, false);
    }

    /**
     * Constructor requires the DrawingCanvas that the figure will be drawn on, the name of the set
     * of the images, and the coordinates (left, top) of where it should be placed
     */
    public CartoonFigure(DrawingCanvas c, String image, int x, int y, boolean delay) {
        canvas = c;
        figureX = x;
        figureY = y;
        imageName = image;

        if (!delay) {
            entered = true;
            draw();
        }
    }

    /** makes the CartoonFigure enter the world */
    public void enterWorld() {
        entered = true;
        draw();
    }

    /** makes the CartoonFigure frown */
    public void frown() {
        erase();
        emotion = "frown";
        draw();
    }

    /**
     * Returns whether this figure has been entered into the world.
     */
    public boolean hasEntered() {
        return entered;
    }

    /** move the CartoonFigure in the direction it is facing */
    public void move(int dist) {
        erase();
        if (direction.equals("right"))
            figureX = figureX + dist;
        else
            figureX = figureX - dist;
        draw();
    }

    /** makes the CartoonFigure smile */
    public void smile() {
        erase();
        emotion = "smile";
        draw();
    }

    /** makes the CartoonFigure say something in a speech bubble */
    public void talk(String words) {
        int bubbleX = figureX;
        int bubbleY = figureY - bubbleHeight - 2;

        if (direction.equals("right"))
            bubbleX += 15;
        else
            bubbleX += figureWidth - 15 - bubbleWidth;

        canvas.drawOval(bubbleX, bubbleY, bubbleWidth, bubbleHeight, false);
        canvas.drawString(words, bubbleX + 12, bubbleY + (bubbleHeight / 2) + 3, false);
        canvas.display();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        canvas.clearRect(bubbleX, bubbleY, bubbleWidth + 1, bubbleHeight + 1, false);
    }

    /** makes the CartoonFigure turn to the left */
    public void turnLeft() {
        erase();
        direction = "left";
        draw();
    }

    /** makes the CartoonFigure turn to the right */
    public void turnRight() {
        erase();
        direction = "right";
        draw();
    }

    /**
     * Helper method that draws the CartoonFigure All the public methods that change the figure call
     * draw.
     */

    private void draw() {
        String filename = imageName + "-" + direction + "-" + emotion;
        if (new File(filename + ".png").exists())
            filename = filename + ".png";
        else
            filename = filename + ".jpg";
        canvas.drawImage(filename, figureX, figureY, figureWidth, figureHeight, false);
        canvas.display();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Helper method that erases the CartoonFigure All the public methods that change the figure
     * call erase first
     */

    private void erase() {
        canvas.clearRect(figureX, figureY, figureWidth + 1, figureHeight + 1, false);
    }

}
