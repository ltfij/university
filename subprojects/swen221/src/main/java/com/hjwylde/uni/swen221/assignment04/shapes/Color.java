package com.hjwylde.uni.swen221.assignment04.shapes;

/*
 * Code for Assignment 4, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class Color {

    private int red;
    private int green;
    private int blue;

    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color BLACK = new Color(0, 0, 0);

    /**
     * Construct a Color object explicitly from the three color components. Note that these must
     * have values between 0 and 255.
     * 
     * @param red the red value.
     * @param green the green value.
     * @param blue the blue value.
     */
    public Color(int red, int green, int blue) {
        if ((red < 0) || (red > 255))
            throw new IllegalArgumentException("red component must take value between 0 and 255");
        if ((green < 0) || (green > 255))
            throw new IllegalArgumentException("green component must take value between 0 and 255");
        if ((blue < 0) || (blue > 255))
            throw new IllegalArgumentException("blue component must take value between 0 and 255");
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Construct a color object from a 6 digit hexadecimal number string of the form "#rrggbb".
     * Here, "rr" corresponds to the red component, "gg" the green component and so on. Each takes a
     * value in the range 00..ff and, hence, there are 256 possibilties for each component.
     * 
     * @param hex the color in hexadecimal notation.
     */
    public Color(String hex) {
        if ((hex.length() != 7) || (hex.charAt(0) != '#'))
            throw new IllegalArgumentException("hex string requires # followed by 6 hex digits");

        // Now, check all digits are indeed hex digits.
        for (int i = 1; i != hex.length(); ++i) {
            char digit = hex.charAt(i);
            if (!Character.isDigit(digit) && (digit != 'a') && (digit != 'b') && (digit != 'c')
                    && (digit != 'd') && (digit != 'e') && (digit != 'f'))
                throw new IllegalArgumentException("hex string requires # followed by 6 hex digits");
        }

        String red = hex.substring(1, 3);
        String green = hex.substring(3, 5);
        String blue = hex.substring(5, 7);

        this.red = Integer.parseInt(red, 16);
        this.blue = Integer.parseInt(blue, 16);
        this.green = Integer.parseInt(green, 16);
    }

    /**
     * Return the blue component of this color.
     */
    public int blue() {
        return blue;
    }

    /**
     * Return the green component of this color.
     */
    public int green() {
        return green;
    }

    /**
     * Return the red component of this color.
     */
    public int red() {
        return red;
    }

    public int toRGB() {
        return ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
    }

    /**
     * Convert this color into a six digit hexacimal string of the form "#rrggbb"
     */
    @Override
    public String toString() {
        return "#" + Color.hexDigit(red) + Color.hexDigit(green) + Color.hexDigit(blue);
    }

    public static Color combine(Color c1, Color c2) {
        return new Color(Color.combine(c1.red, c2.red), Color.combine(c1.green, c2.green),
                Color.combine(c1.blue, c2.blue));
    }

    private static int combine(int i1, int i2) {
        return Math.min((int) ((i1 + i2) * 0.8), 255);
    }

    private static String hexDigit(int c) {
        String r = Integer.toHexString(c);
        if (r.length() < 2)
            r = "0" + r;
        return r;
    }
}
