package com.hjwylde.uni.swen221.lab04.robotwar;

import java.awt.Point;

/*
 * Code for Laboratory 4, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * The turfBot marks out its starting location and hangs around there
 * protecting it.
 * 
 * @author djp
 * 
 */
public class TurfBot extends Robot {
    
    public int turfRadius;
    public Point startPos;
    
    public TurfBot(Point pos) {
        this(pos, Robot.DEFAULT_STRENGTH);
    }
    
    public TurfBot(Point pos, int strength) {
        super(pos, strength);
        
        turfRadius = 5;
        startPos = (Point) pos.clone();
    }
    
    @Override
    public void move(int top, int right, int bottom, int left) {
        top = Math.min(top, startPos.y + turfRadius);
        right = Math.max(right, startPos.x - turfRadius);
        bottom = Math.max(bottom, startPos.y - turfRadius);
        left = Math.min(left, startPos.x + turfRadius);
        
        if ((getPos().x == left) && (getPos().y < top))
            getPos().translate(0, 1);
        else if ((getPos().x == right) && (getPos().y > bottom))
            getPos().translate(0, -1);
        else if ((getPos().y == top) && (getPos().x > right))
            getPos().translate(-1, 0);
        else
            getPos().translate(1, 0);
    }
}