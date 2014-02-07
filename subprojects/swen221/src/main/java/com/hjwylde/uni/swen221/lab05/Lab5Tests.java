package com.hjwylde.uni.swen221.lab05;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.lab05.Solution.DuplicateIdException;
import com.hjwylde.uni.swen221.lab05.Solution.MissingDataException;

/*
 * Code for Laboratory 5, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * <p>
 * Tests for your solution. The solution should be in a class called Solution and should initialise
 * itself in the constructor.
 * </p>
 * <p>
 * <b>Do not modify the tests!</b>
 * </p>
 * 
 * @author ncameron
 */
public class Lab5Tests {

    /**
     * @throws IOException if error.
     */
    @Test
    public void testAddRow() throws IOException {
        Lab5 sol = new Solution();
        try {
            sol.addRow(new Identifier("Lynne", "b"), new int[] {11, 17, 67, 5, 39, 60, 82, 49, 16});
            Assert.assertArrayEquals(sol.getRow(new Identifier("Lynne", "b")), new int[] {11, 17,
                    67, 5, 39, 60, 82, 49, 16});
            sol.addRow(new Identifier("Ella", "c"), new int[] {0, 17, 37, 5, 39, 62, 82, 49, 16});
            Assert.assertArrayEquals(sol.getRow(new Identifier("Ella", "c")), new int[] {0, 17, 37,
                    5, 39, 62, 82, 49, 16});
        } catch (DuplicateIdException e) {
            Assert.assertTrue(false);
        } catch (MissingDataException e) {
            Assert.assertTrue(false);
        }
        try {
            sol.addRow(new Identifier("Angelo", "a"), new int[] {});
            Assert.assertTrue(false);
        } catch (DuplicateIdException e) {
        }
        try {
            sol.addRow(new Identifier("Rongo", "d"), new int[] {});
            Assert.assertTrue(false);
        } catch (DuplicateIdException e) {
        }
    }

    /**
     * @throws IOException if error.
     */
    @Test
    public void testGetRow() throws IOException {
        Lab5 sol = new Solution();

        try {
            int[] ret = sol.getRow(new Identifier("Ella", "b"));
            Assert.assertArrayEquals(ret, new int[] {11, 17, 67, 65, 39, 60, 81, 49, 86});
            ret = sol.getRow(new Identifier("Daniel", "a"));
            Assert.assertArrayEquals(ret, new int[] {57, 47, 25, 87, 3, 42, 6, 62, 57});
            ret = sol.getRow(new Identifier("Chang", "d"));
            Assert.assertArrayEquals(ret, new int[] {52, 58, 73, 22, 42, 50, 2, 14, 17});
        } catch (MissingDataException e) {
            Assert.assertTrue(false);
        }
        try {
            sol.getRow(new Identifier("Ella", "c"));
            Assert.assertTrue(false);
        } catch (MissingDataException e) {
        }
        try {
            sol.getRow(new Identifier("fsdgsdfg", ""));
            Assert.assertTrue(false);
        } catch (MissingDataException e) {
        }
        try {
            sol.getRow(new Identifier("Amelia", "e"));
            Assert.assertTrue(false);
        } catch (MissingDataException e) {
        }
    }

    /**
     * @throws IOException if error.
     */
    @Test
    public void testGetRowAverage() throws IOException {
        Lab5 sol = new Solution();

        try {
            int ret = sol.getRowAverage(new Identifier("Vladimir", "b"));
            Assert.assertEquals(ret, 64);
            ret = sol.getRowAverage(new Identifier("Isabella", "c"));
            Assert.assertEquals(ret, 25);
            ret = sol.getRowAverage(new Identifier("Amit", "a"));
            Assert.assertEquals(ret, 43);
        } catch (MissingDataException e) {
            Assert.assertTrue(false);
        }
        try {
            sol.getRowAverage(new Identifier("Bo", "a"));
            Assert.assertTrue(false);
        } catch (MissingDataException e) {
        }

    }

    /**
     * @throws IOException if error.
     */
    @Test
    public void testGetRowId() throws IOException {
        Lab5 sol = new Solution();

        try {
            Identifier ret = sol.getRowId(0);
            Assert.assertTrue(ret.equals(new Identifier("Ali", "a")));
            ret = sol.getRowId(17);
            Assert.assertTrue(ret.equals(new Identifier("Liam", "b")));
            ret = sol.getRowId(38);
            Assert.assertTrue(ret.equals(new Identifier("William", "d")));
        } catch (IndexOutOfBoundsException e) {
            Assert.assertTrue(false);
        }
        try {
            sol.getRowId(-1);
            Assert.assertTrue(false);
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            sol.getRowId(Integer.MAX_VALUE);
            Assert.assertTrue(false);
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            sol.getRowId(39);
            Assert.assertTrue(false);
        } catch (IndexOutOfBoundsException e) {
        }

    }

    /**
     * @throws IOException if error.
     */
    @Test
    public void testGetRowTotal() throws IOException {
        Lab5 sol = new Solution();

        try {
            int ret = sol.getRowTotal(new Identifier("Liam", "a"));
            Assert.assertEquals(ret, 468);
            ret = sol.getRowTotal(new Identifier("Benjamin", "b"));
            Assert.assertEquals(ret, 669);
            ret = sol.getRowTotal(new Identifier("Taonga", "a"));
            Assert.assertEquals(ret, 429);
        } catch (MissingDataException e) {
            Assert.assertTrue(false);
        }
        try {
            sol.getRowTotal(new Identifier("Taonga", "c"));
            Assert.assertTrue(false);
        } catch (MissingDataException e) {
        }

    }
}
