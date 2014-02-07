package com.hjwylde.uni.swen221.assignment06.com.bytebach.model;

import java.util.Arrays;

/*
 * Code for Assignment 6, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A Reference is a special kind of row entry which refers to an entry in another table. This is
 * useful for ensuring consistency between tables. For example, if we have an order for a given
 * customer, then that customer must exist! Furthermore, we want cascading delete semantics; that
 * is, when a table entry X is removed, then all entries which contain references to X are also
 * removed (since otherwise they are effectively dangling pointers).
 * 
 * @author djp
 */
public class ReferenceValue implements Value {

    private final String table;
    private final Value[] keys;

    /**
     * Create a reference to a table entry. This is specified as a table name, and a list of keys
     * for that table. Obviously, the number and type of keys must match the table field
     * declarations.
     * 
     * @param table the table name.
     * @param keys as objects, which are automatically converted to Values.
     */
    public ReferenceValue(String table, Object... keys) {
        this.table = table;
        this.keys = new Value[keys.length];
        for (int i = 0; i != keys.length; ++i) {
            Object k = keys[i];
            if (k instanceof Integer)
                this.keys[i] = new IntegerValue((Integer) k);
            else if (k instanceof Boolean)
                this.keys[i] = new BooleanValue((Boolean) k);
            else if (k instanceof String)
                this.keys[i] = new StringValue((String) k);
            else if (k instanceof ReferenceValue)
                this.keys[i] = (ReferenceValue) k;
            else
                throw new IllegalArgumentException("Invalid key parameters: " + k);
        }
    }

    /**
     * Create a reference to a table entry. This is specified as a table name, and a list of keys
     * for that table. Obviously, the number and type of keys must match the table field
     * declarations.
     * 
     * @param table the table name.
     * @param keys the keys.
     */
    public ReferenceValue(String table, Value... keys) {
        this.table = table;
        this.keys = keys;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ReferenceValue) {
            ReferenceValue r = (ReferenceValue) o;
            return r.table.equals(table) && Arrays.equals(keys, r.keys);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return table.hashCode() + Arrays.hashCode(keys);
    }

    /**
     * Get the keys associated with this reference.
     */
    public Value[] keys() {
        return keys;
    }

    /**
     * Get the table name.
     */
    public String table() {
        return table;
    }

    @Override
    public String toString() {
        String r = "[" + table;
        for (Value k : keys)
            r += ":" + k;
        return r + "]";
    }

    public static Object getRawValueObject(Value v) {
        if (v instanceof IntegerValue)
            return (((IntegerValue) v).value());
        else if (v instanceof BooleanValue)
            return (((BooleanValue) v).value());
        else if (v instanceof StringValue)
            return (((StringValue) v).value());
        else if (v instanceof ReferenceValue)
            return (v);

        throw new InvalidOperation();
    }
}
