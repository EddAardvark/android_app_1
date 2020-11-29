package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.tutorialapp.R;
import com.patterns.StarParameters;

import java.util.Random;

public class StarsEvolve extends AppCompatActivity {

    public static String KEY_PARAMS = "params";
    public static String KEY_STATE = "state";

    final static double colour_change = 0.05;

    ImageView m_cell_11;
    ImageView m_cell_12;
    ImageView m_cell_13;
    ImageView m_cell_21;
    ImageView m_cell_22;
    ImageView m_cell_23;
    ImageView m_cell_31;
    ImageView m_cell_32;
    ImageView m_cell_33;

    Random m_random = new Random();

    StarParameters [] m_params = new StarParameters[9];

    StarsEvolve.EventListener m_listener = new EventListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stars_evolve);

        fromBundle(getIntent().getExtras());

        m_cell_11 = findViewById(R.id.image_1x1);
        m_cell_12 = findViewById(R.id.image_1x2);
        m_cell_13 = findViewById(R.id.image_1x3);
        m_cell_21 = findViewById(R.id.image_2x1);
        m_cell_22 = findViewById(R.id.image_2x2);
        m_cell_23 = findViewById(R.id.image_2x3);
        m_cell_31 = findViewById(R.id.image_3x1);
        m_cell_32 = findViewById(R.id.image_3x2);
        m_cell_33 = findViewById(R.id.image_3x3);

        m_cell_11.setOnClickListener(m_listener);
        m_cell_12.setOnClickListener(m_listener);
        m_cell_13.setOnClickListener(m_listener);
        m_cell_21.setOnClickListener(m_listener);
        m_cell_22.setOnClickListener(m_listener);
        m_cell_23.setOnClickListener(m_listener);
        m_cell_31.setOnClickListener(m_listener);
        m_cell_32.setOnClickListener(m_listener);
        m_cell_33.setOnClickListener(m_listener);

        draw();
    }
    /**
     * Create a bundle containing the Activity startup parameters
     * @param params The pattern parameters
     * @return A bundle
     */
    public static Bundle makeBundle (StarParameters params)
    {
        Bundle b = new Bundle();
        b.putBundle (KEY_PARAMS, params.toBundle());

        return b;
    }
    void fromBundle (Bundle b) {

        if (b != null) {

            Bundle b2 = b.getBundle(KEY_PARAMS);

            if (b2 != null) {
                m_params[0] = new StarParameters(512, 512);
                m_params[0].fromBundle(b2);
                evolve();
            }
        }
    }

    void evolve(){

        // 1-8 are the evolutions

        for (int i = 1 ; i < 9 ; ++i)
        {
            m_params[i] = m_params[0].clone ();
            randomise(i);
        }
    }
    /**
     * Ransomise one of the patters
     * @param idx the pattern
     */
    void randomise(int idx) {
        m_params[idx].Randomise();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBundle(KEY_STATE, makeBundle (m_params[0]));
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Bundle b =  savedInstanceState.getBundle(KEY_STATE);

        fromBundle (b);
        draw();
    }

    void draw() {
        draw(R.id.image_1x1, 1);
        draw(R.id.image_1x2, 2);
        draw(R.id.image_1x3, 3);
        draw(R.id.image_2x1, 4);
        draw(R.id.image_2x2, 0);
        draw(R.id.image_2x3, 5);
        draw(R.id.image_3x1, 6);
        draw(R.id.image_3x2, 7);
        draw(R.id.image_3x3, 8);
    }

    void draw(int id, int idx){
        ImageView img = findViewById(id);
        m_params[idx].Draw (getResources(), img);
    }

    void setPattern(int idx){
        m_params[0] = m_params [idx];
        evolve();
        draw ();
        StarParameters.setEvolutionParameters(m_params[0].clone());
    }

    public class EventListener extends Activity implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.image_1x1:
                    setPattern (1);
                    break;
                case R.id.image_1x2:
                    setPattern (2);
                    break;
                case R.id.image_1x3:
                    setPattern (3);
                    break;
                case R.id.image_2x1:
                    setPattern (4);
                    break;
                case R.id.image_2x2:
                    setPattern (0);
                    break;
                case R.id.image_2x3:
                    setPattern (5);
                    break;
                case R.id.image_3x1:
                    setPattern (6);
                    break;
                case R.id.image_3x2:
                    setPattern (7);
                    break;
                case R.id.image_3x3:
                    setPattern (8);
                    break;
            }
        }
    }

}


