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

    public RGB (int colour){

        alpha = (colour >> 24) & 0xFF;
        red = (colour >> 16) & 0xFF;
        green = (colour >> 8) & 0xFF;
        blue = colour & 0xFF;
    }

    public int toColour ()
    {
        return Color.argb(alpha, red, green, blue);
    }
}
