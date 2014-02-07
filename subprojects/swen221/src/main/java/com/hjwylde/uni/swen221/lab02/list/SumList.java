package com.hjwylde.uni.swen221.lab02.list;

import java.util.Arrays;

/*
 * Code for Laboratory 2, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Represents a list of integers and keeps the running total of them. If the list is empty, the sum
 * of the list is defined as 0.
 * 
 * @author Henry J. Wylde
 */
public class SumList implements IntList {

    private static final int INITIAL_SIZE = 10;

    private int[] data;
    private int count;

    private int sum;

    /**
     * Creates a sum list with the initial size <code>INITIAL_SIZE</code> and sets the sum to 0.
     */
    public SumList() {
        data = new int[SumList.INITIAL_SIZE];
        count = 0;

        sum = 0;
    }

    /*
     * @see lab2.list.IntList#add(int)
     */
    @Override
    public void add(int i) {
        ensureCapacity();
        data[count++] = i;

        sum += i;
    }

    /*
     * @see lab2.list.IntList#clear()
     */
    @Override
    public void clear() {
        data = new int[SumList.INITIAL_SIZE];
        count = 0;

        sum = 0;
    }

    /*
     * @see lab2.list.IntList#get(int)
     */
    @Override
    public int get(int index) {
        if ((index < 0) || (index >= count))
            throw new IndexOutOfBoundsException();

        return data[index];
    }

    /*
     * @see lab2.list.IntList#remove(int)
     */
    @Override
    public int remove(int index) {
        if ((index < 0) || (index >= count))
            throw new IndexOutOfBoundsException();

        int oldValue = data[index];

        System.arraycopy(data, index + 1, data, index, count - index - 1);
        count--;

        sum -= oldValue;

        return oldValue;
    }

    /*
     * @see lab2.list.IntList#set(int, int)
     */
    @Override
    public int set(int index, int value) {
        if ((index < 0) || (index >= count))
            throw new IndexOutOfBoundsException();

        int oldValue = data[index];
        data[index] = value;

        sum += (value - oldValue);

        return oldValue;
    }

    /*
     * @see lab2.list.IntList#size()
     */
    @Override
    public int size() {
        return count;
    }

    public int sum() {
        return sum;
    }

    /**
     * Ensure the data array has sufficient number of elements to add a new element.
     */
    private void ensureCapacity() {
        if (count < data.length)
            return;

        data = Arrays.copyOf(data, data.length * 2);
    }
}
