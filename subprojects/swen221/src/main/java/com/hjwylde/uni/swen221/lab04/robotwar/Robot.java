package com.hjwylde.uni.swen221.lab04.robotwar;

import java.awt.Point;

/*
 * Code for Laboratory 4, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public abstract class Robot {

    protected static final int DEFAULT_STRENGTH = 10;

    private static int count = 0;
    private int id;

    private Point pos;

    public int strength;

    public Robot(Point pos) {
        this(pos, Robot.DEFAULT_STRENGTH);
    }

    public Robot(Point pos, int strength) {
        id = Robot.count++;

        this.pos = pos;
        this.strength = strength;
    }

    public char getCode() {
        return (isDead() ? '*' : toString().charAt(0));
    }

    public Point getPos() {
        return pos;
    }

    public boolean isDead() {
        return strength <= 0;
    }

    public void isShot() {
        isShot(1);
    }

    public void isShot(int damage) {
        strength -= damage;
    }

    public abstract void move(int top, int right, int bottom, int left);

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + id;
    }
}
