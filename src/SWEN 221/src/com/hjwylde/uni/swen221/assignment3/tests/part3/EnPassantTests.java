package com.hjwylde.uni.swen221.assignment3.tests.part3;

import junit.framework.TestCase;

import org.junit.Test;

import com.hjwylde.uni.swen221.assignment3.tests.TestHelpers;

/*
 * Code for Assignment 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class EnPassantTests extends TestCase {
    
    public @Test
    void testEnPassant() {
        String[][] tests = {
            // Test 1
            {
                "h2-h3 b7-b5\ng2-g3 b5-b4\na2-a4 b4xa3ep",
                "8|r|n|b|q|k|b|n|r|\n" + "7|p|_|p|p|p|p|p|p|\n"
                    + "6|_|_|_|_|_|_|_|_|\n" + "5|_|_|_|_|_|_|_|_|\n"
                    + "4|_|_|_|_|_|_|_|_|\n" + "3|p|_|_|_|_|_|P|P|\n"
                    + "2|_|P|P|P|P|P|_|_|\n" + "1|R|N|B|Q|K|B|N|R|\n"
                    + "  a b c d e f g h"
            },
            // Test 2
            {
                "a2-a4 h7-h6\na4-a5 b7-b5\na5xb6ep",
                "8|r|n|b|q|k|b|n|r|\n" + "7|p|_|p|p|p|p|p|_|\n"
                    + "6|_|P|_|_|_|_|_|p|\n" + "5|_|_|_|_|_|_|_|_|\n"
                    + "4|_|_|_|_|_|_|_|_|\n" + "3|_|_|_|_|_|_|_|_|\n"
                    + "2|_|P|P|P|P|P|P|P|\n" + "1|R|N|B|Q|K|B|N|R|\n"
                    + "  a b c d e f g h"
            }
        };
        TestHelpers.checkValidTests(tests);
    }
    
    public @Test
    void testInvalidEnPassant() {
        String[] tests = {
            "h2-h3 b7-b5\na2-a3 b5-b4\na3-a4 b4xa3ep",
            "a2-a4 b7-b6\na4-a5 b6-b5\na5xb6ep",
            "a2-a3 b7-b5\na3-a4 b5-b4\na3xb5ep",
            "a2-a4 c7-c6\na4-a5 Qd8-b6\nh2-h3 Qb6-b5\na5xb6ep"
        };
        
        TestHelpers.checkInvalidTests(tests);
    }
}
