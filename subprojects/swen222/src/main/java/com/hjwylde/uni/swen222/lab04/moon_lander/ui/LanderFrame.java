package com.hjwylde.uni.swen222.lab04.moon_lander.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.hjwylde.uni.swen222.lab04.moon_lander.concurrency.ClockThread;

public class LanderFrame extends JFrame {
    
    private final LanderCanvas canvas;
    
    public LanderFrame() {
        super("Moon Lander");
        
        canvas = new LanderCanvas();
        addKeyListener(canvas);
        setLayout(new BorderLayout());
        add(canvas, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(true);
        setVisible(true);
        canvas.setDoubleBuffered(true);
        
        new ClockThread(canvas).start();
    }
}