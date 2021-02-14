package com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.tutorialapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView stars = (ImageView) findViewById(R.id.button_stars);

        stars.setOnClickListener(new ActivityMain());
    }

    private void showStars() {

        Intent intent = new Intent(this, StarsPatternActivity.class);
        startActivity(intent);
    }

    public class ActivityMain extends Activity implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            int id = view.getId();

            if (id == R.id.button_stars) {
                showStars();
            }
        }
    }
}


