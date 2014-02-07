package com.hjwylde.uni.comp261.assignment04.modeller;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.vecmath.Matrix4d;

/*
 * Code for Assignment 4, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class Main {

    public static final Matrix4d MATRIX4D_IDENTITY = Main.initIdentityMatrix();

    public static String ROOT_PATH = Main.class.getProtectionDomain().getCodeSource().getLocation()
            .getPath().replaceAll("%20", " ");

    /**
     * Constrain <code>val</code> between <code>min</code> and <code>max</code> ie.
     * 
     * <pre>
     * min &lt;= val &lt;= max
     * </pre>
     * 
     * @param val the value to constrain.
     * @param min the minimum value for val.
     * @param max the maximum value for val.
     * @return the constrained value.
     */
    public static double constrain(double val, double min, double max) {
        return Math.min(Math.max(val, min), max);
    }

    /**
     * Constrain <code>val</code> between <code>min</code> and <code>max</code> ie.
     * 
     * <pre>
     * min &lt;= val &lt;= max
     * </pre>
     * 
     * @param val the value to constrain.
     * @param min the minimum value for val.
     * @param max the maximum value for val.
     * @return the constrained value.
     */
    public static int constrain(int val, int min, int max) {
        return Math.min(Math.max(val, min), max);
    }

    /**
     * Display a Swing error message box with the specified <code>String</code> error message.
     * 
     * @param err The error message to display.
     */
    public static void displayError(String err) {
        JOptionPane.showMessageDialog(null, err, "Error", JOptionPane.ERROR_MESSAGE);
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

    /**
     * Creates an identity Matrix4d.
     * 
     * @return a new identity Matrix4d.
     */
    private static Matrix4d initIdentityMatrix() {
        Matrix4d m = new Matrix4d();
        m.setIdentity();

        return m;
    }
}
