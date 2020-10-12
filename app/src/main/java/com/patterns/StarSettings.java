package com.patterns;

import android.os.Bundle;

import com.dialogs.GetInteger;
import com.dialogs.ManageStarSettings;
import com.example.tutorialapp.R;

import java.io.Serializable;

public class StarSettings {

    final String KEY_BITMAP_SIZE = "x1";
    final String KEY_COLOURING_MODE = "x2";

    final String KEY_RANDOMISE_NUM_POINTS = "y1";
    final String KEY_RANDOMISE_POINT_STEP = "y2";
    final String KEY_RANDOMISE_ANGLES = "y3";
    final String KEY_RANDOMISE_SHRINKAGE = "y4";
    final String KEY_RANDOMISE_FORGROUND_COLOURS = "y5";
    final String KEY_RANDOMISE_BACKROUND_COLOURS = "y6";
    final String KEY_RANDOMISE_COLOUR_MODE = "y7";

    enum ColouringMode
    {
        INWARDS, AROUND, BOTH, ALTERNATE
    }

    // Pattern

    public int m_bm_size = 800;
    public ColouringMode m_colouring_mode = ColouringMode.INWARDS;

    // Randomiser

    boolean m_randomise_num_points = true;
    boolean m_randomise_point_step = true;
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

        b.putInt(KEY_BITMAP_SIZE, m_bm_size);
        b.putSerializable(KEY_COLOURING_MODE, m_colouring_mode);

        b.putBoolean(KEY_RANDOMISE_NUM_POINTS, m_randomise_num_points);
        b.putBoolean(KEY_RANDOMISE_POINT_STEP, m_randomise_point_step);
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

        m_bm_size = b.getInt(KEY_BITMAP_SIZE, m_bm_size);
        Serializable x = b.getSerializable(KEY_COLOURING_MODE);

        if (x != null){
            m_colouring_mode = (ColouringMode) x;
        }

         m_randomise_num_points = b.getBoolean(KEY_RANDOMISE_NUM_POINTS, m_randomise_num_points);
         m_randomise_point_step = b.getBoolean(KEY_RANDOMISE_POINT_STEP, m_randomise_point_step);
         m_randomise_angles = b.getBoolean(KEY_RANDOMISE_ANGLES, m_randomise_angles);
         m_randomise_shrinkage = b.getBoolean(KEY_RANDOMISE_SHRINKAGE, m_randomise_shrinkage);
         m_randomise_forground_colours = b.getBoolean(KEY_RANDOMISE_FORGROUND_COLOURS, m_randomise_forground_colours);
         m_randomise_backround_colours = b.getBoolean(KEY_RANDOMISE_BACKROUND_COLOURS, m_randomise_backround_colours);
         m_randomise_colour_mode = b.getBoolean(KEY_RANDOMISE_COLOUR_MODE, m_randomise_colour_mode);
    }
}
