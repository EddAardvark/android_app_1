package com.dialogs;

import android.os.Bundle;

import com.patterns.StarParameters;
import com.patterns.TileParameters;

import java.util.Map;

public abstract class PatternParameters {

    public final static String KEY_SETTINGS_TYPE = "type";
    public final static int SETTINGS_TYPE_STARS = 1;
    public final static int SETTINGS_TYPE_TILES = 2;

    public abstract FragmentSet GetFragments ();
    public abstract Bundle toBundle();

    /**
     * Construct an instance of the appropriate parrameters class. The KEY_SETTINGS_TYPE attribute of the
     * bundle defines the type
     * @param b The bundle
     * @return An instance or null
     */
    public static PatternParameters FromBundle (Bundle b)
    {
        int type = b.getInt(KEY_SETTINGS_TYPE, -1);

        if (type == SETTINGS_TYPE_STARS)
        {
            StarParameters params = new StarParameters();
            params.fromBundle(b);
            return params;
        }
        if (type == SETTINGS_TYPE_TILES)
        {
            TileParameters params = new TileParameters();
            params.fromBundle(b);
            return params;
        }

        return null;
    }


}
