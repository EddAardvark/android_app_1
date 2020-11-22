package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.tutorialapp.R;
import com.patterns.PatternSet;
import com.patterns.StarParameters;

public class StarsEvolve extends AppCompatActivity {

    public static String KEY_COLOURING_MODE = "cmode";
    public static String KEY_PARAMS = "params";

    ImageView m_cell_11;
    ImageView m_cell_12;
    ImageView m_cell_13;
    ImageView m_cell_21;
    ImageView m_cell_22;
    ImageView m_cell_23;
    ImageView m_cell_31;
    ImageView m_cell_32;
    ImageView m_cell_33;
    StarParameters m_params = new StarParameters(512,512);
    PatternSet.ColouringMode m_cmode = PatternSet.ColouringMode.INWARDS;

    StarsEvolve.EventListener m_listener = new EventListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stars_evolve);

        if (savedInstanceState != null)
        {
            fromBundle(savedInstanceState);
        }
        else
        {
            Bundle b = getIntent().getExtras();

            if (b != null){
                fromBundle(b);
            }
        }

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

        Draw ();
    }

    /**
     * Create a bundle containing the Activity startup parameters
     * @param params The pattern parameters
     * @param cmode The colouring mode
     * @return A bundle
     */
    public static Bundle makeBundle (StarParameters params, PatternSet.ColouringMode cmode)
    {
        Bundle b = new Bundle();
        b.putBundle (KEY_PARAMS, params.toBundle());
        b.putSerializable(KEY_COLOURING_MODE, cmode);

        return b;
    }
    void fromBundle (@NonNull Bundle b) {
        m_params.fromBundle(b.getBundle(KEY_PARAMS));
        m_cmode = (PatternSet.ColouringMode) b.getSerializable(KEY_COLOURING_MODE);
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState = makeBundle (m_params, m_cmode);
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        fromBundle (savedInstanceState);
    }

    void Draw () {
        Draw(R.id.image_1x1);
        Draw(R.id.image_1x2);
        Draw(R.id.image_1x3);
        Draw(R.id.image_2x1);
        Draw(R.id.image_2x2);
        Draw(R.id.image_2x3);
        Draw(R.id.image_3x1);
        Draw(R.id.image_3x2);
        Draw(R.id.image_3x3);
    }

    void Draw (int id){
        ImageView img = findViewById(id);
        m_params.Draw (getResources(), img, m_cmode);
    }


    public static class EventListener extends Activity implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.image_1x1:
                case R.id.image_1x2:
                case R.id.image_1x3:
                case R.id.image_2x1:
                case R.id.image_2x2:
                case R.id.image_2x3:
                case R.id.image_3x1:
                case R.id.image_3x2:
                case R.id.image_3x3:
                    break;
            }
        }
    }

}


