package com.hjwylde.uni.swen222.assignment02.cluedo.util;

import java.util.Objects;

/**
 * A triple is an immutable tuple of 3 elements. The elements may have different types.
 * 
 * @author Henry J. Wylde
 * 
 * @param <F> the type of the first element.
 * @param <S> the type of the second element.
 * @param <T> the type of the third element.
 * 
 * @since 7/05/2013
 */
public class Triple<F, S, T> extends Pair<F, S> {

    /**
     * The third element.
     */
    protected final T third;

    /**
     * @param first the first element.
     * @param second the second element.
     * @param third the third element.
     */
    public Triple(F first, S second, T third) {
        super(first, second);

        this.third = Objects.requireNonNull(third);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) obj;

        return Objects.equals(third, triple.third);
    }

    /**
     * Gets the third element.
     * 
     * @return the third element.
     */
    public T getThird() {
        return third;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), third);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }
}
