package com.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tutorialapp.R;
import com.google.android.material.tabs.TabLayout;
import com.patterns.AnimateSet;
import com.patterns.PatternSet;
import com.patterns.RandomSet;
import com.patterns.StarAnimationFragment;
import com.patterns.StarPatternFragment;
import com.patterns.StarRandomiserFragment;
import com.patterns.StarSettings;

public class ManageStarSettings extends DialogFragment {

    public interface Result {
        abstract void UpdateStarSettings(PatternSet ps, RandomSet rs, AnimateSet as);
    }

    ManageStarSettings.EventListener m_listener = new ManageStarSettings.EventListener();
    ManageStarSettings.Result m_result;
    StarSettings m_settings = new StarSettings();
    StarPatternFragment m_pattern_fragment;
    StarAnimationFragment m_animation_fragment;
    StarRandomiserFragment m_randomiser_fragment;
    TextView m_caption;

    int m_id;

    ManageStarSettings(ManageStarSettings.Result res, int id) {

        m_id = id;
        m_result = res;
    }

    public static ManageStarSettings construct(ManageStarSettings.Result res, int id, StarSettings settings) {

        ManageStarSettings frag = new ManageStarSettings(res, id);
        Bundle args = new Bundle();
        args.putBundle("current", settings.toBundle());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {

            Bundle current = args.getBundle("current");
            m_settings.fromBundle(current);
        }

        m_pattern_fragment = StarPatternFragment.newInstance(m_settings.m_pattern);
        m_animation_fragment = StarAnimationFragment.newInstance(m_settings.m_animate);
        m_randomiser_fragment = StarRandomiserFragment.newInstance(m_settings.m_random);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.star_settings_dialog, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = this.getDialog();

        ((TabLayout)dialog.findViewById(R.id.star_settings_tabs)).addOnTabSelectedListener((TabLayout.BaseOnTabSelectedListener) m_listener);
        dialog.findViewById(R.id.ok_button).setOnClickListener(m_listener);
        dialog.findViewById(R.id.cancel_button).setOnClickListener(m_listener);

        m_caption = (TextView) dialog.findViewById(R.id.star_settings_caption);
        String caption = getString(R.string.star_params_caption);
        m_caption.setText(caption);

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, m_pattern_fragment);
        transaction.commit();
    }

    private void OnShowPattern() {

        String caption = getString(R.string.star_params_caption);
        m_caption.setText(caption);

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, m_pattern_fragment);
        transaction.commit();
    }
    private void OnShowAnimation() {

        String caption = getString(R.string.star_animation_caption);
        m_caption.setText(caption);

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, m_animation_fragment);
        transaction.commit();
    }
    private void OnShowRandomisation() {

        String caption = getString(R.string.star_randomiser_caption);
        m_caption.setText(caption);

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, m_randomiser_fragment);
        transaction.commit();
    }

    public class EventListener implements View.OnClickListener, TabLayout.OnTabSelectedListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cancel_button:
                    dismiss ();
                    break;
                case R.id.ok_button:
                    if (m_animation_fragment.onAccept () && m_pattern_fragment.onAccept() && m_randomiser_fragment.onAccept ()) {
                        dismiss ();

                        m_result.UpdateStarSettings(m_pattern_fragment.getResult(), m_randomiser_fragment.getResult(), m_animation_fragment.getResult());
                    }
                    break;
            }
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {

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





