package com.hjwylde.uni.swen222.assignment02.cluedo.logic;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Board;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Dice;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Player;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Tile;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.Card;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.CharacterCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.RoomCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.WeaponCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.intrigue.ClockCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.intrigue.IntrigueCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.intrigue.KeeperCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.intrigue.KeeperCard.ViewRumourRefuteCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Character;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Room;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Weapon;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.UI;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.choices.PlayerTurnChoice;
import com.hjwylde.uni.swen222.assignment02.cluedo.util.Coordinate;
import com.hjwylde.uni.swen222.assignment02.cluedo.util.Pair;
import com.hjwylde.uni.swen222.assignment02.cluedo.util.Path;
import com.hjwylde.uni.swen222.assignment02.cluedo.util.Triple;

/**
 * The game engine is what handles all of the game logic. It interfaces with the game board and user
 * interface to simulate a Cluedo game and apply all of the normal rules correctly. It also takes
 * care of initialising the game board.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 1/08/2013
 */
public final class GameEngine implements Runnable {
    
    private final UI ui;
    private final Board board;
    
    private final KeeperCards kc;
    
    /**
     * Creates a new <code>GameEngine</code> with the given user interface and game board.
     * 
     * @param ui the user interface.
     * @param board the game board.
     */
    public GameEngine(UI ui, Board board) {
        this.ui = Objects.requireNonNull(ui);
        this.board = Objects.requireNonNull(board);
        
        kc = new KeeperCards(ui, board);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        checkState(!board.isGameOver(),
            "The cluedo game has already been played on this board!");
        
        ui.informGameStart();
        
        initGameState();
        
        int player = 0; // The id of the current player whose turn it is
        while (!board.isGameOver()) {
            // While the game is going, iterate through each player's turn
            if (!board.isPlayerDead(player)) // Skip the dead players
                runPlayerTurn(player);
            
            player = (player + 1) % board.getNumberPlayers();
        }
    }
    
    /**
     * Checks whether the given player can move to the given position in <i>numberMoves</i> or less.
     * This method uses Dijkstras search algorithm to check if a path exists and will return the
     * smallest path that needs to be taken in order to move the player to that position. If the
     * player cannot move their within the given number of moves, then
     * <code>Optional.absent()</code> is returned.
     * 
     * @param player the player id.
     * @param end the position to move to.
     * @param numberMoves the maximum number of moves the player is allowed to make.
     * @return the smallest path to get to the position or absent.
     */
    private Optional<Path<Coordinate>> checkPlayerMove(int player,
        Coordinate end, int numberMoves, int turnStart) {
        if (end.equals(board.getPlayerPosition(player))) // Player can't move to their current
                                                         // position
            return Optional.absent();
        
        List<Coordinate> turn = board.getPlayerHistory(player, turnStart);
        
        // Check the player isn't trying to sneak into the same room they began in
        if (board.getTile(turn.get(0)).isRoom())
            if (board.getTile(turn.get(0)) == board.getTile(end))
                return Optional.absent();
        
        Queue<Path<Coordinate>> fringe = new PriorityQueue<>();
        fringe.offer(new Path<>(board.getPlayerPosition(player)));
        
        Set<Coordinate> visited = new HashSet<>();
        
        while (!fringe.isEmpty()) {
            Path<Coordinate> path = fringe.poll();
            Coordinate node = path.getNode();
            
            // Can't visit a node if:
            // a) it has already been visited by the algorithm
            // b) it's length is greater than the number of moves allowed
            // c) it has already been moved to in this player's current turn
            // d) is an invalid co-ordinate
            // e) is not an empty tile
            if (visited.contains(node))
                continue;
            if ((path.length() - 1) > numberMoves)
                continue;
            // Make sure to not check the very first node
            if (!node.equals(board.getPlayerPosition(player))) {
                if (turn.contains(node))
                    continue;
                if (!board.isValidCoordinate(node))
                    continue;
                if (!board.isEmptyTile(node))
                    continue;
            }
            
            visited.add(node);
            
            if (end.equals(node))
                return Optional.of(path); // Note that this path will have the start node attached
                
            Coordinate north = new Coordinate(node.getX(), node.getY() - 1);
            Coordinate east = new Coordinate(node.getX() - 1, node.getY());
            Coordinate south = new Coordinate(node.getX(), node.getY() + 1);
            Coordinate west = new Coordinate(node.getX() + 1, node.getY());
            
            fringe.offer(new Path<>(north, path));
            fringe.offer(new Path<>(east, path));
            fringe.offer(new Path<>(south, path));
            fringe.offer(new Path<>(west, path));
        }
        
        // Did not find a valid path to the end position.
        return Optional.absent();
    }
    
    /**
     * Initialises the game state. This goes through 6 steps:
     * <ol>
     * <li>Selects the number of players</li>
     * <li>Selects a character for each player</li>
     * <li>Randomises weapon locations</li>
     * <li>Randomly selects a murderer, weapon and room</li>
     * <li>Allocates the game cards (omitting murder cards) to the players</li>
     * <li>Shuffles the intrigue cards</li>
     * </ol>
     */
    private void initGameState() {
        // Select number of players
        int numberPlayers = ui.queryNumberPlayers(Arrays.asList(2, 3, 4, 5, 6));
        
        // Select player characters
        List<Character> available = new ArrayList<>();
        available.addAll(Arrays.asList(Character.values()));
        
        for (int i = 0; i < numberPlayers; i++) {
            Character choice = ui.queryPlayerCharacter(i, available);
            String name = ui.queryPlayerName(i);
            
            board.addPlayer(new Player(name, choice));
            
            available.remove(choice);
        }
        
        // Randomise weapon locations
        // IMPLEMENT: For the GUI this will be needed, for the CLI this isn't needed yet
        
        // Randomly select a murderer, weapon and room
        Character murderer = Character.values()[(int) (Math.random() * Character
            .values().length)];
        Weapon murderWeapon = Weapon.values()[(int) (Math.random() * Weapon
            .values().length)];
        // Little work around to ensure the murder room isn't the pool!
        Room murderRoom = Room.murderRoomValues()[(int) (Math.random() * Room
            .murderRoomValues().length)];
        
        board.setMurderer(murderer);
        board.setMurderWeapon(murderWeapon);
        board.setMurderRoom(murderRoom);
        
        // Allocate cards to players
        List<Card> cards = new ArrayList<>();
        
        cards.addAll(Arrays.asList(CharacterCard.values()));
        cards.remove(new CharacterCard(murderer));
        
        cards.addAll(Arrays.asList(WeaponCard.values()));
        cards.remove(new WeaponCard(murderWeapon));
        
        cards.addAll(Arrays.asList(RoomCard.values()));
        cards.remove(new RoomCard(murderRoom));
        
        Collections.shuffle(cards);
        
        int playerId = 0;
        while (!cards.isEmpty()) {
            board.getPlayer(playerId).addCard(cards.remove(0));
            
            // Get the next players id
            playerId = ++playerId % board.getNumberPlayers();
        }
        
        // Shuffle intrigue cards
        List<IntrigueCard> intrigueCards = new ArrayList<>();
        intrigueCards.addAll(Arrays.asList(KeeperCard.values()));
        intrigueCards.addAll(Arrays.asList(ClockCard.values()));
        
        Collections.shuffle(intrigueCards);
        
        board.setIntrigueCards(intrigueCards);
    }
    
    private void playerMakeAccusation(int player) {
        Triple<Character, Weapon, Room> accusation = ui
            .queryPlayerMakeAccusation(board.getPlayer(player));
        
        boolean correct = true;
        if (!board.getMurderer().equals(accusation.getFirst()))
            correct = false;
        if (!board.getMurderWeapon().equals(accusation.getSecond()))
            correct = false;
        if (!board.getMurderRoom().equals(accusation.getThird()))
            correct = false;
        
        if (correct) {
            ui.informPlayerWin(board.getPlayer(player), accusation);
            board.setGameOver(true);
        } else {
            ui.informPlayerWrongAccusationLoss(board.getPlayer(player));
            board.removePlayer(player);
        }
    }
    
    private int playerMove(int player, int turnStart) {
        Coordinate move = ui.queryPlayerMoveAnywhere(board.getPlayer(player));
        
        // Validate the move
        Optional<Path<Coordinate>> path = checkPlayerMove(player, move,
            1000000000, turnStart);
        if (!path.isPresent()) {
            ui.informPlayerIllegalMove(board.getPlayer(player));
            return 0; // Don't count this move or do anything
        }
        
        Iterator<Coordinate> it = path.get().iterator();
        it.next(); // Don't add the start co-ordinate to the move history
        while (it.hasNext())
            board.movePlayer(player, it.next()); // Count every co-ordinate the player moves to
        if (board.getTile(board.getPlayerPosition(player)) == Tile.INTRIGUE_CARD)
            playerPickUpIntrigueCard(player);
        
        return path.get().length() - 1; // Don't count the start node as a move
    }
    
    private int playerMove(int player, int maxMoves, int turnStart) {
        Coordinate move = checkNotNull(
            ui.queryPlayerMove(board.getPlayer(player), maxMoves),
            "Co-ordinate cannot be null");
        
        // Validate the move
        Optional<Path<Coordinate>> path = checkPlayerMove(player, move,
            maxMoves, turnStart);
        if (!path.isPresent()) {
            ui.informPlayerIllegalMove(board.getPlayer(player));
            return 0; // Don't count this move or do anything
        }
        
        Iterator<Coordinate> it = path.get().iterator();
        it.next(); // Don't add the start co-ordinate to the move history
        while (it.hasNext())
            board.movePlayer(player, it.next()); // Count every co-ordinate the player moves to
        if (board.getTile(board.getPlayerPosition(player)) == Tile.INTRIGUE_CARD) {
            // Nice to do before the UI freezes the board with a popup
            ui.informPlayerNumberMovesLeft(board.getPlayer(player), maxMoves
                - path.get().length() - 1);
            
            playerPickUpIntrigueCard(player);
        }
        
        return path.get().length() - 1; // Don't count the start node as a move
    }
    
    private void playerPickUpIntrigueCard(int player) {
        IntrigueCard card = board.pollIntrigueCard();
        
        if (card instanceof ClockCard) {
            int remaining = Iterables.size(Iterables.filter(
                board.getIntrigueCards(), ClockCard.class));
            
            if (remaining == 0) {
                ui.informPlayerClockCardLoss(board.getPlayer(player));
                board.removePlayer(player);
                
                // Re-add the last clock card and shuffle them
                List<IntrigueCard> cards = new ArrayList<>();
                cards.addAll(board.getIntrigueCards());
                cards.add(card);
                
                Collections.shuffle(cards);
                
                board.setIntrigueCards(cards);
            } else
                ui.informPlayerPickUpClockCard(board.getPlayer(player),
                    remaining);
        } else { // card instanceof KeeperCard
            ui.informPlayerPickUpKeeperCard(board.getPlayer(player),
                (KeeperCard) card);
            
            board.getPlayer(player).addKeeperCard((KeeperCard) card);
        }
    }
    
    private boolean playerRefuteRumour(int player, int refutePlayer,
        Set<Card> rumourCards) {
        Set<Card> cards = new HashSet<>(board.getPlayer(refutePlayer)
            .getCards());
        cards.retainAll(rumourCards);
        
        if (kc.queryPlayerPlayRefuseAnswerRumour(refutePlayer))
            return false;
        
        if (cards.size() == 0) // This player cannot refute the rumour, tell the UI and skip them
            ui.informPlayerUnableRefuteRumour(board.getPlayer(refutePlayer));
        else { // This player must refute the rumour
               // Ask the user what card to refute with
            Card response = ui.queryPlayerChooseRefuteRumourCard(
                board.getPlayer(refutePlayer), cards);
            playerShowPlayerCard(refutePlayer, player, response); // Show the player that started
                                                                  // the rumour the card
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Show a player a card from someone's hand. This is triggered because of a rumour refutation.
     * 
     * @param playerTo the player to show the card to.
     * @param card the card to show.
     */
    private void playerShowPlayerCard(int playerFrom, int playerTo, Card card) {
        // Check if any player wants to play a view rumour refute card keeper card
        for (int i : kc.findPlayersWithKeeperCard(ViewRumourRefuteCard.class,
            playerFrom, playerTo))
            kc.queryPlayerPlayViewRumourRefuteCard(i, card);
        
        board.getPlayer(playerTo).showCard(card); // Cross it off the player's notebook
        ui.informPlayerShowCard(board.getPlayer(playerTo), card); // Let the UI display something
    }
    
    private void playerStartRumour(int player) {
        Pair<Character, Weapon> rumour = ui.queryPlayerStartRumour(board
            .getPlayer(player));
        
        Set<Card> rumourCards = new HashSet<>();
        rumourCards.add(new CharacterCard(rumour.getFirst()));
        rumourCards.add(new WeaponCard(rumour.getSecond()));
        rumourCards.add(new RoomCard(board.getPlayerRoom(player).get()));
        
        // Note that this allows dead players to refute cards as per the rules!
        for (int i = 1; i < board.getNumberPlayers(); i++) {
            // Go in a clockwise rotation to ask players to refute the rumour
            int refutePlayer = (player + i) % board.getNumberPlayers();
            
            if (playerRefuteRumour(player, refutePlayer, rumourCards))
                return;
        }
        
        ui.informNoPlayerRefuteRumour(board.getPlayer(player));
    }
    
    /**
     * Starts the player's turn. The player's turn may involve a variety of steps, including keeper
     * cards, moving, starting a rumour or making an accusation.
     * 
     * @param player the player id.
     */
    private void runPlayerTurn(int player) {
        ui.informPlayerTurnStart(board.getPlayer(player));
        
        // Check if the player wants to move anywhere instead of moving
        boolean moveAnywhere = kc.queryPlayerMoveAnywhere(player);
        ui.informPlayerKeeperCardsChanged(board.getPlayer(player));
        
        // Check if the player wants to start a second rumour instead of moving
        boolean startSecondRumour = false;
        
        Tile tile = board.getTile(board.getPlayerPosition(player));
        if (tile.isRoom() && (tile != Tile.ROOM_POOL) && !moveAnywhere)
            startSecondRumour = kc.queryPlayerStartSecondRumour(player);
        
        ui.informPlayerKeeperCardsChanged(board.getPlayer(player));
        
        // Roll dice
        int numberMoves = 0;
        if (!moveAnywhere && !startSecondRumour) {
            numberMoves = Dice.roll();
            
            ui.informPlayerDiceRoll(board.getPlayer(player), numberMoves);
            
            // Check if player wants to play a dice add keeper card
            if (kc.queryPlayerPlayDiceAdd(player))
                numberMoves += 6;
        }
        
        // Record the starting position of the player, they may not return to any squares they've
        // moved to in this turn!
        int turnStart = board.getPlayerHistory(player).size() - 1;
        // A check to see whether the player has already started a rumour this turn
        boolean startedRumour = false;
        
        TURN:
        while (true) {
            ui.informPlayerNumberMovesLeft(board.getPlayer(player), numberMoves);
            
            Set<PlayerTurnChoice> choices = new TreeSet<>();
            choices.addAll(Arrays.asList(PlayerTurnChoice.values()));
            
            // Can the player no longer move?
            if ((numberMoves <= 0) && !moveAnywhere)
                choices.remove(PlayerTurnChoice.MOVE);
            
            // Can the player no longer start a rumour?
            if (!board.getTile(board.getPlayerPosition(player)).isRoom()
                || startedRumour)
                choices.remove(PlayerTurnChoice.START_RUMOUR);
            
            // Player can't start a rumour in the room they started their turn in, unless they
            // played a keeper card
            
            Coordinate turnStartCoord = board.getPlayerHistory(player).get(
                turnStart);
            if (!startSecondRumour)
                if (board.getTile(turnStartCoord).isRoom()
                    && board.getPlayerPosition(player).equals(turnStartCoord))
                    choices.remove(PlayerTurnChoice.START_RUMOUR);
            
            // Can the player no longer make an accusation?
            if (board.getTile(board.getPlayerPosition(player)) != Tile.ROOM_POOL)
                choices.remove(PlayerTurnChoice.MAKE_ACCUSATION);
            else
                // Catch the fact the last check allows the player to start a rumour in the Pool
                // room
                choices.remove(PlayerTurnChoice.START_RUMOUR);
            
            PlayerTurnChoice choice = ui.queryPlayerTurnChoice(
                board.getPlayer(player), choices);
            switch (choice) {
            case MOVE:
                int initialNumberMoves = numberMoves;
                if (moveAnywhere) {
                    if (playerMove(player, turnStart) > 0) {
                        moveAnywhere = false;
                        numberMoves--;
                    }
                } else
                    numberMoves -= playerMove(player, numberMoves, turnStart);
                
                // Check for whether the player picked up the 8th clock card and lost
                if (board.isPlayerDead(player))
                    break TURN;
                
                if (board.getTile(board.getPlayerPosition(player)).isRoom()
                    && (initialNumberMoves != numberMoves))
                    numberMoves = 0; // Player cannot move any more after moving into a room
                    
                continue;
            case START_RUMOUR:
                startedRumour = true;
                playerStartRumour(player);
                continue;
            case MAKE_ACCUSATION:
                playerMakeAccusation(player);
                return;
            case VIEW_CARDS:
                ui.informPlayerViewCards((board.getPlayer(player)));
                continue;
            case VIEW_KEEPER_CARDS:
                ui.informPlayerViewKeeperCards((board.getPlayer(player)));
                continue;
            case VIEW_NOTEBOOK:
                ui.informPlayerViewNotebook((board.getPlayer(player)));
                continue;
            case END:
                break TURN;
            }
        }
        
        ui.informPlayerTurnEnd(board.getPlayer(player));
        
        if (board.isPlayerDead(player))
            return;
        
        // Check if player wants to play a move palyer to start keeper card
        kc.queryPlayerPlayMovePlayerToStart(player);
        
        // Check if player wants to play a take second turn keeper card
        if (kc.queryPlayerPlayTakeSecondTurn(player))
            runPlayerTurn(player);
        
        // Check if player wants to play a view player card keeper card
        kc.queryPlayerPlayViewPlayerCard(player);
    }
}