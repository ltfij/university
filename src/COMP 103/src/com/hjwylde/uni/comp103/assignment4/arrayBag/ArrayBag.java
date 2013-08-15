package com.hjwylde.uni.comp103.assignment4.arrayBag;

/*
 * Code for Assignment 4, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.util.*;

/**
 * ArrayBag - a Bag collection;
 * 
 * The implementation uses an array to store the items. It does not keep the
 * items in any particular
 * order, and may change the order of the remaining items when removing items.
 * It does not allow
 * null items. Only requires that the elements can be compared with
 * equals(Object) It is not
 * particularly efficient When full, it will create a new array of double the
 * current size, and copy
 * all the items over to the new array
 */

public class ArrayBag<E> extends AbstractCollection<E> {
    
    private static int INITIALCAPACITY = 10;
    private int count = 0;
    private E[] data;
    
    @SuppressWarnings("unchecked")
    public ArrayBag() {
        data = (E[]) new Object[ArrayBag.INITIALCAPACITY];
    }
    
    /**
     * Optional constructor that constructs a Bag containing all the elements of
     * the argument
     * collection
     */
    @SuppressWarnings("unchecked")
    public ArrayBag(Collection<E> c) {
        int size = c.size();
        size += size / 10; // leave some extra space
        data = (E[]) new Object[size];
        for (E item : c)
            if (item != null)
                data[count++] = item;
    }
    
    /**
     * Adds the specified element to this bag. Will not add the null value.
     * Returns true if the
     * collection changes, and false if it did not change.
     */
    @Override
    public boolean add(E item) {
        if (item == null)
            throw new IllegalArgumentException("null invalid value for bag");
        
        ensureCapacity();
        data[count] = item;
        count++;
        
        return true;
    }
    
    /** Returns true if this bag contains the specified element. **/
    @Override
    public boolean contains(Object item) {
        if (item == null)
            return false;
        return findIndex(item) >= 0;
    }
    
    /** Returns an iterator over the elements in this bag. **/
    @Override
    public Iterator<E> iterator() {
        return new ArrayBagIterator<>(this);
    }
    
    /**
     * Remove an element matching a given item Returns true if the item was
     * present and then removed.
     * Makes no change to the bag and returns false if the item is not present.
     */
    @Override
    public boolean remove(Object item) {
        if (item == null)
            return false;
        
        int index = findIndex(item);
        if (index < 0)
            return false;
        
        count--;
        data[index] = data[count];
        return true;
    }
    
    /**
     * Determines number of elements in collection
     * 
     * @return number of elements in collection as integer
     */
    @Override
    public int size() {
        return count;
    }
    
    /**
     * Ensure data array has sufficient number of elements to add a new element
     */
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (count < data.length)
            return;
        E[] newArray = (E[]) (new Object[data.length * 2]);
        for (int i = 0; i < count; i++)
            newArray[i] = data[i];
        data = newArray;
    }
    
    // You may find it convenient to define the following method and use it in
    // the methods above, but you don't need to do it this way.
    
    /**
     * Find the index of an element in the dataarray, or -1 if not present Assumes
     * that the item is
     * not null
     */
    private int findIndex(Object item) {
        // (if you want to define this method)
        for (int i = 0; i < count; i++)
            if (item.equals(data[i]))
                return i;
        return -1;
    }
    
    public static void main(String[] arguments) {
        ArrayBag.test();
    }
    
    /** Tests the primary methods of a Bag */
    public static void test() {
        System.out.format("\n\nTesting basic methods on Bag\n");
        
        Collection<String> bag = new ArrayBag<>();
        
        // size and isEmpty of empty bag
        if (!bag.isEmpty())
            System.out.printf("new bag is not empty\n");
        if (bag.size() != 0)
            System.out.printf("new bag has size %d, should be 0\n", bag.size());
        
        // add 20 items ---------------------------------------------------------
        int n = 20;
        for (int i = 1; i <= n; i++)
            if (!bag.add("v" + i))
                System.out.printf("Bag should successfully add %s\n", "v" + i);
        try {
            if (bag.add(null))
                System.out.printf("Bag should not add null\n");
        } catch (IllegalArgumentException e) {}
        
        if (bag.isEmpty())
            System.out.printf("bag of %d elements should not be  empty\n", n);
        if (bag.size() != n)
            System.out.printf("new bag has size %d, should be %d\n",
                bag.size(), n);
        
        // add duplicates
        for (int i = 1; i <= n; i++)
            if (!bag.add("v" + i))
                System.out.printf("Bag should successfully add %s\n", "v" + i);
        if (bag.size() != (2 * n))
            System.out.printf("new bag has size %d, should be %d\n",
                bag.size(), 2 * n);
        
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
            System.out.printf("new bag has size %d, should be %d\n",
                bag.size(), n);
        
        // remove first, last, and middle item and check result
        // ------------------------------------------
        if (bag.remove("v0"))
            System.out
                .printf("Bag should not be able to remove non existant item v0\n");
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
            System.out.printf("Bag should now contain %d items, not %d\n",
                n - 3, bag.size());
        
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
        
        System.out.printf("Testing iterator\n");
        try {
            List<Integer> items = new ArrayList<>();
            Random r = new Random();
            for (int i = 0; i < 1000; i++)
                items.add(r.nextInt(10000));
            Collection<Integer> numbag = new ArrayBag<>();
            for (int i : items)
                numbag.add(i);
            if (numbag.size() != items.size())
                System.out.printf("Bag should contain %d number \n",
                    items.size());
            for (int i : items)
                if (!numbag.contains(i))
                    System.out.printf("Bag is missing %d \n", i);
            int count = 0;
            for (int i : numbag) {
                count++;
                if (!items.contains(i))
                    System.out.printf("Bag should not contain item: %d \n", i);
            }
            if (count != items.size())
                System.out.printf("Iterator only returned %d items \n", count);
        } catch (Exception e) {
            System.out.println("Iterator test threw an exception");
        }
        System.out
            .println("All tests completed, and passed, unless error signaled");
    }
    
    @SuppressWarnings("hiding")
    private class ArrayBagIterator<E> implements Iterator<E> {
        
        // needs fields, constructor, hasNext(), next(), and remove()
        
        private final ArrayBag<E> bag;
        private int nextIndex = 0;
        private boolean canRemove = false;
        
        private ArrayBagIterator(ArrayBag<E> s) {
            bag = s;
        }
        
        /** Return true if iterator has at least one more element */
        @Override
        public boolean hasNext() {
            return (nextIndex < bag.count);
        }
        
        /** return next element in the bag */
        @Override
        public E next() {
            if (nextIndex >= bag.count)
                throw new NoSuchElementException();
            canRemove = true;
            return bag.data[nextIndex++];
        }
        
        /**
         * Remove from the bag the last element returned by the iterator. Can only
         * be called once per
         * call to next.
         */
        @Override
        public void remove() {
            if (!canRemove)
                throw new IllegalStateException();
            nextIndex--;
            bag.count--;
            bag.data[nextIndex] = bag.data[bag.count];
            bag.data[bag.count] = null;
            canRemove = false;
        }
    }
    
}
