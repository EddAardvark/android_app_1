package com.patterns;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dialogs.GetInteger;
import com.example.tutorialapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StarAnimationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StarAnimationFragment extends Fragment{

    AnimateSet m_result = null;     // Only set when accepted
    AnimateSet m_working_settings = new AnimateSet();
    StarAnimationFragment.EventListener m_listener = new  StarAnimationFragment.EventListener();
    TextView m_rotate_speed;

    final static int CB_ROTATE = 1;


    public StarAnimationFragment() {
    }

    /**
     * Gives us a reference to the shared settings
     * @param settings the settings
     */
    void setSettings (AnimateSet settings){
        m_working_settings.fromBundle(settings.toBundle());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment StarrandomiserSettings.
     * @param settings
     */
    public static StarAnimationFragment newInstance(AnimateSet settings) {
        StarAnimationFragment fragment = new StarAnimationFragment();
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
        View v =  inflater.inflate(R.layout.star_settings_animation, container, false);

        m_rotate_speed = ((TextView)v.findViewById(R.id.anim_rotate_speed));

        m_rotate_speed.setText("1");
        m_rotate_speed.setOnClickListener(m_listener);


        return v;
    }

    public boolean onAccept() {

        m_result = m_working_settings;
        return true;
    }

    public AnimateSet getResult (){
        return m_result;
    }


    public class EventListener implements View.OnClickListener,  GetInteger.Result  {

        @Override
        public void onClick(View view) {

            int id = view.getId();

            switch (id) {
                case R.id.anim_rotate_speed:
                    GetInteger dialog = GetInteger.construct(this, CB_ROTATE, 0, 0, 359, 1, "Rotation Speed", "How fast the whole pattern rotates");
                    dialog.show (getActivity().getSupportFragmentManager(), "Hello");
                    break;
            }
        }
        @Override
        public void SetInteger(int id, int value) {

        }
    }



}