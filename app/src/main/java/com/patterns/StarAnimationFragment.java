package com.patterns;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
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
    TextView m_points_start;
    TextView m_points_end;
    TextView m_points_inc;
    TextView m_points_speed;

    final static int CB_ROTATE_SPEED = 1;
    final static int CB_POINTS_START = 2;
    final static int CB_POINTS_END = 3;
    final static int CB_POINTS_INC = 4;
    final static int CB_POINTS_SPEED = 5;


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

        m_rotate_speed = (TextView)v.findViewById(R.id.anim_rotate_speed);

        m_points_start = (TextView)v.findViewById(R.id.pts_start);
        m_points_end = (TextView)v.findViewById(R.id.pts_end);
        m_points_inc = (TextView)v.findViewById(R.id.pts_inc);
        m_points_speed = (TextView)v.findViewById(R.id.pts_speed);

        m_rotate_speed.setOnClickListener(m_listener);

        m_points_start.setOnClickListener(m_listener);
        m_points_end.setOnClickListener(m_listener);
        m_points_inc.setOnClickListener(m_listener);
        m_points_speed.setOnClickListener(m_listener);

        show_rotate_speed ();
        show_points_start ();
        show_points_end ();
        show_points_inc ();
        show_points_speed ();

        ((Switch)v.findViewById(R.id.anim_rotate)).setChecked(m_working_settings.m_anim_rotate.m_enabled);
        ((Switch)v.findViewById(R.id.anim_points)).setChecked(m_working_settings.m_anim_points.m_enabled);
        ((Switch)v.findViewById(R.id.anim_step)).setChecked(m_working_settings.m_anim_step.m_enabled);
        ((Switch)v.findViewById(R.id.anim_repeats)).setChecked(m_working_settings.m_anim_repeats.m_enabled);
        ((Switch)v.findViewById(R.id.anim_angle)).setChecked(m_working_settings.m_anim_angle.m_enabled);
        ((Switch)v.findViewById(R.id.anim_shrink)).setChecked(m_working_settings.m_anim_shrink.m_enabled);
        ((Switch)v.findViewById(R.id.anim_backround)).setChecked(m_working_settings.m_anim_background.m_enabled);
        ((Switch)v.findViewById(R.id.anim_l1)).setChecked(m_working_settings.m_anim_line1.m_enabled);
        ((Switch)v.findViewById(R.id.anim_l2)).setChecked(m_working_settings.m_anim_line2.m_enabled);


        return v;
    }

    /**
     * Copy from the UI to the working settings
     */
    public void UpdateSettings (){

        View v = getView();

        if (v != null) {
            m_working_settings.m_anim_rotate.m_enabled = ((Switch) v.findViewById(R.id.anim_rotate)).isChecked();
            m_working_settings.m_anim_points.m_enabled = ((Switch) v.findViewById(R.id.anim_points)).isChecked();
            m_working_settings.m_anim_step.m_enabled = ((Switch) v.findViewById(R.id.anim_step)).isChecked();
            m_working_settings.m_anim_repeats.m_enabled = ((Switch) v.findViewById(R.id.anim_repeats)).isChecked();
            m_working_settings.m_anim_angle.m_enabled = ((Switch) v.findViewById(R.id.anim_angle)).isChecked();
            m_working_settings.m_anim_shrink.m_enabled = ((Switch) v.findViewById(R.id.anim_shrink)).isChecked();
            m_working_settings.m_anim_background.m_enabled = ((Switch) v.findViewById(R.id.anim_backround)).isChecked();
            m_working_settings.m_anim_line1.m_enabled = ((Switch) v.findViewById(R.id.anim_l1)).isChecked();
            m_working_settings.m_anim_line2.m_enabled = ((Switch) v.findViewById(R.id.anim_l2)).isChecked();
        }
    }


    public boolean onAccept() {

        UpdateSettings ();
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
                    ShowIntegerDialog(CB_ROTATE_SPEED, m_working_settings.m_anim_rotate.m_speed, 1, 30, "Rotation Speed", "How fast the whole pattern rotates");
                    break;
                case R.id.pts_start:
                    ShowIntegerDialog(CB_POINTS_START, m_working_settings.m_anim_points.m_start, 3, 100, "Starting Points", "The number of points defining the first pattern");
                    break;
                case R.id.pts_end:
                    ShowIntegerDialog(CB_POINTS_END, m_working_settings.m_anim_points.m_end, 3, 100, "End Points", "The number of points defining the last pattern");
                    break;
                case R.id.pts_inc:
                    ShowIntegerDialog(CB_POINTS_INC, m_working_settings.m_anim_points.m_inc, 1, 7, "Points Increment", "The increment in the number of points between patterns");
                    break;
                case R.id.pts_speed:
                    ShowIntegerDialog(CB_POINTS_SPEED, m_working_settings.m_anim_points.m_speed, 1, 30, "Points Speed", "How often the number of points updates");
                    break;
            }
        }
        /**
         * Show a dialog that allows the user to select an integer from a range
         * @param id    The id of the integer being edited
         * @param value The current value
         * @param start The first value in the available range
         * @param end   The last value in the available range
         * @param title A short title
         * @param descr A longer description
         */
        void ShowIntegerDialog (int id, int value, int start, int end, String title, String descr)
        {
            GetInteger dialog = GetInteger.construct(this, id, value, start, end, 1, title, descr);
            dialog.show (getActivity().getSupportFragmentManager(), "Hello");
        }

        /**
         * Callback from the integer dialog box
         * @param id The id of the parameter being edited
         * @param value The new value
         */
        @Override
        public void SetInteger(int id, int value) {
            switch (id)
            {
                case CB_ROTATE_SPEED:
                    m_working_settings.m_anim_rotate.m_speed = value;
                    show_rotate_speed ();
                    break;
                case CB_POINTS_START:
                    m_working_settings.m_anim_points.m_start = value;
                    show_points_start ();
                    break;
                case CB_POINTS_END:
                    m_working_settings.m_anim_points.m_end = value;
                    show_points_end ();
                    break;
                case CB_POINTS_INC:
                    m_working_settings.m_anim_points.m_inc = value;
                    show_points_inc ();
                    break;
                case CB_POINTS_SPEED:
                    m_working_settings.m_anim_points.m_speed = value;
                    show_points_speed ();
                    break;
                default:
                    break;
            }

        }
    }

    void show_rotate_speed () { m_rotate_speed.setText(Integer.toString (m_working_settings.m_anim_rotate.m_speed)); }
    void show_points_start () { m_points_start.setText(Integer.toString (m_working_settings.m_anim_points.m_start)); }
    void show_points_end () { m_points_end.setText(Integer.toString (m_working_settings.m_anim_points.m_end)); }
    void show_points_inc () { m_points_inc.setText(Integer.toString (m_working_settings.m_anim_points.m_inc)); }
    void show_points_speed () { m_points_speed.setText(Integer.toString (m_working_settings.m_anim_points.m_speed)); }

}