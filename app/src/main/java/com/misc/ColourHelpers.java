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
}
