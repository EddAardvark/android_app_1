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

import com.misc.ColourHelpers;
import com.misc.Drawing;
import com.misc.MyMath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class StarParameters {

    public enum ColouringMode {
        INWARDS, AROUND, BOTH, ALTERNATE
    }

    final public static ColouringMode[] cmodes = {ColouringMode.INWARDS, ColouringMode.AROUND, ColouringMode.BOTH, ColouringMode.ALTERNATE};

    public int m_n1 = 5;                        ///< Number of points in the circle
    public int m_n2 = 2;                        ///< Increment when joining points
    public int m_n3 = 1;                        ///< The number of copies to draw, with rotation
    public int m_angle_idx = 5;                 ///< Rotation when drawing multiple images
    public int m_shrink_idx = 5;                ///< Shrinkage when drawing multiple images (percent)
    public int m_background = Color.WHITE;      ///< Background colour
    public int m_first_line = Color.BLUE;       ///< Foreground ground colour
    public int m_last_line = Color.MAGENTA;     ///< Second foreground ground colour when blending

    public ColouringMode m_colouring_mode = ColouringMode.INWARDS;

    final static int IDX_N1 = 0;
    final static int IDX_N2 = 1;
    final static int IDX_N3 = 2;
    final static int IDX_ROTATE = 3;
    final static int IDX_SHRINK = 4;
    final static int IDX_BACKGROUND = 5;
    final static int IDX_LINE1 = 6;
    final static int IDX_LINE2 = 7;
    final static int IDX_CMODE = 8;
    final static int NUM_ATTRIBUTES = 9;

    double m_rotate = 0;                        ///< Rotates the whole pattern, used in animation
    long m_counter = 0;                         ///< Used in animation

    final static String KEY_N1 = "n1";
    final static String KEY_N2 = "n2";
    final static String KEY_N3 = "n3";
    final static String KEY_ROTATE = "rot";
    final static String KEY_SHRINK = "shrnk";
    final static String KEY_BACKGROUND = "bg";
    final static String KEY_LINE1 = "l1";
    final static String KEY_LINE2 = "l2";
    final static String KEY_COLOURING_MODE = "cm";
    final static String KEY_BM_SIZE = "cm";

    int m_width;
    int m_height;
    Bitmap m_bmp;
    static Random m_random = new Random();

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
     * Used to return evolution parameters from the evolve activity to the drawing activity
     */
    static StarParameters m_EvolutionParameters = null;

    /**
     * Create a copy of this object
     * @return the copy
     */
    @Override
    public StarParameters clone (){
        StarParameters ret = new StarParameters (m_width, m_height);

        ret.m_n1 = m_n1;
        ret.m_n2 = m_n2;
        ret.m_n3 = m_n3;
        ret.m_angle_idx = m_angle_idx;
        ret.m_shrink_idx = m_shrink_idx;
        ret.m_background = m_background;
        ret.m_first_line = m_first_line;
        ret.m_last_line = m_last_line;
        ret.m_colouring_mode = m_colouring_mode;


        return ret;
    }

    /**
     * Get the current evolution parameters. Calling this clears the settings.
     * @return the parameters
     */
    public static StarParameters detachEvolutionParameters (){
        StarParameters ret = m_EvolutionParameters;
        m_EvolutionParameters = null;
        return ret;
    }

    /**
     * Set the evolution parameters
     * @param params The parameters
     */

    public static void setEvolutionParameters (StarParameters params){
        m_EvolutionParameters = params;
    }

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

    /**
     * Randomises one of the patterns attributes, chosen at random
     */
    public void Randomise() {

        RandomiseAttr (m_random.nextInt(NUM_ATTRIBUTES));
    }

    void RandomiseAttr (int attr) {
        switch (attr) {
            case IDX_N1:
                m_n1 = m_random.nextInt(98) + 3;
                fixPoints();
                break;
            case IDX_N2:
                m_n2 = m_random.nextInt(m_n1 - 1) + 1;
                fixPoints();
                break;
            case IDX_N3:
                m_n3 = m_random.nextInt(119 + 1);
                break;
            case IDX_ROTATE:
                m_angle_idx = m_random.nextInt(m_angles.length);
                break;
            case IDX_SHRINK:
                m_shrink_idx = m_random.nextInt(m_shrink_pc.length);
                break;
            case IDX_BACKGROUND:
                m_background = ColourHelpers.random_solid_colour();
                break;
            case IDX_LINE1:
                m_first_line = ColourHelpers.random_solid_colour();
                break;
            case IDX_LINE2:
                m_last_line = ColourHelpers.random_solid_colour();
                break;
            case IDX_CMODE:
                m_colouring_mode = cmodes[m_random.nextInt(cmodes.length)];
                break;
        }
    }

    void fixPoints (){

        int hcf = MyMath.hcf(m_n1, m_n2);

        if (hcf > 1) {
            m_n1 /= hcf;
            m_n2 /= hcf;
        }
        if (m_n1 < 3) {
            ++m_n1;
        }
    }

    /**
     * Randomises the pattern based on the randomiser settings
     * @param settings the randomiser settings
     */
    public void Randomise(RandomSet settings){
        if (settings.m_randomise_num_points) {
            RandomiseAttr (IDX_N1);
        }

        if (settings.m_randomise_point_step){
            RandomiseAttr (IDX_N2);
        }

        if (settings.m_randomise_repeats){
            RandomiseAttr (IDX_N3);
        }

        if (settings.m_randomise_angles){
        RandomiseAttr (IDX_ROTATE);
        }

        if (settings.m_randomise_shrinkage){
                RandomiseAttr (IDX_SHRINK);
        }

        if (settings.m_randomise_backround_colours){
                RandomiseAttr (IDX_BACKGROUND);
        }

        if (settings.m_randomise_forground_colours){
                RandomiseAttr (IDX_LINE1);
                RandomiseAttr (IDX_LINE2);
        }

        if (settings.m_randomise_colour_mode)
        {
            RandomiseAttr(IDX_CMODE);
        }
    }

    public Bitmap bitmap ()
    {
        return m_bmp;
    }

    /**
     * Draws the pattern using the current parameters, this version manages it's own bitmap and image size.
     */
    public void draw(Resources resources, ImageView img) {

        Rect rect = new Rect(0, 0, m_width, m_height);
        Drawing drawing = new Drawing(m_bmp);
        double xc = m_width * 0.5;
        double yc = m_height * 0.5;
        double r = m_height * 0.48;

        drawing.fill_rect (rect, m_background);

        draw(drawing, xc, yc, r);

        int h = m_height/40;

        if (h >= 8) {

            int colour = ColourHelpers.GetContrastColour (m_background);
            int m = m_height/80;

            String star = "(" + m_n1 + "," + m_n2 + ")";

            drawing.draw_text(star, h, m, h + m, colour);

            if (m_n3 > 1) {

                String settings = Integer.toString(m_n3) + " x " + getAngleString() + " x " + getShrinkString();
                drawing.draw_text_right(settings, h, m_width - m, h + m);
            }

            colour = ColourHelpers.MakeTransparent(colour, 0.5);
            drawing.draw_text_right("www.eddaardvark.co.uk", h, m, m_height - m);
        }

        // Render

        img.setImageDrawable(new BitmapDrawable(resources, m_bmp));
    }
    /**
     * Draws the pattern using the current parameters, this version draws into a canvas owned by the caller
     * which may already have some conten.
     */
    public void draw(Drawing drawing, double xc, double yc, double r) {

        switch (m_colouring_mode) {
            case BOTH:
                DrawBoth(drawing, xc, yc, r);
                break;
            case INWARDS:
                DrawInwards(drawing, xc, yc, r);
                break;
            case AROUND:
                DrawAround(drawing, xc, yc, r);
                break;
            case ALTERNATE:
                DrawAlternate(drawing, xc, yc, r);
                break;
        }
    }

    /**
     * Colour progresses from the first colour to the second as we move inwards
     */
    void DrawInwards(Drawing drawing, double xc, double yc, double r) {

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

            drawing.set_colour(ColourHelpers.Blend(m_first_line, m_last_line, f));

            for (int i = 0; i <= m_n1; i++) {
                n += m_n2;
                double x = Math.cos(n * theta);
                double y = Math.sin(n * theta);
                double x1 = xc + r * (c * x + s * y);
                double y1 = yc + r * (s * x - c * y);
                drawing.draw_line(x0, y0, x1, y1);
                x0 = x1;
                y0 = y1;
            }
            r *= shrink;
        }
    }

    /**
     * Colour advances as we move around the pattern
     */
    void DrawAround (Drawing drawing, double xc, double yc, double r) {

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

                drawing.set_colour(colours.get(i));

                n += m_n2;
                double x = Math.cos(n * theta);
                double y = Math.sin(n * theta);
                double x1 = xc + r * (c * x + s * y);
                double y1 = yc + r * (s * x - c * y);
                drawing.draw_line(x0, y0, x1, y1);
                x0 = x1;
                y0 = y1;
            }
            r *= shrink;
        }
    }

    /**
     * Colours alternate
     */
    void DrawAlternate (Drawing drawing, double xc, double yc, double r) {

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

                drawing.set_colour(colours.get(i));

                n += m_n2;
                double x = Math.cos(n * theta);
                double y = Math.sin(n * theta);
                double x1 = xc + r * (c * x + s * y);
                double y1 = yc + r * (s * x - c * y);
                drawing.draw_line(x0, y0, x1, y1);
                x0 = x1;
                y0 = y1;
            }
            r *= shrink;
        }
    }

    /**
     * Colours Advance then retreat
     */
    void DrawBoth (Drawing drawing, double xc, double yc, double r) {

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

            drawing.set_colour(ColourHelpers.Blend(m_first_line, m_last_line, f));

            for (int i = 0; i <= m_n1; i++) {
                n += m_n2;
                double x = Math.cos(n * theta);
                double y = Math.sin(n * theta);
                double x1 = xc + r * (c * x + s * y);
                double y1 = yc + r * (s * x - c * y);
                drawing.draw_line(x0, y0, x1, y1);
                x0 = x1;
                y0 = y1;
            }
            r *= shrink;
        }
    }



    public boolean animate (AnimateSet settings) {

        boolean ret = false;
        ++m_counter;

        if (settings.m_anim_rotate.tryAdvance()) {
            ret = true;
            m_rotate = settings.m_anim_rotate.getIntegerValue() * Math.PI / 180;
        }
        if (settings.m_anim_angle.tryAdvance()) {
            m_angle_idx = settings.m_anim_angle.getIntegerValue();
            ret = true;
        }
        if (settings.m_anim_background.tryAdvance()) {
            m_background = settings.m_anim_background.getColour();
            ret = true;
        }
        if (settings.m_anim_line1.tryAdvance()) {
            m_first_line = settings.m_anim_line1.getColour();
            ret = true;
        }
        if (settings.m_anim_line2.tryAdvance()) {
            m_last_line = settings.m_anim_line2.getColour();
            ret = true;
        }
        if (settings.m_anim_points.tryAdvance()) {
            m_n1 = settings.m_anim_points.getIntegerValue();
            ret = true;
        }
        if (settings.m_anim_repeats.tryAdvance()) {
            m_n3 = settings.m_anim_repeats.getIntegerValue();
            ret = true;
        }
        if (settings.m_anim_shrink.tryAdvance()) {
            m_shrink_idx = settings.m_anim_shrink.getIntegerValue();
            ret = true;
        }
        if (settings.m_anim_step.tryAdvance()) {
            m_n2 = settings.m_anim_step.getIntegerValue();
            ret = true;

        }
        return ret;
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
        Serializable x = bundle.getSerializable(KEY_COLOURING_MODE);

        if (x != null) {
            m_colouring_mode = (ColouringMode) x;
        }
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
        b.putSerializable(KEY_COLOURING_MODE, m_colouring_mode);
        
        return b;
    }

    public String makeFileName() {

        return (m_n3 > 1)
                ? String.format(Locale.UK, "Star_%d_%d_%d_%d_%d_%x_%x_%x.png", m_n1, m_n2, m_n3, m_angle_idx, m_shrink_idx, m_background, m_first_line, m_last_line)
                : String.format(Locale.UK, "Star_%d_%d_%d_%x_%x_%x.png", m_n1, m_n2, m_n3, m_background, m_first_line, m_last_line);
    }
}
