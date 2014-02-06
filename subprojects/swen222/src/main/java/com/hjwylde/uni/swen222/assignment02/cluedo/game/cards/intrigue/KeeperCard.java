package com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.intrigue;

import java.util.Objects;

/**
 * Represents a keeper card. A keeper card supports extra behavior in the game at various times.
 * Read individual keeper cards for more information.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 25/07/2013
 */
abstract public class KeeperCard implements IntrigueCard {
    
    private static final KeeperCard[] VALUES = new KeeperCard[] {
        new DiceAdd(), new DiceAdd(), new MoveAnywhere(), new MoveAnywhere(),
        new MovePlayerToStart(), new MovePlayerToStart(),
        new RefuseAnswerRumour(), new RefuseAnswerRumour(),
        new StartSecondRumour(), new TakeSecondTurn(), new ViewPlayerCard(),
        new ViewRumourRefuteCard()
    };
    
    /**
     * The name of this keeper card.
     */
    protected final String name;
    /**
     * The description of this keeper card. The description should describe what the card does and
     * when it may be played.
     */
    protected final String description;
    /**
     * The short description of this keeper card. The short description should describe what it
     * does.
     */
    protected final String shortDescription;
    
    private KeeperCard(String name, String description, String shortDescription) {
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.shortDescription = Objects.requireNonNull(shortDescription);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (getClass() == obj.getClass());
    }
    
    /**
     * Gets the description of this keeper card.
     * 
     * @return the description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the name of this keeper card.
     * 
     * @return the name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the short description of this keeper card.
     * 
     * @return the short description.
     */
    public String getShortDescription() {
        return shortDescription;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name;
    }
    
    /**
     * Gets an array of keeper cards that should be used for a game.
     * 
     * @return an array of keeper cards.
     */
    public static KeeperCard[] values() {
        return VALUES;
    }
    
    /**
     * Keeper card that adds 6 to a player's dice roll.
     * 
     * @author Henry J. Wylde
     * 
     * @since 11/08/2013
     */
    public static final class DiceAdd extends KeeperCard {
        
        private DiceAdd() {
            super(
                "Speedy",
                "Add 6 to a dice roll. This card may be played after you have rolled the die, but before you move.",
                "Add 6 to a dice roll.");
        }
    }
    
    /**
     * Keeper card that lets a player move anywhere on the board.
     * 
     * @author Henry J. Wylde
     * @author Moss Cantwell
     * 
     * @since 11/08/2013
     */
    public static final class MoveAnywhere extends KeeperCard {
        
        private MoveAnywhere() {
            super(
                "Coffee",
                "Move anywhere on your turn. This card may be played instead of rolling the die.",
                "Move anywhere on your turn.");
        }
    }
    
    /**
     * Keeper card that lets a player move another player back to their starting position.
     * 
     * @author Henry J. Wylde
     * @author Moss Cantwell
     * 
     * @since 11/08/2013
     */
    public static final class MovePlayerToStart extends KeeperCard {
        
        private MovePlayerToStart() {
            super(
                "Doosh",
                "Move any player to their starting position. This card may be played after you have ended your turn.",
                "Move any player to their starting position.");
        }
    }
    
    /**
     * Keeper card that allows a player to refuse to answer a rumour.
     * 
     * @author Henry J. Wylde
     * @author Moss Cantwell
     * 
     * @since 11/08/2013
     */
    public static final class RefuseAnswerRumour extends KeeperCard {
        
        private RefuseAnswerRumour() {
            super(
                "Liar",
                "Refuse to refute a rumour. This card may be played when a player asks you to refute a rumour they have started.",
                "Refuse to refute a rumour.");
        }
    }
    
    /**
     * Keeper card that lets a player start a rumour in the same room they ended their last turn in.
     * 
     * @author Henry J. Wylde
     * @author Moss Cantwell
     * 
     * @since 11/08/2013
     */
    public static final class StartSecondRumour extends KeeperCard {
        
        private StartSecondRumour() {
            super(
                "Gossiper",
                "Start a second rumour. This card may be played when you start your turn in a room. You may start a second rumour instead of rolling the die.",
                "Start a second rumour.");
        }
    }
    
    /**
     * Keeper card that lets a player take a second turn.
     * 
     * @author Henry J. Wylde
     * @author Moss Cantwell
     * 
     * @since 11/08/2013
     */
    public static final class TakeSecondTurn extends KeeperCard {
        
        private TakeSecondTurn() {
            super(
                "Again",
                "Take a second turn. This card may be played after you have ended your turn.",
                "Take a second turn.");
        }
    }
    
    /**
     * Keeper card that lets a player view a card from the player to their right's hand.
     * 
     * @author Henry J. Wylde
     * @author Moss Cantwell
     * 
     * @since 11/08/2013
     */
    public static final class ViewPlayerCard extends KeeperCard {
        
        private ViewPlayerCard() {
            super(
                "Cheater",
                "View a card from the player to the right. This card may be played after you have ended your turn.",
                "View a card from the player to the right.");
        }
    }
    
    /**
     * Keeper card that lets a player view a rumour refutation card.
     * 
     * @author Henry J. Wylde
     * @author Moss Cantwell
     * 
     * @since 11/08/2013
     */
    public static final class ViewRumourRefuteCard extends KeeperCard {
        
        private ViewRumourRefuteCard() {
            super(
                "Spy",
                "View the refutation card when a player refutes another player's rumour. This card may be played when another player refutes a rumour.",
                "View the refutation card.");
        }
    }
}