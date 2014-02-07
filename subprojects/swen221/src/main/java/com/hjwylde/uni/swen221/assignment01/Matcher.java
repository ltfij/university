package com.hjwylde.uni.swen221.assignment01;

/*
 * Code for Assignment 1, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A regular expression matcher. The regular expression language which is matched is defined by the
 * implementation.
 */
public interface Matcher {

    /**
     * Returns whether the given text is matched by the given regular expression.
     * 
     * @param text some text to match
     * @param regex a regular expression to match against
     * @return true if and only if the text argument is matched by the regex argument
     */
    public boolean match(String text, String regex);
}
