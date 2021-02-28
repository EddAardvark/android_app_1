package com.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.activities.StarsPatternActivity;
import com.example.JWPatterns.R;
import com.misc.MessageBox;

import java.util.ArrayList;

public class GetInteger extends DialogFragment {

    public interface Result
    {
        abstract void SetInteger (int id, int value);
    }

    DialogInterface.OnClickListener m_listener = new GetInteger.ClickListener();
    NumberPicker m_picker;
    Result m_result;
    int m_id;

    public GetInteger (Result res, int id){

        m_id = id;
        m_result = res;
    }

    /**
     * Seems to be required for rotation handling, without this the app will crash with MethodNotFoundExtension
     * However, the dialog is now useless as the callback interface in m_result no longer exists. I can keep the instance
     * alive by storing the reference statically, but it no longer points to the active instance of the called,
     * which has been recreated during the rotation, so we just need to make the dialog disappear.
     */
    public GetInteger (){

    }

    public static GetInteger construct(Result res, int id, int n, int min, int max, int inc, String title, String message) {

        GetInteger frag = new GetInteger(res, id);
        Bundle args = new Bundle();
        args.putInt("current", n);
        args.putInt("min_val", min);
        args.putInt("max_val", max);
        args.putInt("value_inc", inc);
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    public static GetInteger construct(Result res, int id, int n, ArrayList<String> strings, String title, String message) {

        GetInteger frag = new GetInteger(res, id);
        Bundle args = new Bundle();
        args.putInt("current", n);
        args.putSerializable("strings", strings);
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        m_picker = new NumberPicker(getActivity());

        ArrayList<Object> objects = (ArrayList<Object>) args.getSerializable("strings");

        if (objects == null) {
            m_picker.setMinValue(args.getInt("min_val"));
            m_picker.setMaxValue(args.getInt("max_val"));
        }
        else{
            String [] strings = new String [objects.size()];

            for (int i = 0 ; i < strings.length ; ++i)
            {
                strings [i] = objects.get(i).toString();
            }
            m_picker.setMinValue(0);
            m_picker.setMaxValue(strings.length - 1);
            m_picker.setDisplayedValues(strings);
        }
        m_picker.setValue(args.getInt("current"));

        builder.setTitle(args.getString("title"));
        builder.setMessage(args.getString("message"));
        builder.setView(m_picker);
        builder.setPositiveButton(getString(R.string.accept), m_listener);
        builder.setNegativeButton(getString(R.string.cancel), m_listener);
        return builder.create();
    }

    public class ClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int id) {

            switch (id) {
                case Dialog.BUTTON_NEGATIVE:
                    break;
                case Dialog.BUTTON_POSITIVE:
                    if (m_result == null) {
                        MessageBox.showOK(getActivity(), "No owner", "This dialog no longer has an owner, possibly because a screen rotate has disconnected it", "OK");
                        return;
                    }
                    m_result.SetInteger(m_id, m_picker.getValue());
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    }
}

