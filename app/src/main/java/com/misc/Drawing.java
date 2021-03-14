package com.misc;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import java.util.List;

public class Drawing {

    public class Pt
    {
        float x;
        float y;
    }

    Paint m_paint;
    Canvas m_canvas;

    /**
     * Construct from a bitmap
     * @param bmp The bitmap
     */
    public Drawing (Bitmap bmp) {
        m_paint = new Paint();
        m_canvas = new Canvas(bmp);
    }

    /**
     * Fill a rectangle
     * @param rect The rectangle
     * @param colour The colour
     */
    public void fill_rect (Rect rect, int colour) {
        m_paint.setColor(colour);
        m_paint.setStyle(Paint.Style.FILL);
        m_canvas.drawRect(rect, m_paint);
    }
    public void set_colour (int colour) {
        m_paint.setColor(colour);
    }
    public void setTextSize (int h) {
        m_paint.setTextSize(h);
    }
    public void setTextAlign (Paint.Align a) {
        m_paint.setTextAlign(a);
    }
    public void setStyle (Paint.Style s) {
        m_paint.setStyle(s);
    }
    public void draw_text (String text, int h, int x, int y, int colour) {
        m_paint.setColor(colour);
        m_paint.setTextSize(h);
        m_paint.setTextAlign(Paint.Align.LEFT);
        m_canvas.drawText(text, x, y, m_paint);
    }
    public void draw_text (String text, int h, int x, int y) {
        m_paint.setTextSize(h);
        m_paint.setTextAlign(Paint.Align.LEFT);
        m_canvas.drawText(text, x, y, m_paint);
    }
    public void draw_text_right (String text, int h, int x, int y, int colour) {
        m_paint.setColor(colour);
        m_paint.setTextSize(h);
        m_paint.setTextAlign(Paint.Align.RIGHT);
        m_canvas.drawText(text, x, y, m_paint);
    }
    public void draw_text_right (String text, int h, int x, int y) {
        m_paint.setTextSize(h);
        m_paint.setTextAlign(Paint.Align.RIGHT);
        m_canvas.drawText(text, x, y, m_paint);
    }
    public void draw_line (int x0, int y0, int x1, int y1) {
        m_canvas.drawLine((float) x0, (float) y0, (float) x1, (float) y1, m_paint);
    }
    public void draw_line (double x0, double y0, double x1, double y1) {
        m_canvas.drawLine((float) x0, (float) y0, (float) x1, (float) y1, m_paint);
    }
    public void draw_line (float x0, float y0, float x1, float y1) {
        m_canvas.drawLine(x0, y0, x1, y1, m_paint);
    }
    /**
     * Draw a polygon in a particular colour
     * @param colour
     * @param points
     */
    public void draw_polygon (int colour, float[][] points) {

        m_paint.setColor(colour);
        m_paint.setStyle(Paint.Style.FILL);
        draw_polygon(points);
    }
    /**
     * Draw a polygon (uses current settings)
     * @param points list of points
     */
    public void draw_polygon (float[][] points) {
        Path path = new Path();
        int n = points.length - 1;
        float[] ptn = points [n];

        path.moveTo(ptn[0], ptn[1]);

        //points.forEach((pt)-> { path.lineTo(pt.x, pt.y); } );

        for (float[] pt : points) {
            path.lineTo(pt[0], pt[1]);
        }

        path.setFillType(Path.FillType.EVEN_ODD);
        m_canvas.drawPath(path, m_paint);
    }
}
