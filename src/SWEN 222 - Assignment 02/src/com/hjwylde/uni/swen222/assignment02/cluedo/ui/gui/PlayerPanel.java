package com.hjwylde.uni.swen222.assignment02.cluedo.ui.gui;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.filter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Board;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Notebook;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Player;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.CharacterCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.RoomCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.WeaponCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.intrigue.KeeperCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.choices.PlayerTurnChoice;

/**
 * The player panel displays useful information about the player whose turn it currently is. For
 * example, the player's cards and buttons for actions they may do.
 * 
 * @author Henry J. Wylde
 * 
 * @since 31/08/2013
 */
public final class PlayerPanel extends JPanel implements ActionListener {
    
    private static final long serialVersionUID = 1L;
    
    private final Board board;
    
    private int player = -1;
    
    private final JLabel jLabelPlayerName = new JLabel();
    private final JLabel jLabelCharacterCards = new JLabel("Character cards:");
    private final ImagePanel characterCardsPanel = new ImagePanel();
    private final JLabel jLabelWeaponCards = new JLabel("Weapon cards");
    private final ImagePanel weaponCardsPanel = new ImagePanel();
    private final JLabel jLabelRoomCards = new JLabel("Room cards:");
    private final ImagePanel roomCardsPanel = new ImagePanel();
    private final JButton jButtonViewKeeperCards;
    private final JButton jButtonViewNotebook;
    private final JButton jButtonEndTurn;
    
    private PlayerTurnChoice turnRequest = null;
    
    /**
     * Creates a new <code>PlayerPanel<code> with the given board.
     * 
     * @param board the board.
     */
    public PlayerPanel(Board board) {
        this.board = checkNotNull(board, "Board cannot be null");
        
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(405, 485));
        
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 42, 96), 10),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        // Set player character name
        jLabelPlayerName.setPreferredSize(new Dimension(435, 45));
        jLabelPlayerName.setFont(new Font("Arial", Font.BOLD, 16));
        add(Box.createRigidArea(new Dimension(435, 5)));
        add(jLabelPlayerName);
        
        // Show character cards
        add(jLabelCharacterCards);
        add(characterCardsPanel);
        
        add(Box.createRigidArea(new Dimension(365, 25)));
        
        // Show weapon cards
        add(jLabelWeaponCards);
        add(Box.createRigidArea(new Dimension(365, 5)));
        add(weaponCardsPanel);
        
        add(Box.createRigidArea(new Dimension(365, 25)));
        
        // Show room cards
        add(jLabelRoomCards);
        add(Box.createRigidArea(new Dimension(365, 5)));
        add(roomCardsPanel);
        
        add(Box.createRigidArea(new Dimension(365, 25)));
        
        // Button to view notebook
        JPanel buttons = new JPanel();
        buttons.setBackground(Color.WHITE);
        
        jButtonViewKeeperCards = new JButton("View Keeper Cards");
        jButtonViewKeeperCards.addActionListener(this);
        
        buttons.add(jButtonViewKeeperCards);
        
        jButtonViewNotebook = new JButton("View Notebook");
        jButtonViewNotebook.addActionListener(this);
        
        buttons.add(jButtonViewNotebook);
        
        // Button to end turn
        jButtonEndTurn = new JButton("End Turn");
        jButtonEndTurn.addActionListener(this);
        buttons.add(jButtonEndTurn);
        
        add(buttons);
        
        update();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jButtonViewKeeperCards)
            viewKeeperCards();
        else if (e.getSource() == jButtonViewNotebook)
            viewNotebook();
        else if (e.getSource() == jButtonEndTurn)
            turnRequest = PlayerTurnChoice.END;
    }
    
    /**
     * Gets the turn request of this panel. The turn request indicates that the user applied some
     * action to the panel which implied they wanted to do a certain type of turn, such as ending
     * their turn. After a turn request is retrieved, it is reset.
     * 
     * @return the turn request.
     */
    public Optional<PlayerTurnChoice> getTurnRequest() {
        PlayerTurnChoice ret = turnRequest;
        turnRequest = null;
        
        return Optional.fromNullable(ret);
    }
    
    /**
     * Informs that a player's keeper cards has changed. This method simply calls update to reflect
     * the new player's cards.
     * 
     * @param player the player whose keeper cards have changed.
     */
    public void informPlayerKeeperCardsChanged(Player player) {
        checkState(board.getPlayer(this.player).equals(player));
        
        update();
    }
    
    /**
     * Informs the panel that the given player's turn has just ended.
     * 
     * @param player the player whose turn has just ended.
     */
    public void informPlayerTurnEnd(Player player) {
        checkState(board.getPlayer(this.player).equals(player));
        
        this.player = -1;
        update();
    }
    
    /**
     * Informs that the given player's turn has just started.
     * 
     * @param player the player whose turn has just started.
     */
    public void informPlayerTurnStart(Player player) {
        this.player = board.getPlayerIndex(player);
        
        checkState(!board.isPlayerDead(this.player), "Player cannot be dead!");
        update();
    }
    
    /**
     * Updates the information in the panel to reflect the current set player.
     */
    public void update() {
        if ((player < 0) || board.isPlayerDead(player)) {
            jLabelPlayerName.setText("");
            characterCardsPanel.clearImages();
            weaponCardsPanel.clearImages();
            roomCardsPanel.clearImages();
            return;
        }
        
        jLabelPlayerName.setText("Player: "
            + board.getPlayer(player).toString());
        
        List<CharacterCard> characterCards = new ArrayList<>();
        List<String> hovers = new ArrayList<>();
        for (CharacterCard card : filter(board.getPlayer(player).getCards(),
            CharacterCard.class)) {
            characterCards.add(card);
            hovers.add(card.toString());
        }
        characterCardsPanel.setImages(Images.getImagesForCards(characterCards),
            hovers);
        
        List<WeaponCard> weaponCards = new ArrayList<>();
        hovers.clear();
        for (WeaponCard card : filter(board.getPlayer(player).getCards(),
            WeaponCard.class)) {
            weaponCards.add(card);
            hovers.add(card.toString());
        }
        weaponCardsPanel.setImages(Images.getImagesForCards(weaponCards),
            hovers);
        
        List<RoomCard> roomCards = new ArrayList<>();
        hovers.clear();
        for (RoomCard card : filter(board.getPlayer(player).getCards(),
            RoomCard.class)) {
            roomCards.add(card);
            hovers.add(card.toString());
        }
        roomCardsPanel.setImages(Images.getImagesForCards(roomCards), hovers);
    }
    
    public void viewKeeperCards() {
        StringBuilder sb = new StringBuilder();
        
        Player user = board.getPlayer(player);
        if (user.getKeeperCards().size() == 0)
            sb.append(user + " has no keeper cards.");
        else {
            sb.append(user + "'s keeper cards:\n");
            
            Set<KeeperCard> listed = new HashSet<>();
            
            for (KeeperCard card : user.getKeeperCards()) {
                if (listed.contains(card))
                    continue;
                
                listed.add(card);
                
                int quantity = Iterables.size(Iterables.filter(
                    user.getKeeperCards(), card.getClass()));
                
                sb.append(String.format("%s x%s%n\t%s%n", card.getName(),
                    quantity, card.getDescription()));
            }
        }
        
        JOptionPane.showMessageDialog(getParent(), sb, "Keeper Cards",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Views the notebook of the current player.
     */
    public void viewNotebook() {
        StringBuilder sb = new StringBuilder(board.getPlayer(player).toString())
            .append("'s Notebook:");
        
        Notebook notebook = board.getPlayer(player).getNotebook();
        
        sb.append("\n\n").append("Unseen Character Cards:");
        for (CharacterCard card : filter(notebook.availableCards(),
            CharacterCard.class))
            sb.append("\n").append(card);
        
        sb.append("\n\n").append("Unseen Weapon Cards:");
        for (WeaponCard card : filter(notebook.availableCards(),
            WeaponCard.class))
            sb.append("\n").append(card);
        
        sb.append("\n\n").append("Unseen Room Cards:");
        for (RoomCard card : filter(notebook.availableCards(), RoomCard.class))
            sb.append("\n").append(card);
        
        JOptionPane.showMessageDialog(getParent(), sb, "Notebook",
            JOptionPane.INFORMATION_MESSAGE);
    }
}