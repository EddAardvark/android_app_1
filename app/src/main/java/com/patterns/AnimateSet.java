package com.patterns;

import android.graphics.Color;
import android.os.Bundle;

import com.misc.AnimationSettings;
import com.misc.ColourAnimationSettings;
import com.misc.IntegerAnimationSettings;

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

    IntegerAnimationSettings m_anim_rotate = new IntegerAnimationSettings(0, 359);
    IntegerAnimationSettings m_anim_points = new IntegerAnimationSettings(3, 119);
    IntegerAnimationSettings m_anim_step = new IntegerAnimationSettings(1, 7);
    IntegerAnimationSettings m_anim_repeats = new IntegerAnimationSettings(0, 20);
    IntegerAnimationSettings m_anim_angle = new IntegerAnimationSettings(0, 20);
    IntegerAnimationSettings m_anim_shrink = new IntegerAnimationSettings(0, 20);
    ColourAnimationSettings m_anim_background = new ColourAnimationSettings(Color.WHITE, Color.BLACK);
    ColourAnimationSettings m_anim_line1 = new ColourAnimationSettings(Color.BLACK, Color.CYAN);
    ColourAnimationSettings m_anim_line2 = new ColourAnimationSettings(Color.RED, Color.YELLOW);

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
