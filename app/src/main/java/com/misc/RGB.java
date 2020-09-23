package com.misc;

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

}
