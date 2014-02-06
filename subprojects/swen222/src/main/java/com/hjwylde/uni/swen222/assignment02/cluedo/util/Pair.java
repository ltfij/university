package com.hjwylde.uni.swen222.assignment02.cluedo.util;

import java.util.Objects;

/**
 * A pair is an immutable tuple of 2 elements. The elements may have different types.
 * 
 * @author Henry J. Wylde
 * 
 * @param <F> the type of the first element.
 * @param <S> the type of the second element.
 * 
 * @since 5/12/2012
 */
public class Pair<F, S> {
    
    /**
     * The first element.
     */
    protected final F first;
    /**
     * The second element.
     */
    protected final S second;
    
    /**
     * Creates a new ordered pair of the given two elements.
     * 
     * @param first the first element.
     * @param second the second element.
     */
    public Pair(F first, S second) {
        this.first = Objects.requireNonNull(first);
        this.second = Objects.requireNonNull(second);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (getClass() != obj.getClass()))
            return false;
        
        Pair<?, ?> pair = (Pair<?, ?>) obj;
        
        return Objects.equals(first, pair.first)
            && Objects.equals(second, pair.second);
    }
    
    /**
     * Gets the first element.
     * 
     * @return the first element.
     */
    public F getFirst() {
        return first;
    }
    
    /**
     * Gets the second element.
     * 
     * @return the second element.
     */
    public S getSecond() {
        return second;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}