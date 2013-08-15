package com.hjwylde.uni.comp261.assignment4.graphics.lights;

import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

/*
 * Code for Assignment 4, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A point light class that represents a light direction vector in 3d space. Has an optional falloff
 * distance variable that can be set to ensure this light is not a infinite distance one.
 * 
 * @author Henry J. Wylde
 */
public class PointLight3d extends Vector3d {
    
    private static final long serialVersionUID = 8665187782821492901L;
    
    public static final double NO_FALLOFF_DISTANCE = -1.0;
    
    private double intensity;
    
    private double falloffDistance;
    
    /**
     * Create a new <code>PointLight3d</code> at (0, 0, 0) with an intensity value of 1.0 and
     * infinite
     * distance.
     */
    public PointLight3d() {
        this(0, 0, 0, 1.0, PointLight3d.NO_FALLOFF_DISTANCE);
    }
    
    /**
     * Create a new <code>PointLight3d</code> at (x, y, z) with an intensity value of 1.0 and
     * infinite
     * distance.
     */
    public PointLight3d(double x, double y, double z) {
        this(x, y, z, 1.0, PointLight3d.NO_FALLOFF_DISTANCE);
    }
    
    /**
     * Create a new <code>PointLight3d</code> at (x, y, x) with the specified intensity value and
     * infinite
     * distance.
     */
    public PointLight3d(double x, double y, double z, double intensity) {
        this(x, y, z, intensity, PointLight3d.NO_FALLOFF_DISTANCE);
    }
    
    /**
     * Create a new <code>PointLight3d</code> at (x, y, x) with the specified intensity value and
     * the
     * specified falloff distance.
     */
    public PointLight3d(double x, double y, double z, double intensity,
        double falloffDistance) {
        set(x, y, z);
        
        this.intensity = intensity;
        this.falloffDistance = falloffDistance;
        
        normalize();
    }
    
    /**
     * Create a new <code>PointLight3d</code> at (t.x, t.y, t.z) with an intensity value of 1.0 and
     * infinite
     * distance.
     */
    public PointLight3d(Tuple3d t) {
        super(t);
        
        intensity = 1.0;
        falloffDistance = PointLight3d.NO_FALLOFF_DISTANCE;
        
        normalize();
    }
    
    /**
     * Add and apply the rotation and translation matrix.
     * 
     * @param m the rotation and translation matrix.
     */
    public void add(Matrix4d m) {
        Matrix3d rotation = new Matrix3d();
        m.get(rotation);
        rotation.transform(this);
        
        Vector3d translation = new Vector3d();
        m.get(translation);
        add(translation);
    }
    
    /*
     * @see javax.vecmath.Tuple3d#clone()
     */
    @Override
    public Object clone() {
        return (new PointLight3d(x, y, z, intensity, falloffDistance));
    }
    
    /**
     * Gets the intensity of this light. Same as calling <code>getIntensity(...)</code> if this
     * <code>PointLight3d</code> is an infinite distance one.
     * 
     * @return the <code>intensity</code>.
     */
    public double getIntensity() {
        return intensity;
    }
    
    /**
     * Calculates and returns the intensity of this <code>PointLight3d</code> at the given distance
     * away from it. If this <code>PointLight3d</code> has an infinite distance, then it will return
     * <code>intensity</code>.
     * 
     * @param distance the distance away from the light.
     * @return the intensity at the given distance away.
     */
    public double getIntensity(double distance) {
        if (falloffDistance == PointLight3d.NO_FALLOFF_DISTANCE) // If distance is irrelevant to
                                                                 // intensity...
            return intensity;
        
        if (distance >= falloffDistance) // Too far away! No light!
            return 0;
        
        return (intensity * (falloffDistance - distance))
            / (falloffDistance + distance);
    }
    
    /**
     * Set the falloff distance of this <code>PointLight3d</code>.
     * 
     * @param falloffDistance the <code>falloffDistance</code> to set.
     */
    public void setFalloffDistance(double falloffDistance) {
        this.falloffDistance = falloffDistance;
    }
    
    /**
     * Set the intensity of this <code>PointLight3d</code>.
     * 
     * @param intensity the <code>intensity</code> to set.
     */
    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }
    
    /**
     * Subtract and apply the given rotation and translation matrix. Subtracting a matrix involves
     * transposing / inverting it and applying the inverted translation before the transposed
     * rotation.
     * 
     * @param m the rotation and translation matrix.
     */
    public void sub(Matrix4d m) {
        Vector3d translation = new Vector3d();
        m.get(translation);
        sub(translation);
        
        Matrix3d rotation = new Matrix3d();
        m.get(rotation);
        rotation.transpose();
        rotation.transform(this);
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{{" + super.toString() + "}, {intensity: " + intensity
            + "}, {falloffDistance: " + falloffDistance + "}}";
    }
}