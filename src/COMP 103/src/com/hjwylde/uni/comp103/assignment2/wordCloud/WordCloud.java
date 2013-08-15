package com.hjwylde.uni.comp103.assignment2.wordCloud;

/*
 * Code for Assignment 2, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/*
 * Henry Wylde:
 * 
 * For the Challenge section:
 * 
 * Verticle Dimension Use: I have used the verticle axis to help people find
 * words more easily that
 * they're looking for to compare. For this, I have listed them alphabetically,
 * from a to z, top to
 * bottom. The y position is evenly spread out based on the letter/number
 * combination (0 to 25, for
 * a to z). The font size varies between 10 and 30, rather than 10 and 60, as
 * with a 660 (maximized
 * on a 768 height resolution screen) height canvas the line heights will be
 * about 25 (checked using
 * FontMetrics), so this should keep the a to z lines spread out enough to not
 * overlap each other
 * enough to make it hard to read.
 * 
 * Colour Use: I thought it would be very interesting to colour the words by the
 * score that they
 * would get in Scrabble. For this, there is a HashMap with the values of each
 * letter in it and a
 * method used to calculate the value of a word (not including 50pts for more
 * than 7 letters). After
 * calculating the value of a word, it is passed to a method that determines the
 * colour for that
 * word, the colour coding goes from red -> orange -> yellow -> green -> magenta
 * -> cyan -> blue in
 * order of highest to lowest score. The highest word score for the text files
 * given in this
 * assignment was about 36.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

import com.hjwylde.uni.comp103.util.DrawingCanvas;
import com.hjwylde.uni.comp103.util.FileChooser;

/**
 * This program reads 2 text files and compiles word counts for each. It then
 * eliminates rare words,
 * and words that only occur in one document, and displays the remainder as a
 * "word cloud" on a
 * canvas, to allow the user to examine differences between the word usage in
 * the two documents.
 */
public class WordCloud implements ActionListener {
    
    // Fields:
    // The two maps.
    private final Map<String, Double> counts1, counts2;
    // And a map for Scrabble letter values.
    private Map<String, Integer> letterValues;
    
    // GUI stuff...
    private DrawingCanvas canvas;
    private JFrame frame;
    private JTextArea messageArea;
    private final int canvasWidth = 800, canvasHeight = 600;
    
    private int numWordsToRemove = 1000;
    
    // Constructor
    
    /**
     * Constructs a WordCloud object, sets up the graphical user interface, and
     * calls the basic
     * method.
     */
    public WordCloud() {
        setUpGUI();
        setUpScrabbleMap();
        
        String fname1 = FileChooser.open("First file to read text from.");
        counts1 = buildHistogram(fname1);
        messageArea.setText("Text read from " + fname1);
        
        String fname2 = FileChooser.open("Second filename to read text from.");
        counts2 = buildHistogram(fname2);
        messageArea.setText("Text read from " + fname2);
        
        if ((fname1 == null) || (fname2 == null))
            frame.dispose();
        
        displayWords();
    }
    
    // Methods
    
    /** GUI stuff: respond to button presses */
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();
        messageArea.setText("Button: " + name);
        
        if (name.equals("Remove standard common words")) {
            String fname = FileChooser.open("Filename to read text from");
            if (fname == null)
                return;
            
            messageArea.setText("Getting ignorable words from " + fname);
            
            // set the elements of the toRemove Set to be the words in file
            try (Scanner f = new Scanner(new File(fname))) {
                Set<String> toRemove = new HashSet<>();
                
                f.useDelimiter("\\W");
                while (f.hasNext()) {
                    String str = f.next().toLowerCase(Locale.ENGLISH).trim();
                    toRemove.add(str);
                }
                
                // remove the words
                removeWords(counts1, toRemove);
                removeWords(counts2, toRemove);
            } catch (IOException ex) { // what to do if there is an io error.
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
            
        }
        
        if (name.equals("Remove infrequent words")) {
            messageArea.setText("Keeping only the most common "
                + numWordsToRemove + " words");
            removeInfrequentWords(counts1, numWordsToRemove);
            removeInfrequentWords(counts2, numWordsToRemove);
            numWordsToRemove = numWordsToRemove / 2; // It halves each time.
        }
        
        if (name.equals("Remove un-shared words")) {
            messageArea.setText("Keeping only words that occur in BOTH docs ");
            Set<String> wordsToBeRemoved = new HashSet<>();
            for (String wd : counts1.keySet())
                if (!counts2.keySet().contains(wd))
                    wordsToBeRemoved.add(wd);
            for (String wd : counts2.keySet())
                if (!counts1.keySet().contains(wd))
                    wordsToBeRemoved.add(wd);
            // Now actually remove them.
            removeWords(counts1, wordsToBeRemoved);
            removeWords(counts2, wordsToBeRemoved);
        } else if (name.equals("Quit"))
            frame.dispose();
        
        // Now redo everything on the screen
        displayWords();
    }
    
    /**
     * Read the contents of a file, counting how often each word occurs. Put the
     * counts (as Doubles)
     * into a Map, which is returned.
     */
    public Map<String, Double> buildHistogram(String filename) {
        if ((filename == null) || filename.equals(""))
            return null;
        
        Map<String, Double> wordCounts = new HashMap<>();
        
        // Open the file and get ready to read from it.
        try (Scanner f = new Scanner(new File(filename))) {
            f.useDelimiter("\\W"); // Tells scanner to remove all punctuation. Original: [^-a-zA-Z']
                                   // I
                                   // changed this due to it still including "-" at the start of
                                   // some
                                   // words.
            
            String word = null;
            while (f.hasNext()) {
                word = f.next().toLowerCase(Locale.ENGLISH); // For consistancy.
                
                if (word.length() == 0) // Move on to the next token if this word is "".
                    continue;
                
                // Increment the words count in the hashMap
                if (wordCounts.containsKey(word))
                    wordCounts.put(word, wordCounts.get(word) + 1);
                else
                    wordCounts.put(word, 1.0);
            }
            
            return wordCounts;
        } catch (IOException ex) { // What to do if there is an io error.
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public Color calculateWordColor(int wordScore) {
        if (wordScore <= 0)
            return Color.BLACK;
        
        // Note: Max wordscore for the textfiles given was 36
        
        if (wordScore > 24)
            return Color.RED;
        else if (wordScore > 20)
            return Color.ORANGE;
        else if (wordScore > 16)
            return Color.YELLOW;
        else if (wordScore > 12)
            return Color.GREEN;
        else if (wordScore > 8)
            return Color.MAGENTA;
        else if (wordScore > 4)
            return Color.CYAN;
        else
            return Color.BLUE;
    }
    
    public int calculateWordScore(String word) {
        if (word == null)
            return 0;
        
        int score = 0;
        for (int i = 0; i < word.length(); i++)
            score += letterValues.get(String.valueOf(word.charAt(i)));
        
        return score;
    }
    
    /**
     * Display words that exist in both documents. The y-position is essentially
     * random (it just
     * depends on the order in which an iterator goes through a Set). However the
     * y-position reflects
     * how much the word is used in the 1st document versus the 2nd. That is, a
     * word that is common in
     * the 1st and uncommon in the second should appear at the top. The SIZE of
     * the word as displayed
     * reflects how common the word is overall, including its count over BOTH
     * documents.
     */
    public void displayWords() {
        if ((counts1 == null) || (counts2 == null))
            return;
        
        canvas.clear();
        
        // First we re-normalise the counts.
        normaliseCounts(counts1);
        normaliseCounts(counts2);
        
        // Used in determining the font size.
        Set<String> allWords = findAllWords();
        double largestCount = findLargestCount(allWords);
        
        // For the position of the words.
        int xPosition = 0;
        int yPosition = 0;
        
        for (String word : allWords) {
            double count1, count2;
            if (counts1.get(word) != null)
                count1 = counts1.get(word);
            else
                count1 = 0.0;
            
            if (counts2.get(word) != null)
                count2 = counts2.get(word);
            else
                count2 = 0.0;
            
            xPosition = (int) ((count2 / (count1 + count2)) * canvas.getWidth() * 0.9);
            yPosition = ((word.charAt(0) - 97) * (canvas.getHeight() / 26)) + 10;
            
            // Set the font size based on a ratio to the largestCount.
            // Set the color based on the word score.
            canvas
                .setFontSize((int) ((((count1 + count2) * 20) / largestCount) + 10));
            canvas.setColor(calculateWordColor(calculateWordScore(word)));
            canvas.drawString(word, xPosition, yPosition);
            
        }
    }
    
    /**
     * Construct and return a Set of all the words that occur in EITHER document.
     */
    public Set<String> findAllWords() {
        Set<String> allWords = new HashSet<>(); // Set to contain all the words.
        
        allWords.addAll(counts1.keySet()); // Add the first text's words to the
                                           // set...
        allWords.addAll(counts2.keySet()); // Add the second text's words to the
                                           // set...
        
        return allWords;
    }
    
    /**
     * Find the largest value/count for a word in a collection
     * 
     * @param allWords the collection to search.
     * @return the largest count.
     */
    public double findLargestCount(Set<String> allWords) {
        double largestCount = Double.MIN_VALUE;
        
        // The count for a word is it's total ratio in counts1 and counts2.
        for (String word : allWords)
            if (counts1.containsKey(word) && counts2.containsKey(word))
                largestCount = Math.max(largestCount, counts1.get(word)
                    + counts2.get(word));
            else if (counts1.containsKey(word))
                largestCount = Math.max(largestCount, counts1.get(word));
            else
                largestCount = Math.max(largestCount, counts2.get(word));
        
        return largestCount;
    }
    
    /**
     * Take a Map from words to counts, and "normalise" the counts, so that they
     * are fractions of the
     * total: they should sum to one.
     */
    public void normaliseCounts(Map<String, Double> counts) {
        if (counts == null)
            return;
        
        // Figure out the total in the current Map.
        double total = 0.0;
        for (String wd : counts.keySet())
            total += counts.get(wd);
        
        // Divide all values by the total, so they will sum to one.
        for (String wd : counts.keySet())
            counts.put(wd, counts.get(wd) / total);
    }
    
    /**
     * Print the words and their counts to standard out. Not necessary to the
     * program, but might be
     * useful for debugging
     */
    public void printCounts(Map<String, Double> counts) {
        if (counts == null) {
            System.out.println("The Map is empty");
            JOptionPane.showMessageDialog(null, "The Map is empty");
            return;
        }
        
        for (String s : counts.keySet())
            System.out.printf("%15s \t : \t %.8f \n", s, counts.get(s));
        
        System.out.println("----------------------------------");
        return;
    }
    
    /**
     * Takes a Map from strings to integers, and an integer, limitNumWords. It
     * should leave this Map
     * containing only the limitNumWords most common words in the original.
     */
    public void removeInfrequentWords(Map<String, Double> c, int limitNumWords) {
        if (c.size() < limitNumWords)
            return;
        
        String key;
        double value;
        
        Map<String, Double> c2 = new HashMap<>();
        
        for (int i = 0; i < limitNumWords; i++) {
            key = null;
            value = 0;
            
            for (Map.Entry<String, Double> entry : c.entrySet())
                if (entry.getValue() > value) {
                    value = entry.getValue();
                    key = entry.getKey();
                }
            
            c.remove(key);
            c2.put(key, value);
        }
        
        c.clear();
        c.putAll(c2);
    }
    
    /**
     * Take a word count Map, and a Set of words. Remove those words from the map.
     */
    public void removeWords(Map<String, Double> c, Set<String> wds) {
        for (String word : wds)
            c.remove(word);
    }
    
    // -- GUI stuff --------------------------------------------------------
    
    /** set up the GUI (Graphical User Interface) stuff */
    public void setUpGUI() {
        
        frame = new JFrame("WordCloud - comparing two documents");
        frame.setSize(canvasWidth, canvasHeight);
        
        // The canvas
        canvas = new DrawingCanvas();
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        
        // The message area, mainly for debugging.
        messageArea = new JTextArea(1, 80); // one line text window for messages
        frame.getContentPane().add(messageArea, BorderLayout.SOUTH);
        
        // The button panel
        JPanel buttonPanel = new JPanel();
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        
        // The buttons
        JButton readWordsToremoveButton = new JButton(
            "Remove standard common words");
        readWordsToremoveButton.addActionListener(this);
        buttonPanel.add(readWordsToremoveButton);
        
        JButton readremoveInfrequentWordsButton = new JButton(
            "Remove infrequent words");
        readremoveInfrequentWordsButton.addActionListener(this);
        buttonPanel.add(readremoveInfrequentWordsButton);
        
        JButton readremoveUnsharedWordsButton = new JButton(
            "Remove un-shared words");
        readremoveUnsharedWordsButton.addActionListener(this);
        buttonPanel.add(readremoveUnsharedWordsButton);
        
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(this);
        buttonPanel.add(quitButton);
        
        frame.setVisible(true);
        
        displayWords();
    }
    
    private void setUpScrabbleMap() {
        letterValues = new HashMap<>();
        
        // Assign a value to each letter.
        letterValues.put("a", 1);
        letterValues.put("b", 3);
        letterValues.put("c", 3);
        letterValues.put("d", 2);
        letterValues.put("e", 1);
        letterValues.put("f", 4);
        letterValues.put("g", 2);
        letterValues.put("h", 4);
        letterValues.put("i", 1);
        letterValues.put("j", 8);
        letterValues.put("k", 5);
        letterValues.put("l", 1);
        letterValues.put("m", 3);
        letterValues.put("n", 1);
        letterValues.put("o", 1);
        letterValues.put("p", 3);
        letterValues.put("q", 10);
        letterValues.put("r", 1);
        letterValues.put("s", 1);
        letterValues.put("t", 1);
        letterValues.put("u", 1);
        letterValues.put("v", 4);
        letterValues.put("w", 4);
        letterValues.put("x", 8);
        letterValues.put("y", 4);
        letterValues.put("z", 10);
        letterValues.put("_", 0);
        letterValues.put("0", 0);
        letterValues.put("1", 0);
        letterValues.put("2", 0);
        letterValues.put("3", 0);
        letterValues.put("4", 0);
        letterValues.put("5", 0);
        letterValues.put("6", 0);
        letterValues.put("7", 0);
        letterValues.put("8", 0);
        letterValues.put("9", 0);
    }
    
    public static void main(String[] args) {
        new WordCloud();
    }
    
}
