package com.misc;

import android.graphics.Color;
import android.os.Bundle;

import com.misc.AnimationSettings;

public class ColourAnimationSettings extends AnimationSettings {

    int m_end_colour;
    int m_start_colour;
    static final int RANGE = 64;

    public ColourAnimationSettings (int scolour, int ecolour) {
        super ();

        m_end_colour = ecolour;
        m_start_colour = scolour;

        setRange (RANGE);
    }

    public int getStartColour () { return m_start_colour; }
    public int getEndColour () { return m_end_colour; }
    public void setStartColour (int s) { m_start_colour = s; }
    public void setEndColour (int e) { m_end_colour = e; }

    @Override
    public void fromBundle(Bundle bundle) {
        super.fromBundle (bundle);

        if (bundle != null) {
            m_start_colour = bundle.getInt(KEY_START, m_start_colour);
            m_end_colour = bundle.getInt(KEY_END, m_end_colour);
        }
    }

    public Bundle toBundle() {

        Bundle b = super.toBundle();

        b.putInt(KEY_START, m_start_colour);
        b.putInt(KEY_END, m_end_colour);

        return b;
    }

    /**
     * Returns the value as a colour based on the start and end values.
     * Blensd from 0 - 1, uses end - start as the number of colours
     */
    public int getColour ()
    {
        double f = (double) m_pos / RANGE;
        return ColourHelpers.Blend(m_end_colour, m_start_colour, f);
    }

}
