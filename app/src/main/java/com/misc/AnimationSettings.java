package com.misc;

import android.os.Bundle;

import java.io.Serializable;

public class AnimationSettings {

    public enum Shape{
        WEDGE,          // 1,2,3,4,1,2,3,4,1,2,3,4
        TOOTH,          // 1,2,3,4,3,2,1,2,3,4,3,2,1
        RWEDGE,         // 4,3,2,1,4,3,2,1,4,3,2,1
    }

    public static final Shape[] m_shapes = { Shape.WEDGE, Shape.TOOTH, Shape.RWEDGE};

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
    public int m_end;       ///< End of the animation (inclusive)
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
    /**
     * Returns the value as a colour based on the start and end values.
     * Blensd from 0 - 1, uses end - start as the number of colours
     */
    public int getColour ()
    {
        double f = (double) m_value / Math.abs (m_end - m_start);
        return ColourHelpers.Blend(m_end, m_start, f);
    }
    /**
     * Set the animation to the start
     */
    public void start (){
        m_counter = m_speed;
        m_value = m_start;
        m_up = true;
    }
    /**
     * Colours always cycle from 0 to 1 in 256 steps.
     */
    public void setIsColour () {
        m_start = 0;
        m_end = 255;
        m_inc = 1;
        m_shape = AnimationSettings.Shape.WEDGE;
        m_speed = 1;
    }
    /**
     * Try to update the animation value
     * @return An integer or null.
     */
    public boolean tryAdvance (){

        if (! m_enabled || -- m_counter > 0){
            return false;
        }

        m_counter = m_speed;

        // Counts from start to end and then resets to start

        if (m_shape == Shape.WEDGE)
        {
            m_value += m_inc;
            if (m_value > m_end)
            {
                m_value = m_start + (m_value - m_end - 1);
            }
            return true;
        }

        // Counts from end to start and then resets to end

        if (m_shape == Shape.RWEDGE)
        {
            m_value -= m_inc;
            if (m_value < m_start)
            {
                m_value = m_end - (m_start - m_value - 1);
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

    public static Shape nextShape (Shape s){

        if (s == Shape.WEDGE) return Shape.TOOTH;
        if (s == Shape.TOOTH) return Shape.RWEDGE;
        if (s == Shape.RWEDGE) return Shape.WEDGE;
        return Shape.WEDGE;
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