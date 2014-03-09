package com.hjwylde.uni.comp261.assignment04.graphics;

/*
 * Code for Assignment 4, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A view plane represents the "viewing" window that displays the 3d world. It has a width and
 * height, and a distance from the camera to it which is calculated based on the viewing angle. The
 * viewing angle is the span that the camera sees of the entire width of the viewing plane, ie. if
 * you draw a line from the camera to the left side of the view plane and a line to the right side,
 * then the angle that these lines create between them is the view angle. Default is an angle of 72
 * degrees. A view plane is used for projecting vectors onto it, for perspective projection. Because
 * the camera is always located at (0, 0, 0) the math is simple for projection calculations by
 * similar triangles:
 * 
 * <pre>
 * Vector3d v;
 * ViewPlane view;
 * v.x = (v.x * view.getDistance() / -v.z) + view.getWidth() / 2;
 * v.y = (v.y * view.getDistance() / v.z) + view.getHeight() / 2;
 * </pre>
 * 
 * The x co-ordinate is divided by <code>-v.z</code> as to correctly map to a (0, 0) top left
 * co-ordinate system (the screen).
 * 
 * @author Henry J. Wylde
 */
public class ViewPlane {

    private double height;
    private double width;
    private double distance;

    private double angle = (Math.PI * 2) / 5;

    /**
     * Creates a new <code>ViewPlane</code> with <code>width = 0, height = 0, distance = 1</code>.
     */
    public ViewPlane() {
        height = 0;
        width = 0;
        distance = 1;
    }

    /**
     * Gets the distance from the camera to the <code>ViewPlane</code>.
     * 
     * @return the distance.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Gets the height of the <code>ViewPlane</code>.
     * 
     * @return the height.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Gets the width of the <code>ViewPlane</code>.
     * 
     * @return the width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets the viewing angle of the camera to <code>ViewPlane</code> to the given angle.
     * 
     * @param angle the new viewing angle.
     */
    public void setAngle(double angle) {
        this.angle = angle;

        calcDistance(); // Re-calculate the distance from camera to the ViewPlane.
    }

    /**
     * Sets the height of the <code>ViewPlane</code>
     * 
     * @param height the new height.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Sets the width of the <code>ViewPlane</code>.
     * 
     * @param width the new width.
     */
    public void setWidth(int width) {
        this.width = width;

        calcDistance();
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ViewPlane [height=" + height + ", width=" + width + ", distance=" + distance
                + ", angle=" + angle + "]";
    }

    /**
     * Calculates the distance from the camera to the <code>ViewPlane</code> based on the viewing
     * angle and width of the <code>ViewPlane</code>.
     */
    private void calcDistance() {
        double distance = (width / 2) / Math.tan(angle / 2.0);
        if (distance == 0.0)
            distance = 1.0;

        this.distance = distance;
    }
}
