package com.hjwylde.uni.comp261.assignment04.graphics.polytopes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

import com.hjwylde.uni.comp261.assignment04.graphics.ScanConverter;
import com.hjwylde.uni.comp261.assignment04.graphics.ViewPlane;
import com.hjwylde.uni.comp261.assignment04.graphics.ZBuffer;
import com.hjwylde.uni.comp261.assignment04.modeller.Main;

/*
 * Code for Assignment 4, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A <code>PolytopeGroup</code> represents a group of <code>Polytope</code>s. Provides easy grouping
 * of multiple <code>Polytope</code>s and applying transformations to all of its sub
 * <code>Polytope</code>s recursively.
 * 
 * @author Henry J. Wylde
 */
public class PolytopeGroup implements Polytope, Iterable<Polytope> {

    private List<Polytope> polytopes;

    private Matrix4d transform;

    /**
     * Creates a new empty <code>PolytopeGroup</code>.
     */
    public PolytopeGroup() {
        polytopes = new ArrayList<>();

        transform = new Matrix4d();
        transform.setIdentity();
    }

    /**
     * Creates a new <code>PolytopeGroup</code> with the <code>Collection</code> of
     * <code>Polytope</code>s.
     * 
     * @param c the Collection of Polytopes.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.graphics.polytopes.Polytope
     */
    public PolytopeGroup(Collection<Polytope> c) {
        polytopes = new ArrayList<>(c);

        transform = new Matrix4d();
        transform.setIdentity();
    }

    /**
     * Creates a new <code>PolytopeGroup</code> with the set of <code>Polytope</code>s.
     * 
     * @param c the set of Polytopes.
     * 
     * @see Polytope
     */
    public PolytopeGroup(Polytope... c) {
        polytopes = new ArrayList<>();

        for (Polytope poly : c)
            polytopes.add(poly);

        transform = new Matrix4d();
        transform.setIdentity();
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#add(javax.vecmath.Matrix3d)
     */
    @Override
    public void add(Matrix3d m) {
        for (Polytope poly : polytopes)
            poly.add(m);
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
        for (Polytope poly : polytopes)
            poly.add(v);
    }

    /**
     * Adds the given <code>Polytope</code> to the list of polytopes this <code>PolytopeGroup</code>
     * has.
     * 
     * @param poly the Polytope to add.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.graphics.polytopes.Polytope
     */
    public void addPolytope(Polytope poly) {
        if (this == poly)
            throw new IllegalArgumentException();

        polytopes.add(poly);
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#clip(double)
     */
    @Override
    public boolean clip(double clipZ) {
        for (int i = polytopes.size() - 1; i >= 0; i--)
            if (!polytopes.get(i).clip(clipZ)) // If after clipping a face isn't visible...
                polytopes.remove(i); // ... clean up: remove the face from the list.

        return valid();
    }

    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        PolytopeGroup clone = new PolytopeGroup();

        for (Polytope poly : polytopes)
            clone.addPolytope((Polytope) poly.clone());

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
    public Iterator<Polytope> iterator() {
        return polytopes.iterator();
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#project(assignment4.graphics.ViewPlane)
     */
    @Override
    public void project(ViewPlane view) {
        for (Polytope poly : polytopes)
            poly.project(view);
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#render(assignment4.graphics.ZBuffer,
     * assignment4.graphics.ScanConverter)
     */
    @Override
    public void render(ZBuffer z, ScanConverter sc) {
        for (Polytope poly : polytopes)
            poly.render(z, sc);
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
        Matrix3d rotation = new Matrix3d();
        m.get(rotation);
        sub(rotation);

        Vector3d translation = new Vector3d();
        m.get(translation);
        sub(translation);
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

        for (Polytope polytope : polytopes)
            str += "\n" + polytope;

        str = str.replaceAll("\n", "\n  ");

        return str + "\n}, {" + transform + "}}";
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#transform()
     */
    @Override
    public void transform() {
        for (Polytope poly : polytopes)
            poly.transform();

        if (!transform.equals(Main.MATRIX4D_IDENTITY))
            add(transform);
    }

    /*
     * @see assignment4.graphics.polytopes.Polytope#valid()
     */
    @Override
    public boolean valid() {
        return !polytopes.isEmpty();
    }
}
