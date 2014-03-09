package com.hjwylde.uni.swen222.assignment02.cluedo.game;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkPositionIndex;
import static com.google.common.base.Preconditions.checkState;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.*;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.intrigue.IntrigueCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Character;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Room;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Weapon;
import com.hjwylde.uni.swen222.assignment02.cluedo.util.Coordinate;

/**
 * Represents the board in a game of Cluedo. The board is responsible for handling information such
 * as the tiles, players, dead players, player move history, weapon locations, murder cards and
 * whether the game is over.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 25/07/2013
 */
public final class Board {

    // The game board and players
    private final Table<Integer, Integer, Tile> board;
    private final List<Player> players = new ArrayList<>();
    // Record dead players explicitely, that way a dead player can still refute a rumour
    private final Set<Player> deadPlayers = new HashSet<>();

    // Location stores
    /**
     * We need to store the history of all player moves so as to be able to check where they were on
     * their last turn (for starting rumours only when they have just moved into a room).
     */
    private final Map<Player, List<Coordinate>> playerHistory = new HashMap<>();
    private final Map<Weapon, Room> weaponLocations = new HashMap<>();

    // Murder cards
    private Character murderer = null;
    private Weapon murderWeapon = null;
    private Room murderRoom = null;

    // Intrigue cards
    private List<IntrigueCard> intrigueCards = null;

    private boolean isGameOver = false;

    /**
     * Creates a new <code>Board</code> with the given tiles. The tiles must have at least one tile
     * and none may be null.
     * 
     * @param board the tiles.
     */
    public Board(Tile[][] board) {
        checkArgument(board.length > 0, "Board must have at least one tile!");
        checkArgument(board[0].length > 0, "Board must have at least one tile!");

        Table<Integer, Integer, Tile> table = HashBasedTable.create(board.length, board[0].length);
        for (int x = 0; x < board.length; x++)
            for (int y = 0; y < board[x].length; y++)
                table.put(x, y, board[x][y]);

        this.board = ImmutableTable.copyOf(table);
    }

    /**
     * Adds the given player to the game and initialises their start position based on the character
     * they have chosen.
     * 
     * @param player the new player.
     */
    public synchronized void addPlayer(Player player) {
        players.add(Objects.requireNonNull(player));

        Optional<Coordinate> pos = getCharacterInitialPosition(player.getCharacter());
        checkArgument(
                pos.isPresent(),
                "Board has not been initialised with a valid starting position for: "
                        + player.getCharacter());

        playerHistory.put(player, new ArrayList<Coordinate>());
        playerHistory.get(player).add(pos.get());
    }

    /**
     * Tries to find the player index that is for the given character.
     * 
     * @param character the player's character.
     * @return the player's index if it exists.
     */
    public synchronized Optional<Integer> findPlayer(Character character) {
        for (int i = 0; i < players.size(); i++)
            if (players.get(i).getCharacter() == character)
                return Optional.of(i);

        return Optional.absent();
    }

    /**
     * Gets the character's initial position. The initial character position is pre-determined by
     * the character of the player.
     * 
     * @param character the character.
     * @return the initial position.
     */
    public Optional<Coordinate> getCharacterInitialPosition(Character character) {
        Tile tile = null;
        switch (character) {
            case DIANE_WHITE:
                tile = Tile.CHARACTER_DIANE_WHITE;
                break;
            case ELEANOR_PEACOCK:
                tile = Tile.CHARACTER_ELEANOR_PEACOCK;
                break;
            case JACK_MUSTARD:
                tile = Tile.CHARACTER_JACK_MUSTARD;
                break;
            case JACOB_GREEN:
                tile = Tile.CHARACTER_JACOB_GREEN;
                break;
            case KASANDRA_SCARLETT:
                tile = Tile.CHARACTER_KASANDRA_SCARLETT;
                break;
            case VICTOR_PLUM:
                tile = Tile.CHARACTER_VICTOR_PLUM;
                break;
            default:
                return Optional.absent();
        }

        for (int x : board.rowKeySet())
            for (int y : board.columnKeySet())
                if (board.get(x, y) == tile)
                    return Optional.of(new Coordinate(x, y));

        return Optional.absent();
    }

    /**
     * Gets the height of this board.
     * 
     * @return the height of this board.
     */
    public int getHeight() {
        return board.columnKeySet().size();
    }

    /**
     * Gets the intrigue cards left in this board.
     * 
     * @return the intrigue cards.
     */
    public ImmutableList<IntrigueCard> getIntrigueCards() {
        return ImmutableList.copyOf(intrigueCards);
    }

    /**
     * Gets the murderer for this game.
     * 
     * @return the murderer.
     */
    public Character getMurderer() {
        checkState(murderer != null, "Murderer has not been set!");

        return murderer;
    }

    /**
     * Gets the murder room for this game.
     * 
     * @return the murder room.
     */
    public Room getMurderRoom() {
        checkState(murderRoom != null, "Murder room has not been set!");

        return murderRoom;
    }

    /**
     * Gets the murder weapon for this game.
     * 
     * @return the murder weapon.
     */
    public Weapon getMurderWeapon() {
        checkState(murderWeapon != null, "Murder weapon has not been set!");

        return murderWeapon;
    }

    /**
     * Gets the number of players in this game.
     * 
     * @return the number of players.
     */
    public int getNumberPlayers() {
        return players.size();
    }

    /**
     * Gets the player at the given index.
     * 
     * @param player the player index.
     * @return the player.
     */
    public synchronized Player getPlayer(int player) {
        return players.get(player);
    }

    /**
     * Gets an immutable list of the player's move history.
     * 
     * @param player the player.
     * @return the move history.
     */
    public synchronized ImmutableList<Coordinate> getPlayerHistory(int player) {
        checkElementIndex(player, players.size(), "Player is not present on the board!");
        checkState(playerHistory.containsKey(players.get(player)));

        return ImmutableList.copyOf(playerHistory.get(players.get(player)));
    }

    /**
     * Gets an immutable sub list of the player's move history.
     * 
     * @param player the player.
     * @param from the from index (inclusive).
     * @return the move history.
     */
    public synchronized ImmutableList<Coordinate> getPlayerHistory(int player, int from) {
        List<Coordinate> history = getPlayerHistory(player);

        // from index may be the size, in the event the size is 0
        checkPositionIndex(from, history.size(), "From index is out of bounds!");

        return ImmutableList.copyOf(history.subList(from, history.size()));
    }

    /**
     * Gets the player index for the given player.
     * 
     * @param player the player.
     * @return the player index.
     */
    public synchronized int getPlayerIndex(Player player) {
        for (int i = 0; i < players.size(); i++)
            if (players.get(i).equals(player))
                return i;

        throw new IllegalArgumentException("Player not found: " + player);
    }

    /**
     * Gets the player position as a pair of integers. The first value is the x co-ordinate and the
     * second value is the y co-ordinate.
     * 
     * @param player the player to get the position of.
     * @return the player position.
     */
    public synchronized Coordinate getPlayerPosition(int player) {
        List<Coordinate> history = playerHistory.get(players.get(player));
        return history.get(history.size() - 1);
    }

    /**
     * Attempts to get the room the player is currently in if they are in one.
     * 
     * @param player the player.
     * @return the room if they are in one.
     */
    public synchronized Optional<Room> getPlayerRoom(int player) {
        return getTile(getPlayerPosition(player)).getRoom();
    }

    /**
     * Gets the tile at the given position.
     * 
     * @param pos the position of the tile to get.
     * @return the tile.
     */
    public Tile getTile(Coordinate pos) {
        checkArgument(isValidCoordinate(pos), "Co-ordinate is invalid: " + pos);

        return board.get(pos.getX(), pos.getY());
    }

    /**
     * Gets an immutable map of the weapon locations.
     * 
     * @return an immutable map of the weapon locations.
     */
    public ImmutableMap<Weapon, Room> getWeaponLocations() {
        return ImmutableMap.copyOf(weaponLocations);
    }

    /**
     * Gets the width of this board.
     * 
     * @return the width.
     */
    public int getWidth() {
        return board.rowKeySet().size();
    }

    /**
     * Checks whether a player can move to (be in) a specific position. It checks whether the tile
     * is a valid tile for them, then checks that no other player is currently on that tile.
     * 
     * @param pos the position to check.
     * @return true if the tile is empty / a valid position for a player.
     */
    public boolean isEmptyTile(Coordinate pos) {
        checkArgument(isValidCoordinate(pos), "Co-ordinate is invalid: " + pos);

        // Check if the tile is valid first
        switch (getTile(pos)) {
            case CHARACTER_DIANE_WHITE:
            case CHARACTER_ELEANOR_PEACOCK:
            case CHARACTER_JACK_MUSTARD:
            case CHARACTER_JACOB_GREEN:
            case CHARACTER_KASANDRA_SCARLETT:
            case CHARACTER_VICTOR_PLUM:
            case DOORWAY:
            case EMPTY:
            case INTRIGUE_CARD:
                break; // Valid, go on to next checks
            case ROOM_DINING_ROOM:
            case ROOM_GUEST_HOUSE:
            case ROOM_HALL:
            case ROOM_KITCHEN:
            case ROOM_LIVING_ROOM:
            case ROOM_OBSERVATORY:
            case ROOM_PATIO:
            case ROOM_POOL:
            case ROOM_SPA:
            case ROOM_THEATRE:
                return true; // More than one player may be in a room at once
            case IMPASSABLE:
                return false; // Impassable squares cannot be moved onto
            default:
                throw new InternalError("isEmptyTile(Coordinate) not fully implemented: "
                        + getTile(pos));
        }

        // Check that no player is on the square
        for (int i = 0; i < players.size(); i++)
            if (!isPlayerDead(i) && pos.equals(getPlayerPosition(i)))
                return false;

        return true;
    }

    /**
     * Checks whether the game is over.
     * 
     * @return true if the game is over.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Checks whether a player is dead or not. A dead player is one that has made a wrong accusation
     * and can no longer make moves.
     * 
     * @param player the player id.
     * @return true if the player is dead.
     */
    public boolean isPlayerDead(int player) {
        return deadPlayers.contains(players.get(player));
    }

    /**
     * Checks whether the given position is valid with respect to the board size.
     * 
     * @param pos the position.
     * @return true if it is valid.
     */
    public boolean isValidCoordinate(Coordinate pos) {
        return board.containsRow(pos.getX()) && board.containsColumn(pos.getY());
    }

    /**
     * Moves the given player to the new position on the board.
     * 
     * @param player the player to move.
     * @param pos the new position.
     */
    public void movePlayer(int player, Coordinate pos) {
        checkElementIndex(player, players.size(), "Player is not present on the board!");

        playerHistory.get(players.get(player)).add(pos);
    }

    /**
     * Moves the given weapon to the given room. This functionality of the game is merely for
     * aesthetics. It does nothing to affect gameplay.
     * 
     * @param weapon the weapon to move.
     * @param room the room to move to.
     */
    public void moveWeapon(Weapon weapon, Room room) {
        checkArgument(room != Room.POOL, "The weapon cannot be moved to the pool room!");

        weaponLocations.put(Objects.requireNonNull(weapon), Objects.requireNonNull(room));
    }

    /**
     * Polls the intrigue cards for the next one. This method will remove the topmost intrigue card
     * from the list. Note that it assumes there is at least one intrigue card in the list, this
     * should be ensured by the game logic by forcing there to always be at least one clock card
     * (the eighth) in the list.
     * 
     * @return the next intrigue card.
     */
    public IntrigueCard pollIntrigueCard() {
        return intrigueCards.remove(0);
    }

    /**
     * Removes the given player from the game. A removed player may not make any moves or play any
     * cards, but they may still refute rumours.
     * 
     * @param player the player to remove.
     */
    public void removePlayer(int player) {
        deadPlayers.add(players.get(player));
        playerHistory.remove(players.get(player));

        // Ha... Guess the players managed to all kill themselves
        // FIXME: Should somehow send a message to the UI, in otherwords, this needs to be in the
        // game logic
        if (deadPlayers.size() == players.size())
            isGameOver = true;
    }

    /**
     * Sets whether the game is over or not.
     * 
     * @param isGameOver true if the game is over.
     */
    public void setGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    /**
     * Sets the intrigue cards to the given list. This method will defensively copy the intrigue
     * cards.
     * 
     * @param intrigueCards the new intrigue cards.
     */
    public void setIntrigueCards(List<IntrigueCard> intrigueCards) {
        checkArgument(!Iterables.contains(intrigueCards, null),
                "Intrigue cards must not contain a null value");

        this.intrigueCards = new ArrayList<>(intrigueCards);
    }

    /**
     * Sets the murderer to the given character.
     * 
     * @param murderer the murderer.
     */
    public void setMurderer(Character murderer) {
        checkState(this.murderer == null, "Murderer has already been set!");

        this.murderer = Objects.requireNonNull(murderer);
    }

    /**
     * Sets the murder room to the given room.
     * 
     * @param room the murder room.
     */
    public void setMurderRoom(Room room) {
        checkState(murderRoom == null, "Murder room has already been set!");

        murderRoom = Objects.requireNonNull(room);
    }

    /**
     * Sets the murder weapon to the given weapon.
     * 
     * @param weapon the murder weapon.
     */
    public void setMurderWeapon(Weapon weapon) {
        checkState(murderWeapon == null, "Murder weapon has already been set!");

        murderWeapon = Objects.requireNonNull(weapon);
    }
}
