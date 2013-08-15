package com.hjwylde.uni.comp103.assignment8.bstBag;

/*
 * Code for Assignment 8, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.util.*;

/**
 * SortedArrayBag - an implementation of Bag
 * 
 * The implementation uses an array to store the items. It keeps the items in
 * order, determined by a
 * comparator provided to the constructor
 * 
 * It does not allow null as an element of a bag. It is efficient for accessing
 * items; it is slower
 * to insert. When full it will create a new array of double the current size,
 * and copy all the
 * items over to the new array
 */

public class SortedArrayBag<E> extends AbstractCollection<E> {
    
    // Data fields
    
    private static int INITIALCAPACITY = 10;
    
    private int count = 0;
    
    private E[] data;
    
    private static int AttemptChallenge = 0; // SET TO 1 IF YOU ARE ATTEMPTING
                                             // CHALLENGE
    
    private Comparator<E> comp = new ComparableComparator<>(); // the comparator
    
    // Constructors
    
    /** Construct an empty SortedArrayBag that will use the default comparator */
    
    @SuppressWarnings("unchecked")
    public SortedArrayBag() {
        data = (E[]) new Object[SortedArrayBag.INITIALCAPACITY];
    }
    
    /**
     * Construct a SortedArrayBag out of the given collection that will use the
     * default comparator. It
     * will copy all the items from c into the array and then sort them.
     */
    
    @SuppressWarnings("unchecked")
    public SortedArrayBag(Collection<E> c) {
        int size = c.size();
        data = (E[]) new Object[size];
        size += size / 10; // leave some extra space
        for (E item : c)
            if (item != null)
                data[count++] = item;
        sort(); // sorts the data array
    }
    
    /**
     * Construct SortedArrayBag out of the given collection that will use the
     * given comparator. It
     * will copy all the items from c into the array and then sort them.
     */
    
    @SuppressWarnings("unchecked")
    public SortedArrayBag(Collection<E> c, Comparator<E> comp) {
        int size = c.size();
        size += size / 10; // leave some extra space
        data = (E[]) new Object[size];
        for (E item : c)
            if (item != null) {
                data[count] = item;
                count++;
            }
        this.comp = comp;
        sort(); // sorts the data array
    }
    
    /** Construct an empty SortedArrayBag that will use the given comparator */
    
    @SuppressWarnings("unchecked")
    public SortedArrayBag(Comparator<E> comp) {
        data = (E[]) new Object[SortedArrayBag.INITIALCAPACITY];
        this.comp = comp;
    }
    
    // Methods
    
    /**
     * Add the specified element to this bag, so long as it is not null. Return
     * true if the collection
     * changes, and false if it did not change.
     */
    
    @Override
    public boolean add(E item) {
        if (item == null)
            return false;
        int index = findIndex(item); // where item should be.
        ensureCapacity();
        for (int i = count; i > index; i--)
            data[i] = data[i - 1];
        data[index] = item;
        count++;
        return true;
    }
    
    /**
     * Return true iff the bag contains an item. Uses findIndex to find the
     * position the item should
     * be if it is present
     */
    
    @Override
    public boolean contains(Object item) {
        int index = findIndex(item);
        if (index == count)
            return false;
        return (item.equals(data[index]));
    }
    
    /** Return true iff the collection is empty */
    
    @Override
    public boolean isEmpty() {
        return count == 0;
    }
    
    /** Return an iterator over the elements in this bag. **/
    
    @Override
    public Iterator<E> iterator() {
        return new SortedArrayBagIterator<>(this);
    }
    
    /**
     * Remove the element matching a given item Return true if the collection
     * changes, and false if it
     * did not change.
     */
    
    @Override
    public boolean remove(Object item) {
        if (item == null)
            return false;
        int index = findIndex(item);
        if ((index == count) || !item.equals(data[index]))
            return false;
        count--;
        for (int i = index; i < count; i++)
            data[i] = data[i + 1];
        data[count] = null;
        return true;
    }
    
    /** Return the number of elements in collection */
    
    @Override
    public int size() {
        return count;
    }
    
    // SortedArrayBag utility methods
    
    /** Ensure data array has sufficient space to add a new element */
    
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (count < data.length)
            return;
        E[] newArray = (E[]) new Object[data.length * 2];
        for (int i = 0; i < count; i++)
            newArray[i] = data[i];
        data = newArray;
    }
    
    /**
     * Find the index of where an element is in the dataarray, (or where it ought
     * to be (possibly past
     * the end) if it's not there). Assumes that the item is not null. Uses binary
     * search and requires
     * that the items are kept in order. Returns a value between 0 and count.
     */
    
    @SuppressWarnings("unchecked")
    private int findIndex(Object itm) {
        E item = (E) itm;
        int low = 0; // minimum possible position of item
        int high = count; // maximum possible position of item
                          // item should be at position [low .. high]
        while (low < high) {
            int mid = (low + high) / 2; // low < high, therefore low <= mid and mid <
                                        // high
            if (comp.compare(item, data[mid]) > 0) // item should be after mid (ie
                                                   // [mid+1 .. high] )
                low = mid + 1; // item should still be at [low .. high] low <= high
            else
                // item should be at or before mid (ie [low .. mid] )
                high = mid; // item should still be at [low .. high], low<=high
        }
        // item should be at [low .. high] and low = high therefore item should be
        // at low.
        return low;
    }
    
    /**
     * Sort the data array, from 0 to count, using the comparator in the comp
     * field.
     */
    
    private void sort() {
        for (int i = 0; i < (count - 1); i++) {
            int smallest = i;
            for (int j = i + 1; j < count; j++)
                if (comp.compare(data[smallest], data[j]) > 0)
                    smallest = j;
            E item = data[smallest];
            data[smallest] = data[i];
            data[i] = item;
        }
    }
    
    public static void main(String[] args) {
        SortedArrayBag.test();
    }
    
    // Comparator for comparable
    
    public static void test() {
        SortedArrayBag<String> bag = new SortedArrayBag<>(
            new StringComparator());
        System.out.format("\nCreating new, empty bag:\n");
        System.out.format(" Size: %d, isEmpty: %b, Bag: %s\n", bag.size(),
            bag.isEmpty(), bag);
        
        System.out.format("\nChecking add:\n");
        String[] array1 = {
            "A", "P", "F", "R", "E", "W", "Q", "T", "Z", "X", "C", "V", "B",
            "G", "I", "H", "M", "S", "K", "U", "N"
        };
        for (String s : array1)
            bag.add(s);
        System.out.format(" Size: %d, isEmpty: %b, Bag: %s\n", bag.size(),
            bag.isEmpty(), bag);
        
        System.out.format("\nChecking contains:\n");
        for (String st : array1)
            System.out.print(bag.contains(st) ? " OK" : " FAIL");
        
        System.out.format("\n Contains(D)=%s\n", bag.contains("D"));
        
        System.out.format("\nAdding A, Z, and P again\n");
        
        bag.add("A");
        bag.add("Z");
        bag.add("P");
        System.out.format(" Size: %d, isEmpty: %b, Bag: %s\n", bag.size(),
            bag.isEmpty(), bag);
        
        System.out.format("\nRemoving A, Z, and P\n");
        bag.remove("A");
        System.out.println(" now contains(A)=" + bag.contains("A"));
        bag.remove("Z");
        System.out.println(" now contains(Z)=" + bag.contains("Z"));
        bag.remove("P");
        System.out.println(" now contains(P)=" + bag.contains("P"));
        System.out.format(" Size: %d, isEmpty: %b, Bag: %s\n", bag.size(),
            bag.isEmpty(), bag);
        
        System.out
            .format("\nChecking alternative constructors. Items should be in order\n");
        List<String> list = Arrays.asList("a", "p", "f", "r", "e", "w", "q",
            "q", "t", "z", "x", "c", "v", "b", "a", "z");
        
        SortedArrayBag<String> bag1 = new SortedArrayBag<>(list);
        System.out.format(" Bag(list): %s\n", bag1);
        for (String s : list)
            if (!bag1.contains(s))
                System.out.format(" Fail contains: %s\n", s);
        
        SortedArrayBag<String> bag2 = new SortedArrayBag<>(list,
            new StringComparator());
        System.out.format(" Bag(list, comp): %s\n", bag2);
        for (String s : list)
            if (!bag2.contains(s))
                System.out.format(" Fail contains: %s\n", s);
        
        // check with a reversed String comparator.
        System.out
            .format("\nChecking comparators. Items should be in reverse order\n");
        
        SortedArrayBag<String> bag3 = new SortedArrayBag<>(
            new ReverseStringComparator());
        for (String s : list)
            bag3.add(s);
        System.out.format(" Bag(reverse): %s\n", bag3);
        for (String s : list)
            if (!bag3.contains(s))
                System.out.format(" Fail contains: %s\n", s);
        
        SortedArrayBag<String> bag4 = new SortedArrayBag<>(list,
            new ReverseStringComparator());
        System.out.format(" Bag(list,reverse): %s\n", bag4);
        for (String s : list)
            if (!bag4.contains(s))
                System.out.format(" Fail contains: %s\n", s);
        
    }
    
    private class ComparableComparator<T> implements Comparator<T> {
        
        @SuppressWarnings("unchecked")
        @Override
        public int compare(T item1, T item2) {
            return ((Comparable<T>) item1).compareTo(item2);
        }
        
    }
    
    // Two comparators for the test method.
    
    private static class ReverseStringComparator implements Comparator<String> {
        
        @Override
        public int compare(String str1, String str2) {
            return -str1.compareTo(str2);
        }
    }
    
    private class SortedArrayBagIterator<T> implements Iterator<T> {
        
        // needs fields, constructor, hasNext(), next(), and remove()
        
        private final SortedArrayBag<T> bag;
        private int nextIndex = 0;
        private boolean canRemove = false;
        
        private SortedArrayBagIterator(SortedArrayBag<T> b) {
            bag = b;
        }
        
        /** Return true if iterator has at least one more element */
        
        @Override
        public boolean hasNext() {
            return (nextIndex < bag.count);
        }
        
        /** Return next element in the bag */
        
        @Override
        public T next() {
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
            if (SortedArrayBag.AttemptChallenge == 0)
                throw new UnsupportedOperationException();
            if (!canRemove)
                throw new IllegalStateException();
            nextIndex--;
            bag.count--;
            
            for (int i = nextIndex + 1; i < bag.count; i++)
                data[i - 1] = data[i];
            data[count] = null;
            canRemove = false;
        }
    }
    
    private static class StringComparator implements Comparator<String> {
        
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }
    
}
