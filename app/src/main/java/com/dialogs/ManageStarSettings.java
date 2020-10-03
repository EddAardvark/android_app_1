package com.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tutorialapp.R;
import com.google.android.material.tabs.TabLayout;
import com.patterns.StarAnimationSettings;
import com.patterns.StarBasicSettings;
import com.patterns.StarRandomiserSettings;
import com.patterns.StarSettings;

public class ManageStarSettings extends DialogFragment {

    public interface Result {
        abstract void UpdateStarSettings(int id, StarSettings value);
    }

    ManageStarSettings.EventListener m_listener = new ManageStarSettings.EventListener();
    ManageStarSettings.Result m_result;
    StarSettings m_settings = new StarSettings();
    StarBasicSettings m_pattern_fragment;
    StarAnimationSettings m_animation_fragment;
    StarRandomiserSettings m_randomiser_fragment;

    int m_id;

    ManageStarSettings(ManageStarSettings.Result res, int id) {

        m_id = id;
        m_result = res;

        m_pattern_fragment = StarBasicSettings.newInstance();
        m_animation_fragment = StarAnimationSettings.newInstance();
        m_randomiser_fragment = StarRandomiserSettings.newInstance();
    }

    public static ManageStarSettings construct(ManageStarSettings.Result res, int id, StarSettings settings, String title, String message) {

        ManageStarSettings frag = new ManageStarSettings(res, id);
        Bundle args = new Bundle();
        args.putBundle("current", settings.toBundle());
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }
/*
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle b = args.getBundle("current");

        m_settings.fromBundle(b);

        LayoutInflater inflator = requireActivity().getLayoutInflater();

        builder.setView(inflator.inflate(R.layout.star_settings_top, null));
        builder.setTitle(args.getString("title"));
        builder.setMessage(args.getString("message"));

        builder.setPositiveButton("Accept", m_listener);
        builder.setNegativeButton("Cancel", m_listener);
        return builder.create();
    }
*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.star_settings_dialog, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = this.getDialog();

        TabLayout tabs = dialog.findViewById(R.id.star_settings_tabs);

        tabs.addOnTabSelectedListener((TabLayout.BaseOnTabSelectedListener) m_listener);

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, m_pattern_fragment);
        transaction.commit();
    }

    private void OnShowPattern() {

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, m_pattern_fragment);
        transaction.commit();
    }
    private void OnShowAnimation() {

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, m_animation_fragment);
        transaction.commit();
    }
    private void OnShowRandomisation() {

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, m_randomiser_fragment);
        transaction.commit();
    }

    public class EventListener implements DialogInterface.OnClickListener, TabLayout.OnTabSelectedListener {

        @Override
        public void onClick(DialogInterface dialog, int id) {
            switch (id) {
                case Dialog.BUTTON_NEGATIVE:
                    break;
                case Dialog.BUTTON_POSITIVE:
                    m_result.UpdateStarSettings(m_id, m_settings);
                    break;
                case Dialog.BUTTON_NEUTRAL:
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





