package com.patterns;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.activities.R;
import com.dialogs.TabFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TileRandomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TileRandomFragment extends Fragment implements TabFragment {

    TileRandomSet m_result = null;
    TileRandomSet m_working_settings = new TileRandomSet ();

    public TileRandomFragment() {
    }


    /**
     * Gives us a reference to the shared settings
     * @param settings the settings
     */
    void setSettings (TileRandomSet settings){
        m_working_settings.fromBundle(settings.toBundle());
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment TileRandomFragment.
     * @param settings the current settings
     */
    public static TileRandomFragment newInstance(TileRandomSet settings) {
        TileRandomFragment fragment = new TileRandomFragment();
        fragment.setSettings(settings);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.tile_settings_randomiser, container, false);

        ((Switch)v.findViewById(R.id.randomise_triangles)).setChecked(m_working_settings.m_randomise_triangles);
        ((Switch)v.findViewById(R.id.randomise_bars)).setChecked(m_working_settings.m_randomise_bars);
        ((Switch)v.findViewById(R.id.randomise_blocks)).setChecked(m_working_settings.m_randomise_blocks);
        ((Switch)v.findViewById(R.id.randomise_spots)).setChecked(m_working_settings.m_randomise_spots);
        ((Switch)v.findViewById(R.id.randomise_colours)).setChecked(m_working_settings.m_randomise_colours);
        ((Switch)v.findViewById(R.id.randomise_transform)).setChecked(m_working_settings.m_randomise_code);


        return v;
    }

    public void UpdateSettings (){

        View v = getView();

        if (v != null) {
            m_working_settings.m_randomise_triangles = ((Switch) v.findViewById(R.id.randomise_triangles)).isChecked();
            m_working_settings.m_randomise_bars = ((Switch) v.findViewById(R.id.randomise_bars)).isChecked();
            m_working_settings.m_randomise_blocks = ((Switch) v.findViewById(R.id.randomise_blocks)).isChecked();
            m_working_settings.m_randomise_spots = ((Switch) v.findViewById(R.id.randomise_spots)).isChecked();
            m_working_settings.m_randomise_colours = ((Switch) v.findViewById(R.id.randomise_colours)).isChecked();
            m_working_settings.m_randomise_code = ((Switch) v.findViewById(R.id.randomise_transform)).isChecked();
        }
    }

    @Override
    public boolean onAccept() {

        UpdateSettings();

        m_result = m_working_settings;
        return true;
    }

    public TileRandomSet getResult (){
        return m_result;
    }
}