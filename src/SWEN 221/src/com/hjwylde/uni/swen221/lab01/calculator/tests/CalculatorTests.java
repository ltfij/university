package com.hjwylde.uni.swen221.lab01.calculator.tests;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.lab01.calculator.Calculator;

/*
 * Code for Laboratory 1, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class CalculatorTests {
    
    @Test
    public void addTests() {
        CalculatorTests.test("1+1", "2");
        CalculatorTests.test("(1+1) + 89", "91");
        CalculatorTests.test("(1+(1 + 1))", "3");
        CalculatorTests.test("2+0.387", "2.387");
        CalculatorTests.test("3+47", "50");
        CalculatorTests.test("4.01+7723.089", "7727.099");
        CalculatorTests.test("0.5+4783", "4783.5");
        CalculatorTests.test("0.0 + 12387", "12387.0");
        CalculatorTests.test("0.00123+ 1", "1.00123");
    }
    
    @Test
    public void divTests() {
        CalculatorTests.test("1/1", "1");
        CalculatorTests.test("2/0.5", "4");
        CalculatorTests.test("3/4", "0.75");
        CalculatorTests.test("400/7", "57.1428571429");
        CalculatorTests.test("0.5/4783", "0.0001045369");
        CalculatorTests.test("0.00923/1", "0.00923");
    }
    
    @Test
    public void mulTests() {
        CalculatorTests.test("3*3", "9");
        CalculatorTests.test("2*0.387", "0.774");
        CalculatorTests.test("3*47", "141");
        CalculatorTests.test("4.01*7723.089", "30969.58689");
        CalculatorTests.test("0.5*4783", "2391.5");
        CalculatorTests.test("0.0 * 12387", "0.0");
        CalculatorTests.test("0.00123*1", "0.00123");
    }
    
    @Test
    public void numberTests() {
        CalculatorTests.test("3");
        CalculatorTests.test("    12387", "12387");
    }
    
    @Test
    public void realNumberTests() {
        CalculatorTests.test("0.00137");
        CalculatorTests.test("0.6789723");
        CalculatorTests.test("1.22345");
    }
    
    @Test
    public void subTests() {
        CalculatorTests.test("1-1", "0");
        CalculatorTests.test("2-0.387", "1.613");
        CalculatorTests.test("3-47", "-44");
        CalculatorTests.test("4.01-7723.089", "-7719.079");
        CalculatorTests.test("0.5-4783", "-4782.5");
        CalculatorTests.test("0.0 - 12387", "-12387.0");
        CalculatorTests.test("0.00123- 1", "-0.99877");
    }
    
    private static void test(String input) {
        CalculatorTests.test(input, input);
    }
    
    private static void test(String input, String output) {
        BigDecimal out = new Calculator(input).evaluate();
        BigDecimal num = new BigDecimal(output);
        if (num.compareTo(out) != 0)
            Assert.fail(input + " => " + out + ", not " + output);
    }
}
