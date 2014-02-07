package com.hjwylde.uni.swen221.lab03.polyquiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

/*
 * Code for Laboratory 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class Main {

    public static Random random = new Random(System.currentTimeMillis());

    public static String[] corrects = {"Nice one!", "Yeah, you got it!", "Yup!", "Sure thing!",
            "Oh yeah!", "Sweet as!", "No problem!", "Easy as!", "You're getting the hang of this!",
            "Way to go!", "You got it sorted!", "You're a winner!", "Yes"};

    public static String[] wrongs = {"Sorry", "Better luck next time", "Nope, that's not it",
            "Unlucky", "Close, but not close enough!", "That 'aint right",
            "Sorry, good effort though", "Nope. It's participation that counts, right?",
            "No.  Remember, there are no points for second place!",
            "Oh well, you'll get it next time!", "No", "Nope --- it's trickier than it looks!"};

    public static void main(String[] args) {
        ArrayList<PolyTest> tests = new ArrayList<>();

        try (Scanner scanner = new Scanner(System.in)) {
            tests.add(new PolyTest("Test1.java"));
            tests.add(new PolyTest("Test2.java"));
            tests.add(new PolyTest("Test3.java"));
            tests.add(new PolyTest("Test4.java"));
            tests.add(new PolyTest("Test5.java"));
            tests.add(new PolyTest("Test6.java"));
            tests.add(new PolyTest("Test7.java"));
            tests.add(new PolyTest("Test8.java"));
            tests.add(new PolyTest("Test9.java"));
            tests.add(new PolyTest("Test10.java"));
            tests.add(new PolyTest("Test11.java"));
            tests.add(new PolyTest("Test12.java"));
            tests.add(new PolyTest("Test13.java"));

            // randomly shuffle list of tests
            Collections.shuffle(tests);

            // go through every test
            int count = 0;
            int score = 0;
            for (PolyTest t : tests) {
                count = count + 1;
                score += Main.takeTest(t, scanner);
                System.out.println("=========================================");
                System.out.println("Score: " + score + " / " + count);
            }
            System.out.println("Final Score: " + score + " / " + count);
            // done!
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public static String selectOne(String[] strings) {
        int x = Main.random.nextInt(strings.length);
        return strings[x];
    }

    public static int takeTest(PolyTest test, Scanner scanner) {
        System.out.println("=========================================");
        System.out.print(test.getSource());
        System.out.println("=========================================");
        ArrayList<String> answers = new ArrayList<>(test.answers());
        Collections.shuffle(answers);
        int i = 1;
        for (String a : answers)
            System.out.println(i++ + ") " + a);
        System.out.print("\nWhat is your answer? ");
        int answer = -1;
        while (true) {
            String x = scanner.nextLine();
            answer = Integer.parseInt(x);
            if ((answer >= 1) && (answer <= answers.size()))
                break;
            System.out.println(Main.selectOne(Main.wrongs));
        }
        try {
            if (answers.get(answer - 1).equals(test.answers().get(0))) {
                System.out.println(Main.selectOne(Main.corrects));
                Thread.currentThread();
                Thread.sleep(1000);
                return 1;
            }

            System.out.println(Main.selectOne(Main.wrongs));
            Thread.currentThread();
            Thread.sleep(1000);
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
