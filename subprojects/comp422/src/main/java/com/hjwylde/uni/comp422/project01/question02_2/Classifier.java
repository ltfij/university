package com.hjwylde.uni.comp422.project01.question02_2;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Sets.filter;
import static java.lang.Math.PI;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import com.hjwylde.uni.comp422.project01.common.math.Feature;
import com.hjwylde.uni.comp422.project01.common.util.Pair;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO: Documentation.
 *
 * @author Henry J. Wylde
 */
public final class Classifier {

    // Maps of points to pairs (mean, variance)
    private final Map<Integer, Pair<Double, Double>> trueFeatures;
    private final Map<Integer, Pair<Double, Double>> falseFeatures;

    private double evidence = 0.5;

    private Classifier(Map<Integer, Pair<Double, Double>> trueFeatures,
            Map<Integer, Pair<Double, Double>> falseFeatures) {
        this.trueFeatures = trueFeatures;
        this.falseFeatures = falseFeatures;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean classify(Datum datum) {
        return calculate(datum, trueFeatures) > calculate(datum, falseFeatures);
    }

    public void setEvidence(double evidence) {
        checkArgument(evidence >= 0);
        checkArgument(evidence <= 1);

        this.evidence = evidence;
    }

    private double calculate(Datum datum, Map<Integer, Pair<Double, Double>> features) {
        List<Double> ps = new ArrayList<>();

        for (Map.Entry<Integer, Pair<Double, Double>> entry : features.entrySet()) {
            int index = entry.getKey();
            double mean = entry.getValue().first();
            double variance = entry.getValue().second();

            double p = 1.0 / sqrt(2.0 * PI * variance);
            p *= exp(-pow(datum.getFeatures().get(index) - mean, 2) / (2.0 * variance));

            ps.add(p);
        }

        double result = features == trueFeatures ? evidence : 1 - evidence;

        for (double p : ps) {
            result *= p;
        }

        return result;
    }

    public static final class Builder {

        private final Set<Datum> data = new HashSet<>();

        public Classifier build() {
            // True features
            Map<Integer, Pair<Double, Double>> trueFeatures = new HashMap<>();
            for (int i = 0; i < Feature.FEATURES.size(); i++) {
                final int j = i;

                Iterable<Integer> values = transform(filter(data, new Predicate<Datum>() {
                    @Override
                    public boolean apply(Datum input) {
                        return input.getClazz();
                    }
                }), new Function<Datum, Integer>() {
                    @Override
                    public Integer apply(Datum input) {
                        return input.getFeatures().get(j);
                    }
                });

                double mean = 0;
                for (int value : values) {
                    mean += value;
                }

                mean /= size(values);

                double variance = 0;
                for (int value : values) {
                    variance += pow(value - mean, 2);
                }

                variance /= size(values);

                trueFeatures.put(i, new Pair<>(mean, variance));
            }

            // False features
            Map<Integer, Pair<Double, Double>> falseFeatures = new HashMap<>();
            for (int i = 0; i < Feature.FEATURES.size(); i++) {
                final int j = i;

                Iterable<Integer> values = transform(filter(data, new Predicate<Datum>() {
                    @Override
                    public boolean apply(Datum input) {
                        return !input.getClazz();
                    }
                }), new Function<Datum, Integer>() {
                    @Override
                    public Integer apply(Datum input) {
                        return input.getFeatures().get(j);
                    }
                });

                double mean = 0;
                for (int value : values) {
                    mean += value;
                }

                mean /= size(values);

                double variance = 0;
                for (int value : values) {
                    variance += pow(value - mean, 2);
                }

                variance /= size(values);

                falseFeatures.put(i, new Pair<>(mean, variance));
            }

            return new Classifier(trueFeatures, falseFeatures);
        }

        public Builder train(Collection<? extends Datum> data) {
            checkArgument(!data.contains(null));

            this.data.addAll(data);
            return this;
        }

        public Builder train(Datum datum) {
            data.add(checkNotNull(datum));
            return this;
        }
    }
}
