package com.hjwylde.uni.swen221.lab04.robotwar;

import java.awt.Point;

/*
 * Code for Laboratory 4, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * The RandomBot just moves around randomly within the arena and fires at whatever it can see.
 * 
 * @author djp
 * 
 */
public class RandomBot extends Robot {

    public RandomBot(Point pos) {
        super(pos);
    }

    public RandomBot(Point pos, int strength) {
        super(pos, strength);
    }

    @Override
    public void move(int top, int right, int bottom, int left) {
        int dx = Main.randomInteger(3) - 1;
        int dy = Main.randomInteger(3) - 1;
        int newXPos = getPos().x + dx;
        int newYPos = getPos().y + dy;

        if ((newXPos < left) && (newXPos >= right))
            if ((newYPos < top) && (newYPos >= bottom))
                getPos().translate(dx, dy);
    }
}
