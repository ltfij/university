package com.hjwylde.uni.swen221.assignment02;

import java.awt.event.KeyListener;
import java.io.FileReader;

import maze.Board;
import maze.Walker;
import maze.gui.MazeWindow;

import com.hjwylde.uni.swen221.assignment02.walker.KeyWalker;
import com.hjwylde.uni.swen221.assignment02.walker.LeftWalker;
import com.hjwylde.uni.swen221.assignment02.walker.RandomWalker;
import com.hjwylde.uni.swen221.assignment02.walker.TremauxWalker;

/*
 * Code for Assignment 2, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Main class for the program.
 * 
 * @author ncameron
 */
public class Main {
    
    /**
     * The main method for this program
     */
    public static void main(String[] args) throws Exception {
        String fileName = null; // name of input file to read
        int width = 25; // default width of board to create
        int height = 25; // default height of board to create
        Mode mode = Mode.RANDOM;
        
        // ======================================================
        // ======== First, parse command-line arguments ========
        // ======================================================
        
        for (int i = 0; i != args.length; ++i)
            if (args[i].startsWith("-")) {
                String arg = args[i];
                if (arg.equals("-file"))
                    fileName = args[++i];
                else if (arg.equals("-width"))
                    width = Integer.parseInt(args[++i]);
                else if (arg.equals("-height"))
                    height = Integer.parseInt(args[++i]);
                else if (arg.equals("-keyw"))
                    mode = Mode.KEY;
                else if (arg.equals("-leftw"))
                    mode = Mode.LEFT;
                else if (arg.equals("-tremauxw"))
                    mode = Mode.TREMAUX;
                else
                    throw new RuntimeException("Unknown option: " + args[i]);
            }
        
        // ======================================================
        // ======= Second, create the board to be searched ======
        // ======================================================
        
        Board board;
        if (fileName != null)
            try (FileReader fr = new FileReader(fileName)) {
                board = new Board(fr);
            }
        else
            board = new Board(width, height);
        
        // ======================================================
        // ====== Third, create the walker to walk the maze =====
        // ======================================================
        
        Walker walker = null;
        if (mode == Mode.RANDOM)
            walker = new RandomWalker();
        else if (mode == Mode.KEY)
            walker = new KeyWalker();
        else if (mode == Mode.LEFT)
            walker = new LeftWalker();
        else
            walker = new TremauxWalker();
        
        // ======================================================
        // ============== Fourth, show the GUI ==================
        // ======================================================
        
        // Initialise the GUI and put it on the screen
        MazeWindow.getWindowAndShow(board);
        
        // Now, register the key walker
        if (walker instanceof KeyListener)
            MazeWindow.mainWindow.addKeyListener((KeyListener) walker);
        
        // ======================================================
        // ================== Fifth, solve the maze =============
        // ======================================================
        
        long time = System.currentTimeMillis(); // record start time
        
        // solve the maze!
        walker.solve(board);
        
        time = System.currentTimeMillis() - time; // subtract start time
                                                  // from current time
        System.out.println("Maze solved by " + walker.getName() + " in " + time
            + "ms");
        System.out.println("Solution has " + board.getPath().getSteps()
            + " steps.");
        
    }
}


enum Mode {
    RANDOM,
    KEY,
    LEFT,
    TREMAUX
}