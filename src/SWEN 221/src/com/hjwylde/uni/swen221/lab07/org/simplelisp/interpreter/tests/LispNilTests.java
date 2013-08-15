package com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.tests;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.LispExpr;
import com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.LispNil;
import com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.LispTrue;

/*
 * Code for Laboratory 7, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Test the lispNil class. Should have 100% coverage.
 * 
 * @author Nick
 * 
 */
public class LispNilTests {
    
    @Test
    public void testEquals() {
        Assert.assertTrue(new LispNil().equals(new LispNil()));
        Assert.assertFalse(new LispNil().equals(new LispTrue()));
    }
    
    @Test
    public void testEvaluate() {
        Assert.assertEquals(new LispNil().evaluate(
            new HashMap<String, LispExpr>(), new HashMap<String, LispExpr>()),
            new LispNil());
    }
    
    @Test
    public void testToString() {
        Assert.assertEquals("nil", new LispNil().toString());
    }
}
