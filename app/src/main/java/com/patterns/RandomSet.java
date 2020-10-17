package com.patterns;

import android.os.Bundle;

/**
 * Settings used to randomise the pattern
 */
public class RandomSet {

    final String KEY_RANDOMISE_NUM_POINTS = "r1";
    final String KEY_RANDOMISE_POINT_STEP = "r2";
    final String KEY_RANDOMISE_ANGLES = "r3";
    final String KEY_RANDOMISE_SHRINKAGE = "r4";
    final String KEY_RANDOMISE_FORGROUND_COLOURS = "r5";
    final String KEY_RANDOMISE_BACKROUND_COLOURS = "r6";
    final String KEY_RANDOMISE_COLOUR_MODE = "r7";
    final String KEY_RANDOMISE_REPEATS = "r8";

    boolean m_randomise_num_points = true;
    boolean m_randomise_point_step = true;
    boolean m_randomise_repeats = false;
    boolean m_randomise_angles = false;
    boolean m_randomise_shrinkage = false;
    boolean m_randomise_forground_colours = false;
    boolean m_randomise_backround_colours = false;
    boolean m_randomise_colour_mode = false;

    /**
     * Save the current values to a bundle
     */
    public Bundle toBundle() {
        Bundle b = new Bundle();

        b.putBoolean(KEY_RANDOMISE_NUM_POINTS, m_randomise_num_points);
        b.putBoolean(KEY_RANDOMISE_POINT_STEP, m_randomise_point_step);
        b.putBoolean(KEY_RANDOMISE_REPEATS, m_randomise_repeats);
        b.putBoolean(KEY_RANDOMISE_ANGLES, m_randomise_angles);
        b.putBoolean(KEY_RANDOMISE_SHRINKAGE, m_randomise_shrinkage);
        b.putBoolean(KEY_RANDOMISE_FORGROUND_COLOURS, m_randomise_forground_colours);
        b.putBoolean(KEY_RANDOMISE_BACKROUND_COLOURS, m_randomise_backround_colours);
        b.putBoolean(KEY_RANDOMISE_COLOUR_MODE, m_randomise_colour_mode);

        return b;
    }


    /**
     * Update this instance from a bundle
     * @param b the bundle
     */
    public void fromBundle (Bundle b){

        if (b != null) {
            m_randomise_num_points = b.getBoolean(KEY_RANDOMISE_NUM_POINTS, m_randomise_num_points);
            m_randomise_point_step = b.getBoolean(KEY_RANDOMISE_POINT_STEP, m_randomise_point_step);
            m_randomise_repeats = b.getBoolean(KEY_RANDOMISE_REPEATS, m_randomise_repeats);
            m_randomise_angles = b.getBoolean(KEY_RANDOMISE_ANGLES, m_randomise_angles);
            m_randomise_shrinkage = b.getBoolean(KEY_RANDOMISE_SHRINKAGE, m_randomise_shrinkage);
            m_randomise_forground_colours = b.getBoolean(KEY_RANDOMISE_FORGROUND_COLOURS, m_randomise_forground_colours);
            m_randomise_backround_colours = b.getBoolean(KEY_RANDOMISE_BACKROUND_COLOURS, m_randomise_backround_colours);
            m_randomise_colour_mode = b.getBoolean(KEY_RANDOMISE_COLOUR_MODE, m_randomise_colour_mode);
        }
    }
}
