package com.hjwylde.uni.comp103.assignment4.arrayQueue;

/*
 * Code for Assignment 4, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.util.*;

/**
 * ArrayQueue - a Queue collection; The implementation uses an array to store
 * the items. It
 * "wraps around" the end of the array. When the array is full, it will create a
 * new array of double
 * the current size, and copy all the items over to the new array. It extends
 * AbstractQueue, so it
 * only needs to implement size(), poll(), peek(), offer(), and iterator().
 */

public class ArrayQueue<E> extends AbstractQueue<E> {
    
    private static int INITIAL_CAPACITY = 10;
    
    private E[] data;
    private int front = 0; // the index of the first item in the queue
    private int back = 0; // the index where the next new item will go
    
    // items are stored from front..back-1, except that this may
    // "wrap around" the end of the array
    // if front==back, then the queue is empty.
    // if front==back+1 (or front==0 and back==length-1) then the queue is full
    
    @SuppressWarnings("unchecked")
    public ArrayQueue() {
        data = (E[]) new Object[ArrayQueue.INITIAL_CAPACITY];
    }
    
    /** Returns true if this set contains no elements. */
    @Override
    public boolean isEmpty() {
        return back == front;
    }
    
    /** Returns an iterator over the elements in the list */
    @Override
    public Iterator<E> iterator() {
        return new ArrayQueueIterator<>(this);
    }
    
    /**
     * Adds an item onto the back for the queue, unless the item is null If the
     * item was added, it
     * return true, if the item was null, it returns false
     */
    @Override
    public boolean offer(E value) {
        if (value == null)
            return false;
        
        ensureCapacity();
        
        data[back++] = value;
        back %= data.length; // Sets the back to 0 if it is > data.length
        
        return true;
    }
    
    /**
     * Returns the value at the front of the queue, or null if the queue is empty,
     * but does not change
     * the queue
     */
    @Override
    public E peek() {
        if (isEmpty())
            return null;
        
        return data[front];
    }
    
    /**
     * Removes and returns the value at the front of the queue, or null if the
     * queue is empty
     */
    @Override
    public E poll() {
        if (isEmpty())
            return null;
        
        E element = data[front++]; // Grab the element at the front.
        front %= data.length; // Puts front back to 0 if it is > data.length
        
        return element;
    }
    
    /** Returns the number of elements in collection */
    @Override
    public int size() {
        if (front > back) // Wraps around
            return (data.length - (front - back));
        
        return (back - front); // Elements are in order and not wrapping around
    }
    
    // helpful for debugging.
    @Override
    public String toString() {
        String ans = "(" + front + "," + back + "):[ ";
        int i = front;
        while (i != back) {
            ans = ans + data[i] + " ";
            i = (i + 1) % data.length;
        }
        return ans + "]";
    }
    
    /**
     * Ensure data array has sufficient number of elements to add a new element
     */
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (((front == 0) && (back == (data.length - 1)))
            || (front == (back + 1))) { // If
            // queue
            // is
            // full...
            E[] temp = (E[]) new Object[data.length * 2];
            
            // Create a new array, resetting the front and back to 0 and size()
            // respectively
            for (int i = 0; i < size(); i++)
                // For all elements in the array
                if ((i + front) >= data.length) // If wrapping around...
                    temp[i] = data[(i + front) - data.length]; // ...Adjust the value to
                                                               // grab by -data.length
                else
                    // If just iterating normally through the queue...
                    temp[i] = data[i + front]; // ...Grab the value
                    
            back = size();
            front = 0;
            data = temp;
        }
    }
    
    public static void main(String[] args) {
        ArrayQueue.test();
        ArrayQueue.test2();
    }
    
    // -------------------------------------------------------------------
    /** Tests the primary methods of ArrayList1, on an array of Strings */
    public static void test() {
        System.out.println("Testing a queue....");
        
        int nextoffer = 0;
        int nextpoll = 0;
        
        Queue<String> queue = new ArrayQueue<>();
        
        System.out.println("\nTesting size and isEmpty of empty queue");
        if (!queue.isEmpty())
            System.out.println("new queue is not empty");
        if (queue.size() != 0)
            System.out.println("new queue has a size != 0 ");
        
        System.out.println("\nTesting offer and poll");
        for (int i = 0; i < 8; i++)
            queue.offer("v" + (nextoffer++));
        for (int i = 0; i < 3; i++)
            if (!queue.poll().equals("v" + (nextpoll++)))
                System.out.printf("poll v%d failed \n", i);
        
        // poll/offer 2*INITIALCAPACITY items, to ensure wrap around
        System.out.println("\nTesting wraparound");
        for (int i = 0; i < (2 * ArrayQueue.INITIAL_CAPACITY); i++) {
            queue.offer("v" + (nextoffer++));
            if (!queue.poll().equals("v" + (nextpoll++)))
                System.out.printf("poll v%d failed when testing wrapping \n",
                    (nextpoll - 1));
            if (queue.size() != 5)
                System.out
                    .printf("poll and offer failed to maintain queue size \n");
        }
        
        // offer 2* INITIALCAPACITY +5 more items to check that ensureCapacity works
        System.out.println("\nTesting ensureCapacity");
        
        int beforeSize = queue.size();
        for (int i = 0; i < ((2 * ArrayQueue.INITIAL_CAPACITY) + 5); i++)
            queue.offer("v" + (nextoffer++));
        
        if (queue.size() != (beforeSize + ((2 * ArrayQueue.INITIAL_CAPACITY) + 5)))
            System.out.printf("queue size should now be %d\n", beforeSize
                + ((2 * ArrayQueue.INITIAL_CAPACITY) + 5));
        
        if (!queue.poll().equals("v" + (nextpoll++)))
            System.out.printf("poll failed after enlarging \n");
        
        System.out.println("\nTesting iterator");
        int iterpoll = nextpoll;
        for (String str : queue)
            if (!str.equals("v" + (iterpoll++)))
                System.out
                    .printf(
                        "iterator did not generate items in expected order at v%d \n",
                        iterpoll - 1);
        
        // poll until empty
        System.out.println("\nTesting poll until empty");
        while (!queue.isEmpty())
            if (!queue.poll().equals("v" + (nextpoll++)))
                System.out.printf("poll v%d failed \n", nextpoll - 1);
        
        if (queue.poll() != null)
            System.out.printf("poll of empty queue failed \n");
        
        if (queue.offer(null))
            System.out.printf("Offering null should return false \n");
        
        System.out.println("All tests completed. No news is good news");
    }
    
    public static void test2() {
        System.out.println("\n---------------\nTesting a queue....");
        Queue<String> queue = new ArrayQueue<>();
        Random r = new Random();
        for (int i = 0; i < 50; i++)
            if (r.nextBoolean()) {
                String s = "v" + (10 + r.nextInt(90));
                queue.offer(s);
                System.out.println("       " + queue + "  <- " + s);
            } else if (queue.peek() != null)
                System.out.println(queue.poll() + "<-  " + queue);
    }
    
    /**
     * Iterator for ArrayQueue. This implementation is not smart, and may be
     * corrupted if any changes
     * are made to the ArrayQueue that it is iterating down. Note that because it
     * is an inner class,
     * it has access to the ArrayQueue's private fields.
     */
    @SuppressWarnings("hiding")
    private class ArrayQueueIterator<E> implements Iterator<E> {
        
        // needs fields, constructor, hasNext(), next(), and remove()
        
        private final ArrayQueue<E> queue; // reference to the list it is iterating
                                           // down
        private int nextIndex; // the index of the next value the iterator will
                               // return
        
        private ArrayQueueIterator(ArrayQueue<E> q) {
            queue = q;
            nextIndex = q.front;
        }
        
        /** Return true if iterator has at least one more element */
        @Override
        public boolean hasNext() {
            return (nextIndex != back);
        }
        
        /** return next element in the set */
        @Override
        public E next() {
            if (nextIndex == back)
                throw new NoSuchElementException();
            E ans = queue.data[nextIndex];
            nextIndex = (nextIndex + 1) % data.length;
            return ans;
        }
        
        /**
         * Remove from the set the last element returned by the iterator. Can only
         * be called once per
         * call to next.
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }
    
}
