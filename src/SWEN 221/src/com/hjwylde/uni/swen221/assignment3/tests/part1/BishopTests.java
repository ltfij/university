package com.hjwylde.uni.swen221.assignment3.tests.part1;

import junit.framework.TestCase;

import org.junit.Test;

import com.hjwylde.uni.swen221.assignment3.tests.TestHelpers;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class BishopTests extends TestCase {
    
    public @Test
    void testBishopMoves() {
        String[][] tests = {
            // Test 1
            {
                "e2-e3 e7-e6\nBf1-d3",
                "8|r|n|b|q|k|b|n|r|\n" + "7|p|p|p|p|_|p|p|p|\n"
                    + "6|_|_|_|_|p|_|_|_|\n" + "5|_|_|_|_|_|_|_|_|\n"
                    + "4|_|_|_|_|_|_|_|_|\n" + "3|_|_|_|B|P|_|_|_|\n"
                    + "2|P|P|P|P|_|P|P|P|\n" + "1|R|N|B|Q|K|_|N|R|\n"
                    + "  a b c d e f g h"
            },
            // Test 2
            {
                "d2-d3 d7-d6\nBc1-e3",
                "8|r|n|b|q|k|b|n|r|\n" + "7|p|p|p|_|p|p|p|p|\n"
                    + "6|_|_|_|p|_|_|_|_|\n" + "5|_|_|_|_|_|_|_|_|\n"
                    + "4|_|_|_|_|_|_|_|_|\n" + "3|_|_|_|P|B|_|_|_|\n"
                    + "2|P|P|P|_|P|P|P|P|\n" + "1|R|N|_|Q|K|B|N|R|\n"
                    + "  a b c d e f g h"
            },
            // Test 3
            {
                "e2-e3 e7-e6\nBf1-d3 Bf8-d6",
                "8|r|n|b|q|k|_|n|r|\n" + "7|p|p|p|p|_|p|p|p|\n"
                    + "6|_|_|_|b|p|_|_|_|\n" + "5|_|_|_|_|_|_|_|_|\n"
                    + "4|_|_|_|_|_|_|_|_|\n" + "3|_|_|_|B|P|_|_|_|\n"
                    + "2|P|P|P|P|_|P|P|P|\n" + "1|R|N|B|Q|K|_|N|R|\n"
                    + "  a b c d e f g h"
            },
            // Test 4
            {
                "d2-d3 d7-d6\nBc1-e3 Bc8-e6",
                "8|r|n|_|q|k|b|n|r|\n" + "7|p|p|p|_|p|p|p|p|\n"
                    + "6|_|_|_|p|b|_|_|_|\n" + "5|_|_|_|_|_|_|_|_|\n"
                    + "4|_|_|_|_|_|_|_|_|\n" + "3|_|_|_|P|B|_|_|_|\n"
                    + "2|P|P|P|_|P|P|P|P|\n" + "1|R|N|_|Q|K|B|N|R|\n"
                    + "  a b c d e f g h"
            }
        };
        TestHelpers.checkValidTests(tests);
    }
    
    public @Test
    void testBishopTakes() {
        String[][] tests = {
            // Test 1
            {
                "e2-e3 f7-f5\nBf1-d3 g7-g6\nBd3xf5",
                "8|r|n|b|q|k|b|n|r|\n" + "7|p|p|p|p|p|_|_|p|\n"
                    + "6|_|_|_|_|_|_|p|_|\n" + "5|_|_|_|_|_|B|_|_|\n"
                    + "4|_|_|_|_|_|_|_|_|\n" + "3|_|_|_|_|P|_|_|_|\n"
                    + "2|P|P|P|P|_|P|P|P|\n" + "1|R|N|B|Q|K|_|N|R|\n"
                    + "  a b c d e f g h"
            },
            // Test 2
            {
                "c2-c4 e7-e5\nc4-c5 Bf8xc5",
                "8|r|n|b|q|k|_|n|r|\n" + "7|p|p|p|p|_|p|p|p|\n"
                    + "6|_|_|_|_|_|_|_|_|\n" + "5|_|_|b|_|p|_|_|_|\n"
                    + "4|_|_|_|_|_|_|_|_|\n" + "3|_|_|_|_|_|_|_|_|\n"
                    + "2|P|P|_|P|P|P|P|P|\n" + "1|R|N|B|Q|K|B|N|R|\n"
                    + "  a b c d e f g h"
            },
            // Test 3
            {
                "f2-f4 e7-e5\ng2-g3 Bf8-c5\nNb1-c3 Bc5xNg1",
                "8|r|n|b|q|k|_|n|r|\n" + "7|p|p|p|p|_|p|p|p|\n"
                    + "6|_|_|_|_|_|_|_|_|\n" + "5|_|_|_|_|p|_|_|_|\n"
                    + "4|_|_|_|_|_|P|_|_|\n" + "3|_|_|N|_|_|_|P|_|\n"
                    + "2|P|P|P|P|P|_|_|P|\n" + "1|R|_|B|Q|K|B|b|R|\n"
                    + "  a b c d e f g h"
            },
            // Test 4
            {
                "e2-e4 e7-e5\nBf1-a6 Bf8-a3\nBa6xb7 Ba3xb2",
                "8|r|n|b|q|k|_|n|r|\n" + "7|p|B|p|p|_|p|p|p|\n"
                    + "6|_|_|_|_|_|_|_|_|\n" + "5|_|_|_|_|p|_|_|_|\n"
                    + "4|_|_|_|_|P|_|_|_|\n" + "3|_|_|_|_|_|_|_|_|\n"
                    + "2|P|b|P|P|_|P|P|P|\n" + "1|R|N|B|Q|K|_|N|R|\n"
                    + "  a b c d e f g h"
            }
        };
        
        TestHelpers.checkValidTests(tests);
    }
}
