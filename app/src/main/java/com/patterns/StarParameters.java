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

import com.misc.ColourHelpers;
import com.misc.MyMath;

import java.util.Random;

public class StarParameters {

    public int m_n1 = 5;                        ///< Number of points in the circle
    public int m_n2 = 2;                        ///< Increment when joining points
    public int m_n3 = 1;                        ///< The number of copies to draw, with rotation
    public int m_rotate_degrees = 5;            ///< Rotation when drawing multiple images
    public int m_shrink_pc = 5;                 ///< Shrinkage when drawing multiple images (percent)
    public int m_background = Color.WHITE;      ///< Background colour
    public int m_first_line = Color.BLUE;       ///< Foreground ground colour
    public int m_last_line = Color.MAGENTA;     ///< Second foreground ground colour when blending

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

    public StarParameters (int w, int h)
    {
        setSize (w, h);
    }

    public void setSize (int w, int h)
    {
        m_width = w;
        m_height = h;

        m_bmp = Bitmap.createBitmap(m_width, m_height, Bitmap.Config.RGB_565);
    }

    public void Randomise ()
    {
        Random rand = new Random();
        int n2_range = m_n1/2 - 1;

        m_n1 = rand.nextInt(98) + 3;
        m_n2 = (n2_range > 0) ? rand.nextInt(n2_range) + 1 : 1;

        int hcf = MyMath.hcf (m_n1, m_n2);

        if (hcf > 1)
        {
            m_n1 /= hcf;
            m_n2 /= hcf;
        }
    }

    public Bitmap bitmap ()
    {
        return m_bmp;
    }
    /**
     * Draws the pattern using the current parameters
     * @param img Where to draw the pattern
     */
    public void Draw(Resources resources, ImageView img)
    {
        Rect rect = new Rect(0, 0, m_width, m_height);
        Canvas canvas = new Canvas(m_bmp);
        Paint paint = new Paint();
        paint.setColor(m_background);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(rect, paint);

        double xc = m_width * 0.5;
        double yc = m_height * 0.5;
        double r = m_height * 0.48;
        double theta = Math.PI * 2 / m_n1;
        double shrink = (100 - m_shrink_pc) * 0.01;

        for (int j = 0 ; j < m_n3 ; ++j) {

            int n = 0;
            double t2 = Math.PI * m_rotate_degrees * j / 180;
            double c = Math.cos (t2);
            double s = Math.sin (t2);
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

// Attach the canvas to the ImageView
        img.setImageDrawable(new BitmapDrawable(resources, m_bmp));
    }

    public void fromBundle(Bundle bundle) {

        m_n1 = bundle.getInt(KEY_N1, m_n1);
        m_n2 = bundle.getInt(KEY_N2, m_n2);
        m_n3 = bundle.getInt(KEY_N3, m_n3);
        m_rotate_degrees = bundle.getInt(KEY_ROTATE, m_rotate_degrees);
        m_shrink_pc = bundle.getInt(KEY_SHRINK, m_shrink_pc);
        m_background = bundle.getInt(KEY_BACKGROUND, m_background);
        m_first_line = bundle.getInt(KEY_LINE1, m_first_line);
        m_last_line = bundle.getInt(KEY_LINE2, m_last_line);
    }

    public void toBundle(Bundle bundle) {

        bundle.putInt(KEY_N1, m_n1);
        bundle.putInt(KEY_N2, m_n2);
        bundle.putInt(KEY_N3, m_n3);
        bundle.putInt(KEY_ROTATE, m_rotate_degrees);
        bundle.putInt(KEY_SHRINK, m_shrink_pc);
        bundle.putInt(KEY_BACKGROUND, m_background);
        bundle.putInt(KEY_LINE1, m_first_line);
        bundle.putInt(KEY_LINE2, m_last_line);
    }
}
