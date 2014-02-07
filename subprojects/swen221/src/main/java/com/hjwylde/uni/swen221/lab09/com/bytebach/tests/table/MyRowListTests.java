package com.hjwylde.uni.swen221.lab09.com.bytebach.tests.table;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.assignment06.com.bytebach.impl.MyDatabase;
import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.InvalidOperation;
import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.ReferenceValue;
import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.Value;

/*
 * Code for Laboratory 9, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class MyRowListTests {

    @Test
    public void testInvalidAdd() {
        MyDatabase db = TableTestSuite.generateDb();

        try {
            db.table("table").rows().add(1, null);
            Assert.fail();
        } catch (InvalidOperation e) {
        }
        try {
            db.table("table").rows().add(-1, Arrays.asList(TableTestSuite.TABLE_ROWS[0]));
            Assert.fail();
        } catch (InvalidOperation e) {
        }
        try {
            db.table("table").rows().add(11, Arrays.asList(TableTestSuite.TABLE_ROWS[0]));
            Assert.fail();
        } catch (InvalidOperation e) {
        }
        try {
            db.table("table").rows().add(Arrays.asList(TableTestSuite.REF_ROWS[0]));
            Assert.fail();
        } catch (InvalidOperation e) {
        }
        try {
            db.table("table").rows().add(Arrays.asList(TableTestSuite.TABLE_ROWS[0]));
            Assert.fail();
        } catch (InvalidOperation e) {
        }
    }

    @Test
    public void testInvalidGet() {
        MyDatabase db = TableTestSuite.generateDb();

        try {
            db.table("table").rows().get(-1);
            Assert.fail();
        } catch (InvalidOperation e) {
        }
        try {
            db.table("table").rows().get(11);
            Assert.fail();
        } catch (InvalidOperation e) {
        }
    }

    @Test
    public void testInvalidRemove() {
        MyDatabase db = TableTestSuite.generateDb();

        try {
            db.table("table").rows().remove(-1);
            Assert.fail();
        } catch (InvalidOperation e) {
        }
        try {
            db.table("table").rows().remove(11);
            Assert.fail();
        } catch (InvalidOperation e) {
        }
    }

    @Test
    public void testInvalidSet() {
        MyDatabase db = TableTestSuite.generateDb();

        try {
            db.table("table").rows().set(-1, Arrays.asList(TableTestSuite.TABLE_ROWS[0]));
            Assert.fail();
        } catch (InvalidOperation e) {
        }
        try {
            db.table("table").rows().set(0, null);
            Assert.fail();
        } catch (InvalidOperation e) {
        }
        try {
            db.table("table").rows().set(1, Arrays.asList(TableTestSuite.REF_ROWS[0]));
            Assert.fail();
        } catch (InvalidOperation e) {
        }
        try {
            db.table("table").rows().set(1, Arrays.asList(TableTestSuite.TABLE_ROWS[0]));
            Assert.fail();
        } catch (InvalidOperation e) {
        }
        try {
            db.table("refs").rows().set(0, Arrays.asList(TableTestSuite.REF_INVALID_ROWS[0]));
            Assert.fail();
        } catch (InvalidOperation e) {
        }
        try {
            db.table("table3")
                    .rows()
                    .set(0,
                            Arrays.asList(TableTestSuite.createValues(new Object[][] {{5, "String",
                                    "Not a boolean"}})[0]));
            Assert.fail();
        } catch (InvalidOperation e) {
        }
    }

    @Test
    public void testValidAdd() {
        TableTestSuite.generateDb();
    }

    @Test
    public void testValidGet() {
        MyDatabase db = TableTestSuite.generateDb();

        Assert.assertTrue(db.table("table").rows().get(0) != null);
        Assert.assertTrue(db.table("table").rows().get(1) != null);
    }

    @Test
    public void testValidReferenceAdd() {
        Object[][] rawTableRows = { {0, "Hello WOrld"}, {1, "Blah"}};
        Object[][] rawRefRows = {{0, new ReferenceValue("table", 0)}};
        Value[][] tableRows = TableTestSuite.createValues(rawTableRows);
        Value[][] refRows = TableTestSuite.createValues(rawRefRows);

        MyDatabase db = new MyDatabase();
        db.createTable("table", TableTestSuite.FIELDS_1);
        db.createTable("refs", TableTestSuite.FIELDS_2);
        db.table("table").rows().add(Arrays.asList(tableRows[0]));
        db.table("table").rows().add(Arrays.asList(tableRows[1]));
        db.table("refs").rows().add(Arrays.asList(refRows[0]));
    }

    @Test
    public void testValidRemove() {
        MyDatabase db = TableTestSuite.generateDb();

        db.table("table").rows().remove(0);
        Assert.assertTrue(db.table("table").rows().size() == 1);

        db.table("table").rows().remove(0);
        Assert.assertTrue(db.table("table").rows().size() == 0);
    }

    @Test
    public void testValidSet() {
        MyDatabase db = TableTestSuite.generateDb();

        db.table("table").rows().set(0, Arrays.asList(TableTestSuite.TABLE_ROWS[0]));
        db.table("table").rows().set(1, Arrays.asList(TableTestSuite.TABLE_ROWS[2]));
        db.table("refs").rows().set(1, Arrays.asList(TableTestSuite.REF_ROWS[2]));
        db.table("table3").rows().set(0, Arrays.asList(TableTestSuite.TABLE3_ROWS[3]));
    }

    /*
     * @Test public void testValidRemoveRow1() { Object[][] rawData = { { 0, "Hello WOrld" }, { 1,
     * "Blah" } }; Value[][] rows = MyTableTestSuites.createValues(rawData); MyDatabase db = new
     * MyDatabase(); db.createTable("table", MyTableTestSuites.FIELDS_1);
     * db.table("table").rows().add(Arrays.asList(rows[0]));
     * db.table("table").rows().add(Arrays.asList(rows[1])); db.table("table").delete(new
     * IntegerValue(0)); Value[][] nrows = MyTableTestSuites.createValues(new Object[][] { { 1,
     * "Blah" } }); assertTrue(db.table("table").rows().size() == nrows.length); for (Value[] row :
     * nrows) { assertTrue(db.table("table").row(row[0]).size() == row.length); for (int i = 0; i !=
     * row.length; ++i) assertTrue(db.table("table").row(row[0]).get(i).equals(row[i])); } }
     * @Test public void testValidRemoveRow2() { Object[][] rawData = { { 0, "Hello WOrld" }, { 1,
     * "Blah" } }; Value[][] rows = MyTableTestSuites.createValues(rawData); MyDatabase db = new
     * MyDatabase(); db.createTable("table", MyTableTestSuites.FIELDS_1);
     * db.table("table").rows().add(Arrays.asList(rows[0]));
     * db.table("table").rows().add(Arrays.asList(rows[1])); db.table("table").rows().remove(0);
     * Value[][] nrows = MyTableTestSuites.createValues(new Object[][] { { 1, "Blah" } });
     * assertTrue(db.table("table").rows().size() == nrows.length); for (Value[] row : nrows) {
     * assertTrue(db.table("table").row(row[0]).size() == row.length); for (int i = 0; i !=
     * row.length; ++i) assertTrue(db.table("table").row(row[0]).get(i).equals(row[i])); } }
     */
}
