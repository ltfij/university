package com.hjwylde.uni.swen221.lab02.shapes.viewer;

/*
 * Code for Laboratory 2, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * The Clock Thread is responsible for producing a consistent "pulse" which is used to update the
 * game state, and refresh the display. Setting the pulse rate too high may cause problems, when the
 * point is reached at which the work done to service a given pulse exceeds the time between pulses.
 * 
 * @author djp
 * 
 */
public class ClockThread extends Thread {

    private final int delay; // delay between ticks in us
    private final BoardFrame display;

    public ClockThread(int delay, BoardFrame display) {
        this.delay = delay;
        this.display = display;
    }

    @Override
    public void run() {
        while (true)
            // Loop forever
            try {
                Thread.sleep(delay);
                display.clockTick();
            } catch (InterruptedException e) {
                // should never happen
            }
    }
}
