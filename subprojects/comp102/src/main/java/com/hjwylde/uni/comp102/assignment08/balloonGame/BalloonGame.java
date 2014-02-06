package com.hjwylde.uni.comp102.assignment08.balloonGame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import com.hjwylde.uni.comp102.util.DrawingCanvas;

/*
 * Code for Assignment 8, COMP 102
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Program for a simple game in which the player has to blow up balloons on the
 * screen. The game starts with a collection of randomly placed small balloons
 * (coloured circles) on the canvas. The player then clicks on balloons to blow
 * them up by a small amount (random increase in the radius between 2 and 6
 * pixels). If two balloons ever touch, then they "burst" and disappear. At any
 * time, the player may choose to stop and "lock in" their score. The goal is to
 * get the largest score. The score is the total of the sizes (areas) of all the
 * balloons left on the screen at the end, minus the sizes of the balloons that
 * burst. At each step, the current score is recalculated and displayed, along
 * with the highest score that the player has achieved so far.
 * 
 * The BalloonGame class uses an array of Balloon objects to represent the
 * current set of Balloons on the screen.
 * 
 * The Finish button should finish the current game and start a new one
 * 
 * Clicking (ie, releasing) the mouse on the canvas should do the following Find
 * out if the mouse was clicked on top of any balloon. If so, Make the balloon a
 * bit larger Check whether the balloon is touching any other balloon. If so pop
 * the balloons (which will make them disappear from the canvas) add the sizes
 * of the popped balloons to the penalty remove the popped Balloons from the
 * array Recalculate and redisplay the score If all the balloons are gone, the
 * game is over.
 * 
 * To start a game, the program should Clear the canvas Initialise the penalty
 * Make a new set of Balloons at random positions Report the score
 * 
 * If the game is over, the program should Remember the current score if it is
 * higher than the high score, Report the score, Start a new game.
 * 
 * Note, the Balloon class is written for you. Make sure that you know all its
 * methods.
 */
public class BalloonGame implements ActionListener, MouseListener {
    
    // Fields
    private final JFrame frame = new JFrame("Balloon Game");
    private final DrawingCanvas canvas = new DrawingCanvas();
    private final JTextArea scoreArea = new JTextArea(1, 80);
    
    private double penalty; // the total area of the balloons that have been
    // burst.
    private int highScore; // highest score in game.
    private final int maxBalloons = 20;
    
    // the collection of balloons:
    private Balloon[] balloons = new Balloon[maxBalloons];
    
    // Constructors
    /**
     * Construct a new BalloonGame object and set up the GUI
     */
    public BalloonGame() {
        frame.setSize(700, 700);
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        frame.getContentPane().add(scoreArea, BorderLayout.SOUTH);
        
        canvas.addMouseListener(this);
        
        // The buttons
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        
        addButton(panel, "Finish");
        addButton(panel, "Quit");
        
        frame.setVisible(true);
        
        start();
    }
    
    // GUI Methods
    
    /** Respond to button presses */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Finish"))
            gameOver();
        else if (cmd.equals("Quit"))
            frame.dispose();
    }
    
    /**
     * Count and return the number of Balloons in the array
     * 
     * @return the number of non popped balloons.
     */
    public int countBalloons() {
        int count = 0;
        for (Balloon balloon : balloons)
            if (!balloon.isPopped())
                count++;
        return count;
    }
    
    /**
     * Find the balloon at (x,y) if any, Expand it Check whether it is touching
     * another balloon, If so, update the penalty, pop both balloons, and remove
     * them from the array Report the current score. If there are no balloons
     * left, this game is over.
     * 
     * @param x x co-ordinate.
     * @param y y co-ordinate.
     */
    public void doStep(int x, int y) {
        Balloon clickedBalloon = getClickedBalloon(x, y);
        if (clickedBalloon == null)
            return;
        
        clickedBalloon.expand();
        for (Balloon balloon : balloons) {
            
            if (balloon.isPopped())
                continue;
            
            if (clickedBalloon.touches(balloon) && (clickedBalloon != balloon)) {
                penalty += clickedBalloon.size() + balloon.size();
                clickedBalloon.pop();
                balloon.pop();
            }
        }
        
        // Report the score
        reportScore();
        
        // If no balloons left, then game is over.
        if (countBalloons() == 0)
            gameOver();
    }
    
    /**
     * Report the Score Remember score if greater than current highScore), Pop all
     * the balloons, clear the screen, and start again
     */
    public void gameOver() {
        int score = score();
        JOptionPane.showMessageDialog(frame, "Score was " + score);
        if (score() > highScore)
            highScore = score;
        start();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {} // needed to satisfy interface
    
    @Override
    public void mouseEntered(MouseEvent e) {} // needed to satisfy interface
    
    @Override
    public void mouseExited(MouseEvent e) {} // needed to satisfy interface
    
    // Other Methods
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    /** Respond to mouse events */
    
    @Override
    public void mouseReleased(MouseEvent e) {
        doStep(e.getX(), e.getY());
    }
    
    /** Report the score in the scoreArea at the bottom of the window */
    public void reportScore() {
        scoreArea.setText("Score = " + score() + "    High score = "
            + highScore);
    }
    
    /**
     * Compute and return the current score: Sum of the sizes for the current
     * balloons, minus the penalty (total area of burst balloons)
     * 
     * @return the score.
     */
    public int score() {
        double ans = 0;
        for (Balloon balloon : balloons)
            if (!balloon.isPopped())
                ans += balloon.size();
        
        return (int) (ans - penalty) / 100;
    }
    
    /**
     * Start the game: Clear the canvas Initialise the penalty Make a new set of
     * Balloons at random positions Report the score
     */
    public void start() {
        canvas.clear();
        penalty = 0;
        
        boolean isTouching;
        int i = 0;
        while (i < maxBalloons) {
            balloons[i] = new Balloon(canvas,
                (int) ((Math.random() * 600) + 1),
                (int) ((Math.random() * 600) + 1));
            isTouching = false;
            for (int j = 0; j < i; j++)
                if (balloons[i].touches(balloons[j]))
                    isTouching = true;
            
            if (!isTouching)
                i++;
        }
        redrawCanvas();
        
        reportScore();
    }
    
    /** Helper method for adding buttons */
    private JButton addButton(JPanel panel, String name) {
        JButton button = new JButton(name);
        button.addActionListener(this);
        panel.add(button);
        return button;
    }
    
    private Balloon getClickedBalloon(int x, int y) {
        for (Balloon balloon : balloons)
            if (!balloon.isPopped())
                if (balloon.on(x, y))
                    return balloon;
        
        return null;
    }
    
    private void redrawCanvas() {
        canvas.clear();
        
        for (int i = 0; i < maxBalloons; i++)
            balloons[i].draw();
    }
    
    // Main
    public static void main(String[] arguments) {
        new BalloonGame();
    }
    
}
