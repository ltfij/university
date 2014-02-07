package com.hjwylde.uni.swen221.lab03.polyquiz;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*
 * Code for Laboratory 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * This class represents a single test in the system.
 * 
 * @author djp
 * 
 */
public class PolyTest {

    /**
     * Source code for the test.
     */
    private String source;

    /**
     * Possible answers for the test. First one is always the right answer.
     */
    private ArrayList<String> answers;

    /**
     * Create a PolyTest
     * 
     * @param filename name of file for this test
     */
    public PolyTest(String filename) throws IOException {
        source = "";
        answers = new ArrayList<>();
        // Load the file using a resource URL. This means the file will be
        // loaded, regardless of whether it's in a jar file or not.
        java.net.URL file = PolyTest.class.getResource(filename);
        if (file == null)
            throw new FileNotFoundException(filename);
        // Read the source file line by line, and attempt to strip out the
        // answers provided.
        BufferedReader f = new BufferedReader(new InputStreamReader(file.openStream()));
        String line;
        while ((line = f.readLine()) != null)
            // check if this line is an answer line or not.
            if (line.startsWith("// ANSWER: ")) {
                String answer = line.substring(11);
                answers.add(answer);
            } else
                source += line + "\n";
    }

    public List<String> answers() {
        return answers;
    }

    public String getSource() {
        return source;
    }
}
