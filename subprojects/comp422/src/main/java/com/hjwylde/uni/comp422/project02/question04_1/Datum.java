package com.hjwylde.uni.comp422.project02.question04_1;

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

    private final boolean clazz;

    public Datum(boolean clazz, List<Double> features) {
        this.features = ImmutableList.copyOf(features);

        this.clazz = clazz;
    }

    public Datum(boolean clazz, Double... features) {
        this(clazz, asList(features));
    }

    public boolean getClazz() {
        return clazz;
    }

    public ImmutableList<Double> getFeatures() {
        return features;
    }
}
