package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.graphics.Bitmap;

import com.dialogs.GetColour;
import com.dialogs.GetInteger;
import com.dialogs.ManageStarSettings;
import com.example.tutorialapp.R;
import com.misc.ColourHelpers;
import com.patterns.StarParameters;
import com.patterns.StarSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StarsPatternActivity extends AppCompatActivity implements GetInteger.Result, GetColour.Result, ManageStarSettings.Result {

    final int CB_N1 = 1;
    final int CB_N2 = 2;
    final int CB_N3 = 3;
    final int CB_ANGLE = 4;
    final int CB_SHRINK = 5;
    final int CB_BACKCOLOUR = 6;
    final int CB_LINECOLOUR1 = 7;
    final int CB_LINECOLOUR2 = 8;
    final int CB_SETTINGS = 9;

    StarSettings m_settings = new StarSettings ();
    StarParameters m_params = new StarParameters (m_settings.m_bm_size, m_settings.m_bm_size);

    ClickListener m_listener = new StarsPatternActivity.ClickListener();
    View m_layout_background;
    View m_layout_foreground1;
    View m_layout_foreground2;
    TextView m_layout_background_text;
    TextView m_layout_foreground1_text;
    TextView m_layout_foreground2_text;

    View m_top;

    int m_temp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            m_params.fromBundle (savedInstanceState);
        }

        setContentView(R.layout.stars_pattern_activity);

        findViewById(R.id.layout_n1).setOnClickListener(m_listener);
        findViewById(R.id.layout_n2).setOnClickListener(m_listener);
        findViewById(R.id.layout_n3).setOnClickListener(m_listener);
        findViewById(R.id.layout_angle).setOnClickListener(m_listener);
        findViewById(R.id.layout_share).setOnClickListener(m_listener);
        findViewById(R.id.layout_random).setOnClickListener(m_listener);
        findViewById(R.id.layout_shrink).setOnClickListener(m_listener);
        findViewById(R.id.layout_settings).setOnClickListener(m_listener);

        m_layout_background = findViewById(R.id.layout_back_colour);
        m_layout_foreground1 = findViewById(R.id.layout_fore_colour1);
        m_layout_foreground2 = findViewById(R.id.layout_fore_colour2);
        m_layout_background_text = (TextView)findViewById(R.id.text_backcolour);
        m_layout_foreground1_text = (TextView)findViewById(R.id.text_forecolour1);
        m_layout_foreground2_text = (TextView)findViewById(R.id.text_forecolour2);

        m_layout_background.setOnClickListener(m_listener);
        m_layout_foreground1.setOnClickListener(m_listener);
        m_layout_foreground2.setOnClickListener(m_listener);

        setColours();
        Draw ();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        m_params.fromBundle (savedInstanceState);
        Draw();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        m_params.toBundle (outState);
    }


    void setColours() {

        m_layout_background.setBackgroundColor(m_params.m_background);
        m_layout_foreground1.setBackgroundColor(m_params.m_first_line);
        m_layout_foreground2.setBackgroundColor(m_params.m_last_line);

        m_layout_background_text.setTextColor(ColourHelpers.GetContrastColour (m_params.m_background));
        m_layout_foreground1_text.setTextColor(ColourHelpers.GetContrastColour (m_params.m_first_line));
        m_layout_foreground2_text.setTextColor(ColourHelpers.GetContrastColour (m_params.m_last_line));
    }

    void Draw ()
    {
        ImageView img = findViewById(R.id.image);
        m_params.Draw (getResources(), img, m_settings.m_colouring_mode);
        showSettings ();
    }

    private void share ()
    {
        try {
            File cachePath = new File(this.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            m_params.bitmap ().compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        File imagePath = new File(this.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(this, "com.example.myapp.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }
    void showSettings ()
    {
        ((TextView)findViewById(R.id.text_n1)).setText(Integer.toString(m_params.m_n1));
        ((TextView)findViewById(R.id.text_n2)).setText(Integer.toString(m_params.m_n2));
        ((TextView)findViewById(R.id.text_n3)).setText(Integer.toString(m_params.m_n3));
        ((TextView)findViewById(R.id.text_da)).setText(Integer.toString(m_params.m_rotate_degrees) + '°');
        ((TextView)findViewById(R.id.text_sh)).setText(Integer.toString(m_params.m_shrink_pc) + '%');
    }

    /**
     * Allows you to change the number of points aroundthe circle
     */
    void onClickN1 () {
        GetInteger dialog = GetInteger.construct(this, CB_N1, m_params.m_n1, 3, 101, 1, getString(R.string.star_n1_title), getString(R.string.star_n1_description));
        dialog.show (getSupportFragmentManager(), "Hello");
    }

    /**
     * Allows you to change the number of points moved for each vector in the star. '1' draws the base polygon.
     */
    void onClickN2 () {
        int max_n2 = m_params.m_n1/2;
        int n2 = (m_params.m_n2 > max_n2) ? 1 : max_n2;
        GetInteger dialog = GetInteger.construct(this, CB_N2, n2, 1, max_n2, 1, getString(R.string.star_n2_title), getString(R.string.star_n2_description));
        dialog.show (getSupportFragmentManager(), "Hello");
    }
    /**
     * Allows you to change the number of points moved for each vector in the star. '1' draws the base polygon.
     */
    void onClickN3 () {
        GetInteger dialog = GetInteger.construct(this, CB_N3, m_params.m_n3, 1, 120, 1, getString(R.string.star_n3_title), getString(R.string.star_n3_description));
        dialog.show (getSupportFragmentManager(), "Hello");
    }
    /**
     * Allows you to change the number of points moved for each vector in the star. '1' draws the base polygon.
     */
    void onClickAngle () {
        GetInteger dialog = GetInteger.construct(this, CB_ANGLE, m_params.m_rotate_degrees, 0, 359, 1, getString(R.string.star_rotate_title), getString(R.string.star_angle_description));
        dialog.show (getSupportFragmentManager(), "Hello");
    }
    /**
     * Allows you to change the number of points moved for each vector in the star. '1' draws the base polygon.
     */
    void onClickShrink () {
        GetInteger dialog = GetInteger.construct(this, CB_SHRINK, m_params.m_shrink_pc, 0, 90, 1, getString(R.string.star_shrink_title), getString(R.string.star_shrink_description));
        dialog.show (getSupportFragmentManager(), "Hello");
    }
    /**
     * Change the pattern settings
     */
    void onClickSettings() {
        ManageStarSettings dialog = ManageStarSettings.construct(this, CB_SETTINGS, m_settings);
        dialog.show(getSupportFragmentManager(), "Hello");
    }
    /**
     * Allows you to change the background colour.
     */
    void onClickBackColour () {
        GetColour dialog = GetColour.construct(this, CB_BACKCOLOUR, m_params.m_background, getString(R.string.star_background_title), getString(R.string.star_background_description));
        dialog.show (getSupportFragmentManager(), "Hello");
    }
    /**
     * Allows you to change the main line colour.
     */
    void onClickLineColour1 () {
        GetColour dialog = GetColour.construct(this, CB_LINECOLOUR1, m_params.m_first_line, getString(R.string.star_first_colour_title), getString(R.string.star_first_colour_description));
        dialog.show (getSupportFragmentManager(), "Hello");
    }
    /**
     * Allows you to change the second line colour.
     */
    void onClickLineColour2 () {
        GetColour dialog = GetColour.construct(this, CB_LINECOLOUR2, m_params.m_last_line, getString(R.string.star_last_colour_title), getString(R.string.star_last_colour_description));
        dialog.show (getSupportFragmentManager(), "Hello");
    }
    @Override
    public void SetInteger(int id, int value) {
        switch (id)
        {
            case CB_N1: m_params.m_n1 = value; break;
            case CB_N2: m_params.m_n2 = value; break;
            case CB_N3: m_params.m_n3 = value; break;
            case CB_ANGLE: m_params.m_rotate_degrees = value; break;
            case CB_SHRINK: m_params.m_shrink_pc = value; break;
        }
        Draw ();
    }
    @Override
    public void SetColour(int id, int value) {
        switch (id)
        {
            case CB_BACKCOLOUR: m_params.m_background = value; break;
            case CB_LINECOLOUR1: m_params.m_first_line = value; break;
            case CB_LINECOLOUR2: m_params.m_last_line = value; break;
        }
        setColours();
        Draw ();
    }

    @Override
    public void UpdateStarSettings(int id, Bundle b) {

        m_settings.fromBundle(b);
        m_params.setSize(m_settings.m_bm_size, m_settings.m_bm_size);
        Draw();
    }
    public class ClickListener extends Activity implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout_share:
                    share();
                    break;
                case R.id.layout_n1:
                    onClickN1();
                    break;
                case R.id.layout_n2:
                    onClickN2();
                    break;
                case R.id.layout_n3:
                    onClickN3();
                    break;
                case R.id.layout_angle:
                    onClickAngle ();
                    break;
                case R.id.layout_shrink:
                    onClickShrink ();
                    break;
                case R.id.layout_random:
                    m_params.Randomise ();
                    Draw();
                    break;
                case R.id.layout_settings:
                    onClickSettings ();
                    break;
                case R.id.layout_back_colour:
                    onClickBackColour ();
                    break;
                case R.id.layout_fore_colour1:
                    onClickLineColour1 ();
                    break;
                case R.id.layout_fore_colour2:
                    onClickLineColour2 ();
                    break;
            }
        }
    }

}