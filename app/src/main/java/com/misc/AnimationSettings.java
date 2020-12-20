package com.misc;

import android.os.Bundle;

import java.io.Serializable;

public abstract class AnimationSettings {

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

    public int m_inc;       ///< Increment
    public int m_speed;     ///< Larger numbers are slower.
    public Shape m_shape;   ///< Controls the shape of the sequence

    int m_delay_counter;    ///< Manages speed
    int m_pos;              ///< The current value
    int m_range;            ///< We count from 0 to m_range
    boolean m_up;           ///< Used in TOOTH mode

    public AnimationSettings (){
        m_range = 15;
        m_inc = 1;
        m_speed = 1;
        m_shape = Shape.WEDGE;
    }

    protected void setRange (int x){
        m_range = x;
    }

    /**
     * Try to update the animation value
     * @return An integer or null.
     */
    public boolean tryAdvance (){

        if (! m_enabled || -- m_delay_counter > 0){
            return false;
        }

        m_delay_counter = m_speed;

        // Counts from 0 to range (inclusive) and then back to 0. If inc > 1 it may overstep.

        if (m_shape == Shape.WEDGE)
        {
            m_pos += m_inc;
            if (m_pos > m_range)
            {
                m_pos = m_pos - m_range - 1;
            }
            return true;
        }

        // Counts from end to start and then resets to end

        if (m_shape == Shape.RWEDGE)
        {
            m_pos -= m_inc;
            if (m_pos < 0)
            {
                m_pos = m_pos + m_range - 1;
            }
            return true;
        }

        // Counts from start to end, then changes direction and counts back.

        if (m_shape == Shape.TOOTH)
        {
            if (m_up) {
                m_pos += m_inc;
                if (m_pos > m_range) {
                    m_up = false;
                    m_pos = 2 * m_range - m_pos;
                }
            }
            else {
                m_pos -= m_inc;

                if (m_pos < 0) {
                    m_up = true;
                    m_pos = - m_pos;
                }
            }
            return true;
        }
        return false;
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
            m_inc = bundle.getInt(KEY_INC, m_inc);
            m_speed = bundle.getInt(KEY_SPEED, m_speed);
            m_delay_counter = bundle.getInt(KEY_COUNTER, m_delay_counter);
            m_pos = bundle.getInt(KEY_VALUE, m_pos);
            m_up = bundle.getBoolean(KEY_UP, m_up);
        }
    }

    public Bundle toBundle() {

        Bundle b = new Bundle();

        b.putBoolean(KEY_ENABLED, m_enabled);
        b.putInt(KEY_INC, m_inc);
        b.putInt(KEY_SPEED, m_speed);
        b.putSerializable(KEY_SHAPE, m_shape);
        b.putInt(KEY_COUNTER, m_delay_counter);
        b.putInt(KEY_VALUE, m_pos);
        b.putBoolean(KEY_UP, m_up);

        return b;
    }
}
