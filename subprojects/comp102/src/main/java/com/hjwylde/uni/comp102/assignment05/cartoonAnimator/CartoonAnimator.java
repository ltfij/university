package com.hjwylde.uni.comp102.assignment05.cartoonAnimator;

/*
 * Code for Assignment 5, COMP 102 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JFrame;

import com.hjwylde.uni.comp102.util.DrawingCanvas;
import com.hjwylde.uni.comp102.util.FileChooser;

/**
 * Displays an animated cartoon of two characters from a script in a file. The first two lines of
 * the file are the names (imagefile names) and initial positions of the two characters. Each
 * remainin line in the file contains the name of a character, followed by a command. If the command
 * is "move", it is followed by a distance to move If the command is "say", it is followed by the
 * text to say.
 */
public class CartoonAnimator {

    JFrame frame = new JFrame("Cartoon Animation");
    DrawingCanvas canvas = new DrawingCanvas();

    private final ArrayList<NamedFigure> figures = new ArrayList<>();

    public CartoonAnimator() {
        frame.setSize(700, 400);
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * animateFromFile first creates a JFrame window and a DrawingCanvas It then opens a script
     * file, and reads the names and intial positions of the two characters from the first two lines
     * of the file. It then creates two CartoonFigure objects, using the names as the imageNames and
     * places them on the window. It then has a loop to read each instruction from the script file:
     * Each instruction starts with the name of the character, followed by a command: left, right,
     * move, smile, frown, or talk If the command is move, it must be followed by a single integer
     * (the distance to move). If the command is talk, it must be followed by some words to say. It
     * must call the command on the correct CartoonFigure object. The loop will exit when there are
     * no more instructions.
     */
    public void animateFromFile() {
        try (Scanner scan = new Scanner(new File(FileChooser.open()))) { // open the script file
            while (scan.hasNext())
                readLine(new StringTokenizer(scan.nextLine()));
        } catch (IOException e) {
            System.out.println("File reading failed: " + e);
        }
        frame.dispose();
    }

    /**
     * Creates a Figure object from a script line and adds it to an array for use.
     */
    private void createFigure(String line) {
        StringTokenizer figureInitiator = new StringTokenizer(line);

        NamedFigure namedFigure =
                new NamedFigure(figureInitiator.nextToken(), new CartoonFigure(canvas,
                        figureInitiator.nextToken(), Integer.parseInt(figureInitiator.nextToken()),
                        Integer.parseInt(figureInitiator.nextToken())));

        figures.add(namedFigure);
    }

    /**
   * 
   */
    private void readLine(StringTokenizer line) {
        String name = line.nextToken();

        // If the line is creating a new figure:
        String code = line.nextToken().toLowerCase(Locale.ENGLISH);
        if (code.equals("green") || code.equals("blue") || code.equals("yellow")) {
            createFigure(name + " " + code + " " + line.nextToken() + " " + line.nextToken());
            return;
        }

        // Find the correct figure to use on the line in the script:
        CartoonFigure currentFigure = null;
        for (NamedFigure figure : figures)
            if (figure.getName().equalsIgnoreCase(name))
                currentFigure = figure.getCartoonFigure();

        if (currentFigure == null) {
            System.err.println("Cannot use figure: " + name + ". It has not yet been created.");
            return;
        }

        // If the <code> is to enter the figure into the world...
        // Else If the figure has not yet been entered into the world... Exit.
        if (code.equals("enter"))
            currentFigure.enterWorld();
        else if (!currentFigure.hasEntered()) {
            System.err
                    .println("Cannot use figure: " + name + ". It has not yet entered the world.");
            return;
        }

        // Executes script line for <name> calling the function on the <name>'s
        // figure:
        if (code.equals("smile"))
            currentFigure.smile();
        else if (code.equals("frown"))
            currentFigure.frown();
        else if (code.equals("left"))
            currentFigure.turnLeft();
        else if (code.equals("right"))
            currentFigure.turnRight();
        else if (code.equals("move"))
            currentFigure.move(Integer.parseInt(line.nextToken()));
        else if (code.equals("talk")) {
            String message = line.nextToken();
            while (line.hasMoreTokens())
                message = message + " " + line.nextToken();
            currentFigure.talk(message);
        } else
            System.err.println("Unrecognized code: " + code);
    }

    // Main
    public static void main(String[] arguments) {
        CartoonAnimator ob = new CartoonAnimator();
        ob.animateFromFile();
    }

    /**
     * The NamedFigure class represents a CartoonFigure with a name, allowing them to be searched
     * for by a specific name.
     */
    private class NamedFigure {

        private String name;
        private CartoonFigure cartoonFigure;

        /**
         * Creates a new NamedFigure from <code>name</code> and <code>cartoonFigure</code>.
         * 
         * @param name the name of the cartoonFigure
         * @param cartoonFigure the cartoon figure.
         */
        public NamedFigure(String name, CartoonFigure cartoonFigure) {
            this.name = name;
            this.cartoonFigure = cartoonFigure;
        }

        /**
         * Returns the cartoonFigure of this NamedFigure.
         * 
         * @return the cartoonFigure.
         */
        public CartoonFigure getCartoonFigure() {
            return cartoonFigure;
        }

        /**
         * Returns the name of this NamedFigure.
         * 
         * @return the name.
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the cartoon figure of this NamedFigure to <code>cartoonFigure</code> .
         */
        @SuppressWarnings("unused")
        public void setCartoonFigure(CartoonFigure cartoonFigure) {
            this.cartoonFigure = cartoonFigure;
        }

        /**
         * Sets the name of this NamedFigure to <code>name</code>.
         * 
         * @param name the name to set to.
         */
        @SuppressWarnings("unused")
        public void setName(String name) {
            this.name = name;
        }
    }
}
