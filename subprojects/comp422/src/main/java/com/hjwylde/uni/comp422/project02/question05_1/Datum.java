package com.hjwylde.uni.comp422.project02.question05_1;

import static java.util.Arrays.asList;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * TODO: Documentation.
 *
 * @author Henry J. Wylde
 */
public final class Datum {

    private final ImmutableList<Double> features;

    private final int clazz;

    public Datum(int clazz, List<Double> features) {
        this.features = ImmutableList.copyOf(features);

        this.clazz = clazz;
    }

    public Datum(int clazz, Double... features) {
        this(clazz, asList(features));
    }

    public int getClazz() {
        return clazz;
    }

    public ImmutableList<Double> getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return "Datum{" +
                "features=" + features +
                ", clazz=" + clazz +
                '}';
    }
}
