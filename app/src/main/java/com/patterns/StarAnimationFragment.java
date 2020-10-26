package com.patterns;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.dialogs.GetInteger;
import com.example.tutorialapp.R;
import com.misc.AnimationSettings;

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
    TextView m_step_start;
    TextView m_step_end;
    TextView m_step_inc;
    TextView m_step_speed;
    TextView m_repeat_start;
    TextView m_repeat_end;
    TextView m_repeat_inc;
    TextView m_repeat_speed;
    TextView m_angle_start;
    TextView m_angle_end;
    TextView m_angle_inc;
    TextView m_angle_speed;
    TextView m_shrink_start;
    TextView m_shrink_end;
    TextView m_shrink_inc;
    TextView m_shrink_speed;

    ImageView m_rotate_shape;
    ImageView m_points_shape;
    ImageView m_step_shape;
    ImageView m_repeat_shape;
    ImageView m_angle_shape;
    ImageView m_shrink_shape;

    View m_background_start;
    View m_background_end;
    View m_line1_start;
    View m_line1_end;
    View m_line2_start;
    View m_line2_end;

    final static int CB_ROTATE_SPEED = 1;
    final static int CB_POINTS_START = 2;
    final static int CB_POINTS_END = 3;
    final static int CB_POINTS_INC = 4;
    final static int CB_POINTS_SPEED = 5;
    final static int CB_STEP_START = 6;
    final static int CB_STEP_END = 7;
    final static int CB_STEP_INC = 8;
    final static int CB_STEP_SPEED = 9;
    final static int CB_REPEAT_START = 10;
    final static int CB_REPEAT_END = 11;
    final static int CB_REPEAT_INC = 12;
    final static int CB_REPEAT_SPEED = 13;
    final static int CB_ANGLE_START = 14;
    final static int CB_ANGLE_END = 15;
    final static int CB_ANGLE_INC = 16;
    final static int CB_ANGLE_SPEED = 17;
    final static int CB_SHRINK_START = 18;
    final static int CB_SHRINK_END = 19;
    final static int CB_SHRINK_INC = 20;
    final static int CB_SHRINK_SPEED = 21;
    final static int CB_BACKGROUND_COLOUR_START = 22;
    final static int CB_BACKGROUND_COLOUR_END = 23;
    final static int CB_L1_COLOUR_START = 24;
    final static int CB_L1_COLOUR_END = 25;
    final static int CB_L2_COLOUR_START = 26;
    final static int CB_L2_COLOUR_END = 27;

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

        m_step_start = (TextView)v.findViewById(R.id.step_start);
        m_step_end = (TextView)v.findViewById(R.id.step_end);
        m_step_inc = (TextView)v.findViewById(R.id.step_inc);
        m_step_speed = (TextView)v.findViewById(R.id.step_speed);

        m_repeat_start = (TextView)v.findViewById(R.id.rep_start);
        m_repeat_end   = (TextView)v.findViewById(R.id.rep_end);
        m_repeat_inc   = (TextView)v.findViewById(R.id.rep_inc);
        m_repeat_speed = (TextView)v.findViewById(R.id.rep_speed);

        m_angle_start = (TextView)v.findViewById(R.id.angle_start);
        m_angle_end   = (TextView)v.findViewById(R.id.angle_end);
        m_angle_inc   = (TextView)v.findViewById(R.id.angle_inc);
        m_angle_speed = (TextView)v.findViewById(R.id.angle_speed);

        m_shrink_start = (TextView)v.findViewById(R.id.shrink_start);
        m_shrink_end   = (TextView)v.findViewById(R.id.shrink_end);
        m_shrink_inc   = (TextView)v.findViewById(R.id.shrink_inc);
        m_shrink_speed = (TextView)v.findViewById(R.id.shrink_speed);

        m_rotate_shape  = (ImageView) v.findViewById(R.id.rotate_shape);
        m_points_shape = (ImageView) v.findViewById(R.id.points_shape);
        m_step_shape   = (ImageView) v.findViewById(R.id.step_shape);
        m_repeat_shape = (ImageView) v.findViewById(R.id.rep_shape);
        m_angle_shape  = (ImageView) v.findViewById(R.id.angle_shape);
        m_shrink_shape = (ImageView) v.findViewById(R.id.shrink_shape);

        m_background_start = v.findViewById(R.id.back_colour_start);
        m_background_end = v.findViewById(R.id.back_colour_end);
        m_line1_start = v.findViewById(R.id.line1_colour_start);
        m_line1_end = v.findViewById(R.id.line1_colour_end);
        m_line2_start = v.findViewById(R.id.line2_colour_start);
        m_line2_end = v.findViewById(R.id.line2_colour_end);

        m_rotate_speed.setOnClickListener(m_listener);

        m_points_start.setOnClickListener(m_listener);
        m_points_end.setOnClickListener(m_listener);
        m_points_inc.setOnClickListener(m_listener);
        m_points_speed.setOnClickListener(m_listener);

        m_step_start.setOnClickListener(m_listener);
        m_step_end.setOnClickListener(m_listener);
        m_step_inc.setOnClickListener(m_listener);
        m_step_speed.setOnClickListener(m_listener);

        m_repeat_start.setOnClickListener(m_listener);
        m_repeat_end.setOnClickListener(m_listener);
        m_repeat_inc.setOnClickListener(m_listener);
        m_repeat_speed.setOnClickListener(m_listener);

        m_angle_start.setOnClickListener(m_listener);
        m_angle_end.setOnClickListener(m_listener);
        m_angle_inc.setOnClickListener(m_listener);
        m_angle_speed.setOnClickListener(m_listener);

        m_shrink_start.setOnClickListener(m_listener);
        m_shrink_end.setOnClickListener(m_listener);
        m_shrink_inc.setOnClickListener(m_listener);
        m_shrink_speed.setOnClickListener(m_listener);

        m_rotate_shape.setOnClickListener(m_listener);
        m_points_shape.setOnClickListener(m_listener);
        m_step_shape.setOnClickListener(m_listener);
        m_repeat_shape.setOnClickListener(m_listener);
        m_angle_shape.setOnClickListener(m_listener);
        m_shrink_shape.setOnClickListener(m_listener);

        m_background_start.setOnClickListener(m_listener);
        m_background_end.setOnClickListener(m_listener);
        m_line1_start.setOnClickListener(m_listener);
        m_line1_end.setOnClickListener(m_listener);
        m_line2_start.setOnClickListener(m_listener);
        m_line2_end.setOnClickListener(m_listener);

        show_rotate_speed ();
        show_rotate_shape ();
        show_points_start ();
        show_points_end ();
        show_points_inc ();
        show_points_speed ();
        show_points_shape ();
        show_step_start ();
        show_step_end ();
        show_step_inc ();
        show_step_speed ();
        show_step_shape ();
        show_repeat_start ();
        show_repeat_end ();
        show_repeat_inc ();
        show_repeat_speed ();
        show_repeat_shape ();
        show_angle_start ();
        show_angle_end ();
        show_angle_inc ();
        show_angle_speed ();
        show_angle_shape ();
        show_shrink_start ();
        show_shrink_end ();
        show_shrink_inc ();
        show_shrink_speed ();
        show_shrink_shape ();
        show_background_start();
        show_background_end();
        show_line1_start();
        show_line1_end();
        show_line2_start();
        show_line2_end();

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
                    ShowIntegerDialog(CB_ROTATE_SPEED, m_working_settings.m_anim_rotate.m_speed, 1, 30, "Rotation Speed", "The delay before rotating the pattern");
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
                    ShowIntegerDialog(CB_POINTS_SPEED, m_working_settings.m_anim_points.m_speed, 1, 30, "Points Speed", "The delay before updating the number of points");
                    break;
                case R.id.step_start:
                    ShowIntegerDialog(CB_STEP_START, m_working_settings.m_anim_step.m_start, 3, 100, "Starting Step", "The starting step size");
                    break;
                case R.id.step_end:
                    ShowIntegerDialog(CB_STEP_END, m_working_settings.m_anim_step.m_end, 3, 100, "End Step", "The last step size");
                    break;
                case R.id.step_inc:
                    ShowIntegerDialog(CB_STEP_INC, m_working_settings.m_anim_step.m_inc, 1, 7, "Step Increment", "The increment in the step size between patterns");
                    break;
                case R.id.step_speed:
                    ShowIntegerDialog(CB_STEP_SPEED, m_working_settings.m_anim_step.m_speed, 1, 30, "Step Speed", "The delay before updating the step size");
                    break;
                case R.id.rep_start:
                    ShowIntegerDialog(CB_REPEAT_START, m_working_settings.m_anim_repeats.m_start, 1, 120, "Starting Repeat", "The starting number of repeats");
                    break;
                case R.id.rep_end:
                    ShowIntegerDialog(CB_REPEAT_END, m_working_settings.m_anim_repeats.m_end, 3, 120, "End Repeat", "The last number of repeats");
                    break;
                case R.id.rep_inc:
                    ShowIntegerDialog(CB_REPEAT_INC, m_working_settings.m_anim_repeats.m_inc, 1, 7, "Repeat Increment", "The increment in the number of repeats between patterns");
                    break;
                case R.id.rep_speed:
                    ShowIntegerDialog(CB_REPEAT_SPEED, m_working_settings.m_anim_repeats.m_speed, 1, 30, "Repeat change delay", "The delay before updating the number of repeats");
                    break;
                case R.id.angle_start:
                    ShowIntegerDialog(CB_ANGLE_START, m_working_settings.m_anim_angle.m_start, 0, StarParameters.m_angles.length - 1, "Starting Angle", "The starting angle between repeats");
                    break;
                case R.id.angle_end:
                    ShowIntegerDialog(CB_ANGLE_END, m_working_settings.m_anim_angle.m_end, 0, StarParameters.m_angles.length - 1, "End Angle", "The last angle between repeats");
                    break;
                case R.id.angle_inc:
                    ShowIntegerDialog(CB_ANGLE_INC, m_working_settings.m_anim_angle.m_inc, 1, 7, "Angle Increment", "The increment in the angle between repeats");
                    break;
                case R.id.angle_speed:
                    ShowIntegerDialog(CB_ANGLE_SPEED, m_working_settings.m_anim_angle.m_speed, 1, 30, "Angle change delay", "The delay before updating the angle between repeats");
                    break;
                case R.id.shrink_start:
                    ShowIntegerDialog(CB_SHRINK_START, m_working_settings.m_anim_shrink.m_start, 1, StarParameters.m_shrink_pc.length - 1, "Starting Shrinkage", "The starting shrinkage between repeats");
                    break;
                case R.id.shrink_end:
                    ShowIntegerDialog(CB_SHRINK_END, m_working_settings.m_anim_shrink.m_end, 3, StarParameters.m_shrink_pc.length - 1, "End Shrinkage", "The last shrinkage between repeats");
                    break;
                case R.id.shrink_inc:
                    ShowIntegerDialog(CB_SHRINK_INC, m_working_settings.m_anim_shrink.m_inc, 1, 7, "Shrinkage Increment", "The increment in the shrinkage between repeats");
                    break;
                case R.id.shrink_speed:
                    ShowIntegerDialog(CB_SHRINK_SPEED, m_working_settings.m_anim_shrink.m_speed, 1, 30, "Shrinkage change delay", "The delay before updating the shrinkage between repeats");
                    break;
                case R.id.rotate_shape:
                    m_working_settings.m_anim_rotate.m_shape = AnimationSettings.nextShape(m_working_settings.m_anim_rotate.m_shape);
                    show_rotate_shape();
                    break;
                case R.id.points_shape:
                    m_working_settings.m_anim_points.m_shape = AnimationSettings.nextShape(m_working_settings.m_anim_points.m_shape);
                    show_points_shape();
                    break;
                case R.id.step_shape:
                    m_working_settings.m_anim_step.m_shape = AnimationSettings.nextShape(m_working_settings.m_anim_step.m_shape);
                    show_step_shape();
                    break;
                case R.id.rep_shape:
                    m_working_settings.m_anim_repeats.m_shape = AnimationSettings.nextShape(m_working_settings.m_anim_repeats.m_shape);
                    show_repeat_shape();
                    break;
                case R.id.angle_shape:
                    m_working_settings.m_anim_angle.m_shape = AnimationSettings.nextShape(m_working_settings.m_anim_angle.m_shape);
                    show_angle_shape();
                    break;
                case R.id.shrink_shape:
                    m_working_settings.m_anim_shrink.m_shape = AnimationSettings.nextShape(m_working_settings.m_anim_shrink.m_shape);
                    show_shrink_shape();
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
                case CB_STEP_START:
                    m_working_settings.m_anim_step.m_start = value;
                    show_step_start ();
                    break;
                case CB_STEP_END:
                    m_working_settings.m_anim_step.m_end = value;
                    show_step_end ();
                    break;
                case CB_STEP_INC:
                    m_working_settings.m_anim_step.m_inc = value;
                    show_step_inc ();
                    break;
                case CB_STEP_SPEED:
                    m_working_settings.m_anim_step.m_speed = value;
                    show_step_speed ();
                    break;
                case CB_REPEAT_START:
                    m_working_settings.m_anim_repeats.m_start = value;
                    show_repeat_start ();
                    break;
                case CB_REPEAT_END:
                    m_working_settings.m_anim_repeats.m_end = value;
                    show_repeat_end ();
                    break;
                case CB_REPEAT_INC:
                    m_working_settings.m_anim_repeats.m_inc = value;
                    show_repeat_inc ();
                    break;
                case CB_REPEAT_SPEED:
                    m_working_settings.m_anim_repeats.m_speed = value;
                    show_repeat_speed ();
                    break;
                case CB_ANGLE_START:
                    m_working_settings.m_anim_angle.m_start = value;
                    show_angle_start ();
                    break;
                case CB_ANGLE_END:
                    m_working_settings.m_anim_angle.m_end = value;
                    show_angle_end ();
                    break;
                case CB_ANGLE_INC:
                    m_working_settings.m_anim_angle.m_inc = value;
                    show_angle_inc ();
                    break;
                case CB_ANGLE_SPEED:
                    m_working_settings.m_anim_angle.m_speed = value;
                    show_angle_speed ();
                    break;
                case CB_SHRINK_START:
                    m_working_settings.m_anim_shrink.m_start = value;
                    show_shrink_start ();
                    break;
                case CB_SHRINK_END:
                    m_working_settings.m_anim_shrink.m_end = value;
                    show_shrink_end ();
                    break;
                case CB_SHRINK_INC:
                    m_working_settings.m_anim_shrink.m_inc = value;
                    show_shrink_inc ();
                    break;
                case CB_SHRINK_SPEED:
                    m_working_settings.m_anim_shrink.m_speed = value;
                    show_shrink_speed ();
                    break;
                case CB_BACKGROUND_COLOUR_START:
                    m_working_settings.m_anim_background.m_start = value;
                    show_background_start ();
                    break;
                case CB_BACKGROUND_COLOUR_END:
                    m_working_settings.m_anim_background.m_end = value;
                    show_background_end ();
                    break;
                case CB_L1_COLOUR_START:
                    m_working_settings.m_anim_line1.m_start = value;
                    show_line1_start ();
                    break;
                case CB_L1_COLOUR_END:
                    m_working_settings.m_anim_line1.m_end = value;
                    show_line1_end ();
                    break;
                case CB_L2_COLOUR_START:
                    m_working_settings.m_anim_line2.m_start = value;
                    show_line2_start ();
                    break;
                case CB_L2_COLOUR_END:
                    m_working_settings.m_anim_line2.m_end = value;
                    show_line2_end ();
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
    void show_step_start () { m_step_start.setText(Integer.toString (m_working_settings.m_anim_step.m_start)); }
    void show_step_end ()   { m_step_end.setText(Integer.toString (m_working_settings.m_anim_step.m_end)); }
    void show_step_inc ()   { m_step_inc.setText(Integer.toString (m_working_settings.m_anim_step.m_inc)); }
    void show_step_speed () { m_step_speed.setText(Integer.toString (m_working_settings.m_anim_step.m_speed)); }
    void show_repeat_start () { m_repeat_start.setText(Integer.toString (m_working_settings.m_anim_repeats.m_start)); }
    void show_repeat_end ()   { m_repeat_end.setText(Integer.toString (m_working_settings.m_anim_repeats.m_end)); }
    void show_repeat_inc ()   { m_repeat_inc.setText(Integer.toString (m_working_settings.m_anim_repeats.m_inc)); }
    void show_repeat_speed () { m_repeat_speed.setText(Integer.toString (m_working_settings.m_anim_repeats.m_speed)); }
    void show_angle_start () { m_angle_start.setText(Integer.toString (m_working_settings.m_anim_angle.m_start)); }
    void show_angle_end ()   { m_angle_end.setText(Integer.toString (m_working_settings.m_anim_angle.m_end)); }
    void show_angle_inc ()   { m_angle_inc.setText(Integer.toString (m_working_settings.m_anim_angle.m_inc)); }
    void show_angle_speed () { m_angle_speed.setText(Integer.toString (m_working_settings.m_anim_angle.m_speed)); }
    void show_shrink_start () { m_shrink_start.setText(Integer.toString (m_working_settings.m_anim_shrink.m_start)); }
    void show_shrink_end ()   { m_shrink_end.setText(Integer.toString (m_working_settings.m_anim_shrink.m_end)); }
    void show_shrink_inc ()   { m_shrink_inc.setText(Integer.toString (m_working_settings.m_anim_shrink.m_inc)); }
    void show_shrink_speed () { m_shrink_speed.setText(Integer.toString (m_working_settings.m_anim_shrink.m_speed)); }

    void show_background_start (){m_background_start.setBackgroundColor(m_working_settings.m_anim_background.m_start); }
    void show_background_end (){m_background_start.setBackgroundColor(m_working_settings.m_anim_background.m_end); }
    void show_line1_start (){m_background_start.setBackgroundColor(m_working_settings.m_anim_line1.m_start); }
    void show_line1_end (){m_background_start.setBackgroundColor(m_working_settings.m_anim_line1.m_end); }
    void show_line2_start (){m_background_start.setBackgroundColor(m_working_settings.m_anim_line2.m_start); }
    void show_line2_end (){m_background_start.setBackgroundColor(m_working_settings.m_anim_line2.m_end); }

    void show_rotate_shape () { m_rotate_shape.setImageResource(getShapeImage(m_working_settings.m_anim_rotate.m_shape)); }
    void show_points_shape () { m_points_shape.setImageResource(getShapeImage(m_working_settings.m_anim_points.m_shape)); }
    void show_step_shape ()   { m_step_shape.setImageResource(getShapeImage(m_working_settings.m_anim_step.m_shape)); }
    void show_repeat_shape () { m_repeat_shape.setImageResource(getShapeImage(m_working_settings.m_anim_repeats.m_shape)); }
    void show_angle_shape ()  { m_angle_shape.setImageResource(getShapeImage(m_working_settings.m_anim_angle.m_shape)); }
    void show_shrink_shape () { m_shrink_shape.setImageResource(getShapeImage(m_working_settings.m_anim_shrink.m_shape)); }

    /**
     * Make a shape to a drawable resource
     * @param s The shape
     * @return The resource id
     */
    int getShapeImage (AnimationSettings.Shape s) {

        if (s == AnimationSettings.Shape.WEDGE) return R.drawable.up_ramp;
        if (s == AnimationSettings.Shape.TOOTH) return R.drawable.up_and_down;
        if (s == AnimationSettings.Shape.RWEDGE) return R.drawable.down_ramp;
        return R.drawable.up_ramp;
    }
}