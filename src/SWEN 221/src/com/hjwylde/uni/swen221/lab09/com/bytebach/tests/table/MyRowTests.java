package com.hjwylde.uni.swen221.lab09.com.bytebach.tests.table;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.assignment6.com.bytebach.impl.MyDatabase;
import com.hjwylde.uni.swen221.assignment6.com.bytebach.model.*;

/*
 * Code for Laboratory 9, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class MyRowTests {
    
    @Test
    public void testInvalidAdd() {
        MyDatabase db = TableTestSuite.generateDb();
        
        try {
            db.table("table").rows().get(0).add(new StringValue("Boo!"));
        } catch (InvalidOperation e) {}
    }
    
    @Test
    public void testInvalidGet() {
        MyDatabase db = TableTestSuite.generateDb();
        
        try {
            db.table("table").rows().get(0).get(-1);
        } catch (InvalidOperation e) {}
        try {
            db.table("table").rows().get(0).get(11);
        } catch (InvalidOperation e) {}
    }
    
    @Test
    public void testInvalidReferenceSet1() {
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
        Value[][] tableRows = MyRowTests.createValues(rawTableRows);
        Value[][] refRows = MyRowTests.createValues(rawRefRows);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.createTable("refs", TableTestSuite.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        try {
            db.table("refs").row(new IntegerValue(0))
                .set(1, new ReferenceValue("table", 2));
            Assert
                .fail("Shouldn't be able to set row entry to invalid reference");
        } catch (InvalidOperation e) {
            
        }
    }
    
    @Test
    public void testInvalidReferenceSet2() {
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
        Value[][] tableRows = MyRowTests.createValues(rawTableRows);
        Value[][] refRows = MyRowTests.createValues(rawRefRows);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.createTable("refs", TableTestSuite.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        try {
            db.table("refs").row(new IntegerValue(0))
                .set(1, new ReferenceValue("BROKEN", 2));
            Assert
                .fail("Shouldn't be able to set row entry to invalid reference");
        } catch (InvalidOperation e) {
            
        }
    }
    
    @Test
    public void testInvalidReferenceSet3() {
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
        Value[][] tableRows = MyRowTests.createValues(rawTableRows);
        Value[][] refRows = MyRowTests.createValues(rawRefRows);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.createTable("refs", TableTestSuite.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        try {
            db.table("refs").row(new IntegerValue(0))
                .set(1, new ReferenceValue("table", 1, 1));
            Assert
                .fail("Shouldn't be able to set row entry to invalid reference");
        } catch (InvalidOperation e) {
            
        }
    }
    
    @Test
    public void testInvalidReferenceSet4() {
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
        Value[][] tableRows = MyRowTests.createValues(rawTableRows);
        Value[][] refRows = MyRowTests.createValues(rawRefRows);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.createTable("refs", TableTestSuite.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        try {
            db.table("refs").row(new IntegerValue(0))
                .set(1, new ReferenceValue("table"));
            Assert
                .fail("Shouldn't be able to set row entry to invalid reference");
        } catch (InvalidOperation e) {
            
        }
    }
    
    @Test
    public void testInvalidReferenceSet5() {
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
        Value[][] tableRows = MyRowTests.createValues(rawTableRows);
        Value[][] refRows = MyRowTests.createValues(rawRefRows);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.createTable("refs", TableTestSuite.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        try {
            db.table("refs").row(new IntegerValue(0))
                .set(1, new ReferenceValue("table", "invalidKey"));
            Assert
                .fail("Shouldn't be able to set row entry to invalid reference");
        } catch (InvalidOperation e) {
            
        }
    }
    
    @Test
    public void testInvalidRowModification1() {
        Object[][] rawData = {
            {
                0, "Hello WOrld"
            }, {
                1, "Blah"
            }
        };
        Value[][] rows = MyRowTests.createValues(rawData);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));
        
        try {
            // Here, we're trying to set a key value ... should be impossible!
            db.table("table").row(new IntegerValue(0))
                .add(new StringValue("Hello"));
            Assert.fail("Shouldn't be able to add field to row");
        } catch (InvalidOperation e) {
            
        }
    }
    
    @Test
    public void testInvalidRowModification2() {
        Object[][] rawData = {
            {
                0, "Hello WOrld"
            }, {
                1, "Blah"
            }
        };
        Value[][] rows = MyRowTests.createValues(rawData);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));
        
        try {
            // Here, we're trying to set a key value ... should be impossible!
            db.table("table").row(new IntegerValue(0)).remove(0);
            Assert.fail("Shouldn't be able to remove field from row");
        } catch (InvalidOperation e) {
            
        }
    }
    
    @Test
    public void testInvalidSet() {
        MyDatabase db = TableTestSuite.generateDb();
        
        try {
            db.table("table").rows().get(0).set(-1, new IntegerValue(1));
        } catch (InvalidOperation e) {}
        try {
            db.table("table").rows().get(0).set(0, new IntegerValue(1));
        } catch (InvalidOperation e) {}
        try {
            db.table("table").rows().get(0).set(1, new IntegerValue(1));
        } catch (InvalidOperation e) {}
    }
    
    @Test
    public void testInvalidSetRow1() {
        Object[][] rawData = {
            {
                0, "Hello WOrld"
            }, {
                1, "Blah"
            }
        };
        Value[][] rows = MyRowTests.createValues(rawData);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));
        
        try {
            // Here, we're trying to set a key value ... should be impossible!
            db.table("table").row(new IntegerValue(0))
                .set(0, new IntegerValue(1));
            Assert.fail("Shouldn't be able to change value of key field");
        } catch (InvalidOperation e) {
            
        }
    }
    
    @Test
    public void testInvalidSetRow2() {
        Object[][] rawData = {
            {
                0, "Hello WOrld"
            }, {
                1, "Blah"
            }
        };
        Value[][] rows = MyRowTests.createValues(rawData);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));
        
        try {
            // Here, we're trying to set a key value ... should be impossible!
            db.table("table").row(new IntegerValue(0))
                .set(1, new IntegerValue(1));
            Assert
                .fail("Shouldn't be able to field to value of incorrect type");
        } catch (InvalidOperation e) {
            
        }
    }
    
    @Test
    public void testInvalidSetRow3() {
        Object[][] rawData = {
            {
                0, "Hello WOrld"
            }, {
                1, "Blah"
            }
        };
        Value[][] rows = MyRowTests.createValues(rawData);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));
        
        try {
            // Here, we're trying to set a key value ... should be impossible!
            db.table("table")
                .row(new IntegerValue(0))
                .set(
                    1,
                    new StringValue(
                        "A TEXT field cannot have new lines\nLike this.  Only TEXTAREAs can."));
            Assert
                .fail("Shouldn't be able to field to value of incorrect type");
        } catch (InvalidOperation e) {
            
        }
    }
    
    @Test
    public void testValidAddRow() {
        Object[][] rawData = {
            {
                0, "Hello WOrld"
            }, {
                1, "Blah"
            }
        };
        Value[][] rows = MyRowTests.createValues(rawData);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));
        
        Assert.assertTrue(db.table("table").rows().size() == rows.length);
        
        for (Value[] row : rows) {
            Assert
                .assertTrue(db.table("table").row(row[0]).size() == row.length);
            for (int i = 0; i != row.length; ++i)
                Assert.assertTrue(db.table("table").row(row[0]).get(i)
                    .equals(row[i]));
        }
    }
    
    @Test
    public void testValidGet() {
        MyDatabase db = TableTestSuite.generateDb();
        
        db.table("table").rows().get(0).get(0);
    }
    
    @Test
    public void testValidReferenceSet() {
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
        Value[][] tableRows = MyRowTests.createValues(rawTableRows);
        Value[][] refRows = MyRowTests.createValues(rawRefRows);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.createTable("refs", TableTestSuite.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        db.table("refs").row(new IntegerValue(0))
            .set(1, new ReferenceValue("table", 1));
    }
    
    @Test
    public void testValidSetRow() {
        Object[][] rawData = {
            {
                0, "Hello WOrld"
            }, {
                1, "Blah"
            }
        };
        Value[][] rows = MyRowTests.createValues(rawData);
        
        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));
        db.table("table").row(new IntegerValue(0))
            .set(1, new StringValue("Blah"));
        
        rows[0][1] = new StringValue("Blah"); // mirror update
        
        for (Value[] row : rows) {
            Assert
                .assertTrue(db.table("table").row(row[0]).size() == row.length);
            for (int i = 0; i != row.length; ++i)
                Assert.assertTrue(db.table("table").row(row[0]).get(i)
                    .equals(row[i]));
        }
    }
    
    /**
     * Convert a 2d array of objects into a 2d array of Value objects. The reason for this method is
     * that it simplifies the construction of test databases.
     */
    private static Value[][] createValues(Object[][] rawData) {
        Value[][] values = new Value[rawData.length][];
        
        for (int i = 0; i != values.length; ++i) {
            Object[] rawRow = rawData[i];
            Value[] row = new Value[rawRow.length];
            values[i] = row;
            
            for (int j = 0; j != rawRow.length; ++j) {
                Object o = rawRow[j];
                if (o instanceof Integer)
                    row[j] = new IntegerValue((Integer) o);
                else if (o instanceof Boolean)
                    row[j] = new BooleanValue((Boolean) o);
                else if (o instanceof String)
                    row[j] = new StringValue((String) o);
                else if (o instanceof ReferenceValue)
                    row[j] = (ReferenceValue) o;
                else
                    throw new IllegalArgumentException(
                        "Invalid key parameters: " + o);
            }
        }
        
        return values;
    }
}
