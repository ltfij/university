package com.hjwylde.uni.comp103.assignment03.recordSorter;

/*
 * Code for Assignment 3, COMP 103 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

import java.util.ArrayList;
import java.util.List;

/** The Record class!! A Record is just a List of Strings. */
public class Record implements Comparable<Record> {

    private final List<String> textlines = new ArrayList<>();

    /** This constructor doesn't even set the fields! */
    public Record() {}

    public void addTextline(String str) {
        textlines.add(str); // add at end
    }

    /**
     * Compares this record to another based on the average letter character score.
     * 
     * @param r The record to compare to
     */
    @Override
    public int compareTo(Record r) {
        double totalScore = 0;
        double totalCount = 0;

        for (String line : textlines)
            for (char c : line.toCharArray())
                if (Character.isLetter(c)) {
                    totalScore += c;
                    totalCount++;
                }

        double avgScore1 = totalScore / totalCount;

        totalScore = 0;
        totalCount = 0;

        for (int i = 0; i < r.getNumTextlines(); i++)
            for (char c : r.getTextline(i).toCharArray())
                if (Character.isLetter(c)) {
                    totalScore += c;
                    totalCount++;
                }

        double avgScore2 = totalScore / totalCount;

        if (avgScore1 > avgScore2)
            return -1;
        else if (Math.abs(avgScore1 - avgScore2) < 0.0000000001) // Floating point equality.
            return 0;
        else
            return 1;
    }

    public int getNumTextlines() {
        return textlines.size();
    }

    public String getTextline(int i) {
        return textlines.get(i);
    }

    // Methods
    public void setTextline(int i, String str) {
        textlines.add(i, str);
    }
}
