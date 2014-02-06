package com.hjwylde.uni.swen221.lab05;

import com.hjwylde.uni.swen221.lab05.Solution.DuplicateIdException;
import com.hjwylde.uni.swen221.lab05.Solution.MissingDataException;

/*
 * Code for Laboratory 5, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Your solution should implement this interface.
 * 
 * @author ncameron
 */
public interface Lab5 {
    
    /**
     * Add a row to the data set
     * 
     * @param id the row's identifier.
     * @param data the data for the row.
     * @throws DuplicateIdException if a row with the given identifier already exists.
     */
    public void addRow(Identifier id, int[] data) throws DuplicateIdException;
    
    /**
     * @param id identifier of the row to get.
     * @return identifiers data.
     * @throws MissingDataException if no such entry is found.
     */
    public int[] getRow(Identifier id) throws MissingDataException;
    
    /**
     * @param id the identifier of the row to get the average for.
     * @return the average of all data for the given id.
     * @throws MissingDataException if the id is not present in the data.
     */
    public int getRowAverage(Identifier id) throws MissingDataException;
    
    /**
     * @param index the index of the row.
     * @return the index-th row of the ordered data, where the first row is row 0.
     * @throws IndexOutOfBoundsException if the row at index is not present in the data.
     */
    public Identifier getRowId(int index) throws IndexOutOfBoundsException;
    
    /**
     * @param id the identifier of the row to get the total for.
     * @return the total of all data for the given id.
     * @throws MissingDataException if the id is not present in the data.
     */
    public int getRowTotal(Identifier id) throws MissingDataException;
}