package com.hjwylde.uni.comp103.assignment05.sortingHat;

/*
 * Code for Assignment 5, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.JOptionPane;

import com.hjwylde.uni.comp103.util.FileChooser;

/**
 * SortingHat sorts data using mergeSort and quickSort
 * 
 * The implementation uses an array to store the items.
 * 
 * It does not allow null as an element of the array. When full it will create a
 * new array of double
 * the current size, and copy all the items over to the new array
 */

public class SortingHat<E> {
    
    // data fields
    private static int INITIALCAPACITY = 10;
    private int count = 0;
    private final E[] data;
    private final int countQuickSort = 0;
    private final int countMergeSort = 0;
    
    // I changed to 1 as I try to attempt it all, but there was no specific
    // "Challenge" part so to
    // speak
    @SuppressWarnings("unused")
    private static int AttemptChallenge = 1; // change to 1 if you are attempting
                                             // Challenge
    
    private Comparator<E> comp = new ComparableComparator<>(); // the comparator
    
    /** Constructs an empty SortedArrayBag that will use the default comparator */
    @SuppressWarnings("unchecked")
    public SortingHat() {
        data = (E[]) new Object[SortingHat.INITIALCAPACITY];
    }
    
    /**
     * Constructs SortingHat out of the given collection that will use the default
     * comparator. It will
     * copy all the items from c into the data array.
     */
    @SuppressWarnings("unchecked")
    public SortingHat(Collection<E> c) {
        int size = c.size();
        data = (E[]) new Object[size];
        
        for (E item : c)
            if (item != null)
                data[count++] = item;
    }
    
    /**
     * Constructs SortingHat out of the given collection that will use the given
     * comparator. It will
     * copy all the items from c into the data array.
     */
    @SuppressWarnings("unchecked")
    public SortingHat(Collection<E> c, Comparator<E> comp) {
        int size = c.size();
        data = (E[]) new Object[size];
        
        for (E item : c)
            if (item != null)
                data[count++] = item;
        
        this.comp = comp;
    }
    
    public E[] getData() {
        return data;
    }
    
    /** Returns true iff the collection is empty */
    public boolean isEmpty() {
        return count == 0;
    }
    
    /** finds the median of three values and returns it */
    public E median(E a, E b, E c, Comparator<E> comp) {
        if (((comp.compare(a, b) <= 0) && (comp.compare(b, c) <= 0))
            || ((comp.compare(a, b) >= 0) && (comp.compare(b, c) >= 0)))
            return b;
        if (((comp.compare(b, c) <= 0) && (comp.compare(c, a) <= 0))
            || ((comp.compare(b, c) >= 0) && (comp.compare(c, a) >= 0)))
            return c;
        
        return a;
    }
    
    /**
     * merge method merges from[low..mid-1] with from[mid..high-1] into
     * to[low..high-1] Prints data
     * array after merge using printData
     */
    public void merge(E[] from, E[] to, int low, int mid, int high,
        Comparator<E> comp) {
        int index = low;
        int indexLeft = low;
        int indexRight = mid;
        
        while ((indexLeft < mid) && (indexRight < high))
            if (comp.compare(from[indexLeft], from[indexRight]) <= 0)
                to[index++] = from[indexLeft++];
            else
                to[index++] = from[indexRight++];
        
        while (indexLeft < mid)
            to[index++] = from[indexLeft++];
        while (indexRight < high)
            to[index++] = from[indexRight++];
        
        printData(to);
    }
    
    /**
     * recursive mergeSort method
     */
    public void mergeSort(E[] data, E[] temp, int low, int high,
        Comparator<E> comp) {
        if (high > (low + 1)) {
            int mid = (low + high) / 2;
            // mid = low of upper 1/2, = high of lower half
            mergeSort(temp, data, low, mid, comp);
            mergeSort(temp, data, mid, high, comp);
            merge(temp, data, low, mid, high, comp);
        }
    }
    
    /**
     * non-recursive, wrapper method copies data array into a temporary array
     * calls recursive
     * mergeSort method
     */
    @SuppressWarnings("unchecked")
    public void mergeSort(E[] data, int size, Comparator<E> comp) {
        E[] other = (E[]) new Object[size];
        for (int i = 0; i < size; i++)
            other[i] = data[i];
        mergeSort(data, other, 0, size, comp); // call to recursive merge sort
                                               // method
    }
    
    /**
     * prints the temp array into the screen adds extra spaces to all the words so
     * they are all equal
     * in length after adding the spaces simply returns if length of the temp
     * array is longer than 20
     * without printing
     */
    public void printData(E[] temp) {
        if (temp.length > 20)
            return;
        
        // Find the length for all the item strings to be
        int itemLength = 0;
        for (E item : temp)
            itemLength = Math.max(itemLength, item.toString().length() + 1);
        
        // Add all item's to str
        String str = "";
        for (E item : temp) {
            str += item.toString();
            // If the item's string.length() is less than the required length...
            for (int i = itemLength; i > item.toString().length(); i--)
                // ...add spaces to it
                str += " ";
        }
        
        System.out.println(str); // Print out str
    }
    
    // two comparators for the testing the sorts.
    
    public String printSelect(E[] temp, int start, int end) {
        String s = new String();
        int i = 0;
        
        for (E item : temp) {
            if ((i >= start) && (i < end))
                s += item + " ";
            i++;
        }
        
        return s;
    }
    
    /**
     * Quick sort recursive call
     */
    public void quickSort(E[] data, int low, int high, Comparator<E> comp) {
        if ((high - low) < 2) // only one item to sort.
            return;
        
        // split into two parts, mid = index of boundary
        int mid = partition(data, low, high, comp);
        quickSort(data, low, mid, comp);
        quickSort(data, mid, high, comp);
    }
    
    /** Returns the number of elements in collection */
    public int size() {
        return count;
    }
    
    /**
     * sorts data using MergeSort prints time taken by MergeSort prints number of
     * times Merge is
     * called
     */
    public void testMergeSort(SortingHat<E> s) {
        long start = System.currentTimeMillis();
        mergeSort(s.data, s.size(), s.comp);
        long end = System.currentTimeMillis();
        
        System.out.println("MergeSort takes: " + (end - start)
            + " milliseconds");
        System.out.println("Merge sort was called: " + countMergeSort
            + " times");
    }
    
    /**
     * sorts data using QuickSort Prints time taken by Quick sort prints number of
     * times partition
     * gets called
     */
    public void testQuickSort(SortingHat<E> s) {
        
        long start = System.currentTimeMillis();
        quickSort(s.data, 0, s.size(), s.comp);
        long end = System.currentTimeMillis();
        
        System.out.println("QuickSort takes: " + (end - start)
            + " milliseconds\n");
        System.out.println("Quick sort was called: " + countQuickSort
            + " times");
    }
    
    /* ===============MERGE SORT================= */
    
    /** good to have, just in case */
    @Override
    public String toString() {
        String str = new String();
        for (E item : data)
            str += item + " ";
        System.out.println("In toString: " + str);
        return str;
    }
    
    /** Writes the sorted "temp" array onto a file with name <fname> (Sorted).txt */
    public void writeFile(E[] temp, String fName) {
        // Return if any parameters are invalid
        if ((temp.length == 0) || fName.equals(""))
            return;
        
        String fNameSorted = fName + " (Sorted).txt";
        File sortedFile = new File(fNameSorted);
        
        int i = 2;
        // While sortedFile exists...
        while (sortedFile.exists()) {
            // ...change name of sortedFile
            fNameSorted = fName + " (Sorted) (" + i++ + ").txt";
            sortedFile = new File(fNameSorted);
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(
            sortedFile.getAbsolutePath()))) {
            // For each item in temp...
            for (E item : temp) {
                // ...write item to the text file
                bw.write(item.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Partition into small items (low..mid-1) and large items (mid..high-1)
     * prints data array after
     * partition
     */
    private int partition(E[] data, int low, int high, Comparator<E> comp) {
        int left = low - 1;
        int right = high;
        E pivot = median(data[low], data[high - 1], data[(low + high) / 2],
            comp);
        
        while (left < right) {
            do
                left++;
            while ((left < high) && (comp.compare(data[left], pivot) < 0));
            do
                right--;
            while ((right >= low) && (comp.compare(data[right], pivot) > 0));
            
            if (left < right)
                SortingHat.swap(data, left, right);
        }
        
        printData(data);
        
        return left;
    }
    
    // Main
    public static void main(String[] args) {
        
        List<String> list = Arrays.asList("Harry", "Ron", "Hermione",
            "Dumbledore", "Snape", "Voldemort", "Hagrid", "Remus", "Ginny",
            "Pettigrew", "Sirius", "Draco");
        
        SortingHat<String> s = new SortingHat<>(list);
        
        s.testMergeSort(s);
        System.out
            .println("=================END OF MERGE SORT==================\n");
        
        SortingHat<String> ss = new SortingHat<>(list,
            new ReverseStringComparator());
        ss.testMergeSort(ss);
        System.out
            .println("=================END OF MERGE SORT - REVERSE==================\n");
        
        SortingHat<String> s1 = new SortingHat<>(list);
        s1.testQuickSort(s1);
        System.out
            .println("================END OF QUICK SORT===================\n");
        
        SortingHat<String> s2 = new SortingHat<>(list,
            new ReverseStringComparator());
        s2.testQuickSort(s2);
        System.out
            .println("================END OF QUICK SORT - REVERSE===================\n");
        
        int print = list.size();
        
        System.out.println("\n\nThe SORTING HAT says: \n"
            + s.printSelect(s.data, 0, print / 4) + "are in Gryffindor!\n"
            + s.printSelect(s.data, print / 4, (print / 4) * 2)
            + "are in Hufflepuff!\n"
            + s.printSelect(s.data, (print / 4) * 2, (print / 4) * 3)
            + "are in Ravenclaw!\n"
            + s.printSelect(s.data, (print / 4) * 3, (print / 4) * 4)
            + "are in Slytherin!\n");
        
        // reading input from file
        List<String> listR = SortingHat.readFile();
        SortingHat<String> sR = new SortingHat<>(listR);
        sR.testMergeSort(sR); // mergesort data
        // write sorted data into file
        sR.writeFile(sR.data, "hogwarts");
        System.out
            .println("=================END OF MERGE SORT on file - sorted data written to file==================\n");
        
        // reading input from file
        SortingHat<String> sR1 = new SortingHat<>(listR);
        sR1.testQuickSort(sR1); // quicksort data
        // write sorted data into file
        sR1.writeFile(sR1.data, "hogwarts");
        System.out
            .println("=================END OF QUICK SORT on file - sorted data written to file==================\n");
        
    }
    
    /* ===============QUICK SORT================= */
    
    /**
     * Reads the contents of the file into a List and returns the List
     */
    public static List<String> readFile() {
        // ask the user for the name of the file to read
        String fname = FileChooser.open("Filename to read text from");
        if (fname == null) {
            JOptionPane.showMessageDialog(null, "No file name specified");
            return null;
        }
        
        List<String> temp = new ArrayList<>();
        try (Scanner f = new Scanner(new File(fname))) {
            // open the file and get ready to read from it
            
            // Store the contents of the file in temp (the list)
            // while loop to read each line of the file
            while (f.hasNext()) {
                String str = f.next().trim();
                temp.add(str);
            }
        } catch (IOException ex) { // what to do if there is an io error.
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        return temp;
        
    }
    
    /** swaps two values */
    public static <E> void swap(E[] data, int left, int right) {
        E temp = data[left];
        data[left] = data[right];
        data[right] = temp;
    }
    
    // Comparator for comparable
    private class ComparableComparator<T> implements Comparator<T> {
        
        @SuppressWarnings("unchecked")
        @Override
        public int compare(T item1, T item2) {
            return ((Comparable<T>) item1).compareTo(item2);
        }
        
    }
    
    private static class ReverseStringComparator implements Comparator<String> {
        
        @Override
        public int compare(String str1, String str2) {
            return (-str1.compareTo(str2));
        }
    }
    
    @SuppressWarnings("unused")
    private static class StringComparator implements Comparator<String> {
        
        @Override
        public int compare(String str1, String str2) {
            return (str1).compareTo(str2);
        }
    }
    
}