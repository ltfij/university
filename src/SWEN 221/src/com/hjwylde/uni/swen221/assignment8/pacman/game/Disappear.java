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

package com.hjwylde.uni.swen221.assignment8.pacman.game;

import java.awt.Graphics;
import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
 * Code for Assignment 8, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public final class Disappear extends Character {
    
    private int state;
    
    private static final Image[] DISCONNECTS = {
        BoardCanvas.loadImage("disconnect1.png"),
        BoardCanvas.loadImage("disconnect1.png"),
        BoardCanvas.loadImage("disconnect2.png"),
        BoardCanvas.loadImage("disconnect2.png"),
        BoardCanvas.loadImage("disconnect3.png"),
        BoardCanvas.loadImage("disconnect3.png"),
        BoardCanvas.loadImage("disconnect4.png"),
        BoardCanvas.loadImage("disconnect4.png")
    };
    
    public Disappear(int realX, int realY, int state) {
        super(realX, realY);
        
        this.state = state;
    }
    
    @Override
    public void draw(Graphics g) {
        g.drawImage(Disappear.DISCONNECTS[state], realX, realY, null, null);
    }
    
    public int state() {
        return state;
    }
    
    @Override
    public void tick(Board game) {
        if (state >= 7)
            game.removeCharacter(this);
        else
            state = state + 1;
    }
    
    @Override
    public void toOutputStream(DataOutputStream dout) throws IOException {
        dout.writeByte(Character.DISAPPEAR);
        dout.writeShort(realX);
        dout.writeShort(realY);
        dout.writeByte(state);
    }
    
    public static Disappear fromInputStream(int rx, int ry, DataInputStream din)
        throws IOException {
        byte state = din.readByte();
        return new Disappear(rx, ry, state);
    }
}