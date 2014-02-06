package com.hjwylde.uni.swen221.assignment03.tests.part2;

import junit.framework.TestCase;

import org.junit.Test;

import com.hjwylde.uni.swen221.assignment03.tests.TestHelpers;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class InvalidKingTests extends TestCase {
    
    public @Test
    void testInvalidKingMoves() {
        String[] tests = {
            "Ke1-e3", "Ke1-e2", "Ke1-d2", "e2-e4 e7-e6\nKe1-e3",
            "d2-d4 e7-e6\nKe1-c3", "e2-e4 e7-e6\nKe1-e2 e6-e5\nKe1-e2"
        };
        TestHelpers.checkInvalidTests(tests);
    }
    
    public @Test
    void testInvalidKingTakes() {
        String[] tests = {
            "Ke1xe7", "Ke1xe2", "Ke1xd2", "e2-e4 e7-e6\nKe1xe2",
            "e2-e4 b7-b5\nKe1-e2 c7-c6\nKe2xb5"
        };
        
        TestHelpers.checkInvalidTests(tests);
    }
}
