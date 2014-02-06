package com.hjwylde.uni.swen221.lab09.com.bytebach.tests.model;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.BooleanValue;
import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.IntegerValue;

/*
 * Code for Laboratory 9, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class BooleanValueTests {
    
    @Test
    public void testValidEquals() {
        Assert
            .assertTrue(new BooleanValue(true).equals(new BooleanValue(true)));
        Assert.assertTrue(new BooleanValue(false)
            .equals(new BooleanValue(false)));
        Assert.assertTrue(!(new BooleanValue(false).equals(new BooleanValue(
            true))));
        Assert.assertFalse(new BooleanValue(false).equals(new IntegerValue(2)));
    }
    
    @Test
    public void testValidHashcode() {
        Assert.assertTrue(new BooleanValue(true).hashCode() == 1);
        Assert.assertTrue(new BooleanValue(false).hashCode() == 0);
    }
    
    @Test
    public void testValidToString() {
        Assert.assertTrue(new BooleanValue(true).toString().equals("true"));
        Assert.assertTrue(new BooleanValue(false).toString().equals("false"));
    }
    
    @Test
    public void testValidValue() {
        Assert.assertTrue(new BooleanValue(true).value() == true);
        Assert.assertTrue(new BooleanValue(false).value() == false);
    }
}
