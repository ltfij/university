package com.hjwylde.uni.swen222.assignment02.cluedo.test.game;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.hjwylde.uni.swen222.assignment02.cluedo.game.Board;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Player;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Tile;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Character;

/**
 * Board tests. A rather bad suite of tests... Most aspects are hard to test due to the complicated
 * game rules / dynamics.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 10/08/2013
 */
@SuppressWarnings("static-method")
public final class BoardTests {

    private static final Tile[][] TILES = new Tile[][] {new Tile[] {Tile.EMPTY}};

    /**
     * Tests the fact that this board does not have a player start tile for any of the characters.
     */

    @Test
    public void cantFindPlayerStartPosition() {
        Board board = new Board(TILES);

        try {
            board.addPlayer(new Player("foo", Character.DIANE_WHITE));
            fail();
        } catch (Exception e) {
        }
        try {
            board.addPlayer(new Player("foo", Character.ELEANOR_PEACOCK));
            fail();
        } catch (Exception e) {
        }
        try {
            board.addPlayer(new Player("foo", Character.JACK_MUSTARD));
            fail();
        } catch (Exception e) {
        }
        try {
            board.addPlayer(new Player("foo", Character.JACOB_GREEN));
            fail();
        } catch (Exception e) {
        }
        try {
            board.addPlayer(new Player("foo", Character.KASANDRA_SCARLETT));
            fail();
        } catch (Exception e) {
        }
        try {
            board.addPlayer(new Player("foo", Character.VICTOR_PLUM));
            fail();
        } catch (Exception e) {
        }
    }

    /**
     * Tests a few illegal board configurations.
     */
    @SuppressWarnings("unused")
    @Test
    public void illegalBoard() {
        try {
            new Board(null);
            fail();
        } catch (Exception e) {
        }
        try {
            new Board(new Tile[0][0]);
            fail();
        } catch (Exception e) {
        }
        try {
            new Board(new Tile[5][5]);
            fail();
        } catch (Exception e) {
        }
    }
}
