package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.dialogs.GetColour;
import com.activities.R;
import com.misc.Misc;
import com.patterns.TileParameters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class TilePatternsActivity extends AppCompatActivity implements GetColour.Result {

    boolean m_in_animation = false;

    final int CB_TILE_COLOUR_FIRST = 1;

    View m_pause_button;
    View m_resume_button;
    TilePatternsActivity.EventListener m_listener = new TilePatternsActivity.EventListener();
    TileParameters m_params = new TileParameters(1024,1024);
    ImageView m_template_view;
    ImageView m_colour_view;
    ImageView m_transform_map;

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
            // reload state
        }

        setContentView(R.layout.activity_tile_patterns);

        m_pause_button = findViewById(R.id.pause_animation);
        m_resume_button = findViewById(R.id.resume_animation);

        m_pause_button.setOnClickListener(m_listener);
        m_resume_button.setOnClickListener(m_listener);

        findViewById(R.id.share_pattern).setOnClickListener(m_listener);
        findViewById(R.id.random_picture).setOnClickListener(m_listener);
        findViewById(R.id.evolve_star).setOnClickListener(m_listener);
        findViewById(R.id.edit_settings).setOnClickListener(m_listener);
        findViewById(R.id.app_info).setOnClickListener(m_listener);
        findViewById(R.id.back_one).setOnClickListener(m_listener);

        m_template_view = findViewById(R.id.tile_template);
        m_colour_view = findViewById(R.id.colour_template);
        m_transform_map = findViewById(R.id.transform_map);

        m_template_view.setOnClickListener(m_listener);
        m_colour_view.setOnClickListener(m_listener);
        m_transform_map.setOnClickListener(m_listener);

        m_template_view.setOnTouchListener(m_listener);
        m_colour_view.setOnTouchListener(m_listener);
        m_transform_map.setOnTouchListener(m_listener);

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
     * Choose a new colour for one of the template shapes
     * @param pos Where the user clicked
     */
    void select_colour (float [] pos)
    {
        int idx  = (pos [0] > 0.5 ? 2 : 0) + (pos [1] > 0.5 ? 1 : 0);
        int colour = m_params.get_colour (idx);

        GetColour dialog = GetColour.construct(this, CB_TILE_COLOUR_FIRST + idx, colour, getString(R.string.tile_colour), getString(R.string.tile_colour_description));
        dialog.show(getSupportFragmentManager(), "Hello");
    }    /**
     * Choose a new colour for one of the template shapes
     * @param pos Where the user clicked
     */
    void select_transform (float [] pos)
    {
        int idx  = (pos [1] > 0.5 ? 0 : 5) + (int)(5 * pos [0]);
        char op = "ABCDEFGHIJ".charAt(idx);
        m_params.add_op (op);
        Update ();
    }


    /**
     * Choose a new sjape for one of the template slots
     * @param pos Where the user clicked
     */
    void select_shape (float [] pos)
    {
        int idx  = (pos [0] > 0.5 ? 2 : 0) + (pos [1] > 0.5 ? 1 : 0);
        m_params.next_shape (idx);
        Update ();
    }
    /**
     * Redraw the controls and the image
     */
    void Update() {

        ImageView img_main = findViewById(R.id.main_image);

        m_params.draw_template(getResources(), m_template_view);
        m_params.draw_template_colours(getResources(), m_colour_view);
        m_params.draw_transform_map(getResources(), m_transform_map);
        m_params.draw(getResources(), img_main);
    }

    private void share() {

        Misc.share(this, m_params.makeFileName(), m_params.bitmap());
    }

    void animate() {
        Update();
    }

    /**
     * Change the pattern settings
     */
    void onClickSettings() {
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
    }

    @Override
    public void SetColour(int id, int value) {
        switch (id) {
            case CB_TILE_COLOUR_FIRST:
            case CB_TILE_COLOUR_FIRST+1:
            case CB_TILE_COLOUR_FIRST+2:
            case CB_TILE_COLOUR_FIRST+3:
                m_params.set_tile_colour (id - CB_TILE_COLOUR_FIRST, value);
                Update ();
                break;
        }
    }

    public class EventListener extends Activity implements View.OnClickListener, View.OnTouchListener {

        @Override
        public void onClick(View view) {

            int id = view.getId();

            if (id == R.id.share_pattern) {
                share();
            } else if (id == R.id.evolve_star) {
                showEvolvePage();
            } else if (id == R.id.edit_settings) {
                onClickSettings();
            } else if (id == R.id.app_info) {
                showInfoPage();
            } else if (id == R.id.pause_animation) {
                stopAnimation();
                setAnimationState(false);
            } else if (id == R.id.resume_animation) {
                startAnimation();
                setAnimationState(true);
            } else if (id == R.id.back_one) {
                m_params.back_one();
                Update();
            }
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:   // first finger down only
                    float [] pos = get_pos(view, event);
                    int id = view.getId();

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            if (id == R.id.tile_template)
                    {
                        select_shape (pos);
                    }
                    else if (id == R.id.colour_template)
                    {
                        select_colour (pos);
                    }
                    else if (id == R.id.transform_map)
                    {
                        select_transform (pos);
                    }

                    break;

                case MotionEvent.ACTION_UP: // first finger lifted
                    break;

                case MotionEvent.ACTION_POINTER_UP: // second finger lifted
                    break;

                case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down
                    break;

                case MotionEvent.ACTION_MOVE:
                    break;
            }

            return true; // indicate event was handled
        }

        /**
         * Work out which image has been clicked on. The bitmap is centred in the image and uses the full
         * extent of the smaller dimension. Touch is relative to the image.
         *
         * @param v     The image
         * @param event The event
         */
        float [] get_pos(View v, MotionEvent event) {

            return new float [] { event.getX() /  v.getWidth(), event.getY() /  v.getHeight() };
        }
    }
}
