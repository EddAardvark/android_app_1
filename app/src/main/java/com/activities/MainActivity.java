package com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tutorialapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new ActivityMain());
    }

    private void goToSecondActivity() {

        EditText n1 = this.findViewById(R.id.seed_value_1);
        EditText n2 = this.findViewById(R.id.seed_value_2);
        EditText sf = this.findViewById(R.id.shrink_val);

        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("n1", n1.getText().toString());
        intent.putExtra("n2", n2.getText().toString());
        intent.putExtra("shrink", sf.getText().toString());
        intent.putExtra("n3", "3");
        startActivity(intent);
    }

    public class ActivityMain extends Activity implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button:
                    goToSecondActivity();
                    break;
            }
        }
    }
}

