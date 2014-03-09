package com.hjwylde.uni.swen221.assignment03.tests.part2;

import junit.framework.TestCase;

import org.junit.Test;

import com.hjwylde.uni.swen221.assignment03.tests.TestHelpers;

/*
 * Code for Assignment 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class InvalidQueenTests extends TestCase {

    public @Test
    void testInvalidQueenMoves() {
        String[] tests =
                {"Qd1-d4", "Qd1-f3", "Qd1-e3", "Qd1-e1", "e2-e4 d7-d5\nQd1-e2 d5xe4\nQe2-e6",
                        "e2-e4 d7-d5\nQd1-e2 d5xe4\nQe3-e2", "c2-c3 d7-d5\nQd1-b3 Qd8-d7\nQb3-e6"};
        TestHelpers.checkInvalidTests(tests);
    }

    public @Test
    void testInvalidQueenTakes() {
        String[] tests =
                {"Qd1xQd8", "Qd1xBc1", "e2-e4 d7-d5\nQd1-e2 d5xe4\nQe2xe6",
                        "e2-e4 d7-d5\nQd1-e2 d5xe4\nQe3xe2", "c2-c3 d7-d5\nQd1-b3 e7-e6\nQb3xe6"};

        TestHelpers.checkInvalidTests(tests);
    }
}
