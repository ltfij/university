package com.hjwylde.uni.comp422.project01.common.math;

import javax.vecmath.Matrix3d;

/**
 * TODO: Documentation.
 *
 * @author Henry J. Wylde
 */
public final class Kernel {

    public static final Matrix3d ID = new Matrix3d(0, 0, 0, 0, 1, 0, 0, 0, 0);
    public static final Matrix3d BRIGHTNESS_1 = new Matrix3d(0, 0.5, 0, 0.5, 1, 0.5, 0, 0.5, 0);
    public static final Matrix3d SMOOTH = new Matrix3d(1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0,
            1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0);
    public static final Matrix3d SOBEL_OPERATOR_X = new Matrix3d(-1, 0, 1, -2, 0, 2, -1, 0, 1);
    public static final Matrix3d SOBEL_OPERATOR_Y = new Matrix3d(1, 2, 1, 0, 0, 0, -1, -2, -1);
    public static final Matrix3d LAPLACIAN_OPERATOR_1 = new Matrix3d(1, -2, 1, -2, 5, -2, 1, -2, 1);
    public static final Matrix3d LAPLACIAN_OPERATOR_2 = new Matrix3d(0, -1, 0, -1, 5, -1, 0, -1, 0);
    public static final Matrix3d LAPLACIAN_OPERATOR_3 = new Matrix3d(-1, -1, -1, -1, 8, -1, -1, -1,
            -1);

    /**
     * This class cannot be instantiated.
     */
    private Kernel() {}
}
