package com.hjwylde.uni.comp103.assignment7.organisationChart;

/*
 * Code for Assignment 7, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Job {
    
    String title;
    
    /*
     * Constructors
     */
    
    public Job() {
        title = "";
    }
    
    public Job(String title) {
        this.title = title;
    }
    
    /*
     * Getters
     */
    
    public String getTitle() {
        return title;
    }
    
    /*
     * Setters
     */
    
    public void setTitle(String title) {
        this.title = title;
    }
}
