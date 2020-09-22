package com.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tutorialapp.R;

public class GetColour extends DialogFragment {


    public interface Result {
        abstract void SetColour(int id, int value);
    }

    DialogInterface.OnClickListener m_listener = new GetColour.ClickListener();
    GetColour.Result m_result;
    int m_id;

    GetColour(GetColour.Result res, int id) {
        m_id = id;
        m_result = res;
    }


    public static GetColour construct(GetColour.Result res, int id, int colour, String message) {

        GetColour frag = new GetColour(res, id);
        Bundle args = new Bundle();
        args.putInt("current", colour);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.get_integer_prompt));
        builder.setMessage(args.getString("message", "no message"));
        //builder.setView(m_picker);
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
                    m_result.SetColour(m_id, Color.RED);
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    }
}
