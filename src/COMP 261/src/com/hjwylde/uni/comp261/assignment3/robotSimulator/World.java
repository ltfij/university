package com.hjwylde.uni.comp261.assignment3.robotSimulator;

/*
 * Code for Assignment 3, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class World {
    
    private boolean validWorldLoaded = false;
    private int width = 800;
    private int height = 600;
    private ArrayList<Robot> robots = new ArrayList<>();
    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Thing> things = new ArrayList<>();
    private ArrayList<Box> boxes = new ArrayList<>();
    
    public int distanceToFirstBox(int robotID) {
        for (Robot r : robots)
            if (r.id == robotID) {
                Box b = boxes.get(0);
                return (int) Math.round(World.distanceFromPointToPoint(r.x,
                    r.y, b.x, b.y));
            }
        return -1;
    }
    
    public int distanceToFirstThing(int robotID) {
        if (things.size() == 0)
            return -1;
        for (Robot r : robots)
            if (r.id == robotID) {
                Thing t = things.get(0);
                return (int) Math.round(World.distanceFromPointToPoint(r.x,
                    r.y, t.x, t.y));
            }
        return -1;
    }
    
    public void drawWorld(Graphics g) {
        drawWorld(g, 0, 0, width, height);
    }
    
    public void drop(int robotID, Graphics g) {
        for (Robot r : robots)
            if (r.id == robotID)
                for (Box b : boxes)
                    if (World.circlesClose(r.x, r.y, Robot.d, b.x, b.y, Box.d)) {
                        b.numThings += r.numThings;
                        r.numThings = 0;
                        this.drawWorld(g);
                        return;
                    }
    }
    
    public void executeProgram(int robotID, Graphics g) {
        for (Robot r : robots)
            if (r.id == robotID) {
                if (r.p.execute(r.id, this, g))
                    JOptionPane.showMessageDialog(null,
                        "Successfully executed a program for robot ID: "
                            + robotID, "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null,
                        "Error executing a program for robot ID: " + robotID,
                        "Error", JOptionPane.ERROR_MESSAGE);
                this.drawWorld(g);
                return;
            }
    }
    
    public int getHeight() {
        return height;
    }
    
    public ArrayList<Integer> getRobotIDs() {
        ArrayList<Integer> result = new ArrayList<>();
        for (Robot r : robots)
            result.add(r.id);
        return result;
    }
    
    public ArrayList<Integer> getRobotIDsWithValidProgramsLoaded() {
        ArrayList<Integer> result = new ArrayList<>();
        for (Robot r : robots)
            if (r.p.validProgramLoaded())
                result.add(r.id);
        return result;
    }
    
    public int getWidth() {
        return width;
    }
    
    @SuppressWarnings("resource")
    public void loadProgram(String fileName, int robotID, Graphics g) {
        try (Scanner s = new Scanner(new File(fileName))) {
            s.useDelimiter(""); // Show that the new parser does not need seperation by or removal
                                // of
                                // whitespace.
            
            for (Robot r : robots)
                if (r.id == robotID) {
                    RobotProgram rp = new RobotProgram();
                    if (rp.parse(s)) {
                        r.p = rp; // Don't override a valid program (if any) with an invalid
                                  // one!
                        JOptionPane.showMessageDialog(null,
                            "Successfully loaded a program into robot ID: "
                                + robotID, "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else
                        JOptionPane
                            .showMessageDialog(null,
                                "Error loading a program into robot ID: "
                                    + robotID, "Error",
                                JOptionPane.ERROR_MESSAGE);
                    this.drawWorld(g);
                    
                    return;
                }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "Cannot load robot program file. Not found.", "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    @SuppressWarnings("resource")
    public boolean loadWorld(String fileName) {
        int originalWidth = width;
        int originalHeight = height;
        ArrayList<Robot> originalRobots = robots;
        ArrayList<Wall> originalWalls = walls;
        ArrayList<Thing> originalThings = things;
        ArrayList<Box> originalBoxes = boxes;
        
        width = -1;
        height = -1;
        robots = new ArrayList<>();
        walls = new ArrayList<>();
        things = new ArrayList<>();
        boxes = new ArrayList<>();
        
        int line = 0;
        int robotID = 1;
        
        class LoadWorldException extends Exception {
            
            private static final long serialVersionUID = 8459022553592493319L;
            
            public String errorTitle;
            public String errorMessage;
            
            public LoadWorldException(String errorTitle, String errorMessage) {
                this.errorTitle = errorTitle;
                this.errorMessage = errorMessage;
            }
        }
        
        try (Scanner s = new Scanner(new File(fileName))) {
            while (s.hasNextLine()) {
                line++;
                
                if (!s.hasNext()) {
                    s.nextLine();
                    continue;
                } // Skip blank lines.
                
                String value = s.next();
                if (value.equals("WIDTH:")) {
                    if (width != -1)
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Duplicate width entry!");
                    if (!s.hasNextInt())
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error parsing width entry!");
                    int width = s.nextInt();
                    if (width < 800)
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Width has to be at least 800!");
                    this.width = width;
                } else if (value.equals("HEIGHT:")) {
                    if (height != -1)
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Duplicate height entry!");
                    if (!s.hasNextInt())
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error parsing height entry!");
                    int height = s.nextInt();
                    if (height < 600)
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Height has to be at least 600!");
                    this.height = height;
                } else if (value.equals("ROBOT:")) {
                    Robot r = new Robot();
                    if (!s.hasNextInt())
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error parsing robot entry!");
                    r.x = s.nextInt();
                    if ((r.x < (Robot.d / 2))
                        || ((r.x + (Robot.d / 2)) > width))
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error: robot x entry out of range!");
                    if (!s.hasNextInt())
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error parsing robot entry!");
                    r.y = s.nextInt();
                    if ((r.y < (Robot.d / 2))
                        || ((r.y + (Robot.d / 2)) > height))
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error: robot y entry out of range!");
                    r.id = robotID++;
                    r.numThings = 0;
                    r.angleFromTop = 0;
                    r.p = new RobotProgram();
                    robots.add(r);
                } else if (value.equals("WALL:")) {
                    Wall w = new Wall();
                    if (!s.hasNextInt())
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error parsing wall entry!");
                    w.x1 = s.nextInt();
                    if ((w.x1 < 0) || (w.x1 > width))
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error: wall x1 entry out of range!");
                    if (!s.hasNextInt())
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error parsing wall entry!");
                    w.y1 = s.nextInt();
                    if ((w.y1 < 0) || (w.y1 > height))
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error: wall y1 entry out of range!");
                    if (!s.hasNextInt())
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error parsing wall entry!");
                    w.x2 = s.nextInt();
                    if ((w.x2 < 0) || (w.x2 > width))
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error: wall x2 entry out of range!");
                    if (!s.hasNextInt())
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error parsing wall entry!");
                    w.y2 = s.nextInt();
                    if ((w.y2 < 0) || (w.y2 > height))
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error: wall y2 entry out of range!");
                    walls.add(w);
                } else if (value.equals("THING:")) {
                    Thing t = new Thing();
                    if (!s.hasNextInt())
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error parsing thing entry!");
                    t.x = s.nextInt();
                    if ((t.x < (Thing.d / 2))
                        || ((t.x + (Thing.d / 2)) > width))
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error: thing x entry out of range!");
                    if (!s.hasNextInt())
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error parsing thing entry!");
                    t.y = s.nextInt();
                    if ((t.y < (Thing.d / 2))
                        || ((t.y + (Thing.d / 2)) > height))
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error: thing y entry out of range!");
                    things.add(t);
                } else if (value.equals("BOX:")) {
                    Box b = new Box();
                    if (!s.hasNextInt())
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error parsing box entry!");
                    b.x = s.nextInt();
                    if ((b.x < (Box.d / 2)) || ((b.x + (Box.d / 2)) > width))
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error: box x entry out of range!");
                    if (!s.hasNextInt())
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error parsing box entry!");
                    b.y = s.nextInt();
                    if ((b.y < (Box.d / 2)) || ((b.y + (Box.d / 2)) > height))
                        throw new LoadWorldException(
                            "Error Parsing World (Line: " + line + ")",
                            "Error: box y entry out of range!");
                    b.numThings = 0;
                    boxes.add(b);
                } else
                    throw new LoadWorldException("Error Parsing World (Line: "
                        + line + ")", "Unrecognised entry in the world file: "
                        + value);
            }
            
            for (Wall w : walls) {
                for (Robot r : robots)
                    if (World.lineIntersectsCircle(w.x1, w.y1, w.x2, w.y2, r.x,
                        r.y, Robot.d))
                        throw new LoadWorldException("Error Validating World",
                            "Robot (ID: " + r.id
                                + ") intersects one of the walls!");
                for (Thing t : things)
                    if (World.lineIntersectsCircle(w.x1, w.y1, w.x2, w.y2, t.x,
                        t.y, Thing.d))
                        throw new LoadWorldException("Error Validating World",
                            "Thing (X: " + t.x + ", Y: " + t.y
                                + ") intersects one of the walls!");
                for (Box b : boxes)
                    if (World.lineIntersectsCircle(w.x1, w.y1, w.x2, w.y2, b.x,
                        b.y, Box.d))
                        throw new LoadWorldException("Error Validating World",
                            "Box (X: " + b.x + ", Y: " + b.y
                                + ") intersects one of the walls!");
            }
            
            for (Robot r : robots) {
                for (Thing t : things)
                    if (World.circlesIntersect(r.x, r.y, Robot.d, t.x, t.y,
                        Thing.d))
                        throw new LoadWorldException("Error Validating World",
                            "Robot (ID: " + r.id
                                + ") intersects one of the things!");
                for (Box b : boxes)
                    if (World.circlesIntersect(r.x, r.y, Robot.d, b.x, b.y,
                        Box.d))
                        throw new LoadWorldException("Error Validating World",
                            "Robot (ID: " + r.id
                                + ") intersects one of the boxes!");
            }
            
            for (Thing t : things)
                for (Box b : boxes)
                    if (World.circlesIntersect(t.x, t.y, Thing.d, b.x, b.y,
                        Box.d))
                        throw new LoadWorldException("Error Validating World",
                            "Thing (X: " + t.x + ", Y: " + t.y
                                + ") intersects one of the boxes!");
            
            if (robots.size() < 1)
                throw new LoadWorldException("Error Validating World",
                    "At least one ROBOT is required! None found.");
            
            if (boxes.size() < 1)
                throw new LoadWorldException("Error Validating World",
                    "At least one BOX is required! None found.");
        } catch (LoadWorldException e) {
            validWorldLoaded = false;
            width = originalWidth;
            height = originalHeight;
            robots = originalRobots;
            walls = originalWalls;
            things = originalThings;
            boxes = originalBoxes;
            
            JOptionPane.showMessageDialog(null, e.errorMessage, e.errorTitle,
                JOptionPane.ERROR_MESSAGE);
            
            return false;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "Cannot load world file. Not found.", "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        validWorldLoaded = true;
        
        return true;
    }
    
    public void moveRobot(int id, int move, Graphics g) {
        for (Robot r : robots)
            if (r.id == id) {
                double x1 = r.x;
                double y1 = r.y;
                
                int pos = 0;
                
                double dx = Math.sin((Math.PI * r.angleFromTop) / 180.0);
                double dy = Math.cos((Math.PI * r.angleFromTop) / 180.0);
                
                movestep:
                while (((move > 0) && (++pos < move))
                    || ((move < 0) && (--pos > move))) {
                    double oriX = r.x;
                    double oriY = r.y;
                    
                    r.x = (x1 - (pos * dx));
                    r.y = (y1 - (pos * dy));
                    
                    // Check for collisions before moving on!
                    if ((r.x < (Robot.d / 2))
                        || (r.x > (width - (Robot.d / 2)))
                        || (r.y < (Robot.d / 2))
                        || (r.y > (height - (Robot.d / 2)))) {
                        r.x = oriX;
                        r.y = oriY;
                        break movestep; // We need to stay within world!
                    }
                    
                    for (Wall w : walls)
                        if (World.lineIntersectsCircle(w.x1, w.y1, w.x2, w.y2,
                            r.x, r.y, Robot.d, true)) {
                            r.x = oriX;
                            r.y = oriY;
                            break movestep; // We need to avoid hitting walls!
                        }
                    
                    for (Thing t : things)
                        if (World.circlesIntersect(r.x, r.y, Robot.d, t.x, t.y,
                            Thing.d)) {
                            r.x = oriX;
                            r.y = oriY;
                            break movestep;
                        }
                    
                    for (Box b : boxes)
                        if (World.circlesIntersect(r.x, r.y, Robot.d, b.x, b.y,
                            Box.d)) {
                            r.x = oriX;
                            r.y = oriY;
                            break movestep;
                        }
                    
                    for (Robot otherRobot : robots)
                        if ((otherRobot != r)
                            && World.circlesIntersect(r.x, r.y, Robot.d,
                                otherRobot.x, otherRobot.y, Robot.d)) {
                            r.x = oriX;
                            r.y = oriY;
                            break movestep;
                        }
                    
                    if (g != null)
                        // Animate!
                        try {
                            Thread.sleep(10);
                            
                            int bound = 25;
                            this.drawWorld(g, r.x - bound, r.y - bound, r.x
                                + bound, r.y + bound);
                        } catch (InterruptedException e) {}
                }
                this.drawWorld(g); // Final redraw is full one! Only animation cares
                                   // about flicker!
                return;
            }
    }
    
    public int numberOfThingsNotInBoxes() {
        return things.size();
    }
    
    public void pickUp(int robotID, Graphics g) {
        for (Robot r : robots)
            if (r.id == robotID)
                for (Thing t : things)
                    if (World
                        .circlesClose(r.x, r.y, Robot.d, t.x, t.y, Thing.d)) {
                        r.numThings++;
                        things.remove(t);
                        this.drawWorld(g);
                        return;
                    }
    }
    
    public boolean touchingBox(int robotID) {
        for (Robot r : robots)
            if (r.id == robotID) {
                for (Box b : boxes)
                    if (World.circlesClose(r.x, r.y, Robot.d, b.x, b.y, Box.d))
                        return true;
                return false;
            }
        return false;
    }
    
    public boolean touchingRobot(int robotID) {
        for (Robot r : robots)
            if (r.id == robotID) {
                for (Robot other : robots)
                    if ((r != other)
                        && World.circlesClose(r.x, r.y, Robot.d, other.x,
                            other.y, Robot.d))
                        return true;
                return false;
            }
        return false;
    }
    
    public boolean touchingThing(int robotID) {
        for (Robot r : robots)
            if (r.id == robotID) {
                for (Thing t : things)
                    if (World
                        .circlesClose(r.x, r.y, Robot.d, t.x, t.y, Thing.d))
                        return true;
                return false;
            }
        return false;
    }
    
    public boolean touchingWall(int robotID) {
        for (Robot r : robots)
            if (r.id == robotID) {
                for (Wall w : walls)
                    if ((World.distanceFromPointToLine(r.x, r.y, w.x1, w.y1,
                        w.x2, w.y2) - Robot.d) < 2.0)
                        if ((((World.distanceFromPointToPoint(r.x, r.y, w.x1,
                            w.y1) - Robot.d) + World.distanceFromPointToPoint(
                            r.x, r.y, w.x2, w.y2)) - Robot.d) < (World
                            .distanceFromPointToPoint(w.x1, w.y1, w.x2, w.y2) + 2.0))
                            return true;
                return false;
            }
        return false;
    }
    
    public void turnRobot(int id, int turn, Graphics g) {
        for (Robot r : robots)
            if (r.id == id) {
                while (turn > 0) {
                    r.angleFromTop++;
                    turn--;
                    
                    if (g != null)
                        // Animate!
                        try {
                            Thread.sleep(10);
                            
                            int bound = 25;
                            this.drawWorld(g, r.x - bound, r.y - bound, r.x
                                + bound, r.y + bound);
                        } catch (InterruptedException e) {}
                }
                while (turn < 0) {
                    r.angleFromTop--;
                    turn++;
                    
                    if (g != null)
                        // Animate!
                        try {
                            Thread.sleep(1);
                            
                            int bound = 25;
                            this.drawWorld(g, r.x - bound, r.y - bound, r.x
                                + bound, r.y + bound);
                        } catch (InterruptedException e) {}
                }
                this.drawWorld(g); // Final redraw is full one! Only animation cares
                                   // about flicker!
                return;
            }
    }
    
    public void turnTowardsFirstBox(int robotID, Graphics g) {
        for (Robot r : robots)
            if (r.id == robotID) {
                Box b = boxes.get(0);
                int newAngle = World.directionTowardsPoint(r.x, r.y, b.x, b.y);
                turnRobot(r.id, newAngle - (int) Math.round(r.angleFromTop), g);
            }
    }
    
    public void turnTowardsFirstThing(int robotID, Graphics g) {
        if (things.size() == 0)
            return;
        for (Robot r : robots)
            if (r.id == robotID) {
                Thing t = things.get(0);
                int newAngle = World.directionTowardsPoint(r.x, r.y, t.x, t.y);
                turnRobot(r.id, newAngle - (int) Math.round(r.angleFromTop), g);
            }
    }
    
    public boolean validWorldLoaded() {
        return validWorldLoaded;
    }
    
    private void drawWorld(Graphics g, double x1, double y1, double x2,
        double y2) {
        if (g == null)
            return; // Required for automatic marking script to work.
        if (validWorldLoaded) {
            g.setColor(Color.WHITE);
            g.fillRect((int) Math.round(x1), (int) Math.round(y1),
                (int) Math.round(x2 - x1), (int) Math.round(y2 - y1));
            
            // Now only draw things that cross the box! Remember that
            // only things inside the box could've moved by
            // assumption!
            for (Robot r : robots) {
                if (r.p.validProgramLoaded())
                    g.setColor(Color.ORANGE);
                else
                    g.setColor(Color.BLUE);
                g.drawOval((int) Math.round(r.x - (Robot.d / 2)),
                    (int) Math.round(r.y - (Robot.d / 2)),
                    (int) Math.round(Robot.d), (int) Math.round(Robot.d));
                g.drawString("ID: " + r.id + " (" + r.numThings + ")",
                    (int) Math.round(r.x - Robot.d),
                    (int) Math.round(r.y + Robot.d + 2));
                
                // Figure out the line direction from r.angleFromTop.
                g.drawLine(
                    (int) Math
                        .round(r.x
                            - ((Math.sin((Math.PI * r.angleFromTop) / 180.0) * Robot.d) / 2.0)),
                    (int) Math.round(r.y
                        - ((Math.cos((Math.PI * r.angleFromTop) / 180.0) * Robot.d) / 2.0)),
                    (int) Math.round(r.x), (int) Math.round(r.y));
            }
            
            for (Wall w : walls) {
                g.setColor(Color.RED);
                g.drawLine((int) Math.round(w.x1), (int) Math.round(w.y1),
                    (int) Math.round(w.x2), (int) Math.round(w.y2));
            }
            
            for (Thing t : things) {
                g.setColor(Color.GREEN);
                g.fillOval((int) Math.round(t.x - (Thing.d / 2)),
                    (int) Math.round(t.y - (Thing.d / 2)),
                    (int) Math.round(Thing.d), (int) Math.round(Thing.d));
            }
            
            for (Box b : boxes) {
                g.setColor(Color.BLACK);
                g.drawOval((int) Math.round(b.x - (Box.d / 2)),
                    (int) Math.round(b.y - (Box.d / 2)),
                    (int) Math.round(Box.d), (int) Math.round(Box.d));
                if (b.numThings < 10)
                    g.drawString("0" + b.numThings, (int) Math.round(b.x - 6),
                        (int) Math.round(b.y + 5));
                else
                    g.drawString("" + b.numThings, (int) Math.round(b.x - 6),
                        (int) Math.round(b.y + 5));
            }
            
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            g.setColor(Color.RED);
            g.drawString("No world is loaded.", 200, height / 2);
        }
    }
    
    private static boolean circlesClose(double x1, double y1, double d1,
        double x2, double y2, double d2) {
        return Math.hypot((x1 - x2), (y1 - y2)) < (((d1 + d2) / 2.0) + 2.0);
    }
    
    private static boolean circlesIntersect(double x1, double y1, double d1,
        double x2, double y2, double d2) {
        return ((((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2))) * 4) < ((d1 + d2) * (d1 + d2));
    }
    
    private static int directionTowardsPoint(double xOrigin, double yOrigin,
        double xTarget, double yTarget) {
        int angle1 = (int) Math
            .round((Math.acos((yOrigin - yTarget)
                / World.distanceFromPointToPoint(xOrigin, yOrigin, xTarget,
                    yTarget)) * 180.0)
                / Math.PI);
        int angle2 = (int) Math
            .round((Math.asin((xOrigin - xTarget)
                / World.distanceFromPointToPoint(xOrigin, yOrigin, xTarget,
                    yTarget)) * 180.0)
                / Math.PI);
        return (angle2 > 0) ? angle1 : -angle1;
    }
    
    // http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
    private static double distanceFromPointToLine(double x, double y,
        double x1, double y1, double x2, double y2) {
        return Math.abs(((x2 - x1) * (y1 - y)) - ((x1 - x) * (y2 - y1)))
            / Math.hypot((x2 - x1), (y2 - y1));
    }
    
    private static double distanceFromPointToPoint(double x1, double y1,
        double x2, double y2) {
        return Math.hypot((x1 - x2), (y1 - y2));
    }
    
    private static boolean lineIntersectsCircle(double x1, double y1,
        double x2, double y2, double x, double y, double d,
        boolean... tangentCountsAsIntersection) {
        // See:
        // http://mathworld.wolfram.com/Circle-LineIntersection.html
        // and check if intersection point is within segment or not,
        // since the original is for infinite line.
        
        // First make circle centre in (0,0) and adjust the line to be relative to
        // that.
        x1 -= x;
        x2 -= x;
        y1 -= y;
        y2 -= y;
        
        // Now follow the instructions on MathWorld.
        double d_x = x2 - x1;
        double d_y = y2 - y1;
        double d_r_2 = (d_x * d_x) + (d_y * d_y);
        double D = (x1 * y2) - (x2 * y1);
        
        double discriminant = ((d / 2) * (d / 2) * d_r_2) - (D * D);
        
        if (discriminant < 0)
            return false;
        
        if (discriminant == 0) { // tangent!
            if (tangentCountsAsIntersection.length == 0)
                return false; // default is tangent does not count as intersection
            for (boolean b : tangentCountsAsIntersection) {
                if (b)
                    break;
                
                return false; // tangent does not count as intersection
            }
        }
        
        // Need to check if the two intersection points are within
        // the segment or not.
        
        int sgn = (d_y < 0) ? -1 : 1;
        
        double xi1 = ((D * d_y) + (sgn * d_x * Math.sqrt(discriminant)))
            / d_r_2;
        double yi1 = ((-D * d_x) + (Math.abs(d_y) * Math.sqrt(discriminant)))
            / d_r_2;
        
        boolean xInside = false;
        boolean yInside = false;
        if (x1 < x2)
            xInside = ((x1 <= xi1) && (xi1 <= x2));
        else
            xInside = ((x2 <= xi1) && (xi1 <= x1));
        if (y1 < y2)
            yInside = ((y1 <= yi1) && (yi1 <= y2));
        else
            yInside = ((y2 <= yi1) && (yi1 <= y1));
        if (xInside && yInside)
            return true;
        
        double xi2 = ((D * d_y) - (sgn * d_x * Math.sqrt(discriminant)))
            / d_r_2;
        double yi2 = ((-D * d_x) - (Math.abs(d_y) * Math.sqrt(discriminant)))
            / d_r_2;
        
        if (x1 < x2)
            xInside = ((x1 <= xi2) && (xi2 <= x2));
        else
            xInside = ((x2 <= xi2) && (xi2 <= x1));
        if (y1 < y2)
            yInside = ((y1 <= yi2) && (yi2 <= y2));
        else
            yInside = ((y2 <= yi2) && (yi2 <= y1));
        if (xInside && yInside)
            return true;
        
        return false;
    }
}


class Box {
    
    public double x;
    public double y;
    public int numThings;
    
    public final static double d = 25;
}


class Robot {
    
    public double x;
    public double y;
    public int id;
    public int numThings;
    public double angleFromTop;
    public RobotProgram p;
    
    public final static double d = 20;
}


class Thing {
    
    public double x;
    public double y;
    
    public final static double d = 10;
}


class Wall {
    
    public double x1, y1, x2, y2;
}
