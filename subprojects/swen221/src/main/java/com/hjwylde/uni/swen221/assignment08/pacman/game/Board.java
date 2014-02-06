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

import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

/*
 * Code for Assignment 8, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * The board class represents the pacman game board. This class is used by the Character threads to
 * move, and generally interact with each other.
 * 
 * @author djp
 */
public class Board {
    
    private final int width;
    private final int height;
    
    public static final int WAITING = 0;
    public static final int READY = 1;
    public static final int PLAYING = 2;
    public static final int GAMEOVER = 3;
    public static final int GAMEWON = 4;
    
    private int state; // this is used to tell us what state we're in.
    private int nPillsRemaining; // this is used to count the number of remaining pills
    
    /**
     * The following stores the locations in the grid of all walls. It is effectively implemented as
     * a
     * 2D grid of bits, where each bit represents a wall.
     */
    private BitSet walls;
    
    /**
     * The following stores the locations in the grid of all pills. It is effectively implemented as
     * a
     * 2D grid of bits, where each bit represents a wall.
     */
    private BitSet pills;
    
    /**
     * The following is a list of one dimension integer arrays, which are guaranteed to be of length
     * 2. The first element gives the x-component of the portal, the second component gives the
     * y-component.
     */
    private final ArrayList<int[]> pacmanPortals = new ArrayList<>();
    private int nextPacPortal = 0; // identify the next portal to be used
    
    /**
     * The following is a list of one dimension integer arrays, which are guaranteed to be of length
     * 2. The first element gives the x-component of the portal, the second component gives the
     * y-component.
     */
    private final ArrayList<int[]> ghostPortals = new ArrayList<>();
    private int nextGhostPortal = 0; // identify the next portal to be used
    
    /**
     * The following is a list of the characters in the game. This includes pacmen, ghosts and other
     * misc things.
     */
    private final List<Character> characters = Collections
        .synchronizedList(new ArrayList<Character>());
    
    /**
     * The following constants determine the possible fixed-object types.
     */
    public final static int NOUT = 0;
    
    public final static int WALL = 1;
    
    /**
     * The UID is a unique identifier for all characters in the game. This is required in order to
     * synchronise the movements of different players across boards.
     */
    private static int uid = 0;
    
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        walls = new BitSet();
        pills = new BitSet();
    }
    
    public synchronized void addPill(int x, int y) {
        nPillsRemaining++;
        pills.set(x + (y * width));
    }
    
    public synchronized void addWall(int x, int y) {
        walls.set(x + (y * width));
    }
    
    public boolean canMoveDown(MovingCharacter p) {
        int realX = p.realX();
        int realY = p.realY();
        realY += 5;
        int ny = realY / 30;
        if ((realY % 30) != 0)
            ny++;
        int nx = (realX + 15) / 30;
        return !isWall(nx, ny);
    }
    
    public boolean canMoveLeft(MovingCharacter p) {
        int realX = p.realX();
        int realY = p.realY();
        realX -= 5;
        int nx = realX / 30;
        int ny = (realY + 15) / 30;
        return !isWall(nx, ny);
    }
    
    public boolean canMoveRight(MovingCharacter p) {
        int realX = p.realX();
        int realY = p.realY();
        realX += 5;
        int nx = realX / 30;
        if ((realX % 30) != 0)
            nx++;
        int ny = (realY + 15) / 30;
        return !isWall(nx, ny);
    }
    
    public boolean canMoveUp(MovingCharacter p) {
        int realX = p.realX();
        int realY = p.realY();
        realY -= p.speed();
        int ny = realY / 30;
        int nx = (realX + 15) / 30;
        return !isWall(nx, ny);
    }
    
    /**
     * Iterate the characters in the game.
     * 
     * @return a shallow copy of the list of characters.
     */
    public List<Character> characters() {
        // Return a copy to prevent concurrent modification access errors.
        return new ArrayList<>(characters);
    }
    
    /**
     * The clock tick is essentially a clock trigger, which allows the board to update the current
     * state. The frequency with which this is called determines the rate at which the game state is
     * updated.
     */
    public synchronized void clockTick() {
        if ((state != Board.PLAYING) && (state != Board.GAMEOVER))
            return; // do nothing unless the game is active.
            
        ArrayList<Character> ghosts = new ArrayList<>();
        
        int nplayers = 0;
        for (int i = 0; i != characters.size(); ++i) {
            Character p = characters.get(i);
            p.tick(this);
            
            // reread p, since it might be gone now ...
            p = characters.get(i);
            if (p == null) {
                // dead character encountered, remove it.
                characters.remove(i--);
                continue;
            }
            if (p instanceof Ghost)
                ghosts.add(p);
        }
        
        // Now, perform collision detection to see if any PACMEN have collided with ghosts.
        for (int i = 0; i != characters.size(); ++i) {
            Character c = characters.get(i);
            int px = (c.realX() + 15) / 30;
            int py = (c.realY() + 15) / 30;
            if (c instanceof Pacman) {
                Pacman p = (Pacman) c;
                if (!p.isDead())
                    nplayers++;
                if (p.isDead() || p.isDying())
                    continue;
                for (Character g : ghosts) {
                    int gx = (g.realX() + 15) / 30;
                    int gy = (g.realY() + 15) / 30;
                    if ((px == gx) && (py == gy))
                        // pacman and ghost have collided ...
                        // So, replace pacman with disappearing character
                        p.markAsDying();
                }
            }
        }
        
        if (nplayers == 0)
            state = Board.GAMEOVER;
        else if (nPillsRemaining == 0)
            state = Board.GAMEWON;
    }
    
    public synchronized void disconnectPlayer(int uid) {
        for (int i = 0; i != characters.size(); ++i) {
            Character p = characters.get(i);
            if ((p instanceof Pacman) && (((Pacman) p).uid() == uid))
                characters.set(i, new Disappear(p.realX(), p.realY(), 0));
        }
    }
    
    public synchronized void eatPill(int x, int y) {
        nPillsRemaining--;
        pills.clear(x + (y * width));
    }
    
    /**
     * The following method accepts a byte array representing the state of a pacman board; this
     * state
     * will be broadcast by a master connection, and is then used to overwrite the current state
     * (since it should be more up to date).
     * 
     * @param bytes the bytes.
     * @throws IOException if IO error.
     */
    public synchronized void fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(bin);
        
        state = din.readByte();
        // Second, update pills
        int bitwidth = (width % 8) == 0 ? width : width + 8;
        int bitsize = (bitwidth / 8) * height;
        byte[] pillBytes = new byte[bitsize];
        din.read(pillBytes);
        pills = Board.bitsFromByteArray(pillBytes);
        nPillsRemaining = pills.cardinality();
        
        // Third, update characters
        int ncharacters = din.readInt();
        characters.clear();
        for (int i = 0; i != ncharacters; ++i)
            characters.add(Character.fromInputStream(din));
    }
    
    public int height() {
        return height;
    }
    
    public synchronized boolean isPill(int x, int y) {
        return pills.get(x + (y * width));
    }
    
    public synchronized boolean isWall(int x, int y) {
        return walls.get(x + (y * width));
    }
    
    public synchronized Pacman player(int uid) {
        for (Character p : characters)
            if ((p instanceof Pacman) && (((Pacman) p).uid() == uid))
                return (Pacman) p;
        throw new IllegalArgumentException("Invalid Character UID");
    }
    
    /**
     * Register a new ghost into the game. The ghost will be placed into the next available ghost
     * portal.
     * 
     * @param homer --- is this a homing ghost or not?
     */
    public synchronized void registerGhost(boolean homer) {
        int[] portal = ghostPortals.get(nextGhostPortal);
        nextGhostPortal = (nextGhostPortal + 1) % ghostPortals.size();
        Character r;
        if (homer)
            r = new HomerGhost(portal[0] * 30, portal[1] * 30);
        else
            r = new RandomGhost(portal[0] * 30, portal[1] * 30);
        characters.add(r);
    }
    
    /**
     * Register a new ghost portal on the board. A ghost portal is a place where ghosts will appear
     * from.
     */
    public void registerGhostPortal(int x, int y) {
        ghostPortals.add(new int[] {
            x, y
        });
    }
    
    /**
     * Register a new pacman into the game. The Pacman will be placed onto the next available
     * portal.
     */
    public synchronized int registerPacman() {
        int[] portal = pacmanPortals.get(nextPacPortal);
        nextPacPortal = (nextPacPortal + 1) % pacmanPortals.size();
        Character r = new Pacman(portal[0] * 30, portal[1] * 30,
            MovingCharacter.STOPPED, ++Board.uid, 3, 0);
        characters.add(r);
        return Board.uid;
    }
    
    /**
     * Register a new pacman portal on the board. A pacman portal is a place where pacman will
     * appear
     * from.
     */
    public void registerPacPortal(int x, int y) {
        pacmanPortals.add(new int[] {
            x, y
        });
    }
    
    public synchronized void removeCharacter(Character character) {
        for (int i = 0; i != characters.size(); ++i) {
            Character p = characters.get(i);
            if (character == p) {
                // We can't call remove here, since this results in a concurrent modification
                // exception, as
                // this method will be called indirectly from the clockTick() method.
                characters.set(i, null);
                return;
            }
        }
    }
    
    /**
     * A pre-registered pacman has died, but wants to come back to life. Therefore, allocate it the
     * next available pacman portal.
     */
    public int[] respawnPacman() {
        int[] portal = pacmanPortals.get(nextPacPortal);
        nextPacPortal = (nextPacPortal + 1) % pacmanPortals.size();
        return portal;
    }
    
    /**
     * Set the board state.
     * 
     * @param state the state.
     */
    public synchronized void setState(int state) {
        this.state = state;
    }
    
    /**
     * Get current board state.
     * 
     * @return the state.
     */
    public synchronized int state() {
        return state;
    }
    
    /**
     * The following method converts the current state of the board into a byte array, such that it
     * can be shipped across a connection to an awaiting client.
     */
    public synchronized byte[] toByteArray() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);
        
        dout.writeByte(state);
        
        // First, write output locations of remaining pills
        int bitwidth = (width % 8) == 0 ? width : width + 8;
        int bitsize = (bitwidth / 8) * height;
        byte[] pillBytes = new byte[bitsize];
        Board.bitsToByteArray(pills, pillBytes);
        dout.write(pillBytes);
        
        dout.writeInt(characters.size());
        for (Character p : characters)
            p.toOutputStream(dout);
        
        dout.flush();
        
        // Finally, return!!
        return bout.toByteArray();
    }
    
    /**
     * The following method accepts a byte array representation of the board walls. This is
     * broadcast
     * by a master connection when the connection is established.
     */
    public void wallsFromByteArray(byte[] bytes) {
        walls = Board.bitsFromByteArray(bytes);
    }
    
    /**
     * The following method generates a byte array representation of the walls in the board. This is
     * broadcast by a master connection when that connection is established.
     */
    public byte[] wallsToByteArray() {
        // First, write output locations of remaining pills
        int bitwidth = (width % 8) == 0 ? width : width + 8;
        int bitsize = (bitwidth / 8) * height;
        byte[] wallBytes = new byte[bitsize];
        Board.bitsToByteArray(walls, wallBytes);
        return wallBytes;
    }
    
    /**
     * Get the board width.
     */
    public int width() {
        return width;
    }
    
    /**
     * Read a bit set from a byte array.
     */
    private static BitSet bitsFromByteArray(byte[] bytes) {
        BitSet bits = new BitSet();
        for (int i = 0; i < (bytes.length * 8); i++) {
            int offset = i >> 3;
            if ((bytes[offset] & (1 << (i % 8))) > 0)
                bits.set(i);
        }
        return bits;
    }
    
    /**
     * Create a byte array from a bit set.
     */
    private static byte[] bitsToByteArray(BitSet bits, byte[] bytes) {
        for (int i = 0; i < bits.length(); i++) {
            int offset = i >> 3;
            if (bits.get(i) && (offset < bytes.length))
                bytes[offset] |= 1 << (i % 8);
        }
        return bytes;
    }
}