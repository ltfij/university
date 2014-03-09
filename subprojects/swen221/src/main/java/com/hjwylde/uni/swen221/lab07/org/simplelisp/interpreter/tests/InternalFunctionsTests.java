package com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.tests;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.InternalFunctions;
import com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.Interpreter;
import com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.LispExpr;
import com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.Parser;

/*
 * Code for Laboratory 7, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Test the lispNil class. Should have 100% coverage.
 * 
 * @author Nick
 */
public class InternalFunctionsTests {

    @Test
    public void test() {
        Interpreter i = new Interpreter();
        InternalFunctions.setup_internals(i);

        LispExpr e = Parser.parse("(+ 3 3)");
        Assert.assertTrue(i.evaluate(e).toString().equalsIgnoreCase("6"));
        LispExpr e2 = Parser.parse("(- 3 3)");
        Assert.assertTrue(i.evaluate(e2).toString().equalsIgnoreCase("0"));
        LispExpr e3 = Parser.parse("(/ 3 3)");
        Assert.assertTrue(i.evaluate(e3).toString().equalsIgnoreCase("1"));
        LispExpr e4 = Parser.parse("(* 3 3)");
        Assert.assertTrue(i.evaluate(e4).toString().equalsIgnoreCase("9"));
        LispExpr e5 = Parser.parse("(< 3 3)");
        Assert.assertFalse(i.evaluate(e5).toString().equalsIgnoreCase("t"));
        LispExpr e6 = Parser.parse("(> 3 3)");
        Assert.assertFalse(i.evaluate(e6).toString().equalsIgnoreCase("t"));
        LispExpr e7 = Parser.parse("(= 3 3)");
        Assert.assertTrue(i.evaluate(e7).toString().equalsIgnoreCase("t"));
        LispExpr e8 = Parser.parse("(<= 3 3)");
        Assert.assertTrue(i.evaluate(e8).toString().equalsIgnoreCase("t"));
        LispExpr e9 = Parser.parse("(>= 3 3)");
        Assert.assertTrue(i.evaluate(e9).toString().equalsIgnoreCase("t"));
        LispExpr e10 = Parser.parse("(/= 3 3)");
        Assert.assertFalse(i.evaluate(e10).toString().equalsIgnoreCase("t"));
        LispExpr e11 = Parser.parse("(% 4 3)");
        Assert.assertTrue(i.evaluate(e11).toString().equalsIgnoreCase("1"));
    }
}
