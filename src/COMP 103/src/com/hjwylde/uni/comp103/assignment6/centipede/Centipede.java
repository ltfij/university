package com.hjwylde.uni.comp103.assignment6.centipede;

/*
 * Code for Assignment 6, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import com.hjwylde.uni.comp103.util.DrawingCanvas;

/**
 * A Centipede is a sequence of segments, represented as a Linked list.
 * Centipede is a specialised
 * form of the LinkedNode class, so that a Centipede object contains a Segment
 * and a reference to
 * the next node. We can view a Centipede as just a single node in a list of
 * nodes, (in which case
 * it could have been called "CentipedeNode") or as the whole list of nodes,
 * represented by its
 * front node. A centipede can: - be created: - grow, adding another segment at
 * the end, - shrink,
 * losing the segment just behing the head, - move forward, with each segment
 * following the one
 * ahead of it, - jump, moving all the segments by the same distance, - split
 * itself in half,
 * returning the new centipede, - reverse its direction, returning the new head,
 * which was the tail
 */

public class Centipede implements Comparable<Centipede> {
    
    private final Segment segment; // the info about this segment of the centipede
    private Centipede next; // the next segment of the centipede
    
    // There are two constructors:
    
    /**
     * Constructor for a new centipede (viewed as a whole list of segments) . A
     * new centipede should
     * be just two segments long, be in a random position, have a random
     * direction, have a random
     * color (which changes along the segments - each segment is a different hue
     * from the previous
     * one)
     */
    
    public Centipede() {
        
        segment = new Segment(); // Make a head segment
        next = new Centipede(segment); // make and attach the second node of the
                                       // centipede
    }
    
    /**
     * Constructor for an element of a centipede. This node should be attached to
     * a previous element,
     * and the position, colour, and direction of segment in this node should
     * depend on the segment of
     * the segment in the previous node: - position is one radius over from the x
     * and y of the
     * previous segment (in the opposite direction from the prevDir), - moving in
     * the same direction
     * as the previous segment - color is the next shade through the spectrum from
     * the previous
     * segment
     */
    
    public Centipede(Segment prevSegment) {
        segment = new Segment(prevSegment);
        next = null;
    }
    
    @Override
    public int compareTo(Centipede c) {
        return size() - c.size();
    }
    
    public int getRadius() {
        return segment.getRadius();
    }
    
    /** Return the position of the head segment of the centipede */
    
    public int getX() {
        return segment.getX();
    }
    
    public int getY() {
        return segment.getY();
    }
    
    public void grow() {
        if (next == null) // If at end...
            next = new Centipede(segment); // ...then grow!
        else
            next.grow(); // ...else find end
    }
    
    public void jump(int dx, int dy) {
        segment.jump(dx, dy); // Move this segment
        
        if (next != null)
            next.jump(dx, dy); // Then move the next segment
    }
    
    /**
     * move: the centipede should move along, with each segment following the one
     * in front of it. That
     * means that each segment must move in the direction the previous segment
     * moved the last time.
     * The version of move with no arguments will work out a new direction to move
     * (usually, but not
     * always, the same as the current direction of the head) then call
     * move(newDirection). It will
     * turn if it is over the edge, or with a 1 in 10 probability. The version of
     * move with a new
     * direction should first tell the segment after it to move in this segment's
     * current direction,
     * then it should move itself in the new direction, and remember this new
     * direction. Note, move
     * doesn't cause it to redraw - it just changes the current position.
     */
    
    public void move() {
        Direction newDir = segment.getNewDirection();
        move(newDir);
    }
    
    public void move(Direction newDir) {
        Direction currentDir = segment.getDirection();
        
        if (next != null)
            next.move(currentDir); // Move end segments before head
            
        segment.setDirection(newDir); // Set the direction before moving it
        segment.move();
    }
    
    // HINT for all these methods: Check the methods on the Segment class!!!
    
    public boolean near(Centipede c) {
        return (segment.near(c.getX(), c.getY()));
    }
    
    /*
     * Jump will move the whole centipede over by (dx, dy) Should jump the first
     * segement, then jump
     * the rest.
     */
    
    /* true if the head of this centipede is near to a position */
    public boolean near(int x, int y) {
        return segment.near(x, y);
    }
    
    /*
     * Grow will add a new segment onto the end of the centipede It should have
     * the appropriate
     * position and direction.
     */
    
    /**
     * redraw should first redraw the rest of the centipede, then it should draw
     * this segment (since
     * the segments nearer the face should be drawn in front of segments further
     * from the face). Each
     * segment knows how to draw itself (as a colored circle, centered at its
     * position, and draw two
     * eyes just above its center if it is a face).
     */
    
    public void redraw(DrawingCanvas canvas) {
        if (next != null)
            next.redraw(canvas);
        
        segment.redraw(canvas);
    }
    
    /**
     * Reverse will reverse the centipede, turning the head into the tail and the
     * tail into the head,
     * reversing all the links along the way, and returning a reference to the new
     * head Note, it is
     * also necessary to change the direction of travel in each segment so that
     * the centipede will now
     * move exactly the opposite. This is TRICKY!
     */
    
    public Centipede reverse() {
        // Please Note:
        // This completion task was ambiguous. The above comments state
        // "reverse's the centipede, and all the links along the way" however the
        // code in
        // Simulation.java implied that the reverse() code in Centipede should only
        // create a new
        // reversed centipede, not change the old one:
        // "if(c.reverse() != null) {
        // toRemove.add(c);
        // toAdd.add(c.reverse());
        // }"
        // This calls reverse() twice, which muddles with the reversing code if the
        // centipede is
        // actually reversed itself. I have modified Simulation.java to fix this and
        // kept with reverse()
        // reversing the centipede, not creating a new one.
        
        Centipede reversed = reverse(null); // Reverses this centipede and returns a
                                            // reference to the
                                            // new head.
        segment.setFace(false); // Set this segment to not be a head any more.
        
        return (reversed);
    }
    
    /*
     * As long as there are at least two segments, shorten should remove the very
     * last segment from
     * the centipede (ie, the last node of the list
     */
    public void shorten() {
        if (next == null) // Note: Only occurs if this segment is a head due to size
                          // comparason
            return;
        
        if (size() == 2) // If this is the second to last segment...
            next = null; // ...then remove the last segment
        else
            next.shorten(); // ...else keep finding second to last segment
    }
    
    /**
     * As long as there are at least 3 segments in the caterpiller, shrink will
     * make the centipede
     * shorter by removing the second segment. It has to make all the rest of the
     * centipede move one
     * step to close up the gap, in the direction of the second segment (the one
     * being removed).
     */
    public void shrink() {
        if (size() >= 3) {
            next.move(segment.getDirection()); // Move all segments up one
            next = next.next; // Remove the second segment
        }
    }
    
    /** return the size (number of segments) of the centipede */
    public int size() {
        if (next == null)
            return 1;
        
        return next.size() + 1;
    }
    
    /*
     * As long as there are at least 4 segments in the centipede, split will break
     * the centipede in
     * half, and return the head of the second centipede (the one starting half
     * way down the existing
     * centipede)
     */
    public Centipede split() {
        int length = size();
        if (length >= 4) { // If there is 4 or more segments...
            int half = length / 2; // ...then split the centipede!
            
            Centipede centi = this; // Find the centipede in the middle
            while (half > 1) {
                centi = centi.next;
                half--;
            }
            
            Centipede splitCenti = centi.next; // Create a new centipede at the middle
                                               // of the original
                                               // centipede
            splitCenti.segment.setFace(true);
            centi.next = null; // Cut off the original centipede at the middle
            
            return splitCenti; // Return reference to the newly created centipede
        }
        
        return null;
    }
    
    private Centipede reverse(Centipede prevCenti) {
        segment.reverseDirection(); // Reverse the current segments direction.
        
        Direction prevDir;
        if (prevCenti != null) // If this centipede isn't the old head...
            prevDir = prevCenti.segment.getDirection(); // ...then the new direction will be the
                                                        // previous
                                                        // segments reversed direction.
        else
            prevDir = segment.getDirection(); // ...else the new direction will be the current
                                              // reversed
                                              // direction.
            
        if (next == null) { // If we are at the old tail...
            segment.setDirection(prevDir);
            segment.setFace(true); // ...then customize this segment to a head
                                   // segment.
            next = prevCenti;
            
            return this; // Return the new head.
        }
        
        // ...else reverse the next centipede before we change this segments direction and next.
        Centipede reversed = next.reverse(this);
        
        segment.setDirection(prevDir); // Change this segments direction and next
                                       // to the previous
                                       // centipede (so we are building the
                                       // centipede backwards).
        next = prevCenti;
        
        return (reversed); // Return a reference to the new head.
    }
    
    public static void main(String[] args) {
        Simulation.main(args);
    }
}