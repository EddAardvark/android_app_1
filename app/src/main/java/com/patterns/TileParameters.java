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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.activities.R;
import com.dialogs.FragmentSet;
import com.dialogs.PatternParameters;
import com.misc.ColourHelpers;
import com.misc.Drawing;
import com.misc.Misc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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

public class TileParameters extends PatternParameters {

    final static int SIZE0 = 2;

    final static String KEY_T1 = "t1";
    final static String KEY_T2 = "t2";
    final static String KEY_T3 = "t3";
    final static String KEY_T4 = "t4";
    final static String KEY_C1 = "c1";
    final static String KEY_C2 = "c2";
    final static String KEY_C3 = "c3";
    final static String KEY_C4 = "c4";
    final static String KEY_CODE = "cd";

    final static String KEY_PATTERN = "ps";
    final static String KEY_RANDOM = "rs";

    int [] m_colour_map;
    int [] m_template;
    Element[][] m_pattern;

    String m_code = "";

    TilePatternSet m_pattern_set = new TilePatternSet();
    TileRandomSet m_random_set = new TileRandomSet();
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

    List<Integer> m_random_shapes;

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

    final List<Integer> triangles = Arrays.asList ( 1, 2, 3, 4 ) ;
    final List<Integer> bars      = Arrays.asList ( 5, 6, 7, 8 );
    final List<Integer> spots     = Arrays.asList ( 10,11 );
    final List<Integer> blocks    = Arrays.asList ( 0, 9 );

// Layout

    final int MARGIN = 4;
    final int num_shapes = shapes.length;

    char[] code_letters = {'A', 'B', 'C', 'D', 'E', 'F'};
    Map<Character, Runnable> commands = new HashMap<>();

    /**
     * Construct
     */
    public TileParameters ()
    {
        reset();
        setSize ();
        SetRandomShapes ();
        Initialise();
    }

    /**
     * The random shapes is the set of shapes used when randomising.
     */
    void SetRandomShapes ()
    {
        m_random_shapes = new ArrayList<>();

        if (m_random_set.m_randomise_bars){
            m_random_shapes.addAll(bars);
        }
        if (m_random_set.m_randomise_blocks){
            m_random_shapes.addAll(blocks);
        }
        if (m_random_set.m_randomise_spots){
            m_random_shapes.addAll(spots);
        }
        if (m_random_set.m_randomise_triangles){
            m_random_shapes.addAll(triangles);
        }
    }
    /**
     * Draws the pattern using the current parameters, this version manages it's own bitmap and image size.
     * @param resources
     * @param img
     */
    public void draw (Resources resources, ImageView img){

        Rect rect = new Rect(0, 0, m_pattern_set.m_bm_size, m_pattern_set.m_bm_size);
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

        drawing.fill_rect (rect, m_pattern_set.m_background);

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

        Rect rect = new Rect(0, 0, m_pattern_set.m_bm_size, m_pattern_set.m_bm_size);
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

        drawing.fill_rect (rect, m_pattern_set.m_background);

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
     * Set the parameters back to their default values
     */
    public void reset ()
    {
        m_colour_map = new int [] { Color.BLACK, Color.BLACK, Color.BLACK, Color.RED };
        m_template =  new int [] { 2, 7, 1, 6 };
    }
    /**
     * Create the initial pattern from the template
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
    /**
     * Make a random pattern (controlled by the settings in m_random_set
     */
    public void MakeRandom() {

        if (! m_random_shapes.isEmpty()) {
            for (int i = 0 ; i < SIZE0 * SIZE0 ; ++i){

                int n = m_random.nextInt(m_random_shapes.size());
                m_template [i] = m_random_shapes.get(n);
            }
        }
        if (m_random_set.m_randomise_colours) {
            for (int i = 0; i < SIZE0 * SIZE0; ++i) {
                m_colour_map[i] = ColourHelpers.random_solid_colour();
            }
        }

        Initialise();

        if (m_random_set.m_randomise_code) {
            m_code = "";

            int steps = 1 + m_random.nextInt(6);
            for (int i = 0; i < steps; ++i) {
                Expand();
            }
        }
        else {
            ApplyCode();
        }
    }
    //-----------------------------------------------------------------------------------------------------
    void ApplyCode () {

        ApplyCode(m_code);
    }
    //-----------------------------------------------------------------------------------------------------
    public void ApplyCode (String code) {

        m_code = "";

        for (int i = 0; i < code.length(); i++) {
            ExpandFromCode(code.charAt(i));
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
    public String get_code() { return m_code; }
    //---------------------------------------------------------------------------------------------------------
    public void set_tile_colour (int idx, int colour)
    {
        m_colour_map [idx] = colour;
        Initialise();
        ApplyCode ();
    }
    /**
     * Sets the size of the bitmap drawn. This doesn't affect the size no the screen but does change the detail when the image is shared.
     */
    public void setSize ()
    {
        m_bmp = m_pattern_set.CreateBitmap();
    }
    /**
     * Return a list of fragments, Note the order is important, you must decode them in "Apply" in the same
     * order as you add them here.
     * @return
     */
    @Override
    public FragmentSet GetFragments() {

        FragmentSet ret = new FragmentSet();

        ret.AddFragment(TilePatternFragment.newInstance(m_pattern_set), "Basics", R.string.tile_params_caption);
        ret.AddFragment(TileRandomFragment.newInstance(m_random_set), "Randomiser", R.string.tile_random_caption);

        return ret;
    }
    /**
     * Text that appears at the top of the dialog
     * @return the string id of the caption
     */
    @Override
    public int GetTitleId() {
        return R.string.tile_settings_title;
    }
    /**
     * Used by the manage setting dialog to apply the result (see GetFragments)
     * @param result The result
     */
    public void Apply (FragmentSet result) {

        TilePatternSet ps = ((TilePatternFragment)result.GetFragment(0)).getResult ();
        TileRandomSet rs = ((TileRandomFragment)result.GetFragment(1)).getResult ();

        m_pattern_set.fromBundle(ps.toBundle());
        m_random_set.fromBundle(rs.toBundle());
        SetRandomShapes ();
        setSize();
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

        m_code = bundle.getString(KEY_CODE, "");

        m_pattern_set.fromBundle(bundle.getBundle(KEY_PATTERN));
        m_random_set.fromBundle(bundle.getBundle(KEY_RANDOM));

        SetRandomShapes ();
        Initialise();
        ApplyCode ();
    }

    public Bitmap bitmap ()
    {
        return m_bmp;
    }

    public Bundle toBundle () {

        Bundle b = new Bundle();

        b.putInt(PatternParameters.KEY_SETTINGS_TYPE, PatternParameters.SETTINGS_TYPE_TILES);

        b.putInt(KEY_T1, m_template[0]);
        b.putInt(KEY_T2, m_template[1]);
        b.putInt(KEY_T3, m_template[2]);
        b.putInt(KEY_T4, m_template[3]);

        b.putInt(KEY_C1, m_colour_map[0]);
        b.putInt(KEY_C2, m_colour_map[1]);
        b.putInt(KEY_C3, m_colour_map[2]);
        b.putInt(KEY_C4, m_colour_map[3]);

        b.putString(KEY_CODE, m_code);

        b.putBundle(KEY_PATTERN, m_pattern_set.toBundle());
        b.putBundle(KEY_RANDOM, m_random_set.toBundle());

        return b;
    }

    public String makeFileName () {

        return "tile_pattern_" + m_code + ".png";
    }
}
