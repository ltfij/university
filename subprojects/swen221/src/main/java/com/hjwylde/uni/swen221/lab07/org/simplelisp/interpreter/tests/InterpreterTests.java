package com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.tests;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter.*;

/*
 * Code for Laboratory 7, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * test the interpreter class
 * 
 * @author Nick
 * 
 */
public class InterpreterTests {

    // test the error method
    @Test
    public void testError() {
        try {
            new Interpreter().error("test message");
            Assert.fail("Should have thrown an error");
        } catch (Error e) {
            Assert.assertEquals("incorrect error message", "test message", e.getMessage());
        }
    }

    // test the evaluate method
    @Test
    public void testEvaluate() {
        try {
            LispExpr result = new Interpreter().evaluate(new LispNil());
            Assert.assertEquals(new LispNil(), result);
        } catch (Error e) {
            Assert.fail("Error in evaluation: " + e.getMessage());
        }

        try {
            String s;
            s = "(+ 2 (* 0 (+ 1 3)))";
            Assert.assertEquals(new Interpreter().evaluate(Parser.parse(s)).toString(), "2");
            s = "(+ 2 (* (- 0 1) (- (- 0 1) 3)))";
            Assert.assertEquals(new Interpreter().evaluate(Parser.parse(s)).toString(), "6");
            s = "'(+ 2 (* 0 (+ 1 3)))";
            Assert.assertEquals(new Interpreter().evaluate(Parser.parse(s)).toString(),
                    "(+ 2 (* 0 (+ 1 3)))");
            s = "(print (+ 2 (* 0 (+ 1 3))))";
            Assert.assertEquals(new Interpreter().evaluate(Parser.parse(s)).toString(), "nil");
            s = "\"boo\"";
            Assert.assertEquals(new Interpreter().evaluate(Parser.parse(s)).toString(), "boo");
        } catch (Error e) {
            Assert.fail("Error in evaluation: " + e.getMessage());
        }
    }

    // test the type_check method
    @Test
    public void testType_check() {
        // test an empty input

        try {
            new Interpreter().type_check("test", new LispExpr[] {});
        } catch (Error e) {
            Assert.fail("Error in type checking: " + e.getMessage());
        }

        // test for non-empty input
        try {
            new Interpreter().type_check("test", new LispExpr[] {new LispString("boo"),
                    new LispInteger(5)}, LispString.class, LispInteger.class);
        } catch (Error e) {
            Assert.fail("Error in type checking: " + e.getMessage());
        }

        try {
            new Interpreter().type_check("test", new LispExpr[] {new LispString("boo")},
                    LispString.class, LispInteger.class);
            Assert.fail("Should have failed");
        } catch (Error e) {
        }

        try {
            new Interpreter().type_check("test", new LispExpr[] {new LispString("boo")},
                    LispInteger.class);
            Assert.fail("Should have failed");
        } catch (Error e) {
        }

        try {
            new Interpreter().type_check("test", new LispExpr[] {new LispString("boo")},
                    LispString.class);
        } catch (Error e) {
            Assert.fail("Error in type checking: " + e.getMessage());
        }

        try {
            new Interpreter().type_check("test", new LispExpr[] {new LispString("boo"),
                    new LispChar('c')}, LispString.class, LispChar.class, LispNil.class);
            Assert.fail("Expected error.");
        } catch (Error e) {
        }

        try {
            new Interpreter().type_check("test", new LispExpr[] {});
        } catch (Error e) {
            Assert.fail("Fail");
        }

        LispChar c1 = new LispChar('a');
        LispChar c2 = new LispChar('b');

        try {
            new Interpreter().type_check("test", new LispExpr[] {c1}, LispChar.class);
        } catch (Error e) {
            Assert.fail("Fail");
        }
        try {
            new Interpreter()
                    .type_check("test", new LispExpr[] {c1}, LispChar.class, LispNil.class);
            Assert.fail("Fail");
        } catch (Error e) {
        }
        try {
            new Interpreter().type_check("test", new LispExpr[] {c1, c2}, LispChar.class,
                    LispChar.class, LispNil.class);
            Assert.fail("Fail");
        } catch (Error e) {
        }
    }
}
