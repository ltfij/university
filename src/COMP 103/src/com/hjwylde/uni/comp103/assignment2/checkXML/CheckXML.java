package com.hjwylde.uni.comp103.assignment2.checkXML;

/*
 * Code for Assignment 2, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.*;

import com.hjwylde.uni.comp103.util.FileChooser;

/** CheckXML will read an XML file and check that all the opening and closing tags match
 * and are properly nested
 *
 * An opening tag in XML looks like "<box>" or "<box identifier="mybox" size="15">"
 *  The first word after the "<" is the name of the tag. The other bits after the name and
 *  before the ">" are attributes, and your program can ignore them completely.
 * A closing tag in XML looks like "</box>". The "/" says it is a closing tag.
 *  The word after the "</" is the name of the tag.
 * In between the tags, there can be ordinary text. Your program can ignore it completely.
 *
 * All XML tags should come in matching pairs: "<box>......</box>".  That is, any opening
 *  tag should match with a closing tag that has the same name.
 *
 * XML tags should also be properly nested - if any opening tag (such as "<lid>") occurs
 *  anywhere inside another pair of tags, then the matching closing tag ("</lid>") must also
 *  occur between the same pair.
 * 
 * For example, in the file  "<box><lid>green</lid><sides>blue</sides></box>" each of
 * the opening tags has a closing tag that matches, and the lid and the sides are properly
 * nested inside the box.
 * In the file  "<box><lid>green</lid><sides>blue</box></sides>" each of
 * the opening tags has a closing tag that matches, but lid and the sides are not properly
 * nested inside the box - the "<sides>" tag is between "<box>" and "</box>",
 * so the matching "</sides>" tag ought to be between them also.
 */

public class CheckXML implements ActionListener {
    
    // Fields
    private final JFrame frame;
    private final JTextArea textArea;
    private final JTextArea messageArea;
    
    private String fileName;
    
    // Constructors
    /**
     * Construct a new CheckXML object and set up the GUI
     */
    public CheckXML() {
        
        frame = new JFrame("CheckXML");
        frame.setSize(700, 700);
        
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
        
        addButton(buttonPanel, "Choose File");
        addButton(buttonPanel, "List File");
        addButton(buttonPanel, "Show Tokens");
        addButton(buttonPanel, "Check Tags");
        addButton(buttonPanel, "Count Tags");
        addButton(buttonPanel, "Quit");
        
        frame.setVisible(true);
    }
    
    // GUI Methods
    
    /** Respond to button presses */
    @Override
    public void actionPerformed(ActionEvent e) {
        String button = e.getActionCommand();
        if (button.equals("Choose File")) {
            fileName = FileChooser.open("File Name");
            textArea.setText("Selected " + fileName);
        } else if ((fileName == null) || !(new File(fileName).exists()))
            textArea.setText("No file selected");
        else if (button.equals("List File"))
            listFile();
        else if (button.equals("Show Tokens"))
            showTokens();
        else if (button.equals("Check Tags"))
            checkTags();
        else if (button.equals("Count Tags"))
            countTags();
        
        if (button.equals("Quit"))
            frame.dispose();
    }
    
/** Read a file and check that the tags match and nest properly
    * Uses a Scanner to read the tokens from the file.
    * Uses a Stack to check that the open and close tags match and nest.
    * It can completely ignore any token that doesn't start with "<"
    */
    public void checkTags() {
        /*
         * Completion:
         * 
         * The reason that when one tag causes an error that many consequential tags
         * will report errors
         * too is that once the error is found, the "tag pair" (opening and closing
         * tag) are popped off
         * of the stack. This means that for tags that are spelling mistakes, it
         * won't cause
         * consequential tag errors, however for missing closing tags or missing
         * opening tags, this will
         * result in a lot of further errors as the heirarchy of tags will have been
         * skewed by one.
         * 
         * There are a few different basic types of tag errors that could occur:
         * 
         * 1. Missing opening or closing tag. (Solved by not popping the "error" tag
         * off the stack.)
         * Missing closing tags at the end are solved by doing a check for any
         * left-over items in the
         * stack.
         * 
         * 2. Misspelt or incorrect opening or closing tag. (Solved by popping the
         * "error" tag off the
         * stack.)
         * 
         * 3. A closing tag in the wrong position. (Similar to a missing tag, and
         * shall be treated as
         * one as it is dificult to search through a stack for a specific tag.)
         * 
         * Challenge:
         * 
         * To solve these problems, the program has been designed to pop the tags
         * off of the stack when
         * an incorrect one has been detected. For the missing tag cases, I have
         * just done a check
         * whether the received tag is the next expected tag (the next tag on the
         * tagStack), and if it
         * is, it will pop off the next expected tag as well and report a missing
         * tag. It can't go back
         * more than one tag to check however.
         * 
         * This is by no means a 'fool proof' way of solving the problem of
         * determining incorrect tags,
         * however it is an adequate one for basic problems. In reality, it would be
         * next to impossible
         * to correctly determine incorrect tags as it is hard to determine exactly
         * what the human was
         * thinking/meaning when they wrote an incorrect tag.
         * 
         * To properly solve it, the program should iterate through the file, and
         * after each incorrect
         * tag ask the user to fix it, so it may continue checking the rest of the
         * tags without any
         * previous errors.
         */
        
        try (Scanner s = new Scanner(new File(fileName))) {
            textArea.setText("Checking tags in " + fileName
                + ":\n-------------------\n");
            s.useDelimiter("\\s+|(?=[<>=])|(?<=[>])"); // magic to make the scanner
                                                       // break up the tokens
                                                       // right.
            
            String token = null;
            Stack<String> tagStack = new Stack<>(); // The tag stack holds all
                                                    // the current un-closed
                                                    // open tags.
            
            while (s.hasNext()) {
                token = s.next();
                if (token.startsWith("</")) { // If the tag is a closing one...
                    token = token.substring(2);
                    if (!tagStack.isEmpty())
                        if (tagStack.peek().equalsIgnoreCase(token)) // If the closing tag
                                                                     // is the correct one
                                                                     // expected, pop it off
                                                                     // the stack.
                            tagStack.pop();
                        else { // Tag error!
                            String expectedTag = tagStack.pop();
                            String nextExpectedTag = "";
                            if (!tagStack.isEmpty()) {
                                nextExpectedTag = tagStack.peek();
                                // If the tag after the last open tag is the partner to the
                                // found closing tag...
                                if (token.equalsIgnoreCase(nextExpectedTag)) {
                                    // Report that the last open tag does not have a closing
                                    // partner.
                                    textArea
                                        .append("Tag Error:\nMissing Tag: </"
                                            + expectedTag + ">\n");
                                    tagStack.pop();
                                } else if (textArea
                                    .getText()
                                    .substring(
                                        textArea.getText().length()
                                            - (17 + token.length()))
                                    .equalsIgnoreCase(
                                        "Missing Tag: </" + token + ">\n")) {
                                    // If the tag received, was the previously reported Missing
                                    // Tag...
                                    textArea
                                        .append("Tag Error:\nReceived Tag: </"
                                            + token
                                            + "> is the Missing Tag found Previously\n");
                                    tagStack.push(expectedTag);
                                } else
                                    textArea
                                        .append("Tag Error:\nExpected Tag: </"
                                            + expectedTag
                                            + ">\nReceived Tag: </" + token
                                            + ">\n"); // Tag error,
                                                      // the received
                                                      // tag is
                                                      // not what was
                                                      // expected.
                            }
                        }
                    else
                        textArea.append("Tag Error:\nExtra Closing Tag: </"
                            + token + ">\n");
                } else if (token.startsWith("<")) // If it is an opening tag, add to the
                                                  // stack.
                    tagStack.push(token.substring(1));
            }
            
            while (!tagStack.isEmpty())
                // Any left over open tags that do not have closing partners.
                textArea.append("Tag Error:\nLeft Over Tag was not Closed: <"
                    + tagStack.pop() + ">\n");
        } catch (IOException e) {}
    }
    
    // Other Methods
    
/** Read a file and count the number of occurrences of each kind of opening token
    * Print out all the tokens and their counts.
    * Uses a Map to count the number of occurrences of each kind of open tag
    * It can completely ignore any token that doesn't start with "<"
    */
    public void countTags() {
        try (Scanner s = new Scanner(new File(fileName))) {
            textArea
                .setText("Tags in " + fileName + ":\n-------------------\n");
            s.useDelimiter("\\s+|(?=[<>=])|(?<=[>])"); // magic to make the scanner
                                                       // break up the tokens
                                                       // right.
            
            String token = null;
            Map<String, Integer> tagCounts = new HashMap<>();
            while (s.hasNext()) {
                token = s.next();
                if (token.startsWith("<") && !token.startsWith("</")) {
                    token = token.substring(1);
                    if (tagCounts.containsKey(token))
                        tagCounts.put(token, tagCounts.get(token) + 1);
                    else
                        tagCounts.put(token, 1);
                }
            }
            
            textArea.append("Tag\t\t\tCount\n");
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet())
                textArea.append(entry.getKey() + "\t\t\t" + entry.getValue()
                    + "\n");
        } catch (IOException e) {}
    }
    
    /** List the current file */
    public void listFile() {
        try (Scanner s = new Scanner(new File(fileName))) {
            textArea.setText("Listing of File " + fileName
                + ":\n-------------------\n");
            
            // Print out all the tokens, one on each line
            while (s.hasNext()) {
                textArea.append(s.nextLine());
                textArea.append("\n");
            }
        } catch (IOException e) {}
    }
    
/** List all the tokens in the current file
     * Uses a Scanner to read the tokens from the file.
     * and prints them out, one token per line.
     * This Scanner is set up to make it easy to find the opening and closing tags
     * Each opening tag will have a token starting with "<" followed by the name.
     * Each closing tag will have a token starting with "</" followed by the name.
     * Any token that doesn't start with "<" is an attribute or the text between tags.
     */
    public void showTokens() {
        try (Scanner s = new Scanner(new File(fileName))) {
            textArea.setText("Tokens in " + fileName
                + ":\n-------------------\n");
            
            s.useDelimiter("\\s+|(?=[<>=])|(?<=[>])"); // magic to make the scanner
                                                       // break up the tokens
                                                       // right.
            
            // Print out all the tokens, one on each line
            while (s.hasNext()) {
                textArea.append(s.next());
                textArea.append("\n");
            }
        } catch (IOException e) {}
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
        new CheckXML();
    }
    
}
