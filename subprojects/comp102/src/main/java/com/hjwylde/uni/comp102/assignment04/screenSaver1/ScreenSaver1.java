package com.hjwylde.uni.comp102.assignment04.screenSaver1;

/*
 * Code for Assignment 4, COMP 102 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.hjwylde.uni.comp102.util.DrawingCanvas;

/**
 * A simple "screensaver" that draws lots of repetitive patterns on the screen.
 */
public class ScreenSaver1 {

    /*
     * Draw a pattern involving repetition. Parameters are the top left corner and the size.
     */
    public void drawGeometricPattern(int left, int top, int size, DrawingCanvas canvas) {
        int side = size / 3;

        canvas.fillRect(left + side, top + side, side - 1, side - 1);

        if (side >= 3) {
            drawGeometricPattern(left, top, side, canvas);
            drawGeometricPattern(left + side, top, side, canvas);
            drawGeometricPattern(left + (2 * side), top, side, canvas);
            drawGeometricPattern(left, top + side, side, canvas);
            drawGeometricPattern(left, top + (2 * side), side, canvas);
            drawGeometricPattern(left + (2 * side), top + side, side, canvas);
            drawGeometricPattern(left + side, top + (2 * side), side, canvas);
            drawGeometricPattern(left + (2 * side), top + (2 * side), side, canvas);
        }
    }

    /*
     * Creates a window and then draws lots of squares on it. When the window is full, it clears
     * them, and draws them again. It is easiest to let it loop forever, but this makes it difficult
     * to stop the program when you are tired of it!
     */
    public void drawScreenSaver() {
        JFrame frame = new JFrame("ScreenSaver1");
        frame.setSize(600, 600);
        DrawingCanvas canvas = new DrawingCanvas();
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        frame.setVisible(true);

        drawGeometricPattern(0, 0, 600, canvas);
    }

    /*
     * main method. This allows the program to be run without using Bluej to call the methods
     * directly. From Bluej, you can call the main method directly on the class. From a shell (a
     * command window) you can call "java ScreenSaver1" as long as you are in the right directory.
     * It also makes it possible to can create an executable jar file from Bluej that can be run by
     * clicking on it"
     */
    public static void main(String[] arguments) {
        ScreenSaver1 ss = new ScreenSaver1();
        ss.drawScreenSaver();
    }

}
