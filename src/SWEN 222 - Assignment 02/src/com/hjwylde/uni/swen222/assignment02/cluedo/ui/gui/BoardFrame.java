package com.hjwylde.uni.swen222.assignment02.cluedo.ui.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import com.google.common.base.Optional;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Board;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Player;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.choices.PlayerTurnChoice;
import com.hjwylde.uni.swen222.assignment02.cluedo.util.Coordinate;

/**
 * A board frame encompases the entire game and handles most UI management. The only components that
 * aren't handled by the board frame are simple dialogs from the GUI class.
 * 
 * @author Henry J. Wylde
 * 
 * @since 31/08/2013
 */
public final class BoardFrame extends JFrame implements ActionListener,
    WindowListener {
    
    private static final long serialVersionUID = 1L;
    
    private final JMenuBar mMenuBar = new JMenuBar();
    private final JMenu mMenu = new JMenu("File");
    private final JMenuItem miQuit = new JMenuItem("Quit", 'q');
    private final JMenu mGameMenu = new JMenu("Game");
    private final JMenuItem miViewKeeperCards = new JMenuItem(
        "View Keeper Cards", 'k');
    private final JMenuItem miViewNotebook = new JMenuItem("View Notebook", 'n');
    private final JMenuItem miEndTurn = new JMenuItem("End Turn", 'e');
    
    private final BoardCanvas canvas;
    private final PlayerPanel playerPanel;
    
    private PlayerTurnChoice turnRequest = null;
    
    /**
     * Creates a new <code>BoardFrame</code> with the given board.
     * 
     * @param board the board.
     */
    public BoardFrame(Board board) {
        super("Cluedo");
        
        initialiseMenu();
        
        // Initialise images before creating the canvas
        if (!Images.isImagesInitialised())
            initialiseImages();
        
        JPanel content = new JPanel(new FlowLayout(0, 0, 0));
        content.setBackground(Color.WHITE);
        
        // Create the canvas
        canvas = new BoardCanvas(board);
        canvas.setDoubleBuffered(true);
        canvas.addMouseListener(canvas);
        
        JPanel wrapper = new JPanel(new FlowLayout(0, 0, 0));
        wrapper.setBorder(BorderFactory.createLineBorder(new Color(0, 42, 96),
            10));
        wrapper.add(canvas);
        content.add(wrapper);
        
        playerPanel = new PlayerPanel(board);
        playerPanel.setDoubleBuffered(true);
        content.add(playerPanel);
        
        // Save having to add the key listener to all components that may get focus
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(new KeyEventDispatcher() {
                
                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
                    switch (e.getID()) {
                    case KeyEvent.KEY_PRESSED:
                        canvas.keyPressed(e);
                        break;
                    case KeyEvent.KEY_RELEASED:
                        canvas.keyReleased(e);
                        break;
                    case KeyEvent.KEY_TYPED:
                        canvas.keyTyped(e);
                    }
                    
                    return false;
                }
                
            });
        addWindowListener(this);
        
        setContentPane(content);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        
        validate();
        pack();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == miQuit)
            System.exit(0);
        else if (e.getSource() == miEndTurn)
            turnRequest = PlayerTurnChoice.END;
        else if (e.getSource() == miViewNotebook)
            playerPanel.viewNotebook();
        else if (e.getSource() == miViewKeeperCards)
            playerPanel.viewKeeperCards();
    }
    
    /**
     * Gets the turn request of this frame. The turn request indicates that the user applied some
     * action to either the canvas or the player panel, which implied they wanted to do a certain
     * type of turn, such as moving. After a turn request is retrieved, it is reset.
     * 
     * @return the turn request.
     */
    public Optional<PlayerTurnChoice> getTurnRequest() {
        Optional<PlayerTurnChoice> ret = Optional.fromNullable(turnRequest).or(
            playerPanel.getTurnRequest().or(canvas.getTurnRequest()));
        turnRequest = null;
        
        return ret;
    }
    
    /**
     * Informs the frame that the player rolled the dice. This method simply passes it on to the
     * board canvas.
     * 
     * @param player the player that rolled the dice.
     * @param numberMoves the number of moves the player has.
     */
    public void informPlayerDiceRoll(Player player, int numberMoves) {
        canvas.informPlayerDiceRoll(player, numberMoves);
    }
    
    /**
     * Informs that the player's keeper cards have changed. This method simply passes it on to the
     * player panel.
     * 
     * @param player the player whose keeper cards have changed.
     */
    public void informPlayerKeeperCardsChanged(Player player) {
        playerPanel.informPlayerKeeperCardsChanged(player);
    }
    
    /**
     * Informs that the given player may move anywhere. THis method simply passes it on to the board
     * canvas.
     * 
     * @param player the player who may move anywhere.
     */
    public void informPlayerMoveAnywhere(Player player) {
        canvas.informPlayerMoveAnywhere(player);
    }
    
    /**
     * Informs the frame that the player has the given number of moves left. This method simply
     * passes it on to the board canvas.
     * 
     * @param player the player.
     * @param numberMoves the number of moves left.
     */
    public void informPlayerNumberMovesLeft(Player player, int numberMoves) {
        canvas.informPlayerNumberMovesLeft(player, numberMoves);
    }
    
    /**
     * Informs the frame that the player's turn has just ended. This method simply passes it on to
     * the board canvas and player panel.
     * 
     * @param player the player whose turn has ended.
     */
    public void informPlayerTurnEnd(Player player) {
        canvas.informPlayerTurnEnd(player);
        playerPanel.informPlayerTurnEnd(player);
    }
    
    /**
     * Informs the frame that the player's turn has just started. This method simply passes it on to
     * the board canvas and player panel.
     * 
     * @param player the player whose turn has started.
     */
    public void informPlayerTurnStart(Player player) {
        canvas.informPlayerTurnStart(player);
        playerPanel.informPlayerTurnStart(player);
    }
    
    /**
     * Queries the frame for a move from the player, one in which they may move only the given
     * number of squares.
     * 
     * @param player the player to move.
     * @param maxMoves the maximum number of movement squares.
     * @return the move.
     */
    public Coordinate queryPlayerMove(Player player, int maxMoves) {
        informPlayerNumberMovesLeft(player, maxMoves);
        return canvas.queryPlayerMove(player);
    }
    
    /**
     * Queries the frame for a move from the player, one in which they may move anywhere they like.
     * 
     * @param player the player to move.
     * @return the move.
     */
    public Coordinate queryPlayerMoveAnywhere(Player player) {
        return canvas.queryPlayerMoveAnywhere(player);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void windowActivated(WindowEvent e) {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void windowClosed(WindowEvent e) {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void windowClosing(WindowEvent e) {
        // Query whether the user wants to exit
        int res = JOptionPane
            .showConfirmDialog(
                this,
                "Are you sure you wish to exit this game? You will lose all your current progress.",
                "Exit Confirmation", JOptionPane.YES_NO_OPTION);
        
        switch (res) {
        case JOptionPane.YES_OPTION:
            System.exit(0);
            return;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void windowDeactivated(WindowEvent e) {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void windowDeiconified(WindowEvent e) {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void windowIconified(WindowEvent e) {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void windowOpened(WindowEvent e) {}
    
    private void initialiseImages() {
        try {
            Images.initialiseImages();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to initialise images: "
                + e.getClass().getSimpleName() + " - " + e.getMessage() + "\n"
                + "Please select a directory to load the images from.",
                "Unable to Initialise Images", JOptionPane.OK_OPTION);
        }
        
        if (Images.isImagesInitialised())
            return;
        
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Images Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        do {
            int res = chooser.showOpenDialog(this);
            switch (res) {
            case JFileChooser.APPROVE_OPTION:
                try {
                    String path = chooser.getSelectedFile().getAbsolutePath();
                    if (!path.endsWith(File.separator))
                        path = path + File.separator;
                    
                    Images.initialiseImages(path);
                    return;
                } catch (IOException e) {
                    JOptionPane
                        .showMessageDialog(
                            this,
                            "Unable to initialise images: "
                                + e.getClass().getSimpleName()
                                + " - "
                                + e.getMessage()
                                + "\n"
                                + "Please select a directory to load the images from.",
                            "Unable to Initialise Images",
                            JOptionPane.OK_OPTION);
                }
                break;
            case JFileChooser.CANCEL_OPTION:
                System.exit(0);
            }
            
            JOptionPane.showMessageDialog(this, "Unable to initialise images."
                + "\n" + "Please select a directory to load the images from.",
                "Unable to Initialise Images", JOptionPane.OK_OPTION);
        } while (!Images.isImagesInitialised());
    }
    
    /**
     * Initialises the menu.
     */
    private void initialiseMenu() {
        miQuit.addActionListener(this);
        miQuit.setAccelerator(KeyStroke.getKeyStroke(
            java.awt.event.KeyEvent.VK_Q, java.awt.Event.CTRL_MASK));
        
        mMenu.add(miQuit);
        
        mMenuBar.add(mMenu);
        
        miViewKeeperCards.addActionListener(this);
        miViewKeeperCards.setAccelerator(KeyStroke.getKeyStroke(
            java.awt.event.KeyEvent.VK_K, java.awt.Event.CTRL_MASK));
        miViewNotebook.addActionListener(this);
        miViewNotebook.setAccelerator(KeyStroke.getKeyStroke(
            java.awt.event.KeyEvent.VK_N, java.awt.Event.CTRL_MASK));
        miEndTurn.addActionListener(this);
        miEndTurn.setAccelerator(KeyStroke.getKeyStroke(
            java.awt.event.KeyEvent.VK_E, java.awt.Event.CTRL_MASK));
        
        mGameMenu.add(miViewKeeperCards);
        mGameMenu.add(miViewNotebook);
        mGameMenu.add(miEndTurn);
        
        mMenuBar.add(mGameMenu);
        
        setJMenuBar(mMenuBar);
    }
}