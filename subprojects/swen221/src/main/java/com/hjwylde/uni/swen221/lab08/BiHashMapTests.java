package com.hjwylde.uni.swen221.lab08;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/*
 * Code for Laboratory 8, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class BiHashMapTests {

    @Test
    public void testClear() {
        BiHashMap<String, String> map = new BiHashMap<>();
        map.put("Hello", "World");
        map.clear();
        Assert.assertTrue(map.get("Hello") == null);
        Assert.assertTrue(map.getKeys("World") == null);
        Assert.assertFalse(map.containsKey("Hello"));
        Assert.assertFalse(map.containsValue("World"));
    }

    @Test
    public void testEntrySet() {
        String[][] data = { {"Hello", "World"}, {"Dave", "World"}, {"Something", "Else"}};

        HashMap<String, String> omap = new HashMap<>();
        HashMap<String, Set<String>> rmap = new HashMap<>();

        for (String[] p : data) {
            omap.put(p[0], p[1]);
            Set<String> r = rmap.get(p[1]);
            if (r == null) {
                r = new HashSet<>();
                rmap.put(p[1], r);
            }
            r.add(p[0]);
        }

        BiHashMap<String, String> map = new BiHashMap<>();
        map.putAll(omap);
        for (Map.Entry<String, String> e : map.entrySet()) {
            Assert.assertTrue(omap.get(e.getKey()).equals(e.getValue()));
            // Following line needed to convert set returned by getKeys() into a
            // HashSet for the comparison to work.
            HashSet<String> keys = new HashSet<>(map.getKeys(e.getValue()));
            Assert.assertTrue(rmap.get(e.getValue()).equals(keys));
        }
    }

    @Test
    public void testPut() {
        BiHashMap<String, String> map = new BiHashMap<>();
        map.put("Hello", "World");
        Assert.assertTrue(map.get("Hello").equals("World"));
        Assert.assertTrue(map.getKeys("World").size() == 1);
        Assert.assertTrue(map.getKeys("World").contains("Hello"));
        Assert.assertTrue(map.containsKey("Hello"));
        Assert.assertTrue(map.containsValue("World"));
    }

    @Test
    public void testPutAll() {
        HashMap<String, String> omap = new HashMap<>();
        omap.put("Hello", "World");
        omap.put("Dave", "World");
        omap.put("Something", "Else");

        BiHashMap<String, String> map = new BiHashMap<>();
        map.putAll(omap);

        for (Map.Entry<String, String> e : omap.entrySet())
            Assert.assertTrue(map.get(e.getKey()).equals(e.getValue()));

        Assert.assertTrue(map.getKeys("World").size() == 2);
        Assert.assertTrue(map.getKeys("World").contains("Hello"));
        Assert.assertTrue(map.getKeys("World").contains("Dave"));
        Assert.assertTrue(map.getKeys("Else").size() == 1);
        Assert.assertTrue(map.getKeys("Else").contains("Something"));
    }

    @Test
    public void testRemove() {
        BiHashMap<String, String> map = new BiHashMap<>();
        map.put("Hello", "World");
        map.remove("Hello");
        Assert.assertTrue(map.get("Hello") == null);
        Assert.assertTrue(map.getKeys("World").size() == 0);
        Assert.assertFalse(map.containsKey("Hello"));
        Assert.assertFalse(map.containsValue("World"));
    }
}
