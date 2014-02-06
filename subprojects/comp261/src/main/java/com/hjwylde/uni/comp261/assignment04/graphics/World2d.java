package com.hjwylde.uni.comp261.assignment04.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import com.hjwylde.uni.comp261.assignment04.parser.ParseException;

/*
 * Code for Assignment 4, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * The <code>World2d</code> class represents a 2d world that the <code>World3d</code> may render a
 * 2d image to. It implements a runnable method to constantly redraw the world and update the world.
 * Also provides a few "in-between" methods that go from the <code>MainFrame</code> to the
 * <code>World3d</code>.
 * 
 * @author Henry J. Wylde
 * 
 * @see com.hjwylde.uni.comp261.assignment04.graphics.World3d
 * @see com.hjwylde.uni.comp261.assignment04.modeller.MainFrame
 */
public class World2d extends JPanel implements Runnable {
    
    private static final long serialVersionUID = -3402167549528875745L;
    
    private World3d world;
    private Color bgColor = Color.black;
    
    boolean isRunning = true;
    
    /**
     * Create a new <code>World2d</code>.
     */
    public World2d() {
        world = new World3d();
    }
    
    /**
     * Adds listener interfaces to this component and the specified component.
     * 
     * @param comp the component to register listeners for.
     */
    public void initListeners(Component comp) {
        addKeyListener(world);
        comp.addKeyListener(world);
    }
    
    /**
     * Load a <code>Polytope</code> from the given <code>File</code>.
     * 
     * @param f the File of the Polytope.
     * 
     * @throws ParseException if the grammar in the File is wrong.
     * @throws IOException if reading the File throws an error.
     */
    public void loadPolytope(File f) throws ParseException, IOException {
        world.loadPolytope(f);
    }
    
    /**
     * Renders the <code>World2d</code> with the image generated from the <code>World3d</code>. Sets
     * the backgroudn to be <code>bgColor</code>.
     */
    @Override
    public void paint(Graphics window) {
        Image bufferedImage = new BufferedImage(getWidth() + 20,
            getHeight() + 20, BufferedImage.TYPE_INT_RGB); // + 20 because for some reason it missed
                                                           // out rendering the
                                                           // sides of the JPanel.
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics(); // For double buffering.
        
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth() + 20, getHeight() + 20);
        
        world.render(g); // Render the 3d world onto this 2d world!
        
        g.dispose();
        
        window.drawImage(bufferedImage, 0, 0, this); // Display the image.
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    /**
     * Run method that continually updates this <code>World2d</code>.
     */
    @Override
    public void run() {
        long curTime = System.currentTimeMillis();
        long elapsedTime;
        
        while (isRunning) {
            elapsedTime = System.currentTimeMillis() - curTime;
            curTime = System.currentTimeMillis();
            
            update(elapsedTime);
        }
    }
    
    /**
     * Sets the view angle of the <code>World3d</code> in radians.
     * 
     * @param angle the new angle in radians.
     */
    public void setViewAngle(double angle) {
        world.setViewAngle(angle);
    }
    
    /**
     * Stops the world from running.
     */
    public void stop() {
        isRunning = false;
    }
    
    /**
     * Update the <code>World3d</code>s width and height then redraws this world.
     * 
     * @param elapsedTime
     */
    private void update(long elapsedTime) {
        world.setWidth(getWidth() + 20);
        world.setHeight(getHeight() + 20);
        
        world.update(elapsedTime);
        
        repaint();
    }
}