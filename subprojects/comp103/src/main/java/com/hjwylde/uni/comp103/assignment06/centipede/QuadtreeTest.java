package com.hjwylde.uni.comp103.assignment06.centipede;

/*
 * Code for Assignment 6, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 * 
 * I have included this class which is a test class to see how the Quadtree works. You may click the
 * mouse anywhere on the screen, which it will insert an index into the Quadtree, and then draw the
 * structure of it on a JFrame. Click and drag capabilities have been included to test out the
 * searching for indexes, drag the selection tool over an area to see what indexes are contained
 * within this area (outputed with System.out.println(...)).
 * 
 * The drawing abilities aren't the best, but they do show the Quadtree structure correctly.
 */

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class QuadtreeTest extends JFrame implements MouseListener,
    MouseMotionListener {
    
    private static final long serialVersionUID = 1L;
    
    private final int tileSize = 4;
    private final int width = 512;
    private final int height = 512;
    
    private int beginDragX = -1;
    private int beginDragY = -1;
    private int endDragX = -1;
    private int endDragY = -1;
    
    private final Quadtree tree;
    
    private int counter = 1;
    
    public QuadtreeTest() {
        super();
        
        setSize(width, height);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseListener(this);
        addMouseMotionListener(this);
        validate();
        
        tree = new Quadtree(new Rectangle(0, 0, width, height), 7);
    }
    
    public void drawFrame() {
        createBufferStrategy(2);
        BufferStrategy bf = getBufferStrategy();
        
        Graphics2D g = null;
        
        try {
            g = (Graphics2D) bf.getDrawGraphics();
            
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            
            if (tree != null)
                tree.dumpTree(g);
            
            if ((beginDragX != -1) && (beginDragY != -1)) {
                g.setColor(Color.BLUE);
                
                Rectangle selected = getBounds(beginDragX, beginDragY,
                    endDragX, endDragY);
                g.drawRect(selected.x, selected.y, selected.width,
                    selected.height);
            }
        } finally {
            if (g != null)
                g.dispose();
        }
        
        bf.show();
        Toolkit.getDefaultToolkit().sync();
    }
    
    public Rectangle getBounds(int x1, int y1, int x2, int y2) {
        Rectangle bounds;
        
        if (x1 < x2) {
            if (y1 < y2)
                bounds = new Rectangle(x1, y1, x2 - x1, y2 - y1);
            else
                bounds = new Rectangle(x1, y2, x2 - x1, y1 - y2);
        } else if (y1 < y2)
            bounds = new Rectangle(x2, y1, x1 - x2, y2 - y1);
        else
            bounds = new Rectangle(x2, y2, x1 - x2, y1 - y2);
        
        return bounds;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Adding in index " + counter + " of size "
            + tileSize + "x" + tileSize + " at: (" + e.getX() + ", " + e.getY()
            + ")");
        tree.insert(counter++, new Rectangle(e.getX(), e.getY(), tileSize,
            tileSize));
        
        repaint();
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if ((beginDragX == -1) && (beginDragY == -1)) {
            beginDragX = e.getX();
            beginDragY = e.getY();
        }
        
        endDragX = e.getX();
        endDragY = e.getY();
        
        repaint();
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mouseMoved(MouseEvent e) {
        beginDragX = -1;
        beginDragY = -1;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if ((beginDragX == -1) && (beginDragY == -1))
            return;
        
        Rectangle toSearch = getBounds(beginDragX, beginDragY, e.getX(),
            e.getY());
        
        beginDragX = -1;
        beginDragY = -1;
        
        if ((toSearch.height <= 0) && (toSearch.width <= 0))
            return;
        
        System.out.println("Searching in: " + toSearch.toString());
        for (Integer i : tree.getAllWithin(toSearch))
            System.out.println("Area contains: " + i);
        
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        drawFrame();
    }
    
    public static void main(String[] args) {
        new QuadtreeTest();
    }
}
