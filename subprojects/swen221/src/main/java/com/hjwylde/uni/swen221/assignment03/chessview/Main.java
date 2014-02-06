package com.hjwylde.uni.swen221.assignment03.chessview;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.hjwylde.uni.swen221.assignment03.chessview.viewer.BoardFrame;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Main {
    
    public static void main(String[] args) {
        try (FileReader fr = new FileReader(args[0])) {
            ChessGame game = new ChessGame(fr);
            new BoardFrame(game);
            // textView(game);
        } catch (IOException e) {
            System.err.println("Error loading file: " + args[0]);
            System.err.println(e.getMessage());
        }
    }
    
    public static void textView(ChessGame game) {
        try {
            List<Board> boards = game.boards();
            List<Round> rounds = game.rounds();
            
            System.out.println(boards.get(0));
            
            for (int i = 0; i != rounds.size(); ++i) {
                System.out.println("\n==================");
                Round r = rounds.get(i);
                System.out.println("WHITE PLAYS: " + r.white());
                if (((2 * i) + 1) >= boards.size())
                    throw new RuntimeException("Invalid move!");
                Board wb = boards.get((2 * i) + 1);
                System.out.println(wb);
                if (r.black() != null) {
                    System.out.println("\nBLACK PLAYS: " + r.black());
                    if (((2 * i) + 2) >= boards.size())
                        throw new RuntimeException("Invalid move!");
                    Board bb = boards.get((2 * i) + 2);
                    System.out.println(bb);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace(); // useful for debugging
        }
    }
}
