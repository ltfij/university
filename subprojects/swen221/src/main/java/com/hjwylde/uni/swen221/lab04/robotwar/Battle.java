package com.hjwylde.uni.swen221.lab04.robotwar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
 * Code for Laboratory 4, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Battle {
    
    private List<Robot> robots = new ArrayList<>();
    private List<Robot> deadRobots = new ArrayList<>();
    
    private int arenaWidth;
    private int arenaHeight;
    
    /**
     * Construct a battle with given dimensions and number of robots.
     * 
     * @param width Width of battle arena
     * @param height Height of battle arena
     */
    public Battle(int width, int height) {
        arenaWidth = width;
        arenaHeight = height;
    }
    
    public void addRobot(Robot r) {
        if (!r.isDead())
            robots.add(r);
        else
            deadRobots.add(r);
    }
    
    /**
     * The purpose of this method is to allow each of the robots to make a move.
     */
    public void doRound() {
        Robot r;
        for (int i = 0; i < robots.size(); i++) {
            r = robots.get(i);
            
            if (r.isDead()) {
                log(r + " is dead. Removing from game.");
                deadRobots.add(r);
                robots.remove(r);
                i--;
                
                continue;
            }
            
            List<Robot> enemies = robotsNear(r, 10);
            if (!enemies.isEmpty()) {
                enemies.get(0).isShot();
                log(r + " just shot " + enemies.get(0) + ".");
            }
            
            r.move(arenaHeight, 0, 0, arenaWidth);
            log(r + " just moved to (" + r.getPos().x + ", " + r.getPos().y
                + ").");
        }
    }
    
    public int getArenaHeight() {
        return arenaHeight;
    }
    
    public int getArenaWidth() {
        return arenaWidth;
    }
    
    public boolean isRunning() {
        return robots.size() > 0;
    }
    
    public void log(String msg) {
        System.out.println("LOG: " + msg);
    }
    
    public void print() {
        for (int i = -2; i != arenaWidth; ++i)
            System.out.print("-");
        System.out.println("");
        for (int y = arenaHeight; y >= 0; --y) {
            System.out.print("|");
            for (int x = arenaWidth; x >= 0; --x) {
                // this loop isn't very efficient :(
                boolean match = false;
                for (Robot r : robots)
                    if ((r.getPos().x == x) && (r.getPos().y == y)) {
                        System.out.print(r.getCode());
                        match = true;
                        break;
                    }
                if (!match)
                    for (Robot r : deadRobots)
                        if ((r.getPos().x == x) && (r.getPos().y == y)) {
                            System.out.print(r.getCode());
                            match = true;
                            break;
                        }
                if (!match)
                    System.out.print(" ");
            }
            System.out.println("|");
        }
        for (int i = -2; i != arenaWidth; ++i)
            System.out.print("-");
        System.out.println("");
    }
    
    protected List<Robot> robotsNear(Robot robot, int radius) {
        List<Robot> visibleRobots = new LinkedList<>();
        
        for (Robot r : robots)
            if (r != robot) {
                int dx = robot.getPos().x - r.getPos().x;
                int dy = robot.getPos().y - r.getPos().y;
                // Calculate distance from me to robot r using pythagorus theorem.
                
                double distanceToR = (dx * dx) + (dy * dy);
                if ((radius * radius) > distanceToR)
                    // this robot is in range of sight
                    visibleRobots.add(r);
            }
        
        return visibleRobots;
    }
}