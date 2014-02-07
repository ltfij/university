package com.hjwylde.uni.swen221.lab09.com.bytebach.tests.table;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.hjwylde.uni.swen221.assignment06.com.bytebach.impl.MyDatabase;
import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.*;

/*
 * Code for Laboratory 9, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({MyTableTests.class, MyRowTests.class, MyRowListTests.class})
public class TableTestSuite {

    @SuppressWarnings("serial")
    public static final ArrayList<Field> FIELDS_1 = new ArrayList<Field>() {

        {
            add(new Field("ID", Field.Type.INTEGER, true));
            add(new Field("TEXT", Field.Type.TEXT, false));
        }
    };

    @SuppressWarnings("serial")
    public static final ArrayList<Field> FIELDS_2 = new ArrayList<Field>() {

        {
            add(new Field("ID", Field.Type.INTEGER, true));
            add(new Field("REF", "table", false));
        }
    };

    @SuppressWarnings("serial")
    public static final ArrayList<Field> FIELDS_3 = new ArrayList<Field>() {

        {
            add(new Field("ID", Field.Type.INTEGER, true));
            add(new Field("TEXT", Field.Type.TEXTAREA, false));
            add(new Field("BOOL", Field.Type.BOOLEAN, false));
        }
    };

    public static final Object[][] RAW_TABLE_ROWS = { {0, "Hello WOrld"}, {1, "Blah"}, {2, "Woop"},
            {3, "Nuhuh"}};
    public static final Object[][] RAW_TABLE3_ROWS = { {0, "Hello WOrld", true},
            {1, "Blah", false}, {2, "Woop", true}, {3, "Nuhuh", true}};
    public static final Object[][] RAW_REF_ROWS = { {0, new ReferenceValue("table", 0)},
            {1, new ReferenceValue("table", 1)}, {2, new ReferenceValue("table", 0)}};
    public static final Object[][] RAW_REF_INVALID_ROWS = {{0, "Not a reference value"}};

    public static final Value[][] TABLE_ROWS = TableTestSuite
            .createValues(TableTestSuite.RAW_TABLE_ROWS);
    public static final Value[][] TABLE3_ROWS = TableTestSuite
            .createValues(TableTestSuite.RAW_TABLE3_ROWS);
    public static final Value[][] REF_ROWS = TableTestSuite
            .createValues(TableTestSuite.RAW_REF_ROWS);
    public static final Value[][] REF_INVALID_ROWS = TableTestSuite
            .createValues(TableTestSuite.RAW_REF_INVALID_ROWS);

    /**
     * Convert a 2d array of objects into a 2d array of Value objects. The reason for this method is
     * that it simplifies the construction of test databases.
     */
    public static Value[][] createValues(Object[][] rawData) {
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

    public static MyDatabase generateDb() {
        MyDatabase db = new MyDatabase();

        db.createTable("table", TableTestSuite.FIELDS_1);
        db.table("table").rows().add(Arrays.asList(TableTestSuite.TABLE_ROWS[0]));
        db.table("table").rows().add(Arrays.asList(TableTestSuite.TABLE_ROWS[1]));

        db.createTable("refs", TableTestSuite.FIELDS_2);
        db.table("refs").rows().add(Arrays.asList(TableTestSuite.REF_ROWS[0]));
        db.table("refs").rows().add(Arrays.asList(TableTestSuite.REF_ROWS[1]));

        db.createTable("table3", TableTestSuite.FIELDS_3);
        db.table("table3").rows().add(Arrays.asList(TableTestSuite.TABLE3_ROWS[0]));
        db.table("table3").rows().add(Arrays.asList(TableTestSuite.TABLE3_ROWS[1]));
        db.table("table3").rows().add(Arrays.asList(TableTestSuite.TABLE3_ROWS[2]));

        return db;
    }
}
