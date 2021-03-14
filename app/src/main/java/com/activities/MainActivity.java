package com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.JWPatterns.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView stars = (ImageView) findViewById(R.id.button_stars);
        ImageView tiles = (ImageView) findViewById(R.id.button_tiles);

        stars.setOnClickListener(new ActivityMain());
        tiles.setOnClickListener(new ActivityMain());
    }

    /**
     * Launch the tiles activity
     */
    private void showTiles() {

        Intent intent = new Intent(this, TilePatternsActivity.class);
        startActivity(intent);
    }

    /**
     * Launch the stars activity
     */
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
            if (id == R.id.button_tiles) {
                showTiles();
            }
        }
    }
}


