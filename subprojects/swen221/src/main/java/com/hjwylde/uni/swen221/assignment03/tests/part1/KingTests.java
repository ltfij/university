package com.hjwylde.uni.swen221.assignment03.tests.part1;

import junit.framework.TestCase;

import org.junit.Test;

import com.hjwylde.uni.swen221.assignment03.tests.TestHelpers;

/*
 * Code for Assignment 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class KingTests extends TestCase {

    public @Test
    void testKingMoves() {
        String[][] tests =
                {
                        // Test 1
                        {
                                "e2-e4 d7-d5\nKe1-e2",
                                "8|r|n|b|q|k|b|n|r|\n" + "7|p|p|p|_|p|p|p|p|\n"
                                        + "6|_|_|_|_|_|_|_|_|\n" + "5|_|_|_|p|_|_|_|_|\n"
                                        + "4|_|_|_|_|P|_|_|_|\n" + "3|_|_|_|_|_|_|_|_|\n"
                                        + "2|P|P|P|P|K|P|P|P|\n" + "1|R|N|B|Q|_|B|N|R|\n"
                                        + "  a b c d e f g h"},
                        // Test 2
                        {
                                "e2-e4 d7-d5\nBf1-d3 d5xe4\nKe1-f1",
                                "8|r|n|b|q|k|b|n|r|\n" + "7|p|p|p|_|p|p|p|p|\n"
                                        + "6|_|_|_|_|_|_|_|_|\n" + "5|_|_|_|_|_|_|_|_|\n"
                                        + "4|_|_|_|_|p|_|_|_|\n" + "3|_|_|_|B|_|_|_|_|\n"
                                        + "2|P|P|P|P|_|P|P|P|\n" + "1|R|N|B|Q|_|K|N|R|\n"
                                        + "  a b c d e f g h"},
                        // Test 3
                        {
                                "e2-e4 d7-d5\nBf1-d3 Ke8-d7",
                                "8|r|n|b|q|_|b|n|r|\n" + "7|p|p|p|k|p|p|p|p|\n"
                                        + "6|_|_|_|_|_|_|_|_|\n" + "5|_|_|_|p|_|_|_|_|\n"
                                        + "4|_|_|_|_|P|_|_|_|\n" + "3|_|_|_|B|_|_|_|_|\n"
                                        + "2|P|P|P|P|_|P|P|P|\n" + "1|R|N|B|Q|K|_|N|R|\n"
                                        + "  a b c d e f g h"},
                        // Test 4
                        {
                                "e2-e4 d7-d5\nBf1-d3 Qd8-d6\ne4xd5 Ke8-d8",
                                "8|r|n|b|k|_|b|n|r|\n" + "7|p|p|p|_|p|p|p|p|\n"
                                        + "6|_|_|_|q|_|_|_|_|\n" + "5|_|_|_|P|_|_|_|_|\n"
                                        + "4|_|_|_|_|_|_|_|_|\n" + "3|_|_|_|B|_|_|_|_|\n"
                                        + "2|P|P|P|P|_|P|P|P|\n" + "1|R|N|B|Q|K|_|N|R|\n"
                                        + "  a b c d e f g h"}};
        TestHelpers.checkValidTests(tests);
    }
}
