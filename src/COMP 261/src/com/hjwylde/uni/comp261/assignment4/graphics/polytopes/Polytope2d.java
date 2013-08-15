package com.hjwylde.uni.comp261.assignment4.graphics.polytopes;

import java.awt.Color;
import java.util.*;

import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

import com.hjwylde.uni.comp261.assignment4.graphics.*;
import com.hjwylde.uni.comp261.assignment4.graphics.ScanConverter.Scan;
import com.hjwylde.uni.comp261.assignment4.graphics.lights.PointLight3d;
import com.hjwylde.uni.comp261.assignment4.modeller.Main;

/*
 * Code for Assignment 4, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A <code>Polytope2d</code> represents a flat polygon or face in 3d space. It is used in
 * conjunction with the other <code>Polytope</code> classes to create 3d polytope shapes that may be
 * transformed and rendered onto a 2d screen.
 * 
 * @author Henry J. Wylde
 */
public class Polytope2d implements Polytope, Iterable<Vector3d> {
    
    /**
     * List of vertices in anti clockwise order.
     */
    private List<Vector3d> vertices;
    private Map<Vector3d, Vector3d> vertexNormals; // For Gouraud shading.
    
    private Color color = Color.WHITE; // Default color for a face.
    
    /**
     * Creates a new empty <code>Polytope2d</code>.
     */
    public Polytope2d() {
        vertices = new ArrayList<>();
        vertexNormals = new HashMap<>();
    }
    
    /**
     * Creates a new <code>Polytope2d</code> with vertices from the given <code>Collection</code>.
     * 
     * @param c the collection of vertices.
     */
    public Polytope2d(Collection<Vector3d> c) {
        vertices = new ArrayList<>(c);
        vertexNormals = new HashMap<>();
    }
    
    /**
     * Creates a new <code>Polytope2d</code> with vertices from the given <code>Collection</code>
     * and
     * sets its color to be the given <code>Color</code>.
     * 
     * @param color the color.
     * @param c the collection of vertices.
     */
    public Polytope2d(Color color, Collection<Vector3d> c) {
        vertices = new ArrayList<>(c);
        vertexNormals = new HashMap<>();
        
        this.color = color;
    }
    
    /**
     * Creates a new <code>Polytope2d</code> with the given vertices and sets its color to be the
     * given <code>Color</code>.
     * 
     * @param color the color.
     * @param c the collection of vertices.
     */
    public Polytope2d(Color color, Vector3d... c) {
        vertices = new ArrayList<>();
        vertexNormals = new HashMap<>();
        
        for (Vector3d v : c)
            vertices.add(v);
        
        this.color = color;
    }
    
    /**
     * Creates a new <code>Polytope2d</code> with the given vertices.
     * 
     * @param c the collection of vertices.
     */
    public Polytope2d(Vector3d... c) {
        vertices = new ArrayList<>();
        vertexNormals = new HashMap<>();
        
        for (Vector3d v : c)
            vertices.add(v);
    }
    
    /*
     * @see assignment4.graphics.polytopes.Polytope#add(javax.vecmath.Matrix3d)
     */
    @Override
    public void add(Matrix3d m) {
        for (Vector3d v : vertices)
            m.transform(v);
    }
    
    /*
     * The method is defined to use Matrix3d and Vector3d to apply transforms instead of creating
     * scratch Vector4ds to apply the Matrix4d transform to. By default in the javax.vecmath package
     * a
     * Matrix4d when transforming a Vector3d will set the 4th parameter (w) to 0 rather than 1 as is
     * required to apply correctly.
     * 
     * @see assignment4.graphics.polytopes.Polytope#add(javax.vecmath.Matrix4d)
     */
    @Override
    public void add(Matrix4d m) {
        Matrix3d rotation = new Matrix3d();
        m.get(rotation);
        add(rotation);
        
        Vector3d translation = new Vector3d();
        m.get(translation);
        add(translation);
    }
    
    /*
     * @see assignment4.graphics.polytopes.Polytope#add(javax.vecmath.Vector3d)
     */
    @Override
    public void add(Vector3d v) {
        for (Vector3d v2 : vertices)
            v2.add(v);
    }
    
    /**
     * Add the given vertex to the end of the list of vertices in this face.
     * 
     * @param v the vertex to add.
     */
    public void addVertex(Vector3d v) {
        if (v == null)
            throw new NullPointerException();
        
        vertices.add(v);
    }
    
    /*
     * @see assignment4.graphics.polytopes.Polytope#clip(double)
     */
    @Override
    public boolean clip(double clipZ) {
        boolean hidden = true; // Check flag.
        
        for (int i = 0; i < vertices.size(); i++) { // For each vertex...
            // ... check the edge it creates with the next vertex...
            int next = (i + 1) % vertices.size();
            
            Vector3d v1 = vertices.get(i);
            Vector3d v2 = vertices.get(next);
            
            if (v1.z < clipZ) // If one vertex is in front of the clip plane...
                hidden = false; // ... then we are visible!
                
            if (v1.z > v2.z) { // Ensure v1.z < v2.z
                Vector3d temp = new Vector3d(v1);
                v1 = new Vector3d(v2);
                v2 = temp;
            }
            
            if ((v1.z < clipZ) && (v2.z > clipZ)) { // If this edge intersects with the clip
                                                    // plane...
                double scale = (clipZ - v1.z) / (v2.z - v1.z);
                
                vertices.add(next, new Vector3d(v1.x + (scale * (v2.x - v1.x)),
                    v1.y + (scale * (v2.y - v1.y)), clipZ)); // ... create a new vertex at the
                                                             // intersection
                                                             // point.
                
                i++; // Skip the vertex just created.
            }
        }
        
        if (hidden) // If nothing is visible...
            return false; // ... return false so that we may clean up this polygon.
            
        for (int i = vertices.size() - 1; i >= 0; i--)
            if (vertices.get(i).z > clipZ)
                vertices.remove(i); // Remove any vertices that are behind the clip plane.
                
        return valid();
    }
    
    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        Polytope2d clone = new Polytope2d();
        
        for (Vector3d v : vertices)
            clone.addVertex(new Vector3d(v));
        
        clone.color = color;
        
        return clone;
    }
    
    /**
     * Checks whether this face is facing the given vector by comparing the dot product of its
     * normal
     * and the direction vector to <code>u</code>. If the dot product is greater than 0, then the
     * angle between the normal and direction vector <code>u - vertices.get(0)</code> is between 0
     * and
     * 90 degrees (ie. it is facing).
     * 
     * @param u the vector point to check.
     * @return true if facing the vector.
     */
    public boolean facing(Vector3d u) {
        Vector3d v = new Vector3d(u);
        
        v.sub(vertices.get(0));
        
        return (normal().dot(v) >= 0); // Between 0 and 90 degrees.
    }
    
    /**
     * Calculates the shade at the vertex of this face, with the given <code>PointLight3d</code>s.
     * 
     * The method will return <code>World3d.ambientLight</code> if:
     * <ul>
     * <li>If the <code>World3d</code> is not rendering lights.</li>
     * <li>The vertex normal for <code>v</code> has not been calculated.</li>
     * <li>No lights were supplied or <code>lights.size() == 0</code>.</li>
     * </ul>
     * 
     * @param v the vertex to calculate the shade at.
     * @param lights the set of lights to use for shading.
     * @return a double shade value between 0.0 and 1.0.
     * 
     * @see com.hjwylde.uni.comp261.assignment4.graphics.lights.PointLight3d
     */
    public double getShade(Vector3d v, List<PointLight3d> lights) {
        if (!World3d.renderLightsMode || !vertexNormals.containsKey(v)
            || (lights == null) || (lights.size() == 0))
            return World3d.ambientLight;
        
        double shade = 1.0;
        for (PointLight3d light : lights)
            shade *= World3d.ambientLight
                + (light.getIntensity() * vertexNormals.get(v).dot(light));
        
        return Main.constrain(shade, 0.0, 1.0);
    }
    
    /**
     * Unsupported.
     * 
     * @throws UnsupportedOperationException always.
     */
    @Override
    public Matrix4d getTransform() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Get the list of vertices for this face.
     * 
     * @return the list of vertices.
     */
    public List<Vector3d> getVertices() {
        return vertices;
    }
    
    /*
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Vector3d> iterator() {
        return vertices.iterator();
    }
    
    /**
     * Calculates the unit normal direction vector of this face.
     * 
     * @return the unit normal direction vector.
     */
    public Vector3d normal() {
        Vector3d normal = new Vector3d();
        
        Vector3d v1 = new Vector3d(vertices.get(1));
        Vector3d v2 = new Vector3d(vertices.get(2));
        
        v1.sub(vertices.get(0)); // Direction vector #1.
        v2.sub(vertices.get(0)); // Direction vector #2.
        
        normal.cross(v1, v2); // Calculate normal.
        normal.normalize(); // Unit vector.
        
        return normal;
    }
    
    /*
     * @see assignment4.graphics.polytopes.Polytope#project(assignment4.graphics.ViewPlane)
     */
    @Override
    public void project(ViewPlane view) {
        /*
         * Perspective transform all vertices x and y co-ordinates onto the ViewPlane.
         * 
         * Best to use a scratch Polytope for this so as to not overwrite the original vertices.
         */
        for (Vector3d v : vertices) {
            v.x = ((v.x * view.getDistance()) / -v.z) + (view.getWidth() / 2);
            v.y = ((v.y * view.getDistance()) / v.z) + (view.getHeight() / 2);
        }
    }
    
    /*
     * @see assignment4.graphics.polytopes.Polytope#render(assignment4.graphics.ZBuffer,
     * assignment4.graphics.ScanConverter)
     */
    @Override
    public void render(ZBuffer z, ScanConverter sc) {
        if (!valid()) // Check that there are at least 3 vertices.
            return;
        
        if (!sc.convert(this)) // If after conversion this face isn't visible...
            return; // ... return.
            
        for (int y = sc.getTopBoundary(); y <= sc.getBottomBoundary(); y++) { // For each scan.
            Scan s = sc.getScan(y);
            if (!s.valid()) // If scan is invalid...
                continue; // ... continue.
                
            if (World3d.wireframeMode) { // If wireframe mode...
                z.set(s.getLeft(), y, s.getLeftDepth(),
                    Polytope2d.getShadedColor(color, s.getLeftShade())); // Draw the left pixel of
                                                                         // the scan.
                z.set(s.getRight(), y, s.getRightDepth(),
                    Polytope2d.getShadedColor(color, s.getRightShade())); // Draw the right pixel of
                                                                          // the
                                                                          // scan.
            } else if (s.getLeft() == s.getRight()) // ... else if the scan is just 1 pixel wide...
                /*
                 * This here is odd: For some reason if I didn't include a separate method for 1
                 * pixel width
                 * scans then the face would be drawn with a black dot at the point of each vertex.
                 */
                z.set(s.getLeft(), y, s.getLeftDepth(),
                    Polytope2d.getShadedColor(color, s.getLeftShade())); // Draw the single pixel.
            else { // ... else draw the scan line.
                double depthGradient = (s.getRightDepth() - s.getLeftDepth())
                    / (s.getRight() - s.getLeft()); // For linearly interpolating the depth.
                double shadeGradient = (s.getRightShade() - s.getLeftShade())
                    / (s.getRight() - s.getLeft()); // For linearly interpolating the shade.
                
                for (int x = s.getLeft(); x <= s.getRight(); x++) {
                    double shade = (s.getLeftShade() + ((x - s.getLeft()) * shadeGradient)); // Calculate
                                                                                             // the
                    // shade.
                    
                    z.set(x, y, s.getLeftDepth()
                        + ((x - s.getLeft()) * depthGradient),
                        Polytope2d.getShadedColor(color, shade)); // Set the pixel at (x, y) on the
                                                                  // zBuffer.
                }
            }
        }
    }
    
    /**
     * Set the color of this face.
     * 
     * @param color the <code>color</code> to set.
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Unsupported.
     * 
     * @throws UnsupportedOperationException always.
     */
    @Override
    public void setTransform(Matrix4d m) {
        throw new UnsupportedOperationException();
    }
    
    /*
     * @see assignment4.graphics.polytopes.Polytope#sub(javax.vecmath.Matrix3d)
     */
    @Override
    public void sub(Matrix3d m) {
        Matrix3d inverse = new Matrix3d(m);
        inverse.transpose();
        add(inverse);
    }
    
    /*
     * The method is defined to use Matrix3d and Vector3d to apply transforms instead of creating
     * scratch Vector4ds to apply the Matrix4d transform to. By default in the javax.vecmath package
     * a
     * Matrix4d when transforming a Vector3d will set the 4th parameter (w) to 0 rather than 1 as is
     * required to apply correctly.
     * 
     * @see assignment4.graphics.polytopes.Polytope#add(javax.vecmath.Matrix4d)
     */
    @Override
    public void sub(Matrix4d m) {
        Vector3d translation = new Vector3d();
        m.get(translation);
        sub(translation);
        
        Matrix3d rotation = new Matrix3d();
        m.get(rotation);
        sub(rotation);
    }
    
    /*
     * @see assignment4.graphics.polytopes.Polytope#sub(javax.vecmath.Vector3d)
     */
    @Override
    public void sub(Vector3d v) {
        for (Vector3d v2 : vertices)
            v2.sub(v);
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{{" + vertices + "}, {" + color + "}}";
    }
    
    /**
     * Unsupported.
     * 
     * @throws UnsupportedOperationException always.
     */
    @Override
    public void transform() {
        throw new UnsupportedOperationException();
    }
    
    /*
     * @see assignment4.graphics.polytopes.Polytope#valid()
     */
    @Override
    public boolean valid() {
        return (vertices.size() >= 3); // Only valid if we can actually draw some 2d shape! Not just
                                       // a
                                       // line.
    }
    
    /**
     * Set the unit normal direction vector for the given vertex of this face to the given normal.
     * This method is used for Gouraud shading.
     * 
     * @param v the vertex to set the normal for.
     * @param normal the normal at the vertex.
     */
    protected void setNormal(Vector3d v, Vector3d normal) {
        if ((v == null) || (normal == null))
            throw new IllegalArgumentException();
        
        vertexNormals.put(v, normal);
    }
    
    /**
     * Helper method for calculating the shaded color with a given shade.
     * 
     * @param color the original color.
     * @param shade the shade to apply to the color.
     * @return a new shaded color.
     */
    private static Color getShadedColor(Color color, double shade) {
        return new Color(
            (int) Main.constrain((color.getRed() * shade), 0, 255),
            (int) Main.constrain((color.getGreen() * shade), 0, 255),
            (int) Main.constrain((color.getBlue() * shade), 0, 255));
    }
}