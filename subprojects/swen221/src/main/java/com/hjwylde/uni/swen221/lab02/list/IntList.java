package com.hjwylde.uni.swen221.lab02.list;

/*
 * Code for Laboratory 2, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * An IntList represents a list of primitive ints.
 * 
 * @author djp
 */
public interface IntList {
    
    /**
     * Add an integer to the end of the list.
     */
    public void add(int x);
    
    /**
     * Empty the list
     */
    public void clear();
    
    /**
     * Return the integer at the given index;
     */
    public int get(int index);
    
    /**
     * Remove the integer at a given index from the list. The value returned is the value that was
     * at
     * that index.
     */
    public int remove(int index);
    
    /**
     * Assign a new integer at a given index; the old value at that index is returned.
     */
    public int set(int index, int value);
    
    /**
     * Return the number of arguments in the list.
     */
    public int size();
}
