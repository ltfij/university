package com.hjwylde.uni.comp422.project01.question02_2;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.primitives.Ints.asList;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * TODO: Documentation.
 *
 * @author Henry J. Wylde
 */
public final class Datum {

    private final String name;
    private final ImmutableList<Integer> features;

    private final boolean clazz;

    public Datum(String name, boolean clazz, List<Integer> features) {
        this.name = checkNotNull(name, "name cannot be null");
        this.features = ImmutableList.copyOf(features);

        this.clazz = clazz;
    }

    public Datum(String name, boolean clazz, int... features) {
        this(name, clazz, asList(features));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Datum datum = (Datum) o;

        if (!name.equals(datum.name)) {
            return false;
        }

        return true;
    }

    public boolean getClazz() {
        return clazz;
    }

    public ImmutableList<Integer> getFeatures() {
        return features;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Datum{" +
                "name='" + name + '\'' +
                ", features=" + features +
                ", clazz=" + clazz +
                '}';
    }
}
