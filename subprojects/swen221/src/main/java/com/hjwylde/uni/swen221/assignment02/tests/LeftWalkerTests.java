package com.hjwylde.uni.swen221.assignment02.tests;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import maze.*;
import maze.gui.MazeWindow;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.assignment02.walker.LeftWalker;

/*
 * Code for Assignment 2, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Here are some simple test cases. You should add your own to get more confidence that it works!
 */
public class LeftWalkerTests {

    @Test
    public void maze1() {
        int[][] path = { {2, 2}, {1, 2}, {0, 2}, {0, 1}, {0, 0}};
        LeftWalkerTests.checkTest("3,3\n41,1,5\n8,0,4\n10,2,22\n", path);
    }

    @Test
    public void maze2() {
        int[][] path = { {2, 1}, {2, 0}};
        LeftWalkerTests.checkTest("3,3\n3,1,45\n8,8,28\n10,10,12", path);
    }

    @Test
    public void maze3() {
        int[][] path = { {2, 1}, {2, 0}, {2, 1}, {2, 2}};
        LeftWalkerTests.checkTest("3,3\n3,1,13\n8,8,28\n10,10,44", path);
    }

    @Test
    public void maze4() {
        int[][] path = { {2, 2}, {2, 1}, {2, 0}, {1, 0}, {1, 1}, {1, 2}};
        LeftWalkerTests.checkTest("3,3\n9,1,5\n8,8,12\n10,42,30", path);
    }

    @Test
    public void maze5() {
        int[][] path = { {1, 1}, {1, 0}, {2, 0}, {2, 1}, {2, 2}, {1, 2}, {0, 2}, {0, 1}, {0, 0}};
        LeftWalkerTests.checkTest("3,3\n41,1,5\n8,16,4\n10,2,6\n", path);
    }

    @Test
    public void maze6() {
        // This maze is a trickier one, since it requires memorisation to solve.
        int[][] path =
                { {2, 3}, {3, 3}, {3, 2}, {3, 1}, {2, 1}, {1, 1}, {1, 2}, {1, 3}, {2, 3}, {2, 4},
                        {1, 4}, {0, 4}, {0, 3}, {0, 2}, {0, 1}, {0, 0}, {1, 0}, {2, 0}, {3, 0},
                        {4, 0}, {4, 1}};
        LeftWalkerTests.checkTest(
                "5,5\n9,1,1,1,5\n8,0,0,0,36\n8,0,15,0,4\n8,0,16,0,4\n10,2,2,2,6\n", path);
    }

    @Test
    public void maze7() {
        int[][] path = { {2, 2}, {3, 2}, {3, 1}, {2, 1}, {1, 1}, {0, 1}, {0, 2}};

        LeftWalkerTests.checkTest(
                "5,5\n11,3,3,1,5\n9,3,3,4,12\n8,35,19,4,12\n8,3,5,12,12\n10,3,2,2,6\n", path);
    }

    @Test
    public void maze8() {
        int[][] path = { {1, 2}, {2, 2}, {2, 1}, {1, 1}};

        LeftWalkerTests.checkTest(
                "5,5\n9,3,3,1,5\n40,3,5,12,12\n8,19,4,12,12\n8,3,4,12,12\n10,3,2,2,6\n", path);
    }

    @Test
    public void maze9() {
        int[][] path = { {2, 0}, {3, 0}, {4, 0}, {4, 1}, {4, 2}, {4, 3}, {4, 4}, {3, 4}, {2, 4}};

        LeftWalkerTests.checkTest(
                "5,5\n9,1,19,1,5\n12,8,1,6,12\n10,2,2,7,12\n9,3,3,1,4\n10,35,3,2,6\n", path);
    }

    /**
     * The following method runs the LeftWalker and checks it against the correct path. If there is
     * any deviation the test fails.
     * 
     * @param inputMaze
     * @param correctPath
     */
    private static void checkTest(String inputMaze, int[][] correctPath) {

        System.out.println(Arrays.toString(Direction.values()));
        try {
            Board board = new Board(new StringReader(inputMaze));
            Walker walker = new LeftWalker();
            MazeWindow.getWindowAndShow(board);
            walker.solve(board);
            Path p = board.getPath();

            // First, need to generate the walker's path
            int[][] walkerPath = new int[p.getSteps()][2];
            Coordinate c;
            int idx = walkerPath.length - 1;
            while ((c = p.pop()) != null) {
                walkerPath[idx][0] = c.getX();
                walkerPath[idx][1] = c.getY();
                idx--;
            }

            // Now, check the walker's path was correct
            for (int i = 0; i != walkerPath.length; ++i)
                if ((i >= walkerPath.length) || (i >= correctPath.length)
                        || (walkerPath[i][0] != correctPath[i][0])
                        || (walkerPath[i][1] != correctPath[i][1]))
                    Assert.fail("walker path is: " + LeftWalkerTests.pathString(walkerPath)
                            + ", correct path is: " + LeftWalkerTests.pathString(correctPath));

        } catch (IOException e) {
            // ensure the maximum possible path length, so the test will fail.
            Assert.fail("io exceotion - " + e.getMessage());
        }
    }

    private static String pathString(int[][] path) {
        String r = "";
        boolean firstTime = true;
        for (int[] p : path) {
            if (!firstTime)
                r += ",";
            firstTime = false;
            r += "(" + p[0] + "," + p[1] + ")";
        }
        return r;
    }
}
