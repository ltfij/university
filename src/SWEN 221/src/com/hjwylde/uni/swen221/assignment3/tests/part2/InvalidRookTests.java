package com.hjwylde.uni.swen221.assignment3.tests.part2;

import junit.framework.TestCase;

import org.junit.Test;

import com.hjwylde.uni.swen221.assignment3.tests.TestHelpers;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class InvalidRookTests extends TestCase {
    
    public @Test
    void testInvalidRookMoves() {
        String[] tests = {
            "a2-a4 a7-a6\nRa1-a4"
        };
        TestHelpers.checkInvalidTests(tests);
    }
    
    public @Test
    void testInvalidRookTakes() {
        String[] tests = {
            "Ra1xa2", "Ra2xh2", "Ra1xb1"
        };
        
        TestHelpers.checkInvalidTests(tests);
    }
}
