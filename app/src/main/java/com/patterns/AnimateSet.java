package com.patterns;

import android.os.Bundle;

import com.misc.AnimationSettings;

/**
 * Settings used to animate the star pattern
 */
public class AnimateSet {

    final static String KEY_ANIM_ROTATE = "a1";
    final static String KEY_ANIM_POINTS = "a2";
    final static String KEY_ANIM_STEP = "a3";
    final static String KEY_ANIM_REPEATS = "a4";
    final static String KEY_ANIM_ANGLE = "a5";
    final static String KEY_ANIM_SHRINK = "a6";
    final static String KEY_ANIM_BACKGROUND = "a7";
    final static String KEY_ANIM_LINE1 = "a8";
    final static String KEY_ANIM_LINE2 = "a9";

    AnimationSettings m_anim_rotate = new AnimationSettings();
    AnimationSettings m_anim_points = new AnimationSettings();
    AnimationSettings m_anim_step = new AnimationSettings();
    AnimationSettings m_anim_repeats = new AnimationSettings();
    AnimationSettings m_anim_angle = new AnimationSettings();
    AnimationSettings m_anim_shrink = new AnimationSettings();
    AnimationSettings m_anim_background = new AnimationSettings();
    AnimationSettings m_anim_line1 = new AnimationSettings();
    AnimationSettings m_anim_line2 = new AnimationSettings();

    public AnimateSet () {

        // Rotate the whole pattern

        m_anim_rotate.m_start = 0;
        m_anim_rotate.m_end = 359;

        // Vary the number of points

        m_anim_points.m_start = 3;
        m_anim_points.m_end = 119;

        // Vary the point step

        m_anim_step.m_start = 1;
        m_anim_step.m_end = 7;

        // Repeats

        m_anim_repeats.m_start = 0;
        m_anim_repeats.m_end = 20;

        // Angles

        m_anim_angle.m_start = 0;
        m_anim_angle.m_end = 20;

        // Shrink

        m_anim_shrink.m_start = 0;
        m_anim_shrink.m_end = 20;

        // Colours

        m_anim_background.setIsColour();
        m_anim_line1.setIsColour();
        m_anim_line2.setIsColour();
    }
    /**
     * Save the current values to a bundle
     */
    public Bundle toBundle() {
        Bundle b = new Bundle();

        b.putBundle(KEY_ANIM_ROTATE, m_anim_rotate.toBundle());
        b.putBundle(KEY_ANIM_POINTS, m_anim_points.toBundle());
        b.putBundle(KEY_ANIM_STEP, m_anim_step.toBundle());
        b.putBundle(KEY_ANIM_REPEATS, m_anim_repeats.toBundle());
        b.putBundle(KEY_ANIM_ANGLE, m_anim_angle.toBundle());
        b.putBundle(KEY_ANIM_SHRINK, m_anim_shrink.toBundle());
        b.putBundle(KEY_ANIM_BACKGROUND, m_anim_background.toBundle());
        b.putBundle(KEY_ANIM_LINE1, m_anim_line1.toBundle());
        b.putBundle(KEY_ANIM_LINE2, m_anim_line2.toBundle());

        return b;
    }

    /**
     * Update this instance from a bundle
     * @param b the bundle
     */
    public void fromBundle (Bundle b){

        if (b != null) {
            m_anim_rotate.fromBundle(b.getBundle(KEY_ANIM_ROTATE));
            m_anim_points.fromBundle(b.getBundle(KEY_ANIM_POINTS));
            m_anim_step.fromBundle(b.getBundle(KEY_ANIM_STEP));
            m_anim_repeats.fromBundle(b.getBundle(KEY_ANIM_REPEATS));
            m_anim_angle.fromBundle(b.getBundle(KEY_ANIM_ANGLE));
            m_anim_shrink.fromBundle(b.getBundle(KEY_ANIM_SHRINK));
            m_anim_background.fromBundle(b.getBundle(KEY_ANIM_BACKGROUND));
            m_anim_line1.fromBundle(b.getBundle(KEY_ANIM_LINE1));
            m_anim_line2.fromBundle(b.getBundle(KEY_ANIM_LINE2));
        }
    }
}
