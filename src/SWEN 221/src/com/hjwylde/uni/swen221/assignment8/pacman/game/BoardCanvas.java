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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;

import javax.imageio.ImageIO;

/*
 * Code for Assignment 8, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * The board canvas is responsible for drawing the game. Currently, it uses a relatively primitive
 * form of double buffering to ensure there's no flicker during frame updates. This class also
 * generates a number of images using Java's graphics capabilities, which saves having to have lots
 * of very similar images for the different directions.
 * 
 * @author djp
 */
public class BoardCanvas extends Canvas {
    
    private static final long serialVersionUID = 1L;
    
    private static final String IMAGE_PATH = "images/";
    
    private static final Image WALL_S = BoardCanvas.loadImage("walls.png");
    private static final Image WALL_E = BoardCanvas.loadImage("walle.png");
    private static final Image WALL_C = BoardCanvas.loadImage("wallc.png");
    private static final Image WALL_T = BoardCanvas.loadImage("wallt.png");
    private static final Image WALL_O = BoardCanvas.loadImage("wallo.png");
    private static final Image WALL_X = BoardCanvas.loadImage("wallx.png");
    private static final Image PILL = BoardCanvas.loadImage("pill.png");
    
    private static final Image[] WALL_IMGS = {
        BoardCanvas.rotate(BoardCanvas.WALL_O, 90),
        BoardCanvas.rotate(BoardCanvas.WALL_E, 180), // 1 - | | | U
        BoardCanvas.WALL_E, // 2 - | | D |
        BoardCanvas.WALL_S, // 3 - | | D | U
        BoardCanvas.rotate(BoardCanvas.WALL_E, 90), // 4 - | L | |
        BoardCanvas.rotate(BoardCanvas.WALL_C, 180), // 5 - | L | | U
        BoardCanvas.rotate(BoardCanvas.WALL_C, 90), // 6 - | L | D |
        BoardCanvas.rotate(BoardCanvas.WALL_T, 180), // 7 - | L | D | U
        BoardCanvas.rotate(BoardCanvas.WALL_E, -90), // 8 - R | | |
        BoardCanvas.rotate(BoardCanvas.WALL_C, -90), // 9 - R | | | U
        BoardCanvas.WALL_C, // 0 - R | | D |
        BoardCanvas.WALL_T, // 1 - R | | D | U
        BoardCanvas.rotate(BoardCanvas.WALL_S, 90), // 2 - R | L | |
        BoardCanvas.rotate(BoardCanvas.WALL_T, -90), // 3 - R | L | | U
        BoardCanvas.rotate(BoardCanvas.WALL_T, 90), // 4 - R | L | D |
        BoardCanvas.WALL_X
    // 5 - R | L | D | U
    };
    
    private static final String[] preferredFonts = {
        "Courier New", "Arial", "Times New Roman"
    };
    private Font font;
    private final int uid;
    private final Board gameBoard;
    
    private static final String[] trails = {
        "st", "nd", "rd", "th", "th", "th", "th", "th", "th", "th"
    };
    
    private Image offscreen = null;
    
    public BoardCanvas(int uid, Board gameBoard) {
        this.gameBoard = gameBoard;
        this.uid = uid;
        GraphicsEnvironment env = GraphicsEnvironment
            .getLocalGraphicsEnvironment();
        HashSet<String> availableNames = new HashSet<>();
        
        for (String name : env.getAvailableFontFamilyNames())
            availableNames.add(name);
        
        for (String pf : BoardCanvas.preferredFonts)
            if (availableNames.contains(pf)) {
                font = new Font(pf, Font.BOLD, 24);
                break;
            }
        setSize(new Dimension(gameBoard.width() * 30,
            (gameBoard.height() * 30) + 30));
    }
    
    public void drawGameWone(Graphics g) {
        int myScore = gameBoard.player(uid).score();
        int nAbove = 0;
        int nBelow = 0;
        
        for (Character c : gameBoard.characters())
            if (c instanceof Pacman) {
                Pacman p = (Pacman) c;
                if (p.score() < myScore)
                    nBelow++;
                else if (p.score() > myScore)
                    nAbove++;
            }
        
        if ((nAbove == 0) && (nBelow == 0))
            // single user game
            drawMessage("You Won!", g);
        else
            drawMessage("You came: " + (nAbove + 1)
                + BoardCanvas.trails[(nAbove) % 10], g);
    }
    
    @Override
    public synchronized void paint(Graphics g) {
        // Only want one thread updating / rendering the board at a time.
        int width = gameBoard.width();
        int height = gameBoard.height();
        
        // First, draw the board
        
        for (int x = 0; x != width; ++x)
            for (int y = 0; y != height; ++y)
                if (gameBoard.isWall(x, y)) {
                    // do nothing
                } else if (gameBoard.isPill(x, y))
                    g.drawImage(BoardCanvas.PILL, x * 30, y * 30, null, null);
                else {
                    g.setColor(Color.BLACK);
                    g.fillRect(x * 30, y * 30, 30, 30);
                }
        
        // Second, draw the characters
        int score = 0;
        int nlives = 0;
        
        // When drawing must have the lock on the gameBoard so no changes are made at the same time.
        synchronized (gameBoard) {
            for (Character p : gameBoard.characters())
                if (p instanceof Pacman) {
                    Pacman pm = (Pacman) p;
                    if (pm.uid() == uid) {
                        pm.drawOwn(g);
                        score = pm.score();
                        nlives = pm.lives();
                    } else
                        p.draw(g);
                } else
                    p.draw(g);
        }
        
        // finally, draw any messages
        switch (gameBoard.state()) {
        case Board.WAITING:
            drawMessage("Waiting", g);
            break;
        case Board.READY:
            drawMessage("Ready", g);
            break;
        case Board.GAMEOVER:
            drawMessage("Game Over", g);
            break;
        case Board.GAMEWON:
            drawGameWone(g);
        }
        
        drawScore("SCORE: " + score, g);
        drawLives(nlives, g);
    }
    
    @Override
    public synchronized void update(Graphics g) {
        // Only want one thread updating / rendering graphics at a time.
        
        if (offscreen == null)
            initialiseOffscreen();
        Image localOffscreen = offscreen;
        Graphics offgc = offscreen.getGraphics();
        // do normal redraw
        paint(offgc);
        // transfer offscreen to window
        g.drawImage(localOffscreen, 0, 0, this);
    }
    
    private synchronized void drawLives(int nlives, Graphics g) {
        for (int i = 0; i != nlives; ++i) {
            int rx = (gameBoard.width() - i - 1) * 30;
            g.drawImage(Pacman.PACMAN_RIGHT[2], rx, gameBoard.height() * 30,
                null, null);
        }
    }
    
    private synchronized void drawMessage(String msg, Graphics g) {
        g.setFont(font);
        int width = gameBoard.width();
        int height = gameBoard.height();
        FontMetrics metrics = g.getFontMetrics();
        int ascent = metrics.getAscent();
        char[] chars = msg.toCharArray();
        int msgWidth = metrics.charsWidth(msg.toCharArray(), 0, msg.length());
        int msgHeight = metrics.getHeight();
        int boxWidth = msgWidth + 30;
        int boxHeight = msgHeight + 30;
        int x = ((width * 30) - boxWidth) / 2;
        int y = ((height * 30) - boxHeight) / 2;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, boxWidth, boxHeight);
        g.setColor(Color.yellow);
        g.drawRect(x, y, boxWidth, boxHeight);
        g.drawRect(x + 1, y + 1, boxWidth - 2, boxHeight - 2);
        g.drawChars(chars, 0, chars.length, x + 15, y + 15 + ascent);
        offscreen = null; // reset offscreen, since we want to get rid of the
        // message
    }
    
    private synchronized void drawScore(String score, Graphics g) {
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        int ascent = metrics.getAscent();
        int width = gameBoard.width();
        int height = gameBoard.height();
        int y = (height * 30);
        g.setColor(Color.BLACK);
        g.fillRect(0, y, width * 30, 30);
        char[] chars = score.toCharArray();
        g.setColor(Color.YELLOW);
        g.drawChars(chars, 0, chars.length, 5, y + ascent);
    }
    
    private synchronized void drawWall(int x, int y, Graphics g) {
        boolean above = ((y - 1) >= 0) && gameBoard.isWall(x, y - 1);
        boolean below = ((y + 1) < gameBoard.height())
            && gameBoard.isWall(x, y + 1);
        boolean left = ((x - 1) >= 0) && gameBoard.isWall(x - 1, y);
        boolean right = ((x + 1) < gameBoard.width())
            && gameBoard.isWall(x + 1, y);
        
        int mask = 0;
        
        if (above)
            mask |= 1;
        if (below)
            mask |= 2;
        if (left)
            mask |= 4;
        if (right)
            mask |= 8;
        
        g.drawImage(BoardCanvas.WALL_IMGS[mask], x * 30, y * 30, null, null);
    }
    
    private synchronized void initialiseOffscreen() {
        Dimension d = getSize();
        offscreen = createImage(d.width, d.height);
        // clear the exposed area
        Graphics offgc = offscreen.getGraphics();
        offgc.setColor(getBackground());
        offgc.fillRect(0, 0, d.width, d.height);
        offgc.setColor(getForeground());
        
        int width = gameBoard.width();
        int height = gameBoard.height();
        
        // First, draw the board
        
        for (int x = 0; x != width; ++x)
            for (int y = 0; y != height; ++y)
                if (gameBoard.isWall(x, y))
                    drawWall(x, y, offgc);
        
    }
    
    /**
     * Load an image from the file system, using a given filename.
     */
    public static Image loadImage(String filename) {
        // using the URL means the image loads when stored
        // in a jar or expanded into individual files.
        java.net.URL imageURL = BoardCanvas.class
            .getResource(BoardCanvas.IMAGE_PATH + filename);
        
        try {
            Image img = ImageIO.read(imageURL);
            return img;
        } catch (IOException e) {
            // we've encountered an error loading the image. There's not much we can actually do at
            // this
            // point, except to abort the game.
            throw new RuntimeException("Unable to load image: " + filename);
        }
    }
    
    /**
     * Rotate an image a given number of degrees.
     */
    public static Image rotate(Image src, double angle) {
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        BufferedImage img = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.rotate(Math.toRadians(angle), width / 2, height / 2);
        g.drawImage(src, 0, 0, width, height, null);
        g.dispose();
        return img;
    }
}