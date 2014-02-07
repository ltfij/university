package com.hjwylde.uni.comp261.assignment04.parser.nodes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import com.hjwylde.uni.comp261.assignment04.graphics.polytopes.Polytope2d;

/*
 * Code for Assignment 4, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A non-terminal node representing a POLYTOPE2D.
 * 
 * @author Henry J. Wylde
 */
public class Polytope2dNode implements Node {

    private List<Vector3d> vertices;

    private Color color;

    /**
     * Constructs a new blank <code>Polytope2dNode</code>.
     */
    public Polytope2dNode() {
        vertices = new ArrayList<>();
    }

    /**
     * Add a vertex to this <code>Polytope2dNode</code>.
     * 
     * @param vertex the vertex to add.
     */
    public void addVector3d(Vector3d vertex) {
        vertices.add(vertex);
    }

    /**
     * Creates a new instance of this <code>Polytope2dNode</code>, returning a new
     * <code>Polytope2d</code>.
     * 
     * @return a new Polytope2d.
     * 
     * @see Polytope2d
     */
    public Polytope2d createInstance() {
        return new Polytope2d(color, vertices);
    }

    /**
     * Set the color of this <code>Polytope2dNode</code>.
     * 
     * @param color the <code>color</code> to set.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String str = "{{";
        for (int i = 0; i < vertices.size(); i++)
            str += vertices.get(i);

        return str + "}, {" + color + "}}";
    }
}
