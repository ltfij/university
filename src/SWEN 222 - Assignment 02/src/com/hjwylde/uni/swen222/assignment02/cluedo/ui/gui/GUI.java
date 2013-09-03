package com.hjwylde.uni.swen222.assignment02.cluedo.ui.gui;

import static com.google.common.base.Preconditions.checkArgument;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import com.google.common.base.Optional;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Board;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Player;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.Card;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.intrigue.KeeperCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Character;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Room;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Weapon;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.UI;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.choices.Option;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.choices.PlayerTurnChoice;
import com.hjwylde.uni.swen222.assignment02.cluedo.util.Coordinate;
import com.hjwylde.uni.swen222.assignment02.cluedo.util.Pair;
import com.hjwylde.uni.swen222.assignment02.cluedo.util.Triple;

/**
 * Represents the GUI for this Cluedo game. The GUI takes care of initialising the frame that will
 * be used and acts as the intermediary between the game logic and the GUI frame.
 * 
 * @author Henry J. Wylde
 * 
 * @since 31/08/2013
 */
public final class GUI implements UI, Runnable {
    
    private final BoardFrame frame;
    private final Board board;
    
    /**
     * Creates a new GUI with the given board.
     * 
     * @param board the board.
     */
    public GUI(Board board) {
        frame = new BoardFrame(board);
        this.board = board;
        
        frame.setVisible(true);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informGameStart() {
        inform(
            "Cluedo",
            "Welecome to Cluedo!"
                + "\n"
                + "This game uses a point and click interface for movement as well as keyboard movement if you wish.");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informNoPlayerRefuteRumour(Player player) {
        inform("Rumour", "No player was able to refute " + player + "'s rmour!");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerClockCardLoss(Player player) {
        inform("Player Lost", player
            + " has just lost the game by picking up the 8th clock card!");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerDiceRoll(Player player, int numberMoves) {
        // Pass the call down so that we can count the number of moves
        frame.informPlayerDiceRoll(player, numberMoves);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerIllegalMove(Player player) {
        // Don't need to inform the player, should be intuitive by the move square highlighting
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerKeeperCardsChanged(Player player) {
        frame.informPlayerKeeperCardsChanged(player);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerMoveAnywhere(Player player) {
        frame.informPlayerMoveAnywhere(player);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerMovedToStart(Player player, Character character) {
        // Don't need to inform the player, should be intuitive by the graphics
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerNumberMovesLeft(Player player, int numberMoves) {
        // Pass the call down so that we can count the number of moves
        frame.informPlayerNumberMovesLeft(player, numberMoves);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerPickUpClockCard(Player player, int remaining) {
        inform("Intrigue Card", player
            + " has just picked up a clock card! There are now " + remaining
            + " left.");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerPickUpKeeperCard(Player player, KeeperCard card) {
        inform("Intrigue Card", player + " has just picked up the " + card
            + " keeper card!");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerShowCard(Player player, Card card) {
        // Don't need to show the user graphically the card, they will know
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerTurnEnd(Player player) {
        frame.informPlayerTurnEnd(player);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerTurnStart(Player player) {
        frame.informPlayerTurnStart(player);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerUnableRefuteRumour(Player player) {
        inform("Rumour", player + " is unable to refute the rumour!");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerViewCards(Player player) {
        // Not needed, the player panel will display all the information automatically
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerViewKeeperCards(Player player) {
        // Not needed, the player panel will display all the information automatically
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerViewNotebook(Player player) {
        // Not needed, the player panel will display all the information automatically
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerWin(Player player,
        Triple<Character, Weapon, Room> accusation) {
        inform("Player Win",
            "Congratulations " + player + "! You have just won the game!"
                + "\n" + "It was " + accusation.getFirst() + " with the "
                + accusation.getSecond() + " in the " + accusation.getThird()
                + ".");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerWrongAccusationLoss(Player player) {
        inform("Player Lost", player
            + " has just lost by making an incorrect accusation!");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int queryNumberPlayers(List<Integer> options) {
        return query("Number of Players",
            "Please select the number of players for this game:", options);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Character queryPlayerCharacter(int player, List<Character> options) {
        return query("Player Character", "Player #" + (player + 1)
            + ", who would you like to play as?", options,
            Images.getImagesForCharacters(options));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Card queryPlayerChooseRefuteRumourCard(Player player,
        Set<Card> options) {
        return query("Rumour", player
            + ", please select a card to refute the rumour with:",
            new ArrayList<>(options),
            Images.getImagesForCards(new ArrayList<>(options)));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Triple<Character, Weapon, Room> queryPlayerMakeAccusation(
        Player player) {
        Character character = query("Accusation", player
            + ", please select a character to accuse:", Character.values(),
            Images.getImagesForCharacters(Character.values()));
        Weapon weapon = query("Accusation", player
            + ", please select a weapon for the accusation:", Weapon.values(),
            Images.getImagesForWeapons(Weapon.values()));
        Room room = query("Accusation", player
            + ", please select a room for the accusation:",
            Room.murderRoomValues(),
            Images.getImagesForRooms(Room.murderRoomValues()));
        
        return new Triple<>(character, weapon, room);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Coordinate queryPlayerMove(Player player, int maxMoves) {
        return frame.queryPlayerMove(player, maxMoves);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Coordinate queryPlayerMoveAnywhere(Player player) {
        return frame.queryPlayerMoveAnywhere(player);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Character queryPlayerMovePlayerToStart(Player player,
        List<Character> options) {
        return query("Move Player to Start", player
            + ", who would you like to move to the start?", options,
            Images.getImagesForCharacters(options));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String queryPlayerName(int player) {
        String name = null;
        do {
            name = JOptionPane.showInputDialog(frame, "Player #" + (player + 1)
                + ", please enter your name:", "Player Name",
                JOptionPane.QUESTION_MESSAGE);
            
            if ((name != null) && !name.isEmpty())
                return name;
            
            JOptionPane.showMessageDialog(frame,
                "Sorry, but you must enter a valid name.", "Invalid Name",
                JOptionPane.ERROR_MESSAGE);
        } while (true);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Option queryPlayerPlayKeeperCard(Player player, KeeperCard card) {
        return query("Play Keeper Card",
            player + ", would you like to play the " + card + " keeper card? ("
                + card.getShortDescription() + ")", Option.values());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Card queryPlayerShowCard(Player playerFrom, Player playerTo,
        Set<Card> options) {
        return query("Show Card", playerFrom
            + ", please select a card to show " + playerTo + ":",
            new ArrayList<>(options),
            Images.getImagesForCards(new ArrayList<>(options)));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Character, Weapon> queryPlayerStartRumour(Player player) {
        Character character = query("Rumour", player
            + ", please select a character for the rumour:",
            Character.values(),
            Images.getImagesForCharacters(Character.values()));
        Weapon weapon = query("Rumour", player
            + ", please select a weapon for the rumour:", Weapon.values(),
            Images.getImagesForWeapons(Weapon.values()));
        
        return new Pair<>(character, weapon);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerTurnChoice queryPlayerTurnChoice(Player player,
        Set<PlayerTurnChoice> options) {
        Optional<PlayerTurnChoice> turn = Optional.absent();
        
        // Let the peripheral input determine what the player wants to do, rather than asking them
        // explicitly in the GUI
        
        // Keep asking for a turn while it isn't a valid request
        while (!turn.isPresent() || !options.contains(turn.get())) {
            turn = frame.getTurnRequest();
            
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {}
        }
        
        return turn.get();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        while (!board.isGameOver()) {
            frame.repaint();
            
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {}
        }
    }
    
    private void inform(String title, String message) {
        inform(title, message, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void inform(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(frame, message, title, messageType);
    }
    
    private <T> T query(String title, String query, List<T> options) {
        checkArgument(options.size() > 0,
            "Must be at least one option for the user!");
        
        do {
            OptionDialogBuilder<T> builder = new OptionDialogBuilder<>(frame);
            builder.title(title).query(query).options(options);
            builder.show();
            Optional<T> opt = builder.getOption();
            
            if (opt.isPresent())
                return opt.get();
            
            JOptionPane.showMessageDialog(frame,
                "Sorry, but you must select a valid option.", "Invalid Option",
                JOptionPane.ERROR_MESSAGE);
        } while (true);
    }
    
    private <T> T query(String title, String query, List<T> options,
        List<Image> images) {
        checkArgument(options.size() > 0,
            "Must be at least one option for the user!");
        
        do {
            OptionDialogBuilder<T> builder = new OptionDialogBuilder<>(frame);
            builder.title(title).query(query).options(options, images);
            builder.show();
            Optional<T> opt = builder.getOption();
            
            if (opt.isPresent())
                return opt.get();
            
            JOptionPane.showMessageDialog(frame,
                "Sorry, but you must select a valid option.", "Invalid Option",
                JOptionPane.ERROR_MESSAGE);
        } while (true);
    }
    
    private <T> T query(String title, String query, T[] options) {
        return query(title, query, Arrays.asList(options));
    }
    
    private <T> T query(String title, String query, T[] options, Image[] images) {
        return query(title, query, Arrays.asList(options),
            Arrays.asList(images));
    }
}