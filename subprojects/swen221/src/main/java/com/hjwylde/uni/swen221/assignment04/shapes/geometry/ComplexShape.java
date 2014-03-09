package com.hjwylde.uni.swen221.assignment04.shapes.geometry;

/*
 * Code for Assignment 4, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Represents a complex shape. A complex shape can be any combination of any kinds of shapes.
 * Supports operations on the shapes for calculating the difference, intersection and union between
 * this shape and another.
 * 
 * @author Henry J. Wylde
 */
public class ComplexShape implements Shape, ShapeOperations {

    private Shape left, right;
    private ShapeOperationType op;

    /**
     * Creates a new ComplexShape from the specified ComplexShape. The inner shapes are copied by
     * reference and not cloned so that any changes to them from outside of this ComplexShape is
     * reflected inside.
     * 
     * @param c the ComplexShape to mirror.
     */
    public ComplexShape(ComplexShape c) {
        left = c.left;
        right = c.right;
        op = c.op;
    }

    /**
     * Creates a new ComplexShape from the specified Shape.
     * 
     * @param left the initial Shape.
     */
    public ComplexShape(Shape left) {
        if (left == null)
            throw new IllegalArgumentException();

        this.left = left;
    }

    /*
     * @see assignment4.shapes.geometry.Shape#boundingBox()
     */
    @Override
    public Rectangle boundingBox() {
        if (right == null)
            return left.boundingBox();

        switch (op) {
            case DIFFERENCE:
                return Rectangle.difference(left.boundingBox(), right.boundingBox());
            case INTERSECTION:
                return Rectangle.intersection(left.boundingBox(), right.boundingBox());
            default: // UNION
                return Rectangle.union(left.boundingBox(), right.boundingBox());
        }
    }

    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        ComplexShape clone = new ComplexShape((Shape) left.clone());
        if (right != null) {
            clone.right = (Shape) right.clone();
            clone.op = op;
        }

        return clone;
    }

    /*
     * @see assignment4.shapes.geometry.Shape#contains(int, int)
     */
    @Override
    public boolean contains(int x, int y) {
        if (right == null)
            return left.contains(x, y);

        switch (op) {
            case DIFFERENCE: // Contains only if left contains and right doesn't.
                return left.contains(x, y) && !right.contains(x, y);
            case INTERSECTION: // Contains only if left and right contains.
                return left.contains(x, y) && right.contains(x, y);
            default: // UNION
                // Contains if left or right contains.
                return left.contains(x, y) || right.contains(x, y);
        }
    }

    /*
     * @see
     * assignment4.shapes.geometry.ShapeOperations#difference(assignment4.shapes.geometry.Shape)
     */
    @Override
    public void difference(Shape s) {
        if (right != null) // If this ComplexShape already has two shapes...
            left = new ComplexShape(this); // Squash it up into the left side.

        right = s;
        op = ShapeOperationType.DIFFERENCE;
    }

    /*
     * @see assignment4.shapes.geometry.ShapeOperations#intersect(assignment4.shapes.geometry.Shape)
     */
    @Override
    public void intersect(Shape s) {
        if (right != null) // If this ComplexShape already has two shapes...
            left = new ComplexShape(this); // Squash it up into the left side.

        right = s;
        op = ShapeOperationType.INTERSECTION;
    }

    /*
     * @see assignment4.shapes.geometry.ShapeOperations#union(assignment4.shapes.geometry.Shape)
     */
    @Override
    public void union(Shape s) {
        if (right != null) // If this ComplexShape already has two shapes...
            left = new ComplexShape(this); // Squash it up into the left side.

        right = s;
        op = ShapeOperationType.UNION;
    }
}
