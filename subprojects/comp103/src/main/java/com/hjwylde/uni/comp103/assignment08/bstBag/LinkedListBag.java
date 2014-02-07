package com.hjwylde.uni.comp103.assignment08.bstBag;

/*
 * Code for Assignment 8, COMP 103 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * LinkedListBag - a Bag collection;
 * 
 * The implementation uses a linked list to store the items. It does not keep the items in any
 * particular order. It does not allow null items. Only requires that the elements can be compared
 * with equals(Object) It is not particularly efficient
 */

public class LinkedListBag<E> extends AbstractCollection<E> {

    // data fields

    private LinkedListNode root = null;

    private int count = 0;

    public LinkedListBag() {}

    // Constructors

    /**
     * Optional constructor that constructs a Bag containing all the elements of the argument
     * collection
     */

    public LinkedListBag(Collection<E> c) {
        count = 0;
        for (E item : c)
            if (item != null)
                add(item);
    }

    /**
     * Add the specified element to this bag, so long as it is not null. Return true if the
     * collection changes, and false if it did not change.
     */

    @Override
    public boolean add(E item) {
        if (item == null)
            throw new IllegalArgumentException("null invalid value for bag");

        root = new LinkedListNode(item, root);
        count++;
        return true;
    }

    // Methods

    /** Return true if this bag contains the specified element. **/

    @Override
    public boolean contains(Object item) {
        if (item == null)
            return false;
        for (LinkedListNode n = root; n != null; n = n.next)
            if (item.equals(n.value))
                return true; // found it
        return false; // note found
    }

    /** Return an iterator over the elements in this bag. **/

    @Override
    public Iterator<E> iterator() {
        return new LinkedListBagIterator(this);
    }

    /**
     * Remove an element matching a given item Return true if the item was present and then removed.
     * Makes no change to the bag and return false if the item is not present.
     */

    @Override
    public boolean remove(Object item) {
        if ((item == null) || (root == null))
            return false;
        // check whether item is the first element in the list
        if (item.equals(root.value)) {
            root = root.next;
            count--;
            return true;
        }
        // look for node before the one to be deleted
        LinkedListNode n = root;
        while (n.next != null) {
            if (item.equals(n.next.value)) { // found it
                n.next = n.next.next;
                count--;
                return true;
            }
            n = n.next;
        }
        return false; // not there
    }

    /**
     * Determine number of elements in collection
     * 
     * @return number of elements in collection as integer
     */

    @Override
    public int size() {
        return count;
    }

    public static void main(String[] arguments) {
        LinkedListBag.test();
    }

    /** Test the primary methods of a Bag */

    public static void test() {
        System.out.format("\n\nTesting basic methods on Bag\n");

        Collection<String> bag = new LinkedListBag<>();

        // size and isEmpty of empty bag
        if (!bag.isEmpty())
            System.out.printf("New bag is not empty\n");
        if (bag.size() != 0)
            System.out.printf("New bag has size %d, should be 0\n", bag.size());

        // add 20 items ---------------------------------------------------------
        int n = 20;
        for (int i = 1; i <= n; i++)
            if (!bag.add("v" + i))
                System.out.printf("Bag should successfully add %s\n", "v" + i);
        try {
            if (bag.add(null))
                System.out.printf("Bag should not add null\n");
        } catch (IllegalArgumentException e) {
        }

        if (bag.isEmpty())
            System.out.printf("Bag of %d elements should not be  empty\n", n);
        if (bag.size() != n)
            System.out.printf("New bag has size %d, should be %d\n", bag.size(), n);

        // add duplicates
        for (int i = 1; i <= n; i++)
            if (!bag.add("v" + i))
                System.out.printf("Bag should successfully add %s\n", "v" + i);
        if (bag.size() != (2 * n))
            System.out.printf("New bag has size %d, should be %d\n", bag.size(), 2 * n);

        // check contains on each item and on items that shouldn't be there
        // ------------------------------------------
        for (int i = 1; i <= n; i++)
            if (!bag.contains("v" + i))
                System.out.printf("Bag should contain %s\n", "v" + i);
        if (bag.contains("v0"))
            System.out.printf("Bag should not contain v0\n");
        if (bag.contains("v" + (n + 1)))
            System.out.printf("Bag should not contain v%d\n", n + 1);

        // remove the duplicates, leaving originals.
        for (int i = 1; i <= n; i++)
            if (!bag.remove("v" + i))
                System.out.printf("Bag should be able to remove %s\n", "v" + i);
        if (bag.size() != n)
            System.out.printf("New bag has size %d, should be %d\n", bag.size(), n);

        // remove first, last, and middle item and check result
        // ------------------------------------------
        if (bag.remove("v0"))
            System.out.printf("Bag should not be able to remove non existant item v0\n");
        if (bag.remove(null))
            System.out.printf("Bag should not be able to remove null\n");
        if (!bag.remove("v1"))
            System.out.printf("Bag should be able to remove v1\n");
        if (!bag.remove("v" + (n / 2)))
            System.out.printf("Bag should be able to remove v%d\n", n / 2);
        if (!bag.remove("v" + n))
            System.out.printf("Bag should be able to remove v%d\n", n);
        if (bag.remove("v" + (n + 1)))
            System.out.printf("Bag should not be able to remove v%d\n", n + 1);

        if (bag.size() != (n - 3))
            System.out.printf("Bag should now contain %d items, not %d\n", n - 3, bag.size());

        if (bag.contains("v1"))
            System.out.printf("Bag should not contain v1\n");
        if (bag.contains("v" + (n / 2)))
            System.out.printf("Bag should not contain v%d\n", n / 2);
        if (bag.contains("v" + n))
            System.out.printf("Bag should not contain v%d\n", n);
        for (int i = 2; i <= (n - 1); i++)
            if ((i != (n / 2)) && !bag.contains("v" + i))
                System.out.printf("Bag should contain v%d\n", i);

        // iterator (assumes that items have been added at the front)
        // ---------------------------

        /*
         * System.out.printf("Testing iterator\n"); try{ List<Integer> items = new
         * ArrayList<Integer>(); Random r = new Random(); for (int i = 0; i < 1000; i++)
         * items.add(r.nextInt(10000)); Collection<Integer> numbag = new LinkedListBag<Integer>();
         * for (int i : items) numbag.add(i); if (numbag.size() != items.size())
         * System.out.printf("Bag should contain %d number \n", items.size()); for (int i : items) {
         * if (!numbag.contains(i)) System.out.printf("Bag is missing %d \n", i); } int count = 0;
         * for (int i : numbag) { count++; if (!items.contains(i))
         * System.out.printf("Bag should not contain item: %d \n", i); } if (count != items.size())
         * System.out.printf("Iterator only returned %d items \n", count); } catch (Exception e)
         * {System.out.println("Iterator test threw an exception");}
         */

        System.out.println("All tests completed, and passed, unless error signaled");
    }

    public static void test0() {
        System.out.format("\n\nTesting basic methods on Bag\n");

        Collection<String> bag = new LinkedListBag<>();

        System.out.printf("New bag has size is %d\n", bag.size());

        // add 20 items ---------------------------------------------------------
        int n = 20;
        for (int i = 1; i <= n; i++)
            if (!bag.add("v" + i))
                System.out.printf("Bag should successfully add %s\n", "v" + i);

        System.out.printf("Bag size after adding is %d\n", bag.size());

        // for (String v : bag)
        // System.out.printf("/%s\n", v);

        // check contains on each item and on items that shouldn't be there
        for (int i = 1; i <= n; i++)
            if (!bag.contains("v" + i))
                System.out.printf("Bag should contain %s\n", "v" + i);
        if (bag.contains("v0"))
            System.out.printf("Bag should not contain v0\n");
        if (bag.contains("v" + (n + 1)))
            System.out.printf("Bag should not contain v%d\n", n + 1);

    }

    private class LinkedListBagIterator implements Iterator<E> {

        // needs fields, constructor, hasNext(), next(), and remove()

        private LinkedListNode current;

        private LinkedListBagIterator(LinkedListBag<E> s) {
            current = s.root;
        }

        /** Return true if iterator has at least one more element */

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        /** Return next element in the bag */

        @Override
        public E next() {
            if (current == null)
                throw new NoSuchElementException();
            E v = current.value;
            current = current.next;
            return v;
        }

        /**
         * Remove from the bag the last element returned by the iterator. Not implemented.
         */

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Private class for the nodes Has public fields so methods in BSTBag can access fields directly
     */

    private class LinkedListNode {

        // Data fields

        public E value;

        public LinkedListNode next = null;

        // Constructor

        public LinkedListNode(E v, LinkedListNode n) {
            value = v;
            next = n;
        }

    }

}
