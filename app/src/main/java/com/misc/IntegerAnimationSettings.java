package com.misc;

import android.graphics.Color;
import android.os.Bundle;
import com.misc.AnimationSettings;
import java.io.Serializable;

public class IntegerAnimationSettings extends AnimationSettings {

    int m_start;
    int m_end;

    public IntegerAnimationSettings (int s, int e){
        super ();

        setIntegerRange (s, e);
    }

    public int getStart () { return m_start; }
    public int getEnd () { return m_end; }
    public void setStart (int s) { setIntegerRange (s, m_end); }
    public void setEnd (int e) { setIntegerRange (m_start, e); }

    @Override
    public void fromBundle(Bundle bundle) {
        super.fromBundle (bundle);

        if (bundle != null) {
            int s = bundle.getInt(KEY_START, m_start);
            int e = bundle.getInt(KEY_END, m_end);
            setIntegerRange (s, e);
        }
    }

    public Bundle toBundle() {

        Bundle b = super.toBundle();

        b.putInt(KEY_START, m_start);
        b.putInt(KEY_END, m_end);

        return b;
    }

    public void setIntegerRange (int start, int end){

        m_start = Math.min (start, end);
        m_end = Math.max (start, end);

        setRange (m_end - m_start + 1);
    }

    /**
     * Returns the current value, ideally this should only be called if TryAdvance has returned true.
     * @return The current value
     */
    public int getIntegerValue (){
        return m_start + m_pos;
    }

}
