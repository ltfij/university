package com.hjwylde.uni.comp103.assignment8.bstBag;

/*
 * Code for Assignment 8, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

import com.hjwylde.uni.comp103.util.FileChooser;

/**
 * Measure the performance of different Bag implementations by timing a list of
 * bag operations. Can
 * perform two kinds of test on any of the available bag implementations: 1.
 * Generate a random list
 * of strings then measure the time to add them all, test them all, and them
 * remove them all. 2.
 * Read a list of strings from a file, each preceded by +, - or ? indicating
 * whether it is to be
 * added, removed or tested, and measure the time to perform all operations on
 * the seleeted
 * implementation. There is a button to generate random data and one to read
 * data from a file, and a
 * button for each bag implementation, which runs the appropriate experiment
 * (according to which of
 * the first two was used most recently) using that implementation.
 */

public class MeasureBags implements ActionListener {
    
    // Fields
    
    private final JFrame frame;
    private final JTextArea textArea;
    private final JTextArea messageArea;
    
    private final String[] buttons = {
        "Random data", "Load file", "Array", "Sorted Array", "Linked List",
        "BST", "Hash", "Quit"
    };
    
    // Ensure that only one of these is non-null, so we can tell what
    // experiment to peform.
    private List<String> fileData = null;
    private List<String> randomData = null;
    
    // Constructors
    
    /**
     * Construct a new MeasureBags object and set up the GUI
     */
    
    public MeasureBags() {
        
        frame = new JFrame("MeasureBags");
        frame.setSize(800, 600);
        
        // The message area, mainly for debugging.
        messageArea = new JTextArea(1, 80); // one line text area for messages.
        frame.getContentPane().add(messageArea, BorderLayout.SOUTH);
        
        // The text area
        textArea = new JTextArea(30, 30);
        frame.getContentPane().add(new JScrollPane(textArea),
            BorderLayout.CENTER);
        
        // The buttons
        JPanel buttonPanel = new JPanel();
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        
        for (String label : buttons)
            addButton(buttonPanel, label);
        
        frame.setVisible(true);
        
    }
    
    // GUI Methods
    
    /** Respond to button presses */
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        int k = 0;
        
        while (!buttons[k].equals(e.getActionCommand()))
            k++;
        
        switch (k) {
        
        case 0: // "Random data"
            int num = Integer.parseInt(JOptionPane.showInputDialog(frame,
                "Number of items"));
            generateStrings(num, 6);
            break;
        
        case 1: // "Load file"
            textArea.append("-------------\nLoading test file...\n");
            loadTestFile();
            textArea.append("Done\n\n");
            break;
        
        case 2: // "Array"
            textArea.append("-------------\nMeasuring ArrayBag\n\n");
            runExperiment(new ArrayBag<String>());
            break;
        
        case 3: // "Sorted Array"
            textArea.append("-------------\nMeasuring SortedArrayBag\n\n");
            runExperiment(new SortedArrayBag<String>());
            break;
        
        case 4: // "Linked List"
            textArea.append("-------------\nMeasuring LinkedListBag\n\n");
            runExperiment(new LinkedListBag<String>());
            break;
        
        case 5: // "BST"
            textArea.append("-------------\nMeasuring BSTBag\n\n");
            runExperiment(new BSTBag<String>());
            break;
        
        case 6: // "Hash"
            textArea.append("\n-------------\nMeasuring HashBag\n");
            runExperiment(new HashBag<String>());
            break;
        
        case 7: // "Quit"
            frame.dispose();
            break;
        
        default:
            textArea.append("ERROR: Invalid option\n"); // Just in case!
            
        }
        
    }
    
    /**
     * Generate a List of n strings, each with stringLength characters, chosen at
     * random from
     * 'a'..'g'. Then make a list of test commands to add all of the strings, test
     * whether each is in
     * the bag, then remove them all.
     */
    
    public void generateStrings(int n, int stringLength) {
        messageArea.setText("Constructing " + n + " random strings of length "
            + stringLength);
        randomData = new ArrayList<>();
        fileData = null;
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            char[] chars = new char[stringLength];
            for (int c = 0; c < stringLength; c++)
                chars[c] = (char) (rand.nextInt(8) + 97);
            randomData.add(new String(chars));
        }
        textArea.setText("Constructed " + randomData.size()
            + " random strings of length " + stringLength + "\n");
    }
    
    // Other Methods
    
    /**
     * Read all of the operations from a test files into data (List). Each line
     * must be either +s to
     * add string s, -s to delete s, or ?s to test whether s is in the bag.
     */
    
    public void loadTestFile() {
        fileData = new ArrayList<>();
        randomData = null;
        String fname = FileChooser.open("Filename to read from");
        if (fname == null) {
            JOptionPane.showMessageDialog(null, "No file name specified");
            return;
        }
        try (Scanner f = new Scanner(new File(fname))) {
            // open the file and get ready to read from it
            
            // Store the contents of the file in data
            // Check that each line starts with +, - or ?
            while (f.hasNext()) {
                // String str = f.next().trim();
                String str = f.nextLine();
                char c = str.charAt(0);
                if ((c == '+') || (c == '-') || (c == '?'))
                    fileData.add(str); // add str to the data list
                else
                    JOptionPane.showMessageDialog(null, "Invalid string " + str
                        + " ignored");
            }
        } catch (IOException ex) { // what to do if there is an io error.
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Perform an experiment using the selected bag implementation, according to
     * whether the
     * "Random data" or "Load file" button was clicked most recently. Should never
     * have randomData and
     * fileData both non-null.
     */
    
    public void runExperiment(Collection<String> bag) {
        
        assert (randomData != null) || (fileData != null);
        
        if (randomData != null) {
            runRandomExperiment(bag);
            return;
        }
        
        if (fileData != null) {
            runFileExperiment(bag);
            return;
        }
        
        // randomData and fileData both null
        textArea.setText("No data");
        
    }
    
    /**
     * Measure the performance of a bag implementation on a given set of data
     * Measure how long it
     * takes and print out average time per element in nanoseconds, and number of
     * adds, removes and
     * tests performed. We know that bag and randomData are both non-null.
     */
    public void runFileExperiment(Collection<String> bag) {
        assert (bag != null) && (fileData != null);
        
        long start = System.currentTimeMillis();
        
        for (String s : fileData) {
            String prefix = s.substring(0, 1);
            String element = s.substring(1);
            
            assert (prefix.contains("[+-?]"));
            
            if (prefix.equals("+"))
                bag.add(element);
            else if (prefix.equals("-"))
                bag.remove(element);
            else
                bag.contains(element);
        }
        
        long end = System.currentTimeMillis();
        
        String ms = " milliseconds\n";
        double size = fileData.size();
        double average = 0.0;
        if (size != 0)
            average = (end - start) / size;
        
        textArea.append("Data has " + size + " items\n");
        textArea.append("Total:    " + (end - start) + ms);
        textArea.append("Average:      " + average + ms);
    }
    
    /**
     * Perform an experiment using randomly generated data. Measure the time taken
     * to add all of the
     * strings, test all of the strings, and remove all of the strings. Display
     * the time taken for
     * each set of operations. We know that bag and randomData are both non-null.
     */
    
    public void runRandomExperiment(Collection<String> bag) {
        assert (bag != null) && (randomData != null);
        
        long start = System.currentTimeMillis();
        
        for (String s : randomData)
            bag.add(s);
        
        long afterAdd = System.currentTimeMillis();
        
        for (String s : randomData)
            bag.contains(s);
        
        long afterContains = System.currentTimeMillis();
        
        for (String s : randomData)
            bag.remove(s);
        
        long afterRemove = System.currentTimeMillis();
        
        // report results
        String ms = " milliseconds\n";
        textArea.append("Data has " + randomData.size() + " items\n");
        textArea.append("Total:    " + (afterRemove - start) + ms);
        textArea.append("Add:      " + (afterAdd - start) + ms);
        textArea.append("Contains: " + (afterContains - afterAdd) + ms);
        textArea.append("Remove:   " + (afterRemove - afterContains) + ms);
    }
    
    /** Helper method for adding buttons */
    
    private JButton addButton(JPanel panel, String name) {
        JButton button = new JButton(name);
        button.addActionListener(this);
        panel.add(button);
        return button;
    }
    
    // Main
    public static void main(String[] arguments) {
        new MeasureBags();
    }
    
}
