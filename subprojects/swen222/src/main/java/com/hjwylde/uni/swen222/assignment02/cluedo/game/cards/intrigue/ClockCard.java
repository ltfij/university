package com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.intrigue;

/**
 * A clock card. There are 8 clock cards in the game, when the eighth clock card is drawn, that
 * player is removed from the game. The eighth clock card is then placed back into the game for a
 * future player to pick it up.
 * 
 * For simplicity, a clock card is defined as equal to another if and only if it has the same memory
 * reference. This lets us create any number of cards that may be used without any need to order
 * them.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 25/07/2013
 */
public final class ClockCard implements IntrigueCard {

    private static ClockCard[] VALUES = new ClockCard[] {new ClockCard(), new ClockCard(),
            new ClockCard(), new ClockCard(), new ClockCard(), new ClockCard(), new ClockCard(),
            new ClockCard()};

    /**
     * This class may only be instantiated locally.
     */
    private ClockCard() {}

    /**
     * Gets an array of eight different clock cards.
     * 
     * @return an array of eight clock cards.
     */
    public static ClockCard[] values() {
        return VALUES;
    }
}
