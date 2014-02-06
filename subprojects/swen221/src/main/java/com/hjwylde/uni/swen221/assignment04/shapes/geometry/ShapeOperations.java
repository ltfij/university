package com.hjwylde.uni.swen221.assignment04.shapes.geometry;

/*
 * Code for Assignment 4, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Defines some complex shape operations.
 * 
 * @author Henry J. Wylde
 */
public interface ShapeOperations {
    
    /**
     * Computes the difference between this Shape and the input Shape <code>s</code> and puts the
     * result back into this Shape. The difference is the points that are in this Shape but not in
     * the
     * input Shape.
     * 
     * @param s the Shape to compute the difference from.
     */
    public void difference(Shape s);
    
    /**
     * Computes the intersection between this Shape and the input Shape <code>s</code> and puts the
     * result back into this Shape. The intersection is the points that are in both this Shape and
     * in
     * the input Shape.
     * 
     * @param s the Shape to compute the intersection from.
     */
    public void intersect(Shape s);
    
    /**
     * Computes the union between this Shape and the input Shape <code>s</code> and puts the result
     * back into this Shape. The union is the points that are in either this Shape or in the input
     * Shape.
     * 
     * @param s the Shape to compute the union from.
     */
    public void union(Shape s);
    
}