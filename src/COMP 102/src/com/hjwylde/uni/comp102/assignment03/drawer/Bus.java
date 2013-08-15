package com.hjwylde.uni.comp102.assignment03.drawer;

import java.awt.Color;

import com.hjwylde.uni.comp102.util.DrawingCanvas;

/*
 * Code for Assignment 3, COMP 102
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Bus {
    
    int x;
    int y;
    // 1 for bus facing right, -1 for bus facing left.
    int direction;
    
    Color color;
    
    public Bus(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        direction = 1;
        
        this.color = color;
    }
    
    public Bus(int x, int y, int r, int g, int b) {
        this(x, y, new Color(r, g, b));
    }
    
    public Color getColor() {
        return color;
    }
    
    public int getDirection() {
        return direction;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void paint(DrawingCanvas canvas) {
        Color canvasColor = canvas.getForeground();
        canvas.setColor(color);
        
        if (direction == -1) {
            canvas.fillRect(x - 30, y, 50, 10);
            canvas.fillRect(x - 20, y - 10, 40, 10);
            canvas.setColor(Color.BLACK);
            for (int i = -15; i < 20; i += 5)
                canvas.fillRect(x + i, y - 6, 3, 3);
            canvas.fillOval(x - 20, y + 10, 4, 4);
            canvas.fillOval(x + 14, y + 10, 4, 4);
        } else {
            canvas.fillRect(x - 20, y, 50, 10);
            canvas.fillRect(x - 20, y - 10, 40, 10);
            canvas.setColor(Color.BLACK);
            for (int i = -18; i < 17; i += 5)
                canvas.fillRect(x + i, y - 6, 3, 3);
            canvas.fillOval(x - 14, y + 10, 4, 4);
            canvas.fillOval(x + 20, y + 10, 4, 4);
        }
        
        canvas.setColor(canvasColor);
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public void setDirection(int direction) {
        this.direction = direction;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
}
