package com.hjwylde.uni.swen221.assignment03.tests.part2;

import junit.framework.TestCase;

import org.junit.Test;

import com.hjwylde.uni.swen221.assignment03.tests.TestHelpers;

/*
 * Code for Assignment 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class InvalidBishopTests extends TestCase {

    public @Test
    void testInvalidBishopMoves() {
        String[] tests =
                {"Bc1-c3", "Bc1-e3", "Bc1-b3", "c2-c3 e7-e6\nBc1-c2", "c2-c3 Bc8-c6",
                        "c2-c3 Bc8-e6", "c2-c3 e7-e6\nd2-d4 Bf8-c5\nBc1-d2 Bc5-e3",};
        TestHelpers.checkInvalidTests(tests);
    }

    public @Test
    void testInvalidBishopTakes() {
        String[] tests =
                {"Bc1xc7", "Bc1xh6", "Bc1xd2", "d2-d3 e7-e6\nBc1-f4 f7-f6\nBf4xNb8",
                        "c2-c3 e7-e6\nd2-d4 Bf8-c5\nBc1-d2 Bc5xf2"};

        TestHelpers.checkInvalidTests(tests);
    }
}
