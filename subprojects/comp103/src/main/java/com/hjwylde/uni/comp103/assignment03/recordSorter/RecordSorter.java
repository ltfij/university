package com.hjwylde.uni.comp103.assignment03.recordSorter;

/*
 * Code for Assignment 3, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

import com.hjwylde.uni.comp103.util.FileChooser;

public class RecordSorter implements ActionListener {
    
    // Fields
    private final JFrame frame;
    private final JTextArea textArea;
    private final List<Record> records;
    
    // Constructor
    /**
     * Constructs a RecordSorter object and sets up the graphical user interface
     */
    public RecordSorter() {
        records = new ArrayList<>();
        
        frame = new JFrame("Demo Files and Text Output");
        frame.setSize(800, 600);
        
        // The text area
        textArea = new JTextArea(30, 80); // ie. 30 lines long, 80 chars wide
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.black);
        textArea.setForeground(Color.cyan);
        frame.getContentPane().add(new JScrollPane(textArea),
            BorderLayout.CENTER); // puts
                                  // the
                                  // text
                                  // area
                                  // into
                                  // the
                                  // frame.
        
        // The button panel
        JPanel topPanel = new JPanel();
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        JPanel botPanel = new JPanel();
        frame.getContentPane().add(botPanel, BorderLayout.SOUTH);
        
        // The buttons
        JButton readButton = new JButton("Read a file");
        readButton.addActionListener(this);
        topPanel.add(readButton);
        
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(this);
        topPanel.add(quitButton);
        
        JButton sortNumCButton = new JButton("#lines");
        sortNumCButton.addActionListener(this);
        botPanel.add(sortNumCButton);
        
        JButton sortLineLengthButton = new JButton("line length");
        sortLineLengthButton.addActionListener(this);
        botPanel.add(sortLineLengthButton);
        
        JButton sortLastLineButton = new JButton("last line");
        sortLastLineButton.addActionListener(this);
        botPanel.add(sortLastLineButton);
        
        JButton uppercasePropButton = new JButton("uppercase proportion");
        uppercasePropButton.addActionListener(this);
        botPanel.add(uppercasePropButton);
        
        JButton averageWordLengthButton = new JButton("average word length");
        averageWordLengthButton.addActionListener(this);
        botPanel.add(averageWordLengthButton);
        
        JButton averageLetterScoreButton = new JButton("average letter score");
        averageLetterScoreButton.addActionListener(this);
        botPanel.add(averageLetterScoreButton);
        
        frame.setVisible(true);
    }
    
    // Methods
    /** Respond to button presses */
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();
        if (name.equals("Read a file")) {
            ReadAFile();
            ShowRecords();
        } else if (name.equals("#lines")) {
            Collections.sort(records, new numTextlinesComparator());
            ShowRecords();
        } else if (name.equals("line length")) {
            Collections.sort(records, new textLineLengthComparator());
            ShowRecords();
        } else if (name.equals("last line")) {
            Collections.sort(records, new alphabeticalLastLineComparator());
            ShowRecords();
        } else if (name.equals("uppercase proportion")) {
            Collections.sort(records, new uppercaseProportionComparator());
            ShowRecords();
        } else if (name.equals("average word length")) {
            Collections.sort(records, new averageWordLengthComparator());
            ShowRecords();
        } else if (name.equals("average letter score")) {
            Collections.sort(records);
            ShowRecords();
        } else if (name.equals("Quit"))
            frame.dispose();
    }
    
    /**
     * Read the contents of a file, and store the contents in Record objects.
     */
    public void ReadAFile() {
        records.clear();
        // ask the user for the name of the file to read
        String fname = FileChooser.open("Filename to read text from");
        if (fname == null) {
            JOptionPane.showMessageDialog(null, "No file name specified");
            return;
        }
        
        // open the file and get ready to read from it
        try (Scanner f = new Scanner(new File(fname))) {
            // clear text area before appending the new text
            textArea.setText("Reading from file " + fname
                + ":\n====================\n");
            
            // Store the contents of the file in a list of Record objects
            records.clear();
            Record record = new Record();
            String line;
            
            while (f.hasNextLine()) {
                line = f.nextLine();
                
                if (line.equals("")) {
                    // Add previous record if it has a value, create new one
                    if (record.getNumTextlines() != 0) // This is incase some files have
                                                       // more than 1
                                                       // consecutive empty line
                        records.add(record);
                    record = new Record();
                    
                    continue; // Skip to next line, don't add the "".
                }
                
                record.addTextline(line);
            }
            
            if (record.getNumTextlines() != 0) // If the last record in the file
                                               // wasn't added
                records.add(record);
            
            textArea.append("....\n DONE\n");
        } catch (IOException ex) { // what to do if there is an io error.
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Show the contents of a file, by printing to the (cleared) textArea
     */
    public void ShowRecords() {
        textArea.setText(""); // blanks the text area.
        
        Record record;
        for (int i = 0; i < records.size(); i++) {
            record = records.get(i);
            
            textArea.append("Record " + i + ":\n");
            for (int j = 0; j < record.getNumTextlines(); j++)
                textArea.append(record.getTextline(j) + "\n");
            
            textArea.append("\n");
        }
    }
    
    // ------------------------------------------------------
    // Main
    public static void main(String[] args) {
        new RecordSorter();
    }
    
    // ------------------------------------------------------
    
    // The comparator classes follow. They don't *have* to be inner
    // classes of RecordSorter, and they don't have to be private, but
    // here they are. Essentially this reflects the fact that no other
    // class has any business using them!
    
    // The first one is done for you:
    
    /**
     * Comparator that will order Records alphabetically on their last line.
     */
    private class alphabeticalLastLineComparator implements Comparator<Record> {
        
        @Override
        public int compare(Record record1, Record record2) {
            return record1.getTextline(record1.getNumTextlines() - 1)
                .compareToIgnoreCase(
                    record2.getTextline(record2.getNumTextlines() - 1));
        }
    }
    
    /**
     * Comparator that will order Records on the average word length.
     */
    private class averageWordLengthComparator implements Comparator<Record> {
        
        @Override
        public int compare(Record record1, Record record2) {
            double totalLength = 0;
            double numWords = 0;
            
            for (int i = 0; i < record1.getNumTextlines(); i++) {
                StringTokenizer line = new StringTokenizer(
                    record1.getTextline(i));
                
                while (line.hasMoreTokens()) {
                    totalLength += line.nextToken().length();
                    numWords++;
                }
            }
            
            double avgLength1 = totalLength / numWords;
            
            totalLength = 0;
            numWords = 0;
            
            for (int i = 0; i < record2.getNumTextlines(); i++) {
                StringTokenizer line = new StringTokenizer(
                    record2.getTextline(i));
                
                while (line.hasMoreTokens()) {
                    totalLength += line.nextToken().length();
                    numWords++;
                }
            }
            
            double avgLength2 = totalLength / numWords;
            
            if (avgLength1 > avgLength2)
                return -1;
            else if (Math.abs(avgLength1 - avgLength2) < 0.0000000001) // Floating point equality.
                return 0;
            else
                return 1;
        }
    }
    
    /**
     * Comparator that will order Records based on which has more lines of text.
     */
    private class numTextlinesComparator implements Comparator<Record> {
        
        @Override
        public int compare(Record record1, Record record2) {
            return record1.getNumTextlines() - record2.getNumTextlines();
        }
    }
    
    /**
     * Comparator that will order Records based on the length of their first line
     * of text.
     */
    private class textLineLengthComparator implements Comparator<Record> {
        
        @Override
        public int compare(Record record1, Record record2) {
            return record1.getTextline(0).length()
                - record2.getTextline(0).length();
        }
    }
    
    /**
     * Comparator that will order Records based on the proportion of uppercase
     * letters in the record
     * as a whole.
     */
    private class uppercaseProportionComparator implements Comparator<Record> {
        
        @Override
        public int compare(Record record1, Record record2) {
            double upperCount = 0;
            double totalCount = 0;
            
            for (int i = 0; i < record1.getNumTextlines(); i++)
                for (char c : record1.getTextline(i).toCharArray()) {
                    totalCount++;
                    
                    if (Character.isUpperCase(c))
                        upperCount++;
                }
            
            double proportion1 = upperCount / totalCount;
            
            upperCount = 0;
            totalCount = 0;
            
            for (int i = 0; i < record2.getNumTextlines(); i++)
                for (char c : record2.getTextline(i).toCharArray()) {
                    totalCount++;
                    
                    if (Character.isUpperCase(c))
                        upperCount++;
                }
            
            double proportion2 = upperCount / totalCount;
            
            if (proportion1 > proportion2)
                return -1;
            else if (Math.abs(proportion1 - proportion2) < 0.0000000001) // Floating point equality.
                return 0;
            else
                return 1;
        }
    }
}
