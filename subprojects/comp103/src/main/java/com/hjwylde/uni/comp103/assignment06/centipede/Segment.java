package com.hjwylde.uni.comp103.assignment06.centipede;

/*
 * Code for Assignment 6, COMP 103 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

import java.awt.Color;

import com.hjwylde.uni.comp103.util.DrawingCanvas;

/**
 * A Segment is one part of the body of a centipede. A segment knows - its position on the screen, -
 * its color - the direction it will move next - whether it should have a face on it
 */

public class Segment {

    // Constants
    private static int size = 12; // Segment radius

    // Fields
    private int x; // Segment's x co-ordinate on the screen
    private int y; // Segment's y co-ordinate on the screen

    private Direction direction; // Which direction the segment just moved in.
    private final Color color; // What colour to draw it in.
    private boolean face; // is it the first segment of a centipede and should
                          // have a face

    /**
     * Constructor for the head of a new centipede. A new centipede should be just two segments
     * long, be in a random position, have a random direction, have a random color (which fades
     * along the segments - each segment is a bit more grey than the previous one)
     */
    public Segment() {
        x = (int) (Math.random() * (Simulation.eastEdge - Simulation.westEdge));
        y = (int) (Math.random() * (Simulation.southEdge - Simulation.northEdge));

        double n = Math.random();
        if (n < 0.25)
            direction = Direction.North;
        else if (n < 0.5)
            direction = Direction.East;
        else if (n < 0.75)
            direction = Direction.South;
        else
            direction = Direction.West;

        color = Color.getHSBColor((float) Math.random(), 1.0f, 1.0f);
        face = true;
    }

    /**
     * Constructor for a non-head segment. - position is one radius over from the x and y of the
     * previous segment (in the opposite direction from the prevDir), - moving in the same direction
     * as the previous segment - color is the next shade through the spectrum from the previous
     * segment
     */

    public Segment(Segment prevSegment) {
        x = prevSegment.x;
        y = prevSegment.y;

        if (prevSegment.direction.equals(Direction.North))
            y = y + Segment.size;
        else if (prevSegment.direction.equals(Direction.South))
            y = y - Segment.size;
        else if (prevSegment.direction.equals(Direction.East))
            x = x - Segment.size;
        else if (prevSegment.direction.equals(Direction.West))
            x = x + Segment.size;

        direction = prevSegment.direction;
        color = Segment.nextShade(prevSegment.color);
        face = false;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns the new direction that the segment should move in. bounces back from edges, and
     * otherwise turns left or right with a probability of 0.1, turn to the right or left.
     */
    public Direction getNewDirection() {
        if (x <= Simulation.westEdge)
            return Direction.East;
        else if (x >= Simulation.eastEdge)
            return Direction.West;
        else if (y <= Simulation.northEdge)
            return Direction.South;
        else if (y >= Simulation.southEdge)
            return Direction.North;
        else if (Math.random() < 0.9)
            return direction;
        else // with low probability, turn
        if (Math.random() < 0.6) // more often turn right
            return direction.right();
        else
            return direction.left();
    }

    public int getRadius() {
        return Segment.size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isFace() {
        return face;
    }

    /**
     * Jump will move the segment by (dx, dy)
     */
    public void jump(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void move() {
        if (direction.equals(Direction.North))
            y = y - Segment.size;
        else if (direction.equals(Direction.South))
            y = y + Segment.size;
        else if (direction.equals(Direction.East))
            x = x + Segment.size;
        else if (direction.equals(Direction.West))
            x = x - Segment.size;
    }

    public boolean near(int otherX, int otherY) {
        return ((Math.abs(x - otherX) < Segment.size) && (Math.abs(y - otherY) < Segment.size));
    }

    /**
     * A segment should draw itself as a colored circle, centered at its position. If it is a face,
     * It should draw two eyes just above its center
     */
    public void redraw(DrawingCanvas canvas) {
        canvas.setForeground(color);
        canvas.fillOval(x - Segment.size, y - Segment.size, Segment.size * 2, Segment.size * 2,
                false);

        canvas.setForeground(Color.black);
        canvas.drawOval(x - Segment.size, y - Segment.size, Segment.size * 2, Segment.size * 2,
                false);

        if (face) {
            canvas.setForeground(Color.black);
            canvas.fillOval(x - (Segment.size / 2), y - (Segment.size / 2), Segment.size / 3,
                    Segment.size / 3, false);
            canvas.fillOval(x + (Segment.size / 6), y - (Segment.size / 2), Segment.size / 3,
                    Segment.size / 3, false);
        }
    }

    public void reverseDirection() {
        direction = direction.opposite();
    }

    public void setDirection(Direction newDir) {
        direction = newDir;
    }

    public void setFace(boolean f) {
        face = f;
    }

    /*
     * Utility methods, used by the methods above.
     */

    private static Color nextShade(Color col) {
        float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
        return Color.getHSBColor(hsb[0] + (5.0f / 360), 1.0f, 1.0f);
    }

}
