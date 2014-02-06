package com.hjwylde.uni.comp261.assignment05.systemSimulator;

import java.util.Arrays;

/*
 * Code for Assignment 5, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Main {
    
    /**
     * Number of buffers available in memory for sort-merge / merge-join.
     */
    public static final int NUMBER_BUFFERS = 3;
    
    /**
     * Verbose messages. Used for debugging.
     */
    public static final boolean VERBOSE = true;
    
    /**
     * Gets the next integer from <code>array</code> starting at <code>from</code>. First calculates
     * the length of the integer then calls <code>nextInt(char[], int, int)</code>.
     * 
     * @param array the array to get the int from.
     * @param from the start position.
     * @return the int.
     */
    public static int nextInt(char[] array, int from) {
        /*
         * Find length
         */
        int length = from;
        while ((length < array.length) && Character.isDigit(array[length]))
            length++;
        
        return Main.nextInt(array, from, length - from); // Get int.
    }
    
    /**
     * Gets the next integer from <code>array</code> starting at <code>from</code> going for
     * <code>length</code> digits.
     * 
     * @param array the array to get the int from.
     * @param from the start position.
     * @param length the length of the int in digits.
     * @return the int.
     */
    public static int nextInt(char[] array, int from, int length) {
        try { // Try convert...
            return (Integer.parseInt(new String(Arrays.copyOfRange(array, from,
                from + length))));
        } catch (NumberFormatException e) {
            return 0; // Error!
        }
    }
    
    /**
     * Converts the integer to a character array of the exact length.
     * 
     * @param i the int to convert.
     * @return a char[] array of the int.
     */
    public static char[] toCharArray(int i) {
        return String.format("%d", i).toCharArray();
    }
    
    /**
     * Converts the integer to a character array 0 padded of length <code>length</code>.
     * 
     * @param i the int to convert.
     * @param length the length of the int (for truncation or 0 padding).
     * @return a char[] array of the int.
     */
    public static char[] toCharArray(int i, int length) {
        return String.format("%0" + length + "d", i).toCharArray();
    }
    
    /**
     * Converts the string into a character array of the specified <code>length</code>.
     * 
     * @param str the string to convert.
     * @param length the length of the string (for truncation or padding).
     * @return a char[] array of the string.
     */
    public static char[] toCharArray(String str, int length) {
        return String.format("%" + length + "s", str).toCharArray();
    }
    
    /**
     * Output a non error message. Does not display anything if <code>VERBOSE</code> is set to
     * false.
     * 
     * @param msg the message to output.
     */
    public static void verbose(String msg) {
        Main.verbose(msg, false);
    }
    
    /**
     * Outputs the given message. If it is an error message, it will always output it, if not it
     * will
     * only output when <code>VERBOSE</code> is set to true.
     * 
     * @param msg the message.
     * @param err whether the message is an error.
     */
    public static void verbose(String msg, boolean err) {
        if (err)
            System.err.println("Error: " + msg);
        else if (Main.VERBOSE)
            System.out.println(msg);
    }
}