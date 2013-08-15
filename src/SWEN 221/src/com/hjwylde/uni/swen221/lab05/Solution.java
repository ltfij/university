package com.hjwylde.uni.swen221.lab05;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/*
 * Code for Laboratory 5, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Solution to Lab5.
 * 
 * @author Henry J. Wylde
 */
public class Solution implements Lab5 {
    
    private List<String> header;
    private SortedMap<Identifier, int[]> body;
    
    /**
     * Creates a new solution with the default input.txt input.
     * 
     * @throws IOException if reading the input file fails.
     */
    public Solution() throws IOException {
        this(
            new File(
                "C:/Users/Henry Wylde/Repositories/University/Swen221/src/lab5/data/input.txt"));
    }
    
    /**
     * Initialises the solution with the specified input file.
     * 
     * @param input the input file to initialise with.
     * @throws IOException if reading the input file fails.
     */
    private Solution(File input) throws IOException {
        header = new ArrayList<>();
        body = new TreeMap<>();
        
        // Default headers.
        for (String head : new String[] {
            "Name", "dept", "dd", "yu", "t", "ix", "is", "gu", "op", "ed", "nx"
        })
            header.add(head);
        
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            br.readLine();
            
            Identifier id;
            int[] data;
            
            String line;
            StringTokenizer tokens;
            // Read the file line by line.
            while ((line = br.readLine()) != null) {
                tokens = new StringTokenizer(line, ",");
                
                // First two tokens is the identifier.
                id = new Identifier(tokens.nextToken().trim(), tokens
                    .nextToken().trim()); // FIXME: Check for tokens existing
                
                // Rest of tokens in the line are the data.
                data = new int[header.size() - 2];
                for (int i = 0; i < (header.size() - 2); i++) {
                    if (!tokens.hasMoreTokens())
                        throw new IllegalArgumentException(
                            "Expected more tokens to match the header size.");
                    
                    data[i] = Integer.parseInt(tokens.nextToken().trim());
                }
                
                // Add the row using hte method that checks for duplicate ids.
                addRow(id, data);
            }
        }
        
        printHtml();
    }
    
    /*
     * @see lab5.Lab5#addRow(lab5.Identifier, int[])
     */
    @Override
    public void addRow(Identifier id, int[] data) throws DuplicateIdException {
        if (contains(id) || (data.length != (header.size() - 2))) // Weird...
            throw new DuplicateIdException();
        
        body.put(id, data);
    }
    
    /**
     * Checks whether the identifier is contained in the data collection.
     * 
     * @param id the identifier to check.
     * @return true if the identifier is contained within the collection.
     */
    public boolean contains(Identifier id) {
        return (body.containsKey(id));
    }
    
    @Override
    public int[] getRow(Identifier id) throws MissingDataException {
        if (!contains(id))
            throw new MissingDataException();
        
        return body.get(id);
    }
    
    /*
     * @see lab5.Lab5#getRowAverage(lab5.Identifier)
     */
    @Override
    public int getRowAverage(Identifier id) throws MissingDataException {
        int[] data = getRow(id);
        
        int total = 0;
        for (int datum : data)
            total += datum;
        
        return total / data.length;
    }
    
    /*
     * @see lab5.Lab5#getRowId(int)
     */
    @Override
    public Identifier getRowId(int index) throws IndexOutOfBoundsException {
        if ((index < 0) || (index >= body.size()))
            throw new IndexOutOfBoundsException();
        
        Iterator<Identifier> it = body.keySet().iterator();
        for (int i = 0; i < index; i++)
            it.next();
        
        return it.next();
    }
    
    /*
     * @see lab5.Lab5#getRowTotal(lab5.Identifier)
     */
    @Override
    public int getRowTotal(Identifier id) throws MissingDataException {
        int[] data = getRow(id);
        
        int total = 0;
        for (int datum : data)
            total += datum;
        
        return total;
    }
    
    /**
     * Prints out a textual representation of the ordered data.
     */
    public void print() {
        for (int y = 0; y <= body.size(); y++) {
            for (int x = 0; x < header.size(); x++)
                if (y == 0)
                    System.out.printf("%10s", header.get(x));
                else if (x == 0)
                    System.out.printf("%10s", getRowId(y).getName());
                else if (x == 1)
                    System.out.printf("%10s", getRowId(y).getDept());
                else
                    System.out.printf("%10s", getRow(getRowId(y))[x - 2]);
            
            System.out.println();
        }
    }
    
    /**
     * Prints out the ordered data marked up with HTML table tags.
     */
    public void printHtml() {
        System.out.println("<html>");
        System.out.println("<body>");
        System.out.println("<table>");
        System.out.println("\t<thead>");
        
        printHeaderRow();
        
        System.out.println("\t</thead>");
        System.out.println("\t<tbody>");
        
        for (Entry<Identifier, int[]> e : body.entrySet())
            printRow(e.getKey());
        
        System.out.println("\t</tbody>");
        System.out.println("</table>");
        System.out.println("</body>");
        System.out.println("</html>");
    }
    
    /**
     * Prints out the header row marked up with the correct HTML table header tags.
     */
    private void printHeaderRow() {
        System.out.println("\t\t<tr>");
        
        for (String head : header)
            System.out.println("\t\t\t<th>" + head + "</th>");
        
        System.out.println("\t\t\t<th>total</th>");
        System.out.println("\t\t\t<th>average</th>");
        
        System.out.println("\t\t</tr>");
    }
    
    /**
     * Prints out the row with the given identifier with the correct HTML table row tags.
     * 
     * @param id the identifier of the row to print.
     */
    private void printRow(Identifier id) {
        if (!contains(id))
            return;
        
        System.out.println("\t\t<tr>");
        
        System.out.println("\t\t\t<td>" + id.getName() + "</td>");
        System.out.println("\t\t\t<td>" + id.getDept() + "</td>");
        
        for (int datum : body.get(id))
            System.out.println("\t\t\t<td>" + datum + "</td>");
        
        System.out.println("\t\t\t<td>" + getRowTotal(id) + "</td>");
        System.out.println("\t\t\t<td>" + getRowAverage(id) + "</td>");
        
        System.out.println("\t\t</tr>");
    }
    
    /**
     * Represents a duplicate id when attempting to add an id that already exists in the data
     * collection.
     * 
     * @author Henry J. Wylde
     */
    public class DuplicateIdException extends IllegalArgumentException {
        
        private static final long serialVersionUID = 1L;
    }
    
    /**
     * Represents missing data when trying to access data that doesn't exist in a collection.
     * 
     * @author Henry J. Wylde
     */
    public class MissingDataException extends IllegalArgumentException {
        
        private static final long serialVersionUID = 1L;
    }
    
}
