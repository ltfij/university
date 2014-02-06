package com.hjwylde.uni.comp103.assignment06.centipede;

/*
 * Code for Assignment 6, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.hjwylde.uni.comp103.util.DrawingCanvas;

/**
 * Simulation of a collection of centipedes, squirming around the screen looking
 * for food sources.
 * If they find a food source, they eat it. Different kinds of food will have
 * different effects.
 * 
 * This program is related to some of the earlier computer games (now appearing
 * again on a cellphone
 * near you), but doesn't have a game element. (Of course, you could add one.)
 * 
 * The centre of the program is the run() method that loops forever, controlling
 * the simulation. On
 * each cycle ("clock tick") it - moves the centipedes, - makes them eat if at a
 * food source, -
 * redraws them, and then - waits for the next clock tick.
 * 
 * Several of the buttons (split, grow, shrink, reverse) are just for testing
 * your code - they will
 * perform the operation on the first centipede, regardless of the food.
 * 
 * Note: Because the run() method is called from the main method, it is in a
 * different thread from
 * the buttons. That is why the buttons still work, even while the simulation is
 * running.
 */

public class Simulation implements ActionListener {
    
    // Fields
    private final JFrame frame;
    private final DrawingCanvas canvas;
    
    private Set<Centipede> centipedes;
    private List<FoodSource> foodSources; // size and color of each food source.
    private Quadtree foodSourcesTree;
    private final int jumpSize = 40;
    
    private boolean keepRunning = true;
    
    public static int westEdge = 0; // the edges of the screen
    
    public static int eastEdge = 800;
    
    public static int northEdge = 0;
    
    public static int southEdge = 600;
    
    // Constructors
    /**
     * Construct a new CentipedeSimulation object and set up the GUI
     */
    public Simulation() {
        frame = new JFrame("Centipede Frenzy");
        frame.setSize(Simulation.eastEdge + 5, Simulation.southEdge + 70);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // The graphics area
        canvas = new DrawingCanvas();
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new java.awt.GridLayout(2, 0));
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        
        addButton(buttonPanel, "Reset", Color.black);
        addButton(buttonPanel, "Add", Color.black);
        
        addButton(buttonPanel, "Grow", Color.green);
        addButton(buttonPanel, "Shorten", Color.blue);
        addButton(buttonPanel, "Shrink", Color.magenta);
        addButton(buttonPanel, "Jump", Color.red);
        addButton(buttonPanel, "Split", Color.orange);
        addButton(buttonPanel, "Reverse", Color.black);
        addButton(buttonPanel, "Remove", Color.black);
        addButton(buttonPanel, "Quit", Color.black);
        
        frame.setVisible(true);
    }
    
    // GUI Methods
    
    /** Respond to button presses */
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Reset"))
            reset();
        
        else if (cmd.equals("Add"))
            centipedes.add(new Centipede());
        
        else if (cmd.equals("Quit")) {
            keepRunning = false; // to make the main thread stop
            frame.dispose();
        } else {
            if (centipedes.size() == 0)
                return;
            
            Centipede first = centipedes.iterator().next();
            // The buttons to test the actions
            if (cmd.equals("Jump"))
                first.jump(jumpSize, jumpSize);
            
            else if (cmd.equals("Grow"))
                first.grow();
            
            else if (cmd.equals("Split")) {
                Centipede newCp = first.split();
                if (newCp != null)
                    centipedes.add(newCp);
            }
            
            else if (cmd.equals("Shrink"))
                first.shrink();
            
            else if (cmd.equals("Shorten"))
                first.shorten();
            
            else if (cmd.equals("Reverse")) {
                centipedes.remove(first);
                Centipede newHead = first.reverse();
                centipedes.add(newHead);
            } else if (cmd.equals("Remove"))
                centipedes.remove(first);
            
            else
                throw new RuntimeException("No such button: " + cmd);
        }
    }
    
    /**
     * For each centipede and each food source, check if the centipede is at the
     * food source. If so,
     * perform the appropriate action on the centipede, depending on the type of
     * the food source.
     */
    public void eat() {
        Set<Centipede> toAdd = new HashSet<>();
        Set<Centipede> toRemove = new HashSet<>();
        
        for (Centipede c : centipedes)
            // Finds any FoodSource index within the centipede's head's bounds
            for (Integer i : foodSourcesTree.getAllWithin(new Rectangle(c
                .getX(), c.getY(), c.getRadius(), c.getRadius()))) {
                FoodSource food = foodSources.get(i); // Get the FoodSource for this
                                                      // index
                if (food.mature()) {
                    food.eaten();
                    switch (food.getType()) {
                    case Spicy:
                        c.jump(jumpSize, jumpSize);
                        break;
                    case Nutritious:
                        c.grow();
                        break;
                    case Wasting:
                        c.shorten();
                        break;
                    case Poison:
                        c.shrink();
                        break;
                    case Divisive:
                        Centipede newCp = c.split();
                        if (newCp != null)
                            toAdd.add(newCp);
                        break;
                    case BadTaste:
                        Centipede reversedCenti = c.reverse();
                        if (reversedCenti != null) {
                            toRemove.add(c);
                            toAdd.add(reversedCenti);
                        }
                    }
                }
            }
        
        centipedes.removeAll(toRemove);
        centipedes.addAll(toAdd);
    }
    
    /**
     * Find two centipedes near each other. If one is larger than the other, then
     * remove the smaller
     * one from the set of centipedes.
     */
    public void fight() {
        /*
         * The reason it would be simpler to kill off one centipede at a time is
         * because when iterating
         * through all of the centipedes, if you've added one to the "toRemove"
         * array, then technically
         * it shouldn't be allowed to "fight" any other centipedes near it, but it
         * will unless this is
         * accounted for. The option when not accounting for this is to just remove
         * one centipede at a
         * time and continually check fight() until no more are fighting, and all
         * the centipedes that
         * got eaten have been removed. In my code, I have included a check for
         * "if (!toRemove.contains(centipede))", so if a centipede has lost a fight,
         * it is unable to
         * fight any other centipedes or be fought against, so this is not a
         * problem.
         */
        Set<Centipede> toRemove = new HashSet<>();
        
        int cSize;
        int dSize;
        for (Centipede c : centipedes) { // For every centipede...
            if (toRemove.contains(c)) // If centipede has already been eaten...
                continue; // ...continue to the next centipede.
                
            cSize = c.size(); // To prevent many unneeded recursive calls inside the
                              // centipede class.
            for (Centipede d : centipedes) { // Check the centipede against every
                                             // other centipede...
                if ((c == d) || toRemove.contains(d)) // If the centipede to check
                                                      // against is the same or is
                                                      // already eaten...
                    continue; // ...continue to the next centipede.
                    
                dSize = d.size();
                
                if (c.near(d))
                    if (cSize > dSize) { // Only do something in one direction, when first
                                         // iterator gets to d,
                                         // then
                                         // it will cover the case where dSize > cSize.
                        for (int i = 0; i < dSize; i++)
                            c.grow();
                        
                        toRemove.add(d);
                    }
            }
        }
        
        centipedes.removeAll(toRemove);
    }
    
    public void redraw() {
        canvas.clear(false);
        for (FoodSource fs : foodSources)
            fs.redraw(canvas);
        
        for (Centipede c : centipedes)
            c.redraw(canvas);
        
        canvas.display();
    }
    
    /**
     * Create a new Set of food items at random places and a new Set with one
     * centipede, and display
     * them.
     */
    
    public void reset() {
        int num = Integer.parseInt(JOptionPane
            .showInputDialog("Number of Food Sources:"));
        reset(num);
    }
    
    public void reset(int numFoodSources) {
        int q = Math.max(Simulation.eastEdge - Simulation.westEdge,
            Simulation.southEdge - Simulation.northEdge); // Select the
        // largest out
        // of width
        // and height.
        // A Quadtree needs to work with squares, not rectangles, while it would be
        // possible to set up a
        // Quadtree to work with rectangles, it is easier just to use a Quadtree
        // with a longer height or
        // width to make it a square. Also for how the division of the squares works
        // in my Quadtree
        // implementation, currently it only properly works with powers of 2, so we
        // further round q up
        // to the next power of 2.
        // Reference:
        // http://graphics.stanford.edu/~seander/bithacks.html#RoundUpPowerOf2
        // Code used to round up to the next power of 2 integer.
        q--;
        q |= q >> 1;
        q |= q >> 2;
        q |= q >> 4;
        q |= q >> 8;
        q |= q >> 16;
        q++;
        // Reference end.
        
        foodSources = new ArrayList<>();
        foodSourcesTree = new Quadtree(new Rectangle(Simulation.westEdge,
            Simulation.northEdge, q, q), 9);
        centipedes = new HashSet<>();
        for (int i = 0; i < numFoodSources; i++) {
            FoodSource fs = new FoodSource();
            Rectangle fsBounds = new Rectangle(fs.getX(), fs.getY(),
                fs.getSize(), fs.getSize());
            
            foodSources.add(fs);
            foodSourcesTree.insert(i, fsBounds);
        }
        
        // If you wish to see how the foodSources are displayed on the Quadtree,
        // uncomment this:
        // foodSourcesTree.dumpTree();
        // System.out.println(foodSourcesTree.getAllWithin(foodSourcesTree.getBounds()).size());
        centipedes.add(new Centipede());
        redraw();
    }
    
    /**
     * A never ending simulation loop that waits for the next tick of the
     * simulation, then - moves
     * each centipede, possibly changing its direction - grows each food source -
     * checks each
     * centipede to see if it is next to some food if so, makes it eat the food,
     * possibly adding a new
     * centipede to the bag of centipedes.
     */
    public void run() {
        while (keepRunning) {
            // move each of the centipedes
            for (Centipede c : centipedes)
                c.move();
            
            // grow each of the food sources
            for (FoodSource fs : foodSources)
                fs.grow();
            
            eat(); // check each centipede against each food supply
            
            fight();
            
            // redraw everything
            redraw();
            try {
                Thread.sleep(100);
            } catch (Exception e) {}
        }
    }
    
    /** Helper method for adding buttons */
    private JButton addButton(JPanel panel, String name, Color clr) {
        JButton button = new JButton(name);
        button.addActionListener(this);
        button.setForeground(clr);
        panel.add(button);
        return button;
    }
    
    public static void main(String args[]) {
        Simulation cs = new Simulation();
        cs.reset(40);
        cs.run();
    }
    
}
