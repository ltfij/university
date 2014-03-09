package com.hjwylde.uni.swen221.assignment01.tests;

import java.util.regex.PatternSyntaxException;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.assignment01.BasicMatcher;

/*
 * Code for Assignment 1, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Random JUnit test generator for the class BasicMatcher.
 * 
 * @author Henry J. Wylde
 */
public class BasicMatcherTest extends BasicMatcher {

    /**
     * Allowed characters for the randomly generated text string and regex string.
     */
    private static final String TEST_CHARS =
            "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890$*?+.";

    /**
     * Number of JUnit tests to complete.
     */
    private static final int NUM_TESTS = 1000000;

    /**
     * Performs <code>NUM_TESTS</code> tests on the <code>match(String, String)</code> method by
     * using a randomly generated string and a randomly generated regular expression.
     */
    @Test
    public void testMatch() {
        for (int i = 0; i < BasicMatcherTest.NUM_TESTS; i++)
            test(BasicMatcherTest.generateString((int) (Math.random() * 15)),
                    BasicMatcherTest.generateRegex((int) (Math.random() * 8), Math.random() < 0.30,
                            Math.random() < 0.30));
    }

    /**
     * Performs a test with the specified text and regex. If the <code>match(String, String)</code>
     * function throws an error, asserts that the <code>matchWithLib(String, String)</code> throws
     * an error too, otherwise asserts that they arrive at the same match result.
     * 
     * @param text the text to check.
     * @param regex the regex to match the text against.
     */
    private void test(final String text, final String regex) {
        boolean error = false;
        boolean result = false;
        boolean libResult = false;

        try {
            result = match(text, regex);
        } catch (final PatternSyntaxException e) {
            error = true;
        }

        try {
            libResult = matchWithLib(text, regex);
        } catch (final PatternSyntaxException e) {
            Assert.assertTrue("{result: " + result + ", text: " + text + ", regex: " + regex + "}",
                    error); // An error occured on matchWithLib, assert
                            // that one occured with
                            // match.
        }

        Assert.assertTrue("{result: " + result + ", libResult: " + libResult + ", text: " + text
                + ", regex: " + regex + "}", result == libResult);
    }

    /**
     * Generates a random regular expression with the specified length. If also specified, the
     * regular expression will begin with a carrot ('^') or end with a dollar sign ('$').
     * 
     * @param length the length of the regular expression, not including beginCarrot or endDollar.
     * @param beginCarrot whether to begin the regex with a carrot.
     * @param endDollar whether to end the regex with a dollar.
     * @return the generated regex.
     */
    private static String generateRegex(final int length, final boolean beginCarrot,
            final boolean endDollar) {
        final StringBuilder sb = new StringBuilder();

        sb.append(beginCarrot ? "^" : "");

        for (int i = 0; i < length; i++) {
            if (Math.random() < 0.15) { // 15% chance of adding in a '*', '?' or '+'.
                sb.append(Math.random() < 0.66 ? (Math.random() < 0.5 ? '*' : '+') : '?');
                continue; // '*', '+' and '?' count as a character, go to next i.
            }

            // Pick a random character from allowed test characters.
            sb.append(BasicMatcherTest.TEST_CHARS.charAt((int) (Math.random() * BasicMatcherTest.TEST_CHARS
                    .length())));
        }

        sb.append(endDollar ? "$" : "");

        return sb.toString();
    }

    /**
     * Generates a string of the specified length from the list of allowed test characters.
     * 
     * @param length the length of the string.
     * @return the generated string.
     */
    private static String generateString(final int length) {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++)
            sb.append(BasicMatcherTest.TEST_CHARS.charAt((int) (Math.random() * BasicMatcherTest.TEST_CHARS
                    .length())));

        return sb.toString();
    }

}
