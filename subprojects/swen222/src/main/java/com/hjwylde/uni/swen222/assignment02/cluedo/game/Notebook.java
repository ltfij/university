package com.hjwylde.uni.swen222.assignment02.cluedo.game;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ImmutableSet;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.Card;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.CharacterCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.RoomCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.WeaponCard;

/**
 * Represents a player's notebook. The notebook contains a list of cards the player knows is
 * available as the murderer / murder weapon / murder room.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 2/08/2013
 */
public class Notebook {

    private Map<Card, Boolean> book = new LinkedHashMap<>();

    /**
     * Creates a new <code>Notebook</code>. The notebook is initialised with all possible card
     * values being available.
     */
    public Notebook() {
        // Initialise all the cards to "not seen"
        for (CharacterCard card : CharacterCard.values())
            book.put(card, false);
        for (WeaponCard card : WeaponCard.values())
            book.put(card, false);
        for (RoomCard card : RoomCard.values())
            book.put(card, false);
    }

    /**
     * Gets an immutable set of all the cards still available in this notebook. If a card is
     * available, it means the player still considers it a potential suspect for a murder card.
     * 
     * @return an immutable set of all available cards in this notebook.
     */
    public ImmutableSet<Card> availableCards() {
        ImmutableSet.Builder<Card> builder = ImmutableSet.builder();
        for (Map.Entry<Card, Boolean> e : book.entrySet())
            if (!e.getValue())
                builder.add(e.getKey());

        return builder.build();
    }

    /**
     * Crosses off the given card from the notebook, marking it as being unavailable as the murderer
     * / murder weapon / murder room.
     * 
     * @param card the card to cross off.
     */
    public void cross(Card card) {
        book.put(card, true);
    }

    /**
     * Gets an immutable view of this notebook.
     * 
     * @return an immutable view.
     */
    public Notebook getView() {
        return new View(this);
    }

    /**
     * A "view" into a notebook. This class merely provides a shell to put around the notebook so as
     * to ensure encapsulation when passing around a notebook instance.
     * 
     * @author Henry J. Wylde
     * @author Moss Cantwell
     * 
     * @since 5/08/2013
     */
    private static final class View extends Notebook {

        private final Notebook notebook;

        /**
         * Creates a new <code>View</code> for the given notebook.
         * 
         * @param notebook the notebook.
         */
        public View(Notebook notebook) {
            this.notebook = Objects.requireNonNull(notebook);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ImmutableSet<Card> availableCards() {
            return notebook.availableCards();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void cross(Card card) {
            throw new UnsupportedOperationException("This is an immutable view of the notebook");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Notebook getView() {
            return this;
        }
    }
}
