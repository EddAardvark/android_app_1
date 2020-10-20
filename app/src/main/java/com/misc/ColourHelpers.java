package com.misc;

import android.graphics.Color;

import java.util.Random;

public class ColourHelpers {

    /**
     * Returns a colour that should contrast with the input colour. Intended for creating labels for coloured backgrounds.
     * @param colour A colour.
     * @return The contrasting colour, currently black or white.
     */
    public static int GetContrastColour(int colour) {
        return (RGB.getGreyLevel(colour) > 128) ? Color.BLACK : Color.WHITE;
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
    /**
     * Return a random solid colour (alpha = 255)
     * @return the colour as an integer.
     */
    public static int random_solid_colour() {

        Random rand = new Random();

        int a = 255;
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);

        return Color.argb(a, r, g, b);
    }

    /**
     * Make a colour transparent
     * @param colour The colour
     * @param transparency The transparency (1 = fully, 0 = opaque)
     * @return The new colour
     */
    public static int MakeTransparent (int colour, double transparency)
    {
        RGB rgb = new RGB (colour);
        rgb.alpha = (int) Math.round(255.0 * (1.0-transparency));

        return rgb.toColour();
    }
}
