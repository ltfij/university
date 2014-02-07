package com.hjwylde.uni.swen221.lab10.swen221.concurrent;

/*
 * Code for Laboratory 10, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A job represents a unit of work which can be executed on a ThreadPool. To perform a parallel
 * computation, the task needs to be divided up into a number of small jobs which are then submitted
 * to the thread pool.
 * 
 * @author David J. Pearce
 */
public abstract class Job implements Runnable {

    /**
     * Used to know that this thread is finished.
     */
    private volatile boolean finished;

    /**
     * Execute this job on a given thread.
     */
    @Override
    public abstract void run();

    public synchronized void start() {
        run();
        finished = true;
        notify();
    }

    /**
     * Force the thread which calls this method to block until this job has been finished.
     */
    public synchronized void waitUntilFinished() {
        while (!finished)
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
    }
}
