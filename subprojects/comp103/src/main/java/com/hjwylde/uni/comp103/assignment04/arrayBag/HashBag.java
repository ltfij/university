package com.hjwylde.uni.comp103.assignment04.arrayBag;

/*
 * Code for Assignment 4, COMP 103 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

import java.util.*;

/**
 * HashBag - a Bag collection;
 * 
 * The implementation uses a HashMap to store the items and their counts Duplicate items are stored
 * just once, but their associated count records the number of duplicates. It does not keep the
 * items in any particular order, and may change the order of the remaining items when removing
 * items. It does not allow null items. Only requires that the elements can be compared with
 * equals(Object) It is efficient, becuase the underlying HashMap is efficient.
 */

public class HashBag<E> extends AbstractCollection<E> {

    private final Map<E, Counter> map;

    public HashBag() {
        map = new HashMap<>();
    }

    /**
     * Optional constructor that constructs a Bag containing all the elements of the argument
     * collection
     */
    public HashBag(Collection<E> c) {
        map = new HashMap<>((c.size() * 4) / 3);
        for (E item : c) {
            if (item == null)
                continue;
            Counter val = map.get(item);
            if (val == null)
                map.put(item, new Counter(1));
            else
                val.increment();
        }
    }

    /**
     * Adds the specified element to this bag if it is not already present. Will not add the null
     * value. Returns true if the collection changes, and false if it did not change.
     */
    @Override
    public boolean add(E item) {
        if (item == null)
            throw new IllegalArgumentException("null invalid value for bag");
        Counter count = map.get(item);
        if (count == null)
            map.put(item, new Counter(1));
        else
            count.increment();
        return true;
    }

    /** Returns true if this bag contains the specified element. **/
    @Override
    public boolean contains(Object item) {
        if (item == null)
            return false;
        return map.containsKey(item);
    }

    /**
     * Returns an iterator over the elements in this bag. Uses the hashMap iterator, but has to
     * return the multiple values individually!
     **/
    @Override
    public Iterator<E> iterator() {
        return new HashBagIterator<>(this, map.entrySet().iterator());
    }

    /**
     * Remove an element matching a given item Makes no change to the bag and returns false if the
     * item is not present.
     */
    @Override
    public boolean remove(Object item) {
        if (item == null)
            return false;
        Counter count = map.get(item);
        if (count == null)
            return false;
        count.decrement();
        if (count.isZero())
            map.remove(item);
        return true;
    }

    /**
     * Determines number of elements in collection
     * 
     * @return number of elements in collection as integer
     */
    @Override
    public int size() {
        int count = 0;
        for (Counter c : map.values())
            count += c.value();
        return count;
    }

    /*
     * The count of the number of duplicates of an item Like integer, but mutable
     */
    public class Counter {

        int value;

        public Counter(int v) {
            value = v;
        }

        public void decrement() {
            value--;
        }

        public void increment() {
            value++;
        }

        public boolean isZero() {
            return value == 0;
        }

        public int value() {
            return value;
        }
    }

    @SuppressWarnings("hiding")
    private class HashBagIterator<E> implements Iterator<E> {

        private final HashBag<E> bag;
        private final Iterator<Map.Entry<E, Counter>> iter;
        private E nextItem;
        private E lastItem;
        private Counter remainingItemCount;

        private HashBagIterator(HashBag<E> bag, Iterator<Map.Entry<E, Counter>> it) {
            this.bag = bag;
            iter = it;
            getNextReady();
        }

        /** Return true if iterator has at least one more element */
        @Override
        public boolean hasNext() {
            return (nextItem != null);
        }

        /** return next element in the bag */
        @Override
        public E next() {
            if (nextItem == null)
                throw new NoSuchElementException();
            E item = nextItem;
            lastItem = nextItem;
            remainingItemCount.decrement();
            if (remainingItemCount.isZero())
                getNextReady();
            return item;
        }

        /**
         * Remove from the bag the last element returned by the iterator. Can only be called once
         * per call to next.
         */
        @Override
        public void remove() {
            if (lastItem == null)
                throw new IllegalStateException();
            bag.remove(lastItem);
            lastItem = null;
        }

        /**
         * Gets the next item out of the entrySet iterator, ready for the next call to next.
         */
        private void getNextReady() {
            if (!iter.hasNext())
                nextItem = null;
            else {
                Map.Entry<E, Counter> entry = iter.next();
                nextItem = entry.getKey();
                remainingItemCount = entry.getValue();
            }
        }
    }

}
