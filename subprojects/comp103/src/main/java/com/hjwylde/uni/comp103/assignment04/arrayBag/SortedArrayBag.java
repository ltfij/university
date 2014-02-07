package com.hjwylde.uni.comp103.assignment04.arrayBag;

/*
 * Code for Assignment 4, COMP 103 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

import java.util.*;

/**
 * SortedArrayBag - an implementation of Bag
 * 
 * The implementation uses an array to store the items. It keeps the items in order, determined by a
 * comparator provided to the constructor
 * 
 * It does not allow null as an element of a bag. It is efficient for accessing items; it is slower
 * to insert. When full it will create a new array of double the current size, and copy all the
 * items over to the new array
 */

public class SortedArrayBag<E> extends AbstractCollection<E> {

    // data fields
    private static int INITIALCAPACITY = 10;
    private int count = 0;
    private E[] data;

    private static int AttemptChallenge = 1; // change to 1 if you are attempting
                                             // Challenge

    private Comparator<E> comp = new ComparableComparator<>(); // the comparator

    /** Constructs an empty SortedArrayBag that will use the default comparator */
    @SuppressWarnings("unchecked")
    public SortedArrayBag() {
        data = (E[]) new Object[SortedArrayBag.INITIALCAPACITY];
    }

    /**
     * Constructs SortedArrayBag out of the given collection that will use the default comparator.
     * It will copy all the items from c into the array and then sort them.
     */
    @SuppressWarnings("unchecked")
    public SortedArrayBag(Collection<E> c) {
        data = (E[]) c.toArray();
        count = data.length;

        sort(); // sorts the data array
    }

    /**
     * Constructs SortedArrayBag out of the given collection that will use the given comparator. It
     * will copy all the items from c into the array and then sort them.
     */
    @SuppressWarnings("unchecked")
    public SortedArrayBag(Collection<E> c, Comparator<E> comp) {
        data = (E[]) c.toArray();
        count = data.length;

        this.comp = comp;
        sort(); // sorts the data array
    }

    /** Constructs an empty SortedArrayBag that will use the given comparator */
    @SuppressWarnings("unchecked")
    public SortedArrayBag(Comparator<E> comp) {
        data = (E[]) new Object[SortedArrayBag.INITIALCAPACITY];
        this.comp = comp;
    }

    /**
     * Adds the specified element to this bag Returns true if the collection changes, and false if
     * it did not change.
     */
    @Override
    public boolean add(E item) {
        if (item == null)
            return false;

        int index = findIndex(item); // where item should be.
        ensureCapacity();

        // Shift all values down one to make room for item
        for (int i = count; i > index; i--)
            data[i] = data[i - 1];
        data[index] = item;

        count++;

        return true;
    }

    /**
     * Returns true iff the bag contains an item. Uses findIndex to find the position the item
     * should be if it is present
     */
    @Override
    public boolean contains(Object item) {
        if (item == null)
            return false;

        int index = findIndex(item);
        if ((index >= count) || (index < 0)) // It is possible that index may equal
                                             // count, which is out
                                             // of bounds
            return false;

        return data[index].equals(item); // Must check if the index returned
                                         // actually is the
                                         // item
    }

    /** Returns true iff the collection is empty */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /** Returns an iterator over the elements in this bag. **/
    @Override
    public Iterator<E> iterator() {
        return new SortedArrayBagIterator<>(this);
    }

    /**
     * Remove the element matching a given item Returns true if the collection changes, and false if
     * it did not change.
     */
    @Override
    public boolean remove(Object item) {
        if (item == null)
            return false;

        int index = findIndex(item);
        if (!data[index].equals(item)) // Must check if the index actually is the
                                       // item
            return false;

        count--;

        // Shift all values down one
        for (int i = index; i < count; i++)
            data[i] = data[i + 1];

        data[count] = null;

        return true;
    }

    /** Returns the number of elements in collection */
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

        E[] newArray = (E[]) new Object[data.length * 2];
        for (int i = 0; i < count; i++)
            newArray[i] = data[i];
        data = newArray;
    }

    // SortedArrayBag utility methods

    /**
     * Find the index of where an element is in the dataarray, (or where it ought to be (possibly
     * past the end) if it's not there). Assumes that the item is not null. Uses binary search and
     * requires that the items are kept in order. Returns a value between 0 and count.
     */
    @SuppressWarnings("unchecked")
    private int findIndex(Object itm) {
        E item = (E) itm;

        int low = 0;
        int high = count;
        int mid;

        // Binary search until finds the index that item is at / should be at
        while (low < high) {
            mid = (low + high) / 2;

            if (comp.compare(data[mid], item) < 0)
                low = mid + 1;
            else
                high = mid;
        }

        return low; // Return this itm's index
    }

    /**
     * Sorts the data array, from 0 to count, using the comparator in the comp field. Note that this
     * is not a good design - this code is private to this class, and can't be reused. It would be
     * much nicer to make more re-usable code, for example by having a separate Sorting class. (Even
     * better, though not allowed here, use the Arrays.sort() method)
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

    public static void test() {
        SortedArrayBag<String> bag = new SortedArrayBag<>(new StringComparator());
        System.out.format("\nCreating new, empty bag:\n");
        System.out.format(" Size: %d, isEmpty: %b, Bag: %s\n", bag.size(), bag.isEmpty(), bag);

        System.out.format("\nChecking add:\n");
        String[] array1 =
                {"A", "P", "F", "R", "E", "W", "Q", "T", "Z", "X", "C", "V", "B", "G", "I", "H",
                        "M", "S", "K", "U", "N"};
        for (String s : array1)
            bag.add(s);
        System.out.format(" Size: %d, isEmpty: %b, Bag: %s\n", bag.size(), bag.isEmpty(), bag);

        System.out.format("\nChecking contains:\n");
        for (String st : array1)
            System.out.print(bag.contains(st) ? " OK" : " FAIL");

        System.out.format("\n Contains(D)=%s\n", bag.contains("D"));

        System.out.format("\nAdding A, Z, and P again\n");

        bag.add("A");
        bag.add("Z");
        bag.add("P");
        System.out.format(" Size: %d, isEmpty: %b, Bag: %s\n", bag.size(), bag.isEmpty(), bag);

        System.out.format("\nRemoving A, Z, and P\n");
        bag.remove("A");
        System.out.println(" now contains(A)=" + bag.contains("A"));
        bag.remove("Z");
        System.out.println(" now contains(Z)=" + bag.contains("Z"));
        bag.remove("P");
        System.out.println(" now contains(P)=" + bag.contains("P"));
        System.out.format(" Size: %d, isEmpty: %b, Bag: %s\n", bag.size(), bag.isEmpty(), bag);

        System.out.format("\nChecking alternative constructors. Items should be in order\n");
        List<String> list =
                Arrays.asList("a", "p", "f", "r", "e", "w", "q", "q", "t", "z", "x", "c", "v", "b",
                        "a", "z");

        SortedArrayBag<String> bag1 = new SortedArrayBag<>(list);
        System.out.format(" Bag(list): %s\n", bag1);
        for (String s : list)
            if (!bag1.contains(s))
                System.out.format(" Fail contains: %s\n", s);

        SortedArrayBag<String> bag2 = new SortedArrayBag<>(list, new StringComparator());
        System.out.format(" Bag(list, comp): %s\n", bag2);
        for (String s : list)
            if (!bag2.contains(s))
                System.out.format(" Fail contains: %s\n", s);

        // check with a reversed String comparator.
        System.out.format("\nChecking comparators. Items should be in reverse order\n");

        SortedArrayBag<String> bag3 = new SortedArrayBag<>(new ReverseStringComparator());
        for (String s : list)
            bag3.add(s);
        System.out.format(" Bag(reverse): %s\n", bag3);
        for (String s : list)
            if (!bag3.contains(s))
                System.out.format(" Fail contains: %s\n", s);

        SortedArrayBag<String> bag4 = new SortedArrayBag<>(list, new ReverseStringComparator());
        System.out.format(" Bag(list,reverse): %s\n", bag4);
        for (String s : list)
            if (!bag4.contains(s))
                System.out.format(" Fail contains: %s\n", s);

        SortedArrayBag<String> bag5 = new SortedArrayBag<>(list);
        System.out.println("\nChecking iterator remove method");
        System.out.format(" Bag elements: %s\n", bag5);
        Iterator<String> i = bag5.iterator();
        while (i.hasNext()) {
            String s = i.next();

            i.remove();
            System.out.println(" Removed: " + s + ". Elements left: " + bag5);
        }
    }

    // Comparator for comparable

    @SuppressWarnings("hiding")
    private class ComparableComparator<E> implements Comparator<E> {

        @SuppressWarnings("unchecked")
        @Override
        public int compare(E item1, E item2) {
            return ((Comparable<E>) item1).compareTo(item2);
        }

    }

    // two comparators for the test method.

    private static class ReverseStringComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {
            return (-str1.compareTo(str2));
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

        /** return next element in the bag */
        @Override
        public T next() {
            if (nextIndex >= bag.count)
                throw new NoSuchElementException();
            canRemove = true;
            return bag.data[nextIndex++];
        }

        /**
         * Remove from the bag the last element returned by the iterator. Can only be called once
         * per call to next.
         */
        @Override
        public void remove() {
            if (SortedArrayBag.AttemptChallenge == 0)
                throw new UnsupportedOperationException();

            if (!canRemove)
                return;

            nextIndex--;
            bag.count--;
            for (int i = nextIndex; i < bag.count; i++)
                bag.data[i] = bag.data[i + 1];

            bag.data[bag.count] = null;
        }
    }

    private static class StringComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {
            return (str1).compareTo(str2);
        }
    }

}
