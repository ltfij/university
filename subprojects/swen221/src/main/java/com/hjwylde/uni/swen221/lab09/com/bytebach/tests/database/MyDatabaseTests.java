package com.hjwylde.uni.swen221.lab09.com.bytebach.tests.database;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.assignment06.com.bytebach.impl.MyDatabase;
import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.Field;
import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.InvalidOperation;

/*
 * Code for Laboratory 9, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class MyDatabaseTests {
    
    // The following provides some simple lists of fields to test with
    private static final ArrayList<Field> FIELDS_1 = new ArrayList<Field>() {
        
        private static final long serialVersionUID = 1L;
        
        {
            add(new Field("ID", Field.Type.INTEGER, true));
            add(new Field("TEXT", Field.Type.TEXT, false));
        }
    };
    private static final ArrayList<Field> FIELDS_2 = new ArrayList<Field>() {
        
        private static final long serialVersionUID = 1L;
        
        {
            add(new Field("ID", Field.Type.INTEGER, true));
            add(new Field("REF", "table", false));
        }
    };
    private static final ArrayList<Field> FIELDS_3 = new ArrayList<Field>() {
        
        private static final long serialVersionUID = 1L;
        
        {
            add(new Field("ID", Field.Type.INTEGER, true));
            add(new Field("TEXT", Field.Type.TEXTAREA, false));
            add(new Field("REF", "refs", false));
        }
    };
    
    @Test
    public void testInvalidCreateTable() {
        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        
        try {
            db.createTable("table2", null);
            Assert.fail();
        } catch (InvalidOperation e) {}
        try {
            db.createTable("table", MyDatabaseTests.FIELDS_2);
            Assert.fail();
        } catch (InvalidOperation e) {}
    }
    
    @Test
    public void testInvalidDeleteTable() {
        MyDatabase db = new MyDatabase();
        
        try {
            db.deleteTable(null);
            Assert.fail();
        } catch (InvalidOperation e) {}
        try {
            db.deleteTable("non existant");
            Assert.fail();
        } catch (InvalidOperation e) {}
    }
    
    @Test
    public void testInvalidTable() {
        MyDatabase db = new MyDatabase();
        
        try {
            db.table(null);
            Assert.fail();
        } catch (InvalidOperation e) {}
    }
    
    @Test
    public void testValidCreateTable() {
        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("table2", MyDatabaseTests.FIELDS_2);
    }
    
    @Test
    public void testValidDeleteTable() {
        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("refs", MyDatabaseTests.FIELDS_2);
        db.createTable("table2", MyDatabaseTests.FIELDS_3);
        
        db.deleteTable("table");
        
        Assert.assertTrue(db.tables().size() == 0);
    }
    
    @Test
    public void testValidTable() {
        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        
        Assert.assertTrue(db.table("table") != null);
        Assert.assertTrue(db.table("non existant") == null);
    }
    
    @Test
    public void testValidTables() {
        MyDatabase db = new MyDatabase();
        
        Assert.assertTrue(db.tables().size() == 0);
        
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        Assert.assertTrue(db.tables().size() == 1);
    }
    
    @Test
    public void testValidToString() {
        MyDatabase db = new MyDatabase();
        
        Assert.assertTrue(db.toString() != null);
    }
}