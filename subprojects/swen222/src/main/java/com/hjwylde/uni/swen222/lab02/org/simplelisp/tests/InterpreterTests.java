package com.hjwylde.uni.swen222.lab02.org.simplelisp.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.hjwylde.uni.swen222.lab02.org.simplelisp.compiler.Interpreter;
import com.hjwylde.uni.swen222.lab02.org.simplelisp.compiler.Parser;
import com.hjwylde.uni.swen222.lab02.org.simplelisp.error.ParseException;
import com.hjwylde.uni.swen222.lab02.org.simplelisp.lang.LispExpr;

public class InterpreterTests {

    // NOTE: whilst this list of unit tests should prove helpful, it's a very poor test suite as the
    // coverage obtained will be rather low.

    /**
     * Following method simply runs simple lisp and generates some output
     * 
     * @param input the LISP input.
     * @return the output.
     */
    public String run(String input) {
        Interpreter interpreter = new Interpreter();
        LispExpr root = null;
        try {
            root = Parser.parse(input);
        } catch (ParseException e) {
            fail(e.getMessage());
        }

        return interpreter.evaluate(root).toString();
    }

    @Test
    public void test1() {
        assertEquals(run("(+ 1 2)"), "3");
    }

    @Test
    public void test10() {
        assertEquals(run("(< 1 2)"), "t");
    }

    @Test
    public void test11() {
        assertEquals(run("(< 2 2)"), "nil");
    }

    @Test
    public void test12() {
        assertEquals(run("(= 2 2)"), "t");
    }

    @Test
    public void test13() {
        assertEquals(run("(= 2 (+ 1 2))"), "nil");
    }

    @Test
    public void test14() {
        assertEquals(run("(elt '(A B C) 1)"), "B");
    }

    @Test
    public void test15() {
        assertEquals(run("(reverse '(A B C))"), "(C B A)");
    }

    @Test
    public void test16() {
        assertEquals(run("(setq VAR \"Hello\") (stringp VAR)"), "t");
    }

    @Test
    public void test17() {
        assertEquals("89",
                run("(defun fib (x) (if (<= x 1) 1 (+ (fib (- x 1)) (fib (- x 2)))))(fib 10)"));
    }

    @Test
    public void test2() {
        assertEquals(run("(+ (* 2 3) (+ 3 3))"), "12");
    }

    @Test
    public void test3() {
        assertEquals(run("(car '(A B C))"), "A");
    }

    @Test
    public void test4() {
        assertEquals(run("(cons 'A '(B C))"), "(A B C)");
    }

    @Test
    public void test5() {
        assertEquals(run("(cons (car '(A B)) '(B C))"), "(A B C)");
    }

    @Test
    public void test6() {
        assertEquals(run("(length '(A B C))"), "3");
    }

    @Test
    public void test7() {
        assertEquals(run("(stringp \"Hello World\")"), "t");
    }

    @Test
    public void test8() {
        assertEquals(run("(integerp 1)"), "t");
    }

    @Test
    public void test9() {
        assertEquals(run("(integerp \"STRING\")"), "nil");
    }
}
