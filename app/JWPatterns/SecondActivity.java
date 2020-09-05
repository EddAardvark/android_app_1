package com.example.JWPatterns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    static int WIDTH = 1024;
    static int HEIGHT = 1024;

    Bitmap m_bmp = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.RGB_565);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_second);



        Button button = (Button) findViewById(R.id.share_button);
        button.setOnClickListener(new SecondActivity.ClickListener());


        EditText n1_edit = this.findViewById(R.id.seed_value_1);
        EditText n2_edit = this.findViewById(R.id.seed_value_2);
        EditText sf_edit = this.findViewById(R.id.shrink_val);

        String sn1 = n1_edit.getText().toString();
        String sn2 = n2_edit.getText().toString();
        String ssf = sf_edit.getText().toString();
        String sn3 = "3";

        if ("".equals(sn1) || "".equals(sn2) || "".equals(sn3) || "".equals(ssf))
            return;

        int n1 = Integer.parseInt(sn1);
        int n2 = Integer.parseInt(sn2);
        int n3 = Integer.parseInt(sn3);
        double sf = Double.parseDouble(ssf);

        TextView text = findViewById(R.id.detail);
        text.setText("Star: Points = " + sn1 + ", step = " + sn2);

        ImageView img = findViewById(R.id.image);

        Draw (img, n1, n2, n3, sf);
    }

    private void Draw (ImageView img, int n1, int n2, int n3, double sf)
    {
        if (n3 < 1 || n2 == 0 || n1 < 1 || sf <= 0)
            return;

        Rect rect = new Rect(0, 0, WIDTH, HEIGHT);
        Canvas canvas = new Canvas(m_bmp);
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(rect, paint);
        paint.setColor(Color.BLACK);

        double xc = WIDTH * 0.5;
        double yc = HEIGHT * 0.5;
        double r = HEIGHT * 0.48;
        double theta = Math.PI * 2 / n1;
        double x0 = xc + r;
        double y0 = yc + 0;
        int n = 0;

        for (int i = 0 ; i <= n1 ; i++)
        {
            n += n2;
            double x1 = xc + r * Math.cos (n * theta);
            double y1 = yc + r * Math.sin (n * theta);
            canvas.drawLine((float) x0, (float) y0, (float) x1, (float) y1, paint);
            x0 = x1;
            y0 = y1;
        }

// Attach the canvas to the ImageView
        img.setImageDrawable(new BitmapDrawable(getResources(), m_bmp));
    }

    private void share ()
    {
        try {
            File cachePath = new File(this.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            m_bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
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

    void getParameters ()
    {

        /*
        */
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