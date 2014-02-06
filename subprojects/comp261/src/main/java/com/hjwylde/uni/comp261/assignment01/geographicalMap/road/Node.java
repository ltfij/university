package com.hjwylde.uni.comp261.assignment01.geographicalMap.road;

import java.awt.geom.Point2D;

/*
 * Code for Assignment 1, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Node {
    
    private final int nID;
    
    private Point2D pos;
    
    public Node(int nID, Point2D pos) {
        if (pos == null)
            throw new NullPointerException();
        
        this.nID = nID;
        
        this.pos = pos;
    }
    
    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        
        if ((obj == null) || !(obj instanceof Node))
            return false;
        
        Node other = (Node) obj;
        if (nID != other.nID)
            return false;
        if (pos == null) {
            if (other.pos != null)
                return false;
        } else if (!pos.equals(other.pos))
            return false;
        return true;
    }
    
    public int getNID() {
        return nID;
    }
    
    public Point2D getPosition() {
        return pos;
    }
    
    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        
        result = (prime * result) + nID;
        result = (prime * result) + ((pos == null) ? 0 : pos.hashCode());
        
        return result;
    }
    
    public void setPosition(Point2D pos) {
        if (pos == null)
            throw new NullPointerException();
        
        this.pos = pos;
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Node [nID=" + nID + ", pos=" + pos + "]";
    }
}
