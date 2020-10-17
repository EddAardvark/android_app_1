package com.patterns;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.tutorialapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StarRandomiserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StarRandomiserFragment extends Fragment {

    RandomSet m_result = null;
    RandomSet m_working_settings = new RandomSet ();

    public StarRandomiserFragment() {
    }


    /**
     * Gives us a reference to the shared settings
     * @param settings the settings
     */
    void setSettings (RandomSet settings){
        m_working_settings.fromBundle(settings.toBundle());
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment StarrandomiserSettings.
     * @param settings the current settings
     */
    public static StarRandomiserFragment newInstance(RandomSet settings) {
        StarRandomiserFragment fragment = new StarRandomiserFragment();
        fragment.setSettings(settings);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.star_settings_randomiser, container, false);

        ((Switch)v.findViewById(R.id.num_points)).setChecked(m_working_settings.m_randomise_num_points);
        ((Switch)v.findViewById(R.id.point_step)).setChecked(m_working_settings.m_randomise_point_step);
        ((Switch)v.findViewById(R.id.repeats)).setChecked(m_working_settings.m_randomise_repeats);
        ((Switch)v.findViewById(R.id.angles)).setChecked(m_working_settings.m_randomise_angles);
        ((Switch)v.findViewById(R.id.shrinkage)).setChecked(m_working_settings.m_randomise_shrinkage);
        ((Switch)v.findViewById(R.id.forground_colours)).setChecked(m_working_settings.m_randomise_forground_colours);
        ((Switch)v.findViewById(R.id.backround_colours)).setChecked(m_working_settings.m_randomise_backround_colours);
        ((Switch)v.findViewById(R.id.colour_mode)).setChecked(m_working_settings.m_randomise_colour_mode);

        return v;
    }

    public void UpdateSettings (){

        View v = getView();

        if (v != null) {
            m_working_settings.m_randomise_num_points = ((Switch) v.findViewById(R.id.num_points)).isChecked();
            m_working_settings.m_randomise_point_step = ((Switch) v.findViewById(R.id.point_step)).isChecked();
            m_working_settings.m_randomise_repeats = ((Switch) v.findViewById(R.id.repeats)).isChecked();
            m_working_settings.m_randomise_angles = ((Switch) v.findViewById(R.id.angles)).isChecked();
            m_working_settings.m_randomise_shrinkage = ((Switch) v.findViewById(R.id.shrinkage)).isChecked();
            m_working_settings.m_randomise_forground_colours = ((Switch) v.findViewById(R.id.forground_colours)).isChecked();
            m_working_settings.m_randomise_backround_colours = ((Switch) v.findViewById(R.id.backround_colours)).isChecked();
            m_working_settings.m_randomise_colour_mode = ((Switch) v.findViewById(R.id.colour_mode)).isChecked();
        }
    }

    public boolean onAccept() {

        UpdateSettings();

        m_result = m_working_settings;
        return true;
    }

    public RandomSet getResult (){
        return m_result;
    }
}