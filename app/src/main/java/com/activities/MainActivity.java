package com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.tutorialapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton button = (ImageButton) findViewById(R.id.button_stars);

        button.setOnClickListener(new ActivityMain());
    }

    private void showStars() {

        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    public class ActivityMain extends Activity implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_stars:
                    showStars();
                    break;
            }
        }
    }
}

