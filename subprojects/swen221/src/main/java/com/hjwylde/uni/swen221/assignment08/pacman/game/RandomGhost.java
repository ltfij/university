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
import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

/*
 * Code for Assignment 8, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Random Ghosts are controlled by the computer. They basically just walk in a pretty arbitrary
 * direction.
 * 
 * @author djp
 */
public final class RandomGhost extends MovingCharacter implements Ghost {
    
    protected static final Random random = new Random(
        System.currentTimeMillis());
    
    private static final Image RGHOST_RIGHT = BoardCanvas
        .loadImage("rghostright.png");
    
    private static final Image RGHOST_LEFT = BoardCanvas
        .loadImage("rghostleft.png");
    
    private static final Image RGHOST_UP = BoardCanvas
        .loadImage("rghostup.png");
    
    private static final Image RGHOST_DOWN = BoardCanvas
        .loadImage("rghostdown.png");
    
    public RandomGhost(int realX, int realY) {
        super(realX, realY, MovingCharacter.STOPPED);
    }
    
    @Override
    public void draw(Graphics g) {
        switch (direction) {
        case MovingCharacter.RIGHT:
            g.drawImage(RandomGhost.RGHOST_RIGHT, realX, realY, null, null);
            break;
        case MovingCharacter.UP:
            g.drawImage(RandomGhost.RGHOST_UP, realX, realY, null, null);
            break;
        case MovingCharacter.DOWN:
            g.drawImage(RandomGhost.RGHOST_DOWN, realX, realY, null, null);
            break;
        case MovingCharacter.LEFT:
            g.drawImage(RandomGhost.RGHOST_LEFT, realX, realY, null, null);
            break;
        }
    }
    
    @Override
    public int speed() {
        return 3;
    }
    
    @Override
    public void tick(Board game) {
        super.tick(game);
        
        // check whether we are at an intersection.
        if ((direction == MovingCharacter.DOWN)
            || (direction == MovingCharacter.UP)) {
            // ok, moving in up/down direction
            if (!game.canMoveLeft(this) && !game.canMoveRight(this))
                return; // no horizontal movement possible
        } else if ((direction == MovingCharacter.RIGHT)
            || (direction == MovingCharacter.LEFT))
            // ok, moving in left/right direction
            if (!game.canMoveUp(this) && !game.canMoveDown(this))
                return; // no horizontal movement possible
                
        queued = RandomGhost.random.nextInt(4) + 1; // don't stop
    }
    
    @Override
    public void toOutputStream(DataOutputStream dout) throws IOException {
        dout.writeByte(Character.RANDOMGHOST);
        dout.writeShort(realX);
        dout.writeShort(realY);
        dout.writeByte(direction);
    }
    
    public static RandomGhost fromInputStream(int rx, int ry,
        DataInputStream din) throws IOException {
        int dir = din.readByte();
        RandomGhost r = new RandomGhost(rx, ry);
        r.direction = dir;
        return r;
    }
}