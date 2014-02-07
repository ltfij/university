package com.hjwylde.uni.swen221.assignment02.walker;

import java.awt.Point;
import java.util.Stack;

import maze.Board;
import maze.Direction;
import maze.View;
import maze.Walker;

/*
 * Code for Assignment 2, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * The TremauxWalker is a walker that implements a version of the Tremaux maze solving algorithm.
 * 
 * The algorithm essentially lays down a piece of string when it walks along the path. When it comes
 * to a junction, it picks a random alley or pathway to take. When it comes to a dead end, it will
 * go into a backtracking mode and go back along the path it came until it reaches a junction with a
 * pathway it hasn't taken before - this means that the string will be laid down twice on the
 * backtracked pathways. If it encounters a point where there is already some string laid down, it
 * will treat it as a wall / dead end and won't cross it.
 * 
 * This means that the algorithm will always find a solution if there is one, and will never cross a
 * path more than 2 times maximum. In the end, it will also know a direct-ish path from the start to
 * the target points as this will be the path that has only been laid with string once.
 * 
 * @author Henry J. Wylde
 * 
 * @see "http://en.wikipedia.org/wiki/Maze_solving_algorithm#Tr.C3.A9maux.27s_algorithm"
 * @see "http://www.astrolog.org/labyrnth/algrithm.htm"
 */
public class TremauxWalker extends Walker {

    private static final Point INITIAL_POINT = new Point(0, 0);

    /**
     * Keeps track of where the walker has been.
     */
    private BallOfString path = new BallOfString();

    /**
     * Keeps track of the direct path the walker is taking to the target.
     */
    private Stack<Point> solutionPath = new Stack<>();

    private boolean backtrackMode = false;

    /**
     * Creates a new TremauxWalker with the class as its name.
     */
    public TremauxWalker() {
        this(TremauxWalker.class.getName());
    }

    /**
     * Creates a new TremauxWalker with the specified name.
     * 
     * @param name the name of the walker.
     */
    public TremauxWalker(String name) {
        super(name);

        path.lineTo(TremauxWalker.INITIAL_POINT); // Record the starting point!
        solutionPath.push(path.getEnd()); // Got to record it in the stack too!
    }

    /*
     * Overwritten to print out solutionPath stack.
     * @see maze.Walker#solve(maze.Board)
     */
    @Override
    public void solve(Board board) {
        super.solve(board); // Solve the maze using the parents method.

        // Print out a path solution (not shortest path) that was found (not the path taken).

        System.out.print("Direct-ish path from goal to target:\n(");

        Point p;
        while (!solutionPath.isEmpty()) {
            p = solutionPath.remove(0);
            System.out.print("(" + p.x + ", " + p.y + ")");
            if (!solutionPath.isEmpty())
                System.out.print(", ");
        }

        System.out.println(")");
    }

    /*
     * @see maze.Walker#move(maze.View)
     */
    @Override
    protected Direction move(View view) {
        if (solutionPath.isEmpty()) // No solution.
            return null; // Remain at the start.

        if (!backtrackMode) {
            Direction dir = findAlley(view); // Find a clear alley we can take.

            if (dir != null) { // If there exists a clear alley...
                // Move in that alley's direction.
                path.lineIn(dir);
                solutionPath.push(path.getEnd());

                return dir;
            }
        } else if (findAlley(view) == null) { // (We are in backtrack mode) If there still isn't a
                                              // new
                                              // clear alley to take...
            solutionPath.pop(); // Take the last move off the solution stack.
            if (solutionPath.isEmpty()) // Check if we're at the start of the maze (ie. no
                                        // solution).
                return null;

            path.lineTo(solutionPath.peek()); // Move the path history to the current position.

            return path.getDirection();
        }

        // We get here only if we're alternating the backtrack mode.

        backtrackMode = !backtrackMode;

        return null; // Don't move, just get the solve(Board) method to call move(View) again with
                     // the
                     // changed backtrack variable.
    }

    /**
     * Finds a clear alley for the walker to go through. A clear alley is one that it is possible to
     * move to (ie. no wall blocking it) and one that the walker has not already been to. It is
     * checked by checking the <code>path</code> history. Returns <code>null</code> if there is no
     * clear alley.
     * 
     * @param view the maze view.
     * @return the direction of a clear alley or null if there isn't any.
     */
    private Direction findAlley(View view) {
        for (Direction dir : Direction.values())
            // Check for each direction if we can move, and if we haven't been there before.
            if (view.mayMove(dir)
                    && !path.hasPassedPoint(BallOfString.getNextPoint(path.getEnd(), dir)))
                return dir;

        return null; // No clear alley...
    }
}
