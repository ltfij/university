package com.hjwylde.uni.swen221.assignment5.cards.viewer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import com.hjwylde.uni.swen221.assignment5.cards.game.Card;
import com.hjwylde.uni.swen221.assignment5.cards.game.CardGame;
import com.hjwylde.uni.swen221.assignment5.cards.game.IllegalMoveException;
import com.hjwylde.uni.swen221.assignment5.cards.game.Player;

/*
 * Code for Assignment 5, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A table canvas draws all of the cards in play during the game.
 * 
 * @author djp
 * 
 */
public class TableCanvas extends Canvas implements MouseListener {
    
    private static final long serialVersionUID = 1L;
    
    private static final Image cardDownHorizontal;
    private static final Image cardDownVertical;
    private static final Image[] cardsEast = new Image[52];
    private static final Image[] cardsNorth = new Image[52];
    
    private static final Image[] cardsSouth = new Image[52];
    
    private static final Image[] cardsWest = new Image[52];
    
    private static String[] numbers = {
        "", "", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K",
        "A"
    };
    
    private static final String[] preferredFonts = {
        "Arial", "Times New Roman"
    };
    
    private static char[] suits = {
        '\u2660', '\u2666', '\u2663', '\u2665'
    };
    
    static {
        int width = 75;
        int height = 100;
        
        for (int s = 0; s <= 3; ++s)
            for (int i = 2; i <= 14; ++i) {
                int cn = (13 * s) + (i - 2);
                TableCanvas.cardsNorth[cn] = TableCanvas.createCardImage(width,
                    height, s, i, Player.NORTH);
                TableCanvas.cardsEast[cn] = TableCanvas.createCardImage(width,
                    height, s, i, Player.EAST);
                TableCanvas.cardsSouth[cn] = TableCanvas.createCardImage(width,
                    height, s, i, Player.SOUTH);
                TableCanvas.cardsWest[cn] = TableCanvas.createCardImage(width,
                    height, s, i, Player.WEST);
            }
        
        cardDownHorizontal = TableCanvas.createCardDownImage(height, width);
        cardDownVertical = TableCanvas.createCardDownImage(width, height);
    }
    
    private Font font;
    
    private CardGame game;
    
    private final TableFrame parent;
    
    public TableCanvas(CardGame game, TableFrame parent) {
        GraphicsEnvironment env = GraphicsEnvironment
            .getLocalGraphicsEnvironment();
        HashSet<String> availableNames = new HashSet<>();
        
        for (String name : env.getAvailableFontFamilyNames())
            availableNames.add(name);
        
        for (String pf : TableCanvas.preferredFonts)
            if (availableNames.contains(pf)) {
                font = new Font(pf, Font.PLAIN, 20);
                break;
            }
        
        this.game = game;
        this.parent = parent;
        setBounds(0, 0, 600, 600);
        addMouseListener(this);
    }
    
    public void determinePlayedCard(int distance, Player player) {
        try {
            Set<Card> hand = player.getHand().cards();
            if (hand.size() > 0) {
                float spacing = TableCanvas.spacing(hand);
                int pos = TableCanvas.pos(hand.size(), spacing);
                int idx = 1;
                for (Card card : hand) {
                    float width = spacing;
                    if (idx == hand.size())
                        width = 80; // for last card, which has larger surface
                    if ((distance > pos) && (distance < (pos + width))) {
                        parent.playedEvent(player, card);
                        return;
                    }
                    pos += spacing;
                    idx++;
                }
            }
        } catch (IllegalMoveException e) {
            parent.statusEvent(e.getMessage());
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        int xpos = e.getX();
        int ypos = e.getY();
        if (ypos < 110)
            // NORTH CLICK
            determinePlayedCard(xpos, Player.NORTH);
        else if (ypos > 490)
            // SOUTH CLICK
            determinePlayedCard(xpos, Player.SOUTH);
        else if (xpos < 110)
            // WEST CLICK
            determinePlayedCard(ypos, Player.WEST);
        else if (xpos > 490)
            // EAST CLICK
            determinePlayedCard(ypos, Player.EAST);
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    public void newGame(CardGame game) {
        this.game = game;
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(0, 150, 0));
        g.fillRect(0, 0, getWidth(), getHeight());
        if (font != null)
            g.setFont(font);
        drawVHand(Player.WEST, TableCanvas.cardsWest, g, 10);
        drawVHand(Player.EAST, TableCanvas.cardsEast, g, 490);
        drawHHand(Player.SOUTH, TableCanvas.cardsSouth, g, 490);
        drawHHand(Player.NORTH, TableCanvas.cardsNorth, g, 10);
        
        g.setColor(Color.YELLOW);
        g.drawRect(120, 120, 360, 360);
        TableCanvas.drawPlayedCard(game.getTrick().played(Player.NORTH),
            TableCanvas.cardsNorth, g, 265, 150);
        TableCanvas.drawPlayedCard(game.getTrick().played(Player.EAST),
            TableCanvas.cardsEast, g, 350, 265);
        TableCanvas.drawPlayedCard(game.getTrick().played(Player.SOUTH),
            TableCanvas.cardsSouth, g, 265, 350);
        TableCanvas.drawPlayedCard(game.getTrick().played(Player.WEST),
            TableCanvas.cardsWest, g, 150, 265);
    }
    
    private void drawHHand(Player player, Image[] cards, Graphics g, int ypos) {
        SortedSet<Card> hand = player.getHand().cards();
        if (hand.size() > 0) {
            final float spacing = TableCanvas.spacing(hand);
            int xpos = TableCanvas.pos(hand.size(), spacing);
            for (Card c : hand) {
                Image cimg;
                if (parent.isComputerPlayer(player))
                    cimg = TableCanvas.cardDownVertical;
                else
                    cimg = cards[TableCanvas.cn(c)];
                g.drawImage(cimg, xpos, ypos, null);
                xpos += spacing;
            }
        }
    }
    
    private void drawVHand(Player player, Image[] cards, Graphics g, int xpos) {
        SortedSet<Card> hand = player.getHand().cards();
        if (hand.size() > 0) {
            final float spacing = TableCanvas.spacing(hand);
            int ypos = TableCanvas.pos(hand.size(), spacing);
            for (Card c : hand) {
                Image cimg;
                if (parent.isComputerPlayer(player))
                    cimg = TableCanvas.cardDownHorizontal;
                else
                    cimg = cards[TableCanvas.cn(c)];
                g.drawImage(cimg, xpos, ypos, null);
                ypos += spacing;
            }
        }
    }
    
    public static void pause(int time) throws InterruptedException {
        time = time * 1000; // convert in milli-sec
        Thread.sleep(time);
    }
    
    // ==============================================================
    // Following methods are required for MouseListener
    // ==============================================================
    
    private static int cn(Card c) {
        return (c.suit().ordinal() * 13) + c.rank().ordinal();
    }
    
    private static Image createCardDownImage(int width, int height) {
        GraphicsEnvironment ge = GraphicsEnvironment
            .getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        BufferedImage img = gc.createCompatibleImage(width, height,
            Transparency.OPAQUE);
        Graphics2D g = img.createGraphics();
        
        g.setColor(new Color(0, 150, 0));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.fillRoundRect(0, 0, width, height, 15, 15);
        g.setColor(Color.GRAY);
        g.fillRoundRect(1, 1, width - 2, height - 2, 15, 15);
        
        return img;
    }
    
    private static Image createCardImage(int width, int height, int suit,
        int number, Player dir) {
        // Yeah, this method is pretty crazy ... it probably could be a *lot* better
        double theta;
        int cwidth = width;
        int cheight = height;
        int twidth = 0;
        int theight = 0;
        
        if (dir == Player.WEST) {
            theta = Math.PI / 2;
            cheight = width;
            cwidth = height;
            twidth = 0;
            theight = -height;
        } else if (dir == Player.EAST) {
            theta = -Math.PI / 2;
            cheight = width;
            cwidth = height;
            twidth = -width;
        } else if (dir == Player.SOUTH)
            theta = 0;
        else {
            theta = Math.PI;
            twidth = -width;
            theight = -height;
        }
        
        GraphicsEnvironment ge = GraphicsEnvironment
            .getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        BufferedImage img = gc.createCompatibleImage(cwidth, cheight,
            Transparency.OPAQUE);
        Graphics2D g = img.createGraphics();
        
        AffineTransform at = new AffineTransform();
        at.rotate(theta);
        at.translate(twidth, theight);
        g.setTransform(at);
        
        g.setColor(new Color(0, 150, 0));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.fillRoundRect(0, 0, width, height, 15, 15);
        g.setColor(Color.BLACK);
        g.drawRoundRect(0, 0, width, height, 15, 15);
        
        if ((suit & 1) != 0)
            g.setColor(Color.BLACK);
        else
            g.setColor(Color.RED);
        
        FontMetrics metrics = g.getFontMetrics();
        char[] numChars = (TableCanvas.numbers[number]).toCharArray();
        char[] suitChars = {
            TableCanvas.suits[suit]
        };
        int off = width
            - (metrics.charsWidth(numChars, 0, numChars.length) + 5);
        int ascent = metrics.getAscent();
        g.drawChars(numChars, 0, numChars.length, 5, 5 + ascent);
        g.drawChars(suitChars, 0, suitChars.length, 5, 5 + ascent + ascent);
        g.drawChars(numChars, 0, numChars.length, off, height - 5);
        g.drawChars(suitChars, 0, suitChars.length, off, height - 5 - ascent);
        
        return img;
    }
    
    private static void drawPlayedCard(Card card, Image[] cards, Graphics g,
        int x, int y) {
        if (card != null) {
            Image cimg = cards[TableCanvas.cn(card)];
            g.drawImage(cimg, x, y, null);
        }
    }
    
    private static int pos(int size, float spacing) {
        return 300 - ((int) ((size * spacing) + 80) / 2);
    }
    
    private static int spacing(Set<Card> hand) {
        return Math.min(80, 300 / hand.size());
    }
}
