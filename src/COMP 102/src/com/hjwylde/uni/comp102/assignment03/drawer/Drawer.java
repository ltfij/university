package com.hjwylde.uni.comp102.assignment03.drawer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.hjwylde.uni.comp102.util.DrawingCanvas;

/*
 * Code for Assignment 3, COMP 102
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Draws a bus (or some other simple object) at several places on a window
 */
public class Drawer {
    
    // GUI.
    private final JFrame frame = new JFrame("Drawer");
    private final DrawingCanvas canvas = new DrawingCanvas();
    
    private final ArrayList<Bus> busses = new ArrayList<>();
    
    public Drawer() {
        initGUI();
        
        busses.add(new Bus(100, 100, Color.YELLOW));
        busses.add(new Bus(300, 200, Color.RED));
        busses.get(1).setDirection(-1);
        
        drawObjects();
    }
    
    /** Sets up the window and draws three Buses */
    private void drawObjects() {
        for (Bus bus : busses)
            bus.paint(canvas);
    }
    
    private void initGUI() {
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        new Drawer();
    }
}
