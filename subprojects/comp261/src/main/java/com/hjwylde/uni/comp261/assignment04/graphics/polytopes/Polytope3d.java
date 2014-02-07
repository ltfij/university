package com.hjwylde.uni.comp261.assignment04.graphics.polytopes;

import java.util.*;

import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

import com.hjwylde.uni.comp261.assignment04.graphics.ScanConverter;
import com.hjwylde.uni.comp261.assignment04.graphics.ViewPlane;
import com.hjwylde.uni.comp261.assignment04.graphics.World3d;
import com.hjwylde.uni.comp261.assignment04.graphics.ZBuffer;
import com.hjwylde.uni.comp261.assignment04.modeller.Main;

/*
 * Code for Assignment 4, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A <code>Polytope3d</code> represents a 3d polytope in 3d space. It is used in conjunction with
 * <code>Polytope2d</code> to create 3d polytope shapes that may be transformed and rendered onto a
 * 2d screen.
 * 
 * @author Henry J. Wylde
 * 
 * @see com.hjwylde.uni.comp261.assignment04.graphics.polytopes.Polytope2d
 */
public class Polytope3d implements Polytope, Iterable<Polytope2d> {

    private List<Polytope2d> faces;
    private Map<Vector3d, Vector3d> vertexNormals;

    private Matrix4d transform;

    /**
     * Create a new empty <code>Polytope3d</code>.
     */
    public Polytope3d() {
        faces = new ArrayList<>();
        vertexNormals = new HashMap<>();

        transform = new Matrix4d();
        transform.setIdentity();
    }

    /**
     * Create a new <code>Polytope3d</code> with the set of faces from the specified
     * <code>Collection</code>.
     * 
     * @param c the collection of faces.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.graphics.polytopes.Polytope2d
     */
    public Polytope3d(Collection<Polytope2d> c) {
        faces = new ArrayList<>(c);
        vertexNormals = new HashMap<>();

        transform = new Matrix4d();
        transform.setIdentity();
    }

    /**
     * Create a new <code>Polytope3d</code> with the set of faces from the specified set.
     * 
     * @param c the set of faces.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.graphics.polytopes.Polytope2d
     */
    public Polytope3d(Polytope2d... c) {
        faces = new ArrayList<>();
        vertexNormals = new HashMap<>();

        for (Polytope2d p : c)
            faces.add(p);

        transform = new Matrix4d();
        transform.setIdentity();
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#add(javax.vecmath.Matrix3d)
     */
    @Override
    public void add(Matrix3d m) {
        for (Polytope2d face : faces)
            face.add(m);
    }

    /*
     * The method is defined to use Matrix3d and Vector3d to apply transforms instead of creating
     * scratch Vector4ds to apply the Matrix4d transform to. By default in the javax.vecmath package
     * a Matrix4d when transforming a Vector3d will set the 4th parameter (w) to 0 rather than 1 as
     * is required to apply correctly.
     * @see assignment4.graphics.polytopes.Polytope#add(javax.vecmath.Matrix4d)
     */
    @Override
    public void add(Matrix4d m) {
        Vector3d translation = new Vector3d();
        m.get(translation);

        Matrix3d rotation = new Matrix3d();
        m.get(rotation);

        for (Polytope2d face : faces) {
            face.add(rotation);
            face.add(translation);
        }
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#add(javax.vecmath.Vector3d)
     */
    @Override
    public void add(Vector3d v) {
        for (Polytope2d face : faces)
            face.add(v);
    }

    /**
     * Add the given face to the list of faces in this <code>Polytope3d</code>.
     * 
     * @param poly the face to add.
     */
    public void addFace(Polytope2d poly) {
        if (poly == null)
            throw new NullPointerException();

        faces.add(poly);
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#clip(double)
     */
    @Override
    public boolean clip(double clipZ) {
        for (int i = faces.size() - 1; i >= 0; i--)
            if (!faces.get(i).clip(clipZ)) // If after clipping a face isn't visible...
                faces.remove(i); // ... clean up: remove the face from the list.

        return valid();
    }

    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        Polytope3d clone = new Polytope3d();

        for (Polytope2d face : faces)
            clone.addFace((Polytope2d) face.clone());

        if (transform != null)
            clone.transform = new Matrix4d(transform);

        return clone;
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#getTransform()
     */
    @Override
    public Matrix4d getTransform() {
        return transform;
    }

    /*
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Polytope2d> iterator() {
        return faces.iterator();
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#project(assignment4.graphics.ViewPlane)
     */
    @Override
    public void project(ViewPlane view) {
        for (int i = faces.size() - 1; i >= 0; i--)
            if ((World3d.hiddenRemovalMode ? faces.get(i).facing(new Vector3d(0.0, 0.0, 0.0))
                    : true) // If ((if (hiddenRemovalMode)
                            // {isFacing(camera)}) &&
                            // isValid()...
                    && faces.get(i).valid())
                faces.get(i).project(view); // ... then project to the view window.
            else
                // ... else do not draw this face.
                faces.remove(i); // Clean up.
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#render(assignment4.graphics.ZBuffer,
     * assignment4.graphics.ScanConverter)
     */
    @Override
    public void render(ZBuffer z, ScanConverter sc) {
        calcVertexNormals(); // For Gouraud shading when rendering the face.

        for (Polytope2d face : faces)
            face.render(z, sc);
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#setTransform(javax.vecmath.Matrix4d)
     */
    @Override
    public void setTransform(Matrix4d m) {
        transform = m;
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
     * a Matrix4d when transforming a Vector3d will set the 4th parameter (w) to 0 rather than 1 as
     * is required to apply correctly.
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
        add(new Vector3d(-v.x, -v.y, -v.z));
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String str = "{{";

        for (Polytope2d face : faces)
            str += "\n" + face;

        str = str.replaceAll("\n", "\n  ");

        return str + "\n}, {" + transform + "}}";
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#transform()
     */
    @Override
    public void transform() {
        if (!transform.equals(Main.MATRIX4D_IDENTITY))
            add(transform);
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#valid()
     */
    @Override
    public boolean valid() {
        return !faces.isEmpty();
    }

    /**
     * Calculates the vertex normals for every face in this <code>Polytope3d</code>. Creates a map
     * of "Vertex -> Normal". It then will update the normal for each vertex as it iterates through
     * every face and every faces vertices.
     */
    private void calcVertexNormals() {
        vertexNormals = new HashMap<>(); // Vertex normal map.

        Vector3d normal;
        Vector3d v2;
        for (Polytope2d face : faces) { // For each face...
            normal = face.normal(); // Get the face normal.

            for (Vector3d v : face) { // For each vertex in the current face...
                if (vertexNormals.containsKey(v)) { // If we already have a normal assigned to the
                                                    // vertex.
                    v2 = vertexNormals.get(v); // Get the current normal.

                    v2.add(normal); // Add the new faces normal to the current normal.
                    v2.scale(0.5); // Average the normal.
                } else
                    // ... else no normal currently exists.
                    vertexNormals.put(v, normal); // Put the face normal with the vertex into the
                                                  // map.

                /*
                 * Set the normal for the current face and vertex. As this is by reference, whenever
                 * the normal gets updated from consecutive faces, this normal will also be updated.
                 */
                face.setNormal(v, vertexNormals.get(v));
            }
        }
    }
}
