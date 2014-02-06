package com.hjwylde.uni.comp261.assignment02.stringSearch;

/*
 * Code for Assignment 2, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/*
 * Boyer Moore algorithm code source from:
 * http://www.fmi.uni-sofia.bg/fmi/logic/vboutchkova/sources/BoyerMoore_java.html
 * I modified only the variable names / format.
 */

public interface StringSearch {
    
    public int find(CharSequence haystack, CharSequence needle);
    
    public static class BoyerMoore implements StringSearch {
        
        @Override
        public int find(CharSequence haystack, CharSequence needle) {
            int[] last = BoyerMoore.generateLast(needle);
            int[] match = BoyerMoore.generateMatch(needle);
            
            int i = needle.length() - 1;
            int j = needle.length() - 1;
            while (i < haystack.length())
                if (needle.charAt(j) == haystack.charAt(i)) {
                    if (j == 0)
                        return i;
                    
                    j--;
                    i--;
                } else {
                    i += (needle.length() - j - 1)
                        + Math.max(j - last[haystack.charAt(i)], match[j]);
                    j = needle.length() - 1;
                }
            
            return -1;
        }
        
        private static int[] generateLast(CharSequence needle) {
            int[] last = new int[Character.MAX_VALUE + 1];
            
            for (int i = needle.length() - 1; i >= 0; i--)
                if (last[needle.charAt(i)] == 0)
                    last[needle.charAt(i)] = i;
            
            return last;
        }
        
        private static int[] generateMatch(CharSequence needle) {
            int[] match = new int[needle.length()];
            
            for (int i = 0; i < match.length; i++)
                match[i] = match.length;
            
            int[] suffix = BoyerMoore.generateSuffix(needle);
            
            for (int i = 0; i < (match.length - 1); i++) {
                int j = suffix[i + 1] - 1;
                
                if (suffix[i] > j)
                    match[j] = j - i;
                else
                    match[j] = Math.min((j - i) + match[i], match[j]);
            }
            
            if (suffix[0] < needle.length()) {
                for (int i = suffix[0] - 1; i >= 0; i--)
                    if (suffix[0] < match[i])
                        match[i] = suffix[0];
                
                int j = suffix[0];
                for (int i = suffix[j]; i < needle.length(); i = suffix[i])
                    while (j < i) {
                        if (match[j] > i)
                            match[j] = i;
                        
                        j++;
                    }
            }
            
            return match;
        }
        
        private static int[] generateSuffix(CharSequence needle) {
            int[] suffix = new int[needle.length()];
            
            suffix[suffix.length - 1] = suffix.length;
            int j = suffix.length - 1;
            
            for (int i = suffix.length - 2; i >= 0; i--) {
                while ((j < (suffix.length - 1))
                    && (needle.charAt(j) != needle.charAt(i)))
                    j = suffix[j + 1] - 1;
                
                if (needle.charAt(j) == needle.charAt(i))
                    j--;
                
                suffix[i] = j + 1;
            }
            
            return suffix;
        }
        
    }
    
    public static class KnuthMorrisPratt implements StringSearch {
        
        @Override
        public int find(CharSequence haystack, CharSequence needle) {
            int h = 0;
            int n = 0;
            int[] t = KnuthMorrisPratt.generateMatchTable(needle);
            
            while ((h + n) < haystack.length())
                if (needle.charAt(n) == haystack.charAt(h + n)) {
                    n++;
                    
                    if (n == needle.length())
                        return h;
                } else {
                    h += n - t[n];
                    
                    if (t[n] == -1)
                        n = 0;
                    else
                        n = t[n];
                }
            
            return -1;
        }
        
        private static int[] generateMatchTable(CharSequence needle) {
            int[] t = new int[needle.length()];
            
            int i = 2;
            int j = 0;
            
            t[0] = -1;
            t[1] = 0;
            
            while (i < needle.length())
                if (needle.charAt(i - 1) == needle.charAt(j))
                    t[i++] = ++j;
                else if (j > 0)
                    j = t[j];
                else
                    t[i++] = 0;
            
            return t;
        }
    }
}