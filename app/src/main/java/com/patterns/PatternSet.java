package com.patterns;

import android.os.Bundle;

import java.io.Serializable;
import java.util.Random;

/**
 * Pattern specific settings of the star pattern
 */
public class PatternSet {

    final String KEY_BITMAP_SIZE = "p1";
    final String KEY_COLOURING_MODE = "p2";

    public final static int DEFAULT_BM_SIZE = 800;
    public int m_bm_size = DEFAULT_BM_SIZE;
    public StarParameters.ColouringMode m_colouring_mode = StarParameters.ColouringMode.INWARDS;

    /**
     * Save the current values to a bundle
     */
    public Bundle toBundle() {
        Bundle b = new Bundle();

        b.putInt(KEY_BITMAP_SIZE, m_bm_size);
        b.putSerializable(KEY_COLOURING_MODE, m_colouring_mode);

        return b;
    }

    /**
     * Update this instance from a bundle
     * @param b the bundle
     */
    public void fromBundle(Bundle b) {

        if (b != null) {
            m_bm_size = b.getInt(KEY_BITMAP_SIZE, m_bm_size);
            Serializable x = b.getSerializable(KEY_COLOURING_MODE);

            if (x != null) {
                m_colouring_mode = (StarParameters.ColouringMode) x;
            }
        }
    }
}
