package com.hjwylde.uni.swen221.assignment02.walker;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import maze.Direction;
import maze.View;
import maze.Walker;

/*
 * Code for Assignment 2, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A keywalker is an implementation of a <code>Walker</code>. It allows the user to use keyboard
 * input to solve a maze. Each key press of the user is consumed after having been used to make a
 * move on the maze. The keypresses supported are the arrow keys and the 'w', 'a', 's' and 'd' keys
 * to move the walker in the directions 'NORTH', 'WEST', 'SOUTH' and 'EAST' respectively.
 * 
 * @author Henry J. Wylde
 */
public class KeyWalker extends Walker implements KeyListener {
    
    /**
     * Maps keycodes to directions. Allows for multiple keyboard inputs to specify the direction of
     * the walker.
     */
    private Map<Integer, Direction> inputMap = new HashMap<>();
    
    /**
     * Keeps track of the last pressed (but not consumed) key.
     */
    private int keyPressed = -1;
    
    /**
     * Creates a new KeyWalker with the class as it's name.
     */
    public KeyWalker() {
        this(KeyWalker.class.getName());
    }
    
    /**
     * Creates a new KeyWalker with the specified name.
     * 
     * @param name the name of the KeyWalker.
     */
    public KeyWalker(String name) {
        super(name);
        
        // Add in the key inputs supported.
        inputMap.put(KeyEvent.VK_UP, Direction.NORTH);
        inputMap.put(KeyEvent.VK_RIGHT, Direction.EAST);
        inputMap.put(KeyEvent.VK_DOWN, Direction.SOUTH);
        inputMap.put(KeyEvent.VK_LEFT, Direction.WEST);
        inputMap.put(KeyEvent.VK_W, Direction.NORTH);
        inputMap.put(KeyEvent.VK_D, Direction.EAST);
        inputMap.put(KeyEvent.VK_S, Direction.SOUTH);
        inputMap.put(KeyEvent.VK_A, Direction.WEST);
    }
    
    /*
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if (inputMap.containsKey(keyCode)) // If the key map contains the key that was pressed...
            keyPressed = keyCode; // ...set the current keyPressed.
    }
    
    /*
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {}
    
    /*
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent e) {}
    
    /*
     * @see maze.Walker#move(maze.View)
     */
    @Override
    protected Direction move(View view) {
        if (keyPressed == -1) // If a key hasn't been pressed...
            return null;
        
        Direction next = inputMap.get(keyPressed); // Get the direction the key corresponds to.
        keyPressed = -1; // Consume the key press.
        
        return next;
    }
    
}