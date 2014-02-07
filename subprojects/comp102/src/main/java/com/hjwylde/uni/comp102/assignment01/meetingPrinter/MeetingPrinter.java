package com.hjwylde.uni.comp102.assignment01.meetingPrinter;

/*
 * Code for Assignment 1, COMP 102 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A very simple program that prints out a name in two different ways
 */
public class MeetingPrinter {

    /**
     * Print a nametag with your name inside a box made of asterisks
     */
    public void printNameTag() {
        String name = "Henry Wylde";
        System.out.println("*********************************");
        System.out.println("*                               *");
        System.out.println("*  HELLO, my name is            *");
        System.out.println("*                               *");
        System.out.println("*          " + name + "             *");
        System.out.println("*                               *");
        System.out.println("*********************************");
    }

    /**
     * Print out a reminder of a speaker's presentation time
     * 
     * @param name the name.
     * @param time the time.
     */
    public void printNotice(String name, int time) {
        System.out.println();
        System.out.println(name + ":");
        System.out.println("Your presentation will be at 10:" + time + " for three minutes");
    }
}
