package com.hjwylde.uni.swen221.assignment1.tests;

import java.util.regex.PatternSyntaxException;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.assignment1.BasicMatcher;

/*
 * Code for Assignment 1, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class test2 extends BasicMatcher {
    
    /**
     * Possible characters to be used in a randomly generated string for text
     */
    static final String possibleChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * Possible characters to be used in a randomly generated string for regex
     */
    static final String possibleRegexChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ^*$.+";
    
    /**
     * Performs extra tests made from inputs that failed in random tests
     */
    @Test
    public void extraTest() {
        Assert
            .assertFalse(match(
                "fZZkzClFRECKVeiFIieainwhOXKsqUpWpRzboaRAojRtrqSgxmdIfpnULdtkXxWjlzWbZZnnFMOkblbAH",
                "bF"));
        Assert
            .assertTrue(match(
                "pgmykNpZYmnKferQJRVQGPEfKeyIKilctmrdgtjdneVMkOsujnAKHXBBDHCRpQhnkgecUyoOeB",
                "o"));
        Assert.assertFalse(match("", "fKED.GQKMew"));
        Assert.assertFalse(match("",
            "K*hcSiVpMNahwwpFdiDc$wk*bQaiRgoFx^$nQlFrwgCjbn^."));
        Assert.assertFalse(match(
            "zEoQlbwIIekwVObCLJLROhKoZXleoqaNbThglPcHfFUxZUiniBAGecYmPH",
            "$*k^"));
        Assert
            .assertFalse(match(
                "CxKxgHCKuCrBZHrfgJmAiZjVGACYPxFPgkePuVvImQhvnRAXLLGyMjmceLqFAcvoXzmEXeEhezAVDFJPooaCZlcMygLFOJku",
                "q$"));
        Assert.assertTrue(match("oGgMq", "$w*"));
        Assert
            .assertTrue(match(
                "hLTzINJekzmxyLxYOOpufpHNTRaGnFGqUTdydtnZCyRDjxRILrwXTVDRTyGreQTKpJQuY",
                "$K*"));
        Assert
            .assertTrue(match(
                "vJDLGVfNnLmQiQfLAgAQznskFhtScHEGNbhJRVLjRzFIOLgriyzXDgZjYjQmOEpSfndhVCSRygXLfEbULiMk",
                "$"));
        Assert.assertFalse(match(
            "SBMXlnFFzkNstahmakCMaZvcbOghTMUWeWZmJTlLZNpiIaqqCXyswTudL", "$u"));
        Assert.assertTrue(match("JLJGcKQhrAURqMHfcEtNQDfMdZFCtdZYAwjt", "s*$"));
        Assert
            .assertTrue(match(
                "XNjyjPOAtYLdzeGnJPoqVyttmSpiKdaQKCQGvmJJNVjJzWlwQJVgFVAKcYiwRjgrrXcqMqPZwgOILhLsddUD",
                "l$*"));
        Assert.assertFalse(match("TqNFbpevWvgGAjOG", "b$L*"));
        Assert.assertFalse(match(
            "RQeOvWcQTbGptxFDmRhGrTmJaaijLecNBalhLpEakdYVwbpWlJtNpwt", "j$L"));
        Assert
            .assertTrue(match(
                "QGJFeyAEKYcEXagymNkufyjmTDZHGTkZjKjYSuzhpyHXIWrhcIagZarFbWJapxuxJMuaRiquckhvPiuNE",
                "^"));
        Assert
            .assertFalse(match(
                "QGJFeyAEKYcEXagymNkufyjmTDZHGTkZjKjYSuzhpyHXIWrhcIagZarFbWJapxuxJMuaRiquckhvPiuNE",
                "a^"));
        Assert
            .assertFalse(match(
                "QGJFeyAEKYcEXagymNkufyjmTDZHGTkZjKjYSuzhpyHXIWrhcIagZarFbWJapxuxJMuaRiquckhvPiuNE",
                "$A"));
        Assert
            .assertFalse(match(
                "QGJFeyAEKYcEXagymNkufyjmTDZHGTkZjKjYSuzhpyHXIWrhcIagZarFbWJapxuxJMuaRiquckhvPiuNE",
                "$^"));
        Assert
            .assertTrue(match(
                "OBbaWsIYgakpLseONgCvjITkVnIReunLBVSsUkNHQQgOXtGjjsoeeDWWrMuPeeKxYzolgrsmgnpsEtTyijTulj",
                "W++"));
        Assert.assertTrue(match(
            "ITwmBYHyNHUcfZyCxmnFfOQBpOLZqbeBGXaqFtiwcfNADGnQQFvRhlyk", "S*+"));
        Assert.assertTrue(match("vjDg", "$*"));
        Assert
            .assertFalse(match(
                "IbcYcImuozdidIQEoIQuMmXdyxoFMGrwCwpNoCofIbjlIFXhzZuvdBvjErOjZMpYePmoc",
                "X+$"));
        Assert.assertTrue(match("SokAliQdjsKxtDLxypXVdrYrBqp", "$+"));
        Assert.assertFalse(match(
            "YiMocnHsloiQgNtDJITqwkVxrKEDEoWNZERkakUYwMEuRxNeBIMrp", "w*N^"));
        Assert.assertTrue(match("mtLAxsVVrzzsQPynInFktexKdvsEarJRfmO", "^+"));
        Assert
            .assertTrue(match(
                "WgSFYwGNbcZkIvusxORpreVFtArYlvQfDcYVGhKqldBdaxuYEvoTQvPTbUteNMAwxcLXHYCNkpXhXNPaVkgbVXdmZDfQGQQiL",
                "^*G"));
        Assert
            .assertFalse(match(
                "TVJSNqfwwdufFDcnGPWwWblsCpfemBKwWFRJIxEYiUPWLrTtnXslPEsfnozoFsAUWZmKvLaEmLfBtowhQXXjYdTUoHY",
                "Jy*e"));
        Assert.assertFalse(match("qdJvXaAN", "Aj*$"));
        Assert
            .assertFalse(match(
                "zHapkgVyqUcmVVxRyyRfdofmjmnwBVooIzEnRgNKxdSOMxsCzipYGEJvBjiqYYlwqYGuVGMIxvHSJqQOPRN",
                "^L*$"));
    }
    
    /**
     * Performs a series of random tests
     */
    @Test
    public void randomTests() {
        for (int i = 0; i < 1000; i++)
            randomTest();
    }
    
    /**
     * Generates two random strings for the text and the regex, it then compares the match by the
     * BasicMatcher and that from the librarys
     */
    private void randomTest() {
        String inputText = test2.generateRandomText((int) (Math.random() * 10));
        String inputRegex = test2
            .generateRandomRegex((int) (Math.random() * 5));
        boolean expected = false;
        try {
            expected = matchWithLib(inputText, inputRegex);
        } catch (PatternSyntaxException e) {
            return;
        }
        final String msg = "match(\"" + inputText + "\", \"" + inputRegex
            + "\") should be " + expected;
        Assert.assertEquals(msg, expected, match(inputText, inputRegex));
    }
    
    /**
     * @param length of string to be generated
     * @return A random string of consisting of upper and/or lower case letters and regex specific
     *         characters
     */
    private static String generateRandomRegex(int length) {
        return test2.generateRandomString(length, test2.possibleRegexChars);
    }
    
    /**
     * @param length of string to be generated
     * @param possibleChars the characters which the string will comprise of
     * @return A random string
     */
    private static String generateRandomString(int length, String possibleChars) {
        char[] randomChars = new char[length];
        for (int i = 0; i < length; i++) {
            int randomNum = (int) (Math.random() * possibleChars.length());
            randomChars[i] = possibleChars.charAt(randomNum);
        }
        return new String(randomChars);
    }
    
    /**
     * @param length of string to be generated
     * @return A random string of consisting of upper and/or lower case letters
     */
    private static String generateRandomText(int length) {
        return test2.generateRandomString(length, test2.possibleChars);
    }
}
