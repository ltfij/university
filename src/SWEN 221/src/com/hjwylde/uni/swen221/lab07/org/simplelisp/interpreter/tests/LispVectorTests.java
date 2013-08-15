package com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.*;

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
public class LispVectorTests {
    
    @Test
    public void testEquals() {
        LispVector empty = new LispVector();
        LispVector nonEmpty = new LispVector();
        LispVector nonEmpty2 = new LispVector();
        LispVector nonEmpty3 = new LispVector();
        nonEmpty.add(new LispString("boo"));
        nonEmpty.add(new LispInteger(2));
        nonEmpty2.add(new LispString("boo"));
        nonEmpty2.add(new LispInteger(3));
        nonEmpty3.add(new LispString("boo"));
        nonEmpty3.add(new LispInteger(2));
        
        Assert.assertFalse(empty.equals(new LispNil()));
        Assert.assertTrue(empty.equals(empty));
        Assert.assertFalse(empty.equals(nonEmpty));
        Assert.assertTrue(nonEmpty.equals(nonEmpty3));
        Assert.assertFalse(nonEmpty.equals(nonEmpty2));
    }
    
    @Test
    public void testEvaluate() {
        Assert.assertEquals(new LispVector().evaluate(
            new HashMap<String, LispExpr>(), new HashMap<String, LispExpr>()),
            new LispVector());
    }
    
    @Test
    public void testGet() {
        LispVector nonEmpty = new LispVector();
        nonEmpty.add(new LispString("boo"));
        nonEmpty.add(new LispInteger(2));
        
        Assert.assertTrue(nonEmpty.get(0).equals(new LispString("boo")));
        Assert.assertTrue(nonEmpty.get(1).equals(new LispInteger(2)));
        Assert.assertFalse(nonEmpty.get(0).equals(new LispInteger(2)));
        Assert.assertTrue(nonEmpty.elt(0).equals(new LispString("boo")));
        Assert.assertTrue(nonEmpty.elt(1).equals(new LispInteger(2)));
        Assert.assertFalse(nonEmpty.elt(0).equals(new LispInteger(2)));
    }
    
    @Test
    public void testIterator() {
        ArrayList<LispExpr> l = new ArrayList<>();
        l.add(new LispString("boo"));
        l.add(new LispInteger(2));
        LispVector nonEmpty = new LispVector(l);
        
        Iterator<LispExpr> it = nonEmpty.iterator();
        Assert.assertEquals(it.next(), new LispString("boo"));
        Assert.assertEquals(it.next(), new LispInteger(2));
    }
    
    @Test
    public void testLength() {
        LispVector nonEmpty = new LispVector();
        nonEmpty.add(new LispString("boo"));
        nonEmpty.add(new LispInteger(2));
        
        Assert.assertEquals(nonEmpty.length(), new LispInteger(2));
    }
    
    @Test
    public void testSize() {
        LispVector nonEmpty = new LispVector();
        nonEmpty.add(new LispString("boo"));
        nonEmpty.add(new LispInteger(2));
        
        Assert.assertEquals(nonEmpty.size(), 2);
    }
    
    @Test
    public void testSubseq() {
        LispVector nonEmpty = new LispVector();
        nonEmpty.add(new LispString("boo"));
        nonEmpty.add(new LispInteger(2));
        ArrayList<LispExpr> l = new ArrayList<>();
        l.add(new LispInteger(2));
        
        Assert.assertEquals(new LispVector(), nonEmpty.subseq(1, 1));
        Assert.assertEquals(new LispVector(), nonEmpty.subseq(0, 0));
        Assert.assertEquals(new LispVector(l), nonEmpty.subseq(1, 2));
    }
    
    @Test
    public void testToString() {
        Assert.assertEquals("#()", new LispVector().toString());
    }
}
