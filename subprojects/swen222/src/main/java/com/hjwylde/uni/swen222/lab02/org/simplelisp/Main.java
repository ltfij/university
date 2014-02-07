package com.hjwylde.uni.swen222.lab02.org.simplelisp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.hjwylde.uni.swen222.lab02.org.simplelisp.compiler.Interpreter;
import com.hjwylde.uni.swen222.lab02.org.simplelisp.compiler.Parser;
import com.hjwylde.uni.swen222.lab02.org.simplelisp.error.ParseException;
import com.hjwylde.uni.swen222.lab02.org.simplelisp.lang.LispExpr;

/**
 * Main class
 * 
 * @author Henry J. Wylde
 * 
 * @since 5/08/2013
 */
public class Main {

    private Main() {}

    // Main method, used to run interpreter without GUI
    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();

        // now either run files on command-line or enter interactive mode...
        if (args.length == 0) {
            // interactive mode
            System.out.println("Simple Lisp Interpreter v1.0");
            System.out.println("Written by David J. Pearce, March 2006");
            System.out.println("");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            try {
                while (true) {
                    System.out.print("> ");
                    String text = input.readLine();
                    try {
                        LispExpr root = Parser.parse(text);
                        System.out.println(interpreter.evaluate(root));
                    } catch (ParseException e) {
                        System.err.println(e.getMessage());
                    }
                }
            } catch (IOException e) {
                System.err.println("I/O Error - " + e.getMessage());
            }
        } else
            try {
                interpreter.load(args[0]);
            } catch (FileNotFoundException e) {
                System.err.println("Unable to load \"" + args[0] + "\" " + e.getMessage());
            }
    }
}
