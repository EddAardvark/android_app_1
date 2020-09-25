package com.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tutorialapp.R;
import com.google.android.material.tabs.TabLayout;
import com.patterns.StarSettings;

public class ManageStarSettings extends DialogFragment {

    public interface Result {
        abstract void UpdateStarSettings(int id, StarSettings value);
    }

    DialogInterface.OnClickListener m_listener = new ManageStarSettings.ClickListener();
    ManageStarSettings.Result m_result;
    StarSettings m_settings = new StarSettings();
    int m_id;

    ManageStarSettings(ManageStarSettings.Result res, int id) {

        m_id = id;
        m_result = res;
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle b = args.getBundle("current");
        TabLayout tabs = new TabLayout(getActivity());

        TabLayout.Tab tab1 = tabs.newTab();
        TabLayout.Tab tab2 = tabs.newTab();
        TabLayout.Tab tab3 = tabs.newTab();
        tabs.addTab(tab1, 0);
        tabs.addTab(tab2, 1);
        tabs.addTab(tab3, 2);
        tab1.setText(getString (R.string.pattern));
        tab2.setText(getString (R.string.randomiser));
        tab3.setText(getString (R.string.animator));

        m_settings.fromBundle(b);

        builder.setTitle(args.getString("title"));
        builder.setMessage(args.getString("message"));


        builder.setView(tabs);
        builder.setPositiveButton("Accept", m_listener);
        builder.setNegativeButton("Cancel", m_listener);
        return builder.create();
    }

    public class ClickListener implements DialogInterface.OnClickListener {

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

    }
}




