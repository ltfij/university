package com.hjwylde.uni.comp422.project01.common.math;

import static com.hjwylde.uni.comp422.project01.common.math.Util.toGreyScale;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Documentation.
 *
 * @author Henry J. Wylde
 */
public final class Feature {

    public static final List<Point> FEATURES = new ArrayList<Point>() {{
        for (int[] point : new int[][] {{3, 5}, {3, 6}, {4, 5}, {4, 6}, {15, 5}, {15, 6}, {16, 5},
                {16, 6}, {8, 11}, {9, 11}, {2, 11}, {2, 12}, {3, 11}, {3, 12}, {16, 11}, {16, 12},
                {17, 11}, {17, 12}, {8, 15}, {9, 15}, {10, 15}, {11, 15}, {8, 17}, {9, 17},
                {10, 17}, {11, 17}, {3, 17}, {4, 17}, {14, 17}, {15, 17}}) {
            add(new Point(point[0], point[1]));
        }
    }};

    /**
     * This class cannot be instantiated.
     */
    private Feature() {}

    public static int extract(BufferedImage source, Point feature) {
        return toGreyScale(source.getRGB(feature.x, feature.y));
    }

    public static int[] extract(BufferedImage source, List<Point> features) {
        int[] values = new int[features.size()];

        for (int i = 0; i < features.size(); i++) {
            values[i] = extract(source, features.get(i));
        }

        return values;
    }
}
