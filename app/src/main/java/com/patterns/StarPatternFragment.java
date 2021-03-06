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
import com.dialogs.TabFragment;

import java.util.HashMap;
import java.util.Map;

public class StarPatternFragment extends Fragment implements TabFragment {

    PatternSet m_result = null;  // Set on accept
    PatternSet m_working_settings = new PatternSet ();

    NumberPicker m_size_picker;
    EventListener m_listener = new EventListener();
    Integer m_current_border = null;
    Map<Integer,View> m_cm_buttons = new HashMap<Integer,View>();
    Map<StarParameters.ColouringMode,Integer> m_mode_to_id = new HashMap<StarParameters.ColouringMode,Integer>();

    static String[] size_value_strs = {"80px", "160px", "320px", "480px", "640px", "800px", "1024px", "1280px", "1600px"};
    static int[] size_values = {80, 160, 320, 480, 640, 800, 1024, 1280, 1600};

    public StarPatternFragment() {

        m_mode_to_id.put (StarParameters.ColouringMode.ALTERNATE, R.id.button_cm_alternate);
        m_mode_to_id.put (StarParameters.ColouringMode.AROUND, R.id.button_cm_around);
        m_mode_to_id.put (StarParameters.ColouringMode.BOTH, R.id.button_cm_both);
        m_mode_to_id.put (StarParameters.ColouringMode.INWARDS, R.id.button_cm_inwards);
    }

    /**
     * Gives us a reference to the shared settings
     * @param settings the settings
     */
    void setSettings (PatternSet settings){

        m_working_settings.fromBundle(settings.toBundle());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment StarrandomiserSettings.
     */
    public static StarPatternFragment newInstance(PatternSet settings) {

        StarPatternFragment fragment = new StarPatternFragment();
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

        m_cm_buttons.put(R.id.button_cm_alternate, v.findViewById(R.id.button_cm_alternate));
        m_cm_buttons.put(R.id.button_cm_around, v.findViewById(R.id.button_cm_around));
        m_cm_buttons.put(R.id.button_cm_both, v.findViewById(R.id.button_cm_both));
        m_cm_buttons.put(R.id.button_cm_inwards, v.findViewById(R.id.button_cm_inwards));

        for (Map.Entry<Integer,View> entry : m_cm_buttons.entrySet())
            entry.getValue().setOnClickListener(m_listener);

        m_size_picker = (NumberPicker) v.findViewById(R.id.size_pricker);

        m_size_picker.setMinValue(0);
        m_size_picker.setMaxValue(size_value_strs.length - 1);
        m_size_picker.setDisplayedValues(size_value_strs);

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

        ShowBorder (m_mode_to_id.get(m_working_settings.m_colouring_mode));

        return v;
    }

    void ShowBorder (int id)
    {
        if (m_current_border != null)
        {
            m_cm_buttons.get(m_current_border).setBackgroundResource(android.R.color.transparent);
        }
        m_current_border = id;

        m_cm_buttons.get (m_current_border).setBackgroundResource(R.drawable.selection_border);
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

    public PatternSet getResult (){
        return m_result;
    }

    public class EventListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            int id = view.getId();

            if (id == R.id.button_cm_alternate) {
                m_working_settings.m_colouring_mode = StarParameters.ColouringMode.ALTERNATE;
            } else if (id == R.id.button_cm_around) {
                m_working_settings.m_colouring_mode = StarParameters.ColouringMode.AROUND;
            } else if (id == R.id.button_cm_both) {
                m_working_settings.m_colouring_mode = StarParameters.ColouringMode.BOTH;
            } else if (id == R.id.button_cm_inwards) {
                m_working_settings.m_colouring_mode = StarParameters.ColouringMode.INWARDS;
            }

            ShowBorder (id);
        }
    }

}
