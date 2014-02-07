package com.hjwylde.uni.swen221.lab08;

import java.util.*;

/*
 * Code for Laboratory 8, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class BiHashMap<K, V> implements Map<K, V> {

    private HashMap<K, V> keyToValues = new HashMap<>();
    private HashMap<V, List<K>> valueToKeys = new HashMap<>();

    /*
     * @see java.util.Map#clear()
     */
    @Override
    public void clear() {
        keyToValues.clear();
        valueToKeys.clear();
    }

    /*
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(Object key) {
        return keyToValues.containsKey(key);
    }

    /*
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    @Override
    public boolean containsValue(Object value) {
        List<K> keys = getKeys(value);

        return keys == null ? false : keys.size() > 0;
    }

    /**
     * Returns a collection view of the mappings contained in this map. Each element in the returned
     * collection is a Map.Entry. The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa. The collection supports element removal, which
     * removes the corresponding mapping from the map, via the Iterator.remove, Collection.remove,
     * removeAll, retainAll, and clear operations. It does not support the add or addAll operations.
     * 
     * @return the set of all entries.
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        return keyToValues.entrySet();
    }

    /**
     * Returns the value to which the specified key is mapped in this identity hash map, or null if
     * the map contains no mapping for this key. A return value of null does not necessarily
     * indicate that the map contains no mapping for the key; it is also possible that the map
     * explicitly maps the key to null. The containsKey method may be used to distinguish these two
     * cases.
     * 
     * @param key the key.
     * @return the value associated with key.
     */
    @Override
    public V get(Object key) {
        return keyToValues.get(key);
    }

    /**
     * Get the set of keys associated with a particular value
     * 
     * @param value the value to get the keys for.
     * @return the list of keys for the given value.
     */
    public List<K> getKeys(Object value) {
        return valueToKeys.get(value);
    }

    /**
     * Returns the hash code value for this map. The hash code of a map is defined to be the sum of
     * the hashCodes of each entry in the map's entrySet view. This ensures that t1.equals(t2)
     * implies that t1.hashCode()==t2.hashCode() for any two maps t1 and t2, as required by the
     * general contract of Object.hashCode.
     */
    @Override
    public int hashCode() {
        return keyToValues.hashCode();
    }

    /**
     * Returns true if this map contains no key-value mappings.
     * 
     * @return true if the map is empty.
     */
    @Override
    public boolean isEmpty() {
        return keyToValues.isEmpty();
    }

    /**
     * Returns a set view of the keys contained in this map. The set is backed by the map, so
     * changes to the map are reflected in the set, and vice-versa. If the map is modified while an
     * iteration over the set is in progress (except through the iterator's own remove operation),
     * the results of the iteration are undefined. The set supports element removal, which removes
     * the corresponding mapping from the map, via the Iterator.remove, Set.remove, removeAll
     * retainAll, and clear operations. It does not support the add or addAll operations.
     */
    @Override
    public Set<K> keySet() {
        return keyToValues.keySet();
    }

    /**
     * Associates the specified value with the specified key in this map. If the map previously
     * contained a mapping for this key, the old value is replaced.
     */
    @Override
    public V put(K key, V value) {
        V rv = remove(key); // Remove the previous key and keep a reference to it.

        keyToValues.put(key, value);

        // Update the set of keys associated with this value.
        List<K> keys = valueToKeys.get(value);
        if (keys == null) {
            keys = new ArrayList<>();
            valueToKeys.put(value, keys);
        }

        keys.add(key);

        // Return the original mapping in order to adhere to Map interface.
        return rv;
    }

    /**
     * Copies all of the mappings from the specified map to this map (optional operation). The
     * effect of this call is equivalent to that of calling put(k, v) on this map once for each
     * mapping from key k to value v in the specified map. The behavior of this operation is
     * unspecified if the specified map is modified while the operation is in progress.
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }

    /**
     * Removes the mapping for this key from this map if present.
     */
    @Override
    public V remove(Object key) {
        V value = keyToValues.remove(key); // Remove the key from the map.

        if (value == null)
            return null;

        // Update the set of keys associated with this value.
        List<K> keys = valueToKeys.get(value);
        keys.remove(key);

        // Return the original mapping in order to ad here to Map interface.
        return value;
    }

    /**
     * Returns the number of key-value mappings in this map.
     * 
     * @return the number of keys.
     */
    @Override
    public int size() {
        return keyToValues.size();
    }

    /**
     * Returns a collection view of the values contained in this map. The collection is backed by
     * the map, so changes to the map are reflected in the collection, and vice-versa. If the map is
     * modified while an iteration over the collection is in progress (except through the iterator's
     * own remove operation), the results of the iteration are undefined. The collection supports
     * element removal, which removes the corresponding mapping from the map, via the
     * Iterator.remove, Collection.remove, removeAll, retainAll and clear operations. It does not
     * support the add or addAll operations.
     * 
     * @return the collection of values.
     */
    @Override
    public Collection<V> values() {
        return keyToValues.values();
    }

    /**
     * This is some sample code to illustrate the current usage.
     * 
     * @param args args.
     */
    public static void main(String[] args) {
        BiHashMap<String, String> map = new BiHashMap<>();

        map.put("Dave", "ENGR202");
        map.put("Alex", "COMP205");
        map.put("James", "ENGR202");

        for (String x : map.getKeys("ENGR202"))
            System.out.println("GOT: " + x);
    }
}
