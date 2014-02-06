package com.hjwylde.uni.swen221.lab01.calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * Code for Laboratory 1, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public final class Main {
    
    private Main() {} // avoid instantiation of this class
    
    public static void main(String[] args) {
        final BufferedReader input = new BufferedReader(new InputStreamReader(
            System.in));
        try {
            System.out.println("Welcome to the Calculator!");
            while (true) {
                System.out.print("> ");
                String text = input.readLine();
                
                if (text == null)
                    continue;
                
                // commands goes here
                if (text.equals("help"))
                    Main.printHelp();
                else if (text.equals("exit"))
                    System.exit(0);
                else
                    Main.calculate(text);
            }
        } catch (IOException e) {
            System.err.println("I/O Error - " + e.getMessage());
        }
    }
    
    private static void calculate(String text) {
        try {
            Calculator calc = new Calculator(text);
            System.out.println("= " + calc.evaluate());
        } catch (RuntimeException e) {
            // Catching runtime exceptions is actually rather bad style;
            // see lecture about Exceptions later in the course!
            System.err.println(e.getMessage());
            System.err.println("Type \"help\" for help");
        }
    }
    
    private static void printHelp() {
        System.out.println("Calculator commands:");
        System.out.println("\thelp --- access this help page");
        System.out.println("\texit --- quit the calculator");
    }
}
