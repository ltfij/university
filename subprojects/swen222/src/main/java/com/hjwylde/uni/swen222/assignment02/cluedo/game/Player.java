package com.hjwylde.uni.swen222.assignment02.cluedo.game;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.Card;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.intrigue.KeeperCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Character;

/**
 * A player in the game. A player has a character token and a list of cards along with a notebook.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 25/07/2013
 */
public final class Player {

    private final String name;
    private final Character character;

    private final Set<Card> cards = new HashSet<>();
    private final List<KeeperCard> keeperCards = new ArrayList<>();
    private final Notebook notebook = new Notebook();

    /**
     * Creates a new <code>Player</code> with the given name and character.
     * 
     * @param name the player's name
     * @param character the player's character.
     */
    public Player(String name, Character character) {
        this.name = checkNotNull(name, "Name cannot be null!");
        this.character = Objects.requireNonNull(character);
    }

    /**
     * Adds a card to the player's set and crosses it off the notebook.
     * 
     * @param card the card to add.
     */
    public void addCard(Card card) {
        cards.add(Objects.requireNonNull(card));
        notebook.cross(card); // The player knows his own cards!
    }

    /**
     * Adds the given keeper card to this player's hand.
     * 
     * @param card the keeper card.
     */
    public void addKeeperCard(KeeperCard card) {
        keeperCards.add(card);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player))
            return false;

        return character == ((Player) obj).character;
    }

    /**
     * Gets an immutable set of the player's cards.
     * 
     * @return an immutable set of the player's cards.
     */
    public ImmutableSet<Card> getCards() {
        return ImmutableSet.copyOf(cards);
    }

    /**
     * Gets the player's character.
     * 
     * @return the character.
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * Gets this player's keeper cards.
     * 
     * @return this player's keeper cards.
     */
    public ImmutableList<KeeperCard> getKeeperCards() {
        return ImmutableList.copyOf(keeperCards);
    }

    /**
     * Gets this player's name.
     * 
     * @return the player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets an immutable view of the player's notebook.
     * 
     * @return an immutable view of the player's notebook.
     */
    public Notebook getNotebook() {
        return notebook.getView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return character.hashCode();
    }

    /**
     * Removes a keeper card of the given class from this player's hand.
     * 
     * @param <T> the type of the keeper card.
     * @param clazz the class of the keeper card.
     */
    public <T extends KeeperCard> void removeKeeperCard(Class<T> clazz) {
        Iterator<KeeperCard> it = keeperCards.iterator();

        while (it.hasNext())
            if (clazz.isInstance(it.next())) {
                it.remove();
                return;
            }
    }

    /**
     * Shows the player the given card, essentially crossing it off in their notebook.
     * 
     * @param card the card to cross off.
     */
    public void showCard(Card card) {
        notebook.cross(card);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name;
    }
}
