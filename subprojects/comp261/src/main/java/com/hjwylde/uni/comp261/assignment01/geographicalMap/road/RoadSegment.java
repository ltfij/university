package com.hjwylde.uni.comp261.assignment01.geographicalMap.road;

/*
 * Code for Assignment 1, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class RoadSegment {

    private static int count = 0;

    private final int rsID;

    private final int rID;

    private final int startNode;
    private final int endNode;

    private final double length;

    public RoadSegment(int rID, double length, int startNode, int endNode) {
        rsID = RoadSegment.count++;

        this.rID = rID;

        this.startNode = startNode;
        this.endNode = endNode;

        this.length = length;
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if ((obj == null) || !(obj instanceof RoadSegment))
            return false;

        RoadSegment other = (RoadSegment) obj;
        if (endNode != other.endNode)
            return false;
        if (Double.doubleToLongBits(length) != Double.doubleToLongBits(other.length))
            return false;
        if (rID != other.rID)
            return false;
        if (rsID != other.rsID)
            return false;
        if (startNode != other.startNode)
            return false;

        return true;
    }

    /**
     * @return the <code>endNode</code>
     */
    public int getEndNode() {
        return endNode;
    }

    /**
     * @return the <code>length</code>
     */
    public double getLength() {
        return length;
    }

    /**
     * @return the <code>rID</code>
     */
    public int getRID() {
        return rID;
    }

    /**
     * @return the <code>rsID</code>
     */
    public int getRSID() {
        return rsID;
    }

    /**
     * @return the <code>startNode</code>
     */
    public int getStartNode() {
        return startNode;
    }

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;

        int result = 1;
        result = (prime * result) + endNode;
        long temp;
        temp = Double.doubleToLongBits(length);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        result = (prime * result) + rID;
        result = (prime * result) + rsID;
        result = (prime * result) + startNode;

        return result;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RoadSegment [rsID=" + rsID + ", rID=" + rID + ", startNode=" + startNode
                + ", endNode=" + endNode + ", length=" + length + "]";
    }
}
