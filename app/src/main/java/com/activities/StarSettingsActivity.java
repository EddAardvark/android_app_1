package com.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tutorialapp.R;
import com.google.android.material.tabs.TabLayout;
import com.patterns.StarAnimationSettings;
import com.patterns.StarBasicSettings;
import com.patterns.StarRandomiserSettings;

import android.os.Bundle;
import android.view.View;

public class StarSettingsActivity extends AppCompatActivity {

    TabLayout m_tabs;
    StarSettingsActivity.EventListener m_event_listener = new StarSettingsActivity.EventListener();
    StarBasicSettings m_pattern_fragment;
    StarAnimationSettings m_animation_fragment;
    StarRandomiserSettings m_randomiser_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_settings);

        m_tabs = (TabLayout) findViewById(R.id.star_settings_tabs);
        m_tabs.addOnTabSelectedListener((TabLayout.BaseOnTabSelectedListener) m_event_listener);

        m_pattern_fragment = StarBasicSettings.newInstance();
        m_animation_fragment = StarAnimationSettings.newInstance();
        m_randomiser_fragment = StarRandomiserSettings.newInstance();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, m_pattern_fragment);
        transaction.commit();

    }

    private void OnShowPattern() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, m_pattern_fragment);
        transaction.commit();
    }
    private void OnShowAnimation() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, m_animation_fragment);
        transaction.commit();
    }
    private void OnShowRandomisation() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, m_randomiser_fragment);
        transaction.commit();
    }


    public class EventListener implements View.OnClickListener, TabLayout.OnTabSelectedListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            String x = (String) tab.getText();
            int pos = tab.getPosition();

            switch (pos)
            {
                case 0:
                    OnShowPattern ();
                    break;
                case 1:
                    OnShowRandomisation();
                    break;
                case 2:
                    OnShowAnimation();
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }


    }
    }