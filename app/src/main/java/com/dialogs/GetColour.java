package com.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.graphics.Color;

import androidx.annotation.NonNull;
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
    NumberPicker m_RedPicker;
    NumberPicker m_GreenPicker;
    NumberPicker m_BluePicker;

    GetColour(GetColour.Result res, int id) {
        m_id = id;
        m_result = res;
    }


    public static GetColour construct(GetColour.Result res, int id, int colour, String title, String message) {

        GetColour frag = new GetColour(res, id);
        Bundle args = new Bundle();
        args.putInt("current", colour);
        args.putString("message", message);
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflator = requireActivity().getLayoutInflater();
        builder.setView(inflator.inflate(R.layout.colour_chooser, null));
        builder.setTitle(args.getString("title"));
        builder.setMessage(args.getString("message"));
        builder.setPositiveButton("Accept", m_listener);
        builder.setNegativeButton("Cancel", m_listener);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = this.getDialog();

        m_RedPicker = (NumberPicker) dialog.findViewById(R.id.set_red);
        m_GreenPicker = (NumberPicker) dialog.findViewById(R.id.set_green);
        m_BluePicker = (NumberPicker) dialog.findViewById(R.id.set_blue);

        m_RedPicker.setMinValue(0);
        m_RedPicker.setMaxValue(255);
        m_RedPicker.setValue(0);

        m_GreenPicker.setMinValue(0);
        m_GreenPicker.setMaxValue(255);
        m_GreenPicker.setValue(0);

        m_BluePicker.setMinValue(0);
        m_BluePicker.setMaxValue(255);
        m_BluePicker.setValue(0);
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

    public class ShowListener implements DialogInterface.OnShowListener {

        @Override
        public void onShow(DialogInterface dialog) {
        }
    }
}
