package com.patterns;

import android.os.Bundle;

import com.dialogs.GetInteger;
import com.dialogs.ManageStarSettings;
import com.example.tutorialapp.R;

import java.io.Serializable;

public class StarSettings {

    final String KEY_BITMAP_SIZE = "x1";
    final String KEY_COLOURING_MODE = "x2";

    enum ColouringMode
    {
        INWARDS, AROUND, BOTH, ALTERNATE
    }

    public int m_bm_size = 800;
    public ColouringMode m_colouring_mode = ColouringMode.INWARDS;

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
    public void fromBundle (Bundle b){

        m_bm_size = b.getInt(KEY_BITMAP_SIZE, m_bm_size);
        Serializable x = b.getSerializable(KEY_COLOURING_MODE);

        if (x != null){
            m_colouring_mode = (ColouringMode) x;
        }
    }
}
