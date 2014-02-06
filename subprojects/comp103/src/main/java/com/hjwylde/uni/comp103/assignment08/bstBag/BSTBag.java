package com.hjwylde.uni.comp103.assignment08.bstBag;

/*
 * Code for Assignment 8, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.util.*;

/**
 * Implementation of the Bag type using a binary search tree to store the items.
 * Uses a comparator
 * to compare items. There is a default comparator that works if the items are
 * Comparable Does not
 * allow null as an element of a bag.
 */
public class BSTBag<E> extends AbstractCollection<E> {
    
    // Structure
    private BSTNode root;
    
    // Fields
    private int count = 0;
    private final Comparator<E> comp;
    
    /*
     * Constructors
     */
    
    public BSTBag() {
        comp = new ComparableComparator();
    }
    
    public BSTBag(Comparator<E> c) {
        comp = c;
    }
    
    /**
     * Add the specified element to this bag, so long as it is not null. Return
     * true if the collection
     * changes, and false if it does not change.
     */
    @Override
    public boolean add(E item) {
        if (item == null)
            return false;
        
        if (root == null)
            root = new BSTNode(item);
        else
            root.add(item);
        count++;
        
        return true;
    }
    
    /**
     * Return true iff the bag contains an item (must be non null)
     */
    @Override
    public boolean contains(Object item) {
        if (find(item) == null)
            return false;
        
        return true;
    }
    
    /**
     * Return true iff the bag is empty
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }
    
    /**
     * Return an iterator over the elements in this bag.
     */
    @Override
    public Iterator<E> iterator() {
        return new BSTBagIterator(root);
    }
    
    /**
     * Remove the element matching a given item Return true if the collection
     * changes, and false if it
     * did not change.
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object item) {
        if ((item == null) || (root == null))
            return false;
        
        if (root.value.equals(item)) {
            root = removeNode(root);
            count--;
            return true;
        }
        
        BSTNode node = root;
        while (true)
            if (comp.compare((E) item, node.value) < 0) {
                if (node.left == null)
                    return false;
                else if (node.left.value.equals(item)) {
                    node.left = removeNode(node.left);
                    count--;
                    return true;
                } else
                    node = node.left;
            } else if (node.right == null)
                return false;
            else if (node.right.value.equals(item)) {
                node.right = removeNode(node.right);
                count--;
                return true;
            } else
                node = node.right;
    }
    
    /**
     * Return the number of elements in bag
     */
    @Override
    public int size() {
        return count;
    }
    
    /**
     * Useful method for debugging
     */
    @Override
    public String toString() {
        if (root == null)
            return "<Empty Bag>";
        
        StringBuilder ans = new StringBuilder("{Bag:");
        Iterator<E> itr = iterator();
        if (itr.hasNext())
            ans.append(itr.next());
        while (itr.hasNext()) {
            ans.append(",");
            ans.append(itr.next());
        }
        ans.append("}");
        return ans.toString();
    }
    
    private BSTNode find(Object item) {
        if ((root == null) || (item == null))
            return null;
        
        return root.find(item);
    }
    
    @SuppressWarnings("static-method")
    private BSTNode removeNode(BSTNode node) {
        if ((node.left == null) && (node.right == null))
            return null;
        else if (node.left == null)
            return node.right;
        else if (node.right == null)
            return node.left;
        
        BSTNode parent = node.right; // Left most parent node.
        if (parent.left == null) {
            parent.left = node.left;
            return parent;
        }
        
        while (parent.left.left != null)
            parent = parent.left;
        
        BSTNode replacement = parent.left; // Replacement node to return.
        parent.left = replacement.right;
        
        replacement.left = node.left;
        replacement.right = node.right;
        
        return replacement;
    }
    
    public static void main(String[] args) {
        BSTBag.Test1();
        BSTBag.Test2();
    }
    
    public static void Test1() {
        
        BSTBag<String> bag = new BSTBag<>();
        
        // An array of strings for testing, and a string with all of those
        // strings concatenated
        
        String[] data = {
            "v21", "v13", "v18", "v16", "v19", "v17", "v12", "v20", "v11",
            "v26", "v28", "v27", "v15", "v24", "v22", "v23", "v25", "v14",
            "v29", "v30"
        };
        
        String data1 = "v11v12v13v14v15v16v17v18v19v20v21v22v23v24v25v26v27v28v29v30";
        
        // size and isEmpty of empty bag
        
        if (!bag.isEmpty())
            System.out.format("new bag is not empty: %s\n", bag);
        
        if (bag.size() != 0)
            System.out.format("new bag has size %d, should be 0: %s\n",
                bag.size(), bag);
        
        // adding ---------------------------------------------------------
        
        for (int i = 0; i < data.length; i++)
            if (!bag.add(data[i]))
                System.out.format("Bag should successfully add %s\n", data[i]);
        
        if (bag.add(null))
            System.out.format("Bag should not add null\n");
        
        if (bag.isEmpty())
            System.out.format("bag of 20 elements should not be  empty: %s\n",
                bag);
        
        if (bag.size() != 20)
            System.out.format("new bag has size %d, should be 20: %s\n",
                bag.size(), bag);
        
        for (int i = 11; i <= 30; i++)
            if (!bag.contains("v" + i))
                System.out.format("Bag should contain %s\n", "v" + i);
        
        if (bag.contains("v10"))
            System.out.format("Bag should not contain v0\n");
        
        if (bag.contains("v31"))
            System.out.format("Bag should not contain v31\n");
        
        if (bag.contains(null))
            System.out.format("Bag should not contain null\n");
        
        // iterator ------------------------------------------------------
        
        StringBuilder p = new StringBuilder("");
        
        for (String str : bag)
            p = p.append(str);
        if (!p.toString().equals(data1))
            System.out.format("iterator gives  %s\nbut should give %s\n", p,
                data1);
        
        // removing -------------------------------------------------------
        
        System.out.println("before remove:" + bag);
        
        if (bag.remove("v10"))
            System.out
                .format("Bag should not be able to remove non existant item v10\n");
        
        if (bag.remove(null))
            System.out.format("Bag should not be able to remove null\n");
        
        if (!bag.remove("v11"))
            System.out.format("Bag should be able to remove v11\n");
        
        System.out.println("----------------\nafter remove v11:" + bag);
        
        if (!bag.remove("v20"))
            System.out.format("Bag should be able to remove v20\n");
        
        System.out.println("----------------\nafter remove v20:" + bag);
        
        if (!bag.remove("v30"))
            System.out.format("Bag should be able to remove v30\n");
        
        System.out.println("----------------\nafter remove v30:" + bag);
        
        if (!bag.remove(data[0]))
            System.out.format("Bag should be able to remove %s\n", data[0]);
        
        System.out.println("----------------\nafter remove root:" + bag);
        
        if (bag.remove("v31"))
            System.out.format("Bag should not be able to remove v31\n");
        
        if (bag.contains("v11"))
            System.out.format("Bag should no longer contain v11\n");
        
        if (bag.contains("v20"))
            System.out.format("Bag should no longer contain v20\n");
        
        if (bag.contains("v30"))
            System.out.format("Bag should no longer contain v30\n");
        
        if (bag.size() != 16)
            System.out.format("Bag should now contain 16 items, not %s\n",
                bag.size());
        
        if (bag.contains("v11"))
            System.out.format("Bag should not contain v11\n");
        
        if (!bag.contains("v12"))
            System.out.format("Bag should contain v12\n");
        
        if (!bag.contains("v19"))
            System.out.format("Bag should contain v19\n");
        
        if (bag.contains("v20"))
            System.out.format("Bag should not contain v20\n");
        
        if (!bag.contains("v22"))
            System.out.format("Bag should contain v22\n");
        
        if (!bag.contains("v29"))
            System.out.format("Bag should contain v29\n");
        
        if (bag.contains("v30"))
            System.out.format("Bag should not contain v30\n");
        
        System.out.println("All tests completed");
        
    }
    
    /*
     * Comparator for comparable
     */
    
    public static void Test2() {
        
        BSTBag<String> bag = new BSTBag<>();
        
        System.out.print("\nCreating new, empty bag:\n");
        
        System.out.println(" Size: " + bag.size() + ", isEmpty: "
            + bag.isEmpty() + ", Bag: " + bag);
        
        System.out.print("\nChecking add: ");
        List<String> data = Arrays.asList("A", "P", "F", "R", "E", "W", "Q",
            "T", "Z", "X", "C", "V", "B", "G", "I", "H", "M", "S", "K", "U",
            "N");
        
        for (String s : data) {
            System.out.print(s + " ");
            bag.add(s);
            if (!bag.contains(s))
                System.out.printf("\ncontains(%s) should not be false\n", s);
        }
        
        System.out.println(" Size: " + bag.size() + ", isEmpty: "
            + bag.isEmpty() + ", Bag: " + bag);
        
        System.out.print("\nChecking contains: ");
        
        for (String st : data) {
            System.out.print(st + " ");
            if (!bag.contains(st))
                System.out.printf("\ncontains(%s) should not be false\n", st);
        }
        
        for (String st : Arrays.asList("@", "D", "J", "L", "O", "a")) {
            System.out.print(st + " ");
            if (bag.contains(st))
                System.out.printf("\ncontains(%s) should not be true\n", st);
        }
        
        System.out.printf("\n Bag now:  Size=%d, isEmpty=%b   %s\n",
            bag.size(), bag.isEmpty(), bag);
        
        System.out.print("\nChecking remove: ");
        
        for (String s : Arrays.asList("F", "W", "X", "A", "Z", "P")) {
            System.out.print(s + " ");
            bag.remove(s);
            if (bag.contains(s))
                System.out.printf(
                    "\n Removed %s, but bag still contains it!\n", s);
        }
        
        System.out.printf("\n Bag now:  Size=%d, isEmpty=%b   %s\n",
            bag.size(), bag.isEmpty(), bag);
        
        System.out.println("\nChecking with a reversed String comparator");
        
        Comparator<String> reverseComp = new Comparator<String>() {
            
            @Override
            public int compare(String str1, String str2) {
                return (-str1.compareTo(str2));
            }
        };
        
        BSTBag<String> bag3 = new BSTBag<>(reverseComp);
        for (String s : data)
            bag3.add(s);
        System.out.println(" Bag should be in reverse order: " + bag3);
        for (String s : data)
            if (!bag3.contains(s))
                System.out.println(" Fail contains: " + s);
        
    }
    
    // Two not very good test methods!!!
    
    /**
     * Iterator for BST To iterate through the tree in sorted order, it must do an
     * iterative in-order
     * traversal, using a stack to keep track of its position To accomplish this,
     * it must process all
     * the left subtree of a node before it processes the node. Therefore, every
     * time it pushes a node
     * on the stack, it must also immediately push all the left descendents of the
     * node on to the
     * stack, all the way to the leftmost descendent. When it pops a node from the
     * stack, it processes
     * the node and then pushes its right child onto the stack (and all that
     * child's left descendents)
     */
    private class BSTBagIterator implements Iterator<E> {
        
        Stack<BSTNode> stack = new Stack<>();
        
        public BSTBagIterator(BSTNode root) {
            for (BSTNode nd = root; nd != null; nd = nd.left)
                stack.push(nd);
        }
        
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        
        @Override
        public E next() {
            if (stack.isEmpty())
                throw new NoSuchElementException();
            
            BSTNode next = stack.pop();
            for (BSTNode node = next.right; node != null; node = node.left)
                stack.push(node);
            
            return next.value;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }
    
    /**
     * Private class for the nodes Has public fields so methods in BSTBag can
     * access fields directly
     */
    private class BSTNode {
        
        // Structure
        private final E value;
        private BSTNode left = null;
        private BSTNode right = null;
        
        /*
         * Constructors
         */
        
        public BSTNode(E v) {
            value = v;
        }
        
        /*
         * Operations
         */
        
        public void add(E value) {
            if (comp.compare(value, this.value) < 0) {
                if (left == null)
                    left = new BSTNode(value);
                else
                    left.add(value);
            } else if (right == null)
                right = new BSTNode(value);
            else
                right.add(value);
        }
        
        @SuppressWarnings("unchecked")
        public BSTNode find(Object item) {
            if (value.equals(item))
                return this;
            
            if (comp.compare((E) item, value) < 0) {
                if (left != null)
                    return left.find(item);
            } else if (right != null)
                return right.find(item);
            
            return null;
        }
    }
    
    // For testing - comment out a call if you don't want to run that test
    
    private class ComparableComparator implements Comparator<E> {
        
        @Override
        @SuppressWarnings("unchecked")
        public int compare(E ob1, E ob2) {
            return ((Comparable<E>) ob1).compareTo(ob2);
        }
    }
    
}
