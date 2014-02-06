package com.hjwylde.uni.swen221.assignment07;

import java.awt.Point;
import java.util.*;

import org.junit.Assert;
import org.junit.Test;

/*
 * Code for Assignment 7, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class ArraySetTests {
    
    private static final Integer[] RAW_DATA = ArraySetTests.generateRawData(0);
    private static final Integer[] RAW_DATA_2 = ArraySetTests
        .generateRawData(-1);
    
    private static final List<Integer> LIST_DATA = Arrays
        .asList(ArraySetTests.RAW_DATA);
    private static final List<Integer> LIST_DATA_2 = Arrays
        .asList(ArraySetTests.RAW_DATA_2);
    
    @Test
    public void testClear() {
        ArraySet<Integer> as = ArraySetTests.genAS();
        
        as.clear();
        Assert.assertTrue(as.isEmpty());
    }
    
    @Test
    public void testConstructor() {
        ArraySet<Integer> as;
        
        new ArraySet<>();
        as = ArraySetTests.genAS();
        as = new ArraySet<>(as);
        new ArraySet<>(new TreeSet<>(as));
        
        TreeSet<Integer> ts = new TreeSet<>(new Comparator<Integer>() {
            
            @Override
            public int compare(Integer first, Integer second) {
                return second % first;
            }
        });
        new ArraySet<>(new TreeSet<>(ts));
    }
    
    @Test
    public void testInvalidAdd() {
        ArraySet<Integer> as = ArraySetTests.genAS();
        
        try {
            as.add(null);
            Assert.fail();
        } catch (NullPointerException e) {}
    }
    
    @SuppressWarnings("serial")
    @Test
    public void testInvalidAddAll() {
        ArraySet<Integer> as = new ArraySet<>();
        
        try {
            as.addAll(null);
            Assert.fail();
        } catch (NullPointerException e) {}
        try {
            as.addAll(new ArrayList<Integer>() {
                
                {
                    add(1);
                    add(null);
                }
            });
            Assert.fail();
        } catch (NullPointerException e) {}
    }
    
    @Test
    public void testInvalidContainsAll() {
        ArraySet<Integer> as = ArraySetTests.genAS();
        
        try {
            as.containsAll(null);
            Assert.fail();
        } catch (NullPointerException e) {}
    }
    
    @Test
    public void testInvalidRemove() {
        ArraySet<Integer> as = ArraySetTests.genAS();
        
        try {
            as.remove(null);
            Assert.fail();
        } catch (NullPointerException e) {}
    }
    
    @Test
    public void testInvalidRemoveAll() {
        ArraySet<Integer> as = ArraySetTests.genAS();
        
        try {
            as.removeAll(null);
            Assert.fail();
        } catch (NullPointerException e) {}
        try {
            as.removeAll(new ArrayList<Integer>() {
                
                private static final long serialVersionUID = 1L;
                
                {
                    add(2);
                    add(null);
                }
            });
            Assert.fail();
        } catch (NullPointerException e) {}
    }
    
    @Test
    public void testInvalidRetainAll() {
        ArraySet<Integer> as = ArraySetTests.genAS();
        
        try {
            as.retainAll(null);
            Assert.fail();
        } catch (NullPointerException e) {}
    }
    
    @Test
    public void testIsEmpty() {
        ArraySet<Integer> as;
        
        as = ArraySetTests.genAS();
        Assert.assertFalse(as.isEmpty());
        as.clear();
        Assert.assertTrue(as.isEmpty());
        
        as = ArraySetTests.genAS();
        as.removeAll(ArraySetTests.LIST_DATA);
        Assert.assertTrue(as.isEmpty());
        
        Assert.assertTrue(new ArraySet<Integer>().isEmpty());
    }
    
    @Test
    public void testIterator() {
        ArraySet<Integer> as = ArraySetTests.genAS();
        
        Iterator<Integer> it = as.iterator();
        
        for (int i = 0; i < as.size(); i++)
            it.next();
        
        try {
            it.next();
            Assert.fail();
        } catch (NoSuchElementException e) {}
        try {
            it.remove();
            Assert.fail();
        } catch (UnsupportedOperationException e) {}
    }
    
    @Test
    public void testSize() {
        Assert.assertEquals(ArraySetTests.genAS().size(),
            ArraySetTests.RAW_DATA.length);
        Assert.assertEquals(new ArraySet<Integer>().size(), 0);
    }
    
    @Test
    public void testToArray() {
        Assert.assertTrue(Arrays.equals(ArraySetTests.genAS().toArray(),
            ArraySetTests.RAW_DATA));
    }
    
    @Test
    public void testToEArray() {
        Assert.assertTrue(Arrays.equals(
            ArraySetTests.genAS().toArray(new Integer[0]),
            ArraySetTests.RAW_DATA));
        Assert.assertTrue(Arrays.equals(
            ArraySetTests.genAS().toArray(new Integer[100]),
            ArraySetTests.RAW_DATA));
        Assert.assertTrue(Arrays.equals(
            ArraySetTests.genAS().toArray(new Integer[150]),
            Arrays.copyOf(ArraySetTests.RAW_DATA, 150)));
    }
    
    @Test
    public void testToString() {
        String ar = ArraySetTests.genAS().toString();
        for (int i = 0; i < ArraySetTests.RAW_DATA.length; i++)
            if (ar.indexOf((i == 0 ? "[" : ", ") + ArraySetTests.RAW_DATA[i]
                + (i == (ArraySetTests.RAW_DATA.length - 1) ? "]" : ",")) < 0)
                Assert.fail();
        
        Assert.assertTrue(ar.equals(new TreeSet<>(ArraySetTests.LIST_DATA)
            .toString()));
    }
    
    @Test
    public void testValidAdd() {
        ArraySet<Integer> as = new ArraySet<>();
        
        for (int i = 0; i < 100; i++) {
            Assert.assertTrue(as.add(i));
            Assert.assertFalse(as.add(i / 2));
        }
    }
    
    @Test
    public void testValidAddAll() {
        ArraySet<Integer> as = new ArraySet<>();
        
        Assert.assertTrue(as.addAll(ArraySetTests.LIST_DATA));
        Assert.assertTrue(as.addAll(ArraySetTests.LIST_DATA_2));
        Assert.assertFalse(as.addAll(ArraySetTests.LIST_DATA));
        Assert.assertFalse(as.addAll(new ArrayList<Integer>()));
        Assert.assertFalse(as.addAll(new ArraySet<Integer>()));
        Assert.assertFalse(as
            .addAll((Collection<Integer>) new ArraySet<Integer>()));
        
        as = new ArraySet<>();
        Assert.assertTrue(as.addAll(new ArraySet<>(ArraySetTests.LIST_DATA)));
        Assert.assertFalse(as.addAll(new ArraySet<>(ArraySetTests.LIST_DATA)));
        Assert.assertTrue(as.addAll(new ArraySet<>(ArraySetTests.LIST_DATA_2)));
    }
    
    @Test
    public void testValidContains() {
        ArraySet<Integer> as = ArraySetTests.genAS();
        
        for (int i : ArraySetTests.RAW_DATA)
            Assert.assertTrue(as.contains(i));
        
        for (int i : as)
            Assert.assertTrue(as.contains(i));
        
        for (int i : ArraySetTests.RAW_DATA)
            Assert.assertFalse(!as.remove(i) || as.contains(i));
        
        for (char c : "Wobble fish like to go swimming down the stream."
            .toCharArray())
            Assert.assertFalse(as.contains(c));
        
        Assert.assertFalse(as.contains(new Uncomparable()));
        Assert.assertFalse(as.contains(null));
    }
    
    @Test
    public void testValidContainsAll() {
        Assert.assertTrue(ArraySetTests.genAS().containsAll(
            ArraySetTests.genAS()));
        Assert.assertFalse(new ArraySet<Integer>().containsAll(ArraySetTests
            .genAS()));
        Assert.assertFalse(ArraySetTests.genAS().containsAll(
            new ArrayList<>(ArraySetTests.LIST_DATA_2)));
        Assert.assertTrue(ArraySetTests.genAS().containsAll(
            ArraySetTests.LIST_DATA));
        Assert.assertTrue(ArraySetTests.genAS().containsAll(
            new ArraySet<Integer>()));
        
        ArraySet<Integer> as = ArraySetTests.genAS();
        as.addAll(ArraySetTests.LIST_DATA_2);
        Assert.assertTrue(as.containsAll(ArraySetTests.genAS()));
    }
    
    @Test
    public void testValidRemove() {
        ArraySet<Integer> as = ArraySetTests.genAS();
        
        Assert.assertTrue(as.remove(2));
        Assert.assertFalse(as.remove(2));
        Assert.assertFalse(as.remove(new Point(2, 3)));
        Assert.assertFalse(as.remove(new Uncomparable()));
    }
    
    @Test
    public void testValidRemoveAll() {
        ArraySet<Integer> as = ArraySetTests.genAS();
        
        Assert.assertTrue(as.removeAll(ArraySetTests.genAS()));
        Assert.assertFalse(as.removeAll(ArraySetTests.genAS()));
        Assert.assertFalse(as.removeAll(ArraySetTests.LIST_DATA_2));
    }
    
    @Test
    public void testValidRetainAll() {
        ArraySet<Integer> as = ArraySetTests.genAS();
        
        Assert.assertFalse(as.retainAll(ArraySetTests.LIST_DATA));
        Assert.assertTrue(as.retainAll(ArraySetTests.LIST_DATA_2));
        Assert.assertFalse(as.retainAll(ArraySetTests.LIST_DATA));
    }
    
    private static ArraySet<Integer> genAS() {
        return new ArraySet<>(ArraySetTests.LIST_DATA);
    }
    
    private static Integer[] generateRawData(int start) {
        Integer[] raw = new Integer[100];
        for (int i = start; i < 200; i += 2)
            raw[i / 2] = i;
        
        return raw;
    }
    
    private class Uncomparable {}
}
