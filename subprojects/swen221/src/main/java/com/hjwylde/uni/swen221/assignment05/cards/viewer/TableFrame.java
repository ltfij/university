package com.hjwylde.uni.swen221.assignment05.cards.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.hjwylde.uni.swen221.assignment05.cards.game.Card;
import com.hjwylde.uni.swen221.assignment05.cards.game.CardGame;
import com.hjwylde.uni.swen221.assignment05.cards.game.IllegalMoveException;
import com.hjwylde.uni.swen221.assignment05.cards.game.Player;

/*
 * Code for Assignment 5, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A Table Frame constructs the window that is the "card table".
 * 
 * @author djp
 */
public final class TableFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final TableCanvas canvas;
    private final Map<Player, ComputerPlayer> computerPlayers = new HashMap<>();
    private CardGame game;
    private final JLabel statusBar;

    /**
     * Construct a table frame from a given game. The list of players determines who is, and who is
     * not a computer player. That is, an entry which is null indicates a human player in that
     * position.
     */
    public TableFrame(CardGame game) {
        super("Card Game");
        this.game = game;
        // Initially set all computer players, except south.
        for (Player p : Player.values())
            if (!p.equals(Player.SOUTH))
                computerPlayers.put(p, new ComputerPlayer(p));
        canvas = new TableCanvas(game, this);
        statusBar = new JLabel();
        add(canvas, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        setMinimumSize(new Dimension(600, 610));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        // Center window in screen
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        setBounds((scrnsize.width - getWidth()) / 2, (scrnsize.height - getHeight()) / 2,
                getWidth(), getHeight());
        // Display window
        setVisible(true);
        new PlayerDialog(this, computerPlayers);
        // Finally, start the game off ...
        startEvent();
    }

    public boolean isComputerPlayer(Player player) {
        return computerPlayers.containsKey(player);
    }

    public void playedEvent(Player player, Card card) throws IllegalMoveException {
        game.getTrick().play(player, card);
        statusBar.setText(game + " (" + player + " played " + card + ")");
        canvas.repaint();
        Player nextPlayer = game.getTrick().nextPlayer();
        if (nextPlayer == null)
            // Round has finished
            requestTimerEvent(2);
        else if (isComputerPlayer(nextPlayer))
            // Computer player next to play
            requestTimerEvent(1);
    }

    public void requestTimerEvent(final int delay) {
        Thread timer = new Thread() {

            @Override
            public void run() {
                try {
                    Thread.sleep(delay * 1000);
                } catch (InterruptedException e) {
                    // ignore
                }
                timerEvent();
            }
        };
        timer.start();
    }

    public void startEvent() {
        statusBar.setText(game + " " + game.getTrick().nextPlayer() + " to start");
        if (isComputerPlayer(game.getTrick().nextPlayer()))
            // Computer player to start
            requestTimerEvent(1);
    }

    public void statusEvent(String msg) {
        statusBar.setText(game + " (" + msg + ")");
    }

    public void timerEvent() {
        if (game.getTrick().nextPlayer() == null) {
            game.nextRound();
            if (game.isFinished()) {
                int r =
                        JOptionPane.showConfirmDialog(this, new JLabel(game.getWinner()
                                + " is the winner!!   Play Again?"), "Yes",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (r == JOptionPane.YES_OPTION) {
                    game = new CardGame();
                    statusBar.setText(game.getTrick().nextPlayer() + " to start.");
                    canvas.newGame(game);
                    canvas.repaint();
                } else
                    System.exit(0);
            } else {
                statusBar.setText(game + " (" + game.getTrick().nextPlayer() + " to play)");
                canvas.repaint();
            }
            startEvent();
        } else {
            // this indicates we're waiting for a computer player to play.
            Player nextPlayer = game.getTrick().nextPlayer();
            try {
                ComputerPlayer computerPlayer = computerPlayers.get(nextPlayer);
                if (computerPlayer != null)
                    playedEvent(nextPlayer, computerPlayer.nextCard(game.getTrick()));
            } catch (IllegalMoveException e) {
                throw new RuntimeException("Computer player is cheating!", e);
            }
        }
    }

    public static void main(String[] args) {
        CardGame game = new CardGame();
        new TableFrame(game);
    }
}
