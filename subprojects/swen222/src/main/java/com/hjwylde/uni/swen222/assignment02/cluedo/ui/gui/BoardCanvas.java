package com.hjwylde.uni.swen222.assignment02.cluedo.ui.gui;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.Math.abs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JPanel;

import com.google.common.base.Optional;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Board;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Player;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Room;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.choices.PlayerTurnChoice;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.gui.Images.ImageObject;
import com.hjwylde.uni.swen222.assignment02.cluedo.util.Coordinate;

/**
 * A canvas that draws the Cluedo board. This class takes care of drawing other useful information,
 * such as what squares are available for a player to move to in their turn.
 * 
 * @author Henry J. Wylde
 * 
 * @since 31/08/2013
 */
public final class BoardCanvas extends JPanel implements KeyListener, MouseListener {

    private static final long serialVersionUID = 1L;

    private final Board board;

    private PlayerTurnChoice turnRequest = null;
    private int player = -1;
    private int turnStart = -1;
    private int movesLeft = 0;
    private boolean moveAnywhere = false;
    private Coordinate move = null;

    /**
     * Creates a new <code>BoardCanvas</code> with the given board.
     * 
     * @param board the board.
     */
    public BoardCanvas(Board board) {
        this.board = checkNotNull(board, "Board cannot be null");

        setLayout(null);
        setPreferredSize(new Dimension(465, 465));
    }

    /**
     * Gets the turn request of this canvas. The turn request indicates that the user applied some
     * action to the canvas which implied they wanted to do a certain type of turn, such as moving.
     * After a turn request is retrieved, it is reset.
     * 
     * @return the turn request.
     */
    public synchronized Optional<PlayerTurnChoice> getTurnRequest() {
        // Fast track the turn requests for certain events
        if (board.isPlayerDead(player)) {
            turnRequest = null;
            return Optional.of(PlayerTurnChoice.END);
        }

        Optional<Room> opt = board.getPlayerRoom(player);

        if (opt.isPresent() && (turnRequest == null)) {
            turnRequest = null;

            if (opt.get() == Room.POOL)
                return Optional.of(PlayerTurnChoice.MAKE_ACCUSATION);
            return Optional.of(PlayerTurnChoice.START_RUMOUR);
        }

        PlayerTurnChoice ret = turnRequest;
        turnRequest = null;

        return Optional.fromNullable(ret);
    }

    /**
     * Informs the canvas of a player dice roll. This helps to maintain the right number of squares
     * to show that a user may move to.
     * 
     * @param player the player whose turn it is.
     * @param numberMoves the number of moves they may make.
     */
    public void informPlayerDiceRoll(Player player, int numberMoves) {
        checkState(player.equals(board.getPlayer(this.player)));

        movesLeft = numberMoves;
    }

    /**
     * Informs that the player may move anywhere.
     * 
     * @param player the player who can move anywhere.
     */
    public void informPlayerMoveAnywhere(Player player) {
        checkState(player.equals(board.getPlayer(this.player)));

        moveAnywhere = true;
    }

    /**
     * Informs the canvas of the number of moves left in a player's turn.
     * 
     * @param player the player whose turn it is.
     * @param numberMoves the number of moves they may make.
     */
    public void informPlayerNumberMovesLeft(Player player, int numberMoves) {
        checkState(player.equals(board.getPlayer(this.player)));

        movesLeft = numberMoves;
    }

    /**
     * Informs the canvas that the given player's turn has just ended. This method will reset any
     * variables relating to that player.
     * 
     * @param player the player whose turn has just ended.
     */
    public synchronized void informPlayerTurnEnd(Player player) {
        checkState(player.equals(board.getPlayer(this.player)));

        turnRequest = null;
        this.player = -1;
        turnStart = -1;
        movesLeft = 0;
        moveAnywhere = false;
        move = null;
    }

    /**
     * Informs the canvas that the given player's turn has just started. This method will then set
     * variables to their defaults for the given player.
     * 
     * @param player the player whose turn has just started.
     */
    public synchronized void informPlayerTurnStart(Player player) {
        this.player = board.getPlayerIndex(player);

        checkState(!board.isPlayerDead(this.player), "Player cannot be dead!");

        turnRequest = null;
        turnStart = board.getPlayerHistory(this.player).size() - 1;
        movesLeft = 0;
        moveAnywhere = false;
        move = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void keyPressed(KeyEvent e) {
        if ((e.getModifiers() != 0) || (player < 0) || ((movesLeft <= 0) && !moveAnywhere)
                || (turnStart < 0) || board.isPlayerDead(player))
            return;

        Coordinate pos = board.getPlayerPosition(player);

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_K:
                move = new Coordinate(pos.getX(), pos.getY() - 1);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_J:
                move = new Coordinate(pos.getX(), pos.getY() + 1);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_H:
                move = new Coordinate(pos.getX() - 1, pos.getY());
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_L:
                move = new Coordinate(pos.getX() + 1, pos.getY());
        }

        turnRequest = PlayerTurnChoice.MOVE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void mouseClicked(MouseEvent e) {
        if ((player < 0) || ((movesLeft <= 0) && !moveAnywhere) || (turnStart < 0)
                || board.isPlayerDead(player))
            return;

        move = boardToTile(new Coordinate(e.getX(), e.getY()));
        turnRequest = PlayerTurnChoice.MOVE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseEntered(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        paintBoard(g2d);
        paintCharacters(g2d);

        if ((player < 0) || ((movesLeft <= 0) && !moveAnywhere) || (turnStart < 0)
                || board.isPlayerDead(player))
            return;

        if (moveAnywhere)
            paintValidMoves(g2d, new Coordinate(0, 0),
                    new Coordinate(board.getWidth(), board.getHeight()), 100000);
        else {
            // For when paint is called before the game has fully been initialised
            if (board.getPlayerHistory(player).isEmpty())
                return;

            Coordinate pos = board.getPlayerPosition(player);
            paintValidMoves(g2d, new Coordinate(pos.getX() - movesLeft, pos.getY() - movesLeft),
                    new Coordinate(pos.getX() + movesLeft + 1, pos.getY() + movesLeft + 1),
                    movesLeft);
        }
    }

    /**
     * Queries the canvas for a player move. This method should only be called after a move turn
     * request has been sent off, so as to ensure that the move variable is not null.
     * 
     * @param player the player whose turn it is.
     * @return the move.
     */
    public synchronized Coordinate queryPlayerMove(Player player) {
        checkState(player.equals(board.getPlayer(this.player)));

        if (move == null)
            return board.getPlayerPosition(board.getPlayerIndex(player));

        Coordinate ret = move;
        move = null;

        return ret;
    }

    /**
     * Queries for a move that may be any valid square on the board for the given player.
     * 
     * @param player the player.
     * @return the move.
     */
    public Coordinate queryPlayerMoveAnywhere(Player player) {
        checkState(player.equals(board.getPlayer(this.player)));
        checkState(moveAnywhere, "Logic should not call move anywhere unless we actually can!");

        if (move == null)
            return board.getPlayerPosition(board.getPlayerIndex(player));

        moveAnywhere = false;

        Coordinate ret = move;
        move = null;

        return ret;
    }

    /**
     * Converts a board co-ordinate to a tile co-ordinate.
     * 
     * @param coord the board co-ordinate.
     * @return the tile co-ordinate.
     */
    private Coordinate boardToTile(Coordinate coord) {
        int x = ((coord.getX() * board.getWidth()) / getWidth());
        int y = ((coord.getY() * board.getHeight()) / getHeight());
        return new Coordinate(x, y);
    }

    /**
     * Gets the tile height in pixels relative to the canvas.
     * 
     * @return the tile height in pixels.
     */
    private int getTileHeight() {
        return getHeight() / board.getHeight();
    }

    /**
     * Gets the tile width in pixels relative to the canvas.
     * 
     * @return the tile width in pixels.
     */
    private int getTileWidth() {
        return getWidth() / board.getWidth();
    }

    /**
     * Draws the board image to the canvas.
     * 
     * @param g the graphics.
     */
    private void paintBoard(Graphics2D g) {
        g.drawImage(Images.getImage(ImageObject.BOARD), 0, 0, this);
    }

    /**
     * Draws all characters on the canvas in their current positions.
     * 
     * @param g the graphics.
     */
    private void paintCharacters(Graphics2D g) {
        for (int i = 0; i < board.getNumberPlayers(); i++) {
            if (board.isPlayerDead(i))
                continue;

            switch (board.getPlayer(i).getCharacter()) {
                case DIANE_WHITE:
                    g.setColor(Color.WHITE);
                    break;
                case ELEANOR_PEACOCK:
                    g.setColor(Color.BLUE);
                    break;
                case JACK_MUSTARD:
                    g.setColor(Color.YELLOW);
                    break;
                case JACOB_GREEN:
                    g.setColor(Color.GREEN);
                    break;
                case KASANDRA_SCARLETT:
                    g.setColor(Color.RED);
                    break;
                case VICTOR_PLUM:
                    g.setColor(Color.MAGENTA);
                    break;
            }

            Coordinate pos = tileToBoard(board.getPlayerPosition(i));

            g.fillRect(pos.getX(), pos.getY(), getTileWidth(), getTileHeight());
        }
    }

    /**
     * Paint all the valid moves of the current player.
     * 
     * @param g the graphics.
     */
    private void paintValidMoves(Graphics2D g, Coordinate start, Coordinate end, int movesLeft) {
        Coordinate pos = board.getPlayerPosition(player);
        List<Coordinate> turn = board.getPlayerHistory(player, turnStart);

        for (int x = start.getX(); x < end.getX(); x++)
            for (int y = start.getY(); y < end.getY(); y++) {
                // Check whether the Manhattan distance between the two tiles is less than the
                // number of moves left
                if ((abs(x - pos.getX()) + abs(y - pos.getY())) > movesLeft)
                    continue;

                Coordinate coord = new Coordinate(x, y);

                if (!board.isValidCoordinate(coord))
                    continue;

                if (!board.isEmptyTile(coord))
                    continue;

                if (turn.contains(coord))
                    g.setColor(Color.RED); // Colour squares they have already moved to in red to
                                           // show they can't move there
                else
                    g.setColor(Color.GREEN);

                Coordinate tile = tileToBoard(coord);
                g.drawRect(tile.getX(), tile.getY(), getTileWidth(), getTileHeight());
            }
    }

    /**
     * Converts a tile co-ordinate to a board co-ordinate.
     * 
     * @param coord the tile co-ordinate.
     * @return the board co-ordinate.
     */
    private Coordinate tileToBoard(Coordinate coord) {
        int x = coord.getX() * getTileWidth();
        int y = coord.getY() * getTileHeight();
        return new Coordinate(x, y);
    }
}
