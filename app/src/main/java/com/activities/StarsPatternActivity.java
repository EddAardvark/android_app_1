package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;

import com.dialogs.FragmentSet;
import com.dialogs.GetColour;
import com.dialogs.GetInteger;
import com.dialogs.ManageStarSettings;
import com.misc.ColourHelpers;
import com.misc.Misc;
import com.patterns.AnimateSet;
import com.patterns.PatternSet;
import com.patterns.RandomSet;
import com.patterns.StarAnimationFragment;
import com.patterns.StarParameters;
import com.patterns.StarPatternFragment;
import com.patterns.StarRandomiserFragment;

import java.util.Locale;

public class StarsPatternActivity extends AppCompatActivity implements GetInteger.Result, GetColour.Result, ManageStarSettings.Result {

    final int CB_N1 = 1;
    final int CB_N2 = 2;
    final int CB_N3 = 3;
    final int CB_ANGLE = 4;
    final int CB_SHRINK = 5;
    final int CB_BACKCOLOUR = 6;
    final int CB_LINECOLOUR1 = 7;
    final int CB_LINECOLOUR2 = 8;
    final int CB_SETTINGS = 9;

    StarParameters m_params = new StarParameters();
    boolean m_in_animation = false;

    EventListener m_listener = new StarsPatternActivity.EventListener();
    View m_layout_background;
    View m_layout_foreground1;
    View m_layout_foreground2;
    View m_pause_button;
    View m_resume_button;
    TextView m_layout_background_text;
    TextView m_layout_foreground1_text;
    TextView m_layout_foreground2_text;

    Handler m_timer_handler = new Handler();
    Runnable m_timer_runnable = new Runnable() {
        @Override
        public void run() {
            animate();
            m_timer_handler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            m_params.fromBundle(savedInstanceState);
        }

        setContentView(R.layout.activity_stars_pattern);

        findViewById(R.id.layout_n1).setOnClickListener(m_listener);
        findViewById(R.id.layout_n2).setOnClickListener(m_listener);
        findViewById(R.id.layout_n3).setOnClickListener(m_listener);
        findViewById(R.id.layout_angle).setOnClickListener(m_listener);
        findViewById(R.id.share_pattern).setOnClickListener(m_listener);
        findViewById(R.id.random_picture).setOnClickListener(m_listener);
        findViewById(R.id.evolve_star).setOnClickListener(m_listener);
        findViewById(R.id.layout_shrink).setOnClickListener(m_listener);
        findViewById(R.id.edit_settings).setOnClickListener(m_listener);
        findViewById(R.id.app_info).setOnClickListener(m_listener);

        m_layout_background = findViewById(R.id.layout_back_colour);
        m_layout_foreground1 = findViewById(R.id.layout_fore_colour1);
        m_layout_foreground2 = findViewById(R.id.layout_fore_colour2);
        m_layout_background_text = (TextView) findViewById(R.id.text_backcolour);
        m_layout_foreground1_text = (TextView) findViewById(R.id.text_forecolour1);
        m_layout_foreground2_text = (TextView) findViewById(R.id.text_forecolour2);
        m_pause_button = findViewById(R.id.pause_animation);
        m_resume_button = findViewById(R.id.resume_animation);

        m_layout_background.setOnClickListener(m_listener);
        m_layout_foreground1.setOnClickListener(m_listener);
        m_layout_foreground2.setOnClickListener(m_listener);
        m_pause_button.setOnClickListener(m_listener);
        m_resume_button.setOnClickListener(m_listener);

        setAnimationState(false);
        Update();
    }

    /**
     * Part of the activity lifecycle, we stop the animation events but dont turn animation off, so that when resume is called
     * we can start up again
     */
    @Override
    public void onPause() {
        super.onPause();
        if (m_in_animation) {
            stopAnimation();
        }
    }

    /**
     * Part of the activity lifecycle. If animation was active when we were paused we resume it here.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (m_in_animation) {
            startAnimation();
        }
        // In case anything changed while we were paused
        draw();
    }

    /**
     * Start the timer events that drive the animation
     */
    void startAnimation() {
        m_timer_handler.postDelayed(m_timer_runnable, 0);
    }

    /**
     * Stop the timer events that drive the animation
     */
    void stopAnimation() {
        m_timer_handler.removeCallbacks(m_timer_runnable);
    }

    /**
     * Update the animation flag to the new state and adjust the pause and play buttons accordingly
     *
     * @param active The new animation state.
     */
    void setAnimationState(boolean active) {
        m_in_animation = active;

        if (m_in_animation) {
            m_resume_button.setVisibility(View.GONE);
            m_pause_button.setVisibility(View.VISIBLE);
        } else {
            m_pause_button.setVisibility(View.GONE);
            m_resume_button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Bundle params = savedInstanceState.getBundle("params");

        m_in_animation = savedInstanceState.getBoolean("animate", m_in_animation);

        if (params != null) {
            m_params.fromBundle(params);
        }
        Update();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("animate", m_in_animation);
        outState.putBundle("params", m_params.toBundle());
    }

    /**
     * Redraw the controls and the image
     */
    void Update() {
        setColours();
        showSettings();
        draw();
    }

    void setColours() {

        m_layout_background.setBackgroundColor(m_params.m_background);
        m_layout_foreground1.setBackgroundColor(m_params.m_first_line);
        m_layout_foreground2.setBackgroundColor(m_params.m_last_line);

        m_layout_background_text.setTextColor(ColourHelpers.GetContrastColour(m_params.m_background));
        m_layout_foreground1_text.setTextColor(ColourHelpers.GetContrastColour(m_params.m_first_line));
        m_layout_foreground2_text.setTextColor(ColourHelpers.GetContrastColour(m_params.m_last_line));
    }

    void draw() {
        // If we have just returned from an evolution this will recover the most recent generation

        StarParameters eparams = StarParameters.detachEvolutionParameters();

        if (eparams != null) {
            m_params = eparams;
        }
        ImageView img = findViewById(R.id.main_image);
        m_params.draw(getResources(), img);
        showSettings();
    }

    private void share() {
        Misc.share(this, m_params.makeFileName(), m_params.bitmap());
    }

    void showSettings() {
        ((TextView) findViewById(R.id.text_n1)).setText(String.format(Locale.getDefault(), "%d", m_params.m_n1));
        ((TextView) findViewById(R.id.text_n2)).setText(String.format(Locale.getDefault(), "%d", m_params.m_n2));
        ((TextView) findViewById(R.id.text_n3)).setText(String.format(Locale.getDefault(), "%d", m_params.m_n3));
        ((TextView) findViewById(R.id.text_da)).setText(m_params.getAngleString());
        ((TextView) findViewById(R.id.text_sh)).setText(m_params.getShrinkString());
    }

    void animate() {
        if (m_params.animate(m_params.m_animate)) {
            Update();
        }
    }

    /**
     * Allows you to change the number of points around the circle
     */
    void onClickN1() {
        GetInteger dialog = GetInteger.construct(this, CB_N1, m_params.m_n1, 3, 101, 1, getString(R.string.star_n1_title), getString(R.string.star_n1_description));
        dialog.show(getSupportFragmentManager(), "Hello");
    }

    /**
     * Allows you to change the number of points moved for each vector in the star. '1' draws the base polygon.
     */
    void onClickN2() {
        int max_n2 = m_params.m_n1 / 2;
        int n2 = (m_params.m_n2 > max_n2) ? 1 : max_n2;
        GetInteger dialog = GetInteger.construct(this, CB_N2, n2, 1, max_n2, 1, getString(R.string.star_n2_title), getString(R.string.star_n2_description));
        dialog.show(getSupportFragmentManager(), "Hello");
    }

    /**
     * Allows you to change the number of points moved for each vector in the star. '1' draws the base polygon.
     */
    void onClickN3() {
        GetInteger dialog = GetInteger.construct(this, CB_N3, m_params.m_n3, 1, 120, 1, getString(R.string.star_n3_title), getString(R.string.star_n3_description));
        dialog.show(getSupportFragmentManager(), "Hello");
    }

    /**
     * Allows you to change the number of points moved for each vector in the star. '1' draws the base polygon.
     */
    void onClickAngle() {

        GetInteger dialog = GetInteger.construct(this, CB_ANGLE, m_params.m_angle_idx, StarParameters.m_angle_str, getString(R.string.star_rotate_title), getString(R.string.star_angle_description));
        dialog.show(getSupportFragmentManager(), "Hello");
    }

    /**
     * Allows you to change the number of points moved for each vector in the star. '1' draws the base polygon.
     */
    void onClickShrink() {
        GetInteger dialog = GetInteger.construct(this, CB_SHRINK, m_params.m_shrink_idx, StarParameters.m_shrink_str, getString(R.string.star_shrink_title), getString(R.string.star_shrink_description));
        dialog.show(getSupportFragmentManager(), "Hello");
    }

    /**
     * Change the pattern settings
     */
    void onClickSettings() {
        ManageStarSettings dialog = ManageStarSettings.construct(this, CB_SETTINGS, m_params);
        dialog.show(getSupportFragmentManager(), "Hello");
    }

    /**
     * Allows you to change the background colour.
     */
    void onClickBackColour() {
        GetColour dialog = GetColour.construct(this, CB_BACKCOLOUR, m_params.m_background, getString(R.string.star_background_title), getString(R.string.star_background_description));
        dialog.show(getSupportFragmentManager(), "Hello");
    }

    /**
     * Allows you to change the main line colour.
     */
    void onClickLineColour1() {
        GetColour dialog = GetColour.construct(this, CB_LINECOLOUR1, m_params.m_first_line, getString(R.string.star_first_colour_title), getString(R.string.star_first_colour_description));
        dialog.show(getSupportFragmentManager(), "Hello");
    }

    /**
     * Allows you to change the second line colour.
     */
    void onClickLineColour2() {
        GetColour dialog = GetColour.construct(this, CB_LINECOLOUR2, m_params.m_last_line, getString(R.string.star_last_colour_title), getString(R.string.star_last_colour_description));
        dialog.show(getSupportFragmentManager(), "Hello");
    }

    @Override
    public void SetInteger(int id, int value) {
        switch (id) {
            case CB_N1:
                m_params.m_n1 = value;
                break;
            case CB_N2:
                m_params.m_n2 = value;
                break;
            case CB_N3:
                m_params.m_n3 = value;
                break;
            case CB_ANGLE:
                m_params.m_angle_idx = value;
                break;
            case CB_SHRINK:
                m_params.m_shrink_idx = value;
                break;
        }
        draw();
    }

    @Override
    public void SetColour(int id, int value) {
        switch (id) {
            case CB_BACKCOLOUR:
                m_params.m_background = value;
                break;
            case CB_LINECOLOUR1:
                m_params.m_first_line = value;
                break;
            case CB_LINECOLOUR2:
                m_params.m_last_line = value;
                break;
        }
        setColours();
        draw();
    }

    @Override
    public void UpdateSettings(FragmentSet result) {

        m_params.Apply (result);

        draw();
    }

    void showInfoPage() {
        Intent intent = new Intent(this, InfoPageActivity.class);

        Bundle b = new Bundle();
        b.putInt("title", R.string.stars_page_label);
        b.putInt("url", R.string.stars_page_url);
        intent.putExtras(b);

        startActivity(intent);
    }

    void showEvolvePage() {
        Intent intent = new Intent(this, StarsEvolve.class);
        Bundle b = StarsEvolve.makeBundle(m_params);
        intent.putExtras(b);
        startActivity(intent);
    }

    public class EventListener extends Activity implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.share_pattern) {
                share();
            } else if (id == R.id.layout_n1) {
                onClickN1();
            } else if (id == R.id.layout_n2) {
                onClickN2();
            } else if (id == R.id.layout_n3) {
                onClickN3();
            } else if (id == R.id.layout_angle) {
                onClickAngle();
            } else if (id == R.id.layout_shrink) {
                onClickShrink();
            } else if (id == R.id.random_picture) {
                m_params.Randomise(m_params.m_randomise);
                Update();
            } else if (id == R.id.evolve_star) {
                showEvolvePage();
            } else if (id == R.id.edit_settings) {
                onClickSettings();
            } else if (id == R.id.layout_back_colour) {
                onClickBackColour();
            } else if (id == R.id.layout_fore_colour1) {
                onClickLineColour1();
            } else if (id == R.id.layout_fore_colour2) {
                onClickLineColour2();
            } else if (id == R.id.app_info) {
                showInfoPage();
            } else if (id == R.id.pause_animation) {
                stopAnimation();
                setAnimationState(false);
            } else if (id == R.id.resume_animation) {
                startAnimation();
                setAnimationState(true);
            }
        }
    }
}