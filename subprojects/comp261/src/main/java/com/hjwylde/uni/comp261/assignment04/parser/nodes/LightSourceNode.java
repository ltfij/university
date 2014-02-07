package com.hjwylde.uni.comp261.assignment04.parser.nodes;

import com.hjwylde.uni.comp261.assignment04.graphics.lights.PointLight3d;

/*
 * Code for Assignment 4, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A non-terminal node representing a LIGHT_SOURCE.
 * 
 * @author Henry J. Wylde
 */
public class LightSourceNode implements Node {

    private PointLight3d src;

    /**
     * Constructs a <code>LightSourceNode</code> with the given <code>PointLight3d</code>.
     * 
     * @param src the PointLight3d.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.graphics.lights.PointLight3d
     */
    public LightSourceNode(PointLight3d src) {
        this.src = src;
    }

    /**
     * Returns an instance of this node, returning a <code>PointLight3d</code>.
     * 
     * @return a light source instance.
     */
    public PointLight3d createInstance() {
        return src;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return src.toString();
    }
}
