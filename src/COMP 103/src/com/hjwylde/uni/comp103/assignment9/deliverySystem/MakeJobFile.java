package com.hjwylde.uni.comp103.assignment9.deliverySystem;

/*
 * Code for Assignment 9, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Create a random job file for testing CarlsCourier simulation. Each line in
 * the file contains a
 * description of a courier delivery job, giving the time when the order is
 * received, the charge
 * rate (P[remium], S[tandard] or B[udget]), and the destination suburb.
 * Possible destination
 * suburbs are read from the file places.txt (this also has distance information
 * which is used in
 * the simulation but is not needed here).
 * 
 * The program steps through the minutes in a day (from 9am to 4pm), choosing
 * whether to generate an
 * order and if so which kind of order. Times are in minutes from 9am, so range
 * from 1 to 420.
 * Budget jobs are not accepted after noon, nor are standard jobs after 2pm or
 * premium jobs after
 * 4pm. The program will not generate jobs that won't be accepted, but you can
 * suppress that test
 * and check that your program will reject them.
 * 
 * The program can take up to two command line arguments. The first argument
 * gives the stem for the
 * names of the files (default is 'test"), and the second specifies the number
 * of files to be
 * created (default is 1). If one argument is given, it is taken as the number
 * of files if it is a
 * number, and as the stem otherwise. Thus, if no argument is given, the program
 * creates a file
 * called test1.
 */

public class MakeJobFile {
    
    // Fields
    
    // Cumulative Probabilities for urgent, same day and next day in the next
    // minute (plain probablities are .01, .035 and .025).
    
    private static final double[] probability = {
        0.15, 0.25, 6
    };
    
    private static ArrayList<String> places = new ArrayList<>();
    
    private static Random rand = new Random();
    
    // Constructors (none)
    
    // Methods (all static!)
    
    public static void loadPlaces() {
        try (Scanner sc = new Scanner(new File("src/deliverySystem/places.txt"))) {
            while (sc.hasNext()) {
                MakeJobFile.places.add(sc.next());
                sc.nextLine();
            }
        } catch (IOException e) {
            System.out.println("ERROR: places.txt not found." + e);
        }
        System.out.println(MakeJobFile.places); // for testing
    }
    
    // Create a data file with the given name
    
    public static void main(String[] args) {
        
        int numFiles = 1; // Number of files to create - default is 1.
        
        String stem = "test"; // Stem for file names - default is "test"
        
        if (args.length == 2) {
            stem = args[0];
            numFiles = Integer.valueOf(args[1]);
        } else if ((args.length == 1) && args[0].matches("[0-9]+"))
            // The argument given is number of files
            numFiles = Integer.valueOf(args[0]);
        else if (args.length == 1)
            // The argument given is the file stem
            stem = args[0];
        
        System.out
            .println("  stem = " + stem + "number of files = " + numFiles); // for
                                                                            // testing
        
        // Read list of suburbs from places.txt.
        MakeJobFile.loadPlaces();
        
        // Create the required number of files
        for (int i = 1; i <= numFiles; i++) {
            MakeJobFile.createFile(stem + i);
            System.out.println("File " + stem + i + " created");
        }
        
    }
    
    // Main
    
    private static void createFile(String name) {
        
        /*
         * int[] limit = {
         * 420, 300, 180
         * };
         */
        
        try (PrintStream ps = new PrintStream(new File(name))) {
            for (int time = 1; time <= 520; time++) {
                double r = MakeJobFile.rand.nextDouble();
                int rate;
                String[] Rate = {
                    "P", "S", "B"
                };
                
                if (r < MakeJobFile.probability[0])
                    rate = 0;
                else if (r < MakeJobFile.probability[1])
                    rate = 1;
                else if (r < MakeJobFile.probability[2])
                    rate = 2;
                else
                    continue; // No order this period
                    
                // Check whether order should be accepted
                // Delete this to allow such tests in the job file
                // if (time >= limit[rate]) continue;
                
                String dest = MakeJobFile.places.get(MakeJobFile.rand
                    .nextInt(MakeJobFile.places.size()));
                String line = time + " " + Rate[rate] + " " + dest;
                ps.println(line);
                // System.out.println(line); // For testing
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
