package com.hjwylde.uni.comp261.assignment01.geographicalMap;

import java.util.Locale;

import javax.swing.UIManager;

/*
 * Code for Assignment 1, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class Main {

    public static String ROOT_PATH = Main.class.getProtectionDomain().getCodeSource().getLocation()
            .getPath().replaceAll("%20", " ");

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Main.ROOT_PATH.substring(0, 1).equals("/"))
            Main.ROOT_PATH = Main.ROOT_PATH.substring(1);
        if (!Main.ROOT_PATH.endsWith("/"))
            Main.ROOT_PATH = Main.ROOT_PATH.substring(0, Main.ROOT_PATH.lastIndexOf("/"));

        new MainFrame();
    }

    public static double round(double d, int dp) {
        if (dp < 0)
            throw new IllegalArgumentException();

        double temp = d;
        double divisor = Math.pow(10, dp);
        temp *= divisor;
        temp = Math.round(temp);
        temp /= divisor;

        return temp;
    }

    public static String toTitleCase(String str) {
        if (str == null)
            return null;
        if (str.length() == 0)
            return "";

        String sentence = "";
        String[] words = str.trim().split(" ");
        for (String word : words)
            if (word.length() == 0)
                continue;
            else if (word.length() == 1)
                sentence += " " + word.toUpperCase(Locale.ENGLISH);
            else
                sentence +=
                        " " + word.substring(0, 1).toUpperCase(Locale.ENGLISH)
                                + word.substring(1).toLowerCase(Locale.ENGLISH);

        return sentence.trim();
    }
}
