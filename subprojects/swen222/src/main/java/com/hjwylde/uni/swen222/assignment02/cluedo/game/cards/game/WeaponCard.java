package com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Weapon;

/**
 * Represents a weapon card. A weapon card takes a single weapon from the enumeration of possible
 * weapons.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 25/07/2013
 */
public final class WeaponCard implements Card {
    
    private final Weapon weapon;
    
    /**
     * Creates a new <code>WeaponCard</code> with the given weapon.
     * 
     * @param weapon the weapon.
     */
    public WeaponCard(Weapon weapon) {
        this.weapon = Objects.requireNonNull(weapon);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WeaponCard))
            return false;
        
        return weapon == ((WeaponCard) obj).weapon;
    }
    
    /**
     * Gets the weapon of this card.
     * 
     * @return the weapon.
     */
    public Weapon getWeapon() {
        return weapon;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return weapon.hashCode();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return weapon.toString();
    }
    
    /**
     * Gets an array of all possible values for this class. The array is generated using the enum of
     * weapons.
     * 
     * @return an array of all possible weapon cards.
     */
    public static WeaponCard[] values() {
        List<WeaponCard> builder = new ArrayList<>();
        for (Weapon weapon : Weapon.values())
            builder.add(new WeaponCard(weapon));
        
        return builder.toArray(new WeaponCard[0]);
    }
}