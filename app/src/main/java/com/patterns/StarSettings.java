package com.patterns;

import android.os.Bundle;

import com.dialogs.GetInteger;
import com.dialogs.ManageStarSettings;
import com.example.tutorialapp.R;

public class StarSettings {

    final String KEY_BITMAP_SIZE = "x1";

    int m_bm_size = 800;

    /**
     * Save the current values to a bundle
     */
    public Bundle toBundle() {
        Bundle b = new Bundle();

        b.putInt(KEY_BITMAP_SIZE, m_bm_size);

        return b;
    }

    /**
     * Update this instance from a bundle
     * @param b the bundle
     */
    public void fromBundle (Bundle b){

        m_bm_size = b.getInt(KEY_BITMAP_SIZE, m_bm_size);

    }
}
