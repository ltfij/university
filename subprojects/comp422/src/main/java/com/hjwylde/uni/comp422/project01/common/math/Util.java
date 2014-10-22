package com.hjwylde.uni.comp422.project01.common.math;

import java.awt.Color;

/**
 * TODO: Documentation.
 *
 * @author Henry J. Wylde
 */
public final class Util {

    /**
     * This class cannot be instantiated.
     */
    private Util() {}

    public static int clamp(int value, int min, int max) {
        return Math.min(max, Math.max(min, value));
    }

    public static int rgb(int r, int g, int b) {
        return new Color(clamp(r, 0, 255), clamp(g, 0, 255), clamp(b, 0, 255)).getRGB();
    }

    public static int toGreyScale(Color rgb) {
        return clamp((rgb.getRed() + rgb.getGreen() + rgb.getBlue()) / 3, 0, 255);
    }

    public static int toGreyScale(int rgb) {
        return toGreyScale(new Color(rgb));
    }

    public static boolean within(int i, int min, int max) {
        return i >= min && i < max;
    }
}
