package com.hjwylde.uni.swen222.assignment02.cluedo;

import com.hjwylde.uni.swen222.assignment02.cluedo.game.Board;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Tile;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Tiles;
import com.hjwylde.uni.swen222.assignment02.cluedo.logic.GameEngine;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.CLI;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.UI;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.gui.GUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

/**
 * Main class.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 25/07/2013
 */
public final class Main {

    private static final String DEFAULT_BOARD_PATH = "/assignment02/board.txt";

    /**
     * This class cannot be instantiated.
     */
    private Main() {}

    /**
     * Attempts to load the board. This method will continually ask the user for a path to the board
     * until a valid one is given. First though, the default board path is attempted to be used.
     * 
     * @return the tiles for the board.
     */
    private static Tile[][] loadBoard() {
        Tile[][] tiles = null;

        try {
            tiles = Tiles.parseTiles(Main.class.getResourceAsStream(DEFAULT_BOARD_PATH));

            return tiles;
        } catch (IOException e) {
            // Use System.out not System.err so all print messages are queued correctly
            System.out.println("Unable to load board from: " + DEFAULT_BOARD_PATH);
            e.printStackTrace(System.out);
        }

        String path = null;

        // Continually ask for a path until the user enters a valid board path
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (tiles == null)
            try {
                System.out.println("\nPlease enter a path to load the board from:");
                path = in.readLine();

                tiles = Tiles.parseTiles(Paths.get(path));
            } catch (IOException e) {
                System.out.println("Unable to load board from: " + path);
                e.printStackTrace(System.out);
            }

        return tiles;
    }

    /**
     * Main.
     * 
     * @param args the program arguments.
     */
    public static void main(String[] args) {
        Board board = new Board(loadBoard());

        UI ui = null;

        if ((args.length > 0) && args[0].equals("-cli"))
            ui = new CLI(board, System.in, System.out);
        else {
            ui = new GUI(board);
            new Thread((Runnable) ui).start(); // TEMP
        }

        // We don't need to bother running in a separate thread
        new GameEngine(ui, board).run();
    }
}
