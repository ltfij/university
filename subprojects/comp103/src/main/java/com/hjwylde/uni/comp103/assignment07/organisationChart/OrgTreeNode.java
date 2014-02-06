package com.hjwylde.uni.comp103.assignment07.organisationChart;

/*
 * Code for Assignment 7, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.util.HashSet;
import java.util.Set;

/**
 * OrgTreeNode represents a node in an organisational chart
 */

public class OrgTreeNode {
    
    // Structure
    private OrgTreeNode parent;
    private Set<OrgTreeNode> children = new HashSet<>();
    
    // Fields
    private Employee employee;
    private Job job;
    
    private Location location; // location of the center of the node on the screen
    
    /*
     * Constructors
     */
    
    public OrgTreeNode() {}
    
    /*
     * Getters
     */
    
    public void addChild(OrgTreeNode child) {
        children.add(child);
    }
    
    public Set<OrgTreeNode> getChildren() {
        return children;
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public Job getJob() {
        return job;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public OrgTreeNode getParent() {
        return parent;
    }
    
    /*
     * Setters
     */
    
    public void removeChild(OrgTreeNode child) {
        children.remove(child);
    }
    
    public void setChildren(Set<OrgTreeNode> children) {
        this.children = children;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public void setJob(Job job) {
        this.job = job;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    /*
     * Operations
     */
    
    public void setParent(OrgTreeNode parent) {
        this.parent = parent;
    }
    
    public int size() {
        int size = 1;
        
        for (OrgTreeNode subNode : children)
            size += subNode.size();
        
        return size;
    }
}
