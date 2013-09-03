package com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Character;

/**
 * Represents a character card. A character card takes a single character from the enumeration of
 * possible characters.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 25/07/2013
 */
public final class CharacterCard implements Card {
    
    private final Character character;
    
    /**
     * Creates a new <code>CharacterCard</code> with the given character.
     * 
     * @param character the character.
     */
    public CharacterCard(Character character) {
        this.character = Objects.requireNonNull(character);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CharacterCard))
            return false;
        
        return character == ((CharacterCard) obj).character;
    }
    
    /**
     * Gets the character of this card.
     * 
     * @return the character.
     */
    public Character getCharacter() {
        return character;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return character.hashCode();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return character.toString();
    }
    
    /**
     * Gets an array of all possible values for this class. The array is generated using the enum of
     * characters.
     * 
     * @return an array of all possible character cards.
     */
    public static CharacterCard[] values() {
        List<CharacterCard> builder = new ArrayList<>();
        for (Character character : Character.values())
            builder.add(new CharacterCard(character));
        
        return builder.toArray(new CharacterCard[0]);
    }
}