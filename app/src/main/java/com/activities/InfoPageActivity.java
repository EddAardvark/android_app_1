package com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.JWPatterns.R;

public class InfoPageActivity extends AppCompatActivity {

    int m_TitleStringId = 0;

    InfoPageActivity.EventListener m_listener = new InfoPageActivity.EventListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info_page);
        // Previous state

        if (savedInstanceState != null) {
        }
        // Intent

        Bundle b = getIntent().getExtras();

        if(b != null){

            m_TitleStringId = b.getInt("title", 0);
        }

        if (m_TitleStringId != 0) {
            TextView title = (TextView) findViewById(R.id.title);
            title.setText(m_TitleStringId);
        }

        findViewById(R.id.email_me).setOnClickListener(m_listener);
        findViewById(R.id.view_stars).setOnClickListener(m_listener);
        findViewById(R.id.home_page).setOnClickListener(m_listener);
    }

    void openURL(int urlid) {

        try {
            String url = getString (urlid);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
        catch (ActivityNotFoundException ignored) {

        }
    }

    /**
     * Handle clicks on the controls
     */
    public class EventListener extends Activity implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.email_me){
                openURL(R.string.email_address);
            }
            else if (id == R.id.view_stars){
                openURL(R.string.stars_page_url);
            }
            else if (id == R.id.home_page){
                openURL(R.string.home_page_url);
            }
        }
    }
}