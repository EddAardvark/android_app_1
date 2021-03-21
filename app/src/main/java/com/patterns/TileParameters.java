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

import androidx.core.util.Pair;

import com.misc.ColourHelpers;
import com.misc.Drawing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

//==========================================================================================================
// A pattern created by repeating shapes with various symmetry operations on a square grid.
// based on the javascript triangle_patterns
// (c) John Whitehouse 2018 - 2021
// www.eddaardvark.co.uk
//
// The pattern starts off with a 2x2 grid (called the template) and generates a series of larger patterns
// by repeating the original pattern suitable transformed
//==========================================================================================================

public class TileParameters {

    final static int SIZE0 = 2;

    final static String KEY_T1 = "t1";
    final static String KEY_T2 = "t2";
    final static String KEY_T3 = "t3";
    final static String KEY_T4 = "t4";
    final static String KEY_C1 = "c1";
    final static String KEY_C2 = "c2";
    final static String KEY_C3 = "c3";
    final static String KEY_C4 = "c4";
    final static String KEY_BG = "bg";
    final static String KEY_CODE = "cd";

    int m_background = Color.WHITE;      ///< Background colour
    int [] m_colour_map = { Color.BLACK, Color.BLACK, Color.BLACK, Color.RED };
    int [] m_template = { 2, 7, 1, 6 };
    Element[][] m_pattern;

    String m_code = "";

    int m_width;
    int m_height;
    Bitmap m_bmp;
    Bitmap m_bmp_template = Bitmap.createBitmap(256, 256, Bitmap.Config.RGB_565);
    Bitmap m_bmp_colour = Bitmap.createBitmap(256, 256, Bitmap.Config.RGB_565);

    Rect m_trects [] =
            {
                    new Rect(0, 0, 128, 128),
                    new Rect(0, 128, 128, 256),
                    new Rect(128, 0, 256, 128),
                    new Rect(128, 128, 256, 256)
            };
    static Random m_random = new Random();

    /**
     * Ways of combining the shapes into a template
     */
    enum PatternModes {

        TRIANGLE,       ///< Random set of triangles
        RECTANGLE,      ///< Random set of rectangles
        SPOTS,          ///< Random set of spots
        T_AND_R,        ///< Random set of triangles + rectangles
        T_AND_S,        ///< Random set of triangles + spots
        R_AND_S,        ///< Random set of rectangles + spots
        ALL,            ///< Random set of everything
        CUSTOM,         ///< User defined
    }

// transformations

    static final int[] Rotate90 = new int[]{0, 4, 1, 2, 3, 8, 5, 6, 7, 9, 10, 11};
    static final int[] Rotate180 = new int[]{0, 3, 4, 1, 2, 7, 8, 5, 6, 9, 10, 11};
    static final int[] Rotate270 = new int[]{0, 2, 3, 4, 1, 6, 7, 8, 5, 9, 10, 11};
    static final int[] ReflectX = new int[]{0, 2, 1, 4, 3, 7, 6, 5, 8, 9, 10, 11};
    static final int[] ReflectY = new int[]{0, 4, 3, 2, 1, 5, 8, 7, 6, 9, 10, 11};
    static final int[] Invert = new int[]{9, 3, 4, 1, 2, 7, 8, 5, 6, 0, 11, 10};

    //=============================================================================================
    /**
     * One of the elements in the pattern or template
     */
    class Element {
        int idx;
        int colour;

        Element(int ix, int col) {
            idx = ix;
            colour = col;
        }

        Element Copy() {
            return new Element(idx, colour);
        }

        /**
         * Construct a new element by rotating this one 90 degrees
         */
        Element Rotate90() {
            return new Element(Rotate90[idx], colour);
        }

        /**
         * Construct a new element by rotating this one 180 degrees
         */
        Element Rotate180() {
            return new Element(Rotate180[idx], colour);
        }

        /**
         * Construct a new element by rotating this one 270 degrees
         */
        Element Rotate270() {
            return new Element(Rotate270[idx], colour);
        }

        /**
         * Construct a new element by reflecting this one in the X axis
         */
        Element ReflectX() {
            return new Element(ReflectX[idx], colour);
        }

        /**
         * Construct a new element by reflecting this one in the Y axis
         */
        Element ReflectY() {
            return new Element(ReflectY[idx], colour);
        }

        /**
         * Construct a new element by reflecting this one in the X and Y axes (same as rotate 180)
         */
        Element ReflectXY() {
            return new Element(Rotate180[idx], colour);
        }

        /**
         * Construct a new element by inverting this one (swaps foreground and background)
         */
        Element Invert() {
            return new Element(Invert[idx], colour);
        }
    }
    //=============================================================================================

    final float[][][] shapes =
            {
                    {},
                    {{0, 0}, {0, 1}, {1, 1}},
                    {{0, 1}, {1, 1}, {1, 0}},
                    {{0, 0}, {1, 1}, {1, 0}},
                    {{0, 0}, {0, 1}, {1, 0}},
                    {{0, 0}, {0, 1}, {0.5f, 1}, {0.5f, 0}},
                    {{0, 0.5f}, {0, 1}, {1, 1}, {1, 0.5f}},
                    {{0.5f, 0}, {0.5f, 1}, {1, 1}, {1, 0}},
                    {{0, 0}, {0, 0.5f}, {1, 0.5f}, {1, 0}},
                    {{0, 0}, {0, 1}, {1, 1}, {1, 0}},
                    {{0.25f, 0.25f}, {0.25f, 0.75f}, {0.75f, 0.75f}, {0.75f, 0.25f}},
                    {{0, 0}, {0, 1}, {1, 1}, {1, 0}, {0, 0}, {0.25f, 0.25f}, {0.75f, 0.25f}, {0.75f, 0.75f}, {0.25f, 0.75f}, {0.25f, 0.25f}}
            };

    final int[][] cell_types =
            {
                    {1, 2, 3, 4},                           // Triangles
                    {5, 6, 7, 8},                           // Rectangles
                    {0, 9, 10, 11},                         // Spots
                    {1, 2, 3, 4, 5, 6, 7, 8},               // Triangles + Rectangles
                    {1, 2, 3, 4, 0, 9, 10, 11},             // Triangles + Spots
                    {5, 6, 7, 8, 0, 9, 10, 11},             // Rectangles + Spots
                    {1, 2, 3, 4, 5, 6, 7, 8, 0, 9, 10, 11}, // Everything
            };

// Layout

    final int MARGIN = 4;
    final int num_shapes = shapes.length;

    char[] code_letters = {'A', 'B', 'C', 'D', 'E', 'F'};
    Map<Character, Runnable> commands = new HashMap<>();

    /**
     * Construct
     * @param w bitmap width
     * @param h bitmap height
     */
    public TileParameters (int w, int h)
    {
        setSize (w, h);
        Initialise();
    }
    /**
     * Make a random pattern
     * @param type
     * @param colours
     */
    void MakeRandom(int type, int[] colours) {

        int size = m_pattern.length;
        m_pattern = new Element[size][];

        int[] shape_list = cell_types[type];

        for (int x = 0; x < size; ++x) {
            m_pattern[x] = new Element[size];

            for (int y = 0; y < size; ++y) {
                int c = (colours != null) ? colours[2 * x + y] : Color.BLACK;
                int r = m_random.nextInt(shape_list.length);
                m_pattern[x][y] = new Element(r, c);
            }
        }
        m_code = "";
    }

    //-----------------------------------------------------------------------------------------------------
    void MakeCustom(Element[] elements) {

        m_pattern = new Element[SIZE0][];

        int idx = 0;

        for (int x = 0; x < SIZE0; ++x) {
            m_pattern[x] = new Element[SIZE0];

            for (int y = 0; y < SIZE0; ++y) {
                m_pattern[x][y] = elements[idx].Copy();
                ++idx;
            }
        }
        m_code = "";
    }
    /**
     * Draws the pattern using the current parameters, this version manages it's own bitmap and image size.
     * @param resources
     * @param img
     */
    public void draw (Resources resources, ImageView img){

        Rect rect = new Rect(0, 0, m_width, m_height);
        Drawing drawing = new Drawing(m_bmp);

        Draw(drawing);

        // Render

        img.setImageDrawable(new BitmapDrawable(resources, m_bmp));
    }
    /**
     * Draws the pattern template, the four shapes that initialise the pattern
     */
    public void draw_template(Resources resources, ImageView img) {

        Rect rect = new Rect(0, 0, 256, 256);
        Drawing drawing = new Drawing(m_bmp_template);

        drawing.fill_rect (rect, Color.WHITE);

        draw_element_at (drawing, m_template[0], m_colour_map[0], 0, 0, 128);
        draw_element_at (drawing, m_template[1], m_colour_map[1], 0, 128, 128);
        draw_element_at (drawing, m_template[2], m_colour_map[2], 128, 0, 128);
        draw_element_at (drawing, m_template[3], m_colour_map[3], 128, 128, 128);

        img.setImageDrawable(new BitmapDrawable(resources, m_bmp_template));
    }
    /**
     * Draws the colour template
     */
    public void draw_template_colours(Resources resources, ImageView img) {

        Drawing drawing = new Drawing(m_bmp_colour);

        for (int i = 0 ; i < 4 ; ++i) {
            drawing.fill_rect(m_trects [i], m_colour_map[i]);
        }

        img.setImageDrawable(new BitmapDrawable(resources, m_bmp_colour));
    }
    /**
     * Draws the colour template
     */
    public void draw_transform_map(Resources resources, ImageView img) {

        Element [][] temp = m_pattern;
        String code = m_code;

        Bitmap bmp = Bitmap.createBitmap(640, 256, Bitmap.Config.RGB_565);
        Drawing drawing = new Drawing(bmp);

        Rect rect = new Rect(0, 0, 640, 256);
        drawing.fill_rect(rect, Color.GRAY);

        for (int i = 0 ; i < 10 ; ++i) {
            char op = "FGHIJABCDE".charAt(i);

            int ix = 6 + 128 * (i % 5);
            int iy = 6 + 128 * (int) (i / 5);

            Initialise ();
            m_code = "";
            ExpandFromCode(op);

            rect = new Rect(ix, iy, ix + 116, iy + 116);
            draw_at (drawing, rect, m_pattern);
        }

        img.setImageDrawable(new BitmapDrawable(resources, bmp));
        m_pattern = temp;
        m_code = code;
    }

    //-----------------------------------------------------------------------------------------------------
    void Draw(Drawing drawing) {

        Rect rect = new Rect(0, 0, m_width, m_height);
        draw_at (drawing, rect, m_pattern);
    }
    //-----------------------------------------------------------------------------------------------------
    void draw_at (Drawing drawing, Rect rect, Element [][] pattern) {

        int size = pattern.length;
        float w =rect.width();
        float h = rect.height();
        float dx = w / size;
        float dy = h / size;
        float len = Math.min(dx, dy);
        float x0 = rect.left;
        float y0 = rect.top;

        drawing.fill_rect (rect, m_background);

        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                Element e = pattern[x][y];
                draw_element_at (drawing, e.idx, e.colour, x0 + x * len, y0 + y * len, len);
            }
        }
    }
    //-----------------------------------------------------------------------------------------------------
    void draw_element_at(Drawing drawing, int idx, int colour, float x, float y, float size) {

        float[][] piece = shapes[idx];

        if (piece.length > 0) {
            float[][] points = new float[piece.length][];

            for (int i = 0; i < piece.length; ++i) {
                points[i] = new float[]{(float) (x + piece[i][0] * size), (float) (y + piece[i][1] * size)};
            }

            drawing.draw_polygon(colour, points);
        }
    }
    /**
     * Create the initial pattern
     */
    void Initialise ()
    {
        m_pattern = new Element[SIZE0][];

        int n = 0;

        m_pattern [0] = new Element[2];
        m_pattern [1] = new Element[2];

        m_pattern [0][0] = new Element (m_template [0], m_colour_map [0]);
        m_pattern [0][1] = new Element (m_template [1], m_colour_map [1]);
        m_pattern [1][0] = new Element (m_template [2], m_colour_map [2]);
        m_pattern [1][1] = new Element (m_template [3], m_colour_map [3]);
    }
    //-----------------------------------------------------------------------------------------------------
    void ApplyCode () {
        String s = m_code;
        m_code = "";

        for (int i = 0; i < s.length(); i++) {
            ExpandFromCode(s.charAt(i));
        }
    }
    //-----------------------------------------------------------------------------------------------------
    void Expand() {
        // Applies a random transformation.

        int r = m_random.nextInt(code_letters.length);
        char code = code_letters[r];

        ExpandFromCode(code);
    }
    //-----------------------------------------------------------------------------------------------------
    void ExpandFromCode(char code) {
        m_code += code;

        if (code == 'A') {
            ExpandSlide();
        } else if (code == 'B') {
            ExpandInvert();
        } else if (code == 'C') {
            ExpandRotate90();
        } else if (code == 'D') {
            ExpandRotate180();
        } else if (code == 'E') {
            ExpandRotate270();
        } else if (code == 'F') {
            ExpandReflect();
        } else if (code == 'G') {
            ExpandRotate90Invert();
        } else if (code == 'H') {
            ExpandRotate180Invert();
        } else if (code == 'I') {
            ExpandRotate270Invert();
        } else if (code == 'J') {
            ExpandReflectInvert();
        }
    }
    //-----------------------------------------------------------------------------------------------------
    void ExpandSlide() {

        int size = m_pattern.length;
        Element[][] new_pattern = new Element[2 * size][];

        for (int x = 0; x < 2 * size; ++x) {
            new_pattern[x] = new Element[2 * size];
        }

        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                new_pattern[x][y] = m_pattern[x][y].Copy();
                new_pattern[size + x][y] = m_pattern[x][y].Copy();
                new_pattern[size + x][size + y] = m_pattern[x][y].Copy();
                new_pattern[x][size + y] = m_pattern[x][y].Copy();
            }
        }

        m_pattern = new_pattern;
    }
//-----------------------------------------------------------------------------------------------------
    void ExpandInvert_X (){

        int size = m_pattern.length;
        Element[][] new_pattern = new Element[2 * size][];

        for (int x = 0; x < 2 * size; ++x) {
            new_pattern[x] = new Element[2 * size];
        }

        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                new_pattern[x][y] = m_pattern[x][y].Copy();
                new_pattern[size + x][size + y] = m_pattern[x][y].Copy();
                new_pattern[size + x][y] = m_pattern[x][y].Invert();
                new_pattern[x][size + y] = m_pattern[x][y].Invert();
            }
        }

        m_pattern = new_pattern;
    }
//-----------------------------------------------------------------------------------------------------
    void ExpandInvert ()
    {
        ExpandSlide();
        InvertCorners();
    }
//-----------------------------------------------------------------------------------------------------
    void ExpandRotate90 () {

        int size = m_pattern.length;
        Element[][] new_pattern = new Element[2 * size][];

        for (int x = 0; x < 2 * size; ++x) {
            new_pattern[x] = new Element[2 * size];
        }

        int n = size - 1;

        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                int x1 = x;
                int x2 = size + (n - y);
                int x3 = size + (n - x);
                int x4 = y;
                int y1 = y;
                int y2 = x;
                int y3 = size + (n - y);
                int y4 = size + (n - x);

                new_pattern[x1][y1] = m_pattern[x][y].Copy();
                new_pattern[x2][y2] = m_pattern[x][y].Rotate90();
                new_pattern[x3][y3] = m_pattern[x][y].Rotate180();
                new_pattern[x4][y4] = m_pattern[x][y].Rotate270();
            }
        }

        m_pattern = new_pattern;
    }
//-----------------------------------------------------------------------------------------------------
    void ExpandRotate180 (){

        int size = m_pattern.length;
        Element[][] new_pattern = new Element[2 * size][];

        for (int x = 0; x < 2 * size; ++x) {
            new_pattern[x] = new Element[2 * size];
        }

        int n = size - 1;

        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                int x1 = x;
                int x2 = size + (n - x);
                int x3 = size + x;
                int x4 = n - x;
                int y1 = y;
                int y2 = n - y;
                int y3 = size + y;
                int y4 = size + (n - y);

                new_pattern[x1][y1] = m_pattern[x][y].Copy();
                new_pattern[x2][y2] = m_pattern[x][y].Rotate180();
                new_pattern[x3][y3] = m_pattern[x][y].Copy();
                new_pattern[x4][y4] = m_pattern[x][y].Rotate180();
            }
        }

        m_pattern = new_pattern;
    }
//-----------------------------------------------------------------------------------------------------
// The pieces and squares rotate in the opposite direction
    void ExpandRotate270 (){

        int size = m_pattern.length;
        Element[][] new_pattern = new Element[2 * size][];

        for (int x = 0; x < 2 * size; ++x) {
            new_pattern[x] = new Element[2 * size];
        }

        int n = size - 1;

        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                int x1 = x;
                int x2 = size + y;
                int x3 = size + (n - x);
                int x4 = n - y;
                int y1 = y;
                int y2 = n - x;
                int y3 = size + (n - y);
                int y4 = size + x;

                new_pattern[x1][y1] = m_pattern[x][y];
                new_pattern[x2][y2] = m_pattern[x][y].Rotate270();
                new_pattern[x3][y3] = m_pattern[x][y].Rotate180();
                new_pattern[x4][y4] = m_pattern[x][y].Rotate90();
            }
        }

        m_pattern = new_pattern;
    }
//-----------------------------------------------------------------------------------------------------
// Reflect in the principle axes

    void ExpandReflect () {

        int size = m_pattern.length;
        Element[][] new_pattern = new Element[2 * size][];

        for (int x = 0; x < 2 * size; ++x) {
            new_pattern[x] = new Element[2 * size];
        }

        int n = size - 1;

        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                int x1 = x;
                int x2 = size + (n - x);
                int x3 = size + (n - x);
                int x4 = x;
                int y1 = y;
                int y2 = y;
                int y3 = size + (n - y);
                int y4 = size + (n - y);

                new_pattern[x1][y1] = m_pattern[x][y].Copy();
                new_pattern[x2][y2] = m_pattern[x][y].ReflectX();
                new_pattern[x3][y3] = m_pattern[x][y].ReflectXY();
                new_pattern[x4][y4] = m_pattern[x][y].ReflectY();
            }
        }

        m_pattern = new_pattern;
    }
//-----------------------------------------------------------------------------------------------------
    void ExpandRotate90Invert ()
    {
        this.ExpandRotate90();
        this.InvertCorners();
    }
//-----------------------------------------------------------------------------------------------------
    void ExpandRotate180Invert ()
    {
        this.ExpandRotate180();
        this.InvertCorners();
    }
//-----------------------------------------------------------------------------------------------------
    void ExpandRotate270Invert ()
    {
        this.ExpandRotate270();
        this.InvertCorners();
    }
//-----------------------------------------------------------------------------------------------------
    void ExpandReflectInvert ()
    {
        this.ExpandReflect();
        this.InvertCorners();
    }
//-----------------------------------------------------------------------------------------------------
    void InvertCorners () {

        int n = m_pattern.length / 2;

        for (int x = 0; x < n; ++x) {
            for (int y = 0; y < n; ++y) {
                m_pattern[x][y] = m_pattern[x][y].Invert();
                m_pattern[x + n][y + n] = m_pattern[x + n][y + n].Invert();
            }
        }
    }
//---------------------------------------------------------------------------------------------------------
    public void next_shape (int idx) {
        m_template[idx] = (m_template[idx] + 1) % num_shapes;
        Initialise();
        ApplyCode();
    }
    //---------------------------------------------------------------------------------------------------------
    public void add_op (char op)
    {
        if (m_code.length() < 7) {
            ExpandFromCode(op);
        }
    }    //---------------------------------------------------------------------------------------------------------
    public void back_one ()
    {
        if (m_code.length() > 0) {
            m_code = m_code.substring(0, m_code.length() - 1);
            Initialise();
            ApplyCode();
        }
    }
    //---------------------------------------------------------------------------------------------------------
    public int get_colour (int idx)
    {
        return m_colour_map [idx];
    }
    //---------------------------------------------------------------------------------------------------------
    public void set_tile_colour (int idx, int colour)
    {
        m_colour_map [idx] = colour;
        Initialise();
        ApplyCode ();
    }
    /**
     * Sets the size of the bitmap drawn. This doesn't affect the size no the screen but does change the detail when the image is shared.
     * @param w Width in pixels
     * @param h Height in pixels
     */
    public void setSize ( int w, int h)
    {
        m_width = w;
        m_height = h;

        m_bmp = Bitmap.createBitmap(m_width, m_height, Bitmap.Config.RGB_565);
    }

    public void fromBundle (Bundle bundle){

        m_template[0] = bundle.getInt(KEY_T1, 2);
        m_template[1] = bundle.getInt(KEY_T2, 7);
        m_template[2] = bundle.getInt(KEY_T3, 1);
        m_template[3] = bundle.getInt(KEY_T4, 6);

        m_colour_map[0] = bundle.getInt(KEY_C1, Color.BLACK);
        m_colour_map[1] = bundle.getInt(KEY_C2, Color.BLACK);
        m_colour_map[2] = bundle.getInt(KEY_C3, Color.BLACK);
        m_colour_map[3] = bundle.getInt(KEY_C4, Color.RED);

        m_background = bundle.getInt(KEY_BG, Color.WHITE);
        m_code = bundle.getString(KEY_CODE, "");

        Initialise();
        ApplyCode ();
    }

    public Bitmap bitmap ()
    {
        return m_bmp;
    }

    public Bundle toBundle () {

        Bundle b = new Bundle();

        b.putInt(KEY_T1, m_template[0]);
        b.putInt(KEY_T2, m_template[1]);
        b.putInt(KEY_T3, m_template[2]);
        b.putInt(KEY_T4, m_template[3]);

        b.putInt(KEY_C1, m_colour_map[0]);
        b.putInt(KEY_C2, m_colour_map[1]);
        b.putInt(KEY_C3, m_colour_map[2]);
        b.putInt(KEY_C4, m_colour_map[3]);

        b.putInt(KEY_BG, m_background);
        b.putString(KEY_CODE, m_code);

        return b;
    }

    public String makeFileName () {

        return "tile_pattern_" + m_code;
    }
}
