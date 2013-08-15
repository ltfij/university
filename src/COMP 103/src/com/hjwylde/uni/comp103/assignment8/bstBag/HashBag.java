package com.hjwylde.uni.comp103.assignment8.bstBag;

/*
 * Code for Assignment 8, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.util.*;

/**
 * HashBag - a Bag collection;
 * 
 * The implementation uses a HashMap to store the items and their counts
 * Duplicate items are stored
 * just once, but their associated count records the number of duplicates. It
 * does not keep the
 * items in any particular order, and may change the order of the remaining
 * items when removing
 * items. It does not allow null items. Only requires that the elements can be
 * compared with
 * equals(Object) It is efficient, because the underlying HashMap is efficient.
 */

public class HashBag<E> extends AbstractCollection<E> {
    
    // Data fields
    
    private final Map<E, Counter> map;
    
    public HashBag() {
        map = new HashMap<>();
    }
    
    // Constructors
    
    /**
     * Optional constructor to construct a Bag containing all the elements of the
     * argument collection
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
    
    // Methods
    
    /**
     * Add the specified element to this bag, so long as it is not null. Return
     * true if the collection
     * changes, and false if it does not change.
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
    
    /** Return true if this bag contains the specified element. **/
    
    @Override
    public boolean contains(Object item) {
        if (item == null)
            return false;
        return map.containsKey(item);
    }
    
    /**
     * Return an iterator over the elements in this bag. Uses the hashMap
     * iterator, but has to return
     * the multiple values individually!
     **/
    
    @Override
    public Iterator<E> iterator() {
        return new HashBagIterator<>(this, map.entrySet().iterator());
    }
    
    /**
     * Remove an element matching a given item Make no change to the bag and
     * return false if the item
     * is not present.
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
     * Determine number of elements in collection
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
     * A counter class for counting the number of occurrences of an item Like
     * integer, but mutable
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
    
    private class HashBagIterator<T> implements Iterator<T> {
        
        private final HashBag<T> bag;
        private final Iterator<Map.Entry<T, Counter>> iter;
        private T nextItem;
        private T lastItem;
        private Counter remainingItemCount;
        
        private HashBagIterator(HashBag<T> bag,
            Iterator<Map.Entry<T, Counter>> it) {
            this.bag = bag;
            iter = it;
            getNextReady();
        }
        
        /** Return true if iterator has at least one more element */
        
        @Override
        public boolean hasNext() {
            return (nextItem != null);
        }
        
        /** Return next element in the bag */
        
        @Override
        public T next() {
            if (nextItem == null)
                throw new NoSuchElementException();
            T item = nextItem;
            lastItem = nextItem;
            remainingItemCount.decrement();
            if (remainingItemCount.isZero())
                getNextReady();
            return item;
        }
        
        /**
         * Remove from the bag the last element returned by the iterator. Can only
         * be called once per
         * call to next.
         */
        
        @Override
        public void remove() {
            if (lastItem == null)
                throw new IllegalStateException();
            bag.remove(lastItem);
            lastItem = null;
        }
        
        /**
         * Get the next item out of the entrySet iterator, ready for the next call
         * to next.
         */
        
        private void getNextReady() {
            if (!iter.hasNext())
                nextItem = null;
            else {
                Map.Entry<T, Counter> entry = iter.next();
                nextItem = entry.getKey();
                remainingItemCount = entry.getValue();
            }
        }
        
    }
    
}
