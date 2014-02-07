package com.hjwylde.uni.swen222.lab04.moon_lander.ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class LanderCanvas extends JPanel implements KeyListener {

    private static final int[] GROUND_XS = {0, 30, 40, 100, 140, 160, 180, 200, 220, 230, 300, 310,
            330, 350, 360, 400, 410, 435, 460, 465, 500, 545, 560, 575, 580, 600, 600, 0};
    private static final int[] GROUND_YS = {500, 450, 480, 510, 350, 400, 395, 480, 490, 480, 480,
            520, 515, 520, 515, 550, 400, 350, 360, 400, 410, 480, 455, 465, 480, 500, 600, 600};
    private static final int[] LANDER_XS = {11, 13, 27, 29, 30, 26, 37, 40, 40, 30, 30, 33, 24, 21,
            24, 16, 19, 16, 7, 0, 0, 10, 10, 3, 14, 10};
    private static final int[] LANDER_YS = {5, 0, 0, 5, 20, 20, 35, 35, 40, 40, 35, 35, 20, 20, 25,
            25, 20, 20, 35, 35, 40, 40, 35, 35, 20, 20};
    private static final Polygon TERRAIN = new Polygon(GROUND_XS, GROUND_YS, GROUND_XS.length);

    private long landerRelativeX = 0;
    private long landerRelativeY = 0;

    private boolean rightPressed = false;
    private boolean leftPressed = false;
    private boolean upPressed = false;
    private long upPressedTime = 0;

    private boolean isGameOver = false;

    private int fuel = 2500;

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.GRAY);
        g.fillPolygon(GROUND_XS, GROUND_YS, GROUND_XS.length);

        g.setColor(Color.LIGHT_GRAY);
        g.fillPolygon(getAdjustedLanderXs(), getAdjustedLanderYs(), LANDER_XS.length);

        g.setColor(Color.RED);
        g.fillRect(500, 20, fuel / 25, 10);

        Rectangle box =
                new Polygon(getAdjustedLanderXs(), getAdjustedLanderYs(), LANDER_XS.length)
                        .getBounds();
        if (upPressed) {
            Polygon thruster =
                    new Polygon(new int[] {(int) box.getMinX(), (int) (box.getMinX() + 10),
                            (int) (box.getMinX() + 5)}, new int[] {(int) box.getMaxY(),
                            (int) box.getMaxY(), (int) (box.getMaxY() + 8)}, 3);
            g.fillPolygon(thruster);
            thruster =
                    new Polygon(new int[] {(int) box.getMaxX() - 10, (int) (box.getMaxX()),
                            (int) (box.getMaxX() - 5)}, new int[] {(int) box.getMaxY(),
                            (int) box.getMaxY(), (int) (box.getMaxY() + 8)}, 3);
            g.fillPolygon(thruster);
        }
        if (rightPressed) {
            Polygon thruster =
                    new Polygon(new int[] {(int) box.getMaxX(), (int) (box.getMaxX()),
                            (int) (box.getMaxX() + 8)}, new int[] {(int) box.getMaxY(),
                            (int) box.getMaxY() - 7, (int) (box.getMaxY() - 3)}, 3);
            g.fillPolygon(thruster);
        }
        if (leftPressed) {
            Polygon thruster =
                    new Polygon(new int[] {(int) box.getMinX(), (int) (box.getMinX()),
                            (int) (box.getMinX() - 8)}, new int[] {(int) box.getMaxY(),
                            (int) box.getMaxY() - 7, (int) (box.getMaxY() - 3)}, 3);
            g.fillPolygon(thruster);
        }
    }

    private int[] getAdjustedLanderXs() {
        return adjust(LANDER_XS, (int) (landerRelativeX / 2));
    }

    private int[] getAdjustedLanderYs() {
        // int time = (int) (landerRelativeY / 100);
        // int distance = (time + time * time) / 5;

        return adjust(LANDER_YS, (int) (landerRelativeY / 100));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }

    public void update(long time) {
        // Move based on the time elapsed
        landerRelativeY += (time) + (time * time);
        if (rightPressed)
            landerRelativeX -= time;
        if (leftPressed)
            landerRelativeX += time;
        if (upPressed) {
            upPressedTime += time;
        }
        if (rightPressed || leftPressed || upPressed)
            fuel -= time;
        if (fuel <= 0) {
            rightPressed = false;
            leftPressed = false;
            upPressed = false;
            upPressedTime = 0;
        }

        if (upPressed) {
            int dist = (int) (upPressedTime / 50);
            landerRelativeY -= dist * dist;
        }

        // Check if game is over
        if (hasLanderCollided()) {
            // Game is over!
            isGameOver = true;
            if (hasLanderLanded()) {
                JOptionPane.showMessageDialog(this, "You win!");
            } else {
                JOptionPane.showMessageDialog(this, "You lose!");
            }
        }
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    private boolean hasLanderLanded() {
        Rectangle box =
                new Polygon(getAdjustedLanderXs(), getAdjustedLanderYs(), LANDER_XS.length)
                        .getBounds();

        Point left = new Point((int) box.getMinX(), (int) box.getMaxY());
        Point right = new Point((int) box.getMaxX(), (int) box.getMaxY());

        for (int i = 0; i < GROUND_XS.length; i++) {
            if (i + 1 >= GROUND_XS.length)
                return false;

            if (GROUND_YS[i] != GROUND_YS[i + 1])
                continue;

            if (left.y < GROUND_YS[i])
                continue;

            if (left.x >= GROUND_XS[i] && right.x <= GROUND_XS[i + 1])
                return true;
            if (left.x <= GROUND_XS[i] && right.x >= GROUND_XS[i + 1])
                return true;
        }

        return false;
    }

    private boolean hasLanderCollided() {
        Rectangle box =
                new Polygon(getAdjustedLanderXs(), getAdjustedLanderYs(), LANDER_XS.length)
                        .getBounds();

        if (TERRAIN.contains(box.getMaxX(), box.getMaxY()))
            return true;
        if (TERRAIN.contains(box.getMaxX(), box.getMinY()))
            return true;
        if (TERRAIN.contains(box.getMinX(), box.getMaxY()))
            return true;
        if (TERRAIN.contains(box.getMinX(), box.getMinY()))
            return true;

        return false;
    }

    private int[] adjust(int[] arr, int amount) {
        int[] ret = new int[arr.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = arr[i] + amount;
        }

        return ret;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_KP_LEFT) {
            if (fuel > 0)
                rightPressed = true;
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_KP_RIGHT) {
            if (fuel > 0)
                leftPressed = true;
        }
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_KP_UP) {
            if (fuel > 0)
                upPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_KP_LEFT) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_KP_RIGHT) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_KP_UP) {
            upPressed = false;
            upPressedTime = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
