package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.JWPatterns.R;
import com.patterns.StarParameters;

import java.util.Random;

public class StarsEvolve extends AppCompatActivity {

    public static String KEY_PARAMS = "params";
    public static String KEY_STATE = "state";

    final static int m_grid = 3;
    final static int m_cells = m_grid * m_grid;
    final static int m_width = 1200;
    final static int m_height = 1200;
    final static int m_cell_width = m_width / m_grid;
    final static int m_cell_height = m_height / m_grid;
    final static int m_centre = (m_grid + 1) * (m_grid / 2);
    final static double m_radius = m_cell_width * 0.49;

    ImageView m_evolve_view;
    Bitmap m_bmp = Bitmap.createBitmap(m_width, m_height, Bitmap.Config.RGB_565);
    Random m_random = new Random();

    StarParameters[] m_params = new StarParameters[m_cells];

    StarsEvolve.EventListener m_listener = new EventListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stars_evolve);

        fromBundle(getIntent().getExtras());

        m_evolve_view = findViewById(R.id.evolve_image);

        m_evolve_view.setOnClickListener(m_listener);
        m_evolve_view.setOnTouchListener(m_listener);

        draw();
    }

    /**
     * Create a bundle containing the Activity startup parameters
     *
     * @param params The pattern parameters
     * @return A bundle
     */
    public static Bundle makeBundle(StarParameters params) {
        Bundle b = new Bundle();
        b.putBundle(KEY_PARAMS, params.toBundle());

        return b;
    }

    void fromBundle(Bundle b) {

        if (b != null) {

            Bundle b2 = b.getBundle(KEY_PARAMS);

            if (b2 != null) {
                m_params[m_centre] = new StarParameters(512, 512);
                m_params[m_centre].fromBundle(b2);
                evolve();
            }
        }
    }

    void evolve() {

        // 1-8 are the evolutions

        for (int i = 0; i < m_cells; ++i) {
            if (i != m_centre) {

                m_params[i] = m_params[m_centre].clone();
                m_params[i].Randomise();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Only save the centre one, the others are recreated using evolve
        outState.putBundle(KEY_STATE, makeBundle(m_params[m_centre]));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Bundle b = savedInstanceState.getBundle(KEY_STATE);

        fromBundle(b);
        draw();
    }

    /**
     * Draws all 9 images into the same bitmap
     */
    void draw() {

        Rect rect = new Rect(0, 0, m_width, m_height);
        Canvas canvas = new Canvas(m_bmp);
        Paint paint = new Paint();

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(rect, paint);

        for (int i = 0; i < 9; ++i) {
            int x0 = m_cell_width * (i % m_grid);
            int y0 = m_cell_height * (i / m_grid);
            int xc = x0 + m_cell_width / 2;
            int yc = y0 + m_cell_height / 2;

            Rect rect2 = new Rect(x0, y0, x0 + m_cell_width, y0 + m_cell_height);
            m_params[i].draw_background(paint, canvas, rect2);
            m_params[i].draw(paint, canvas, xc, yc, m_radius);
        }

        m_evolve_view.setImageDrawable(new BitmapDrawable(getResources(), m_bmp));
    }

    void setPattern(int idx) {
        m_params[m_centre] = m_params[idx];
        evolve();
        draw();
        StarParameters.setEvolutionParameters(m_params[m_centre].clone());
    }

    public class EventListener extends Activity implements View.OnClickListener, View.OnTouchListener {

        @Override
        public void onClick(View view) {
            int id = view.getId();
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:   // first finger down only
                    get_pos(v, event);
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
    }


    /**
     * Work out which image has been clicked on. The bitmap is centred in the image and uses the full
     * extent of the smaller dimension. Touch is relative to the image.
     *
     * @param v     The image
     * @param event The event
     */
    void get_pos(View v, MotionEvent event) {

        int touch_x = (int) event.getX();
        int touch_y = (int) event.getY();

        double img_width = v.getWidth();
        double img_height = v.getHeight();
        double bm_size = Math.min(img_width, img_height);
        double bm_xc = img_width / 2;
        double bm_yc = img_height / 2;
        double bm_x0 = bm_xc - bm_size / 2;
        double bm_y0 = bm_yc - bm_size / 2;

        double x = (touch_x - bm_x0) * m_grid / bm_size;
        double y = (touch_y - bm_y0) * m_grid / bm_size;

        int ix = (int) x;
        int iy = (int) y;
        int idx = m_grid * iy + ix;

        setPattern(idx);
    }
}



