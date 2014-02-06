package com.hjwylde.uni.comp103.assignment06.centipede;

/*
 * Code for Assignment 6, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.awt.Color;

import com.hjwylde.uni.comp103.util.DrawingCanvas;

/**
 * A Food Source is a (never ending) food source stuck on the ground. It comes
 * in six types, some
 * make centipedes grow, the rest don't. It can draw itself, and return its
 * position and type. It
 * takes 20 ticks to regrow after it has been eaten. This is represented by its
 * age, and mature() is
 * only true when its age > 10;
 */

public class FoodSource {
    
    private final int size = 4; // the size of the food source
    
    private final int x; // the FoodSource's location on the screen
    private final int y;
    private Food type; // the kind of food
    private Color color;
    private int age = FoodSource.timeToGrow; // is it old enough to eat?.
    
    private static int timeToGrow = 20;
    
    public FoodSource() {
        x = (int) (Math.random() * (Simulation.eastEdge - Simulation.westEdge));
        y = (int) (Math.random() * (Simulation.southEdge - Simulation.northEdge));
        double r = Math.random();
        if (r < 0.1) {
            type = Food.Poison;
            color = Color.magenta;
        } else if (r < 0.2) {
            type = Food.Wasting;
            color = Color.blue;
        } else if (r < 0.3) {
            type = Food.Divisive;
            color = Color.orange;
        } else if (r < 0.4) {
            type = Food.BadTaste;
            color = Color.black;
        } else if (r < 0.5) {
            type = Food.Spicy;
            color = Color.red;
        } else {
            type = Food.Nutritious;
            color = Color.green;
        }
    }
    
    public void eaten() {
        age = 0;
    }
    
    public int getSize() {
        return size;
    }
    
    public Food getType() {
        return type;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void grow() {
        if (age < FoodSource.timeToGrow)
            age++;
    }
    
    public boolean mature() {
        return (age >= FoodSource.timeToGrow);
    }
    
    public boolean near(int otherX, int otherY) {
        return ((Math.abs(x - otherX) < size) && (Math.abs(y - otherY) < size));
    }
    
    public void redraw(DrawingCanvas canvas) {
        Color clr = color;
        if (age >= FoodSource.timeToGrow)
            clr = Color.lightGray;
        canvas.setForeground(clr);
        canvas.fillRect(x - size, y - size, size * 2, size * 2, false);
    }
    
    public enum Food {
        Nutritious,
        Poison,
        Wasting,
        Divisive,
        BadTaste,
        Spicy
    }
    
}
