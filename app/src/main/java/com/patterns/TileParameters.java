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

    final static int KEY_BACKGROUND = 1;

    int m_background = Color.WHITE;      ///< Background colour
    int [] m_colour_map = { Color.BLACK, Color.BLUE, Color.BLUE, Color.BLACK };
    int [] m_template = { 1, 2, 3, 4 };
    int m_size = 2;
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
        int draw;
        int colour;

        Element(int idx, int col) {
            draw = idx;
            colour = col;
        }

        Element Copy() {
            return new Element(draw, colour);
        }

        /**
         * Construct a new element by rotating this one 90 degrees
         */
        Element Rotate90() {
            return new Element(Rotate90[draw], colour);
        }

        /**
         * Construct a new element by rotating this one 180 degrees
         */
        Element Rotate180() {
            return new Element(Rotate180[draw], colour);
        }

        /**
         * Construct a new element by rotating this one 270 degrees
         */
        Element Rotate270() {
            return new Element(Rotate270[draw], colour);
        }

        /**
         * Construct a new element by reflecting this one in the X axis
         */
        Element ReflectX() {
            return new Element(ReflectX[draw], colour);
        }

        /**
         * Construct a new element by reflecting this one in the Y axis
         */
        Element ReflectY() {
            return new Element(ReflectY[draw], colour);
        }

        /**
         * Construct a new element by reflecting this one in the X and Y axes (same as rotate 180)
         */
        Element ReflectXY() {
            return new Element(Rotate180[draw], colour);
        }

        /**
         * Construct a new element by inverting this one (swaps foreground and background)
         */
        Element Invert() {
            return new Element(Invert[draw], colour);
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
        m_size = 2;
        m_pattern = new Element[m_size][];

        int[] shape_list = cell_types[type];

        for (int x = 0; x < m_size; ++x) {
            m_pattern[x] = new Element[m_size];

            for (int y = 0; y < m_size; ++y) {
                int c = (colours != null) ? colours[2 * x + y] : Color.BLACK;
                int r = m_random.nextInt(shape_list.length);
                m_pattern[x][y] = new Element(r, c);
            }
        }
        m_code = "";
    }

    //-----------------------------------------------------------------------------------------------------
    void MakeCustom(Element[] elements) {

        m_size = 2;
        m_pattern = new Element[m_size][];

        int idx = 0;

        for (int x = 0; x < m_size; ++x) {
            m_pattern[x] = new Element[m_size];

            for (int y = 0; y < m_size; ++y) {
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

        draw_element_at (drawing, m_template[0], Color.BLACK, 0, 0, 128);
        draw_element_at (drawing, m_template[1], Color.BLACK, 0, 128, 128);
        draw_element_at (drawing, m_template[2], Color.BLACK, 128, 0, 128);
        draw_element_at (drawing, m_template[3], Color.BLACK, 128, 128, 128);

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

    //-----------------------------------------------------------------------------------------------------
    void Draw(Drawing drawing) {

        Rect rect = new Rect(0, 0, m_width, m_height);
        float w = m_width;
        float h = m_height;
        float dx = (w - 2 * MARGIN) / m_size;
        float dy = (h - 2 * MARGIN) / m_size;
        float len = Math.min(dx, dy);
        float x0 = (w - m_size * len) / 2;
        float y0 = (h - m_size * len) / 2;

        drawing.fill_rect (rect, m_background);

        for (int x = 0; x < m_size; ++x) {
            for (int y = 0; y < m_size; ++y) {
                Element e = m_pattern[x][y];
                draw_element_at (drawing, e.draw, e.colour, x0 + x * len, y0 + y * len, len);
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
        m_size = 2;
        m_pattern = new Element[2][];

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
        for (int i = 0; i < m_code.length(); i++) {
            ExpandFromCode(m_code.charAt(i));
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

        Element[][] new_pattern = new Element[2 * m_size][];

        for (int x = 0; x < 2 * m_size; ++x) {
            new_pattern[x] = new Element[2 * m_size];
        }

        for (int x = 0; x < m_size; ++x) {
            for (int y = 0; y < m_size; ++y) {
                new_pattern[x][y] = m_pattern[x][y].Copy();
                new_pattern[m_size + x][y] = m_pattern[x][y].Copy();
                new_pattern[m_size + x][m_size + y] = m_pattern[x][y].Copy();
                new_pattern[x][m_size + y] = m_pattern[x][y].Copy();
            }
        }

        m_pattern = new_pattern;
        m_size *= 2;
    }
//-----------------------------------------------------------------------------------------------------
    void ExpandInvert_X (){
        Element[][] new_pattern = new Element[2 * m_size][];

        for (int x = 0; x < 2 * m_size; ++x) {
            new_pattern[x] = new Element[2 * m_size];
        }

        for (int x = 0; x < m_size; ++x) {
            for (int y = 0; y < m_size; ++y) {
                new_pattern[x][y] = m_pattern[x][y].Copy();
                new_pattern[m_size + x][m_size + y] = m_pattern[x][y].Copy();
                new_pattern[m_size + x][y] = m_pattern[x][y].Invert();
                new_pattern[x][m_size + y] = m_pattern[x][y].Invert();
            }
        }

        m_pattern = new_pattern;
        m_size *= 2;
    }
//-----------------------------------------------------------------------------------------------------
    void ExpandInvert ()
    {
        ExpandSlide();
        InvertCorners();
    }
//-----------------------------------------------------------------------------------------------------
    void ExpandRotate90 ()
    {
        Element[][] new_pattern = new Element[2 * m_size][];

        for (int x = 0; x < 2 * m_size; ++x) {
            new_pattern[x] = new Element[2 * m_size];
        }

        int n = m_size - 1;

        for (int x = 0; x < m_size; ++x) {
            for (int y = 0; y < m_size; ++y) {
                int x1 = x;
                int x2 = m_size + (n - y);
                int x3 = m_size + (n - x);
                int x4 = y;
                int y1 = y;
                int y2 = x;
                int y3 = m_size + (n - y);
                int y4 = m_size + (n - x);

                new_pattern[x1][y1] = m_pattern[x][y].Copy();
                new_pattern[x2][y2] = m_pattern[x][y].Rotate90();
                new_pattern[x3][y3] = m_pattern[x][y].Rotate180();
                new_pattern[x4][y4] = m_pattern[x][y].Rotate270();
            }
        }

        m_pattern = new_pattern;
        m_size *= 2;
    }
//-----------------------------------------------------------------------------------------------------
    void ExpandRotate180 (){

        Element[][] new_pattern = new Element[2 * m_size][];

        for (int x = 0; x < 2 * m_size; ++x) {
            new_pattern[x] = new Element[2 * m_size];
        }

        int n = m_size - 1;

        for (int x = 0; x < m_size; ++x) {
            for (int y = 0; y < m_size; ++y) {
                int x1 = x;
                int x2 = m_size + (n - x);
                int x3 = m_size + x;
                int x4 = n - x;
                int y1 = y;
                int y2 = n - y;
                int y3 = m_size + y;
                int y4 = m_size + (n - y);

                new_pattern[x1][y1] = m_pattern[x][y].Copy();
                new_pattern[x2][y2] = m_pattern[x][y].Rotate180();
                new_pattern[x3][y3] = m_pattern[x][y].Copy();
                new_pattern[x4][y4] = m_pattern[x][y].Rotate180();
            }
        }

        m_pattern = new_pattern;
        m_size *= 2;
    }
//-----------------------------------------------------------------------------------------------------
    void ExpandRotate270 (){
        // The pieces and squares rotate in the opposite direction
        Element[][] new_pattern = new Element[2 * m_size][];

        for (int x = 0; x < 2 * m_size; ++x) {
            new_pattern[x] = new Element[2 * m_size];
        }

        int n = m_size - 1;

        for (int x = 0; x < m_size; ++x) {
            for (int y = 0; y < m_size; ++y) {
                int x1 = x;
                int x2 = m_size + y;
                int x3 = m_size + (n - x);
                int x4 = n - y;
                int y1 = y;
                int y2 = n - x;
                int y3 = m_size + (n - y);
                int y4 = m_size + x;

                new_pattern[x1][y1] = m_pattern[x][y];
                new_pattern[x2][y2] = m_pattern[x][y].Rotate270();
                new_pattern[x3][y3] = m_pattern[x][y].Rotate180();
                new_pattern[x4][y4] = m_pattern[x][y].Rotate90();
            }
        }

        m_pattern = new_pattern;
        m_size *= 2;
    }
//-----------------------------------------------------------------------------------------------------
    void ExpandReflect ()
    {
        // Reflect in the principle axes
        Element[][] new_pattern = new Element[2 * m_size][];

        for (int x = 0; x < 2 * m_size; ++x) {
            new_pattern[x] = new Element[2 * m_size];
        }

        int n = m_size - 1;

        for (int x = 0; x < m_size; ++x) {
            for (int y = 0; y < m_size; ++y) {
                int x1 = x;
                int x2 = m_size + (n - x);
                int x3 = m_size + (n - x);
                int x4 = x;
                int y1 = y;
                int y2 = y;
                int y3 = m_size + (n - y);
                int y4 = m_size + (n - y);

                new_pattern[x1][y1] = m_pattern[x][y].Copy();
                new_pattern[x2][y2] = m_pattern[x][y].ReflectX();
                new_pattern[x3][y3] = m_pattern[x][y].ReflectXY();
                new_pattern[x4][y4] = m_pattern[x][y].ReflectY();
            }
        }

        m_pattern = new_pattern;
        m_size *= 2;
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
    void InvertCorners ()
    {
        int n = m_size / 2;

        for (int x = 0; x < n; ++x) {
            for (int y = 0; y < n; ++y) {
                m_pattern[x][y] = m_pattern[x][y].Invert();
                m_pattern[x + n][y + n] = m_pattern[x + n][y + n].Invert();
            }
        }
    }
//---------------------------------------------------------------------------------------------------------
    public void next_shape (int idx)
    {
        m_template [idx] = (m_template [idx] + 1) % num_shapes;
        Initialise();
        ApplyCode ();
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
    }

    public Bundle toBundle () {

        Bundle b = new Bundle();

        return b;
    }

    public String makeFileName () {

        return "tile_pattern";
    }
}
