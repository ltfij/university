package com.hjwylde.uni.comp422.project01.common.math;

import static com.google.common.collect.Iterables.get;
import static com.hjwylde.uni.comp422.project01.common.math.Util.rgb;
import static com.hjwylde.uni.comp422.project01.common.math.Util.within;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.TreeSet;

import javax.vecmath.Matrix3d;

/**
 * TODO: Documentation.
 *
 * @author Henry J. Wylde
 */
public final class Image {

    /**
     * This class cannot be instantiated.
     */
    private Image() {}

    public static BufferedImage applySobelOperation(BufferedImage source) {
        BufferedImage destination = new BufferedImage(source.getWidth(), source.getHeight(),
                source.getType());

        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                int greyX = applyKernel2(source, Kernel.SOBEL_OPERATOR_X, x, y);
                int greyY = applyKernel2(source, Kernel.SOBEL_OPERATOR_Y, x, y);

                int g = (int) sqrt(pow(greyX, 2) + pow(greyY, 2));

                destination.setRGB(x, y, rgb(g, g, g));
            }
        }

        return destination;
    }

    public static BufferedImage convolve(BufferedImage source, Matrix3d kernel) {
        BufferedImage destination = new BufferedImage(source.getWidth(), source.getHeight(),
                source.getType());

        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                destination.setRGB(x, y, applyKernel(source, kernel, x, y));
            }
        }

        return destination;
    }

    public static BufferedImage median(BufferedImage source) {
        BufferedImage destination = new BufferedImage(source.getWidth(), source.getHeight(),
                source.getType());

        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                destination.setRGB(x, y, median(source, x, y));
            }
        }

        return destination;
    }

    public static BufferedImage subtract(BufferedImage lhs, BufferedImage rhs) {
        BufferedImage destination = new BufferedImage(lhs.getWidth(), lhs.getHeight(),
                lhs.getType());

        for (int x = 0; x < lhs.getWidth(); x++) {
            for (int y = 0; y < lhs.getHeight(); y++) {
                Color lhsColor = new Color(lhs.getRGB(x, y));
                Color rhsColor = new Color(rhs.getRGB(x, y));

                int r = lhsColor.getRed() - rhsColor.getRed();
                int g = lhsColor.getGreen() - rhsColor.getGreen();
                int b = lhsColor.getBlue() - rhsColor.getBlue();

                destination.setRGB(x, y, rgb(r, g, b));
            }
        }

        return destination;
    }

    public static BufferedImage threshold(BufferedImage source, int threshold) {
        return threshold(source, threshold, threshold, threshold);
    }

    public static BufferedImage threshold(BufferedImage source, int rThreshold, int gThreshold,
            int bThreshold) {
        BufferedImage destination = new BufferedImage(source.getWidth(), source.getHeight(),
                source.getType());

        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                Color rgb = new Color(source.getRGB(x, y));

                int r = rgb.getRed() >= rThreshold ? 255 : 0;
                int g = rgb.getGreen() >= gThreshold ? 255 : 0;
                int b = rgb.getBlue() >= bThreshold ? 255 : 0;

                destination.setRGB(x, y, rgb(r, g, b));
            }
        }

        return destination;
    }

    private static int applyKernel(BufferedImage source, Matrix3d kernel, int x, int y) {
        int r = 0;
        int g = 0;
        int b = 0;

        for (int kx = -1; kx <= 1; kx++) {
            for (int ky = -1; ky <= 1; ky++) {
                if (!within(x + kx, 0, source.getWidth())) {
                    continue;
                }
                if (!within(y + ky, 0, source.getHeight())) {
                    continue;
                }

                Color rgb = new Color(source.getRGB(x + kx, y + ky));

                r += rgb.getRed() * kernel.getElement(kx + 1, ky + 1);
                g += rgb.getGreen() * kernel.getElement(kx + 1, ky + 1);
                b += rgb.getBlue() * kernel.getElement(kx + 1, ky + 1);
            }
        }

        return rgb(r, g, b);
    }

    private static int applyKernel2(BufferedImage source, Matrix3d kernel, int x, int y) {
        int g = 0;

        for (int kx = -1; kx <= 1; kx++) {
            for (int ky = -1; ky <= 1; ky++) {
                if (!within(x + kx, 0, source.getWidth())) {
                    continue;
                }
                if (!within(y + ky, 0, source.getHeight())) {
                    continue;
                }

                Color rgb = new Color(source.getRGB(x + kx, y + ky));

                g += rgb.getGreen() * kernel.getElement(kx + 1, ky + 1);
            }
        }

        return g;
    }

    private static int median(BufferedImage source, int x, int y) {
        Set<Integer> rs = new TreeSet<>();
        Set<Integer> gs = new TreeSet<>();
        Set<Integer> bs = new TreeSet<>();

        for (int kx = -1; kx <= 1; kx++) {
            for (int ky = -1; ky <= 1; ky++) {
                if (!within(x + kx, 0, source.getWidth())) {
                    continue;
                }
                if (!within(y + ky, 0, source.getHeight())) {
                    continue;
                }

                Color rgb = new Color(source.getRGB(x + kx, y + ky));

                rs.add(rgb.getRed());
                gs.add(rgb.getGreen());
                bs.add(rgb.getBlue());
            }
        }

        return rgb(get(rs, rs.size() / 2), get(gs, gs.size() / 2), get(bs, bs.size() / 2));
    }
}

