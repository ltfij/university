package com.hjwylde.uni.comp103.assignment03.sokoban;

/*
 * Code for Assignment 3, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.hjwylde.uni.comp103.util.DrawingCanvas;

/**
 * Sokoban
 */
public class Sokoban implements ActionListener, KeyListener, MouseListener {
    
    // Fields
    private final JFrame frame = new JFrame("Sokoban");
    private final DrawingCanvas canvas = new DrawingCanvas();
    private final JTextArea messageArea;
    
    private Cell[][] cells; // the array describing the current warehouse.
    private int rows;
    private int cols;
    
    private Coord agentPos;
    private Direction agentDirection = Direction.left;
    private Stack<Action> agentActions = new Stack<>(); // The stack holding
                                                        // all of the agents
                                                        // previous actions.
    
    private final int maxLevels = 4;
    private int level = 0;
    
    private Map<Character, Cell> cellMapping;
    private Map<Cell, String> imageMapping;
    private Map<Direction, String> agentMapping;
    private Map<Character, Direction> keyMapping;
    
    private static final int leftMargin = 40;
    
    // GUI Methods
    
    private static final int topMargin = 40;
    
    private static final int cellSize = 25;
    
    // Constructors
    /**
     * Construct a new Sokoban object and set up the GUI
     */
    public Sokoban() {
        
        frame.setSize(800, 550);
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
        
        messageArea = new JTextArea(1, 80); // one line text area for messages.
        frame.getContentPane().add(messageArea, BorderLayout.SOUTH);
        
        JPanel buttonPanel = new JPanel();
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        
        addButton(buttonPanel, "New Level");
        addButton(buttonPanel, "Restart");
        addButton(buttonPanel, "Undo");
        addButton(buttonPanel, "Left");
        addButton(buttonPanel, "Up");
        addButton(buttonPanel, "Down");
        addButton(buttonPanel, "Right");
        addButton(buttonPanel, "Quit");
        frame.setVisible(true);
        initialiseMappings();
        load();
        messageArea
            .setText("Put the boxes away. You may use keys (WASD or IJKL) if you click on the canvas first");
    }
    
    /** Respond to button presses */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("New Level")) {
            level = (level + 1) % maxLevels;
            load();
            
            canvas.requestFocus(); // Added this in to allow for hotkeys after
                                   // pressing a button.
        } else if (cmd.equals("Restart")) {
            load();
            
            canvas.requestFocus();
        } else if (cmd.equals("Quit"))
            frame.dispose();
        else if (cmd.equals("Undo")) {
            undoAction();
            
            canvas.requestFocus();
        } else {
            doAction(Direction.valueOf(cmd.toLowerCase(Locale.ENGLISH)));
            
            canvas.requestFocus();
        }
    }
    
    /**
     * Compare two coordinates to see if they are the same.
     * 
     * @param c1
     *            The first Coord
     * @param c2
     *            The second Coord
     * @return Whether c1 is the same as c2;
     */
    public boolean compareCoords(Coord c1, Coord c2) {
        return ((c1.row == c2.row) && (c1.col == c2.col));
    }
    
    /**
     * Move the agent in the specified direction, if possible. If there is box in
     * front of the agent
     * and a space in front of the box, then push the box. Otherwise, if there is
     * anything in front of
     * the agent, do nothing.
     */
    public void doAction(Direction d) {
        if (d == null)
            return;
        agentDirection = d;
        Coord newP = agentPos.next(d); // where the agent will move to
        Coord nextP = newP.next(d); // the place two steps over
        if (cells[newP.row][newP.col].hasBox()
            && cells[nextP.row][nextP.col].free()) {
            agentActions.push(new Action("push", d)); // Add to the history of all
                                                      // actions.
            push(d);
        } else if (cells[newP.row][newP.col].free()) {
            agentActions.push(new Action("move", d)); // Add to the history of all
                                                      // actions.
            move(d);
        }
    }
    
    /** Draws the grid of cells on the screen, and the agent */
    public void draw() {
        canvas.clear(false);
        // draw cells
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                drawCell(row, col);
        drawAgent();
        canvas.display();
    }
    
    /**
     * Returns true iff the warehouse is solved - all the shelves have boxes on
     * them
     */
    public boolean isSolved() {
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                if (cells[row][col] == Cell.shelf)
                    return false;
        return true;
    }
    
    /**
     * Jump the agent to the position if there is a clear path to it.
     * 
     * @param pos
     *            The position to jump to.
     */
    public void jump(Coord pos) {
        try {
            findPath(new boolean[rows][cols], agentPos, pos); // Check if there is a
                                                              // valid path to pos
        } catch (Exception e) {
            if (e.getMessage().equals("Path Found.")) { // If there is a path found,
                                                        // move the agent.
                drawCell(agentPos);
                agentPos = pos;
                drawAgent();
                messageArea.setText("Jump to " + pos.toString());
                canvas.display();
            }
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {
        doAction(keyMapping.get(e.getKeyChar()));
    }
    
    /** Loads a grid of cells (and agent position) from a file */
    public void load() {
        File f = new File("bin/sokoban/warehouse" + level + ".txt");
        if (f.exists()) {
            List<String> lines = new ArrayList<>();
            try (Scanner sc = new Scanner(f)) {
                while (sc.hasNext())
                    lines.add(sc.nextLine());
            } catch (IOException e) {
                System.out.println("File error " + e);
            }
            
            rows = lines.size();
            cols = lines.get(0).length();
            
            cells = new Cell[rows][cols];
            
            agentActions = new Stack<>();
            
            for (int row = 0; row < rows; row++) {
                String line = lines.get(row);
                for (int col = 0; col < cols; col++)
                    if (col >= line.length())
                        cells[row][col] = Cell.empty;
                    else {
                        char ch = line.charAt(col);
                        if (cellMapping.containsKey(ch))
                            cells[row][col] = cellMapping.get(ch);
                        else {
                            cells[row][col] = Cell.empty;
                            System.out.printf("Invalid char: (%d, %d) = %c \n",
                                row, col, ch);
                        }
                        if (ch == 'A')
                            agentPos = new Coord(row, col);
                    }
            }
            draw();
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        jump(new Coord((e.getY() - Sokoban.topMargin) / Sokoban.cellSize,
            (e.getX() - Sokoban.leftMargin) / Sokoban.cellSize));
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    /** Move the agent into the new position (guaranteed to be empty) */
    public void move(Direction d) {
        drawCell(agentPos);
        agentPos = agentPos.next(d);
        drawAgent();
        messageArea.setText("Move " + d);
        canvas.display();
    }
    
    /**
     * Pull: (useful for undoing a push in direction d) move the agent in the
     * reverse direction from
     * d, pulling the box into the agent's old position
     */
    public void pull(Direction d) {
        Coord boxP = agentPos.next(d);
        cells[boxP.row][boxP.col] = cells[boxP.row][boxP.col].moveOff();
        cells[agentPos.row][agentPos.col] = cells[agentPos.row][agentPos.col]
            .moveOn();
        drawCell(boxP);
        drawCell(agentPos);
        agentPos = agentPos.next(d.opposite());
        drawAgent();
        canvas.display();
    }
    
    // Drawing
    
    /** Push: Move the agent, pushing the box one step */
    public void push(Direction d) {
        drawCell(agentPos);
        agentPos = agentPos.next(d);
        drawAgent();
        Coord boxP = agentPos.next(d);
        cells[agentPos.row][agentPos.col] = cells[agentPos.row][agentPos.col]
            .moveOff();
        cells[boxP.row][boxP.col] = cells[boxP.row][boxP.col].moveOn();
        drawCell(boxP);
        messageArea.setText("Push " + d);
        canvas.display();
        
        if (isSolved()) { // If is solved... Go to next level.
            level = (level + 1) % maxLevels;
            load();
            
            canvas.requestFocus();
        }
    }
    
    /**
     * Retreat: (useful for undoing a move in direction d) move the agent in the
     * reverse direction
     * from d.
     */
    public void retreat(Direction d) {
        // Added in this function for consistancy with having a seperate function to
        // undo an action,
        // also as using "move(d.opposite())" would draw the agents model in the
        // wrong position.
        drawCell(agentPos);
        agentPos = agentPos.next(d.opposite());
        drawAgent();
        canvas.display();
    }
    
    /**
     * Undo the last action made by the agent if he is not at the beginning.
     */
    public void undoAction() {
        if (agentActions.isEmpty()) // If at the start... Do nothing
            return;
        
        Action lastAction = agentActions.pop();
        agentDirection = lastAction.dir();
        
        if (lastAction.isMove())
            retreat(agentDirection);
        else if (lastAction.isPush())
            pull(agentDirection);
    }
    
    /** Helper method for adding buttons */
    private JButton addButton(JPanel panel, String name) {
        JButton button = new JButton(name);
        button.addActionListener(this);
        panel.add(button);
        return button;
    }
    
    private void drawAgent() {
        canvas.drawImage(agentMapping.get(agentDirection), Sokoban.leftMargin
            + (Sokoban.cellSize * agentPos.col), Sokoban.topMargin
            + (Sokoban.cellSize * agentPos.row), Sokoban.cellSize,
            Sokoban.cellSize, false);
    }
    
    private void drawCell(Coord pos) {
        drawCell(pos.row, pos.col);
    }
    
    private void drawCell(int row, int col) {
        String imageName = imageMapping.get(cells[row][col]);
        if (imageName != null)
            canvas.drawImage(imageName, Sokoban.leftMargin
                + (Sokoban.cellSize * col), Sokoban.topMargin
                + (Sokoban.cellSize * row), Sokoban.cellSize, Sokoban.cellSize,
                false);
    }
    
    /**
     * Recursive algorithm to find whether there is a clear path to <code>end</code>.
     * 
     * @param tried
     *            An array holding a boolean value as to whether that cell on the
     *            map has been tried
     *            before.
     * @param start
     *            Start position.
     * @param end
     *            End position to get to.
     */
    private void findPath(boolean[][] tried, Coord start, Coord end)
        throws Exception {
        if (compareCoords(start, end))
            throw new Exception("Path Found."); // Break out of the recursion, return
                                                // to the top of the
                                                // stack trace until "jump" catches
                                                // the exception
            
        Coord up = start.next(Direction.up);
        Coord right = start.next(Direction.right);
        Coord down = start.next(Direction.down);
        Coord left = start.next(Direction.left);
        
        // Try each direction, up, right, down and left if that cell is free and has
        // not already been
        // tried.
        // Add a new 'move' action to the actions stack. If the findPath method
        // returns (ie. no path
        // found, as if one was found it would have thrown an exception) then remove
        // the action from the
        // stack.
        if (!tried[up.row][up.col] && cells[up.row][up.col].free()) {
            tried[up.row][up.col] = true;
            
            agentActions.push(new Action("move", Direction.up));
            findPath(tried, up, end);
            agentActions.pop();
        }
        
        if (!tried[right.row][right.col] && cells[right.row][right.col].free()) {
            tried[right.row][right.col] = true;
            
            agentActions.push(new Action("move", Direction.right));
            findPath(tried, right, end);
            agentActions.pop();
        }
        
        if (!tried[down.row][down.col] && cells[down.row][down.col].free()) {
            tried[down.row][down.col] = true;
            
            agentActions.push(new Action("move", Direction.down));
            findPath(tried, down, end);
            agentActions.pop();
        }
        
        if (!tried[left.row][left.col] && cells[left.row][left.col].free()) {
            tried[left.row][left.col] = true;
            
            agentActions.push(new Action("move", Direction.left));
            findPath(tried, left, end);
            agentActions.pop();
        }
    }
    
    private void initialiseMappings() {
        cellMapping = new HashMap<>();
        cellMapping.put('.', Cell.empty);
        cellMapping.put('A', Cell.empty); // initial position of agent must be an
                                          // empty cell
        cellMapping.put('#', Cell.wall);
        cellMapping.put('S', Cell.shelf);
        cellMapping.put('B', Cell.box);
        
        imageMapping = new EnumMap<>(Cell.class);
        imageMapping.put(Cell.empty, "bin/sokoban/empty.gif");
        imageMapping.put(Cell.wall, "bin/sokoban/wall.gif");
        imageMapping.put(Cell.box, "bin/sokoban/box.gif");
        imageMapping.put(Cell.shelf, "bin/sokoban/shelf.gif");
        imageMapping.put(Cell.boxOnShelf, "bin/sokoban/boxOnShelf.gif");
        
        agentMapping = new EnumMap<>(Direction.class);
        agentMapping.put(Direction.up, "bin/sokoban/agent-up.gif");
        agentMapping.put(Direction.down, "bin/sokoban/agent-down.gif");
        agentMapping.put(Direction.left, "bin/sokoban/agent-left.gif");
        agentMapping.put(Direction.right, "bin/sokoban/agent-right.gif");
        
        keyMapping = new HashMap<>();
        keyMapping.put('i', Direction.up);
        keyMapping.put('w', Direction.up);
        keyMapping.put('k', Direction.down);
        keyMapping.put('s', Direction.down);
        keyMapping.put('j', Direction.left);
        keyMapping.put('a', Direction.left);
        keyMapping.put('l', Direction.right);
        keyMapping.put('d', Direction.right);
    }
    
    public static void main(String[] args) {
        new Sokoban();
    }
}
