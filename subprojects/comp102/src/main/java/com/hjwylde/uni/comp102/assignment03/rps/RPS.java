package com.hjwylde.uni.comp102.assignment03.rps;

import java.util.Scanner;

/*
 * Code for Assignment 3, COMP 102 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A program that plays Rock-Paper-Scissors with the user.
 */
public class RPS {

    // Rock = 1;
    // Paper = 2;
    // Scissors = 3;

    // Array for storing user choices.
    private final int[] pastChoices = {0, 0, 0};

    /** Play one round of RPS and print out the choices and result */
    public int playRound(Scanner scan) {
        // Determine computer choice, based on past choices made by the user.
        int computerChoice;
        System.out.println(calculateProbability(3));
        if (Math.random() <= calculateProbability(3))
            computerChoice = 1;
        else if (Math.random() <= calculateProbability(1))
            computerChoice = 2;
        else
            computerChoice = 3;

        System.out.print("Your choice (Rock = 1, Paper = 2, Scissors = 3): ");
        int yourChoice = scan.nextInt();
        pastChoices[yourChoice - 1]++;

        System.out.print("Computer choice: ");
        switch (computerChoice) {
            case (1):
                System.out.println("Rock");
                break;
            case (2):
                System.out.println("Paper");
                break;
            case (3):
                System.out.println("Scissors");
                break;
        }

        return ((2 * yourChoice) + computerChoice) % 3;
    }

    /** Play rounds of RPS until score = +- 5 */
    public void playRPS() {
        int score = 0;
        int result = 0;

        try (Scanner scan = new Scanner(System.in)) {
            // Play until user hits -5 or 5.
            while ((score < 5) && (score > -5)) {
                System.out.println("------------------------------------------");
                result = playRound(scan);
                switch (result) {
                    case (0):
                        System.out.println("Draw.");
                        break;
                    case (1):
                        System.out.println("You lose.");
                        score -= 1;
                        break;
                    case (2):
                        System.out.println("You win.");
                        score += 1;
                        break;
                }
            }
        }

        System.out.println("------------------------------------------");
        System.out.println("All done, your score was: " + score);
    }

    private double calculateProbability(int choice) {
        int totalChoices = pastChoices[0] + pastChoices[1] + pastChoices[2];
        if (totalChoices == 0)
            return 1 / 3;

        return pastChoices[choice - 1] / totalChoices;
    }

    public static void main(String[] args) {
        new RPS().playRPS();
    }
}
