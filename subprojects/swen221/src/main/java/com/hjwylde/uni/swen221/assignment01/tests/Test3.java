package com.hjwylde.uni.swen221.assignment01.tests;

import java.util.regex.PatternSyntaxException;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.assignment01.BasicMatcher;

/*
 * Code for Assignment 1, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class Test3 extends BasicMatcher {

    public Test3() {
        callme();
    }

    @Test
    public void callme() {

        // test("6bbFKWt2Hb7XrPNoiX1P9IhPPLHwctz", "z+$");
        /*
         * test("", "fsa+++"); test("", "fsa++*"); test("", "fsa++?"); test("", "fsa+*+"); test("",
         * "fsa+**"); test("", "fsa+*?"); test("", "fsa+?+"); test("", "fsa+?*"); test("",
         * "fsa+??"); test("", "fsa*++"); test("", "fsa*+*"); test("", "fsa*+?"); test("",
         * "fsa**+"); test("", "fsa***"); test("", "fsa**?"); test("", "fsa*?+"); test("",
         * "fsa*?*"); test("", "fsa*??"); test("", "fsa?++"); test("", "fsa?+*"); test("",
         * "fsa?+?"); test("", "fsa?*+"); test("", "fsa?**"); test("", "fsa?*?"); test("",
         * "fsa??+"); test("", "fsa??*"); test("", "fsa???");
         */

        // test("f", "^++*f");

        // test("a0HMyQuPpNYw6UV0COIyfHT8", "^++Q?+");
        // System.out.println(matchWithLib("sww", "w$?w+"));
        // test("fffw", "f+$?w+");
        // test("oGgMq", "$w*");
        // test("", "as^ffdsa^fdsa^g^he");
        // test("1H$BfOlFJPV2", "$+B");
        // test("gdfsa", "^$++");
        test("q4$", "4$?$");
    }

    private void test(String text, String regex) {
        boolean error = false;
        boolean result = false;
        boolean libResult = false;
        try {
            result = match(text, regex);
        } catch (PatternSyntaxException e) {
            error = true;
        }

        try {
            libResult = matchWithLib(text, regex);
        } catch (PatternSyntaxException e) {
            Assert.assertTrue("{result: " + result + ", text: " + text + ", regex: " + regex
                    + ", error: " + e.getMessage() + "}", error);
        }

        Assert.assertTrue("{result: " + result + ", libResult: " + libResult + ", text: " + text
                + ", regex: " + regex + "}", result == libResult);

        System.out.println("Test: " + result);
    }
}
