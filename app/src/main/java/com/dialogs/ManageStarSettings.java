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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.activities.R;
import com.google.android.material.tabs.TabLayout;
import com.misc.MessageBox;

import java.util.Map;

public class ManageStarSettings extends DialogFragment {

    public interface Result {
        abstract void UpdateSettings(FragmentSet fragments);
    }
    DialogFragment m_Showing = null;

    ManageStarSettings.EventListener m_listener = new ManageStarSettings.EventListener();
    ManageStarSettings.Result m_result;
    TextView m_caption;
    PatternParameters m_params;
    FragmentSet m_fragments;
    int m_id;

    /**
     * Seems to be required for rotation handling, without this the app will crash with MethodNotFoundExtension
     * However, the dialog is now useless as the callback interface in m_result no longer exists. I can keep the instance
     * alive by storing the reference statically, but it no longer points to the active instance of the called,
     * which has been recreated during the rotation, so we just need to make the dialog disappear.
     */
    public ManageStarSettings (){
    }

    ManageStarSettings(ManageStarSettings.Result res, int id) {

        m_Showing = this;
        m_id = id;
        m_result = res;
    }

    public static ManageStarSettings construct(ManageStarSettings.Result res, int id, PatternParameters settings) {

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
            //m_params.fromBundle(current);

            m_params = PatternParameters.FromBundle(current);
            if (m_params != null) {
                m_fragments = m_params.GetFragments();
            }
        }
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

        ((TabLayout) dialog.findViewById(R.id.star_settings_tabs)).addOnTabSelectedListener((TabLayout.OnTabSelectedListener) m_listener);
        dialog.findViewById(R.id.ok_button).setOnClickListener(m_listener);
        dialog.findViewById(R.id.cancel_button).setOnClickListener(m_listener);

        m_caption = (TextView) dialog.findViewById(R.id.star_settings_caption);

        AddInitialFragment(0);
    }

    /**
     * Adds the initial fragment
     * @param idx The fragment index
     */
    void AddInitialFragment (int idx) {

        FragmentSet.FragmentHolder holder = m_fragments.GetHolder(idx);

        if (holder != null) {

            String caption = getString(holder.m_caption_id);
            m_caption.setText(caption);

            FragmentManager manager = getChildFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_container, (Fragment) holder.m_fragment);
            transaction.commit();
        }
    }

    /**
     * Replaces  the current fragment
     * @param idx The fragment index
     */
    void ReplaceFragment (int idx) {

        FragmentSet.FragmentHolder holder = m_fragments.GetHolder(idx);

        if (holder != null) {

            String caption = getString(holder.m_caption_id);
            m_caption.setText(caption);

            FragmentManager manager = getChildFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container, (Fragment) holder.m_fragment);
            transaction.commit();
        }
    }

    private void OnShowPattern() {
        ReplaceFragment (0);
    }
    private void OnShowAnimation() {
        ReplaceFragment (1);
    }
    private void OnShowRandomisation() {
        ReplaceFragment (2);
    }

    public class EventListener implements View.OnClickListener, TabLayout.OnTabSelectedListener {

        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.cancel_button) {
                dismiss();
            } else if (id == R.id.ok_button) {

                if (m_result == null) {
                    MessageBox.showOK(getActivity(), "No owner", "This dialog no longer has an owner, possibly because a screen rotate has disconnected it", "OK");
                    dismiss();
                    return;
                }

                boolean ok = m_fragments.onAccept();

                if (ok) {
                    m_result.UpdateSettings(m_fragments);
                    dismiss();
                }
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





