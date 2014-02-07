package com.hjwylde.uni.swen221.lab05;

/*
 * Code for Laboratory 5, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Identifies a row in the data.
 * 
 * You must modify this class!
 * 
 * @author ncameron
 */
public class Identifier implements Comparable<Identifier> {

    private String name;
    private String dept;

    /**
     * Creates a new identifier with the given name and department.
     * 
     * @param name the identifiers name.
     * @param dept the identifiers department.
     */
    public Identifier(String name, String dept) {
        if ((name == null) || (dept == null))
            throw new IllegalArgumentException();

        this.name = name;
        this.dept = dept;
    }

    /*
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Identifier i) {
        // Order by department first, then name.

        int res = dept.compareTo(i.dept);

        return (res != 0 ? res : name.compareTo(i.name));
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if ((obj == null) || !(obj instanceof Identifier))
            return false;

        Identifier other = (Identifier) obj;
        return (dept.equals(other.dept) && name.equals(other.name));
    }

    /**
     * Gets the identifiers department.
     * 
     * @return the department.
     */
    public String getDept() {
        return dept;
    }

    /**
     * Gets the identifiers name.
     * 
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;

        int result = 1;
        result = (prime * result) + dept.hashCode();
        result = (prime * result) + name.hashCode();

        return result;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{name: \"" + name + "\", dept: \"" + dept + "\"}";
    }
}
