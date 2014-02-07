package com.hjwylde.uni.swen304.project02.db.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Useful methods for printing out various database objects / relations in a pretty manner.
 * 
 * @author Henry J. Wylde
 * @version 1.0.0
 * 
 * @since 1.0.0, 12/10/2013
 */
public final class PrettyPrinter {

    private static final String INDENT = "    ";

    /**
     * This class cannot be instantiated.
     */
    private PrettyPrinter() {}

    public static <A, B> String print(Map<A, List<B>> map) {
        StringBuilder sb = new StringBuilder();

        for (Entry<A, List<B>> e : map.entrySet()) {
            sb.append(e.getKey());

            for (B b : e.getValue()) {
                sb.append("\n").append(INDENT);
                sb.append(b);
            }

            sb.append("\n");
        }

        // If we have added some text, delete the last newline
        if (sb.length() != 0)
            sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
