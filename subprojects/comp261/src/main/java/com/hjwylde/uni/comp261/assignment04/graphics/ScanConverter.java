package com.hjwylde.uni.comp261.assignment04.graphics;

/*
 * Code for Assignment 4, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/*
 * Reference: Developing Games in Java By David Brackeen, Bret Barker, Laurence Vanhelsuwe Publisher
 * : New Riders Publishing Pub Date : August 20, 2003 ISBN : 1-5927-3005-1 This book was a reference
 * for some parts of this assignment, including "scan converting". The term is taken from the book,
 * and the 'base' code format for the ScanConverter, but was re-written for re-use and changed to
 * use depth values. I read this book and wrote similar code (but my own) a year / two ago, so some
 * formats will follow the same methods as the book, but it was written from scratch.
 */

import java.util.List;

import javax.vecmath.Vector3d;

import com.hjwylde.uni.comp261.assignment04.graphics.lights.PointLight3d;
import com.hjwylde.uni.comp261.assignment04.graphics.polytopes.Polytope2d;

/**
 * Scan converts a polygon. A scan is just a line, with boundary (left and right) values with
 * corresponding depth values. For calculating depth values for x values between left and right, use
 * a line equation from the calculated depth gradient. A polygon should be scan converted after it
 * has been projected to the <code>ViewPlane</code>.
 * 
 * @see com.hjwylde.uni.comp261.assignment04.graphics.ScanConverter.Scan
 * @see com.hjwylde.uni.comp261.assignment04.graphics.ViewPlane
 */
public class ScanConverter {

    private ViewPlane view;
    private List<PointLight3d> lights;

    private Scan[] scans;
    private int top;
    private int bottom;

    /**
     * Create a new <code>ScanConverter</code> for the given <code>ViewPlane</code>.
     * 
     * @param view the ViewPlane.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.graphics.ViewPlane
     */
    public ScanConverter(ViewPlane view) {
        this.view = view;
    }

    /**
     * Create a new <code>ScanConverter</code> for the given <code>ViewPlane</code> with the given
     * lights.
     * 
     * @param view the ViewPlane
     * @param lights the list of lights.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.graphics.ViewPlane
     */
    public ScanConverter(ViewPlane view, List<PointLight3d> lights) {
        this.view = view;

        if ((lights != null) && (lights.size() != 0))
            this.lights = lights;
    }

    /**
     * Converts the given <code>Polytope2d</code> to a series of scans. Clears any current scans
     * this scan converter has and sets the top and bottom boundaries to the min / max y value of
     * the <code>Polytope2d</code>.
     * 
     * Scanning a polygon involves iterating through every edge of a polygon (calculated from 2
     * vertices) and setting the x boundary for the y'th scan. This works so long as a polygon is
     * convex, otherwise it will fill in concave polygons. By going through every edge and setting
     * the boundary the result will have a list of scans with correct left and right values.
     * 
     * @param face the face to scan convert.
     * @return true if the polygon is visible after scan converted.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.graphics.polytopes.Polytope2d
     */
    public boolean convert(Polytope2d face) {
        ensureCapacity(); // Ensure capacity for Scan[] array to ViewPlane width / height as these
                          // values may change in between conversions.
        clear(); // Clear any past scans.

        // ViewPlane boundaries.
        int minX = 0;
        int maxX = (int) view.getWidth() - 1;
        int minY = 0;
        int maxY = (int) view.getHeight() - 1;

        List<Vector3d> vertices = face.getVertices();
        for (int i = 0; i < vertices.size(); i++) {
            Vector3d v1 = vertices.get(i); // Vertex #1
            Vector3d v2 = vertices.get((i + 1) % vertices.size()); // Vertex after V#1.

            if ((v2.y - v1.y) == 0.0) // Skip horizontal lines. These are already included.
                continue;

            // Ensure v1.y < v2.y: less if statements later on.
            if (v1.y > v2.y) {
                Vector3d temp = new Vector3d(v1);
                v1 = new Vector3d(v2);
                v2 = temp;
            }

            Line3d edge = new Line3d(v1, face.getShade(v1, lights), v2, face.getShade(v2, lights)); // Line
                                                                                                    // equation
                                                                                                    // with
                                                                                                    // easy
                                                                                                    // "get"
                                                                                                    // methods.

            int startY = (int) Math.max(Math.ceil(v1.y), minY); // Start y for this edge.
            int endY = (int) Math.min(Math.ceil(v2.y) - 1, maxY); // End y for this edge.

            top = Math.min(top, startY); // Update top for this polygon.
            bottom = Math.max(bottom, endY); // Update bottom for this polygon.

            if ((v2.x - v1.x) == 0.0) { // If Vertical line...
                int x = (int) Math.ceil(Math.min(maxX + 1, Math.max(v1.x, minX))); // Calculate x.

                for (int y = startY; y <= endY; y++)
                    // For each y in this line...
                    scans[y].setBoundary(x, edge.getWithY(y).z, edge.getShadeWithY(y)); // ... set
                                                                                        // boundary
                                                                                        // to
                                                                                        // be x with
                                                                                        // a depth
                                                                                        // calculated
                                                                                        // with y.
            } else { // ... else ....
                // Begin startX edge clipping checks.
                double startX = edge.getWithY(startY).x;
                if (startX < minX) { // If edge is clipped on left side of screen...
                    int y = (int) Math.ceil(Math.min(edge.getWithX(minX).x, endY)); // Calculate
                                                                                    // edge y.

                    while (startY <= y) { // For each y where x is clipped...
                        scans[startY].setBoundary(minX, edge.getWithX(minX).z,
                                edge.getShadeWithX(minX)); // ... set boundary.
                        startY++; // Update new start y.
                    }
                } else if (startX > maxX) { // ... else if edge is clipped on right side of
                                            // screen...
                    int y = (int) Math.ceil(Math.min(edge.getWithX(maxX).y, endY)); // Calculate
                                                                                    // edge y.

                    while (startY <= y) { // For each y where x is clipped...
                        scans[startY].setBoundary(maxX + 1, edge.getWithX(maxX + 1).z,
                                edge.getShadeWithX(maxX + 1)); // ... set boundary.
                        startY++; // Update new start y.
                    }
                } // End startX edge clipping checks.

                if (startY > endY) // If there are no more scans for this edge...
                    continue; // ... continue.

                // Begin endX edge clipping checks.
                double endX = edge.getWithY(endY).x;
                if (endX < minX) { // If edge is clipped on left side of screen...
                    int y = (int) Math.ceil(Math.max(edge.getWithX(minX).x, startY)); // Calculate
                                                                                      // edge y.

                    while (endY >= y) { // For each y where x is clipped...
                        scans[endY].setBoundary(minX, edge.getWithX(minX).x,
                                edge.getShadeWithX(minX)); // ... set boundary.
                        endY--; // Update new end y.
                    }
                } else if (endX > maxX) { // ... else if edge is clipped on right side of screen...
                    int y = (int) Math.ceil(Math.max(edge.getWithX(maxX).x, startY)); // Calculate
                                                                                      // edge y.

                    while (endY >= y) { // For each y where x is clipped...
                        scans[endY].setBoundary(maxX + 1, edge.getWithX(maxX + 1).z,
                                edge.getShadeWithX(maxX + 1)); // ... set boundary.
                        endY--; // Update new end y.
                    }
                } // End endX edge clipping checks.

                if (startY > endY) // If there are no more scans for this edge...
                    continue; // ... continue.

                for (int y = startY; y <= endY; y++)
                    // Go through rest of the line...
                    scans[y].setBoundary((int) Math.ceil(edge.getWithY(y).x), edge.getWithY(y).z,
                            edge.getShadeWithY(y)); // Use line equation to
                                                    // calculate x, depth
                                                    // and shade at each y.
            }
        }

        for (int i = top; i <= bottom; i++)
            // For each scan...
            if (scans[i].valid()) // ... if at least one scan is valid (polygon visible)...
                return true; // ... return true.

        return false; // No scans are valid: this polygon is not visible.
    }

    /**
     * Gets the bottom boundary for this <code>ScanConverter</code>. The bottom boundary is the end
     * y value for the polygon that this <code>ScanConverter</code> has converted.
     * 
     * @return the bottom boundary.
     */
    public int getBottomBoundary() {
        return bottom;
    }

    /**
     * Get the horizontal scan at the given y value.
     * 
     * @param y the y value of the scan.
     * @return the Scan at y.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.graphics.ScanConverter.Scan
     */
    public Scan getScan(int y) {
        return scans[y];
    }

    /**
     * Gets the top boundary for this <code>ScanConverter</code>. The top boundary is the start y
     * value for this polygon that this <code>ScanConverter</code> has converted.
     * 
     * @return the top boundary.
     */
    public int getTopBoundary() {
        return top;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String str = "{";

        for (int i = top; i <= bottom; i++) {
            str += "(" + i + ", " + scans[i] + ")";
            if (i != bottom)
                str += ", ";
        }

        return str + "}";
    }

    /**
     * Clear all previous scans and reset the bottom and top values of this
     * <code>ScanConverter</code> .
     */
    private void clear() {
        for (int i = top; i <= bottom; i++)
            scans[i].clear();

        top = Integer.MAX_VALUE;
        bottom = Integer.MIN_VALUE;
    }

    /**
     * Ensure capacity for the <code>Scan</code>s array. This method is included in case the
     * <code>ViewPlane</code>s dimensions change in between scan converting polygons.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.graphics.ScanConverter.Scan
     * @see com.hjwylde.uni.comp261.assignment04.graphics.ViewPlane
     */
    private void ensureCapacity() {
        if ((scans == null) || (scans.length != view.getHeight())) {
            scans = new Scan[(int) view.getHeight()];

            for (int i = 0; i < view.getHeight(); i++)
                scans[i] = new Scan();

            top = 0;
            bottom = (int) view.getHeight() - 1;
        }
    }

    /**
     * A <code>Scan</code> is a horizontal line with a left and right values, with corresponding
     * depth and shade values for these points. Used in conjunction with scan converting and
     * rendering a 3d image with a <code>ZBuffer</code>.
     * 
     * @author Henry J. Wylde
     * 
     * @see com.hjwylde.uni.comp261.assignment04.graphics.ZBuffer
     */
    public class Scan {

        private int left;
        private int right;

        private double leftDepth;
        private double rightDepth;

        private double leftShade;
        private double rightShade;

        /**
         * Creates a new <code>Scan</code> and clears it.
         */
        public Scan() {
            clear();
        }

        /**
         * Clears this <code>Scan</code>, setting the left and right values to invalid values and
         * the depths to the highest possible depth.
         */
        public void clear() {
            left = Integer.MAX_VALUE;
            right = Integer.MIN_VALUE;

            leftDepth = Double.MAX_VALUE;
            rightDepth = Double.MAX_VALUE;

            leftShade = 1.0;
            rightShade = 1.0;
        }

        /**
         * Get the <code>Scan</code>s left.
         * 
         * @return the left value.
         */
        public int getLeft() {
            return left;
        }

        /**
         * Get the <code>Scan</code>s left depth.
         * 
         * @return the left depth value.
         */
        public double getLeftDepth() {
            return leftDepth;
        }

        /**
         * Get the <code>Scan</code>s left shade.
         * 
         * @return the left shade value.
         */
        public double getLeftShade() {
            return leftShade;
        }

        /**
         * Get the <code>Scan</code>s right.
         * 
         * @return the right value.
         */
        public int getRight() {
            return right;
        }

        /**
         * Get the <code>Scan</code>s right depth.
         * 
         * @return the right depth value.
         */
        public double getRightDepth() {
            return rightDepth;
        }

        /**
         * Get the <code>Scan</code>s right shade.
         * 
         * @return the right shade value.
         */
        public double getRightShade() {
            return rightShade;
        }

        /**
         * Sets this <code>Scan</code>s left and right values to the given values.
         * 
         * @param left the new left value.
         * @param right the new right value.
         */
        public void set(int left, int right) {
            this.left = left;
            this.right = right;
        }

        /**
         * Attempts to set the boundary to the given <code>x</code> value. If <code>x</code> is less
         * than the current left, the left will be set to x, otherwise if it is greater than the
         * current right, the right will be set to x. Also sets the depth and shade values if x gets
         * assigned.
         * 
         * @param x the x value.
         * @param depth the depth value at x.
         */
        public void setBoundary(int x, double depth, double shade) {
            if (x < left) {
                left = x;
                leftDepth = depth;
                leftShade = shade;
            }

            if ((x - 1) > right) {
                right = x - 1;
                rightDepth = depth;
                rightShade = shade;
            }
        }

        /*
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "(" + left + ", " + right + ")";
        }

        /**
         * Checks if this <code>Scan</code> is valid or not. If left is larger than right, then it
         * is invalid.
         * 
         * @return true if this Scan is valid.
         */
        public boolean valid() {
            return (left <= right);
        }
    }

    /**
     * A private inner class for a 3d line in space. Provides easy "get" methods for getting a
     * corresponding point in a line from either a x, y or z value. Also holds shade values and a
     * gradient shade value.
     * 
     * A line is "a + td" where a is a point on the line, d is the lines direction vector and t is
     * some scalar.
     * 
     * @author Henry J. Wylde
     */
    private class Line3d {

        // a + td Where t is a scalar, and a and d are vectors.

        private Vector3d a, d;
        private double aShade, dShade;

        /**
         * Constructs a 3d line from 2 vectors.
         * 
         * @param a V#1.
         * @param b V#2.
         */
        private Line3d(Vector3d a, double aShade, Vector3d b, double bShade) {
            this.a = a;
            this.aShade = aShade;

            d = new Vector3d(b);
            d.sub(a);
            dShade = bShade - aShade;
        }

        /**
         * Get the shade at the point on the line that has the corresponding x value. Throws an
         * <code>UnsupportedOperationException</code> if (dx == 0), ie. all points on the line have
         * the same x values.
         * 
         * @param x the x co-ordinate.
         * @return the point on the line with v.x = x.
         */
        public double getShadeWithX(double x) {
            if (dShade == 0.0)
                return aShade;

            if (d.x == 0.0)
                throw new UnsupportedOperationException();

            double t = (x - a.x) / d.x;

            return aShade + (t * dShade);
        }

        /**
         * Get the shade at the point on the line that has the corresponding y value. Throws an
         * <code>UnsupportedOperationException</code> if (dy == 0), ie. all points on the line have
         * the same y values.
         * 
         * @param y the y co-ordinate.
         * @return the point on the line with v.y = y.
         */
        public double getShadeWithY(double y) {
            if (dShade == 0.0)
                return aShade;

            if (d.y == 0.0)
                throw new UnsupportedOperationException();

            double t = (y - a.y) / d.y;

            return aShade + (t * dShade);
        }

        /**
         * Get the shade at the point on the line that has the corresponding z value. Throws an
         * <code>UnsupportedOperationException</code> if (dz == 0), ie. all points on the line have
         * the same z values.
         * 
         * @param z the z co-ordinate.
         * @return the point on the line with v.z = z.
         */
        @SuppressWarnings("unused")
        public double getShadeWithZ(double z) {
            if (dShade == 0.0)
                return aShade;

            if (d.z == 0.0)
                throw new UnsupportedOperationException();

            double t = (z - a.z) / d.z;

            return aShade + (t * dShade);
        }

        /**
         * Get the point on the line that has the corresponding x value. Throws an
         * <code>UnsupportedOperationException</code> if (dx == 0), ie. all points on the line have
         * the same x values.
         * 
         * @param x the x co-ordinate.
         * @return the point on the line with v.x = x.
         */
        public Vector3d getWithX(double x) {
            if (d.x == 0.0)
                throw new UnsupportedOperationException();

            Vector3d v = new Vector3d(a);
            Vector3d vd = new Vector3d(d);
            double t = (x - a.x) / d.x;
            vd.scale(t);
            v.add(vd);

            return v;
        }

        /**
         * Get the point on the line that has the corresponding y value. Throws an
         * <code>UnsupportedOperationException</code> if (dy == 0), ie. all points on the line have
         * the same y values.
         * 
         * @param y the y co-ordinate.
         * @return the point on the line with v.y = y.
         */
        public Vector3d getWithY(double y) {
            if (d.y == 0.0)
                throw new UnsupportedOperationException();

            Vector3d v = new Vector3d(a);
            Vector3d vd = new Vector3d(d);
            double t = (y - a.y) / d.y;
            vd.scale(t);
            v.add(vd);

            return v;
        }

        /**
         * Get the point on the line that has the corresponding z value. Throws an
         * <code>UnsupportedOperationException</code> if (dz == 0), ie. all points on the line have
         * the same z values.
         * 
         * @param z the z co-ordinate.
         * @return the point on the line with v.z = z.
         */
        @SuppressWarnings("unused")
        public Vector3d getWithZ(double z) {
            if (d.z == 0.0)
                throw new UnsupportedOperationException();

            Vector3d v = new Vector3d(a);
            Vector3d vd = new Vector3d(d);
            double t = (z - a.z) / d.z;
            vd.scale(t);
            v.add(vd);

            return v;
        }
    }
}
