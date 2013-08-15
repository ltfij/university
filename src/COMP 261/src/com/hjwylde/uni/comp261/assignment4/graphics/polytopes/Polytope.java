package com.hjwylde.uni.comp261.assignment4.graphics.polytopes;

import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

import com.hjwylde.uni.comp261.assignment4.graphics.ScanConverter;
import com.hjwylde.uni.comp261.assignment4.graphics.ViewPlane;
import com.hjwylde.uni.comp261.assignment4.graphics.ZBuffer;

/*
 * Code for Assignment 4, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A generic <code>Polytope</code> class that defines basic methods that must be used for mostly all
 * <code>Polytope</code>s, with the exception being for some 2d <code>Polytope</code>s.
 * 
 * Note:
 * A 2d <code>Polytope</code> is also referred to as a Polygon or a Face.
 * 
 * @author Henry J. Wylde
 */
public interface Polytope extends Cloneable {
    
    /**
     * Add and apply the rotation matrix.
     * 
     * @param m the rotation matrix
     */
    public void add(Matrix3d m);
    
    /**
     * Add and apply the rotation and translation matrix.
     * 
     * @param m the rotation and translation matrix.
     */
    public void add(Matrix4d m);
    
    /**
     * Add and apply the translation vector.
     * 
     * @param v the translation vector.
     */
    public void add(Vector3d v);
    
    /**
     * Clips this <code>Polytope</code> and any sub <code>Polytope</code>s at the
     * <code>z = clipZ</code> plane. This is used to remove <code>Polytope</code>s that are behind
     * the
     * camera as the camera is looking down the z-axis. Safer to use a value that is 1 pixel away
     * from
     * the camera such as -1.0 rather than 0.0. Returns true if the <code>Polytope</code> is visible
     * after clipping.
     * 
     * @param clipZ the z value of the clip plane.
     * @return true if this Polytope is visible after clipping.
     */
    public boolean clip(double clipZ);
    
    /**
     * Creates a hard copy clone this <code>Polytope</code>.
     * 
     * @return the clone.
     */
    public Object clone();
    
    /**
     * Gets the transform for this <code>Polytope</code>.
     * 
     * @return the transform.
     */
    public Matrix4d getTransform();
    
    /**
     * Project this <code>Polytope</code> to the <code>ViewPlane</code>. This applies perspective
     * transformations without altering the z value of any vertices.
     * 
     * @param view the ViewPlane to project to.
     * 
     * @see com.hjwylde.uni.comp261.assignment4.graphics.ViewPlane
     */
    public void project(ViewPlane view);
    
    /**
     * Renders this <code>Polytope</code> onto the given <code>ZBuffer</code> using the given
     * <code>ScanConverter</code>. The <code>ScanConverter</code> will linearly interpolate the
     * <code>Polytope</code> into horizontal lines with depth and shade values, while the
     * <code>ZBuffer</code> will render the <code>Polytope</code> only if it is the closest to the
     * camera.
     * 
     * Note:
     * The <code>ScanConverter</code> holds the data for the <code>World3d</code>s light sources and
     * <code>ViewPlane</code>.
     * 
     * @param z the ZBuffer.
     * @param sc the ScanConverter.
     * 
     * @see com.hjwylde.uni.comp261.assignment4.graphics.ScanConverter
     * @see com.hjwylde.uni.comp261.assignment4.graphics.ViewPlane
     * @see com.hjwylde.uni.comp261.assignment4.graphics.World3d
     * @see com.hjwylde.uni.comp261.assignment4.graphics.ZBuffer
     */
    public void render(ZBuffer z, ScanConverter sc);
    
    /**
     * Sets this <code>Polytope</code>s transform matrix to the given matrix.
     * 
     * @param m the transform matrix.
     */
    public void setTransform(Matrix4d m);
    
    /**
     * Subtract and apply the given rotation matrix. Subtracting a rotation matrix involves
     * transposing it then adding it.
     * 
     * Note:
     * For rotation matrices transposing and inverting are the same thing.
     * 
     * @param m the rotation matrix.
     */
    public void sub(Matrix3d m);
    
    /**
     * Subtract and apply the given rotation and translation matrix. Subtracting a matrix involves
     * transposing / inverting it and applying the inverted translation before the transposed
     * rotation.
     * 
     * @param m the rotation and translation matrix.
     */
    public void sub(Matrix4d m);
    
    /**
     * Subtract and apply the translation vector.
     * 
     * @param v the translation vector.
     */
    public void sub(Vector3d v);
    
    /**
     * Apply the <code>Polytope</code>s own transform to itself and all sub <code>Polytope</code>s,
     * while also recursively calling <code>transform()</code> on sub <code>Polytope</code>s too.
     */
    public void transform();
    
    /**
     * Checks whether this <code>Polytope</code> is valid. This just checks whether there is some
     * sort
     * of visible 3d shape.
     * 
     * @return true if the Polytope is visible.
     */
    public boolean valid();
}