package com.hjwylde.uni.swen222.assignment02.cluedo.ui;

import java.util.List;
import java.util.Set;

import com.hjwylde.uni.swen222.assignment02.cluedo.game.Player;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.Card;
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
 * An interface for the Cluedo game engine to interact with the user interface through. This
 * interface provides two basic methods, one in which the game engine may send something and expect
 * a reply and one in which the game engine just wishes to send an event.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 31/07/2013
 */
public interface UI {
    
    /**
     * Informs that the game is starting.
     */
    public void informGameStart();
    
    /**
     * Informs that no player was able to refute the given player's rumour.
     * 
     * @param player the player that started the un-refuted rumour.
     */
    public void informNoPlayerRefuteRumour(Player player);
    
    /**
     * Informs that the given player has now been removed from the game due to having picked up the
     * 8th clock card.
     * 
     * @param player the player removed from the game.
     */
    public void informPlayerClockCardLoss(Player player);
    
    /**
     * Informs the given player that they have got <code>numberMoves</code> from their dice roll.
     * 
     * @param player the player.
     * @param numberMoves the number of moves on the dice roll.
     */
    public void informPlayerDiceRoll(Player player, int numberMoves);
    
    /**
     * Informs the player that they attempted to move to an illegal position.
     * 
     * @param player the player attempting to move.
     */
    public void informPlayerIllegalMove(Player player);
    
    /**
     * Informs that the given player's keeper cards have changed. This method is useful for the GUI
     * when it needs to refresh the display.
     * 
     * @param player the player whose keeper cards have changed.
     */
    public void informPlayerKeeperCardsChanged(Player player);
    
    /**
     * Informs the player that they are allowed to move anywhere this turn.
     * 
     * @param player the player.
     */
    public void informPlayerMoveAnywhere(Player player);
    
    /**
     * Informs that the given player has moved the given character to their start position.
     * 
     * @param player the player that moved the character.
     * @param character the character moved to the start.
     */
    public void informPlayerMovedToStart(Player player, Character character);
    
    /**
     * Informs that the given player has the given number of moves left to make.
     * 
     * @param player the player.
     * @param numberMoves the number of moves left to make.
     */
    public void informPlayerNumberMovesLeft(Player player, int numberMoves);
    
    /**
     * Informs that the given player has picked up a clock card and there are <code>remaining</code>
     * left.
     * 
     * @param player the player that picked up the clock card.
     * @param remaining the number of remaining clock cards.
     */
    public void informPlayerPickUpClockCard(Player player, int remaining);
    
    /**
     * Informs that the given player has picked up the given keeper card.
     * 
     * @param player the player that picked up the card.
     * @param card the keeper card.
     */
    public void informPlayerPickUpKeeperCard(Player player, KeeperCard card);
    
    /**
     * Informs that the player has been shown the given card.
     * 
     * @param player the player being shown the card.
     * @param card the card being shown.
     */
    public void informPlayerShowCard(Player player, Card card);
    
    /**
     * Informs that the given player's turn has just ended.
     * 
     * @param player the player whose turn has ended.
     */
    public void informPlayerTurnEnd(Player player);
    
    /**
     * Informs that the given player's turn has just started.
     * 
     * @param player the player whose turn has started.
     */
    public void informPlayerTurnStart(Player player);
    
    /**
     * Informs that the given player is unable to refute the rumour.
     * 
     * @param player the player unable to refute the rumour.
     */
    public void informPlayerUnableRefuteRumour(Player player);
    
    /**
     * Informs that the player would like to view their own cards.
     * 
     * @param player the player.
     */
    public void informPlayerViewCards(Player player);
    
    /**
     * Informs that the player would like to view their own keeper cards.
     * 
     * @param player the player.
     */
    public void informPlayerViewKeeperCards(Player player);
    
    /**
     * Informs that the player would like to view their own notebook.
     * 
     * @param player the player.
     */
    public void informPlayerViewNotebook(Player player);
    
    /**
     * Informs that the given player has won with the given accusation.
     * 
     * @param player the player.
     * @param accusation the correct accusation.
     */
    public void informPlayerWin(Player player,
        Triple<Character, Weapon, Room> accusation);
    
    /**
     * Informs that the given player has lost and is now removed from the game due to an incorrect
     * accusation.
     * 
     * @param player the removed player.
     */
    public void informPlayerWrongAccusationLoss(Player player);
    
    /**
     * Queries for the number of players this game will be for. It is expected that a number between
     * 2 and 6 inclusive is returned. A list of these integer options is available for use via the
     * parameter.
     * 
     * @param options the list of available player options (2 - 6).
     * @return a valid number of players.
     */
    public int queryNumberPlayers(List<Integer> options);
    
    /**
     * Queries the player for the character they would like to play as. A list of available
     * characters is provided.
     * 
     * @param player the player to select the character.
     * @param options the list of available characters.
     * @return the player's character from the list of available characters.
     */
    public Character queryPlayerCharacter(int player, List<Character> options);
    
    /**
     * Queries the given player to select a card to refute the rumour with. A list of available
     * cards is provided.
     * 
     * @param player the player to refute the rumour.
     * @param options the list of available cards to refute with.
     * @return the card chosen to refute the rumour.
     */
    public Card queryPlayerChooseRefuteRumourCard(Player player,
        Set<Card> options);
    
    /**
     * Queries the given player to make an accusation.
     * 
     * @param player the player to make the accusation.
     * @return the accusation.
     */
    public Triple<Character, Weapon, Room> queryPlayerMakeAccusation(
        Player player);
    
    /**
     * Queries the given player to make a move. The player has <code>maxMoves</code> available to
     * them.
     * 
     * @param player the player to move.
     * @param maxMoves the maximum number of spots they can move.
     * @return a co-ordinate of the place the player wants to move to.
     */
    public Coordinate queryPlayerMove(Player player, int maxMoves);
    
    /**
     * Queries the given player for a co-ordinate on where to move. This query does not have a move
     * limit.
     * 
     * @param player the player to query.
     * @return the co-ordinate of the place the player wants to move to.
     */
    public Coordinate queryPlayerMoveAnywhere(Player player);
    
    /**
     * Queries the given player about which character they want to send to the start. A list of
     * possible characters is provided.
     * 
     * @param player the player to pick a character.
     * @param options the list of available characters.
     * @return the character to send back to the start.
     */
    public Character queryPlayerMovePlayerToStart(Player player,
        List<Character> options);
    
    /**
     * Queries for the player's custom name. It is expected that a non-empty string is returned.
     * 
     * @param player the player.
     * @return the player's name.
     */
    public String queryPlayerName(int player);
    
    /**
     * Queries the given player about whether they want to play the given keeper card.
     * 
     * @param player the player to ask.
     * @param card the keeper card to play.
     * @return a choice of whether the player wants to play the keeper card.
     */
    public Option queryPlayerPlayKeeperCard(Player player, KeeperCard card);
    
    /**
     * Queries the given from player to show a card to the given to player. A list of possible card
     * options is provided.
     * 
     * @param playerFrom the from player to show the card.
     * @param playerTo the to player to see the card.
     * @param options the list of card options.
     * @return the chosen card to show.
     */
    public Card queryPlayerShowCard(Player playerFrom, Player playerTo,
        Set<Card> options);
    
    /**
     * Queries the given player about what cards they would like to rumour.
     * 
     * @param player the player who's starting the rumour.
     * @return the rumour.
     */
    public Pair<Character, Weapon> queryPlayerStartRumour(Player player);
    
    /**
     * Queries the given player for a turn choice. A list of turn choice options is provided.
     * 
     * @param player the player whose turn it is.
     * @param options the possible turn options.
     * @return the chosen turn choice.
     */
    public PlayerTurnChoice queryPlayerTurnChoice(Player player,
        Set<PlayerTurnChoice> options);
}
