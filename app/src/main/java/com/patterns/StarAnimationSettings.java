package com.patterns;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tutorialapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StarAnimationSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StarAnimationSettings extends Fragment {

    StarSettings m_settings;

    public StarAnimationSettings() {
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
    public static StarAnimationSettings newInstance(StarSettings settings) {
        StarAnimationSettings fragment = new StarAnimationSettings();
        fragment.setSettings(settings);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.star_settings_randomiser, container, false);
        return v;
    }

    public boolean onAccept() {
        return true;
    }
}