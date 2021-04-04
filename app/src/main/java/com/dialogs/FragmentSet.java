package com.dialogs;

import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;

public class FragmentSet
{
    class FragmentHolder {

        TabFragment m_fragment;
        String m_tab_name;
        int m_caption_id;
        int m_id;

        /**
         * Construct the fragment holder
         *
         * @param id     The identifier
         * @param mtf    The fragment
         * @param tab    The name that used to select the tab
         * @param cap_id The id of the caption string that appears above the tab
         */
        FragmentHolder(int id, TabFragment mtf, String tab, int cap_id) {
            m_fragment = mtf;
            m_tab_name = tab;
            m_caption_id = cap_id;
            m_id = id;
        }

        boolean onAccept() {
            return m_fragment.onAccept();
        }

        public TabFragment GetFragment() {
            return m_fragment;
        }
    }

    Map<Integer, FragmentHolder> m_fragments = new HashMap<Integer, FragmentHolder>();

    /**
     * Construct the fragment holder
     *
     * @param id     The identifier
     * @param mtf    The fragment
     * @param tab    The name that used to select the tab
     * @param cap_id The id of the caption string that appears above the tab
     */
    public void AddFragment(int id, TabFragment mtf, String tab, int cap_id) {
        FragmentHolder fh = new FragmentHolder(id, mtf, tab, cap_id);

        m_fragments.put(id, fh);
    }

    /**
     * Returns the holder for the fragment matching the id
     * @param id the fragment id
     * @return The fragment (or null)
     */
    public FragmentHolder GetHolder(int id) {
        return m_fragments.get(id);
    }

    /**
     * Get the fragment for a given id
     * @param id The id
     * @return The fragment (may be null)
     */
    public TabFragment GetFragment (int id){
        FragmentHolder fh = GetHolder(id);
        return (fh == null) ? null : fh.GetFragment();
    }

    /**
     * Calls onAccept for all the fragments
     * @return true if all the fragments accept the changes, false if any refuse.
     */
    boolean onAccept() {

        boolean ret = true;

        for (FragmentHolder f : m_fragments.values()) {
            if (! f.onAccept()){
                ret = false;
            }
        }

        return ret;
    }
}
