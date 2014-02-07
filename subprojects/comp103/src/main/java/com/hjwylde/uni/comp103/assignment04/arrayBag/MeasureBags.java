package com.hjwylde.uni.comp103.assignment04.arrayBag;

/*
 * Code for Assignment 4, COMP 103 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
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
 * Measures the performance of different implementations of Bag by adding a large number of Strings
 * to a Bag. The strings to be added are read from a file and may or may not contain duplicates.
 */
public class MeasureBags implements ActionListener {

    // Fields
    private final JFrame frame;
    private final JTextArea textArea;
    private final JTextArea messageArea;

    private final List<String> data = new ArrayList<>();

    // Constructors
    /**
     * Construct a new MeasureBags object and set up the GUI
     */
    public MeasureBags() {

        frame = new JFrame("MeasureBags");
        frame.setSize(600, 400);

        // The message area, mainly for debugging.
        messageArea = new JTextArea(1, 80); // one line text area for messages.
        frame.getContentPane().add(messageArea, BorderLayout.SOUTH);

        // The text area
        textArea = new JTextArea(30, 30);
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

        // The buttons
        JPanel buttonPanel = new JPanel();
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);

        addButton(buttonPanel, "Random data");
        addButton(buttonPanel, "Read data");
        addButton(buttonPanel, "ArrayBag");
        addButton(buttonPanel, "SortedArrayBag");
        addButton(buttonPanel, "HashBag");
        addButton(buttonPanel, "Quit");

        frame.setVisible(true);
    }

    // GUI Methods

    /** Respond to button presses */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Read data"))
            readStrings();
        else if (e.getActionCommand().equals("Random data")) {
            int num = Integer.parseInt(JOptionPane.showInputDialog(frame, "Number of items"));
            constructStrings(num, (int) Math.ceil((Math.log(num) / Math.log(7)) - 1));
        } else if (e.getActionCommand().equals("ArrayBag")) {
            textArea.append("-------------\nMeasuring ArrayBag\n\n");
            measure(new ArrayBag<String>());
        } else if (e.getActionCommand().equals("SortedArrayBag")) {
            textArea.append("-------------\nMeasuring SortedArrayBag\n\n");
            measure(new SortedArrayBag<String>());
        } else if (e.getActionCommand().equals("HashBag")) {
            textArea.append("\n-------------\nMeasuring HashBag\n");
            measure(new HashBag<String>());
        } else if (e.getActionCommand().equals("Quit"))
            frame.dispose();
    }

    /**
     * Construct a List of data strings, The List will have n random strings, each with stringLength
     * characters. The characters are chosen at random from 'a'..'g'
     */
    public void constructStrings(int n, int stringLength) {
        messageArea.setText("Constructing " + n + " random strings of length " + stringLength
                + "\n");
        data.clear();
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            char[] chars = new char[stringLength];
            for (int c = 0; c < stringLength; c++)
                chars[c] = (char) (rand.nextInt(8) + 97);
            data.add(new String(chars));
        }
        textArea.setText("Constructed " + data.size() + " random strings of length " + stringLength);
    }

    // Other Methods

    public void measure(Collection<String> bag) {
        if (data == null) {
            textArea.append("No data to test on");
            return;
        }
        int distinct = 0;
        for (int i = 0; i < data.size(); i++)
            if (data.lastIndexOf(data.get(i)) == i)
                distinct++;

        long start = System.currentTimeMillis();
        for (String s : data)
            bag.add(s);

        long afterAdd = System.currentTimeMillis();
        for (String s : data)
            bag.contains(s);
        long afterContains = System.currentTimeMillis();
        for (String s : data)
            bag.remove(s);
        long afterRemove = System.currentTimeMillis();

        // report results
        textArea.append("Data has " + data.size() + " items, " + distinct + " distinct\n");
        textArea.append("Adding:   " + (afterAdd - start) + " milliseconds\n");
        textArea.append("Contains: " + (afterContains - afterAdd) + " milliseconds\n");
        textArea.append("Remove:   " + (afterRemove - afterContains) + " milliseconds\n\n");
        textArea.append("Total:    " + (afterRemove - start) + " milliseconds\n");
    }

    /**
     * Read strings from a file into a List. This ensures that the timing is based on the set
     * operations rather than the cost of reading data from the file system.
     */
    public void readStrings() {
        data.clear();

        String fname = FileChooser.open("Data File");
        if (fname == null)
            messageArea.setText("No file specified");
        else
            try (Scanner sc = new Scanner(new File(fname))) {
                while (sc.hasNext())
                    data.add(sc.nextLine());
                textArea.setText("Read " + data.size() + " strings from " + fname);
            } catch (IOException ex) {
                messageArea.setText("Reading data from " + fname + " FAILED");
            }
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
