package com.hjwylde.uni.comp261.assignment01.geographicalMap.road;

/*
 * Code for Assignment 1, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class RoadRestriction {
    
    private final int fromNID;
    private final int fromRID;
    private final int midNID;
    private final int toNID;
    private final int toRID;
    
    public RoadRestriction(int fromNID, int fromRID, int midNID, int toRID,
        int toNID) {
        this.fromNID = fromNID;
        this.fromRID = fromRID;
        this.midNID = midNID;
        this.toNID = toNID;
        this.toRID = toRID;
    }
    
    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        
        if ((obj == null) || !(obj instanceof RoadRestriction))
            return false;
        
        RoadRestriction other = (RoadRestriction) obj;
        if (fromNID != other.fromNID)
            return false;
        if (fromRID != other.fromRID)
            return false;
        if (midNID != other.midNID)
            return false;
        if (toNID != other.toNID)
            return false;
        if (toRID != other.toRID)
            return false;
        
        return true;
    }
    
    /**
     * @return the <code>fromNID</code>.
     */
    public int getFromNID() {
        return fromNID;
    }
    
    /**
     * @return the <code>fromRID</code>.
     */
    public int getFromRID() {
        return fromRID;
    }
    
    /**
     * @return the <code>midNID</code>.
     */
    public int getMidNID() {
        return midNID;
    }
    
    /**
     * @return the <code>toNID</code>.
     */
    public int getToNID() {
        return toNID;
    }
    
    /**
     * @return the <code>toRID</code>.
     */
    public int getToRID() {
        return toRID;
    }
    
    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        
        int result = 1;
        result = (prime * result) + fromNID;
        result = (prime * result) + fromRID;
        result = (prime * result) + midNID;
        result = (prime * result) + toNID;
        result = (prime * result) + toRID;
        
        return result;
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RoadRestriction [fromNID=" + fromNID + ", fromRID=" + fromRID
            + ", midNID=" + midNID + ", toNID=" + toNID + ", toRID=" + toRID
            + "]";
    }
}
