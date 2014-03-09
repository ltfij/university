package com.hjwylde.uni.comp261.assignment04.parser.nodes;

import java.util.ArrayList;
import java.util.List;

import com.hjwylde.uni.comp261.assignment04.graphics.polytopes.Polytope3d;

/*
 * Code for Assignment 4, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A non-terminal node representing a POLYTOPE3d.
 * 
 * @author Henry J. Wylde
 */
public class Polytope3dNode implements Node {

    private LightSourceNode light;

    private List<Polytope2dNode> polytopes;

    /**
     * Constructs a new blank <code>Polytope3dNode</code>.
     */
    public Polytope3dNode() {
        polytopes = new ArrayList<>();
    }

    /**
     * Add a <code>Polytope2dNode</code> to the current list of polytopes.
     * 
     * @param poly the Polytope2dNode
     * 
     * @see com.hjwylde.uni.comp261.assignment04.parser.nodes.Polytope2dNode
     */
    public void addPolytope2d(Polytope2dNode poly) {
        polytopes.add(poly);
    }

    /**
     * Creates an instance of this <code>Polytope3dNode</code>, returning a new
     * <code>Polytope3d</code>.
     * 
     * @return a new Polytope3d.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.graphics.polytopes.Polytope3d
     */
    public Polytope3d createInstance() {
        Polytope3d poly = new Polytope3d();

        for (Polytope2dNode polyNode : polytopes)
            poly.addFace(polyNode.createInstance());

        return poly;
    }

    /**
     * Get the light source that this <code>Polytope3dNode</code> had defined for it.
     * 
     * @return the <code>light</code>.
     */
    public LightSourceNode getLight() {
        return light;
    }

    /**
     * Set the light source that this <code>Polytope3dNode</code> has defined for it.
     * 
     * @param light the <code>light</code> to set.
     */
    public void setLight(LightSourceNode light) {
        this.light = light;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String str = light.toString();
        for (Polytope2dNode poly : polytopes)
            str += "\n" + poly;

        return str;
    }
}
