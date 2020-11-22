package com.patterns;

import android.os.Bundle;

import java.util.Random;


public class StarSettings {

    final String KEY_PATTERM = "x1";
    final String KEY_RANDOM = "x2";
    final String KEY_ANIMATE = "x1";

    public PatternSet m_pattern = new PatternSet();
    public RandomSet m_random = new RandomSet();
    public AnimateSet m_animate = new AnimateSet();

    /**
     * Save the current values to a bundle
     */
    public Bundle toBundle() {
        Bundle b = new Bundle();

        b.putBundle(KEY_PATTERM, m_pattern.toBundle());
        b.putBundle(KEY_RANDOM, m_random.toBundle());
        b.putBundle(KEY_ANIMATE, m_animate.toBundle());

        return b;
    }

    /**
     * Update this instance from a bundle
     * @param b the bundle
     */
    public void fromBundle (Bundle b){

        m_pattern.fromBundle(b.getBundle(KEY_PATTERM));
        m_random.fromBundle(b.getBundle(KEY_RANDOM));
        m_animate.fromBundle(b.getBundle(KEY_ANIMATE));
    }

    /**
     * Choose a random colouring mode
     */
    public void randomise_colour_mode() {

        if (m_random.m_randomise_colour_mode) {
            m_pattern.m_colouring_mode = PatternSet.randomColourMode();
        }
    }
}
