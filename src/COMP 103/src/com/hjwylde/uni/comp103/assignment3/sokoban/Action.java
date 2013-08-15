package com.hjwylde.uni.comp103.assignment3.sokoban;

/*
 * Code for Assignment 3, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/** A class to record an action: move or push in a given direction. */

public class Action {
    
    private String kind;
    private final Direction dir;
    
    public Action(String a, Direction d) {
        if (a.equals("push"))
            kind = "push";
        else if (a.equals("move"))
            kind = "move";
        dir = d;
    }
    
    public Direction dir() {
        return dir;
    }
    
    public boolean isMove() {
        return kind.equals("move");
    }
    
    public boolean isPush() {
        return kind.equals("push");
    }
    
    @Override
    public String toString() {
        return (kind + " to " + dir);
    }
    
    /** Test method */
    public static void main(String[] args) {
        Action A = new Action("push", Direction.up);
        System.out.println(A + " is a push: " + A.isPush());
        System.out.println(A + " is a Move: " + A.isMove());
        Action B = new Action("move", Direction.right);
        System.out.println(B + " is a push: " + B.isPush());
        System.out.println(B + " is a Move: " + B.isMove());
    }
    
}
