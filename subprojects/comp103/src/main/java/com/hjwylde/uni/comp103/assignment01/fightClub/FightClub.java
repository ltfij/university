package com.hjwylde.uni.comp103.assignment01.fightClub;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.hjwylde.uni.comp103.util.DrawingCanvas;

/*
 * Code for Assignment 1, COMP 103 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class FightClub implements ActionListener, MouseListener {

    private final int arenaHeight = 600;
    private final int arenaWidth = 1000; // these will be the canvas dimensions.
    private final DrawingCanvas canvas;
    // The array of creatures
    private final List<Creature> creatures = new ArrayList<>();
    private boolean depleteResources = false, doOneStep = true;
    private boolean drawOldestFirst = false; // For use in determining draw order
                                             // of the Creatures.
    // Fields:
    // the window and the drawing canvas
    private final JFrame frame;
    private boolean increaseResources = false;
    private int index = 0;
    private final int numInitialCreatures = 20;

    private boolean quitNow = false, keepGoing = true, cosmicRay = false;
    // private int numStepsAltogether = 0;
    private final Random rng = new Random();
    private final int sleepLength = 10;

    // Constructor:
    /** sets up the window and drawing canvas, storing them in the fields. */
    public FightClub() {

        frame = new JFrame("FightClub");
        frame.setSize(arenaWidth, arenaHeight);
        // The graphics area
        canvas = new DrawingCanvas();
        canvas.addMouseListener(this);

        // Put the canvas into the frame.
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        // The buttons.
        JPanel buttonPanel = new JPanel(new java.awt.GridLayout(2, 0));
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        addButton(buttonPanel, "Quit");
        addButton(buttonPanel, "Step");
        addButton(buttonPanel, "Run");
        addButton(buttonPanel, "Cosmic ray");
        addButton(buttonPanel, "Increase resource");
        addButton(buttonPanel, "Halve resource");
        addButton(buttonPanel, "Toggle Draw Order");
        // this.addButton(buttonPanel, "Faster");
        // this.addButton(buttonPanel, "Slower");
        frame.setVisible(true);

        // Put some initial creatures into the world.
        for (int i = 0; i < numInitialCreatures; ++i)
            creatures.add(addNewCreature(null, index++));

        keepGoing = true;
        run();
    }

    // METHOD FOR THE ACTION LISTENER INTERFACE.
    /** Respond to button presses */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("Run"))
            keepGoing = true;
        else if (cmd.equals("Step")) {
            keepGoing = true;
            doOneStep = true;
        } else if (cmd.equals("Quit"))
            quitNow = true;
        else if (cmd.equals("Cosmic ray"))
            cosmicRay = true;
        else if (cmd.equals("Halve resource"))
            depleteResources = true;
        else if (cmd.equals("Increase resource"))
            increaseResources = true;
        else if (cmd.equals("Toggle draw order"))
            drawOldestFirst = !drawOldestFirst;
    }

    @Override
    public void mouseClicked(MouseEvent e) {} // needed to satisfy interface

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {} // needed to satisfy interface

    // METHODS FOR THE MOUSE LISTENER INTERFACE.
    @Override
    public void mousePressed(MouseEvent e) {} // needed for interface

    @Override
    public void mouseReleased(MouseEvent e) {} // needed for interface

    /** animate the simulation. */
    public void run() {
        while (true) {
            if ((doOneStep || keepGoing) && (creatures.size() > 1))
                step();

            // NOW CHECK A BUNCH OF FLAGS FOR OTHER STUFF TO DO....
            if (doOneStep) {
                keepGoing = false;
                doOneStep = false;
            }

            if (cosmicRay) {
                for (Creature dude : creatures)
                    if (rng.nextDouble() >= 0.5) // Make roughly half mutate themselves.
                        dude.mutateSelf(3.0);
                cosmicRay = false; // otherwise it'll do it again....!
            }

            if (increaseResources) {
                // Double everyone's resources
                for (Creature dude : creatures)
                    dude.setResource(dude.getResource() * 2);

                increaseResources = false; // So that it won't double them again.
            }

            if (depleteResources) {
                // Halve everyone's resources
                for (Creature dude : creatures)
                    dude.setResource(dude.getResource() / 2);

                depleteResources = false; // So that it won't halve them again.
            }

            if (quitNow) {
                frame.dispose();
                return;
            }

            // go to sleep for a bit: controls speed of simulation.
            try {
                Thread.sleep(sleepLength);
            } catch (InterruptedException e) {
            }
        }
    }

    public void step() {
        // ++numStepsAltogether;
        // (1) Sense and decide.
        for (Creature dude : creatures)
            dude.decideActions(creatures);

        // (2) Creatures move (but we don't re-draw them yet).
        for (Creature dude : creatures)
            dude.move();

        // (3) Creatures signal their attacks on others
        for (Creature dude : creatures)
            dude.attack();

        // (4) Update abilities, distribute resources if beaten,
        // and remove the beaten creatures.
        List<Creature> deadCreatures = new ArrayList<>();
        for (Creature dude : creatures)
            if (dude.updateAbilitiesAndResources() != null)
                deadCreatures.add(dude); // Add to a list, for removal from Creatures.

        creatures.removeAll(deadCreatures); // Remove after iterating through the
                                            // original list of
                                            // Creatures.
        deadCreatures.clear();

        // (5) replicate those creatures that can.
        List<Creature> newCreatures = new ArrayList<>();
        for (Creature dude : creatures)
            if (dude.pregnancyTest())
                newCreatures.add(addNewCreature(dude, index++)); // Add to a list, for
                                                                 // addition to
                                                                 // Creatures.

        creatures.addAll(newCreatures); // Add after iterating through the original
                                        // list of Creatures.
        newCreatures.clear();

        // Clear canvas, draw creatures on it, and redisplay
        canvas.clear(false); // clear canvas, but don't display it.
        for (int i = 0; i < creatures.size(); i++)
            // redraw everyone
            if (drawOldestFirst)
                creatures.get(creatures.size() - (i + 1)).draw(); // Simple calculation to iterate
                                                                  // through
                                                                  // the list back to front.
            else
                creatures.get(i).draw();

        canvas.display(); // actually display the canvas!
    }

    /**
     * Utility method to make new button and add it to the panel. Returns the button, in case we
     * need it.
     */
    private JButton addButton(JPanel panel, String name) {
        JButton button = new JButton(name);
        button.addActionListener(this);
        panel.add(button);
        return button;
    }

    // Note: a better way might be to recycle creatures when possible, to
    // avoid making and destroying so many objects and thus save some
    // work for the garbage collector. But here we'll keep it simple.
    // This method appears to do nothing much, but it makes creating
    // a new creature a bit simpler.
    private Creature addNewCreature(Creature dad, int index) {
        return new Creature(arenaWidth, arenaHeight, canvas, dad, index, rng);
    }

    /**
     * Main.
     * 
     * @param args arguments.
     */
    public static void main(String[] args) {
        new FightClub();
    }
}
