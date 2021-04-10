package com.patterns;

import android.os.Bundle;

/**
 * Settings used to randomise the pattern
 */
public class TileRandomSet {

    final static String KEY_RANDOMISE_TRIANGLES = "r1";
    final static String KEY_RANDOMISE_BARS = "r2";
    final static String KEY_RANDOMISE_BLOCKS = "r3";
    final static String KEY_RANDOMISE_SPOTS = "r4";
    final static String KEY_RANDOMISE_COLOURS = "r5";
    final static String KEY_RANDOMISE_CODE = "r6";

    boolean m_randomise_triangles = true;
    boolean m_randomise_bars = true;
    boolean m_randomise_blocks = false;
    boolean m_randomise_spots = false;
    boolean m_randomise_colours = false;
    boolean m_randomise_code = true;

    /**
     * Save the current values to a bundle
     */
    public Bundle toBundle() {
        Bundle b = new Bundle();

        b.putBoolean(KEY_RANDOMISE_TRIANGLES, m_randomise_triangles);
        b.putBoolean(KEY_RANDOMISE_BARS, m_randomise_bars);
        b.putBoolean(KEY_RANDOMISE_BLOCKS, m_randomise_blocks);
        b.putBoolean(KEY_RANDOMISE_SPOTS, m_randomise_spots);
        b.putBoolean(KEY_RANDOMISE_COLOURS, m_randomise_colours);
        b.putBoolean(KEY_RANDOMISE_CODE, m_randomise_code);

        return b;
    }

    /**
     * Update this instance from a bundle
     * @param b the bundle
     */
    public void fromBundle (Bundle b){

        if (b != null) {
            m_randomise_triangles = b.getBoolean(KEY_RANDOMISE_TRIANGLES, m_randomise_triangles);
            m_randomise_bars = b.getBoolean(KEY_RANDOMISE_BARS, m_randomise_bars);
            m_randomise_blocks = b.getBoolean(KEY_RANDOMISE_BLOCKS, m_randomise_blocks);
            m_randomise_spots = b.getBoolean(KEY_RANDOMISE_SPOTS, m_randomise_spots);
            m_randomise_colours = b.getBoolean(KEY_RANDOMISE_COLOURS, m_randomise_colours);
            m_randomise_code = b.getBoolean(KEY_RANDOMISE_CODE, m_randomise_code);
        }
    }
}
