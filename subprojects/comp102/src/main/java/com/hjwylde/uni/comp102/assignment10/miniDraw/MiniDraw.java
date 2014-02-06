package com.hjwylde.uni.comp102.assignment10.miniDraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.Scanner;

import javax.swing.*;

import com.hjwylde.uni.comp102.util.DrawingCanvas;
import com.hjwylde.uni.comp102.util.FileChooser;

/*
 * Code for Assignment 10, COMP 102
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * The MiniDraw program allows the user to create, save, and reload files
 * specifying drawings consisting of a list of simple shapes. The program allows
 * the user to - add a new shape to the drawing - remove a shape from the
 * drawing - move a shape to a different position - set the colour for the next
 * shape - save the current drawing to a file - load a previous drawing from a
 * file. The shapes include lines, rectangles, ovals, and dots
 * 
 * Classes The MiniDraw class handles all the user interaction: buttons, mouse
 * actions, file opening and closing. It stores the current drawing in an array
 * of Shape .
 * 
 * The Shape interface specifies the Shape type The shape classes all implement
 * Shape and represent different kinds of shapes.
 * 
 * Files: A drawing is stored in a file containing one line for each shape, Each
 * line has the name of the type of shape, followed by a specification of the
 * shape, including the position (x and y) and the colour (three integers for
 * red, blue, and green). The other values will depend on the shape.
 * 
 * User Interface: There are two panels of buttons, one at the top and one at
 * the bottom: The top panel has buttons for dealing with the whole drawing
 * (New, Load, Save, Quit) and buttons for moving and removing shapes, and
 * setting the color. The bottom panel has buttons for specifying the next shape
 * to draw.
 */

public class MiniDraw implements ActionListener, MouseListener {
    
    // GUI.
    private final JFrame frame = new JFrame("MiniDraw");
    private final DrawingCanvas canvas = new DrawingCanvas();
    
    // Action parameters.
    private int pressedX;
    private int pressedY;
    private String currentAction = "Line";
    private Color currentColor = Color.black;
    
    // Shapes array.
    private static final int maxShapes = 200;
    private final Shape[] shapes = new Shape[MiniDraw.maxShapes];
    private int count = 0;
    
    /** Constructor sets up the GUI. */
    public MiniDraw() {
        initGUI();
    }
    
    /**
     * Respond to button presses
     * 
     * @param e
     *            the action event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String button = e.getActionCommand();
        
        if (button.equals("New"))
            newDrawing();
        else if (button.equals("Open"))
            try {
                openDrawing();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        else if (button.equals("Save"))
            try {
                saveDrawing();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        else if (button.equals("Color"))
            selectColor();
        else if (button.equals("Quit"))
            frame.dispose();
        else
            currentAction = button; // the name of the button is the action
    }
    
    /**
     * If there is room in the array, Construct a new Shape object of the
     * appropriate kind (depending on currentAction) Uses the appropriate
     * constructor of the Line, Rectangle, Oval, or Dot classes. Adds the shape it
     * to the collection of shapes in the drawing, and Renders the shape on the
     * canvas
     * 
     * @param x1
     *            the x co-ordinate of initial position of the mouse.
     * @param y1
     *            the y co-ordinate of initial position of the mouse.
     * @param x2
     *            the x co-ordinate of final position of the mouse.
     * @param y2
     *            the y co-ordinate of final position of the mouse.
     */
    public void addShape(int x1, int y1, int x2, int y2) {
        if (count == MiniDraw.maxShapes)
            return;
        
        Shape shape = null;
        if (currentAction.equals("Line"))
            shape = new Line(x1, y1, x2, y2, currentColor);
        else if (currentAction.equals("Dot"))
            shape = new Dot(x2, y2, currentColor);
        else if (currentAction.equals("Rect"))
            shape = new Rectangle(Math.min(x1, x2), Math.min(y1, y2),
                Math.abs(x1 - x2), Math.abs(y1 - y2), currentColor);
        else if (currentAction.equals("Oval"))
            shape = new Oval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1
                - x2), Math.abs(y1 - y2), currentColor);
        else if (currentAction.equals("Tree"))
            shape = new Tree(x1, y1, x2, y2, currentColor);
        
        shapes[count++] = shape;
        render();
    }
    
    /**
     * If there is room in the array, Construct a new Shape object of the
     * appropriate kind. Uses the appropriate constructor of the Line, Rectangle,
     * Oval, or Dot classes. Adds the shape it to the collection of shapes in the
     * drawing, and Renders the shape on the canvas
     * 
     * @param data
     *            the scanner holding data used to create a shape.
     */
    public void addShape(Scanner data) {
        if (count == MiniDraw.maxShapes)
            return;
        
        Shape shape = null;
        String shapeType = data.next();
        if (shapeType.equals("Line"))
            shape = new Line(data);
        else if (shapeType.equals("Dot"))
            shape = new Dot(data);
        else if (shapeType.equals("Rect"))
            shape = new Rectangle(data);
        else if (shapeType.equals("Oval"))
            shape = new Oval(data);
        else if (shapeType.equals("Tree"))
            shape = new Tree(data);
        else
            return;
        
        shapes[count++] = shape;
        render();
    }
    
    /**
     * Deletes the shape that was under the mouseReleased position (x, y)
     * 
     * @param x
     *            the x co-ordinate of the final position of the mouse.
     * @param y
     *            the y co-ordinate of the final position of the mouse.
     */
    public void deleteShape(int x, int y) {
        int index = indexOf(x, y);
        if (index == -1)
            return;
        
        shapes[index] = null;
        for (int i = index + 1; i < count; i++)
            shapes[i - 1] = shapes[i];
        shapes[count--] = null;
        
        render();
    }
    
    /**
     * Checks each shape in the list to see if the point (x,y) is on the shape. It
     * returns the index of the topmost shape for which this is true. Returns -1
     * if there is no such shape. Useful helper method for moveShape and
     * deleteShape
     * 
     * @param x x co-ordinate.
     * @param y y co-ordinate.
     * @return the index of the top-most shape on (x, y).
     */
    public int indexOf(int x, int y) {
        for (int i = count - 1; i >= 0; i--)
            if (shapes[i].pointOnShape(x, y))
                return i;
        
        return -1;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    // Helper methods for implementing the button and mouse actions
    
    /**
     * When mouse is pressed, remember the position in order to construct move, or
     * resize the Shape when the mouse is released.
     * 
     * @param e
     *            the mouse event.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        pressedX = e.getX();
        pressedY = e.getY();
    }
    
    /**
     * When the Mouse is released, depending on the currentAction, either
     * construct the shape that was being drawn, or perform the action (delete,
     * resize, or move) on the shape under the point where the mouse was pressed.
     * 
     * @param e
     *            the mouse event.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        
        if (currentAction.equals("Move"))
            moveShape(pressedX, pressedY, x, y);
        else if (currentAction.equals("Resize"))
            resizeShape(pressedX, pressedY, x, y);
        else if (currentAction.equals("Delete"))
            deleteShape(x, y);
        else
            addShape(pressedX, pressedY, x, y);
    }
    
    /**
     * Moves the shape that was under the mousePressed position (pressedX,
     * pressedY) to where the mouse was released. Ie, change its position by
     * (newX-pressedX) and (newY-pressedY)
     * 
     * @param fromX
     *            the x co-ordinate of initial position of the mouse.
     * @param fromY
     *            the y co-ordinate of initial position of the mouse.
     * @param toX
     *            the x co-ordinate of final position of the mouse.
     * @param toY
     *            the y co-ordinate of final position of the mouse.
     */
    public void moveShape(int fromX, int fromY, int toX, int toY) {
        int index = indexOf(fromX, fromY);
        if (index == -1)
            return;
        
        shapes[index].moveBy(toX - fromX, toY - fromY);
        
        render();
    }
    
    /**
     * Start a new drawing - initialise the shapes array and clear the canvas.
     */
    public void newDrawing() {
        canvas.clear();
        
        for (int i = 0; i < count; i++)
            shapes[i] = null;
        
        count = 0;
    }
    
    /**
     * Open a file, and read all the shape descriptions into the current drawing.
     * 
     * @throws IOException if IO error.
     */
    public void openDrawing() throws IOException {
        // Determine whether a valid file was chosen.
        String fname = FileChooser.open();
        if (fname == null)
            return;
        
        File openFile = new File(fname);
        if (!openFile.exists())
            return;
        
        // Create a new canvas drawing and read the lines from the file.
        newDrawing();
        try (BufferedReader br = new BufferedReader(new FileReader(openFile))) {
            String line;
            while ((line = br.readLine()) != null)
                try (Scanner scan = new Scanner(line)) {
                    addShape(scan);
                }
        }
    }
    
    /**
     * Renders all the shapes in the list on the canvas First clears the canvas,
     * then renders each shape, Finally redisplays the canvas
     */
    public void render() {
        canvas.clear(false);
        
        for (int i = 0; i < count; i++)
            shapes[i].render(canvas);
        canvas.display();
    }
    
    /**
     * Resizes the shape that was under the mousePressed position (fromX, fromY),
     * by the amount that the mouse was moved (ie from (fromX, fromY) to (toX,
     * toY)). If the mouse is moved to the right, the shape should be made that
     * much wider on each side; if the mouse is moved to the left, the shape
     * should be made that much narrower on each side If the mouse is moved up,
     * the shape should be made that much higher top and bottom; if the mouse is
     * moved down, the shape should be made that much shorter top and bottom. The
     * effect is that if the user drags from the top right corner of the shape,
     * the shape should be resized to whereever the dragged to.
     * 
     * @param fromX
     *            the x co-ordinate of initial position of the mouse.
     * @param fromY
     *            the y co-ordinate of initial position of the mouse.
     * @param toX
     *            the x co-ordinate of final position of the mouse.
     * @param toY
     *            the y co-ordinate of final position of the mouse.
     */
    public void resizeShape(int fromX, int fromY, int toX, int toY) {
        int index = indexOf(fromX, fromY);
        if (index == -1)
            return;
        
        shapes[index].resize(toX - fromX, fromY - toY);
        
        render();
    }
    
    /**
     * Save the current drawing to a file.
     * 
     * @throws IOException if IO error.
     */
    public void saveDrawing() throws IOException {
        // Determine whether file selected is text file, if not, append ".txt" to
        // end of name.
        String fname = FileChooser.save();
        if (fname.lastIndexOf(".") == -1)
            fname += ".txt";
        else if (!fname.substring(fname.lastIndexOf(".")).equalsIgnoreCase(
            ".txt"))
            fname += ".txt";
        
        // If the file already exists, ask whether to overwrite existing file.
        File saveFile = new File(fname);
        if (saveFile.exists()) {
            int response = JOptionPane.showConfirmDialog(frame, "File " + fname
                + ".txt already exists. Do you wish to overwrite it?",
                "Overwrite Confirmation", JOptionPane.OK_CANCEL_OPTION);
            
            if (response != 0)
                return;
            
            saveFile.delete();
        }
        
        // Create the new file and write all the shapes on the canvas to it.
        saveFile.createNewFile();
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile))) {
            for (int i = 0; i < count; i++) {
                bw.write(shapes[i].toString());
                bw.newLine();
            }
        }
        
        JOptionPane.showMessageDialog(frame, "File saved.");
    }
    
    /**
     * Utility method to make new button and add it to the panel Returns the
     * button, in case we need it.
     * 
     * @param panel
     *            the JPanel to add the button to.
     * @param name
     *            the name of the button to add.
     * 
     * @return the button made.
     */
    private JButton addButton(JPanel panel, String name) {
        JButton button = new JButton(name);
        button.addActionListener(this);
        panel.add(button);
        return button;
    }
    
    /**
     * Initiates the GUI.
     */
    private void initGUI() {
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // The graphics area.
        canvas.addMouseListener(this);
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        
        // The buttons.
        JPanel topPanel = new JPanel();
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        JPanel botPanel = new JPanel();
        frame.getContentPane().add(botPanel, BorderLayout.SOUTH);
        
        addButton(topPanel, "New");
        addButton(topPanel, "Open");
        addButton(topPanel, "Save");
        addButton(topPanel, "Color");
        addButton(topPanel, "Move");
        addButton(topPanel, "Resize");
        addButton(topPanel, "Delete");
        
        botPanel.add(new JLabel("Shapes: "));
        addButton(botPanel, "Line");
        addButton(botPanel, "Rect");
        addButton(botPanel, "Oval");
        addButton(botPanel, "Dot");
        addButton(botPanel, "Tree");
        addButton(topPanel, "Quit");
        
        frame.setVisible(true);
    }
    
    /**
     * Sets the current color. Asks user for a new color using a JColorChooser
     * (see lecture slide) As long as the color is not null, it remembers the
     * color
     */
    private void selectColor() {
        Color chosenColor = JColorChooser.showDialog(frame,
            "Choose painting color.", currentColor);
        
        if (chosenColor != null)
            currentColor = chosenColor;
    }
    
    public static void main(String args[]) {
        new MiniDraw();
    }
}
