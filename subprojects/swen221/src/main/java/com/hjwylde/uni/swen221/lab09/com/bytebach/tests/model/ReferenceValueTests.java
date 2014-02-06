package com.hjwylde.uni.swen221.lab09.com.bytebach.tests.model;

import java.awt.Point;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.IntegerValue;
import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.ReferenceValue;

/*
 * Code for Laboratory 9, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class ReferenceValueTests {
    
    @Test
    public void testHashcode() {
        ReferenceValue rv = new ReferenceValue("table", 2);
        Assert.assertTrue(rv.hashCode() != 0);
    }
    
    @Test
    public void testInvalidConstructor() {
        try {
            new ReferenceValue("table", new Point(2, 3));
            Assert.fail();
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void testToString() {
        ReferenceValue rv = new ReferenceValue("table", 2);
        
        Assert.assertTrue(rv.toString() != null);
    }
    
    @Test
    public void testValidConstructor() {
        new ReferenceValue("table", new Integer(0), "Nuhuh", true,
            new ReferenceValue("table", 2));
        new ReferenceValue("table", new IntegerValue(2));
    }
    
    @Test
    public void testValidEquals() {
        ReferenceValue rv = new ReferenceValue("table", 2);
        
        Assert.assertTrue(rv.equals(new ReferenceValue("table", 2)));
        Assert.assertFalse(rv.equals(new IntegerValue(2)));
        Assert.assertFalse(rv.equals(new ReferenceValue("table2", 2)));
        Assert.assertFalse(rv.equals(new ReferenceValue("table", 3)));
        Assert.assertFalse(rv.equals(new ReferenceValue("table2", 3)));
    }
    
    @Test
    public void testValidKeys() {
        ReferenceValue rv = new ReferenceValue("table", 2);
        Assert.assertTrue(rv.keys().length == 1);
    }
    
    @Test
    public void testValidTable() {
        ReferenceValue rv = new ReferenceValue("table", 2);
        
        Assert.assertTrue(rv.table().equals("table"));
    }
}
