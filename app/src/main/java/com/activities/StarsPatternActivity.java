package com.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.graphics.Bitmap;

import com.dialogs.GetInteger;
import com.example.tutorialapp.R;
import com.patterns.StarParameters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StarsPatternActivity extends AppCompatActivity implements GetInteger.Result{

    final int WIDTH = 1024;
    final int HEIGHT = 1024;

    final int CB_N1 = 1;
    final int CB_N2 = 2;

    StarParameters m_params = new StarParameters (WIDTH, HEIGHT);
    ClickListener m_listener = new StarsPatternActivity.ClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stars_pattern_activity);

        ConstraintLayout l1 = (ConstraintLayout) findViewById(R.id.layout_n1);
        ConstraintLayout l2 = (ConstraintLayout) findViewById(R.id.layout_n2);
        ConstraintLayout ls = (ConstraintLayout) findViewById(R.id.layout_share);
        ConstraintLayout rn = (ConstraintLayout) findViewById(R.id.layout_random);

        l1.setOnClickListener(m_listener);
        l2.setOnClickListener(m_listener);
        ls.setOnClickListener(m_listener);
        rn.setOnClickListener(m_listener);

        Draw ();
    }

    void Draw ()
    {
        ImageView img = findViewById(R.id.image);
        m_params.Draw (getResources(), img);
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
        TextView n1_view = (TextView)  findViewById(R.id.text_n1);
        TextView n2_view = (TextView)  findViewById(R.id.text_n2);
        n1_view.setText(Integer.toString(m_params.m_n1));
        n2_view.setText(Integer.toString(m_params.m_n2));
    }

    /**
     * Allows you to change the number of points aroundthe circle
     */
    void onClickN1 () {
        GetInteger dialog = GetInteger.construct(this, CB_N1, m_params.m_n1, 3, 101, 1, getString(R.string.star_n1_description));
        dialog.show (getSupportFragmentManager(), "Hello");
    }

    /**
     * Allows you to change the number of points moved for each vector in the star. '1' draws the base polygon.
     */
    void onClickN2 () {
        int max_n2 = m_params.m_n1/2;
        int n2 = (m_params.m_n2 > max_n2) ? 1 : max_n2;
        GetInteger dialog = GetInteger.construct(this, CB_N2, n2, 1, max_n2, 1, getString(R.string.star_n2_description));
        dialog.show (getSupportFragmentManager(), "Hello");
    }
    @Override
    public void SetInteger(int id, int value) {
        switch (id)
        {
            case CB_N1: m_params.m_n1 = value; break;
            case CB_N2: m_params.m_n2 = value; break;
        }
        Draw ();
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
                case R.id.layout_random:
                    m_params.Randomise ();
                    Draw();
                    break;
            }
        }

    }

}