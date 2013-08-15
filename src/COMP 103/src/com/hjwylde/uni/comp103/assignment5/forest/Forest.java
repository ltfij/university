package com.hjwylde.uni.comp103.assignment5.forest;

/*
 * Code for Assignment 5, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.hjwylde.uni.comp103.util.DrawingCanvas;

public class Forest implements ActionListener {
    
    // Fields
    private final JFrame frame;
    private final DrawingCanvas canvas;
    
    // Constructor
    public Forest() {
        frame = new JFrame("Forest");
        frame.setSize(900, 600);
        
        // The graphics area
        canvas = new DrawingCanvas();
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        
        // The buttons
        JPanel buttonPanel = new JPanel();
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        
        addButton(buttonPanel, "Plant one");
        addButton(buttonPanel, "Show Growth");
        addButton(buttonPanel, "Clear Canvas");
        addButton(buttonPanel, "Quit");
        
        frame.setVisible(true);
    }
    
    // GUI Methods
    
    /** Respond to button presses */
    @Override
    public void actionPerformed(ActionEvent e) {
        String button = e.getActionCommand();
        if (button.equals("Quit"))
            frame.dispose();
        else if (button.equals("Plant one"))
            new Dandelion(canvas);
        else if (button.equals("Show Growth")) {
            Dandelion d = new Dandelion(canvas, 100.0,
                canvas.getHeight() - 50.0, false);
            d.showGrowth();
        } else if (button.equals("Clear Canvas"))
            canvas.clear();
    }
    
    /** Helper method for adding buttons */
    private JButton addButton(JPanel panel, String name) {
        JButton button = new JButton(name);
        button.addActionListener(this);
        panel.add(button);
        return button;
    }
    
    // Main
    public static void main(String[] arguments) {
        new Forest();
    }
}
