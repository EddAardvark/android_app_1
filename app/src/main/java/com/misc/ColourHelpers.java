package com.misc;

import android.graphics.Color;

public class ColourHelpers {
    /**
     * Returns a colour that should contrast with the niput colour. Intended for creating labels for coloured backgrounds.
     * @param red the red component (0-255)
     * @param green the green component (0-255)
     * @param blue the blue component (0-255)
     * @return The contrasting colour, currently black or white.
     */
    public static int GetContrastColour(int red, int green, int blue){

        return (Math.max (Math.max (red, green), blue)) > 128 ? Color.BLACK : Color.WHITE;
    }
    /**
     * Returns a colour that should contrast with the niput colour. Intended for creating labels for coloured backgrounds.
     * @param colour
     * @return The contrasting colour, currently black or white.
     */
    public static int GetContrastColour(int colour){

        int red = (colour >> 16) & 0xFF;
        int green = (colour >> 8) & 0xFF;
        int blue = colour & 0xFF;

        return GetContrastColour(red, green, blue);
    }
    public static int GetContrastColour(RGB rgb){
        return GetContrastColour(rgb.red, rgb.green, rgb.blue);
    }

    /**
     * Mix two colours
     * @param c1 the first colour
     * @param c2 the second colour
     * @param f the mixing factor (c = f * c1 + (1-f) * c2), 0 <= f <= 1.
     * @return the mixed colour
     */
    public static int Blend(int c1, int c2, double f){

        RGB r1 = new RGB (c1);
        RGB r2 = new RGB (c2);
        double f2 = 1 - f;

        int a = (int) Math.round(r1.alpha * f + r2.alpha * f2);
        int r = (int) Math.round(r1.red * f + r2.red * f2);
        int g = (int) Math.round(r1.green * f + r2.green * f2);
        int b = (int) Math.round(r1.blue * f + r2.blue * f2);

        return Color.argb(a, r, g, b);
    }
}