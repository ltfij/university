package com.hjwylde.uni.swen221.lab04.robotwar;

import java.awt.Point;
import java.util.Random;
import java.util.Scanner;

/*
 * Code for Laboratory 4, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Main {
    
    private static Random random = new Random();
    
    public static void main(String[] args) {
        Main.printWelcomeMessage();
        
        try (Scanner scanner = new Scanner(System.in)) {
            int width = Main.inputInteger("Width of Arena?", scanner);
            int height = Main.inputInteger("Height of Arena?", scanner);
            Battle battle = new Battle(width, height);
            int numRandomRobots = Main.inputInteger("How many randomBots?",
                scanner);
            int numTurfRobots = Main
                .inputInteger("How many turfBots?", scanner);
            
            for (int i = 0; i != numRandomRobots; i++)
                battle.addRobot(new RandomBot(new Point(Main
                    .randomInteger(battle.getArenaWidth()), Main
                    .randomInteger(battle.getArenaHeight()))));
            
            for (int i = 0; i != numTurfRobots; i++)
                battle.addRobot(new TurfBot(new Point(Main.randomInteger(battle
                    .getArenaWidth()), Main.randomInteger(battle
                    .getArenaHeight()))));
            
            while (battle.isRunning()) {
                // Play the turn
                battle.doRound();
                // Print out the display
                battle.print();
                // Get input
                Main.inputAnyKey(scanner);
            }
        }
        
        System.out.println("THE BATTLE IS OVER!!");
    }
    
    /**
     * Generate a random integer between 0 and n
     */
    public static int randomInteger(int n) {
        return Main.random.nextInt(n);
    }
    
    private static void inputAnyKey(Scanner scanner) {
        System.out.println("Press enter to continue");
        scanner.nextLine();
    }
    
    /**
     * Input an integer from the scanner. If an invalid input is given, then
     * keep trying until a valid input is given.
     * 
     * @param msg The message to print before reading the input
     * @param in The scanner from which to read the input
     * @return
     */
    private static int inputInteger(String msg, Scanner scanner) {
        while (true) {
            System.out.println(msg);
            try {
                int i = Integer.parseInt(scanner.nextLine());
                return i;
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer!");
            }
        }
    }
    
    /**
     * Print a simple welcome message for when the game starts.
     */
    private static void printWelcomeMessage() {
        System.out.println("Welcome to Robot War!");
    }
}
