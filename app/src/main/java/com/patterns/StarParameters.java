package com.patterns;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tutorialapp.R;
import com.misc.ColourHelpers;
import com.misc.MyMath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StarParameters {

    public int m_n1 = 5;                        ///< Number of points in the circle
    public int m_n2 = 2;                        ///< Increment when joining points
    public int m_n3 = 1;                        ///< The number of copies to draw, with rotation
    public int m_angle_idx = 5;                 ///< Rotation when drawing multiple images
    public int m_shrink_idx = 5;                ///< Shrinkage when drawing multiple images (percent)
    public int m_background = Color.WHITE;      ///< Background colour
    public int m_first_line = Color.BLUE;       ///< Foreground ground colour
    public int m_last_line = Color.MAGENTA;     ///< Second foreground ground colour when blending

    double m_rotate = 0;                        ///< Rotates the whole pattern, used in animation
    long m_counter = 0;                         ///< Used in animation

    String KEY_N1 = "n1";
    String KEY_N2 = "n2";
    String KEY_N3 = "n3";
    String KEY_ROTATE = "rot";
    String KEY_SHRINK = "shrnk";
    String KEY_BACKGROUND = "bg";
    String KEY_LINE1 = "l1";
    String KEY_LINE2 = "l2";

    int m_width;
    int m_height;
    Bitmap m_bmp;

    public final static ArrayList<String> m_angle_str = new ArrayList<String>( Arrays.asList(
            "0", "0.05°", "0.1°", "0.2°", "0.3°", "0.4°", "0.5°", "1°", "2°", "3°", "4°", "5°", "6°", "10°", "12°", "15°", "20°", "30°", "45°", "60°", "72°", "90°", "108°", "120°", "144°", "180°",
            "-144°", "-120°", "-108°", "-90°", "-72°", "-60°", "-45°", "-30°", "-20°", "-15°", "-12°", "-10°", "-6°", "-5°", "-4°", "-3°", "-2°", "-1°", "-0.5°", "-0.4°", "-0.3°", "-0.2°", "-0.1°", "-0.05°"));

    public final static double [] m_angles = {
            0, 0.05, 0.1, 0.2, 0.3, 0.4, 0.5, 1, 2, 3, 4, 5, 6, 10, 12, 15, 20, 30, 45, 60, 72, 90, 108, 120, 144, 180,
            216, 240, 252, 270, 288, 300, 315, 330, 340, 345, 348, 350, 354, 355, 356, 357, 358, 359, 359.5, 359.6, 359.7, 359.8, 359.9, 359.95 };

    public final static ArrayList<String> m_shrink_str = new ArrayList<String>( Arrays.asList(
            "none", "0.05%", "0.1%", "0.2%", "0.25%", "0.3%", "0.4%", "0.5%", "0.6%", "0.7%", "0.8%", "0.9%", "1%", "1.5%", "2%", "3%",
            "4%", "5%", "6%", "7%", "8%", "9%", "10%", "15%", "20%", "25%", "30%", "40%", "50%", "60%", "70%", "75%"));

    public final static double [] m_shrink_pc = {
            0, 0.05, 0.1, 0.2, 0.25, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1, 1.5, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 40, 50, 60, 70, 75 };

    public StarParameters (int w, int h)
    {
        setSize (w, h);
    }

    public String getAngleString () { return m_angle_str.get(m_angle_idx);}
    public String getShrinkString () { return m_shrink_str.get(m_shrink_idx);}

    /**
     * Sets the size of the bitmap drawn. This doesn't affect the size no the screen but does change the detail when the image is shared.
     * @param w Width in pixels
     * @param h Height in pixels
     */
    public void setSize (int w, int h)
    {
        m_width = w;
        m_height = h;

        m_bmp = Bitmap.createBitmap(m_width, m_height, Bitmap.Config.RGB_565);
    }

    public void Randomise(RandomSet settings)
    {
        Random rand = new Random();

        if (settings.m_randomise_num_points) {
            m_n1 = rand.nextInt(98) + 3;
        }

        if (settings.m_randomise_point_step)
        {
            m_n2 = rand.nextInt(m_n1-1) + 1;
        }

        if (settings.m_randomise_num_points || settings.m_randomise_point_step) {

            int hcf = MyMath.hcf(m_n1, m_n2);

            if (hcf > 1) {
                m_n1 /= hcf;
                m_n2 /= hcf;
            }
            if (m_n1 < 3) {
                ++m_n1;
            }
        }

        if (settings.m_randomise_repeats)
        {
            m_n3 = rand.nextInt(119 + 1);
        }

        if (settings.m_randomise_angles)
        {
            m_angle_idx = rand.nextInt(m_angles.length);
        }

        if (settings.m_randomise_shrinkage)
        {
            m_shrink_idx = rand.nextInt(m_shrink_pc.length);
        }

        if (settings.m_randomise_backround_colours)
        {
            m_background = ColourHelpers.random_solid_colour ();
        }

        if (settings.m_randomise_forground_colours)
        {
            m_first_line = ColourHelpers.random_solid_colour ();
            m_last_line = ColourHelpers.random_solid_colour ();
        }
    }

    public Bitmap bitmap ()
    {
        return m_bmp;
    }

    /**
     * Draws the pattern using the current parameters
     * @param resources
     * @param img
     * @param colour_mode
     */
    public void Draw(Resources resources, ImageView img, PatternSet.ColouringMode colour_mode) {

        Rect rect = new Rect(0, 0, m_width, m_height);
        Canvas canvas = new Canvas(m_bmp);
        Paint paint = new Paint();
        paint.setColor(m_background);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(rect, paint);

        switch (colour_mode) {
            case BOTH:
                DrawBoth(paint, canvas);
                break;
            case INWARDS:
                DrawInwards(paint, canvas);
                break;
            case AROUND:
                DrawAround(paint, canvas);
                break;
            case ALTERNATE:
                DrawAlternate(paint, canvas);
                break;
        }

        int h = m_height/40;

        if (h >= 8) {

            int colour = ColourHelpers.GetContrastColour (m_background);
            int m = m_height/80;

            paint.setColor(colour);
            paint.setTextSize(h);
            paint.setTextAlign(Paint.Align.LEFT);

            String star = "(" + m_n1 + "," + m_n2 + ")";

            canvas.drawText(star, m, h + m, paint);

            if (m_n3 > 1) {

                paint.setTextAlign(Paint.Align.RIGHT);
                String settings = Integer.toString(m_n3) + " x " + getAngleString() + " x " + getShrinkString();
                canvas.drawText(settings, m_width - m, h + m, paint);
                paint.setTextAlign(Paint.Align.LEFT);
            }

            colour = ColourHelpers.MakeTransparent(colour, 0.5);
            paint.setTextSize((int)(h * 1.3));
            paint.setColor(colour);
            canvas.drawText("www.eddaardvark.co.uk", m, m_height - m, paint);
        }

        // Render

        img.setImageDrawable(new BitmapDrawable(resources, m_bmp));
    }

    /**
     * Colour progresses from the first colour to the second as we move inwards
     * @param paint
     * @param canvas
     */
    void DrawInwards(Paint paint, Canvas canvas) {

        double xc = m_width * 0.5;
        double yc = m_height * 0.5;
        double r = m_height * 0.48;
        double theta = Math.PI * 2 / m_n1;
        double rotate = Math.PI * m_angles[m_angle_idx] / 180;
        double shrink = (100 - m_shrink_pc [m_shrink_idx]) * 0.01;

        for (int j = 0; j < m_n3; ++j) {

            int n = 0;
            double t2 = rotate * j + m_rotate;
            double c = Math.cos(t2);
            double s = Math.sin(t2);
            double x0 = xc + r * c;
            double y0 = yc + r * s;
            double f = (m_n3 > 1) ? 1.0 - (double) j / (m_n3 - 1) : 1.0;

            paint.setColor(ColourHelpers.Blend(m_first_line, m_last_line, f));

            for (int i = 0; i <= m_n1; i++) {
                n += m_n2;
                double x = Math.cos(n * theta);
                double y = Math.sin(n * theta);
                double x1 = xc + r * (c * x + s * y);
                double y1 = yc + r * (s * x - c * y);
                canvas.drawLine((float) x0, (float) y0, (float) x1, (float) y1, paint);
                x0 = x1;
                y0 = y1;
            }
            r *= shrink;
        }
    }

    /**
     * Colour advances as we move around the pattern
     * @param paint
     * @param canvas
     */
    void DrawAround (Paint paint, Canvas canvas) {

        double xc = m_width * 0.5;
        double yc = m_height * 0.5;
        double r = m_height * 0.48;
        double theta = Math.PI * 2 / m_n1;
        double rotate = Math.PI * m_angles[m_angle_idx] / 180;
        double shrink = (100 - m_shrink_pc [m_shrink_idx]) * 0.01;
        List<Integer> colours = new ArrayList<>(m_n1);

        // Initialise the colours used in the inner loop

        for (int i = 0; i < m_n1; i++) {

            int mid = m_n1 / 2;
            int n = (i <= mid) ? i : (m_n1 - i);
            double f = 1.0 - (double) n / mid;

            int colour = ColourHelpers.Blend(m_first_line, m_last_line, f);
            colours.add(colour);
        }
        // Draw

        for (int j = 0; j < m_n3; ++j) {

            int n = 0;
            double t2 = rotate * j + m_rotate;
            double c = Math.cos(t2);
            double s = Math.sin(t2);
            double x0 = xc + r * c;
            double y0 = yc + r * s;

            for (int i = 0; i < m_n1; i++) {

                paint.setColor(colours.get(i));

                n += m_n2;
                double x = Math.cos(n * theta);
                double y = Math.sin(n * theta);
                double x1 = xc + r * (c * x + s * y);
                double y1 = yc + r * (s * x - c * y);
                canvas.drawLine((float) x0, (float) y0, (float) x1, (float) y1, paint);
                x0 = x1;
                y0 = y1;
            }
            r *= shrink;
        }
    }

    /**
     * Colours alternate
     * @param paint
     * @param canvas
     */
    void DrawAlternate (Paint paint, Canvas canvas) {
        double xc = m_width * 0.5;
        double yc = m_height * 0.5;
        double r = m_height * 0.48;
        double theta = Math.PI * 2 / m_n1;
        double rotate = Math.PI * m_angles[m_angle_idx] / 180;
        double shrink = (100 - m_shrink_pc [m_shrink_idx]) * 0.01;
        List<Integer> colours = new ArrayList<>(m_n1);

        // Initialise the colours used in the inner loop

        for (int i = 0; i < m_n1; i++) {

            double f = ((i % 2) == 0) ? 1.0 : 0.0;

            int colour = ColourHelpers.Blend(m_first_line, m_last_line, f);
            colours.add(colour);
        }

        // Draw

        for (int j = 0; j < m_n3; ++j) {

            int n = 0;
            double t2 = rotate * j + m_rotate;
            double c = Math.cos(t2);
            double s = Math.sin(t2);
            double x0 = xc + r * c;
            double y0 = yc + r * s;

            for (int i = 0; i < m_n1; i++) {

                paint.setColor(colours.get(i));

                n += m_n2;
                double x = Math.cos(n * theta);
                double y = Math.sin(n * theta);
                double x1 = xc + r * (c * x + s * y);
                double y1 = yc + r * (s * x - c * y);
                canvas.drawLine((float) x0, (float) y0, (float) x1, (float) y1, paint);
                x0 = x1;
                y0 = y1;
            }
            r *= shrink;
        }
    }

    /**
     * Colours Advance then retreat
     * @param paint
     * @param canvas
     */
    void DrawBoth (Paint paint, Canvas canvas) {
        double xc = m_width * 0.5;
        double yc = m_height * 0.5;
        double r = m_height * 0.48;
        double theta = Math.PI * 2 / m_n1;
        double rotate = Math.PI * m_angles[m_angle_idx] / 180;
        double shrink = (100 - m_shrink_pc [m_shrink_idx]) * 0.01;

        for (int j = 0; j < m_n3; ++j) {

            int n = 0;
            double t2 = rotate * j + m_rotate;
            double c = Math.cos(t2);
            double s = Math.sin(t2);
            double x0 = xc + r * c;
            double y0 = yc + r * s;
            double f = (m_n3 > 1) ? 1.0 - (double) j / (m_n3 - 1) : 1.0;

            f = (f < 0.5) ? (2 * f) : (2 - 2 * f);

            paint.setColor(ColourHelpers.Blend(m_first_line, m_last_line, f));

            for (int i = 0; i <= m_n1; i++) {
                n += m_n2;
                double x = Math.cos(n * theta);
                double y = Math.sin(n * theta);
                double x1 = xc + r * (c * x + s * y);
                double y1 = yc + r * (s * x - c * y);
                canvas.drawLine((float) x0, (float) y0, (float) x1, (float) y1, paint);
                x0 = x1;
                y0 = y1;
            }
            r *= shrink;
        }
    }



    public void animate (StarSettings settings){

        ++ m_counter;
        m_rotate += Math.PI / 180.0;
    }



    public void fromBundle(Bundle bundle) {

        m_n1 = bundle.getInt(KEY_N1, m_n1);
        m_n2 = bundle.getInt(KEY_N2, m_n2);
        m_n3 = bundle.getInt(KEY_N3, m_n3);
        m_angle_idx = bundle.getInt(KEY_ROTATE, m_angle_idx);
        m_shrink_idx = bundle.getInt(KEY_SHRINK, m_shrink_idx);
        m_background = bundle.getInt(KEY_BACKGROUND, m_background);
        m_first_line = bundle.getInt(KEY_LINE1, m_first_line);
        m_last_line = bundle.getInt(KEY_LINE2, m_last_line);
    }

    public Bundle toBundle() {

        Bundle b = new Bundle();

        b.putInt(KEY_N1, m_n1);
        b.putInt(KEY_N2, m_n2);
        b.putInt(KEY_N3, m_n3);
        b.putInt(KEY_ROTATE, m_angle_idx);
        b.putInt(KEY_SHRINK, m_shrink_idx);
        b.putInt(KEY_BACKGROUND, m_background);
        b.putInt(KEY_LINE1, m_first_line);
        b.putInt(KEY_LINE2, m_last_line);

        return b;
    }
}
