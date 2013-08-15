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
import java.util.Scanner;

import javax.swing.*;

import com.hjwylde.uni.comp103.util.FileChooser;

/** ListXMLTags will read an XML file and list all the opening tags.
 *
 * An opening tag in XML looks like "<box>" or "<box identifier="mybox" size="15">"
 *  The first word after the "<" is the name of the tag. The other bits after the name and
 *  before the ">" are attributes.
 * A closing tag in XML looks like "</box>". The "/" says it is a closing tag.
 *  The word after the "</" is the name of the tag.
 * In between the tags, there can be ordinary text. 
 */

public class ListXMLTags implements ActionListener {
    
    // Fields
    private final JFrame frame;
    private final JTextArea textArea;
    private final JTextArea messageArea;
    
    private String fileName;
    
    // Constructors
    /**
     * Construct a new CheckXML object and set up the GUI
     */
    public ListXMLTags() {
        
        frame = new JFrame("ListXMLTags");
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
        addButton(buttonPanel, "List Tags");
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
            listFile();
        } else if ((fileName == null) || !(new File(fileName).exists())) {
            textArea.setText("No file selected");
            return;
        } else if (button.equals("List File"))
            listFile();
        if (button.equals("Show Tokens"))
            showTokens();
        else if (button.equals("List Tags"))
            listTags();
        else if (button.equals("Quit"))
            frame.dispose();
    }
    
    /** List the current file */
    public void listFile() {
        try (Scanner s = new Scanner(new File(fileName))) {
            textArea.setText("Listing of File " + fileName
                + ":\n-------------------\n");
            
            while (s.hasNext()) {
                textArea.append(s.nextLine());
                textArea.append("\n");
            }
        } catch (IOException e) {}
    }
    
    // Other Methods
    
/** Read a file and list the types of all the tags
       * Uses a Scanner to read the tokens from the file.
       * It simply ignores any token that doesn't start with "<"
       */
    public void listTags() {
        try (Scanner s = new Scanner(new File(fileName))) {
            textArea
                .setText("Tags in " + fileName + ":\n-------------------\n");
            
            s.useDelimiter("\\s+|(?=[<>=])|(?<=[>])"); // magic to make the scanner
                                                       // break up the tokens
                                                       // right.
            
            // Print out all the tokens, one on each line
            while (s.hasNext()) {
                String token = s.next();
                if (token.startsWith("<"))
                    textArea.append(token.substring(1) + "\n");
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
        new ListXMLTags();
    }
    
}
