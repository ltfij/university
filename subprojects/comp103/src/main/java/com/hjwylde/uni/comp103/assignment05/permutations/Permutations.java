package com.hjwylde.uni.comp103.assignment05.permutations;

/*
 * Code for Assignment 5, COMP 103 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Prints out all permuations of a string The static method permute constructs all the permutations
 * The main method gets the string, calls recPermute, and prints the result.
 */
public class Permutations {

    // Main
    public static void main(String[] arguments) {
        String string = "";
        if (arguments.length > 0)
            string = arguments[0];
        else {
            System.out.print("Enter string to permute: ");

            try (Scanner scan = new Scanner(System.in)) {
                string = scan.nextLine();
            }
        }
        List<String> permutations = Permutations.recPermute(string);
        System.out.println("Number of permutations of " + string + " is " + permutations.size());
        for (String p : permutations)
            System.out.println(p);
        System.out.println("---------");
    }

    // Returns a List of all the permutations of a String.
    public static List<String> recPermute(String string) {
        List<String> permutations = new ArrayList<>();
        String subString;

        if (string.length() > 1) // If the string to permute has a possible
                                 // permutation...
            // ...then go through each letter in string to find all sub permutations
            // that begin with it
            for (int i = 0; i < string.length(); i++) {
                // Create a string omiting letter charAt(i) to use to find sub
                // permutations
                subString = string.substring(0, i) + string.substring(i + 1, string.length());

                for (String str : Permutations.recPermute(subString))
                    permutations.add(string.charAt(i) + str); // Concatenate the beginning
                                                              // letter charAt(i)
                                                              // with every sub
                                                              // permutation
            }
        else
            // ...otherwise the only permutation of the string is string
            permutations.add(string);

        return permutations; // Return all permutations of string
    }
}
