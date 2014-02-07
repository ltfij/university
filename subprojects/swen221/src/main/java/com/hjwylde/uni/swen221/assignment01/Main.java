package com.hjwylde.uni.swen221.assignment01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/*
 * Code for Assignment 1, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class Main {

    public static void main(String[] args) throws Throwable {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int total = 0;
        int passes = 0;

        while (true) {
            String input = null;
            String regex = null;
            try {
                input = reader.readLine();
                if (input == null)
                    // end-of-file reached.
                    break;
                regex = reader.readLine();
                if (regex == null)
                    break;

                total = total + 1;

                boolean theiranswer = Main.runStudentMatcher(input, regex);
                boolean rightanswer = Main.matchWithLib(input, regex);

                if (theiranswer == rightanswer) {
                    System.out.println("Test " + total + ": PASSED");
                    System.out.println("Input: " + input);
                    System.out.println("Regex: " + regex);
                    if (rightanswer)
                        System.out.println("Matched (correct)\n");
                    else
                        System.out.println("No match (correct)\n");
                    passes++;
                } else {
                    System.out.println("Test " + total + ": FAILED");
                    System.out.println("Input: " + input);
                    System.out.println("Regex: " + regex);
                    if (rightanswer)
                        System.out.println("No match (incorrect)\n");
                    else
                        System.out.println("Matched (incorrect)\n");
                }
            } catch (Throwable e) {
                System.out.println("Test " + total + ": FAILED");
                System.out.println("Input: " + input);
                System.out.println("Regex: " + regex);
                System.out.println("Exception occurred (" + e.getClass().getName() + "): "
                        + e.getMessage());
                System.out.println();
            }
        }

        System.out.println("Exactly " + passes + " / " + total + " tests passed.");
        System.err.print("= " + passes + " / " + total);
        System.exit(0);
    }

    protected static boolean matchWithLib(String text, String regex) {
        Pattern p = Pattern.compile(regex);
        java.util.regex.Matcher m = p.matcher(text);
        return m.find();
    }

    @SuppressWarnings("deprecation")
    protected static boolean runStudentMatcher(String input, String regex) {
        TimerThread timer = new TimerThread(5, Thread.currentThread());
        timer.start();
        try {
            return new BasicMatcher().match(input, regex);
        } finally {
            // stop timer to prevent it causing an exception after this point!!
            timer.stop();
        }
    }

    private static class TimedOutException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public TimedOutException(String msg) {
            super(msg);
        }
    }

    private static final class TimerThread extends Thread {

        private int timeout;
        private Thread target;

        public TimerThread(int t, Thread target) {
            timeout = t;
            this.target = target;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void run() { // how to let this terminate gracefully?
            try {
                Thread.sleep(timeout * 1000);
            } catch (InterruptedException e) {
            }

            // the following will cause the thread in question to throw a timed-out exception
            target.stop(new TimedOutException("method timed out after " + timeout + "s"));
        }
    }

}
