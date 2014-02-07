package com.hjwylde.uni.swen221.assignment01;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/*
 * Code for Assignment 1, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A basic regular expression matcher. Matches regular expressions consisting of .*+?$^ and any
 * character literal.
 * <p>
 * A regular expressions (or regexes) are a very small language for describing text strings. A regex
 * describes zero or more strings; a regex matcher attempts to match a text string with a regex. A
 * match occurs if the text string is described by the regex. See <a
 * href="http://en.wikipedia.org/wiki/Regex">wikipedia</a> for more details
 * <p>
 * A regular expression consists of characters which match against themselves and control characters
 * which have special behaviour, such as wildcard characters. There are many varieties of regular
 * expression languages with different syntax and semantics. The language for this assignment is a
 * small subset of the most common language, used in Perl and Java and elsewhere.
 * <p>
 * If you are unclear on whether a regex should match a text string or not you could use the Java
 * regex library to compare against, this might be useful in the edge cases. The method matchWithLib
 * will help you do this. Note that you may not use the Java regex library in your solutions (only
 * to help you understand regular expressions).
 * 
 * <ul>
 * <li>A character literal matches that character.
 * <li>. matches any character.
 * <li>? matches zero or one occurrences of the preceding character (if there is no preceding char,
 * then never matches). This can be changed to a reluctant or possessive quantifier by appending a
 * '?' or '+' respectively.
 * <li>* matches zero or more occurrences of the preceding character (if there is no preceding char,
 * then never matches). This can be changed to a reluctant or possessive quantifier by appending a
 * '?' or '+' respectively.
 * <li>+ matches one or more occurrences of the preceding character (if there is no preceding char,
 * then never matches). This can be changed to a reluctant or possessive quantifier by appending a
 * '?' or '+' respectively.
 * <li>^ matches the start of a string - this character is only allowed at the beginning of the
 * string.
 * <li>$ matches the end of a string - multiple of these characters are supported.
 * </ul>
 */

public class BasicMatcher implements Matcher {

    // Logger logger = Logger.getLogger(BasicMatcher.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(String text, String regex) {
        // logger.fine("match {text: \"" + text + "\", regex: \"" + regex + "\"}");

        if (!validateRegex(regex))
            return false;

        /*
         * Check if the regex should be matched straight away with the beginning of the text. This
         * is a rather poor method of checking whether the start character has a quantifier... I
         * haven't improved on it as I see the problem being a fundamental problem with the grammar
         * for this type of parser. I thought about how it could be done better, but it all seemed
         * like it would require quite a lot of specific conditionals for special cases which isn't
         * what it should require. Note: this doesn't fix the problem of having more than one begin
         * character in the string. I didn't understand how the Java regex's work when given
         * multiple carrots so I left that out.
         */
        int regexLength = regex.length();
        if ((regexLength > 0) && (regex.charAt(0) == '^')) { // If we begin with a start
                                                             // character...
            boolean consumeCarrot = false; // For reference on whether the quantifier requires us to
                                           // use
                                           // the carrot or not.
            String restOfRegex = regex.substring(1);

            if (regexLength > 1)
                if ((regex.charAt(1) == '?') || (regex.charAt(1) == '*')
                        || (regex.charAt(1) == '+')) { // Initial quantifier check.
                    restOfRegex = regex.substring(2);

                    if (regex.charAt(1) == '+')
                        consumeCarrot = true;

                    if (regexLength > 2)
                        if ((regex.charAt(2) == '?') || (regex.charAt(2) == '+')) // Second
                                                                                  // quantifier
                                                                                  // check -
                                                                                  // whether we are
                                                                                  // possessive
                                                                                  // or reluctant.
                            restOfRegex = regex.substring(3);

                } else
                    // ...else we have no quantifier, consume the carrot.
                    consumeCarrot = true;

            return (consumeCarrot ? matchHere(text, restOfRegex) : match(text, restOfRegex)); // If
                                                                                              // we
                                                                                              // don't
                                                                                              // need
                                                                                              // to
                                                                                              // consume
                                                                                              // the
                                                                                              // carrot,
                                                                                              // don't
                                                                                              // bother
                                                                                              // and
                                                                                              // just
                                                                                              // call
                                                                                              // match
                                                                                              // again
                                                                                              // without
                                                                                              // it.
        }

        // If we can match right away... Return true.
        if (matchHere(text, regex))
            return true;

        // Loop through the text attempting to match, quit at the first success or the end of the
        // text
        while (text.length() != 0) {
            text = text.substring(1);

            if (matchHere(text, regex))
                return true;
        }

        return false;
    }

    /**
     * Matches the first character of the text against the first character of the regex. Assumes the
     * regex is valid, in other words: the only special character the regex could be is a '.', all
     * other characters are treated as literals and the text and regex are not null nor empty
     * strings.
     * 
     * @param text to be matched against the regular expression
     * @param regex the regular expression used for matching
     * @return true if the first characters match; false otherwise.
     */
    protected boolean matchChar(String text, String regex) {
        // logger.fine("matchChar {text: \"" + text + "\", regex: \"" + regex + "\"}");

        // Needs to check for the ending character because I have allowed that character to be in
        // the
        // text string.
        return (regex.charAt(0) == '.')
                || (((regex.charAt(0) == text.charAt(0)) && (regex.charAt(0) != '$')));
    }

    /**
     * Returns whether the given regular expression can be matched against the start of the text
     * string. This is in contrast to the {@link #match(String, String)} method which attempts to
     * match the regular expression anywhere in the text string. Assumes that the regular expression
     * has been processed by the {@link #match(String, String)} method already, such that it does
     * not begin with a '^' character.
     * 
     * @param text some text to match
     * @param regex a regular expression to match against
     * @return true if the regular expression can be matched against the start of the string; false
     *         otherwise
     */
    protected boolean matchHere(String text, String regex) {
        // logger.fine("matchHere {text: \"" + text + "\", regex: \"" + regex + "\"}");

        if (regex.length() == 0)
            return true;

        /*
         * Quantifier checks Allowed quantifiers: '?' - matches 0..1 of the preceding character. '*'
         * - matches 0..* of the preceding character. '+' - matches 1..* of the preceding character.
         */
        if (regex.length() > 1)
            switch (regex.charAt(1)) { // Check for a quantifier pattern
                case '?': // 0..1
                    return matchQuestionMark(text, regex);
                case '*': // 0..*
                    return matchStar(text, regex);
                case '+': // 1..*
                    return matchPlus(text, regex);
            }

        // If we're at the end string, check if text is empty and regex matches an empty string
        // (accounts for "$w*x?" etc).
        if (regex.charAt(0) == '$')
            return ((text.length() == 0) && (matchHere(text, regex.substring(1))));

        // Recursively match the whole text with the regex, one char at a time
        if ((text.length() != 0) && matchChar(text, regex))
            return matchHere(text.substring(1), regex.substring(1));

        return false;
    }

    /**
     * Assumes that the start of the regex is some character followed by a '+' character. It tries
     * to match the first character of the regular expression one or more times against the text
     * input, and then attempts to match the rest of the regular expression against the rest of the
     * text input.
     * 
     * Checks the character following the '+' to see if it is a reluctant or possessive quantifier
     * and applies the appropriate logic. A possessive quantifier will consume all of the possible
     * characters without backtracking, while a reluctant or greedy quantifier will backtrack to try
     * all combinations.
     * 
     * @param text some text to match
     * @param regex the regular expression; must be at least two characters long, and the second
     *        should be '+'
     * @return true if the match is successful, false otherwise.
     */
    protected boolean matchPlus(String text, String regex) {
        // logger.fine("matchPlus {text: \"" + text + "\", regex: \"" + regex + "\"}");

        int regexLength = regex.length();
        String restOfRegex;

        // Check what kind of quantifier we are
        quantifierCheck: if ((regexLength > 2) && (regex.charAt(2) == '+')) { // If we're a
                                                                              // possessive
                                                                              // quantifier...
            restOfRegex = regex.substring(3);

            if (regex.charAt(0) == '$')
                break quantifierCheck; // Just needed to know the restOfRegex, string end character
                                       // is
                                       // handled at the end of this method.

            if ((text.length() == 0) || !matchChar(text, regex)) // Catch corner condition, ensure
                                                                 // there's
                                                                 // at least one matched character.
                return false;

            while ((text.length() != 0) && matchChar(text, regex))
                // Eat all possible matched characters. Don't allow backtracks.
                text = text.substring(1);

            return matchHere(text, restOfRegex);
        }
        // We are a greedy or reluctant quantifier. For the sake of this assignment, these can be
        // treated the same as we only care about whether the regex matches the text, and not how it
        // is split up. This implementation is a reluctant implementation.

        if ((regexLength > 2) && (regex.charAt(2) == '?'))
            restOfRegex = regex.substring(3); // Reluctant quantifier.
        else
            restOfRegex = regex.substring(2); // Greedy quantifier.

        // Match as few characters as possible and try to match the rest of the regex. This
        // effectively backtracks when we can't match the rest of the regex and tries matching more
        // characters to the plus.
        while ((text.length() != 0) && matchChar(text, regex)) {
            text = text.substring(1);

            if (matchHere(text, restOfRegex))
                return true;
        }

        // If we're matching against a string end character, then the text has to be length 0 as
        // this is
        // a 1..* quantifier. We then need to ensure that the rest of the regex matches the empty
        // string.
        return ((regex.charAt(0) == '$') && (text.length() == 0) ? matchHere(text, restOfRegex)
                : false);
    }

    /**
     * Assumes that the start of the regex is some character followed by a '?' character. It tries
     * to match the first character of the regular expression zero or one times against the text
     * input, and then attempts to match the rest of the regular expression against the rest of the
     * text input.
     * 
     * Checks the character following the '?' to see if it is a reluctant or possessive quantifier
     * and applies the appropriate logic. A possessive quantifier will consume all of the possible
     * characters without backtracking, while a reluctant or greedy quantifier will backtrack to try
     * all combinations.
     * 
     * @param text some text to match
     * @param regex the regular expression; must be at least two characters long, and the second
     *        should be '?'
     * @return true if the match is successful, false otherwise.
     */
    protected boolean matchQuestionMark(String text, String regex) {
        // logger.fine("matchQuestionMark {text: \"" + text + "\", regex: \"" + regex + "\"}");

        int regexLength = regex.length();

        // Check what kind of quantifier we are
        if ((regexLength > 2) && (regex.charAt(2) == '+')) { // If we're a possessive quantifier...
            String restOfRegex = regex.substring(3);

            if ((text.length() != 0) && (matchChar(text, regex))) // Eat the character if we can.
                text = text.substring(1);

            return matchHere(text, restOfRegex); // Check if we match, no backtracking!
        }

        // We are a greedy or reluctant quantifier. For the sake of this assignment, these can be
        // treated the same as we only care about whether the regex matches the text, and not how it
        // is split up. This implementation is a reluctant implementation.

        String restOfRegex;
        if ((regexLength > 2) && (regex.charAt(2) == '?'))
            restOfRegex = regex.substring(3); // Reluctant quantifier.
        else
            restOfRegex = regex.substring(2); // Greedy quantifier.

        // Match as few characters as possible and try to match the rest of the regex. This
        // effectively backtracks when we can't match the rest of the regex and tries matching more
        // characters to the question mark.
        if (matchHere(text, restOfRegex)) // Check the 0 of preceding character match.
            return true;
        else if ((text.length() != 0) && (matchChar(text, regex))) { // Check the 1 of preceding
                                                                     // character match.
            text = text.substring(1);

            return (matchHere(text, restOfRegex));
        }

        return false;
    }

    /**
     * Assumes that the start of the regex is some character followed by a '*' character. It tries
     * to match the first character of the regular expression zero or more times against the text
     * input, and then attempts to match the rest of the regular expression against the rest of the
     * text input.
     * 
     * Checks the character following the '*' to see if it is a reluctant or possessive quantifier
     * and applies the appropriate logic. A possessive quantifier will consume all of the possible
     * characters without backtracking, while a reluctant or greedy quantifier will backtrack to try
     * all combinations.
     * 
     * @param text some text to match
     * @param regex the regular expression; must be at least two characters long, and the second
     *        should be '*'
     * @return true if the match is successful, false otherwise.
     */
    protected boolean matchStar(String text, String regex) {
        // logger.fine("matchStar {text: \"" + text + "\", regex: \"" + regex + "\"}");

        int regexLength = regex.length();

        // Check what kind of quantifier we are
        if ((regexLength > 2) && (regex.charAt(2) == '+')) { // If we're a possessive quantifier...
            String restOfRegex = regex.substring(3);

            while ((text.length() != 0) && matchChar(text, regex))
                // Eat as many characters as possible.
                text = text.substring(1);

            return matchHere(text, restOfRegex); // Check if match, no backtracking!
        }

        String restOfRegex;
        if ((regexLength > 2) && (regex.charAt(2) == '?'))
            restOfRegex = regex.substring(3); // Reluctant quantifier.
        else
            restOfRegex = regex.substring(2); // Greedy quantifier.

        if (matchHere(text, restOfRegex))
            return true;

        // Match as few characters as possible and try to match the rest of the regex. This
        // effectively backtracks when we can't match the rest of the regex and tries matching more
        // characters to the plus
        while ((text.length() != 0) && matchChar(text, regex)) { // While characters match...
            text = text.substring(1); // ...eat the character.

            if (matchHere(text, restOfRegex)) // And try match what's left against the regular
                                              // expression.
                return true;
        }

        return false;
    }

    /**
     * Attempts to match text against regex using the Java regex library.
     * <p>
     * This method is not used by the above matching code, and you must NOT use it in your solution!
     * However, you may find it helpful for testing.
     * 
     * @param text - A text string to match (String)
     * @param regex - A regular expression to match against (String)
     * @return true if the match is successful, false otherwise
     */
    protected boolean matchWithLib(String text, String regex) {
        Pattern p = Pattern.compile(regex);
        java.util.regex.Matcher m = p.matcher(text);
        return m.find();
    }

    /**
     * Validates the regular expression to the implementation of regex's for this class. Only checks
     * for dangling meta characters, and will throw a PatternSyntaxException if any is found.
     * 
     * @param regex the regular expression to validate.
     * @return true if a valid regular expression
     */
    protected boolean validateRegex(String regex) {
        // logger.fine("validateRegex {regex: \"" + regex + "\"}");

        // Dangling meta character sequences that can occur anywhere in the regex.
        String[] danglingMetaSequences =
                {"?*", "**", "+*", "+++", "++?", "+?+", "+??", "*++", "*+?", "*?+", "*??", "?++",
                        "?+?", "??+", "???"};

        // Dangling meta characters at the start of the regex.
        String[] danglingStartMetaChars = {"?", "*", "+"};

        /*
         * Note: The regular expression is only being validated for the implementations we have been
         * told to make and I have allowed greedy, reluctant and possessive quantifiers as an
         * addition (eg. "*", "*?" and "*+" respectively)
         */

        /*
         * Find dangling meta characters:
         */

        int index;
        for (String danglingMetaSequence : danglingMetaSequences) {
            index = regex.indexOf(danglingMetaSequence);
            if (index >= 0)
                throw new PatternSyntaxException("Dangling meta character '"
                        + danglingMetaSequence.substring(danglingMetaSequence.length() - 1) + "'",
                        regex, (index + danglingMetaSequence.length()) - 1);
        }

        for (String danglingStartMetaChar : danglingStartMetaChars) {
            index = regex.indexOf(danglingStartMetaChar);
            if (regex.indexOf(danglingStartMetaChar) == 0)
                throw new PatternSyntaxException("Dangling meta character '"
                        + danglingStartMetaChar + "'", regex, 0);
        }

        return true;
    }
}
