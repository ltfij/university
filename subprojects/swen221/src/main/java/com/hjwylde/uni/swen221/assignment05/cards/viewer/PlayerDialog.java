package com.hjwylde.uni.swen221.assignment05.cards.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.Border;

import com.hjwylde.uni.swen221.assignment05.cards.game.Player;

/*
 * Code for Assignment 5, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class PlayerDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    public PlayerDialog(JFrame parent, final Map<Player, ComputerPlayer> players) {
        super(parent, true);
        final JCheckBox[] checkBoxes = new JCheckBox[4];

        for (final Player p : Player.values()) {
            checkBoxes[p.ordinal()] = new JCheckBox("", players.containsKey(p));
            checkBoxes[p.ordinal()].setAction(new AbstractAction(p + " is a Computer Player.") {

                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (players.containsKey(p))
                        players.remove(p);
                    else
                        players.put(p, new ComputerPlayer(p));
                }
            });
        }

        JPanel middlePanel = new JPanel();
        Border cb = BorderFactory.createEmptyBorder(15, 15, 15, 15);
        cb = BorderFactory.createCompoundBorder(cb, BorderFactory.createLineBorder(Color.GRAY, 1));
        cb =
                BorderFactory.createCompoundBorder(cb,
                        BorderFactory.createEmptyBorder(15, 15, 15, 15));
        middlePanel.setBorder(cb);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.add(new JLabel("Choose the Computer Players:"));
        middlePanel.add(new JLabel(" "));
        for (JCheckBox box : checkBoxes)
            middlePanel.add(box);
        JPanel topPanel = new JPanel();
        Border tb = BorderFactory.createEmptyBorder(15, 15, 15, 15);
        tb = BorderFactory.createCompoundBorder(tb, BorderFactory.createLineBorder(Color.GRAY, 1));
        tb =
                BorderFactory.createCompoundBorder(tb,
                        BorderFactory.createEmptyBorder(15, 15, 15, 15));
        topPanel.setBorder(tb);
        topPanel.add(new JLabel("Welcome to the Card Game!"));

        JPanel bottomPanel = new JPanel();
        JButton button = new JButton();
        button.setAction(new AbstractAction("Let's Play") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        bottomPanel.add(button);
        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        pack();

        // Center window in screen
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        setBounds((scrnsize.width - getWidth()) / 2, (scrnsize.height - getHeight()) / 2,
                getWidth(), getHeight());

        // Display window!
        setVisible(true);
    }

}
