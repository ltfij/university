package com.hjwylde.uni.comp102.assignment06.circles;

/*
 * Code for Assignment 6, COMP 102 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.hjwylde.uni.comp102.util.DrawingCanvas;

/**
 * The Circles program is a very simple program that lets the user place small and large circles on
 * the canvas using the mouse. It has four buttons: Small, Large, Clear, and Quit. The Small button
 * should change the "current size" to 10, and the Large button should change the current size to
 * 30. The Clear button should clear the canvas. When the user releases the mouse at any point on
 * the canvas, the program should draw a blue circle at that point of the current size. Note that
 * the Circles class must implement both the ActionListener and the MouseListener interfaces, and
 * must also have the actionPerformed method and all the mouse.... methods.
 */

public class Circles implements MouseListener, ActionListener {

    private final JFrame frame = new JFrame("Circles");
    private final DrawingCanvas canvas = new DrawingCanvas();

    private int circleRadius = 10;

    /** Construct a new Circles object and set up the GUI */
    public Circles() {
        frame.setSize(600, 400);
        frame.getContentPane().add(canvas, BorderLayout.CENTER);

        canvas.addMouseListener(this);

        JPanel buttonPanel = new JPanel();
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        addButton(buttonPanel, "Set Small");
        addButton(buttonPanel, "Set Large");
        addButton(buttonPanel, "Clear Canvas");
        addButton(buttonPanel, "Quit");

        frame.setVisible(true);
        canvas.setColor(Color.blue);
    }

    /**
     * Respond to button presses the Large button should set the current size to 30 the Small button
     * should set the current size to 10 the Clear button should clear the canvas. the Quit button
     * should dispose of the window. Note that Large and Small do not draw anything!!
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if (action.equals("Set Small"))
            circleRadius = 10;
        else if (action.equals("Set Large"))
            circleRadius = 30;
        else if (action.equals("Clear Canvas"))
            canvas.clear();
        else if (action.equals("Quit"))
            frame.dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    /**
     * Respond to mouse events. This program only does something on mouseReleased - draws a circle
     * of the current size at the position the mouse was released
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        canvas.fillOval(x - (circleRadius / 2), y - (circleRadius / 2), circleRadius, circleRadius);
    }

    /** Helper method for adding buttons */
    private JButton addButton(JPanel panel, String name) {
        JButton button = new JButton(name);
        button.addActionListener(this);
        panel.add(button);
        return button;
    }

    public static void main(String[] arguments) {
        new Circles();
    }

}
