package com.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.graphics.Bitmap;

import com.example.tutorialapp.R;
import com.patterns.StarParameters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StarsPatternActivity extends AppCompatActivity {

    static int WIDTH = 1024;
    static int HEIGHT = 1024;

    StarParameters m_params = new StarParameters (WIDTH, HEIGHT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

        Button button = (Button) findViewById(R.id.share_button);
        button.setOnClickListener(new StarsPatternActivity.ClickListener());

        ImageView img = findViewById(R.id.image);
        m_params.Draw (getResources(), img);
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

    void oldstuff ()
    {

        Intent intent = getIntent();
        String sn1 = intent.getStringExtra("n1");
        String sn2 = intent.getStringExtra("n2");
        String sn3 = intent.getStringExtra("n3");
        String ssf = intent.getStringExtra("shrink");

        if ("".equals(sn1) || "".equals(sn2) || "".equals(sn3) || "".equals(ssf))
            return;

        int n1 = Integer.parseInt(sn1);
        int n2 = Integer.parseInt(sn2);
        int n3 = Integer.parseInt(sn3);
        double sf = Double.parseDouble(ssf);

        TextView text = findViewById(R.id.detail);
        text.setText("Star: Points = " + sn1 + ", step = " + sn2);
    }

    public class ClickListener extends Activity implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.share_button:
                    share();
                    break;
            }
        }

    }

}