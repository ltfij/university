package com.hjwylde.uni.swen222.lab04.moon_lander.concurrency;

import com.hjwylde.uni.swen222.lab04.moon_lander.ui.LanderCanvas;

public class ClockThread extends Thread {
    
    private final LanderCanvas canvas;
    
    public ClockThread(LanderCanvas canvas) {
        this.canvas = canvas;
    }
    
    @Override
    public void run() {
        long cur = System.currentTimeMillis();
        while (true) {
            long elapsed = System.currentTimeMillis() - cur;
            
            canvas.update(elapsed);
            canvas.repaint();
            
            if (canvas.isGameOver())
                return;
            
            cur = System.currentTimeMillis();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
        }
    }
}
