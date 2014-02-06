package com.hjwylde.uni.swen221.assignment06.com.bytebach.impl;

import java.util.*;

import com.hjwylde.uni.swen221.assignment06.com.bytebach.Main;
import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.Database;
import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.Field;
import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.InvalidOperation;
import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.Table;

/*
 * Code for Assignment 6, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * A database implementation that ensures data integrity between rows and tables.
 * 
 * @author Henry J. Wylde
 */
public class MyDatabase implements Database {
    
    private final Set<Table> tables = new TreeSet<>(MyTable.COMP_TABLE_NAME);
    
    /*
     * @see assignment6.com.bytebach.model.Database#createTable(java.lang.String, java.util.List)
     */
    @Override
    public void createTable(String name, List<Field> fields) {
        Main.validateParameters(name, fields);
        
        if (!tables.add(new MyTable(this, name, fields)))
            throw new InvalidOperation(); // Table already existed.
    }
    
    /*
     * @see assignment6.com.bytebach.model.Database#deleteTable(java.lang.String)
     */
    @Override
    public void deleteTable(String name) {
        Main.validateParameters(name);
        
        Table deleted = table(name);
        if (deleted == null) // Table doesn't exist.
            throw new InvalidOperation();
        
        tables.remove(deleted);
        
        cascadeDelete(deleted); // Ensure data integrity.
    }
    
    /*
     * @see assignment6.com.bytebach.model.Database#table(java.lang.String)
     */
    @Override
    public Table table(String name) {
        Main.validateParameters(name);
        
        for (Table table : tables)
            if (table.name().equals(name))
                return table;
        
        return null;
    }
    
    /*
     * @see assignment6.com.bytebach.model.Database#tables()
     */
    @Override
    public Collection<? extends Table> tables() {
        return tables;
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Database {" + tables + "\n}";
    }
    
    /**
     * Ensures data integrity by deleting any tables that have a field that references the just
     * deleted table.
     * 
     * @param table the table that has just been deleted.
     */
    private void cascadeDelete(Table table) {
        List<String> toDelete = new ArrayList<>();
        
        // Find all tables that have a reference field and store them in a list.
        for (Table t : tables)
            for (Field field : t.fields()) {
                if (field.type() != Field.Type.REFERENCE)
                    continue;
                
                if (field.refTable().equals(table.name())) {
                    toDelete.add(t.name());
                    break;
                }
            }
        
        // Delete all the tables that had the reference.
        for (String name : toDelete)
            deleteTable(name);
    }
}