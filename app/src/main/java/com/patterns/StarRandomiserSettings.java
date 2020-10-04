package com.patterns;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activities.StarsPatternActivity;
import com.example.tutorialapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StarRandomiserSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StarRandomiserSettings extends Fragment {

    StarSettings m_settings;

    public StarRandomiserSettings() {
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
    public static StarRandomiserSettings newInstance(StarSettings settings) {
        StarRandomiserSettings fragment = new StarRandomiserSettings();
        fragment.setSettings(settings);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.star_settings_randomiser, container, false);
    }

    public boolean onAccept() {
        return true;
    }
}