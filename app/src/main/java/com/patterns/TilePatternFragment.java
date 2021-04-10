package com.patterns;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.activities.R;
import com.dialogs.GetColour;
import com.dialogs.TabFragment;

import java.util.HashMap;
import java.util.Map;

public class TilePatternFragment extends Fragment implements TabFragment {

    TilePatternSet m_result = null;  // Set on accept
    TilePatternSet m_working_settings = new TilePatternSet ();

    final static int CB_BACKCOLOUR = 1;

    NumberPicker m_size_picker;
    View m_layout_background;

    EventListener m_listener = new EventListener();

    static String[] size_value_strs = {"80px", "160px", "320px", "480px", "640px", "800px", "1024px", "1280px", "1600px"};
    static int[] size_values = {320, 480, 640, 800, 1024, 1280, 1600, 1920, 2400};

    public TilePatternFragment() {

    }

    /**
     * Gives us a reference to the shared settings
     * @param settings the settings
     */
    void setSettings (TilePatternSet settings){

        m_working_settings.fromBundle(settings.toBundle());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment TilePatternFragment.
     */
    public static TilePatternFragment newInstance(TilePatternSet settings) {

        TilePatternFragment fragment = new TilePatternFragment();
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

        View v =  inflater.inflate(R.layout.tile_settings_pattern, container, false);

        m_size_picker = (NumberPicker) v.findViewById(R.id.size_pricker);
        m_layout_background = v.findViewById(R.id.background_colour);

        m_size_picker.setMinValue(0);
        m_size_picker.setMaxValue(size_value_strs.length - 1);
        m_size_picker.setDisplayedValues(size_value_strs);

        m_layout_background.setOnClickListener(m_listener);
        m_layout_background.setBackgroundColor(m_working_settings.m_background);

        String temp = Integer.toString(m_working_settings.m_bm_size) + "px";
        int idx = -1;

        for (int i = 0; i < size_value_strs.length; i++) {
            String x = size_value_strs [i];
            if (size_value_strs[i].equals(temp)) {
                idx = i;
                break;
            }
        }

        m_size_picker.setValue(Math.max(idx, 0));
        show_background();

        return v;
    }

    /**
     * Show the current background colour in the ui
     */
    void show_background (){
        m_layout_background.setBackgroundColor(m_working_settings.m_background);
    }

    /**
     * Called when the parent dialog is accepted
     */
    @Override
    public boolean onAccept (){

        int x = m_size_picker.getValue();
        m_working_settings.m_bm_size = size_values[x];

        m_result = m_working_settings;
        return true;
    }

    public TilePatternSet getResult (){
        return m_result;
    }

    public class EventListener implements View.OnClickListener, GetColour.Result {

        @Override
        public void onClick(View view) {

            int id = view.getId();

            if (id == R.id.background_colour) {
                GetColour dialog = GetColour.construct(this, CB_BACKCOLOUR, m_working_settings.m_background, getString(R.string.background_title), getString(R.string.background_description));
                dialog.show(getActivity().getSupportFragmentManager(), "Hello");
            }
        }
        @Override
        public void SetColour(int id, int value) {

            switch (id) {
                case CB_BACKCOLOUR:
                    m_working_settings.m_background = value;
                    show_background();
                    break;
            }
        }

    }

}
