package com.hjwylde.uni.swen222.assignment02.cluedo.ui.choices;

/**
 * An enumeration of the different choices a player may make in their turn.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 9/08/2013
 */
public enum PlayerTurnChoice {

    /**
     * Move player.
     */
    MOVE {

        @Override
        public String toString() {
            return "Move";
        }
    },
    /**
     * Start a rumour.
     */
    START_RUMOUR {

        @Override
        public String toString() {
            return "Start a rumour";
        }
    },
    /**
     * Make an accusation.
     */
    MAKE_ACCUSATION {

        @Override
        public String toString() {
            return "Make an accusation";
        }
    },
    /**
     * View cards.
     */
    VIEW_CARDS {

        @Override
        public String toString() {
            return "View cards";
        }
    },
    /**
     * View keeper cards.
     */
    VIEW_KEEPER_CARDS {

        @Override
        public String toString() {
            return "View keeper cards";
        }
    },
    /**
     * View notebook.
     */
    VIEW_NOTEBOOK {

        @Override
        public String toString() {
            return "View notebook";
        }
    },
    /**
     * End the player's turn.
     */
    END {

        @Override
        public String toString() {
            return "End your turn";
        }
    };
}
