package com.hjwylde.uni.swen222.assignment02.cluedo.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

/**
 * A path is essentially a linked list that has a comparator based on size. A path is used for the
 * Dijkstra's search algorithm in graph searching.
 * 
 * @author Henry J. Wylde
 * 
 * @param <T> the type of node in this path.
 * 
 * @since 6/08/2013
 */
public final class Path<T> implements Comparable<Path<T>>, Iterable<T> {

    private final T node;
    private final Path<T> link;

    /**
     * Creates a new <code>Path</code> with the given node.
     * 
     * @param node the node.
     */
    public Path(T node) {
        this.node = Objects.requireNonNull(node);
        this.link = null;
    }

    /**
     * Creates a new <code>Path</code> with the given node and previous link.
     * 
     * @param node the node.
     * @param link the previous link.
     */
    public Path(T node, Path<T> link) {
        this.node = Objects.requireNonNull(node);
        this.link = Objects.requireNonNull(link);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Path<T> other) {
        return length() - other.length();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Path<?>))
            return false;

        Path<?> path = (Path<?>) obj;

        return Objects.equals(node, path.node) && Objects.equals(link, path.link);
    }

    /**
     * Attempts to get the previous link for this path if it exists.
     * 
     * @return the previous link.
     */
    public Optional<Path<T>> getLink() {
        return Optional.fromNullable(link);
    }

    /**
     * Gets the node for this path.
     * 
     * @return the node.
     */
    public T getNode() {
        return node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(node, link);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private List<T> links = initLinks();

            @Override
            public boolean hasNext() {
                return !links.isEmpty();
            }

            @Override
            public T next() {
                return links.remove(0);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private List<T> initLinks() {
                LinkedList<T> links = new LinkedList<>();
                for (Path<T> cur = Path.this; cur != null; cur = cur.link)
                    links.addFirst(cur.node);

                return links;
            }
        };
    }

    /**
     * Calculates the length of this path. Note that a path may never have a length of 0.
     * 
     * @return the length of this path.
     */
    public int length() {
        return 1 + (link == null ? 0 : link.length());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return Iterables.toString(this);
    }
}
