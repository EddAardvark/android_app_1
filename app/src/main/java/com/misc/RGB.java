package com.misc;

import android.graphics.Color;

/**
 * Package for the components of a colour
 */
public class RGB
{
    public int alpha;
    public int red;
    public int green;
    public int blue;

    /**
     * Construct from the integer representation of a colour
     * @param colour the colour
     */
    public RGB (int colour){

        alpha = (colour >> 24) & 0xFF;
        red = (colour >> 16) & 0xFF;
        green = (colour >> 8) & 0xFF;
        blue = colour & 0xFF;
    }

    /**
     * Construct from the 4 separate channels
     * @param a Alpha
     * @param r Red
     * @param g Green
     * @param b Blue
     */
    public RGB (int a, int r, int g, int b){

        alpha = a & 0xFF;
        red = r & 0xFF;
        green = g & 0xFF;
        blue = b & 0xFF;
    }
    /**
     * Construct from the three colour channels
     * @param r Red
     * @param g Green
     * @param b Blue
     */
    public RGB (int r, int g, int b){

        alpha = 0xFF;
        red = r & 0xFF;
        green = g & 0xFF;
        blue = b & 0xFF;
    }
    /**
     * Convert to the integer representation of the colour
     * @return The colour
     */
    public int toColour ()
    {
        return Color.argb(alpha, red, green, blue);
    }
    /**
     * Returns the grey level of this RGB instance. Numbers taken from wikipedia
     * @return a number in the range 0 - 255.
     */
    public int greyLevel () { return (int) Math.round (0.2126 * red + 0.7152 * green + 0.0722 * blue); }
    /**
     * Get the grey level for the integer representation of a colour
     * @param colour The colour
     * @return The grey level
     */
    public static int getGreyLevel (int colour)
    {
        return new RGB (colour).greyLevel();
    }
    /**
     * Convert this colour to grey.
     * @return a new RGB instance representing the current instance in grey scale.
     */
    public RGB toGrey ()
    {
        int x = greyLevel();
        return new RGB (x, x, x);
    }
}
