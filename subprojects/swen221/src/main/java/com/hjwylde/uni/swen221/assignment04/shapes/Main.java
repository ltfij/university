package com.hjwylde.uni.swen221.assignment04.shapes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.hjwylde.uni.swen221.assignment04.shapes.parser.Interpreter;

/*
 * Code for Assignment 4, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Main.
 * 
 * @author Henry J. Wylde
 */
public class Main {

    /**
     * Main.
     * 
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        if (args.length == 0)
            return;

        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(new File(args[0])))) {
            String line;
            try {
                while ((line = br.readLine()) != null)
                    sb.append(line + " ");
            } catch (IOException e) {
                e.printStackTrace();
            }

            new Interpreter(sb.toString()).run().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
