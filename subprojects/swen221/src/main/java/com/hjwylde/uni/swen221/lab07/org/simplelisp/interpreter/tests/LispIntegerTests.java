package com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.tests;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.LispExpr;
import com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.LispInteger;
import com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.LispString;

/*
 * Code for Laboratory 7, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Test the lispNil class. Should have 100% coverage.
 * 
 * @author Nick
 * 
 */
public class LispIntegerTests {

    @Test
    public void testEquals() {
        Assert.assertFalse(new LispInteger(1).equals(new LispInteger(2)));
        Assert.assertTrue(new LispInteger(2).equals(new LispInteger(2)));
        Assert.assertFalse(new LispInteger(1).equals(new LispString("2")));
    }

    @Test
    public void testEvaluate() {
        Assert.assertEquals(new LispInteger(2).evaluate(new HashMap<String, LispExpr>(),
                new HashMap<String, LispExpr>()), new LispInteger(2));
    }

    @Test
    public void testToString() {
        Assert.assertTrue(new LispInteger(2).toString().equals("2"));
    }

    @Test
    public void testValue() {
        Assert.assertTrue(new LispInteger(2).value() == 2);
    }
}
