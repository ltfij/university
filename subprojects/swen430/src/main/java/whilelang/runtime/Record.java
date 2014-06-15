package whilelang.runtime;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * A record. A record is a set of {@code str -> obj} values.
 *
 * @author Henry J. Wylde
 */
public final class Record {

    private final Map<String, Object> data = new TreeMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return data.equals(((Record) o).data);
    }

    /**
     * Gets the object at the given key.
     *
     * @param key the key.
     * @return the object.
     */
    public Object get(String key) {
        return data.get(key);
    }

    /**
     * Gets all the data of this record.
     *
     * @return the underlying map data of this record.
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return data.hashCode();
    }

    /**
     * Associates the given value with the given key.
     *
     * @param key the key.
     * @param value the new value.
     */
    public void put(String key, Object value) {
        data.put(key, value);
    }

    /**
     * Copies over all the data of the given record to this record.
     *
     * @param record the record to copy from.
     */
    public void putAll(Record record) {
        data.putAll(record.data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");

        for (Iterator<Map.Entry<String, Object>> it = data.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> next = it.next();

            sb.append(next.getKey());
            sb.append(":");
            sb.append(next.getValue());

            if (it.hasNext()) {
                sb.append(",");
            }
        }

        return sb.append("}").toString();
    }
}
