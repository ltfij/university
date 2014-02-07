// This file is part of the Multi-player Pacman Game.
//
// Pacman is free software; you can redistribute it and/or modify it under the terms of the GNU
// General Public License as published by the Free Software Foundation; either version 3 of the
// License, or (at your option) any later version.
//
// Pacman is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
// the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
// Public License for more details.
//
// You should have received a copy of the GNU General Public License along with Pacman. If not, see
// <http://www.gnu.org/licenses/>
//
// Copyright 2010, David James Pearce.

package com.hjwylde.uni.swen221.assignment08.pacman.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/*
 * Code for Assignment 8, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class BoardFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final BoardCanvas canvas;

    public BoardFrame(String title, Board game, int uid, KeyListener... keys) {
        super(title);

        canvas = new BoardCanvas(uid, game);
        setLayout(new BorderLayout());
        for (KeyListener k : keys)
            canvas.addKeyListener(k);
        add(canvas, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center window in screen
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        setBounds((scrnsize.width - getWidth()) / 2, (scrnsize.height - getHeight()) / 2,
                getWidth(), getHeight());
        pack();
        setResizable(false);

        // Display window
        setVisible(true);
        canvas.requestFocus();
    }

    @Override
    public void repaint() {
        canvas.repaint();
    }
}
