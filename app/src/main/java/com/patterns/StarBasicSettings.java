package com.patterns;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tutorialapp.R;

public class StarBasicSettings extends Fragment {

    StarSettings m_settings;
    NumberPicker m_size_picker;

    static String[] size_value_strs = {"80px", "160px", "320px", "640px", "800px", "1024px", "1280px", "1600px"};
    static int[] size_values = {80, 160, 320, 640, 800, 1024, 1280, 1600};

    public StarBasicSettings () {
    }

    /**
     * Gives us a reference to the shared settings
     * @param settings the settings
     */
    void setSettings (StarSettings settings){
        m_settings = settings;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment StarrandomiserSettings.
     */
    public static StarBasicSettings newInstance(StarSettings settings) {
        StarBasicSettings fragment = new StarBasicSettings();
        fragment.setSettings(settings);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.star_settings_pattern, container, false);

        m_size_picker = (NumberPicker) v.findViewById(R.id.size_pricker);

        m_size_picker.setMinValue(0);
        m_size_picker.setMaxValue(size_value_strs.length - 1);
        m_size_picker.setDisplayedValues(size_value_strs);

        String temp = Integer.toString(m_settings.m_bm_size) + "px";
        int idx = -1;

        for (int i = 0; i < size_value_strs.length; i++) {
            String x = size_value_strs [i];
            if (size_value_strs[i].equals(temp)) {
                idx = i;
                break;
            }
        }

        m_size_picker.setValue(Math.max(idx, 0));
        return v;
    }

    /**
     * Called when the parent dialog is accepted
     */
    public boolean onAccept (){
        int x = m_size_picker.getValue();
        m_settings.m_bm_size = size_values[x];
        return true;
    }

}
