package com.hjwylde.uni.swen222.assignment02.cluedo.logic;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Board;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.Player;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.Card;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.intrigue.*;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.intrigue.KeeperCard.*;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Character;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.UI;
import com.hjwylde.uni.swen222.assignment02.cluedo.ui.choices.Option;

/**
 * Helper class for providing game logic related to playing a keeper card. This class helps to keep
 * the <code>GameEngine</code> class clean and tidy.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 11/08/2013
 */
public final class KeeperCards {

    private final UI ui;
    private final Board board;

    /**
     * Creates a new <code>KeeperCards</code> with the given UI and board.
     * 
     * @param ui the UI.
     * @param board the board.
     */
    public KeeperCards(UI ui, Board board) {
        this.board = Objects.requireNonNull(board);
        this.ui = Objects.requireNonNull(ui);
    }

    /**
     * Finds all players with at least one of the given type of keeper card. The user may provide a
     * collection of player indices that will be ignored in this list.
     * 
     * @param <T> the type of keeper card for the players to have.
     * @param clazz the class of keeper card.
     * @param ignore the list of players to ignore.
     * @return a list of players who have the keeper card.
     */
    public <T extends KeeperCard> ImmutableList<Integer> findPlayersWithKeeperCard(Class<T> clazz,
            Collection<Integer> ignore) {
        ImmutableList.Builder<Integer> players = ImmutableList.builder();

        for (int i = 0; i < board.getNumberPlayers(); i++) {
            if (board.isPlayerDead(i) || ignore.contains(i))
                continue;

            if (playerContainsKeeperCard(board.getPlayer(i), clazz))
                players.add(i);
        }

        return players.build();
    }

    /**
     * Finds all players with at least one of the given type of keeper card. The user may provide a
     * collection of player indices that will be ignored in this list.
     * 
     * @param <T> the type of keeper card for the players to have.
     * @param clazz the class of keeper card.
     * @param ignore the list of players to ignore.
     * @return a list of players who have the keeper card.
     */
    public <T extends KeeperCard> ImmutableList<Integer> findPlayersWithKeeperCard(Class<T> clazz,
            Integer... ignore) {
        return findPlayersWithKeeperCard(clazz, Arrays.asList(ignore));
    }

    /**
     * Queries the UI as to whether the given player wants to play a keeper card and move anywhere
     * on their turn. This method will remove the keeper card from the player's keeper cards.
     * 
     * @param player the player to query.
     * @return true if the player played their keeper card
     */
    public boolean queryPlayerMoveAnywhere(int player) {
        if (!queryPlayerPlayKeeperCard(board.getPlayer(player), MoveAnywhere.class))
            return false;

        ui.informPlayerMoveAnywhere(board.getPlayer(player));

        board.getPlayer(player).removeKeeperCard(MoveAnywhere.class);

        return true;
    }

    /**
     * Queries the UI as to whether the given player wants to play a keeper card and add 6 to their
     * dice roll. This method will remove the keeper card from the player's keeper cards.
     * 
     * @param player the player to query.
     * @return true if the player played their keeper card
     */
    public boolean queryPlayerPlayDiceAdd(int player) {
        if (!queryPlayerPlayKeeperCard(board.getPlayer(player), DiceAdd.class))
            return false;

        board.getPlayer(player).removeKeeperCard(DiceAdd.class);
        return true;
    }

    /**
     * Queries whether the player wants to play a keeper card of the given class.
     * 
     * @param <T> the type of the keeper card.
     * @param player the player to query.
     * @param clazz the class of the keeper card.
     * @return true if the player wants to play their keeper card.
     */
    public <T extends KeeperCard> boolean queryPlayerPlayKeeperCard(Player player, Class<T> clazz) {
        Optional<T> card = getPlayerKeeperCard(player, clazz);

        return card.isPresent() && (ui.queryPlayerPlayKeeperCard(player, card.get()) == Option.YES);
    }

    /**
     * Queries whether the player wants to play their keeper card to move another player to their
     * start position. This method will perform all necessary operations to the game board if the
     * player wants to play their keeper card.
     * 
     * @param player the player to query.
     * @return true if the player played their keeper card.
     */
    public boolean queryPlayerPlayMovePlayerToStart(int player) {
        if (!queryPlayerPlayKeeperCard(board.getPlayer(player), MovePlayerToStart.class))
            return false;

        // Get a list of possible players to move to the start
        List<Character> options = new ArrayList<>();
        for (int i = 0; i < board.getNumberPlayers(); i++) {
            if (board.isPlayerDead(i) || (i == player))
                continue;

            options.add(board.getPlayer(i).getCharacter());
        }

        Character character = ui.queryPlayerMovePlayerToStart(board.getPlayer(player), options);

        board.movePlayer(board.findPlayer(character).get(),
                board.getCharacterInitialPosition(character).get());
        ui.informPlayerMovedToStart(board.getPlayer(player), character);

        board.getPlayer(player).removeKeeperCard(MovePlayerToStart.class);

        return true;
    }

    /**
     * Queries whether the player wants to play their keeper card and refuse to answer a rumour.
     * This method will remove the keeper card from the player's keeper cards.
     * 
     * @param player the player to query.
     * @return true if the player played their keeper card.
     */
    public boolean queryPlayerPlayRefuseAnswerRumour(int player) {
        if (!queryPlayerPlayKeeperCard(board.getPlayer(player), RefuseAnswerRumour.class))
            return false;

        board.getPlayer(player).removeKeeperCard(RefuseAnswerRumour.class);

        return true;
    }

    /**
     * Queries whether the player wants to play their keeper card and take a second turn. This
     * method will remove the keeper card from the player's keeper cards.
     * 
     * @param player the player to query.
     * @return true if the player played their keeper card.
     */
    public boolean queryPlayerPlayTakeSecondTurn(int player) {
        if (!queryPlayerPlayKeeperCard(board.getPlayer(player), TakeSecondTurn.class))
            return false;

        board.getPlayer(player).removeKeeperCard(TakeSecondTurn.class);

        return true;
    }

    /**
     * Queries whether the player wants to play their keeper card and view another player's card.
     * This method will take care of all necessary operations to view the other player's card.
     * 
     * @param player the player to query.
     * @return true if the player played their keeper card.
     */
    public boolean queryPlayerPlayViewPlayerCard(int player) {
        if (!queryPlayerPlayKeeperCard(board.getPlayer(player), ViewPlayerCard.class))
            return false;

        int rightPlayer = player;
        do {
            rightPlayer--;

            if (rightPlayer == -1)
                rightPlayer = board.getNumberPlayers() - 1;
            if (rightPlayer == player) // Uh oh, they're the only player left alive!
                return false;
        } while (board.isPlayerDead(rightPlayer));

        Card card =
                ui.queryPlayerShowCard(board.getPlayer(rightPlayer), board.getPlayer(player), board
                        .getPlayer(rightPlayer).getCards());

        board.getPlayer(player).showCard(card); // Cross it off the player's notebook
        ui.informPlayerShowCard(board.getPlayer(player), card); // Let the UI display something

        board.getPlayer(player).removeKeeperCard(ViewPlayerCard.class);

        return true;
    }

    /**
     * Queries whether the given player wants to play their keeper card and view a rumour refutation
     * card. This method takes care of all necessary operations to let the player view the
     * refutation card.
     * 
     * @param player the player to query.
     * @param card the refutation card.
     * @return true if the player played their keeper card.
     */
    public boolean queryPlayerPlayViewRumourRefuteCard(int player, Card card) {
        if (!queryPlayerPlayKeeperCard(board.getPlayer(player), ViewRumourRefuteCard.class))
            return false;

        board.getPlayer(player).showCard(card);
        ui.informPlayerShowCard(board.getPlayer(player), card);

        board.getPlayer(player).removeKeeperCard(ViewRumourRefuteCard.class);

        return true;
    }

    /**
     * Queries whether the given player wants to play their keeper card and start a second rumour on
     * their turn. This method will remove the keeper card from the player's keeper cards.
     * 
     * @param player the player to query.
     * @return true if the player played their keeper card
     */
    public boolean queryPlayerStartSecondRumour(int player) {
        if (!queryPlayerPlayKeeperCard(board.getPlayer(player), StartSecondRumour.class))
            return false;

        board.getPlayer(player).removeKeeperCard(StartSecondRumour.class);

        return true;
    }

    /**
     * Attempts to get a keeper card of the given class from the given player's hand.
     * 
     * @param <T> the type of the keeper card.
     * @param player the player whose hand to check.
     * @param clazz the class of the keeper card.
     * @return the keeper card if it exists.
     */
    public static <T extends KeeperCard> Optional<T> getPlayerKeeperCard(Player player,
            Class<T> clazz) {
        Iterator<T> it = Iterables.filter(player.getKeeperCards(), clazz).iterator();

        if (!it.hasNext())
            return Optional.absent();

        return Optional.of(it.next());
    }

    /**
     * Checks whether the given player has a keeper card of the given type.
     * 
     * @param <T> the type of the keeper card.
     * @param player the player to check.
     * @param clazz the class of the keeper card.
     * @return true if the player has a keeper card of the given type.
     */
    public static <T extends KeeperCard> boolean playerContainsKeeperCard(Player player,
            Class<T> clazz) {
        return getPlayerKeeperCard(player, clazz).isPresent();
    }
}
