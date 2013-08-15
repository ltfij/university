package com.hjwylde.uni.comp261.assignment2.stringSearch;

import javax.swing.UIManager;

/*
 * Code for Assignment 2, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Main {
    
    public static String ROOT_PATH = Main.class.getProtectionDomain()
        .getCodeSource().getLocation().getPath().replaceAll("%20", " ");
    
    public static int getSign(double d) {
        if (d < 0)
            return -1;
        else if (d > 0)
            return 1;
        
        return 0;
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        if (Main.ROOT_PATH.substring(0, 1).equals("/"))
            Main.ROOT_PATH = Main.ROOT_PATH.substring(1);
        if (!Main.ROOT_PATH.endsWith("/"))
            Main.ROOT_PATH = Main.ROOT_PATH.substring(0,
                Main.ROOT_PATH.lastIndexOf("/"));
        
        new AssignmentTasks();
    }
}
