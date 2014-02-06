package com.hjwylde.uni.comp103.assignment07.organisationChart;

/*
 * Code for Assignment 7, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Represents an employee in a company
 */
public class Employee {
    
    // Fields
    private final String name;
    private String initials;
    
    /*
     * Constructors
     */
    
    /**
     * Construct a new Employee object with a given name Work out initials by
     * taking the first letter
     * of each name if there are two names (or of the first two names if more than
     * two), or the first
     * two letters if just one name given - as long as it has at least two
     * letters, etc.
     */
    public Employee(String n) {
        name = n.trim();
        
        setInitials();
    }
    
    // Note: No longer need a testing employee.
    /*
     * /** For testing - give random initials, so the used doesn't have to enter a
     * name
     */
    /*
     * public Employee() { name = "" + ((char) (rand.nextInt(26)) + 'A') + ((char)
     * (rand.nextInt(26))
     * + 'A'); initials = name; }
     */
    
    /*
     * Getters
     */
    
    public String getInitials() {
        return initials;
    }
    
    public String getName() {
        return name;
    }
    
    /*
     * Setters
     */
    
    /**
     * Returns true if this employee is not a test employee
     */
    public boolean isValid() {
        // If the initials are not 2 numbers (ie. testing numbers)...
        if (!Character.isDigit(initials.charAt(0))
            || !Character.isDigit(initials.charAt(1)))
            return true; // ...then employee is a proper employee.
            
        return false; // ...else employee is a test employee.
    }
    
    /**
     * Return the name of the person
     */
    @Override
    public String toString() {
        return name;
    }
    
    private void setInitials() {
        if (name != null)
            if (name.length() == 0)
                initials = "--";
            else if (name.length() == 1)
                initials = name + '?';
            else {
                int sp = name.lastIndexOf(" ");
                
                if (sp < 0)
                    initials = name.substring(0, 2);
                else
                    initials = "" + name.charAt(0) + name.charAt(sp + 1);
            }
    }
}
