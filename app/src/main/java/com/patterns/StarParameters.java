package com.patterns;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class StarParameters {

    public int m_n1 = 5;                       ///< Number of points in the circle
    public int m_n2 = 2;                       ///< Increment when joining points
    public double m_rotate_degrees = 0;        ///< Rotation when drawing multiple images
    public double m_shrink = 0.95;             ///< Shrinkage when drawing multiple images
    public int m_background = Color.WHITE;     ///< Background colour
    public int m_first_line = Color.BLACK;     ///< Foreground ground colour
    public int m_last_line = Color.BLACK;      ///< Second foreground ground colour when blending

    int m_width;
    int m_height;
    Bitmap m_bmp;

    public StarParameters (int w, int h)
    {
        m_width = w;
        m_height = h;

        m_bmp = Bitmap.createBitmap(m_width, m_height, Bitmap.Config.RGB_565);
    }

    public void Randomise ()
    {
        Random rand = new Random();

        m_n1 = rand.nextInt(98) + 3;
        m_n2 = rand.nextInt(49) + 1;
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
        if (m_n2 == 0 || m_n1 < 1 || m_shrink <= 0)
            return;

        Rect rect = new Rect(0, 0, m_width, m_height);
        Canvas canvas = new Canvas(m_bmp);
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(rect, paint);
        paint.setColor(Color.BLACK);

        double xc = m_width * 0.5;
        double yc = m_height * 0.5;
        double r = m_height * 0.48;
        double theta = Math.PI * 2 / m_n1;
        double x0 = xc + r;
        double y0 = yc + 0;
        int n = 0;

        for (int i = 0 ; i <= m_n1 ; i++)
        {
            n += m_n2;
            double x1 = xc + r * Math.cos (n * theta);
            double y1 = yc + r * Math.sin (n * theta);
            canvas.drawLine((float) x0, (float) y0, (float) x1, (float) y1, paint);
            x0 = x1;
            y0 = y1;
        }

// Attach the canvas to the ImageView
        img.setImageDrawable(new BitmapDrawable(resources, m_bmp));
    }
}
