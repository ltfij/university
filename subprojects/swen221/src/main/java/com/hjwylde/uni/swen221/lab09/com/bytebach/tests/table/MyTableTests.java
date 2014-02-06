package com.hjwylde.uni.swen221.lab09.com.bytebach.tests.table;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.assignment06.com.bytebach.impl.MyDatabase;
import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.*;

/*
 * Code for Laboratory 9, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class MyTableTests {
    
    @Test
    public void testCascadingDelete1() {
        Object[][] rawTableRows = {
            {
                0, "Hello WOrld"
            }, {
                1, "Blah"
            }
        };
        Object[][] rawRefRows = {
            {
                0, new ReferenceValue("table", 0)
            }
        };
        Value[][] tableRows = TableTestSuite.createValues(rawTableRows);
        Value[][] refRows = TableTestSuite.createValues(rawRefRows);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.createTable("refs", TableTestSuite.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        db.table("table").delete(new IntegerValue(0));
        Assert.assertTrue(db.table("refs").rows().size() == 0);
    }
    
    @Test
    public void testCascadingDelete2() {
        Object[][] rawTableRows = {
            {
                0, "Hello WOrld"
            }, {
                1, "Blah"
            }
        };
        Object[][] rawRefRows = {
            {
                0, new ReferenceValue("table", 0)
            }, {
                1, new ReferenceValue("table", 1)
            }
        };
        Value[][] tableRows = TableTestSuite.createValues(rawTableRows);
        Value[][] refRows = TableTestSuite.createValues(rawRefRows);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.createTable("refs", TableTestSuite.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        db.table("refs").rows().add(Arrays.asList(refRows[1]));
        db.table("table").delete(new IntegerValue(0));
        Assert.assertTrue(db.table("refs").rows().size() == 1);
    }
    
    @Test
    public void testCascadingDelete3() {
        Object[][] rawTableRows = {
            {
                0, "Hello WOrld"
            }, {
                1, "Blah"
            }
        };
        Object[][] rawRefRows = {
            {
                0, new ReferenceValue("table", 0)
            }, {
                1, new ReferenceValue("table", 1)
            }, {
                2, new ReferenceValue("table", 0)
            }
        };
        Value[][] tableRows = TableTestSuite.createValues(rawTableRows);
        Value[][] refRows = TableTestSuite.createValues(rawRefRows);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.createTable("refs", TableTestSuite.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        db.table("refs").rows().add(Arrays.asList(refRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[2]));
        db.table("table").delete(new IntegerValue(0));
        Assert.assertTrue(db.table("refs").rows().size() == 1);
    }
    
    @Test
    public void testCascadingDelete4() {
        Object[][] rawTableRows = {
            {
                0, "Hello WOrld"
            }, {
                1, "Blah"
            }
        };
        Object[][] rawRefRows = {
            {
                0, new ReferenceValue("table", 0)
            }, {
                1, new ReferenceValue("table", 1)
            }, {
                2, new ReferenceValue("table", 0)
            }
        };
        Value[][] tableRows = TableTestSuite.createValues(rawTableRows);
        Value[][] refRows = TableTestSuite.createValues(rawRefRows);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.createTable("refs", TableTestSuite.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        db.table("refs").rows().add(Arrays.asList(refRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[2]));
        db.table("table").delete(new IntegerValue(1));
        Assert.assertTrue(db.table("refs").rows().size() == 2);
    }
    
    @Test
    public void testInvalidAddRow() {
        Object[][] rawData = {
            {
                0, "Hello WOrld"
            }, {
                0, "Blah"
            }
        };
        Value[][] rows = TableTestSuite.createValues(rawData);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        try {
            db.table("table").rows().add(Arrays.asList(rows[1]));
            Assert
                .fail("Shouldn't be able to add row with same key field as another");
        } catch (InvalidOperation e) {
            
        }
    }
    
    @Test
    public void testInvalidConstructor() {
        MyDatabase db = new MyDatabase();
        db.createTable("table2", TableTestSuite.FIELDS_1);
        
        try {
            db.createTable("refs", TableTestSuite.FIELDS_2);
            Assert.fail();
        } catch (InvalidOperation e) {}
    }
    
    @Test
    public void testInvalidDelete() {
        MyDatabase db = TableTestSuite.generateDb();
        
        try {
            db.table("table").delete(new StringValue("2"));
            Assert.fail();
        } catch (InvalidOperation e) {}
    }
    
    @Test
    public void testInvalidReferenceAdd1() {
        Object[][] rawTableRows = {
            {
                0, "Hello WOrld"
            }, {
                1, "Blah"
            }
        };
        Object[][] rawRefRows = {
            {
                0, new ReferenceValue("table", 2)
            }
        };
        Value[][] tableRows = TableTestSuite.createValues(rawTableRows);
        Value[][] refRows = TableTestSuite.createValues(rawRefRows);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.createTable("refs", TableTestSuite.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        try {
            db.table("refs").rows().add(Arrays.asList(refRows[0]));
            Assert
                .fail("Shouldn't be able to add row containing invalid reference");
        } catch (InvalidOperation e) {
            
        }
    }
    
    @Test
    public void testInvalidReferenceAdd2() {
        Object[][] rawTableRows = {
            {
                0, "Hello WOrld"
            }, {
                1, "Blah"
            }
        };
        Object[][] rawRefRows = {
            {
                0, new ReferenceValue("BROKEN", 0)
            }
        };
        Value[][] tableRows = TableTestSuite.createValues(rawTableRows);
        Value[][] refRows = TableTestSuite.createValues(rawRefRows);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.createTable("refs", TableTestSuite.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        try {
            db.table("refs").rows().add(Arrays.asList(refRows[0]));
            Assert
                .fail("Shouldn't be able to add row containing invalid reference");
        } catch (InvalidOperation e) {}
    }
    
    @Test
    public void testValidCascadeUpdate() {
        TableTestSuite.generateDb();
    }
    
    @Test
    public void testValidDelete() {
        MyDatabase db = TableTestSuite.generateDb();
        
        db.table("table").delete(new IntegerValue(1));
        db.table("table").delete(new IntegerValue(2));
        
        Assert.assertTrue(db.table("table").rows().size() == 1);
    }
    
    @Test
    public void testValidToString() {
        MyDatabase db = TableTestSuite.generateDb();
        
        Assert.assertTrue(db.table("table").toString() != null);
    }
}
