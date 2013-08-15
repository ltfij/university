package com.hjwylde.uni.swen221.assignment7;

import java.util.*;

/*
 * Code for Assignment 7, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * An ArraySet implements the Set interface using an array, in a similar fashion to an ArrayList.
 * However, a key difference is that it maintains the elements in a sorted order, thus permitting
 * faster implementations of certain operations.
 * 
 * @author djp, hjw
 * 
 * @param <T> the type of elements this ArraySet contians.
 */
public class ArraySet<T extends Comparable<T>> implements Set<T> {
    
    private final static int INITIAL_CAPACITY = 10;
    
    private Object[] items;
    private int size;
    
    /**
     * Creates a new empty ArraySet.
     */
    public ArraySet() {
        clear();
    }
    
    /**
     * Construct an ArraySet<T> from another collection of items. In the case that that is also an
     * ArraySet, then we guarantee to avoid resorting it.
     */
    public ArraySet(Collection<? extends T> c) {
        items = c.toArray().clone();
        size = c.size();
        
        // Don't need to sort if it is an ArraySet.
        if (c instanceof ArraySet<?>)
            return;
        // Don't need to sort if it's already a sorted set and uses its natural ordering.
        if ((c instanceof SortedSet<?>)
            && (((SortedSet<?>) c).comparator() == null))
            return;
        
        Arrays.sort(items, 0, size());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(T item) {
        if (item == null)
            throw new NullPointerException();
        
        int index = indexOf(item);
        if (index >= 0)
            return false;
        
        index = -index - 1;
        
        ensureCapacity();
        
        // Shift and update size.
        System.arraycopy(items, index, items, index + 1, size++ - index);
        
        items[index] = item;
        
        return true;
    }
    
    /**
     * Add all items from ndata ArraySet into this ArraySet, whilst maintaining the sorted order.
     * Items already in this ArraySet will, hence, not be added. This returns true if this ArraySet
     * is
     * modified as a result.
     * 
     * @param c the collection the add all elements from.
     * @return true if this ArraySet has changed.
     */
    @SuppressWarnings("unchecked")
    public boolean addAll(ArraySet<? extends T> c) {
        // Determine max size of new array; this is an upper bound, since some things in c may
        // already
        // be in items.
        int maxSize = c.size() + size;
        
        Object[] nItems = new Object[maxSize];
        
        int i = 0; // Index into items.
        int j = 0; // Index into c.items.
        int w = 0; // Index into nItems.
        
        while ((i < size()) && (j < c.size())) {
            int res = ((T) items[i]).compareTo((T) c.items[j]);
            
            if (res < 0)
                nItems[w++] = items[i++];
            else if (res > 0)
                nItems[w++] = c.items[j++];
            else {
                maxSize--;
                nItems[w++] = items[i++];
                j++;
            }
        }
        
        // Only one if branch will be run as only one array may be unfinished.
        if (i < size())
            System.arraycopy(items, i, nItems, w, size() - i);
        if (j < c.size())
            System.arraycopy(c.items, j, nItems, w, c.size() - j);
        
        // Have we changed something?
        if (maxSize != size) {
            size = maxSize;
            items = nItems;
            
            return true;
        }
        
        // ...nope!
        
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        // Use optimized add.
        if (c instanceof ArraySet<?>)
            return addAll((ArraySet<? extends T>) c);
        
        boolean modified = false;
        for (T e : c)
            modified |= add(e);
        
        return modified;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        items = new Object[ArraySet.INITIAL_CAPACITY];
        size = 0;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        if (o == null)
            return false;
        
        return indexOf(o) >= 0;
    }
    
    /**
     * Check whether all keys in an ArraySet are contained in this ArraySet. This implementation is
     * faster than the default one, because it exploits knowledge of the fact that both arrays are
     * sorted.
     * 
     * @param other the other ArraySet to check.
     * @return true if we contain all of the other set or if the ArraySet is empty.
     */
    public boolean containsAll(ArraySet<T> other) {
        if (other.size() == 0)
            return true;
        
        Iterator<T> it = other.iterator();
        
        T cur = it.next();
        for (T next : this)
            if (next.compareTo(cur) == 0)
                if (it.hasNext())
                    cur = it.next();
                else {
                    cur = null;
                    break;
                }
        
        return cur == null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o))
                return false;
        
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({
        "unchecked", "rawtypes"
    })
    @Override
    public Iterator<T> iterator() {
        return new InternalIterator(items, size);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        if (o == null)
            throw new NullPointerException();
        
        int index = indexOf(o);
        if (index < 0)
            return false;
        
        // Shift and update size.
        System.arraycopy(items, index + 1, items, index, --size - index);
        
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c)
            modified |= remove(o);
        
        return modified;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        int index = 0;
        for (int i = 0; i != size; ++i) {
            Object item = items[i];
            if (c.contains(item))
                items[index++] = item;
        }
        
        // Have we changed anything?
        if (size() != index) {
            size = index;
            return true;
        }
        
        // ...nope!
        
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return size;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(items, size);
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <E> E[] toArray(E[] array) {
        if (array.length < size)
            // Following required by Collection interface.
            array = (E[]) java.lang.reflect.Array.newInstance(array.getClass()
                .getComponentType(), size);
        
        System.arraycopy(items, 0, array, 0, size);
        
        if (array.length > size)
            // Following required by Collection interface.
            array[size] = null;
        
        return array;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String r = "[";
        boolean firstTime = true;
        for (int i = 0; i != size; ++i) {
            Object o = items[i];
            if (!firstTime)
                r += ", ";
            firstTime = false;
            r += o;
        }
        return r + "]";
    }
    
    /**
     * Ensures the capacity for at least one more item to be added to this ArraySet.
     */
    private void ensureCapacity() {
        if (size() < items.length)
            return;
        
        // Double the length of the array because we are full.
        items = Arrays.copyOf(items, items.length * 2);
    }
    
    /**
     * Finds the index or (-expectedIndex - 1) for the given key. Assumes the key is not null.
     * Returns
     * -1 if the object isn't a compatible type. Note that this only happens in cases where the
     * actual
     * value of the index doesn't matter, only the fact that it's negative.
     * 
     * @param key the key to find.
     * @return the index of it or (-index - 1) if it isn't contained inside this ArraySet.
     */
    private int indexOf(Object key) {
        try {
            return Arrays.binarySearch(items, 0, size(), key);
        } catch (ClassCastException e) {
            // The only time this happens is in contains or remove, which the actual returned index
            // doesn't matter, only the fact that it's negative. Therefore it's okay to just return
            // -1.
            return -1;
        }
    }
    
    /**
     * An iterator implementation.
     * 
     * @author djp
     */
    private static final class InternalIterator<S> implements Iterator<S> {
        
        private final S[] items;
        private final int size;
        private int index;
        
        /**
         * Creates a new iterator for the given items with size <code>size</code>.
         * 
         * @param items the items to iterator over.
         * @param size the number of items in <code>items</code>.
         */
        public InternalIterator(S[] items, int size) {
            this.size = size;
            this.items = items;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            return index < size;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public S next() {
            if (!hasNext())
                throw new NoSuchElementException();
            
            return items[index++];
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                "SortedSet.Iterator.remove() is unsupported");
        }
    }
}