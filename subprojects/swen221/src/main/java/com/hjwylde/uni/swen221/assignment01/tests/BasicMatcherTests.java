package com.hjwylde.uni.swen221.assignment01.tests;

import java.util.regex.PatternSyntaxException;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.assignment01.BasicMatcher;

/*
 * Code for Assignment 1, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A very incomplete set of tests for the methods of BasicMatcher.
 * You should improve test coverage by writing 50-100% more tests.
 * You should also write tests for the match method (there are none here).
 * Remember to test edge cases!
 */
public class BasicMatcherTests extends BasicMatcher {
    
    /**
     * Some strings of length 1 that do not start with 'x'.
     */
    static final String[] SHORT_STRINGS = {
        "a", "y", "6", " ", "."
    };
    
    /**
     * Some strings of length greater than 1 that do not start with 'c'.
     */
    static final String[] LONG_STRINGS = {
        "yertyerty", "6ertyerty", " ertyety"
    };
    
    @Test
    public void matchCharDot() {
        for (String s : BasicMatcherTests.SHORT_STRINGS) {
            testMatchChar(true, s, ".");
            testMatchChar(true, s, s);
            testMatchChar(false, s, "x");
        }
    }
    
    @Test
    public void matchCharFalseLong() {
        for (String s : BasicMatcherTests.LONG_STRINGS) {
            testMatchChar(false, s, "c");
            testMatchChar(false, s, "$");
            testMatchChar(false, s, "^a");
            testMatchChar(false, s, "a");
            testMatchChar(false, s, "A*");
            testMatchChar(false, s, "Y*");
        }
    }
    
    @Test
    public void matchDotStarManyDot() {
        Assert.assertTrue(matchStar("bbbb", ".*..bb"));
        Assert.assertTrue(matchStar("bbbb", ".*.*.*bb"));
    }
    
    @Test
    public void matchError() {
        String[][] errorTests = new String[][] {
            {
                "", "fsa+++"
            }, {
                "", "fsa++*"
            }, {
                "", "fsa++?"
            }, {
                "", "fsa+*+"
            }, {
                "", "fsa+**"
            }, {
                "", "fsa+*?"
            }, {
                "", "fsa+?+"
            }, {
                "", "fsa+?*"
            }, {
                "", "fsa+??"
            }, {
                "", "fsa*++"
            }, {
                "", "fsa*+*"
            }, {
                "", "fsa*+?"
            }, {
                "", "fsa**+"
            }, {
                "", "fsa***"
            }, {
                "", "fsa**?"
            }, {
                "", "fsa*?+"
            }, {
                "", "fsa*?*"
            }, {
                "", "fsa*??"
            }, {
                "", "fsa?++"
            }, {
                "", "fsa?+*"
            }, {
                "", "fsa?+?"
            }, {
                "", "fsa?*+"
            }, {
                "", "fsa?**"
            }, {
                "", "fsa?*?"
            }, {
                "", "fsa??+"
            }, {
                "", "fsa??*"
            }, {
                "", "fsa???"
            }
        };
        
        for (String errorTest[] : errorTests)
            try {
                match(errorTest[0], errorTest[1]);
                Assert.fail();
            } catch (PatternSyntaxException e) {}
    }
    
    @Test
    public void matchFalse() {
        Assert.assertFalse(match("T", "T+p"));
        Assert.assertFalse(match("T", "T*p"));
        Assert.assertFalse(match("", "^.+$"));
        Assert.assertFalse(match("", "as^ffdsa^fdsa^g^he"));
        Assert.assertFalse(match("1H$BfOlFJPV2", "$+B"));
        Assert.assertFalse(match("gdfsa", "^$++"));
        Assert.assertFalse(match("q4$", "4$?$"));
    }
    
    @Test
    public void matchHereFalse() {
        for (String s : BasicMatcherTests.SHORT_STRINGS)
            Assert.assertFalse(matchHere("", s));
        
        Assert.assertFalse(matchHere("abc", "abb"));
        Assert.assertFalse(matchHere("aaa", "a++a$"));
        Assert.assertFalse(matchHere("aaa", "a*+a$"));
    }
    
    @Test
    public void matchHereManyDot() {
        Assert.assertTrue(matchHere("afgsd", "a.g"));
        Assert.assertTrue(matchHere("bwerqsdf", "..er."));
    }
    
    @Test
    public void matchHereOneLit() {
        for (String s : BasicMatcherTests.LONG_STRINGS)
            testMatchHere(true, s, s.substring(0, 1));
    }
    
    @Test
    public void matchStar() {
        for (String s : BasicMatcherTests.LONG_STRINGS) {
            testMatchStar(true, s, ".*");
            testMatchStar(true, s, s.substring(0, 1) + "*");
        }
    }
    
    @Test
    public void matchStarEmptyText() {
        Assert.assertTrue(matchStar("", "x*"));
        Assert.assertTrue(match("", "^x*"));
    }
    
    @Test
    public void matchStarEnd() {
        Assert.assertTrue(matchStar("a", "a*$"));
        Assert.assertTrue(matchStar("a", "a+$"));
        Assert.assertTrue(match("a", "b*$"));
        Assert.assertFalse(match("a", "^c*$"));
    }
    
    @Test
    public void matchTrue() {
        Assert.assertTrue(match("a", "^.$"));
        Assert.assertTrue(match("", "^$"));
        Assert.assertTrue(match("bbbb", "^.*$"));
        Assert.assertTrue(match("bbbb", "^.+$"));
        Assert.assertTrue(match("", "^.*$"));
        Assert.assertTrue(match("", "^$"));
        Assert.assertTrue(match("", ""));
        Assert.assertTrue(match("gdsa", ""));
        Assert.assertTrue(match("gg", "^"));
        Assert.assertTrue(match("aaa", "$"));
        Assert.assertTrue(match("", "^"));
        Assert.assertTrue(match("", "$"));
        Assert.assertTrue(match("bbbb", "^.*..bb"));
        Assert.assertTrue(match("a0HMyQuPpNYw6UV0COIyfHT8", "^++Q?+"));
        Assert.assertTrue(match("6bbFKWt2Hb7XrPNoiX1P9IhPPLHwctz", "z+$"));
        Assert.assertTrue(match("fffw", "f+$?w+"));
        Assert.assertTrue(match("oGgMq", "$w*"));
    }
    
    private void testMatchChar(boolean expected, String text, String regexp) {
        final String msg = "matchChar(\"" + text + "\", \"" + regexp
            + "\") should be " + expected;
        Assert.assertEquals(msg, expected, matchChar(text, regexp));
    }
    
    private void testMatchHere(boolean expected, String text, String regexp) {
        final String msg = "matchHere(\"" + text + "\", \"" + regexp
            + "\") should be " + expected;
        Assert.assertEquals(msg, expected, matchHere(text, regexp));
    }
    
    private void testMatchStar(boolean expected, String text, String regexp) {
        final String msg = "matchStar(\"" + text + "\", \"" + regexp
            + "\") should be " + expected;
        Assert.assertEquals(msg, expected, matchStar(text, regexp));
    }
}
