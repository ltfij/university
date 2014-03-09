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

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
 * Code for Assignment 8, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A Character is a record of information about a particular character in the game. There are
 * essentially two kinds of characters: player controlled and computer controlled.
 * 
 * @author djp
 */
public abstract class Character {

    protected int realX; // real x-position
    protected int realY; // real y-position

    // Character type constants
    public static final int ENTERING = 0;

    public static final int LEAVING = 1;

    public static final int HOMERGHOST = 2;

    public static final int RANDOMGHOST = 3;

    public static final int PACMAN = 4;

    public static final int DISAPPEAR = 5;

    public Character(int realX, int realY) {
        this.realX = realX;
        this.realY = realY;
    }

    /**
     * This method enables characters to draw themselves onto a given canvas.
     */
    public abstract void draw(Graphics g);

    public int realX() {
        return realX;
    }

    public int realY() {
        return realY;
    }

    /**
     * The following method is provided to allow characters to take actions on every clock tick; for
     * example, ghosts may choose new directions to move in.
     */
    public abstract void tick(Board game);

    /**
     * The following method is provided to simplify the process of writing a given character to the
     * output stream.
     */
    public abstract void toOutputStream(DataOutputStream dout) throws IOException;

    /**
     * The following constructs a character given a byte array.
     */
    public static Character fromInputStream(DataInputStream din) throws IOException {
        int type = din.readByte();
        int rx = din.readShort();
        int ry = din.readShort();

        if (type == Character.PACMAN)
            return Pacman.fromInputStream(rx, ry, din);
        else if (type == Character.HOMERGHOST)
            return HomerGhost.fromInputStream(rx, ry, din);
        else if (type == Character.RANDOMGHOST)
            return RandomGhost.fromInputStream(rx, ry, din);
        else if (type == Character.DISAPPEAR)
            return Disappear.fromInputStream(rx, ry, din);
        else
            throw new IllegalArgumentException("Unrecognised character type: " + type);
    }

}
