package com.hjwylde.uni.swen222.assignment02.cluedo.util;

import java.util.Locale;

/**
 * Utility constants and functions.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 25/07/2013
 */
public final class Util {
    
    /**
     * This class cannot be instantiated.
     */
    private Util() {}
    
    /**
     * Converts the given string to title case using the given locale.
     * 
     * @param str the string to convert.
     * @param locale the locale.
     * @return the title cased string.
     */
    public static String toTitleCase(String str, Locale locale) {
        if (str.length() <= 1)
            return str.toUpperCase(locale);
        
        return str.substring(0, 1).toUpperCase(locale)
            + str.substring(1).toLowerCase(locale);
    }
}