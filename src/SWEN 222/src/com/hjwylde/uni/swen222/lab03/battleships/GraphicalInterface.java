package com.hjwylde.uni.swen222.lab03.battleships;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class GraphicalInterface extends JFrame implements WindowListener,
    MouseListener {
    
    /**
     * The following fields cache various icons so we don't need to load them
     * everytime.
     */
    private static ImageIcon boardPartition = makeImageIcon("boardPartition.png");
    private static ImageIcon emptySquare = makeImageIcon("water.png");
    private static ImageIcon hitSquare = makeImageIcon("explosion.png");
    private static ImageIcon missSquare = makeImageIcon("missedWater.png");
    private static ImageIcon hShipLeftSquare = makeImageIcon("hShipLeft.png");
    private static ImageIcon hShipRightSquare = makeImageIcon("hShipRight.png");
    private static ImageIcon hShipMiddleSquare = makeImageIcon("hShipMiddle.png");
    private static ImageIcon vShipBottomSquare = makeImageIcon("vShipBottom.png");
    private static ImageIcon vShipTopSquare = makeImageIcon("vShipTop.png");
    private static ImageIcon vShipMiddleSquare = makeImageIcon("vShipMiddle.png");
    
    private final BattleShipsGame game;
    private JPanel outerMostPanel;
    private JLabel[][] leftBattleGrid;
    private JLabel[][] rightBattleGrid;
    
    public GraphicalInterface() {
        super("The Game of Battleships!");
        
        game = new BattleShipsGame(15, 15);
        game.createRandomBoard(new Random());
        
        // Construct the outermost panel. This will include the grid display, as
        // well as any buttons as needed.
        makeOutermostPanel(15, 15);
        getContentPane().add(outerMostPanel);
        drawBoard();
        
        // tell frame to fire a WindowsListener event
        // but not to close when "x" button clicked.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        
        // Pack the window. This causes the window to compute its size,
        // including the layout of its components.
        pack();
        
        // Finally, make the window visible
        setVisible(true);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getComponent() instanceof JLabel)
            // loop and find JLabel
            for (int x = 0; x < game.getWidth(); x++)
                for (int y = 0; y < game.getHeight(); y++)
                    if (e.getComponent() == rightBattleGrid[x][y]) {
                        boolean isOver = game.bombSquare(x, y, false);
                        drawBoard();
                        
                        if (isOver) {
                            JDialog dialog = new JDialog();
                            dialog.setTitle("Game over! "
                                + (game.didPlayerWin() ? "You won!"
                                    : "You lost!"));
                            dialog.show();
                        }
                    }
        
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    // The following methods are part of the WindowListener interface,
    // but are not needed here.
    @Override
    public void windowActivated(WindowEvent e) {}
    
    /**
     * This method is called after the X button has been depressed.
     * 
     * @param e
     */
    @Override
    public void windowClosed(WindowEvent e) {}
    
    /**
     * This method is called when the user clicks on the "X" button in the
     * right-hand corner.
     * 
     * @param e
     */
    @Override
    public void windowClosing(WindowEvent e) {
        // Ask the user to confirm they wanted to do this
        int r = JOptionPane.showConfirmDialog(this, new JLabel(
            "Exit BattleShips?"), "Confirm Exit", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        if (r == JOptionPane.YES_OPTION)
            System.exit(0);
    }
    
    @Override
    public void windowDeactivated(WindowEvent e) {}
    
    @Override
    public void windowDeiconified(WindowEvent e) {}
    
    @Override
    public void windowIconified(WindowEvent e) {}
    
    @Override
    public void windowOpened(WindowEvent e) {}
    
    /**
     * This method is used to draw the game board
     */
    private void drawBoard() {
        for (int x = 0; x < game.getWidth(); x++)
            for (int y = 0; y < game.getHeight(); y++) {
                leftBattleGrid[x][y].setIcon(getSquareIcon(
                    game.getLeftSquare(x, y), true));
                rightBattleGrid[x][y].setIcon(getSquareIcon(
                    game.getRightSquare(x, y), false));
            }
    }
    
    /**
     * This method determines the appropriate icon for a given square.
     * 
     * @param gs
     * @param visible
     * @return
     */
    private ImageIcon getSquareIcon(GridSquare gs, boolean visible) {
        if (gs instanceof EmptySquare)
            return emptySquare;
        else if (gs instanceof MissSquare)
            return missSquare;
        else if (gs instanceof HitSquare)
            return hitSquare;
        else { // gs instanceof ShipSquare
            if (!visible)
                return emptySquare;
            
            ShipSquare ss = (ShipSquare) gs;
            switch (ss.getType()) {
            case HORIZONTAL_LEFT_END:
                return hShipLeftSquare;
            case HORIZONTAL_MIDDLE:
                return hShipMiddleSquare;
            case HORIZONTAL_RIGHT_END:
                return hShipRightSquare;
            case VERTICAL_BOTTOM_END:
                return vShipBottomSquare;
            case VERTICAL_MIDDLE:
                return vShipMiddleSquare;
            case VERTICAL_TOP_END:
                return vShipTopSquare;
            }
        }
        
        throw new InternalError("DEADCODE");
    }
    
    /**
     * This method is used to create the outermost panel.
     * 
     * @return
     */
    private void makeOutermostPanel(int width, int height) {
        leftBattleGrid = new JLabel[width][height];
        rightBattleGrid = new JLabel[width][height];
        
        // This method needs to:
        //
        // 1. Construct a panel for representing the left and right battle
        // grids. This should use a GridLayout which is large enough to contain
        // both the left and right grids, as well as a single partition square
        // in the middle. Each square in the left and right battle grids should
        // be initialised with a JLabel containing the emptySquare icon. An
        // empty border of sufficient size should be placed around this panel.
        
        JPanel battlePanel = new JPanel();
        battlePanel.setLayout(new BoxLayout(battlePanel, BoxLayout.X_AXIS));
        JPanel leftBattlePanel = new JPanel(new GridLayout(width, height, 1, 1));
        JPanel rightBattlePanel = new JPanel(
            new GridLayout(width, height, 1, 1));
        JPanel boardPartitionPanel = new JPanel(new GridLayout(width, 1, 1, 1));
        
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                leftBattleGrid[x][y] = new JLabel(emptySquare);
                rightBattleGrid[x][y] = new JLabel(emptySquare);
                rightBattleGrid[x][y].addMouseListener(this);
                
                leftBattlePanel.add(leftBattleGrid[x][y]);
                rightBattlePanel.add(rightBattleGrid[x][y]);
            }
        
        for (int x = 0; x < width; x++)
            boardPartitionPanel.add(new JLabel(boardPartition));
        
        battlePanel.add(leftBattlePanel);
        battlePanel.add(boardPartitionPanel);
        battlePanel.add(rightBattlePanel);
        
        // 2. Construct another panel for holding the "new game" button, and add
        // that button to the panel. This should use the FlowLayout to ensure
        // the button is located in the center.
        //
        // 3. Finally, construct the outermost panel, whilst adding the battle
        // grid panel and button panel (put the buttons above the battle grid).
        
        FlowLayout f = new FlowLayout();
        JPanel panel2 = new JPanel(f);
        JButton button2 = new JButton("New Game");
        button2.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                game.createRandomBoard(new Random());
                GraphicalInterface.this.drawBoard();
            }
        });
        panel2.add(button2);
        panel2.add(new JLabel("Score = "));
        outerMostPanel = new JPanel();
        outerMostPanel
            .setLayout(new BoxLayout(outerMostPanel, BoxLayout.Y_AXIS));
        outerMostPanel.add(panel2);
        outerMostPanel.add(battlePanel);
    }
    
    /**
     * Main method for the Graphical User Interface
     */
    public static void main(String[] args) {
        new GraphicalInterface();
    }
    
    /**
     * Helper method for loading image icons.
     * 
     * @param filename
     * @return
     */
    private static ImageIcon makeImageIcon(String filename) {
        // using the URL means the image loads when stored
        // in a jar or expanded into individual files.
        java.net.URL imageURL = BattleShip.class.getResource(filename);
        
        ImageIcon icon = null;
        if (imageURL != null)
            icon = new ImageIcon(imageURL);
        return icon;
    }
}
