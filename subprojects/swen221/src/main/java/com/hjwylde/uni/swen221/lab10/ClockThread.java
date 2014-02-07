package com.hjwylde.uni.swen221.lab10;

import java.awt.Frame;

/*
 * Code for Laboratory 10, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * The Clock Thread is responsible for producing a consistent "pulse" which is used to refresh the
 * display. Setting the pulse rate too high may cause problems, when the point is reached at which
 * the work done to service a given pulse exceeds the time between pulses.
 * 
 * @author djp
 * 
 */
public class ClockThread extends Thread {

    private final int delay; // delay between pulses in us
    private final Frame display;

    public ClockThread(int delay, Frame display) {
        this.delay = delay;
        this.display = display;
    }

    @Override
    public void run() {
        while (true)
            // Loop forever
            try {
                Thread.sleep(delay);
                if (display != null) {
                    display.invalidate();
                    display.repaint();
                }
            } catch (InterruptedException e) {
                // should never happen
            }
    }
}
