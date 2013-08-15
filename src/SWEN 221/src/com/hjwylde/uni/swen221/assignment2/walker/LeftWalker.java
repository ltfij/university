package com.hjwylde.uni.swen221.assignment2.walker;

import java.awt.Point;

import maze.Direction;
import maze.View;
import maze.Walker;

import com.hjwylde.uni.swen221.assignment2.helper.DirectionHelper;

/*
 * Code for Assignment 2, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * The LeftWalker uses a "hug the left wall" algorithm with a fix for some special cases, but does
 * not account for all tricky cases and can get into infinite loops sometimes. It will continually
 * try to follow the left wall. When it encounters a point that it has passed through in the same
 * direction before, it will turn on a opposite mode and hug the right walls until it encounters a
 * point it hasn't come across before.
 * 
 * @author Henry J. Wylde
 */
public class LeftWalker extends Walker {
    
    private static final Point INITIAL_POINT = new Point(0, 0);
    
    /**
     * Keeps track of where the walker has been.
     */
    private BallOfString path = new BallOfString();
    private boolean oppositeMode = false;
    
    private boolean beginning = true;
    
    /**
     * Creates a new LeftWalker with the class as its name.
     */
    public LeftWalker() {
        this(LeftWalker.class.getName());
    }
    
    /**
     * Creates a new LeftWalker with the specified name.
     * 
     * @param name the name of the walker.
     */
    public LeftWalker(String name) {
        super(name);
    }
    
    /*
     * @see maze.Walker#move(maze.View)
     */
    @Override
    protected Direction move(View view) {
        Direction dir = path.getDirection(); // Get the current direction.
        
        // Check if we're encountering a point that we've come across before
        if (path.numberOfPasses(path.getEnd(1), path.getEnd()) > 1)
            oppositeMode = true;
        else if (oppositeMode) { // Only turn oppositeMode off if we were already in it...
            oppositeMode = false;
            
            // ...because of this:
            dir = DirectionHelper.clockwise(dir); // A correction to ensure that after leaving
                                                  // oppositeMode we follow the correct left wall.
        }
        
        // Initialisation of the walker (these if statements occur when we're still trying to find
        // the
        // first wall)
        // if (dir == null) {
        if (beginning) {
            // Try find a wall around us.
            Direction wall = null;
            for (Direction d : Direction.values())
                if (!view.mayMove(d))
                    wall = d;
            
            if (wall == null) // Head North, but don't change "beginning": we need to keep going
                              // through
                              // this condition until we find a start wall.
                dir = Direction.NORTH;
            else {
                dir = DirectionHelper.clockwise(wall); // Set the direction to put the wall on the
                                                       // walkers
                                                       // left.
                beginning = false;
            }
            
            // Check for setting up the initial values of the path ball of string variable.
            // This adds in a INITIAL_POINT to the path, and also a point just before it so as to
            // allow
            // the INITIAL_POINT to have a direction specified with it.
            // This is needed so that the BallOfString can properly count the INITIAL_POINT as a
            // passed
            // point when using the methods such as numbeOfPasses(Point to, Point from).
            if (path.length() == 0) {
                // Direction we'll want to simulate having moved to INITIAL_POINT in is "dir", so
                // get the
                // point that is "opposite(dir)".
                path.lineTo(BallOfString.getNextPoint(LeftWalker.INITIAL_POINT,
                    DirectionHelper.opposite(dir)));
                
                path.lineTo(LeftWalker.INITIAL_POINT);
            }
        } else if (!oppositeMode) { // Hug left wall mode!
            while (!view.mayMove(DirectionHelper.antiClockwise(dir)))
                // While we can't move in an anti-clockwise direction...
                dir = DirectionHelper.clockwise(dir); // Turn clockwise.
                
            dir = DirectionHelper.antiClockwise(dir); // Correct the direction, when we get out of
                                                      // the
                                                      // loop it means we can move in direction
                                                      // antiClockwise(dir).
        } else { // Hug right wall mode!
            while (!view.mayMove(DirectionHelper.clockwise(dir)))
                // While we can't move in a clockwise direction...
                dir = DirectionHelper.antiClockwise(dir); // Turn anti-clockwise.
                
            dir = DirectionHelper.clockwise(dir); // Correct the direction, when we get out of the
                                                  // loop it
                                                  // means we can move in direction clockwise(dir).
        }
        
        path.lineIn(dir); // Record / move to the direction found.
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return path.getDirection();
    }
}