package com.hjwylde.uni.swen221.assignment06.com.bytebach.model;

import java.util.List;

/*
 * Code for Assignment 6, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public interface Table {

    /**
     * Delete a row with the matching keys
     */
    public void delete(Value... keys);

    /**
     * Return the list of all fields (i.e. including key fields) in the table. This list is <b>not
     * modifiable</b>. Any attempts to modify this list should result in an InvalidOperation
     * exception.
     */
    public List<Field> fields();

    /**
     * Get the table name.
     */
    public String name();

    /**
     * <p>
     * Return row matching a given key. The row is simply a list of values, which must correspond to
     * the types determined in fields.
     * </p>
     * 
     * <p>
     * The list returned is <b>partially modifiable</b>. One cannot add or remove items from the
     * list; however, one can set items in the list (provided they are not key fields). Special care
     * must be taken to ensure that such operations are checked to ensure they do not violate the
     * database constraints.
     * </p>
     */
    public List<Value> row(Value... keys);

    /**
     * <p>
     * Return the list of rows in the table. Each row is simply a list of values, which must
     * correspond to the types determined in fields
     * </p>
     * 
     * <p>
     * The list returned is <b>modifiable</b>. This means one can add and remove rows from the table
     * via the list interface. Special care must be taken to ensure that such operations are checked
     * to ensure they do not violate the database constraints. However, one is not permitted to add
     * and remove fields within a row; rather, one may update them.
     * </p>
     */
    public List<List<Value>> rows();
}
