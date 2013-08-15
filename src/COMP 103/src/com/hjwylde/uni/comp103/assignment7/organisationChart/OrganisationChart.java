package com.hjwylde.uni.comp103.assignment7.organisationChart;

/*
 * Code for Assignment 7, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;

import javax.swing.*;

import com.hjwylde.uni.comp103.util.DrawingCanvas;

/**
 * <description of class OrganisationChart>
 */

public class OrganisationChart implements ActionListener, MouseListener {
    
    // GUI
    private final JFrame frame = new JFrame("OrganisationChart");
    
    private final DrawingCanvas canvas = new DrawingCanvas();
    private final JTextArea textArea = new JTextArea(10, 30);
    // Button names
    private final String[][] labels = {
        {
            "New", "Load", "Save", "Quit"
        }, {
            "Add", "Remove", "Move"
        }, {
            "Employ", "Fire", "Promote", "Reassign"
        }, {
            "All Below", "All Above"
        }, {
            "Set Job", "Remove Job"
        }
    };
    
    private final int windowWidth = 1000;
    
    // Fields
    private OrgTreeNode root = new OrgTreeNode(); // Root node
    
    private String currentAction = "";
    private OrgTreeNode pressedNode; // The node that the user pressed the mouse
    // on (if any)
    private OrgTreeNode releasedNode; // The node that the user released the mouse
                                      // on (if any)
    private final int nodeRad = 15;
    
    private final int nodeDi = nodeRad * 2;
    private final int levelSep = nodeRad;
    
    // Save / Load
    public static final javax.swing.filechooser.FileFilter CSV_FILTER = new javax.swing.filechooser.FileFilter() {
        
        @Override
        public boolean accept(File f) {
            int ext = f.getName().lastIndexOf(".");
            
            if (ext == -1)
                return f.isDirectory();
            
            return (f.getName().substring(ext).equalsIgnoreCase(".csv"));
        }
        
        @Override
        public String getDescription() {
            return "Csv Files";
        }
    };
    
    /**
     * Construct a new OrganisationChart object and set up the GUI
     */
    public OrganisationChart() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth, 800);
        
        // The graphics area
        canvas.addMouseListener(this);
        frame.getContentPane()
            .add(new JScrollPane(canvas), BorderLayout.CENTER);
        
        // The text area
        frame.getContentPane().add(new JScrollPane(textArea),
            BorderLayout.SOUTH);
        
        // The buttons
        JPanel buttonPanel = new JPanel(new java.awt.GridLayout(2, 0)); // we need
                                                                        // two rows
                                                                        // of
                                                                        // buttons
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        
        addButtons(buttonPanel, "General:", labels[0]);
        addButtons(buttonPanel, "Roles:", labels[1]);
        addButtons(buttonPanel, "Employees:", labels[2]);
        addButtons(buttonPanel, "Reports:", labels[3]);
        addButtons(buttonPanel, "Jobs:", labels[4]);
        
        frame.setVisible(true);
        redrawChart();
    }
    
    /*
     * Constructors
     */
    
    /**
     * Respond to button presses
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Should use the labels array!
        String action = e.getActionCommand();
        if (action.equals("New"))
            root = new OrgTreeNode();
        else if (action.equals("Quit"))
            frame.dispose();
        else if (action.equals("Load"))
            doLoad();
        else if (action.equals("Save"))
            doSave();
        else
            currentAction = action;
        
        redrawChart();
    }
    
    /*
     * GUI Methods
     */
    
    @Override
    public void mouseClicked(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    /**
     * Respond to mouse events
     */
    @Override
    public void mousePressed(MouseEvent e) {
        pressedNode = findNodeAt(new Location(e.getX(), e.getY()));
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        releasedNode = findNodeAt(new Location(e.getX(), e.getY()));
        performCurrentAction();
        redrawChart();
    }
    
    /**
     * Perform the current action (stored in currentAction) operates on nodes in
     * pressedNode and
     * releasedNode
     */
    public void performCurrentAction() {
        // Should use the labels array!
        // Role buttons
        if (currentAction.equals("Add"))
            addNode(releasedNode);
        else if (currentAction.equals("Move"))
            moveNode(pressedNode, releasedNode);
        else if (currentAction.equals("Remove"))
            OrganisationChart.removeNode(releasedNode);
        // Employee buttons
        else if (currentAction.equals("Employ"))
            OrganisationChart.employ(releasedNode);
        else if (currentAction.equals("Fire"))
            OrganisationChart.fire(releasedNode);
        else if (currentAction.equals("Promote"))
            OrganisationChart.promote(releasedNode);
        else if (currentAction.equals("Reassign"))
            OrganisationChart.reassign(pressedNode, releasedNode);
        // Report buttons
        else if (currentAction.equals("All Below")) {
            textArea.setText("Subtree:\n");
            listSubtree(releasedNode);
        } else if (currentAction.equals("All Above")) {
            textArea.setText("Command chain:\n");
            listChain(releasedNode);
        } else if (currentAction.equals("Set Job"))
            OrganisationChart.setJob(releasedNode);
        else if (currentAction.equals("Remove Job"))
            OrganisationChart.removeJob(releasedNode);
    }
    
    /**
     * Add a group of buttons and its group label
     */
    private void addButtons(JPanel panel, String group, String... names) {
        panel.add(new JLabel(group));
        for (String name : names) {
            JButton button = new JButton(name);
            button.addActionListener(this);
            panel.add(button);
        }
    }
    
    /**
     * Add a new node under the node. Do nothing if the node is null.
     */
    private void addNode(OrgTreeNode node) {
        if (node == null)
            return;
        
        OrgTreeNode newNode = new OrgTreeNode();
        
        newNode.setParent(node);
        node.addChild(newNode);
        
        redrawChart();
    }
    
    private void calculateLocations() {
        root.setLocation(new Location(windowWidth / 2, levelSep + nodeRad));
        calculateLocations(root, getNodeWidth(root));
    }
    
    private void calculateLocations(OrgTreeNode node, int nodeWidth) {
        int x = node.getLocation().getX() - (nodeWidth * nodeDi); // To make it
        // clearer:
        // loc.getX() -
        // (nodeWidth/2) *
        // (nodeDi*2), the
        // initial x is half
        // the width of the
        // parent node to
        // the left.
        int y = node.getLocation().getY() + nodeDi + levelSep; // The y is seperated
                                                               // by levelSep.
        
        for (OrgTreeNode child : node.getChildren()) {
            int childWidth = getNodeWidth(child);
            child.setLocation(new Location(x + (childWidth * nodeDi), y));
            
            calculateLocations(child, childWidth);
            
            x += childWidth * nodeDi * 2;
        }
    }
    
    /**
     * (challenge) Construct a new tree loaded from a file that was saved by
     * doSave.
     */
    private void doLoad() {
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(OrganisationChart.CSV_FILTER);
        
        String filePath;
        if (fc.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION)
            filePath = fc.getSelectedFile().getAbsolutePath();
        else
            return;
        
        if (!new File(filePath).exists()) // If user didn't select a valid file...
            return; // ...then exit.
            
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringTokenizer line;
            String t;
            
            OrgTreeNode node;
            Map<String, OrgTreeNode> nodeReferences = new HashMap<>(); // A map with references to
                                                                       // the
                                                                       // nodes for each nodeID.
            
            while ((t = br.readLine()) != null) {
                line = new StringTokenizer(t, ",");
                
                node = new OrgTreeNode();
                
                try {
                    String id = line.nextToken();
                    String employee = line.nextToken();
                    if (!employee.equals("~"))
                        node.setEmployee(new Employee(employee));
                    else
                        node.setEmployee(null);
                    String job = line.nextToken();
                    if (!job.equals("~"))
                        node.setJob(new Job(job));
                    else
                        node.setJob(null);
                    
                    nodeReferences.put(id, node); // Add the newly created node to the
                                                  // map.
                    
                    if (line.hasMoreTokens()) { // If this node is not the root...
                        String parent = line.nextToken(); // ...then get parentID.
                        
                        node.setParent(nodeReferences.get(parent)); // Set the parent to the
                                                                    // reference stored in
                                                                    // the map.
                        nodeReferences.get(parent).getChildren().add(node); // Add this node
                                                                            // as a child to
                                                                            // the
                                                                            // parent.
                    } else
                        root = node;
                } catch (NoSuchElementException e) {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
     * Tree Methods (Nodes)
     */
    
    /**
     * (challenge) Save the whole tree in a file in a format that it can be loaded
     * back in and
     * reconstructed
     */
    private void doSave() {
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(OrganisationChart.CSV_FILTER); // Save in a CSV file.
        
        String filePath;
        if (fc.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION)
            filePath = fc.getSelectedFile().getAbsolutePath();
        else
            return; // User cancelled.
            
        if (!filePath.endsWith(".csv")) // Check whether user only typed in a name,
                                        // or included the
                                        // .csv.
            filePath += ".csv";
        File saveFile = new File(filePath);
        
        if (saveFile.exists()) { // If the file already exists...
            int choice = JOptionPane.showConfirmDialog(null,
                "Error with saving file: " + saveFile.getPath()
                    + ".\nFile already exists.\nOverwrite?",
                "File Exists Error", JOptionPane.YES_NO_OPTION); // ...then query the user for
                                                                 // overwriting.
            if (choice != JOptionPane.YES_OPTION)
                return; // User declined overwriting file.
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile))) {
            /*
             * CSV File Format:
             * 
             * nodeID, nodeEmployeeName, nodeJobTitle, parentID
             * 
             * parentID is used to recreate parent links.
             */
            
            Stack<OrgTreeNode> nodes = new Stack<>();
            nodes.add(root);
            
            OrgTreeNode node;
            while (!nodes.isEmpty()) {
                node = nodes.pop();
                
                bw.append(node.toString() + ",");
                if (node.getEmployee() != null)
                    bw.append(node.getEmployee().getName() + ",");
                else
                    bw.append("~,"); // Position holder character, so the stringtokenizer
                                     // on reading the file
                                     // doesn't skip over this token.
                if (node.getJob() != null)
                    bw.append(node.getJob().getTitle() + ",");
                else
                    bw.append("~,"); // Position holder character, so the stringtokenizer
                                     // on reading the file
                                     // doesn't skip over this token.
                if (node.getParent() != null)
                    bw.append(node.getParent().toString());
                bw.newLine();
                
                for (OrgTreeNode child : node.getChildren())
                    nodes.push(child);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Finds the node in the tree under the given location. Traverses the tree,
     * looking at each node.
     * If the distance between location and the position of the node is less than
     * two times the node
     * radius, then return the node. If no node, then return null
     */
    private OrgTreeNode findNodeAt(Location loc) {
        return findNodeAt(loc, root);
    }
    
    private OrgTreeNode findNodeAt(Location loc, OrgTreeNode nd) {
        if (loc.distance(nd.getLocation()) <= nodeRad) // If loc is within nd's
                                                       // radius...
            return nd; // ...then node found!
            
        OrgTreeNode foundNode = null;
        for (OrgTreeNode child : nd.getChildren()) {
            foundNode = findNodeAt(loc, child); // Try find the node in each child
            
            if (foundNode != null) // If we found the node in a child...
                break; // ...then break out of loop, no need to check any more.
        }
        
        return foundNode; // Returns null if no node was found.
    }
    
    /**
     * A node's width is an integer representing the max number of child nodes
     * spanning across the
     * x-axis.
     */
    private int getNodeWidth(OrgTreeNode node) {
        int nodeWidth = 0;
        
        Set<OrgTreeNode> children = node.getChildren();
        if (children.size() > 0) // If the node has a child...
            for (OrgTreeNode child : node.getChildren())
                // ...then this node's width is the sum of the children's node widths.
                nodeWidth += getNodeWidth(child);
        else
            nodeWidth = 1; // ...else this node width is 1.
            
        return nodeWidth;
    }
    
    /**
     * (Extension) Return true if the node is in the subtree, and false otherwise
     * Uses == to determine
     * node equality since we want to know if this exact node is in the subtree,
     * not just if a node
     * containing the same employee is in the subtree. Only needed for extension
     * version of moveNode
     */
    private boolean inSubtree(OrgTreeNode node, OrgTreeNode subtree) {
        if ((node == null) || (subtree == null))
            return false;
        if (node == subtree)
            return true; // Found! Node is in subtree.
            
        boolean found = false;
        for (OrgTreeNode child : subtree.getChildren()) { // Check all subtrees.
            found = inSubtree(node, child);
            
            if (found == true) // If found is true...
                break; // ...then don't check any more subtrees.
        }
        
        return found; // Returns false if node not found in any subtrees.
    }
    
    /**
     * List (in the text area) the names of all the employees in the reporting
     * chain from this node to
     * the root of the tree.
     */
    private void listChain(OrgTreeNode node) {
        String name;
        if (node.getEmployee() != null)
            name = node.getEmployee().getName();
        else
            name = "--";
        
        textArea.append(name);
        if (node.getParent() != null) {
            textArea.append(" -> ");
            listChain(node.getParent());
        }
    }
    
    /*
     * Tree Methods (Employees)
     */
    
    /**
     * List (in the text area) the names of all the employees under the manager in
     * this node
     * (including the manager themselves).
     */
    private void listSubtree(OrgTreeNode node) {
        // Root node:
        String name;
        if (node.getEmployee() != null)
            name = node.getEmployee().getName();
        else
            name = "--";
        textArea.append(name + " employs:\n ");
        
        for (OrgTreeNode child : node.getChildren())
            listSubtree(child, 1); // List the subtree for each child, they are the
                                   // first level below the
                                   // root.
    }
    
    private void listSubtree(OrgTreeNode node, int level) {
        String name;
        if (node.getEmployee() != null)
            name = node.getEmployee().getName();
        else
            name = "--";
        for (int i = level; i > 0; i--)
            textArea.append("--"); // Tabs out the different levels in the tree to
                                   // easily see who employes
                                   // who.
        textArea.append(" " + name + "\n ");
        
        for (OrgTreeNode child : node.getChildren())
            listSubtree(child, level++);
    }
    
    /**
     * Move node to be a subordinate of the "to" node, along with the subtree
     * under node. Note that
     * this is a problem if the "to" node is in the subtree rooted at node
     * (including if the "to" node
     * is the same as node) Testing for this situation is an extension.
     */
    private void moveNode(OrgTreeNode node, OrgTreeNode to) {
        if ((node == null) || (to == null))
            return;
        if (inSubtree(to, node)) // Can't move a node to be within itself!
            return;
        
        node.getParent().removeChild(node);
        node.setParent(to);
        to.addChild(node);
    }
    
    /**
     * Redraw the chart. First step is to calculate all the locations of the nodes
     * in the tree Then
     * traverse the tree to draw all the nodes and lines between parents and
     * children
     */
    private void redrawChart() {
        calculateLocations();
        
        canvas.clear(false); // To stop flickering.
        redrawNodes(root);
        canvas.display();
    }
    
    private void redrawNode(OrgTreeNode nd) {
        int x = nd.getLocation().getX();
        int y = nd.getLocation().getY();
        canvas.setColor(Color.WHITE);
        canvas.fillOval(x - nodeRad, y - nodeRad, (nodeRad * 2) - 1,
            (nodeRad * 2) - 1);
        canvas.setColor(Color.BLACK);
        canvas.drawOval(x - nodeRad, y - nodeRad, (nodeRad * 2) - 1,
            (nodeRad * 2) - 1);
        
        Employee emp = nd.getEmployee();
        if (emp != null)
            canvas.drawString(emp.getInitials(), x - 8, y + 5);
        else
            canvas.drawString("--", x - 6, y + 5);
        
        canvas.setColor(Color.RED);
        Job job = nd.getJob();
        FontMetrics fm = canvas.getFontMetrics(canvas.getFont());
        if (job != null)
            canvas.drawString(job.getTitle(),
                x - (fm.stringWidth(job.getTitle()) / 2), y - (nodeRad + 6));
    }
    
    /*
     * Tree Methods (Reports)
     */
    
    /**
     * Recursive method to draw all nodes in a subtree. The provided code just
     * draws the root node;
     * you need to make it draw all the nodes
     */
    private void redrawNodes(OrgTreeNode nd) {
        for (OrgTreeNode child : nd.getChildren()) {
            canvas.setColor(Color.BLACK);
            canvas.drawLine(nd.getLocation().getX(), nd.getLocation().getY(),
                child.getLocation().getX(), child.getLocation().getY());
            redrawNodes(child); // Draw child nodes and lines before drawing this
                                // node.
        }
        
        redrawNode(nd);
    }
    
    /*
     * Main
     */
    public static void main(String[] arguments) {
        new OrganisationChart();
    }
    
    /** Ask the user for a new employee and assign them to this node */
    private static boolean employ(OrgTreeNode node) {
        String employeeName = JOptionPane.showInputDialog(null,
            "Enter the employee's name (No coma's):", "Set Employee");
        if (employeeName != null) {
            if (!employeeName.equals("") && !employeeName.contains(",")) // I don't
                                                                         // allow
                                                                         // coma's
                                                                         // just to
                                                                         // make
                                                                         // saving /
                                                                         // loading
                                                                         // easy.
                return OrganisationChart.employ(node,
                    new Employee(employeeName));
            
            JOptionPane.showMessageDialog(null,
                "Error: Illegal employee name entered.",
                "Error: Employee Name", JOptionPane.ERROR_MESSAGE);
        }
        
        return false;
    }
    
    /*
     * Tree Methods (File I/O)
     */
    
    // I've overloaded this method, allows for easy moving employees from node to
    // node.
    // Returns true if the employee was successfully moved to node.
    private static boolean employ(OrgTreeNode node, Employee employee) {
        if ((node == null) || (employee == null))
            return false;
        
        Employee nodeEmployee = node.getEmployee();
        if (nodeEmployee != null)
            if (nodeEmployee.isValid() || !employee.isValid()) // If the node already
                                                               // has an employee, or
                                                               // the new employee
                                                               // isn't valid (is a
                                                               // test)...
                return false; // ...then return false.
                
        node.setEmployee(employee);
        return true;
    }
    
    /** Remove the employee from this node (leaving null) */
    private static void fire(OrgTreeNode node) {
        node.setEmployee(null);
    }
    
    /*
     * Tree Methods (Job)
     */
    
    /**
     * Move the employee in this node up to the immediate manager postion, as long
     * as there is
     * currently no employee in that position.
     */
    private static void promote(OrgTreeNode node) {
        OrganisationChart.reassign(node, node.getParent()); // Promoting is a special case of
        // re-assigning.
    }
    
    /**
     * Move the employee in "node" to the "to" position in the tree, as long as
     * there is currently no
     * employee in that position.
     */
    private static void reassign(OrgTreeNode node, OrgTreeNode to) {
        if (node == null)
            return;
        
        if (OrganisationChart.employ(to, node.getEmployee())) // If the employee was moved
            // successfully...
            OrganisationChart.fire(node); // ...then remove employee from the original node.
    }
    
    /** Remove the employee from this node (leaving null) */
    private static void removeJob(OrgTreeNode node) {
        node.setJob(null);
    }
    
    /*
     * Helper Methods
     */
    
    /**
     * Remove node and move all its children to be children of its parent Do
     * nothing if the node is
     * the root (or if node is null).
     */
    private static void removeNode(OrgTreeNode node) {
        if (node == null)
            return;
        
        if (node.getParent() != null) { // If node is not the root...
            OrgTreeNode parent = node.getParent();
            
            for (OrgTreeNode child : node.getChildren()) { // For each child, move it
                                                           // up to parent node.
                child.setParent(parent);
                parent.addChild(child);
            }
            
            node.getParent().removeChild(node);
        }
    }
    
    private static void setJob(OrgTreeNode node) {
        String jobTitle = JOptionPane.showInputDialog(null,
            "Enter the job's title (No coma's):", "Set Job"); // To allow for easy
                                                              // save / loading.
        if (jobTitle != null)
            if (!jobTitle.equals("") && !jobTitle.contains(","))
                OrganisationChart.setJob(node, new Job(jobTitle));
            else
                JOptionPane.showConfirmDialog(null,
                    "Error: Illegal job title entered.", "Error: Job Title",
                    JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
    }
    
    private static void setJob(OrgTreeNode node, Job job) {
        if ((node == null) || (job == null))
            return;
        
        if (node.getJob() == null)
            node.setJob(job);
    }
}
