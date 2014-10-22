package com.hjwylde.uni.comp422.project01.question02_1;

import static com.google.common.base.Preconditions.checkNotNull;

import com.hjwylde.uni.comp422.project01.common.math.Image;
import com.hjwylde.uni.comp422.project01.common.math.Kernel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * TODO: Documentation.
 *
 * @author Henry J. Wylde
 */
public final class Main {

    private static final String FORMAT = "png";

    private static final String SOURCE_IMAGE_PATH = "/project01/question02_1/hubble." + FORMAT;
    private static final String DESTINATION_IMAGE_PATH =
            "out/project01/question02_1/hubble." + FORMAT;

    private static final BufferedImage IMAGE;

    static {
        try {
            InputStream in = checkNotNull(Main.class.getResourceAsStream(SOURCE_IMAGE_PATH),
                    "in cannot be null");
            IMAGE = checkNotNull(ImageIO.read(in), "image cannot be null");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This class cannot be instantiated.
     */
    private Main() {}

    public static void main(String[] args) throws IOException {
        File destination = new File(DESTINATION_IMAGE_PATH);
        destination.getParentFile().mkdirs();

        BufferedImage result = IMAGE;

        result = Image.median(result);
        result = Image.convolve(result, Kernel.SMOOTH);
        result = Image.threshold(result, 210);

        ImageIO.write(result, FORMAT, destination);
    }
}
