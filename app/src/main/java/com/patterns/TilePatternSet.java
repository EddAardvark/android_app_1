package com.patterns;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import java.io.Serializable;

/**
 * Pattern specific settings of the star pattern
 */
public class TilePatternSet {

    final String KEY_BITMAP_SIZE = "p1";
    final String KEY_BACKGROUND = "p2";

    public final static int DEFAULT_BM_SIZE = 1024;
    public int m_bm_size = DEFAULT_BM_SIZE;
    public int m_background = Color.WHITE;

    /**
     * Create the bitmap
     * @return a bitmap
     */
    public Bitmap CreateBitmap ()
    {
        return Bitmap.createBitmap(m_bm_size, m_bm_size, Bitmap.Config.RGB_565);
    }
    /**
     * Save the current values to a bundle
     */
    public Bundle toBundle() {
        Bundle b = new Bundle();

        b.putInt(KEY_BITMAP_SIZE, m_bm_size);
        b.putSerializable(KEY_BACKGROUND, m_background);

        return b;
    }

    /**
     * Update this instance from a bundle
     * @param b the bundle
     */
    public void fromBundle(Bundle b) {

        if (b != null) {
            m_bm_size = b.getInt(KEY_BITMAP_SIZE, m_bm_size);
            m_background = b.getInt(KEY_BACKGROUND, m_background);
        }
    }
}
