package com.example.JWPatterns;

import android.graphics.Color;

public class StarParameters {
    int m_n1 = 5;                       ///< Number of points in the circle
    int m_n2 = 2;                       ///< Increment when joining points
    double m_rotate_degrees = 0;        ///< Rotation when drawing multiple images
    double m_shrink = 0.95;             ///< Shrinkage when drawing multiple images
    int m_background = Color.WHITE;     ///< Background colour
    int m_first_line = Color.BLACK;     ///< Foreground ground colour
    int m_last_line = Color.BLACK;      ///< Second foreground ground colour when blending
}
