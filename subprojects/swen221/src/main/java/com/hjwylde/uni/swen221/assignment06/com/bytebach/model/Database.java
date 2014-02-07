package com.hjwylde.uni.swen221.assignment06.com.bytebach.model;

import java.util.Collection;
import java.util.List;

/*
 * Code for Assignment 6, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public interface Database {

    /**
     * Create a table with the given name, and fields. If one already exists, then an
     * InvalidOperation is thrown.
     * 
     * @param name table name.
     * @param fields fields.
     */
    public void createTable(String name, List<Field> fields);

    /**
     * Delete the table with the given name. Observe that this must additionally result in the
     * removal of any tables containing a field of reference type, which refers to this table.
     */
    public void deleteTable(String name);

    /**
     * Return the table with the given name, or null if none exists.
     */
    public Table table(String name);

    /**
     * Return the list of tables currently stored in the database. Observe that this list is
     * implicitly unmodifiable, because of the return type.
     */
    public Collection<? extends Table> tables();
}
