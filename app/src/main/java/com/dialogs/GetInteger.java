package com.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class GetInteger extends DialogFragment {

    public interface Result
    {
        abstract void SetInteger (int id, int value);
    }

    DialogInterface.OnClickListener m_listener = new GetInteger.ClickListener();
    NumberPicker m_picker;
    Result m_result;
    int m_id;

    GetInteger (Result res, int id){

        m_id = id;
        m_result = res;
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

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        m_picker = new NumberPicker(getActivity());
        m_picker.setMinValue(args.getInt("min_val"));
        m_picker.setMaxValue(args.getInt("max_val"));
        m_picker.setValue(args.getInt("current"));
        builder.setTitle(args.getString("title"));
        builder.setMessage(args.getString("message"));
        builder.setView(m_picker);
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
                    m_result.SetInteger(m_id, m_picker.getValue());
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }

    }
}

