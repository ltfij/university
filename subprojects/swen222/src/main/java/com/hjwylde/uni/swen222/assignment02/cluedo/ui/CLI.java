package com.hjwylde.uni.swen222.assignment02.cluedo.ui;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Board;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Player;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.Card;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.CharacterCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.RoomCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.WeaponCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.intrigue.KeeperCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Character;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Room;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Weapon;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.choices.Option;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.choices.PlayerTurnChoice;
import com.hjwylde.uni.swen222.assignment02.cluedo.util.Coordinate;
import com.hjwylde.uni.swen222.assignment02.cluedo.util.Pair;
import com.hjwylde.uni.swen222.assignment02.cluedo.util.Triple;

/**
 * A command line interface for playing the game Cluedo with. This interface outputs and receives
 * input strictly in textual format.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 25/07/2013
 */
public final class CLI implements UI {
    
    private final Board board;
    
    private final BufferedReader in;
    private final PrintWriter out;
    
    /**
     * Creates a new <code>CLI</code> with the given board, input and output streams.
     * 
     * @param board the board.
     * @param in the input stream to read input from.
     * @param out the output stream to output information to.
     */
    public CLI(Board board, InputStream in, OutputStream out) {
        this.board = Objects.requireNonNull(board);
        
        this.in = new BufferedReader(new InputStreamReader(
            Objects.requireNonNull(in)));
        this.out = new PrintWriter(Objects.requireNonNull(out));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informGameStart() {
        out.println("Welcome to Cluedo!");
        out.println("This game uses a text based user interface. Here's a few notes to help you get started:");
        out.println("When it's your turn, your character position will be indicated with a '$' marker on the board.");
        out.println("When selecting an option, enter the option number (e.g. \"1\") into the console and press enter.");
        out.println("When selecting a move, enter the co-ordinates of the position you want to move to (e.g. \"(x, y)\") into the console and press enter.");
        out.println();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informNoPlayerRefuteRumour(Player player) {
        out.println();
        out.printf("Looks like no one could refute %s's rumour...%n", player);
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerClockCardLoss(Player player) {
        out.println();
        out.printf(
            "%s has just picked up the 8th clock card! They are now out of the game.%n",
            player);
        out.print("The 8th clock card has been placed back into the intrigue card stack.%n");
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerDiceRoll(Player player, int numberMoves) {
        out.println();
        out.printf("%s has just rolled a %s!%n", player, numberMoves);
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerIllegalMove(Player player) {
        out.println();
        out.printf("Sorry %s, but you can't move there!%n", player);
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerKeeperCardsChanged(Player player) {
        // Don't need to do anything, this is for the GUI
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerMoveAnywhere(Player player) {
        out.println();
        out.printf(
            "%s has played their keeper card and can move anywhere they like this turn!%n",
            player);
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerMovedToStart(Player player, Character moved) {
        out.println();
        out.printf("%s has just moved %s to their starting position!%n",
            player, moved);
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerNumberMovesLeft(Player player, int numberMoves) {
        // Don't need to do anything, this method is for the GUI
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerPickUpClockCard(Player player, int remaining) {
        out.println();
        out.printf("%s has just picked up the %s%s clock card!%n", player,
            8 - remaining, remaining == 7 ? "st" : remaining == 6 ? "nd"
                : remaining == 5 ? "rd" : "th");
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerPickUpKeeperCard(Player player, KeeperCard card) {
        out.println();
        out.printf("%s has just picked up the \"%s\" keeper card!%n", player,
            card);
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerShowCard(Player player, Card card) {
        out.println();
        out.printf("Here %s, have a look at this card... %s%n", player, card);
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerTurnEnd(Player player) {
        // Don't need to do anything, this method is for the GUI
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerTurnStart(Player player) {
        // Don't need to do anything, this method is for the GUI
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerUnableRefuteRumour(Player player) {
        out.println();
        out.printf("%s is unable to refute the rumour.%n", player);
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerViewCards(Player player) {
        out.println();
        out.printf("%s's cards:%n", player);
        
        for (Card card : player.getCards())
            out.println(card);
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerViewKeeperCards(Player player) {
        out.println();
        
        if (player.getKeeperCards().size() == 0)
            out.printf("%s has no keeper cards.%n", player);
        else {
            out.printf("%s's keeper cards:%n", player);
            
            Set<KeeperCard> listed = new HashSet<>();
            
            for (KeeperCard card : player.getKeeperCards()) {
                if (listed.contains(card))
                    continue;
                
                listed.add(card);
                
                int quantity = Iterables.size(Iterables.filter(
                    player.getKeeperCards(), card.getClass()));
                
                out.printf("%s x%s%n\t%s%n", card.getName(), quantity,
                    card.getDescription());
            }
        }
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerViewNotebook(Player player) {
        out.println();
        out.printf("%s's notebook of unseen cards:%n", player);
        
        int heading = -1; // 0 for Character, 1 for Weapon, 2 for Room
        for (Card card : player.getNotebook().availableCards()) {
            // Print out headings
            if (card instanceof CharacterCard) {
                if (heading != 0)
                    out.println("Character card(s):");
                
                heading = 0;
            } else if (card instanceof WeaponCard) {
                if (heading != 1) {
                    out.println();
                    out.println("Weapon card(s):");
                }
                
                heading = 1;
            } else if (card instanceof RoomCard) {
                if (heading != 2) {
                    out.println();
                    out.println("Room card(s):");
                }
                
                heading = 2;
            }
            
            out.println(card);
        }
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerWin(Player player,
        Triple<Character, Weapon, Room> accusation) {
        out.println();
        out.printf("Congratulations %s! You have won the game!%n", player);
        out.printf("%s was killed with the %s in the %s room.%n",
            accusation.getFirst(), accusation.getSecond(),
            accusation.getThird());
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void informPlayerWrongAccusationLoss(Player player) {
        out.println();
        out.printf(
            "%s has just made a wrong accusation! They are now out of the game.%n",
            player);
        
        out.flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int queryNumberPlayers(List<Integer> options) {
        out.println();
        out.printf("Please select the number of players for this game:%n");
        
        return query(options);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Character queryPlayerCharacter(int player, List<Character> options) {
        out.println();
        out.printf("Player #%s, please select your character:%n", player + 1);
        
        return query(options);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Card queryPlayerChooseRefuteRumourCard(Player player,
        Set<Card> options) {
        out.println();
        out.printf("%s please refute the rumour with one of your cards:%n",
            player);
        
        return query(options);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Triple<Character, Weapon, Room> queryPlayerMakeAccusation(
        Player player) {
        out.println();
        out.printf("%s is about to make an accusation!%n", player);
        
        out.printf("%s, please select a murderer:%n", player);
        Character murderer = query(Character.values());
        
        out.printf("%s, please select a murder weapon:%n", player);
        Weapon weapon = query(Weapon.values());
        
        out.printf("%s, please select a murder room:%n", player);
        Room room = query(Room.murderRoomValues());
        
        return new Triple<>(murderer, weapon, room);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Coordinate queryPlayerMove(Player player, int maxMoves) {
        out.println();
        out.printf("You have %s move%s left to make.%n", maxMoves,
            maxMoves == 1 ? "" : "s");
        printBoard(player);
        out.print("Please enter a co-ordinate: ");
        out.flush();
        
        return queryCoordinate();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Coordinate queryPlayerMoveAnywhere(Player player) {
        out.println();
        out.println("You can move anywhere you like.");
        printBoard(player);
        out.print("Please enter a co-ordinate: ");
        out.flush();
        
        return queryCoordinate();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Character queryPlayerMovePlayerToStart(Player player,
        List<Character> options) {
        out.println();
        out.printf("Which player would you like to move to the start?%n",
            player);
        
        return query(options);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String queryPlayerName(int player) {
        // IMPLEMENT: queryPlayerName(int)
        throw new InternalError("queryPlayerName(int) not implemented");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Option queryPlayerPlayKeeperCard(Player player, KeeperCard card) {
        out.println();
        out.printf("%s, would you like to play the \"%s\" keeper card? (%s)%n",
            player, card.getName(), card.getShortDescription());
        
        return query(Option.values());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Card queryPlayerShowCard(Player playerFrom, Player playerTo,
        Set<Card> options) {
        out.println();
        out.printf("%s,  please select a card to show %s:%n", playerFrom,
            playerTo);
        
        return query(options);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Character, Weapon> queryPlayerStartRumour(Player player) {
        out.println();
        out.printf("%s is about to start a rumour!%n", player);
        
        out.printf("%s, please select a murderer:%n", player);
        Character murderer = query(Character.values());
        
        out.printf("%s, please select a murder weapon:%n", player);
        Weapon weapon = query(Weapon.values());
        
        return new Pair<>(murderer, weapon);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerTurnChoice queryPlayerTurnChoice(Player player,
        Set<PlayerTurnChoice> options) {
        out.println();
        out.printf("It's your turn %s! What would you like to do?%n", player);
        
        return query(options);
    }
    
    private void printBoard(Player player) {
        // Print out top x co-ordinates
        out.print("  ");
        for (int x = 0; x < board.getWidth(); x++) {
            char c = (char) ('a' + x);
            out.printf("%s", c);
        }
        
        out.println();
        
        for (int y = 0; y < board.getHeight(); y++) {
            // Print out left y co-ordinates
            char c = (char) ('a' + y);
            out.printf("%s ", c);
            
            // Print out tiles for the row
            for (int x = 0; x < board.getWidth(); x++)
                printTile(player, new Coordinate(x, y));
            
            // Print out right y co-ordinates
            out.printf(" %s", c);
            
            out.println();
        }
        
        // Print out bottom x co-ordinates
        out.print("  ");
        for (int x = 0; x < board.getWidth(); x++) {
            char c = (char) ('a' + x);
            out.printf("%s", c);
        }
        
        out.println();
    }
    
    private void printTile(Player player, Coordinate pos) {
        // Print out a special character for the current player
        for (int i = 0; i < board.getNumberPlayers(); i++) {
            if (board.isPlayerDead(i)
                || !board.getPlayerPosition(i).equals(pos))
                continue;
            
            if (board.getPlayer(i).equals(player)) {
                out.print('$');
                return;
            }
        }
        
        // Do the next checks for other players
        for (int i = 0; i < board.getNumberPlayers(); i++) {
            if (board.isPlayerDead(i)
                || !board.getPlayerPosition(i).equals(pos))
                continue;
            
            switch (board.getPlayer(i).getCharacter()) {
            case DIANE_WHITE:
                out.print('W');
                return;
            case ELEANOR_PEACOCK:
                out.print('P');
                return;
            case JACK_MUSTARD:
                out.print('M');
                return;
            case JACOB_GREEN:
                out.print('G');
                return;
            case KASANDRA_SCARLETT:
                out.print('S');
                return;
            case VICTOR_PLUM:
                out.print('R');
                return;
            }
        }
        
        // Check what tile is at this position
        switch (board.getTile(pos)) {
        case CHARACTER_DIANE_WHITE:
        case CHARACTER_ELEANOR_PEACOCK:
        case CHARACTER_JACK_MUSTARD:
        case CHARACTER_JACOB_GREEN:
        case CHARACTER_KASANDRA_SCARLETT:
        case CHARACTER_VICTOR_PLUM:
            out.print(' '); // Don't print the start position tiles
            return;
        default:
            out.print(board.getTile(pos).getTile());
        }
    }
    
    private <T> T query(Iterable<T> options) {
        return query(ImmutableList.copyOf(options));
    }
    
    private <T> T query(List<T> options) {
        checkArgument(options.size() > 0,
            "Must have at least one option for the user!");
        
        // Print out the options
        for (int i = 1; i <= options.size(); i++)
            out.printf("Option #%s: %s%n", i, options.get(i - 1));
        
        out.flush();
        
        // Keep asking for an option until a valid one is given
        int option = -1;
        while (true) {
            String answer = null;
            try {
                answer = in.readLine();
            } catch (IOException e) { // Uh oh...
                e.printStackTrace(out);
                out.flush();
                System.exit(1);
            }
            
            try {
                option = Integer.valueOf(answer);
            } catch (NumberFormatException e) {}
            
            if ((option >= 1) && (option <= options.size()))
                return options.get(option - 1);
            
            out.printf(
                "Sorry, but \"%s\" is not a valid option. Please select again.%n",
                answer);
            out.flush();
        }
    }
    
    private <T> T query(T[] options) {
        return query(ImmutableList.copyOf(options));
    }
    
    private Coordinate queryCoordinate() {
        String answer = null;
        try {
            answer = in.readLine();
        } catch (IOException e) { // Uh oh...
            e.printStackTrace(out);
            out.flush();
            throw new RuntimeException(e); // CONSIDER: Surely there must be something better to
                                           // do...
        }
        
        if (!Pattern.matches("\\(., .\\)", answer)) {
            out.printf(
                "Sorry, but \"%s\" is not in a valid co-ordinate format. Please enter another: ",
                answer);
            out.flush();
            
            return queryCoordinate();
        }
        
        int x = answer.charAt(1) - 'a'; // Co-ordinates start from 'a'
        int y = answer.charAt(4) - 'a';
        
        Coordinate coord = new Coordinate(x, y);
        
        if (!board.isValidCoordinate(coord)) {
            out.printf(
                "Sorry, but \"%s\" is not a valid co-ordinate. Please enter another: ",
                coord);
            out.flush();
            
            return queryCoordinate();
        }
        
        return coord;
    }
}