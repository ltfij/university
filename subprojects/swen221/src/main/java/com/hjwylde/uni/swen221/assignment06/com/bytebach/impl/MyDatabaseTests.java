package com.hjwylde.uni.swen221.assignment06.com.bytebach.impl;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.*;

/*
 * Code for Assignment 6, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
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

    @Test
    public void testCascadingDelete1() {
        Object[][] rawTableRows = { {0, "Hello WOrld"}, {1, "Blah"}};
        Object[][] rawRefRows = {{0, new ReferenceValue("table", 0)}};
        Value[][] tableRows = MyDatabaseTests.createValues(rawTableRows);
        Value[][] refRows = MyDatabaseTests.createValues(rawRefRows);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("refs", MyDatabaseTests.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        db.table("table").delete(new IntegerValue(0));
        Assert.assertTrue(db.table("refs").rows().size() == 0);
    }

    @Test
    public void testCascadingDelete2() {
        Object[][] rawTableRows = { {0, "Hello WOrld"}, {1, "Blah"}};
        Object[][] rawRefRows =
                { {0, new ReferenceValue("table", 0)}, {1, new ReferenceValue("table", 1)}};
        Value[][] tableRows = MyDatabaseTests.createValues(rawTableRows);
        Value[][] refRows = MyDatabaseTests.createValues(rawRefRows);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("refs", MyDatabaseTests.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        db.table("refs").rows().add(Arrays.asList(refRows[1]));
        db.table("table").delete(new IntegerValue(0));
        Assert.assertTrue(db.table("refs").rows().size() == 1);
    }

    @Test
    public void testCascadingDelete3() {
        Object[][] rawTableRows = { {0, "Hello WOrld"}, {1, "Blah"}};
        Object[][] rawRefRows =
                { {0, new ReferenceValue("table", 0)}, {1, new ReferenceValue("table", 1)},
                        {2, new ReferenceValue("table", 0)}};
        Value[][] tableRows = MyDatabaseTests.createValues(rawTableRows);
        Value[][] refRows = MyDatabaseTests.createValues(rawRefRows);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("refs", MyDatabaseTests.FIELDS_2);
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
        Object[][] rawTableRows = { {0, "Hello WOrld"}, {1, "Blah"}};
        Object[][] rawRefRows =
                { {0, new ReferenceValue("table", 0)}, {1, new ReferenceValue("table", 1)},
                        {2, new ReferenceValue("table", 0)}};
        Value[][] tableRows = MyDatabaseTests.createValues(rawTableRows);
        Value[][] refRows = MyDatabaseTests.createValues(rawRefRows);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("refs", MyDatabaseTests.FIELDS_2);
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
        Object[][] rawData = { {0, "Hello WOrld"}, {0, "Blah"}};
        Value[][] rows = MyDatabaseTests.createValues(rawData);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        try {
            db.table("table").rows().add(Arrays.asList(rows[1]));
            Assert.fail("Shouldn't be able to add row with same key field as another");
        } catch (InvalidOperation e) {

        }
    }

    @Test
    public void testInvalidCreateTable() {
        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        try {
            db.createTable("table", MyDatabaseTests.FIELDS_1);
            Assert.fail("Shouldn't be able to creat table with same name");
        } catch (InvalidOperation e) {
        }
    }

    @Test
    public void testInvalidReferenceAdd1() {
        Object[][] rawTableRows = { {0, "Hello WOrld"}, {1, "Blah"}};
        Object[][] rawRefRows = {{0, new ReferenceValue("table", 2)}};
        Value[][] tableRows = MyDatabaseTests.createValues(rawTableRows);
        Value[][] refRows = MyDatabaseTests.createValues(rawRefRows);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("refs", MyDatabaseTests.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        try {
            db.table("refs").rows().add(Arrays.asList(refRows[0]));
            Assert.fail("Shouldn't be able to add row containing invalid reference");
        } catch (InvalidOperation e) {

        }
    }

    @Test
    public void testInvalidReferenceAdd2() {
        Object[][] rawTableRows = { {0, "Hello WOrld"}, {1, "Blah"}};
        Object[][] rawRefRows = {{0, new ReferenceValue("BROKEN", 0)}};
        Value[][] tableRows = MyDatabaseTests.createValues(rawTableRows);
        Value[][] refRows = MyDatabaseTests.createValues(rawRefRows);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("refs", MyDatabaseTests.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        try {
            db.table("refs").rows().add(Arrays.asList(refRows[0]));
            Assert.fail("Shouldn't be able to add row containing invalid reference");
        } catch (InvalidOperation e) {

        }
    }

    @Test
    public void testInvalidReferenceSet1() {
        Object[][] rawTableRows = { {0, "Hello WOrld"}, {1, "Blah"}};
        Object[][] rawRefRows = {{0, new ReferenceValue("table", 0)}};
        Value[][] tableRows = MyDatabaseTests.createValues(rawTableRows);
        Value[][] refRows = MyDatabaseTests.createValues(rawRefRows);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("refs", MyDatabaseTests.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        try {
            db.table("refs").row(new IntegerValue(0)).set(1, new ReferenceValue("table", 2));
            Assert.fail("Shouldn't be able to set row entry to invalid reference");
        } catch (InvalidOperation e) {

        }
    }

    @Test
    public void testInvalidReferenceSet2() {
        Object[][] rawTableRows = { {0, "Hello WOrld"}, {1, "Blah"}};
        Object[][] rawRefRows = {{0, new ReferenceValue("table", 0)}};
        Value[][] tableRows = MyDatabaseTests.createValues(rawTableRows);
        Value[][] refRows = MyDatabaseTests.createValues(rawRefRows);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("refs", MyDatabaseTests.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        try {
            db.table("refs").row(new IntegerValue(0)).set(1, new ReferenceValue("BROKEN", 2));
            Assert.fail("Shouldn't be able to set row entry to invalid reference");
        } catch (InvalidOperation e) {

        }
    }

    @Test
    public void testInvalidReferenceSet3() {
        Object[][] rawTableRows = { {0, "Hello WOrld"}, {1, "Blah"}};
        Object[][] rawRefRows = {{0, new ReferenceValue("table", 0)}};
        Value[][] tableRows = MyDatabaseTests.createValues(rawTableRows);
        Value[][] refRows = MyDatabaseTests.createValues(rawRefRows);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("refs", MyDatabaseTests.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        try {
            db.table("refs").row(new IntegerValue(0)).set(1, new ReferenceValue("table", 1, 1));
            Assert.fail("Shouldn't be able to set row entry to invalid reference");
        } catch (InvalidOperation e) {

        }
    }

    @Test
    public void testInvalidReferenceSet4() {
        Object[][] rawTableRows = { {0, "Hello WOrld"}, {1, "Blah"}};
        Object[][] rawRefRows = {{0, new ReferenceValue("table", 0)}};
        Value[][] tableRows = MyDatabaseTests.createValues(rawTableRows);
        Value[][] refRows = MyDatabaseTests.createValues(rawRefRows);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("refs", MyDatabaseTests.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        try {
            db.table("refs").row(new IntegerValue(0)).set(1, new ReferenceValue("table"));
            Assert.fail("Shouldn't be able to set row entry to invalid reference");
        } catch (InvalidOperation e) {

        }
    }

    @Test
    public void testInvalidReferenceSet5() {
        Object[][] rawTableRows = { {0, "Hello WOrld"}, {1, "Blah"}};
        Object[][] rawRefRows = {{0, new ReferenceValue("table", 0)}};
        Value[][] tableRows = MyDatabaseTests.createValues(rawTableRows);
        Value[][] refRows = MyDatabaseTests.createValues(rawRefRows);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("refs", MyDatabaseTests.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        try {
            db.table("refs").row(new IntegerValue(0))
                    .set(1, new ReferenceValue("table", "invalidKey"));
            Assert.fail("Shouldn't be able to set row entry to invalid reference");
        } catch (InvalidOperation e) {

        }
    }

    @Test
    public void testInvalidRowModification1() {
        Object[][] rawData = { {0, "Hello WOrld"}, {1, "Blah"}};
        Value[][] rows = MyDatabaseTests.createValues(rawData);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));

        try {
            // Here, we're trying to set a key value ... should be impossible!
            db.table("table").row(new IntegerValue(0)).add(new StringValue("Hello"));
            Assert.fail("Shouldn't be able to add field to row");
        } catch (InvalidOperation e) {

        }
    }

    @Test
    public void testInvalidRowModification2() {
        Object[][] rawData = { {0, "Hello WOrld"}, {1, "Blah"}};
        Value[][] rows = MyDatabaseTests.createValues(rawData);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
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
    public void testInvalidSetRow1() {
        Object[][] rawData = { {0, "Hello WOrld"}, {1, "Blah"}};
        Value[][] rows = MyDatabaseTests.createValues(rawData);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));

        try {
            // Here, we're trying to set a key value ... should be impossible!
            db.table("table").row(new IntegerValue(0)).set(0, new IntegerValue(1));
            Assert.fail("Shouldn't be able to change value of key field");
        } catch (InvalidOperation e) {

        }
    }

    @Test
    public void testInvalidSetRow2() {
        Object[][] rawData = { {0, "Hello WOrld"}, {1, "Blah"}};
        Value[][] rows = MyDatabaseTests.createValues(rawData);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));

        try {
            // Here, we're trying to set a key value ... should be impossible!
            db.table("table").row(new IntegerValue(0)).set(1, new IntegerValue(1));
            Assert.fail("Shouldn't be able to field to value of incorrect type");
        } catch (InvalidOperation e) {

        }
    }

    @Test
    public void testInvalidSetRow3() {
        Object[][] rawData = { {0, "Hello WOrld"}, {1, "Blah"}};
        Value[][] rows = MyDatabaseTests.createValues(rawData);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));

        try {
            // Here, we're trying to set a key value ... should be impossible!
            db.table("table")
                    .row(new IntegerValue(0))
                    .set(1,
                            new StringValue(
                                    "A TEXT field cannot have new lines\nLike this.  Only TEXTAREAs can."));
            Assert.fail("Shouldn't be able to field to value of incorrect type");
        } catch (InvalidOperation e) {

        }
    }

    @Test
    public void testValidAddRow() {
        Object[][] rawData = { {0, "Hello WOrld"}, {1, "Blah"}};
        Value[][] rows = MyDatabaseTests.createValues(rawData);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));

        Assert.assertTrue(db.table("table").rows().size() == rows.length);

        for (Value[] row : rows) {
            Assert.assertTrue(db.table("table").row(row[0]).size() == row.length);
            for (int i = 0; i != row.length; ++i)
                Assert.assertTrue(db.table("table").row(row[0]).get(i).equals(row[i]));
        }
    }

    @Test
    public void testValidCreateTable() {
        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
    }

    @Test
    public void testValidReferenceAdd() {
        Object[][] rawTableRows = { {0, "Hello WOrld"}, {1, "Blah"}};
        Object[][] rawRefRows = {{0, new ReferenceValue("table", 0)}};
        Value[][] tableRows = MyDatabaseTests.createValues(rawTableRows);
        Value[][] refRows = MyDatabaseTests.createValues(rawRefRows);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("refs", MyDatabaseTests.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
    }

    @Test
    public void testValidReferenceSet() {
        Object[][] rawTableRows = { {0, "Hello WOrld"}, {1, "Blah"}};
        Object[][] rawRefRows = {{0, new ReferenceValue("table", 0)}};
        Value[][] tableRows = MyDatabaseTests.createValues(rawTableRows);
        Value[][] refRows = MyDatabaseTests.createValues(rawRefRows);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.createTable("refs", MyDatabaseTests.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
        db.table("refs").row(new IntegerValue(0)).set(1, new ReferenceValue("table", 1));
    }

    @Test
    public void testValidRemoveRow1() {
        Object[][] rawData = { {0, "Hello WOrld"}, {1, "Blah"}};
        Value[][] rows = MyDatabaseTests.createValues(rawData);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));
        db.table("table").delete(new IntegerValue(0));

        Value[][] nrows = MyDatabaseTests.createValues(new Object[][] {{1, "Blah"}});

        Assert.assertTrue(db.table("table").rows().size() == nrows.length);

        for (Value[] row : nrows) {
            Assert.assertTrue(db.table("table").row(row[0]).size() == row.length);
            for (int i = 0; i != row.length; ++i)
                Assert.assertTrue(db.table("table").row(row[0]).get(i).equals(row[i]));
        }
    }

    @Test
    public void testValidRemoveRow2() {
        Object[][] rawData = { {0, "Hello WOrld"}, {1, "Blah"}};
        Value[][] rows = MyDatabaseTests.createValues(rawData);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));
        db.table("table").rows().remove(0);

        Value[][] nrows = MyDatabaseTests.createValues(new Object[][] {{1, "Blah"}});

        Assert.assertTrue(db.table("table").rows().size() == nrows.length);

        for (Value[] row : nrows) {
            Assert.assertTrue(db.table("table").row(row[0]).size() == row.length);
            for (int i = 0; i != row.length; ++i)
                Assert.assertTrue(db.table("table").row(row[0]).get(i).equals(row[i]));
        }
    }

    @Test
    public void testValidSetRow() {
        Object[][] rawData = { {0, "Hello WOrld"}, {1, "Blah"}};
        Value[][] rows = MyDatabaseTests.createValues(rawData);

        MyDatabase db = new MyDatabase();
        db.createTable("table", MyDatabaseTests.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(rows[0]));
        db.table("table").rows().add(Arrays.asList(rows[1]));
        db.table("table").row(new IntegerValue(0)).set(1, new StringValue("Blah"));

        rows[0][1] = new StringValue("Blah"); // mirror update

        for (Value[] row : rows) {
            Assert.assertTrue(db.table("table").row(row[0]).size() == row.length);
            for (int i = 0; i != row.length; ++i)
                Assert.assertTrue(db.table("table").row(row[0]).get(i).equals(row[i]));
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
                    throw new IllegalArgumentException("Invalid key parameters: " + o);
            }
        }

        return values;
    }
}
