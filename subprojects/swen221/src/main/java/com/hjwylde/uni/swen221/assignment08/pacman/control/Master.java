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

package com.hjwylde.uni.swen221.assignment08.pacman.control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.hjwylde.uni.swen221.assignment08.pacman.game.Board;

/*
 * Code for Assignment 8, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A master connection receives events from a slave connection via a socket. These events are
 * registered with the board. The master connection is also responsible for transmitting information
 * to the slave about the current board state.
 */
public final class Master extends Thread {

    private final Board board;
    private final int broadcastClock;
    private final int uid;
    private final Socket socket;

    public Master(Socket socket, int uid, int broadcastClock, Board board) {
        this.board = board;
        this.broadcastClock = broadcastClock;
        this.socket = socket;
        this.uid = uid;
    }

    @Override
    public void run() {
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            // First, write the period to the stream
            output.writeInt(uid);
            output.writeInt(board.width());
            output.writeInt(board.height());
            output.write(board.wallsToByteArray());

            while (true)
                try {

                    if (input.available() != 0) {

                        // read direction event from client.
                        int dir = input.readInt();
                        switch (dir) {
                            case 1:
                                board.player(uid).moveUp();
                                break;
                            case 2:
                                board.player(uid).moveDown();
                                break;
                            case 3:
                                board.player(uid).moveRight();
                                break;
                            case 4:
                                board.player(uid).moveLeft();
                                break;
                        }
                    }

                    // Now, broadcast the state of the board to client
                    byte[] state = board.toByteArray();
                    output.writeInt(state.length);
                    output.write(state);
                    output.flush();
                    Thread.sleep(broadcastClock);
                } catch (InterruptedException e) {
                }
        } catch (IOException e) {
            System.err.println("PLAYER " + uid + " DISCONNECTED");
            board.disconnectPlayer(uid);
        } finally {
            try {
                socket.close(); // release socket ... v.important!
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
