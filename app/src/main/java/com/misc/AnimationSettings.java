package com.misc;

import android.os.Bundle;

import com.patterns.StarSettings;

import java.io.Serializable;

public class AnimationSettings {

    public enum Shape{
        WEDGE,          // 1,2,3,4,1,2,3,4,1,2,3,4
        TOOTH,          // 1,2,3,4,3,2,1,2,3,4,3,2,1
    }

    final static String KEY_START = "a1";
    final static String KEY_END = "a2";
    final static String KEY_INC = "a3";
    final static String KEY_SPEED = "a4";
    final static String KEY_SHAPE = "a5";
    final static String KEY_COUNTER = "a6";
    final static String KEY_VALUE = "a7";
    final static String KEY_UP = "a8";
    final static String KEY_ENABLED = "a9";

    public boolean m_enabled = false;

    public int m_start;     ///< Start of the animation
    public int m_end;       ///< Start of the animation (inclusive)
    public int m_inc;       ///< Increment
    public int m_speed;     ///< Larger numbers are slower.
    public Shape m_shape;   ///< Controls the shape of the sequence

    int m_counter;          ///< Manages speed
    int m_value;            ///< The current value
    boolean m_up;           ///< Used in TOOTH mode


    public AnimationSettings (){
        m_start = 0;
        m_end = 15;
        m_inc = 1;
        m_speed = 1;
        m_shape = Shape.WEDGE;
    }

    public void start (){
        m_counter = m_speed;
        m_value = m_start;
        m_up = true;
    }
    /**
     * Try to update the animation value
     * @return An integer or null.
     */
    public boolean tryAdvance (){

        if (! m_enabled || -- m_counter > 0){
            return false;
        }

        // Counts from start to end and then resets to start

        m_counter = m_speed;

        if (m_shape == Shape.WEDGE)
        {
            m_value += m_inc;
            if (m_value > m_end)
            {
                m_value = m_start;
            }
            return true;
        }

        // Counts from start to end, then changes direction and counts back.

        if (m_shape == Shape.TOOTH)
        {
            if (m_up) {
                ++m_value;
                if (m_value > m_end) {
                    m_up = false;
                    m_value = 2 * m_end - m_value;
                }
            }
            else {
                m_value -= m_inc;

                if (m_value < m_start) {
                    m_up = true;
                    m_value = 2 * m_start - m_value;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns the current value, ideally this should only be called if TryAdvance has returned true.
     * @return The current value
     */
    public int getValue (){
        return m_value;
    }


    public void fromBundle(Bundle bundle) {

        if (bundle != null) {
            Serializable x = bundle.getSerializable(KEY_SHAPE);

            if (x != null) {
                m_shape = (AnimationSettings.Shape) x;
            }

            m_enabled = bundle.getBoolean(KEY_ENABLED, m_enabled);
            m_start = bundle.getInt(KEY_START, m_start);
            m_end = bundle.getInt(KEY_END, m_end);
            m_inc = bundle.getInt(KEY_INC, m_inc);
            m_speed = bundle.getInt(KEY_SPEED, m_speed);
            m_counter = bundle.getInt(KEY_COUNTER, m_counter);
            m_value = bundle.getInt(KEY_VALUE, m_value);
            m_up = bundle.getBoolean(KEY_UP, m_up);
        }
    }

    public Bundle toBundle() {

        Bundle b = new Bundle();

        b.putBoolean(KEY_ENABLED, m_enabled);
        b.putInt(KEY_START, m_start);
        b.putInt(KEY_END, m_end);
        b.putInt(KEY_INC, m_inc);
        b.putInt(KEY_SPEED, m_speed);
        b.putSerializable(KEY_SHAPE, m_shape);
        b.putInt(KEY_COUNTER, m_counter);
        b.putInt(KEY_VALUE, m_value);
        b.putBoolean(KEY_UP, m_up);

        return b;
    }
}
