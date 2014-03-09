package com.hjwylde.uni.swen221.assignment06.com.bytebach.impl;

import java.util.*;

import com.hjwylde.uni.swen221.assignment06.com.bytebach.Main;
import com.hjwylde.uni.swen221.assignment06.com.bytebach.model.*;

/*
 * Code for Assignment 6, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class MyTable implements Table {

    /**
     * A comparator that compares just the names of a table.
     */
    public static final Comparator<Table> COMP_TABLE_NAME = new Comparator<Table>() {

        /**
         * @param self the self table.
         * @param other the other table.
         * @return a value < 0 if self comes before other.
         */
        @Override
        public int compare(Table self, Table other) {
            return self.name().compareTo(other.name());
        }

    };

    private final Database db;

    private final String name;
    private final List<Field> schema;
    private final List<List<Value>> rows;

    /**
     * Creates a new table with the given fields. Throws an invalid operation if any of them are
     * null, or if there exists a reference field that references a non-existant table.
     * 
     * @param db the database this table is contained in.
     * @param name the name of the table.
     * @param fields the list of fields for this table.
     */
    public MyTable(Database db, String name, List<Field> fields) {
        Main.validateParameters(db, name, fields);

        // Check if any of the fields is a reference field and references a non-existant table.
        for (Field field : fields)
            if (field.type() == Field.Type.REFERENCE)
                if (db.table(field.refTable()) == null)
                    throw new InvalidOperation("Invalid field list.");

        this.db = db;

        this.name = name;
        schema = Collections.unmodifiableList(fields);
        rows = new RowList();
    }

    /*
     * @see assignment6.com.bytebach.model.Table#delete(assignment6.com.bytebach.model.Value[])
     */
    @Override
    public void delete(Value... keys) {
        Main.validateParameters((Object[]) keys);

        // Find the index of the row.
        int index = rowIndex(keys);

        // If the row doesn't exist...
        if (index < 0)
            return;

        // Remove the row!
        rows.remove(index);
    }

    /*
     * @see assignment6.com.bytebach.model.Table#fields()
     */
    @Override
    public List<Field> fields() {
        return schema;
    }

    /*
     * @see assignment6.com.bytebach.model.Table#name()
     */
    @Override
    public String name() {
        return name;
    }

    /**
     * Get a list of values for the given primary keys.
     * 
     * @param keys the primary keys.
     * @return the row that contains those keys.
     */
    public List<Value> row(List<Value> keys) {
        Main.validateParameters(keys);

        // Find the index of the row.
        int index = rowIndex(keys);

        // Return either the row or null if it didn't exist.
        return (index >= 0 ? rows.get(index) : null);
    }

    /*
     * @see assignment6.com.bytebach.model.Table#row(assignment6.com.bytebach.model.Value[])
     */
    @Override
    public List<Value> row(Value... keys) {
        Main.validateParameters((Object[]) keys);

        return row(Arrays.asList(keys));
    }

    /*
     * @see assignment6.com.bytebach.model.Table#rows()
     */
    @Override
    public List<List<Value>> rows() {
        return rows;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "\nTable : '" + name + "',\n" + rows;
    }

    /**
     * Ensure data integrity by deleting any row in other tables in the database when a row is
     * deleted. The given row is the row just deleted.
     * 
     * @param row the deleted row.
     */
    private void cascadeDelete(List<Value> row) {
        Main.validateParameters(row);

        // Find the primary key values for the row.
        List<Value> rowKeyFieldValues = getRowKeyFieldValues(row);

        List<List<Value>> toDelete;

        Field field;
        for (Table t : db.tables()) {
            if (t == this) // Ignore this table.
                continue;

            // Go through all the fields and add to a new list the row if it should be deleted.
            toDelete = new ArrayList<>();
            for (int i = 0; i < t.fields().size(); i++) {
                field = t.fields().get(i);

                // We can only check reference fields.
                if (field.type() != Field.Type.REFERENCE)
                    continue;

                // Must match the name of the table of the row that was just deleted.
                if (!field.refTable().equals(name))
                    continue;

                // Go through each row, if it references the keys of the row just deleted, add it to
                // the
                // list.
                for (List<Value> r : t.rows())
                    if (Arrays.asList(((ReferenceValue) r.get(i)).keys()).equals(rowKeyFieldValues))
                        toDelete.add(r);
            }

            // Delete them all.
            for (List<Value> r : toDelete)
                t.delete(MyTable.getRowFieldValues(MyTable.getKeyFieldIndices(t.fields()), r)
                        .toArray(new Value[1]));
        }
    }

    /**
     * Ensure data integrity when a row is updated by altering any references to that row. A row may
     * have its primary keys updated.
     * 
     * @param oldRow the old row that was changed.
     * @param newRow the new rows values.
     */
    private void cascadeUpdate(List<Value> oldRow, List<Value> newRow) {
        Main.validateParameters(oldRow, newRow);

        List<Value> oldRowKeyFieldValues = getRowKeyFieldValues(oldRow);
        List<Object> newRowKeyFieldValues = new ArrayList<>();

        for (Value v : getRowKeyFieldValues(newRow))
            newRowKeyFieldValues.add(ReferenceValue.getRawValueObject(v));

        Field field;
        // Go through each table that isn't this one...
        for (Table t : db.tables()) {
            if (t == this)
                continue;

            // Go through every field.
            for (int i = 0; i < t.fields().size(); i++) {
                field = t.fields().get(i);

                if (field.type() != Field.Type.REFERENCE)
                    continue;

                if (!field.refTable().equals(name))
                    continue;

                // The field is a reference field referencing this table.

                // Find the rows that reference the old row, and alter its reference value to point
                // to the
                // new rows keys.
                for (List<Value> row : t.rows())
                    if (Arrays.asList(((ReferenceValue) row.get(i)).keys()).equals(
                            oldRowKeyFieldValues))
                        row.set(i, new ReferenceValue(name, newRowKeyFieldValues.toArray()));

                // NOTE: Will not change if the reference field is a primary key
            }
        }
    }

    /**
     * Checks if this table contains the given row from a list of primary keys.
     * 
     * @param keys the primary keys.
     * @return true if the row is contained in this table.
     */
    private boolean containsRow(List<Value> keys) {
        Main.validateParameters(keys);

        return rowIndex(keys) >= 0;
    }

    /**
     * Gets the primary key field indices for this table.
     * 
     * @return a list of indices for the primary key fields.
     */
    private List<Integer> getKeyFieldIndices() {
        return MyTable.getKeyFieldIndices(schema);
    }

    /**
     * Gets a list of the primary key fields for this table.
     * 
     * @return a list of the primary key fields.
     */
    private List<Field> getKeyFields() {
        List<Field> keyFields = new ArrayList<>();
        for (Field field : schema)
            if (field.isKey())
                keyFields.add(field);

        return Collections.unmodifiableList(keyFields);
    }

    /**
     * Gets the primary key field values for a given row.
     * 
     * @param row the row to get the primary key values from.
     * @return a list of primary key field values.
     */
    private List<Value> getRowKeyFieldValues(List<Value> row) {
        return MyTable.getRowFieldValues(getKeyFieldIndices(), row);
    }

    /**
     * Checks whether the given value is a valid value for the given field.
     * 
     * @param field the field for the value.
     * @param value the value to check.
     * @return true if it is valid.
     */
    private boolean isValidFieldValue(Field field, Value value) {
        Main.validateParameters(field, value);

        switch (field.type()) {
            case REFERENCE:
                if (!(value instanceof ReferenceValue))
                    return false;

                // A reference value must reference the same table as its field header.
                ReferenceValue refValue = (ReferenceValue) value;
                if (!(refValue.table().equals(field.refTable())))
                    return false;

                // A reference value must reference a valid row.
                if (db.table(refValue.table()).row(refValue.keys()) == null)
                    return false;

                break;
            case BOOLEAN:
                if (!(value instanceof BooleanValue))
                    return false;
                break;
            case INTEGER:
                if (!(value instanceof IntegerValue))
                    return false;
                break;
            case TEXT:
            case TEXTAREA:
                if (!(value instanceof StringValue))
                    return false;

                // A text value can't contain new lines.
                if (field.type() == Field.Type.TEXT)
                    if (((StringValue) value).value().contains("\n"))
                        return false;

                break;
        }

        return true;
    }

    /**
     * Checks whether the list of values are all valid for the list of fields given.
     * 
     * @param fields the list of fields to check against.
     * @param row the row to check.
     * @return true if the row is valid.
     */
    private boolean isValidRow(List<Field> fields, List<Value> row) {
        Main.validateParameters(fields, row);

        // Can't be valid if they aren't the same size.
        if (row.size() != fields.size())
            return false;

        // Check that each value is valid for its corresponding field.
        for (int i = 0; i < fields.size(); i++)
            if (!isValidFieldValue(fields.get(i), row.get(i)))
                return false;

        return true;
    }

    /**
     * Checks if a row is a valid row.
     * 
     * @param row the row to check.
     * @return true if valid.
     */
    private boolean isValidRow(List<Value> row) {
        return isValidRow(schema, row);
    }

    /**
     * Finds a rows index given its primary keys.
     * 
     * @param keys the primary keys of the row.
     * @return its index, or -1 if it isn't in the table.
     */
    private int rowIndex(List<Value> keys) {
        Main.validateParameters(keys);

        // First check if its a valid list of keys for this table.
        if (!isValidRow(getKeyFields(), keys))
            throw new InvalidOperation("Invalid keys.");

        // Check all rows to see if they match.
        List<Value> row;
        for (int i = 0; i < rows.size(); i++) {
            row = rows.get(i);

            if (getRowKeyFieldValues(row).equals(keys))
                return i;
        }

        return -1;
    }

    /**
     * Finds the row index for the given primary keys.
     * 
     * @param keys the primary keys of the row.
     * @return its index, or -1 if it isn't in the table.
     */
    private int rowIndex(Value... keys) {
        Main.validateParameters((Object[]) keys);

        return rowIndex(Arrays.asList(keys));
    }

    /**
     * Gets a list of primary key field indices for a list of fields.
     * 
     * @param fields the list of fields.
     * @return a list of fields only containing the primary key ones.
     */
    public static List<Integer> getKeyFieldIndices(List<Field> fields) {
        List<Integer> keyFieldIndices = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++)
            if (fields.get(i).isKey())
                keyFieldIndices.add(i);

        return Collections.unmodifiableList(keyFieldIndices);
    }

    /**
     * Returns the row but only containing the values that match the given indices.
     * 
     * @param fieldIndices the indices of the fields to get out of the row.
     * @param row the row.
     * @return a list of values just of the indices given.
     */
    public static List<Value> getRowFieldValues(List<Integer> fieldIndices, List<Value> row) {
        Main.validateParameters(fieldIndices, row);

        List<Value> keyFieldValues = new ArrayList<>();
        for (int i : fieldIndices)
            keyFieldValues.add(row.get(i));

        return Collections.unmodifiableList(keyFieldValues);
    }

    /**
     * Represents a single row. A row is a <code>List<Value></code>. A row can have values set, but
     * not added or removed. A set value must be valid for its field header in this table.
     * 
     * @author Henry J. Wylde
     */
    private class MyRow extends AbstractList<Value> {

        private Value[] data;

        /**
         * Constructs a new row with the given list of values.
         * 
         * @param values the list of values.
         */
        public MyRow(List<Value> values) {
            Main.validateParameters(values);

            data = values.toArray(new Value[1]).clone();
        }

        /*
         * @see java.util.AbstractList#add(int, java.lang.Object)
         */
        @Override
        public void add(int index, Value v) {
            throw new InvalidOperation();
        }

        /*
         * @see java.util.AbstractList#get(int)
         */
        @Override
        public Value get(int index) {
            if (!inBounds(index))
                throw new InvalidOperation("Invalid index.");

            return data[index];
        }

        /*
         * @see java.util.AbstractList#remove(int)
         */
        @Override
        public Value remove(int index) {
            throw new InvalidOperation();
        }

        /*
         * @see java.util.AbstractList#set(int, java.lang.Object)
         */
        @Override
        public Value set(int index, Value v) {
            if (!inBounds(index))
                throw new InvalidOperation("Invalid index.");
            if (!isValidFieldValue(schema.get(index), v))
                throw new InvalidOperation("Invalid value.");
            if (schema.get(index).isKey())
                throw new InvalidOperation();

            Value old = data[index];
            data[index] = v;

            return old;
        }

        /*
         * @see java.util.AbstractCollection#size()
         */
        @Override
        public int size() {
            return data.length;
        }

        /**
         * Checks whether the index is within the bounds of this array.
         * 
         * @param index the index to check.
         * @return true if it's in bounds.
         */
        private boolean inBounds(int index) {
            return (index >= 0) && (index < size());
        }
    }

    /**
     * Represents a list of rows. A list of rows is a <code>List<List<Value>></code>.
     * 
     * @author Henry J. Wylde
     */
    private class RowList extends AbstractList<List<Value>> {

        private List<List<Value>> data;

        /**
         * Creates a new empty row list.
         */
        public RowList() {
            data = new ArrayList<>();
        }

        /*
         * @see java.util.AbstractList#add(int, java.lang.Object)
         */
        @Override
        public void add(int index, List<Value> row) {
            if (!inBounds(index) && (index != size()))
                throw new InvalidOperation("Invalid index.");
            if (!isValidRow(row))
                throw new InvalidOperation("Invalid row.");
            if (containsRow(getRowKeyFieldValues(row)))
                throw new InvalidOperation("Duplicate row.");

            data.add(index, new MyRow(row));
        }

        /*
         * @see java.util.AbstractList#get(int)
         */
        @Override
        public List<Value> get(int index) {
            if (!inBounds(index))
                throw new InvalidOperation("Invalid index.");

            return data.get(index);
        }

        /*
         * @see java.util.AbstractList#remove(int)
         */
        @Override
        public List<Value> remove(int index) {
            if (!inBounds(index))
                throw new InvalidOperation("Invalid index.");

            List<Value> old = data.remove(index);

            cascadeDelete(old);

            return old;
        }

        /*
         * @see java.util.AbstractList#set(int, java.lang.Object)
         */
        @Override
        public List<Value> set(int index, List<Value> row) {
            if (!inBounds(index))
                throw new InvalidOperation("Invalid index.");
            if (!isValidRow(row))
                throw new InvalidOperation("Invalid row.");
            // It's a duplicate row if the table already contains the row and the contained row is
            // not the
            // one we are setting.
            if (containsRow(getRowKeyFieldValues(row)))
                if (!getRowKeyFieldValues(row).equals(getRowKeyFieldValues(data.get(index))))
                    throw new InvalidOperation("Duplicate row.");

            List<Value> old = data.set(index, new MyRow(row));

            cascadeUpdate(old, data.get(index));

            return old;
        }

        /*
         * @see java.util.AbstractCollection#size()
         */
        @Override
        public int size() {
            return data.size();
        }

        /**
         * Checks whether the index is within the bounds of this array.
         * 
         * @param index the index to check.
         * @return true if it's in bounds.
         */
        private boolean inBounds(int index) {
            return (index >= 0) && (index < data.size());
        }
    }
}
