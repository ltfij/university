package com.hjwylde.uni.comp261.assignment06.trees;

import java.util.*;

/*
 * Code for Assignment 6, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class BTree implements Iterable<BTree.Record> {
    
    /**
     * Order = 2m + 1.
     * The order of the BTree is the maximum number of children it may have.
     */
    private final int order;
    
    private Node root;
    
    private int size = 0;
    
    /**
     * Creates a new BTree with the specified <code>nodeSize</code>. The <code>nodeSize</code> is
     * the
     * maximum number of records a node may contain. Must be an even number and greater than 0.
     * 
     * @param nodeSize the node size.
     */
    public BTree(int nodeSize) {
        if (((nodeSize % 2) == 1) || (nodeSize <= 0))
            throw new IllegalArgumentException();
        
        order = nodeSize + 1;
    }
    
    /**
     * Deletes a record with the given <code>id</code> from this BTree if it exists. Returns true if
     * the record is deleted.
     * 
     * @param id the id of the record to delete.
     * @return true if deleted.
     */
    public boolean delete(int id) {
        if (isEmpty())
            return false;
        
        Node n = findNode(id); // Find the node the record should be in.
        if (n.getRecord(id) == null) // If the node doesn't contain the record...
            return false; // ...return false.
            
        Node root = n.delete(id); // Delete the record and get the new root if the tree contracted.
        if (root != null) // If there is a new root...
            this.root = root; // ...update the root.
            
        size--;
        
        if (isEmpty())
            this.root = null;
        
        return true;
    }
    
    /**
     * Find the record with the given <code>id</code> and return its value as a
     * <code>CharSequence</code>.
     * 
     * @param id the record id.
     * @return the records value.
     */
    public CharSequence find(int id) {
        if (isEmpty())
            return null;
        
        Record r = findNode(id).getRecord(id); // Find the node the record should be in, then get
                                               // the
                                               // record.
        
        return (r == null ? null : r.data); // If the record was found, return its data.
    }
    
    /**
     * Create a new record of the given <code>id</code> and <code>data</code> then insert it into
     * the
     * tree if it doesn't already exist.
     * 
     * @param id the record id.
     * @param data the record data.
     * @return true if inserted.
     */
    public boolean insert(int id, CharSequence data) {
        if (isEmpty())
            root = new Node();
        
        Node n = findNode(id); // Find the node the record should be in.
        if (n.getRecord(id) != null) // If a record with the id already exists...
            return false; // ...return false.
            
        Node root = n.insert(new Record(id, data)); // Insert the new record, getting the new root
                                                    // if
                                                    // the tree expanded.
        if (root != null) // If there is a new root...
            this.root = root; // ...update the root.
            
        size++;
        
        return true;
    }
    
    /**
     * Returns true if this tree has no records in it.
     * 
     * @return true if empty.
     */
    public boolean isEmpty() {
        return (size() == 0);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Record> iterator() {
        return new Iterator<Record>() {
            
            Queue<Record> queue = initQueue(root);
            
            @Override
            public boolean hasNext() {
                return !queue.isEmpty();
            }
            
            @Override
            public Record next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                
                return queue.poll();
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
            
            private Queue<Record> initQueue(Node n) {
                Queue<Record> queue = new LinkedList<>();
                
                for (int i = 0; i < (n.size + 1); i++) {
                    if (!n.isLeaf())
                        queue.addAll(initQueue(n.children[i]));
                    
                    if (i < n.size)
                        queue.add(n.records[i]);
                }
                
                return queue;
            }
        };
    }
    
    /**
     * Prints out this BTree to the console in an indented display.
     */
    public void print() {
        System.out.println(isEmpty() ? "EMPTY" : root.print());
    }
    
    /**
     * Returns the number of records in this tree.
     * 
     * @return the size of the tree.
     */
    public int size() {
        return (root == null ? 0 : size);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        
        Iterator<Record> it = iterator();
        
        if (it.hasNext())
            sb.append(it.next());
        
        while (it.hasNext())
            sb.append(", ").append(it.next());
        
        return sb.append("}").toString();
    }
    
    /**
     * Finds the node that the given record id should be in.
     * 
     * @param id the record id.
     * @return the node that the record should be in.
     */
    private Node findNode(int id) {
        assert (!isEmpty());
        
        Node n = root;
        while (!n.isLeaf()) { // While we aren't at the bottom level of the tree...
            int pos = n.getRecordPosition(id); // Find the position that the record should be in.
            
            if ((pos == n.size) || (id < n.records[pos].id)) // If the record belongs below pos...
                n = n.children[pos]; // ...move down a level.
            else if (n.records[pos].id == id) // If the record is at the found position...
                return n; // ...return this node.
            else
                // id > n.records[pos].id.
                // If the record belongs above pos...
                n = n.children[pos + 1]; // ...move down a level.
        } // Reached the bottom tree level.
        
        return n; // Return the final node.
    }
    
    /**
     * A class for a simple record which has an id and some data.
     * 
     * @author Henry J. Wylde
     */
    public class Record implements Comparable<Record> {
        
        private final int id;
        
        private CharSequence data;
        
        /**
         * Creates a new record with the given <code>id</code> and <code>data</code>.
         * 
         * @param id the id.
         * @param data the data.
         */
        public Record(int id, CharSequence data) {
            if (data == null)
                throw new NullPointerException();
            
            this.id = id;
            
            this.data = data;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(Record r) {
            if (r == null)
                return -1;
            
            return id - r.id;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            
            if ((o == null) || !(o instanceof Record))
                return false;
            
            Record r = (Record) o;
            if (id != r.id)
                return false;
            
            return true;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return id;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "(" + id + ", " + data + ")";
        }
    }
    
    /**
     * A BTree node, has a parent, children (max: order) and records (max: order - 1, min: (order -
     * 1)
     * / 2 (if !isRoot())).
     * 
     * @author Henry J. Wylde
     */
    private class Node {
        
        private Node parent;
        private Node[] children;
        
        private Record[] records;
        
        private int size = 0;
        
        /**
         * Creates a new node with no parent.
         */
        public Node() {
            records = new Record[order]; // Size of order so that when insert splitting all the
                                         // records
                                         // may be placed in one array.
        }
        
        /**
         * Deletes the record of the given id from this node.
         * 
         * @param id the record id.
         * @return the new root node, or null if it wasn't changed.
         */
        public Node delete(int id) {
            // assert (getRecord(id) != null);
            
            int pos = getRecordPosition(id); // Get the position of the record.
            
            if (isLeaf()) {
                int childId = records[0].id; // Holder variable so we can find this nodes position
                                             // from
                                             // parent.
                
                shuffle(records, pos + 1, pos); // Move records down one.
                
                size--;
                
                if ((size < (order / 2)) && !isRoot()) // If we have underflowed...
                    return parent.childUnderflow((size == 1 ? records[0].id
                        : childId)); // ...tell the
                                     // parent.
            } else { // !isLeaf()
                // Find the right left-most child.
                Node n = children[pos + 1];
                while (!n.isLeaf())
                    n = n.children[0];
                
                Record r = n.records[0];
                records[pos] = r; // Replace the record to delete with the right left-most record.
                
                return n.delete(r.id); // Delete the duplicate.
            }
            
            return null;
        }
        
        /**
         * Gets the record with the given id from the list of records in this node.
         * 
         * @param id the record id.
         * @return the record if this node contains it.
         */
        public Record getRecord(int id) {
            int pos = getRecordPosition(id); // Get the record position.
            
            if ((pos != size) && (records[pos].id == id)) // If the record at pos is the correct
                                                          // one...
                return records[pos]; // ...return the record.
                
            return null;
        }
        
        /**
         * Gets the record position within this node by using a binary search. Returns the position
         * where the record is or should be.
         * 
         * @param id the record id.
         * @return the position within the range of 0 to size.
         */
        public int getRecordPosition(int id) {
            int low, mid, high;
            low = 0;
            high = size;
            
            while (low < high) {
                mid = (low + high) / 2;
                
                if (id > records[mid].id)
                    low = mid + 1;
                else
                    high = mid;
            }
            
            return low;
        }
        
        /**
         * Insert the record into this node. Should only be called on leaf nodes.
         * 
         * @param r the record to insert.
         * @return the new root node if the tree has been split. Null if the root did not change.
         */
        public Node insert(Record r) {
            // assert (isLeaf());
            
            return insert(r, null); // Insert without a right child to the record.
        }
        
        /**
         * Prints this node and all its children into a tabbed formatted string.
         * 
         * @return a string representation of this node and its children.
         */
        public String print() {
            String str = new String();
            
            for (int i = 0; i < size; i++) {
                str += records[i];
                if (i < (size - 1))
                    str += "\t| ";
            }
            
            if (!isLeaf())
                for (int i = 0; i < (size + 1); i++) {
                    if (children[i] == null)
                        break;
                    
                    str += "\n" + children[i].print();
                }
            
            str = str.replaceAll("\n", "\n\t"); // Tab all the lines an extra one.
            
            return str;
        }
        
        /**
         * Called by a child to a parent. Notifies the parent that the child of the given id has
         * underflowed and must be either redistributed or merged.
         * 
         * @param id the id of the underflowed child.
         * @return the new root node if changed.
         */
        protected Node childUnderflow(int id) {
            // assert (!isLeaf());
            
            int pos = getRecordPosition(id); // Get the child nodes position in the children[]
                                             // array.
            
            if ((pos < size) && (records[pos].id <= id)) // Fix for when we pull up a record and
                                                         // place
                                                         // into the parent, then call child
                                                         // underflow
                                                         // with the same records id.
                pos++;
            
            int neighbourPos = pos - 1; // Use left neighbour by default.
            if ((pos < size)
                && ((neighbourPos == -1) || (children[pos + 1].size > children[neighbourPos].size)))
                neighbourPos = pos + 1; // Use right neighbour if there exists no left neighbour or
                                        // the
                                        // right neighbour has more records to redistribute.
                
            Node child = children[pos];
            Node neighbour = children[neighbourPos];
            if (neighbour.size > (order / 2)) { // Redistribute.
                Record insertRecord;
                Node insertChild = null;
                
                if (neighbourPos == (pos + 1)) { // Using right neighbour.
                    insertRecord = records[pos]; // Record to insert into underflowed child.
                    records[pos] = neighbour.records[0]; // Redistribute least neighbour record up
                                                         // to parent.
                    shuffle(neighbour.records, 1, 0); // Shuffle neighbour records down one.
                    
                    if (!neighbour.isLeaf()) {
                        insertChild = neighbour.children[0]; // Child to insert with the
                                                             // insertRecord to the
                                                             // underflowed child.
                        shuffle(neighbour.children, 1, 0); // Shuffle neighbour children down one.
                    }
                } else { // Using left neighbour.
                    // assert (neighbourPos == pos - 1);
                    
                    insertRecord = records[neighbourPos]; // Record to insert into underflowed
                                                          // child.
                    records[neighbourPos] = neighbour.records[neighbour.size - 1]; // Redistribute
                                                                                   // greatest
                                                                                   // neighbour
                                                                                   // record up to
                                                                                   // parent.
                    neighbour.records[neighbour.size - 1] = null; // Clear greatest neighbour
                                                                  // record.
                    
                    if (!neighbour.isLeaf()) {
                        insertChild = child.children[0]; // Child to insert with the insertRecord to
                                                         // the
                                                         // underflowed child.
                        child.setChild(0, neighbour.children[neighbour.size]); // The insert child
                                                                               // technically
                                                                               // is to be inserted
                                                                               // on the left,
                                                                               // instead the method
                                                                               // only uses a
                                                                               // right child so set
                                                                               // the left
                                                                               // child here.
                        neighbour.setChild(neighbour.size, null); // Clear the greatest neighbour
                                                                  // child.
                    }
                }
                
                neighbour.size--;
                
                return child.insert(insertRecord, insertChild); // Insert the child / record into
                                                                // the
                                                                // underflowed child.
            }
            
            // Merge
            
            Node merge = new Node(); // The merge node with the child and neighbour records, and one
                                     // parent record.
            
            int childId = records[0].id; // Holder for the record id. Needed for 2-3 trees when node
                                         // may
                                         // have 0 records after removing 1.
            
            if (neighbourPos == (pos + 1)) { // Using right neighbour.
                merge.records = Arrays.copyOf(child.records, order); // Add child records to merge
                                                                     // node.
                merge.records[child.size] = records[pos]; // Add the parent record to the merge
                                                          // node.
                System.arraycopy(neighbour.records, 0, merge.records,
                    child.size + 1, neighbour.size); // Add the neighbour records to the merge node.
                
                merge.size = neighbour.size + child.size + 1; // order - 1.
                
                if (!child.isLeaf()) {
                    merge.children = new Node[order + 1];
                    
                    for (int i = 0; i < (child.size + 1); i++)
                        merge.setChild(i, child.children[i]); // Add the child children to the merge
                                                              // node.
                    for (int i = 0; i < (neighbour.size + 1); i++)
                        merge.setChild(child.size + 1 + i,
                            neighbour.children[i]); // Add the neighbour
                                                    // children to the merge
                                                    // node.
                }
                
                setChild(pos, merge); // Set the merge child.
                shuffle(records, neighbourPos, pos); // Move records down one.
                shuffle(children, neighbourPos + 1, neighbourPos); // Move children down one.
                
                size--;
                
                if ((size < (order / 2)) && !isRoot())
                    return parent.childUnderflow((size == 1 ? records[0].id
                        : childId));
                else if ((size == 0) && isRoot()) {
                    children[0].parent = null;
                    
                    return children[0];
                }
            } else { // Using left neighbour.
                // assert (neighbourPos == pos - 1);
                
                merge.records = Arrays.copyOf(neighbour.records, order); // Add neighbour records to
                                                                         // merge
                                                                         // node.
                merge.records[neighbour.size] = records[neighbourPos]; // Add the parent record to
                                                                       // the
                                                                       // merge node.
                System.arraycopy(child.records, 0, merge.records,
                    neighbour.size + 1, child.size); // Add the child records to the merge node.
                
                merge.size = neighbour.size + 1 + child.size; // order - 1.
                
                if (!child.isLeaf()) {
                    merge.children = new Node[order + 1];
                    
                    for (int i = 0; i < (neighbour.size + 1); i++)
                        merge.setChild(i, neighbour.children[i]); // Add the neighbour children to
                                                                  // the merge
                                                                  // node.
                    for (int i = 0; i < (child.size + 1); i++)
                        merge.setChild(neighbour.size + 1 + i,
                            child.children[i]); // Add the child children
                                                // to the merge node.
                }
                
                setChild(neighbourPos, merge); // Set the merge node.
                shuffle(records, pos, neighbourPos); // Move the records down one.
                shuffle(children, pos + 1, pos); // Move the children down one.
                
                size--;
                
                if ((size < (order / 2)) && !isRoot())
                    return parent.childUnderflow((size == 1 ? records[0].id
                        : childId));
                else if ((size == 0) && isRoot()) {
                    children[0].parent = null;
                    
                    return children[0];
                }
            }
            
            return (isRoot() ? this : null);
        }
        
        /**
         * Insert the record into this node, with a right child if this node isn't a leaf node.
         * 
         * @param r the record to insert.
         * @param right the right child of the record.
         * @return the new root node if there is one, or null if it hasn't changed.
         */
        protected Node insert(Record r, Node right) {
            // assert (((right == null) && isLeaf()) || ((right != null) && !isLeaf()));
            
            int pos = getRecordPosition(r.id); // Get the records position
            
            shuffle(records, pos, pos + 1); // Move the records up one.
            records[pos] = r; // Set the record.
            if (!isLeaf()) {
                shuffle(children, pos + 1, pos + 2); // Move the children up one.
                setChild(pos + 1, right); // Insert the right child.
            }
            
            if (size == (order - 1)) { // If overflowed (ie, after inserting size = size + 1)...
                if (isRoot()) { // If we are the root...
                    // ...then we need to create a new root.
                    parent = new Node();
                    parent.children = new Node[order + 1];
                    parent.setChild(0, this);
                }
                
                Record insertRecord = records[size / 2]; // Insert record will be the middle record.
                Node insertChild = new Node(); // The right split child.
                
                System.arraycopy(records, (size / 2) + 1, insertChild.records,
                    0, size / 2); // Copy half the records to the right split child.
                records = Arrays.copyOf(
                    Arrays.copyOfRange(records, 0, size / 2), order); // Set the records to (this
                                                                      // left split child) be only
                                                                      // half.
                
                if (!isLeaf()) {
                    insertChild.children = new Node[order + 1];
                    
                    insertChild.setChildren(Arrays.copyOf(Arrays.copyOfRange(
                        children, (size / 2) + 1, children.length), order + 1)); // Copy half the
                                                                                 // children to the
                                                                                 // right split
                                                                                 // child.
                    Arrays
                        .fill(children, (size / 2) + 1, children.length, null); // Clear the
                                                                                // children just
                    // copied.
                }
                
                size /= 2;
                insertChild.size = size;
                
                return parent.insert(insertRecord, insertChild); // Insert the child with the new
                                                                 // split
                                                                 // child to the parent.
            }
            
            size++;
            
            return (isRoot() ? this : null);
        }
        
        /**
         * Checks if this node is a leaf node.
         * 
         * @return true if a leaf.
         */
        protected boolean isLeaf() {
            return (children == null);
        }
        
        /**
         * Checks if this node is the root node.
         * 
         * @return true if the root.
         */
        protected boolean isRoot() {
            return (parent == null);
        }
        
        /**
         * Sets the child at index to be child, updating the childs parent too.
         * 
         * @param index the index of the child.
         * @param child the child.
         */
        protected void setChild(int index, Node child) {
            children[index] = child;
            
            if (child != null)
                child.parent = this;
        }
        
        /**
         * Sets the children to be the given children.
         * 
         * @param children to set to.
         */
        protected void setChildren(Node[] children) {
            // assert (children.length <= order + 1);
            
            for (int i = 0; i < children.length; i++) {
                if (children[i] == null)
                    return;
                
                setChild(i, children[i]);
            }
        }
        
        /**
         * Shuffles all the objects in the src array from <code>from</code> to <code>to</code>.
         * Clears
         * all inbetween / after elements to null after shuffling.
         * 
         * @param src the array to shuffle.
         * @param from the from index.
         * @param to the to index.
         */
        private void shuffle(Object[] src, int from, int to) {
            System.arraycopy(src, from, src, to,
                src.length - Math.max(from, to));
            
            if (from > to)
                for (int i = src.length - 1; i >= (src.length - (from - to)); i--)
                    src[i] = null;
            else
                for (int i = from; i < to; i++)
                    src[i] = null;
        }
    }
}